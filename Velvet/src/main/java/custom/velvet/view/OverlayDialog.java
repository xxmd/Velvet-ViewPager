package custom.velvet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import custom.common.util.LogUtil;

public class OverlayDialog extends Dialog {
    private IBinder token;

    public OverlayDialog(@NonNull Context context, IBinder token) {
        super(context, android.R.style.Theme_Light);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        LogUtil.debug("FullScreenDialog window: " + window);
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
////                    window.addFlags(0x80000000);
            window.setWindowManager(null, token, "Acetone", true);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_DRAWN_APPLICATION;
            layoutParams.width = -1;
            layoutParams.height = -1;
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            layoutParams.dimAmount = 0.0f;
            layoutParams.gravity = Gravity.LEFT;
            window.setAttributes(layoutParams);
            if (Build.VERSION.SDK_INT >= 29) {
                window.setNavigationBarContrastEnforced(false);
                window.setStatusBarContrastEnforced(false);
            }
            if (Build.VERSION.SDK_INT >= 30) {
                window.setDecorFitsSystemWindows(false);
            }
////                    window.clearFlags(0x100000);
//            minusOneLayout = createRootView(context);
//            window.setContentView(minusOneLayout, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            window.getWindowManager().addView(window.getDecorView(), window.getAttributes());
//            if (Build.VERSION.SDK_INT >= 29) {
//                window.setNavigationBarContrastEnforced(false);
//                window.setStatusBarContrastEnforced(false);
//            }
//            if (Build.VERSION.SDK_INT >= 30) {
//                window.setDecorFitsSystemWindows(false);
//            }
//            // 最后再通知
//            windowStateManager = new WindowStateManager(window);
//            windowStateManager.setVisible(false);
//            windowStateManager.setTouchable(false);
//            notifyServerStatus(SERVER_STATUS_OK);
//            bindEvent();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.debug("FullScreenDialog onCreate");
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
        LogUtil.debug("FullScreenDialog setContentView");
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        LogUtil.debug("FullScreenDialog setContentView");
    }
}
