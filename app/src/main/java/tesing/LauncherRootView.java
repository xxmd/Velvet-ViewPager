package tesing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import custom.common.util.LogUtil;

public class LauncherRootView extends FrameLayout {
    private float downX;
    private View scrollableChild;
    private int touchSlop;

    public LauncherRootView(Context context) {
        super(context);
    }

    public LauncherRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LauncherRootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LauncherRootView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        LogUtil.debug("LauncherRootView onAttachedToWindow");
        super.onAttachedToWindow();
        scrollableChild = findPgeViewChild(this);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - downX;
                if (showIntercept(deltaX)) {
                    LogUtil.debug("开始拦截触屏事件，自己消费，不给到ScrollView");
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean showIntercept(float deltaX) {
        return deltaX > touchSlop && scrollableChild != null && !scrollableChild.canScrollHorizontally(-1);
    }

    public static View findPgeViewChild(View parent) {
        if (parent == null) return null;
//        if (parent instanceof PagedView) {
//            return parent;
//        }
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View result = findPgeViewChild(viewGroup.getChildAt(i));
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
