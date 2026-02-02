package impl.aidl;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.googlequicksearchbox.R;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.LogUtil;
import custom.WindowStateManager;
import impl.view.MinusOneLayout;


public class LauncherOverlayImpl extends DefaultLauncherOverlayImpl {
    private final int screenWidth;
    private Context context;
    private final int SERVER_STATUS_OK = 3;

    private ILauncherOverlayCallback callback;
    private View ovlayWindowRootView;
    private Handler mainHandler;
    private Window window;

    private WindowStateManager windowStateManager;
    private MotionEvent mLastMoveEvent;
    private long mDownTime;
    private float mLastX;
    private MinusOneLayout minusOneLayout;

    public LauncherOverlayImpl(Context context) {
//        this.serverVersion = serverVersion;
//        this.clientVersion = clientVersion;
        this.context = context;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void startScroll() throws RemoteException {
        super.startScroll();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                View decorView = window.getDecorView();
                if (decorView == null) return;

                long now = SystemClock.uptimeMillis();
                mDownTime = now;

                // DOWN 事件：x 坐标从 0 开始（或屏幕中心，根据需求选）
                float startX = 0f;  // 或 decorView.getWidth() / 2f
                float y = 0f;       // y 始终为 0

                MotionEvent down = MotionEvent.obtain(
                        now, now, MotionEvent.ACTION_DOWN,
                        startX, y, 0
                );
                decorView.dispatchTouchEvent(down);
                down.recycle();

                // 初始化最后 x 坐标
                mLastX = startX;
            }
        });
    }

    @Override
    public void onScroll(float progress) throws RemoteException {
        super.onScroll(progress);
        if (progress <= 0) {
            return;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (Math.abs(progress - 0.0) > 0.05f) {
                    windowStateManager.setVisible(true);
                }
                if (mDownTime == -1) return; // 防止没有 DOWN 就 MOVE

                View decorView = window.getDecorView();
                if (decorView == null) return;

                long now = SystemClock.uptimeMillis();

                // x 坐标 = progress × screenWidth
                // progress 通常 0~1，这里直接映射到 0~screenWidth
                float currentX = progress * screenWidth;
                float y = 0f;  // y 始终为 0

                // 发送 ACTION_MOVE
                MotionEvent move = MotionEvent.obtain(
                        mDownTime,          // 复用 DOWN 时间戳
                        now,
                        MotionEvent.ACTION_MOVE,
                        currentX, y, 0
                );

                decorView.dispatchTouchEvent(move);

                // 回收上一次 MOVE
                if (mLastMoveEvent != null) {
                    mLastMoveEvent.recycle();
                }
                mLastMoveEvent = move;

                // 更新最后 x，用于 UP 事件
                mLastX = currentX;
            }
        });
    }

    @Override
    public void endScroll() throws RemoteException {
        super.endScroll();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mDownTime == -1) return;

                View decorView = window.getDecorView();
                if (decorView == null) return;

                long now = SystemClock.uptimeMillis();

                // UP 事件：使用最后一次 MOVE 的 x 坐标
                float finalX = mLastX;
                float y = 0f;

                MotionEvent up = MotionEvent.obtain(
                        mDownTime,
                        now,
                        MotionEvent.ACTION_UP,
                        finalX, y, 0
                );

                decorView.dispatchTouchEvent(up);
                up.recycle();

                // 清理状态
                if (mLastMoveEvent != null) {
                    mLastMoveEvent.recycle();
                    mLastMoveEvent = null;
                }
                mDownTime = -1;
                mLastX = 0f;
            }
        });
    }

    @Override
    public void windowAttached2(Bundle bundle, ILauncherOverlayCallback callback) {
        this.callback = callback;
        WindowManager.LayoutParams layoutParams = bundle.getParcelable("layout_params");
        mainHandler.post(new Runnable() {

            @Override
            public void run() {
                Dialog dialog = new Dialog(context, R.style.ThemeOverlay_MinusOne_Dialog);
                window = dialog.getWindow();
                if (window != null) {
                    window.setStatusBarColor(0);
                    window.setNavigationBarColor(0);
                    window.addFlags(0x80000000);
                    window.setWindowManager(null, layoutParams.token, "Acetone", true);
                    layoutParams.type = WindowManager.LayoutParams.TYPE_DRAWN_APPLICATION;
                    layoutParams.width = -1;
                    layoutParams.height = -1;
                    layoutParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    layoutParams.dimAmount = 0.0f;
                    layoutParams.gravity = Gravity.LEFT;
                    window.setAttributes(layoutParams);
                    window.clearFlags(0x100000);
                    minusOneLayout = createRootView(context);
                    window.setContentView(minusOneLayout, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    window.getWindowManager().addView(window.getDecorView(), window.getAttributes());
                    if (Build.VERSION.SDK_INT >= 29) {
                        window.setNavigationBarContrastEnforced(false);
                        window.setStatusBarContrastEnforced(false);
                    }
                    if (Build.VERSION.SDK_INT >= 30) {
                        window.setDecorFitsSystemWindows(false);
                    }
                    // 最后再通知
                    windowStateManager = new WindowStateManager(window);
                    windowStateManager.setVisible(false);
                    windowStateManager.setTouchable(false);
                    notifyServerStatus(SERVER_STATUS_OK);
                    bindEvent();
                }
            }
        });
    }

    private void bindEvent() {
        minusOneLayout.setOnScrollListener(new MinusOneLayout.OnScrollListener() {
            @Override
            public void onScrolling(float progress) {
                if (progress >= 0) {
                    View rightView = minusOneLayout.getRightView();
                    if (rightView != null && rightView.getParent() != null) {
                        ViewGroup parent = (ViewGroup) rightView.getParent();
                        float alpha = 1 - progress;
                        parent.setAlpha(alpha);
                    }
                }
            }

            @Override
            public void onLeftFullyShown() {
                LogUtil.debug("minusOneLayout onLeftFullyShown");
                windowStateManager.setTouchable(true);
            }

            @Override
            public void onRightFullyShown() {
                LogUtil.debug("minusOneLayout onRightFullyShown");
                windowStateManager.setVisible(false);
                windowStateManager.setTouchable(false);
            }
        });
    }

    private void notifyServerStatus(int status) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("service_status", status);
            callback.onServiceStatus(bundle);
        } catch (RemoteException e) {
            LogUtil.error("notifyServerStatus error", e);
        }
    }

    private MinusOneLayout createRootView(Context context) {
        MinusOneLayout minusOneLayout = new MinusOneLayout(context);
        TextView textView = new TextView(context);
        textView.setText("负一屏广告区域");
        textView.setTextSize(30);
//        minusOneLayout.setCustomLeftView(textView);
        return minusOneLayout;
    }
}
