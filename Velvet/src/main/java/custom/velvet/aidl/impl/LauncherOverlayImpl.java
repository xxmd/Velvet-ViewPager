package custom.velvet.aidl.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.common.util.LogUtil;
import custom.velvet.entity.converter.HorizonScrollToTouchConverter;
import custom.velvet.entity.enums.ServiceStatus;
import custom.velvet.view.MinusOneLayout;
import custom.velvet.window.OverlayWindowCallback;
import custom.velvet.window.OverlayWindowController;
import custom.velvet.window.OverlayWindowWrapper;

public class LauncherOverlayImpl extends DefaultLauncherOverlayImpl {
    private final Context context;
    private final int screenWidth;
    private ILauncherOverlayCallback callback;
    private Handler mainHandler;
    private OverlayWindowController overlayWindowController;
    private MinusOneLayout windowContentView;
    private OverlayWindowWrapper windowWrapper;
    private HorizonScrollToTouchConverter scrollTouchConverter = new HorizonScrollToTouchConverter();

    public LauncherOverlayImpl(Context context) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void startScroll() throws RemoteException {
        super.startScroll();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MotionEvent downEvent = scrollTouchConverter.onScrollStart();
                dispatchTouchEvent(downEvent);
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
                MotionEvent moveEvent = scrollTouchConverter.onScrolling(progress, screenWidth);
                dispatchTouchEvent(moveEvent);
            }
        });
    }

    @Override
    public void endScroll() throws RemoteException {
        super.endScroll();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MotionEvent upOrCancelEvent = scrollTouchConverter.onScrollEnd();
                dispatchTouchEvent(upOrCancelEvent);
            }
        });
    }

    private void dispatchTouchEvent(MotionEvent event) {
        View decorView = windowWrapper.getWindow().getDecorView();
        if (decorView != null) {
            decorView.dispatchTouchEvent(event);
        }
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
                overlayWindowController = new OverlayWindowController(window);
                overlayWindowController.setVisible(false);
                overlayWindowController.setTouchable(false);
                notifyServiceStatus(ServiceStatus.OK);
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

    private void notifyServiceStatus(ServiceStatus status) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("service_status", status.getValue());
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
