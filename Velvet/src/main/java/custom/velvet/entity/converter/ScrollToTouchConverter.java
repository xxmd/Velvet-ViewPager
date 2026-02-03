package custom.velvet.entity.converter;

import android.view.MotionEvent;

public interface ScrollToTouchConverter {
    MotionEvent onScrollStart();

    MotionEvent onScrolling(float progress, int maxScrollDistance);

    MotionEvent onScrollEnd();
}
