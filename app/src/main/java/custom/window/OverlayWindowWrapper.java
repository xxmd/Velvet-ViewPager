package custom.window;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class OverlayWindowWrapper {
    private Context context;
    private IBinder token;
    private Window innerWindow;

    public OverlayWindowWrapper(Context context, IBinder token) {
        this.context = context;
        this.token = token;
        init();
    }

    private void init() {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        innerWindow = dialog.getWindow();
        if (innerWindow != null) {
            innerWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            innerWindow.setStatusBarColor(Color.TRANSPARENT);
            innerWindow.setNavigationBarColor(Color.TRANSPARENT);
            innerWindow.setWindowManager(null, token, "Velvet-ViewPager", true);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_DRAWN_APPLICATION;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            layoutParams.dimAmount = 0.0f;
            innerWindow.setAttributes(layoutParams);
            if (Build.VERSION.SDK_INT >= 29) {
                innerWindow.setNavigationBarContrastEnforced(false);
                innerWindow.setStatusBarContrastEnforced(false);
            }
            if (Build.VERSION.SDK_INT >= 30) {
                innerWindow.setDecorFitsSystemWindows(false);
            }
        }
    }

    public void setContentView(View view) {
        innerWindow.setContentView(view, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        innerWindow.getWindowManager().addView(innerWindow.getDecorView(), innerWindow.getAttributes());
    }

    public Window getWindow() {
        return innerWindow;
    }
}
