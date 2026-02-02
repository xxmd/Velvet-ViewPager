package custom.window;

import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import custom.util.LogUtil;

public class OverlayWindowCallback implements Window.Callback {
    private Window.Callback srcCallback;

    public void onBackPressed() {
        LogUtil.debug("OverlayWindowCallback onBackPressed");
    }

    public OverlayWindowCallback(Window.Callback srcCallback) {
        this.srcCallback = srcCallback;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return srcCallback.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                onBackPressed();
            }
        }
        return srcCallback.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return srcCallback.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return srcCallback.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return srcCallback.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return srcCallback.dispatchTrackballEvent(event);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        srcCallback.onActionModeFinished(mode);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        srcCallback.onActionModeStarted(mode);
    }

    @Override
    public void onAttachedToWindow() {
        srcCallback.onAttachedToWindow();
    }

    @Override
    public void onContentChanged() {
        srcCallback.onContentChanged();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        return srcCallback.onCreatePanelMenu(featureId, menu);
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return srcCallback.onCreatePanelView(featureId);
    }

    @Override
    public void onDetachedFromWindow() {
        srcCallback.onDetachedFromWindow();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        return srcCallback.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onMenuOpened(int featureId, @NonNull Menu menu) {
        return srcCallback.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        srcCallback.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, @Nullable View view, @NonNull Menu menu) {
        return srcCallback.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onSearchRequested() {
        return srcCallback.onSearchRequested();
    }

    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return srcCallback.onSearchRequested(searchEvent);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        srcCallback.onWindowAttributesChanged(attrs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        srcCallback.onWindowFocusChanged(hasFocus);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return srcCallback.onWindowStartingActionMode(callback);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return srcCallback.onWindowStartingActionMode(callback, type);
    }
}
