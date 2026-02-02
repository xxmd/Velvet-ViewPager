package custom.server.window;

import android.view.Window;
import android.view.WindowManager;

public class OverlayWindowController {
    private Window window;

    public OverlayWindowController(Window window) {
        this.window = window;
    }

    public void setVisible(boolean visible) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = visible ? 1.0f : 0.0f;
        window.setAttributes(attributes);
        window.getWindowManager().updateViewLayout(window.getDecorView(), window.getAttributes());
    }

    public void setTouchable(boolean touchable) {
        int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        if (touchable) {
            window.clearFlags(flag);
        } else {
            window.addFlags(flag);
        }
        window.getWindowManager().updateViewLayout(window.getDecorView(), window.getAttributes());
    }
}
