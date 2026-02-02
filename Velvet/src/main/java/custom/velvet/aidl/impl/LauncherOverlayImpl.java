package custom.server.aidl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.util.HorizonScrollSimulator;
import custom.common.util.LogUtil;
import custom.server.view.MinusOneLayout;
import custom.server.window.OverlayWindowCallback;
import custom.server.window.OverlayWindowController;
import custom.server.window.OverlayWindowWrapper;

public class LauncherOverlayImpl extends DefaultLauncherOverlayImpl {
    private final Context context;
    private final int screenWidth;
    private final int SERVER_STATUS_OK = 3;
    private ILauncherOverlayCallback callback;
    private Handler mainHandler;
    private OverlayWindowController overlayWindowController;
    private MinusOneLayout windowContentView;
    private OverlayWindowWrapper windowWrapper;
    private HorizonScrollSimulator scrollSimulator = new HorizonScrollSimulator();

    public LauncherOverlayImpl(Context context) {
        this.context = context;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void startScroll() throws RemoteException {
        super.startScroll();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scrollSimulator.simulateDown();
            }
        });
    }

    @Override
    public void onScroll(float progress) throws RemoteException {
        super.onScroll(progress);
        if (progress <= 0) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Math.abs(progress - 0.0) > 0.05f) {
                    overlayWindowController.setVisible(true);
                }
                float currentX = progress * screenWidth;
                scrollSimulator.simulateMove(currentX);
            }
        });
    }

    @Override
    public void endScroll() throws RemoteException {
        super.endScroll();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scrollSimulator.simulateUp();
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        if (mainHandler == null || runnable == null) {
            return;
        }
        mainHandler.post(runnable);
    }

    @Override
    public void windowAttached2(Bundle bundle, ILauncherOverlayCallback callback) {
        this.callback = callback;
        WindowManager.LayoutParams layoutParams = bundle.getParcelable("layout_params");
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                windowWrapper = new OverlayWindowWrapper(context, layoutParams.token);
                Window window = windowWrapper.getWindow();
                windowContentView = createContentView(context);
                windowWrapper.setContentView(windowContentView);

                scrollSimulator.setTargetView(window.getDecorView());
                overlayWindowController = new OverlayWindowController(window);
                overlayWindowController.setVisible(false);
                overlayWindowController.setTouchable(false);
                sendServiceStatus(SERVER_STATUS_OK);
                bindEvent();
            }
        });
    }

    private void bindEvent() {
        windowContentView.setOnScrollListener(new MinusOneLayout.OnScrollListener() {
            @Override
            public void onScrolling(float progress) {
                if (progress >= 0) {
                    View rightView = windowContentView.getRightView();
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
                overlayWindowController.setTouchable(true);
            }

            @Override
            public void onRightFullyShown() {
                LogUtil.debug("minusOneLayout onRightFullyShown");
                overlayWindowController.setVisible(false);
                overlayWindowController.setTouchable(false);
            }
        });
        Window.Callback srcCallback = windowWrapper.getWindow().getCallback();
        windowWrapper.getWindow().setCallback(new OverlayWindowCallback(srcCallback) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                if (windowContentView.isLeftFullShown()) {
                    windowContentView.scrollToRight();
                }
            }
        });
    }

    private void sendServiceStatus(int status) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("service_status", status);
            callback.onServiceStatus(bundle);
        } catch (RemoteException e) {
            LogUtil.error("notifyServerStatus error", e);
        }
    }

    private MinusOneLayout createContentView(Context context) {
        MinusOneLayout minusOneLayout = new MinusOneLayout(context);
        TextView textView = new TextView(context);
        textView.setText("负一屏广告区域");
        textView.setTextSize(30);
        return minusOneLayout;
    }
}
