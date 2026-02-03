package custom.velvet.entity.converter;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

public class HorizonScrollToTouchConverter implements ScrollToTouchConverter {
    private final int fixY = 0;
    private View targetView;
    private long mDownTime;
    private float mLastX;
    private MotionEvent mLastMoveEvent;

    private void reset() {
        if (mLastMoveEvent != null) {
            mLastMoveEvent.recycle();
            mLastMoveEvent = null;
        }
        mDownTime = -1;
        mLastX = 0f;
    }

    private void dispatchTouchEvent(MotionEvent event) {
        if (targetView != null) {
            targetView.dispatchTouchEvent(event);
        }
    }

    @Override
    public MotionEvent onScrollStart() {
        long now = SystemClock.uptimeMillis();
        mDownTime = now;
        float startX = 0f;
        MotionEvent down = MotionEvent.obtain(
                now, now, MotionEvent.ACTION_DOWN,
                startX, fixY, 0
        );
        dispatchTouchEvent(down);
        down.recycle();
        mLastX = startX;
        return down;
    }

    @Override
    public MotionEvent onScrolling(float scrollProgress, int maxScrollDistance) {
        long now = SystemClock.uptimeMillis();
        float moveX = maxScrollDistance * scrollProgress;
        MotionEvent move = MotionEvent.obtain(
                mDownTime,
                now,
                MotionEvent.ACTION_MOVE,
                moveX, fixY, 0
        );
        dispatchTouchEvent(move);
        if (mLastMoveEvent != null) {
            mLastMoveEvent.recycle();
        }
        mLastMoveEvent = move;
        mLastX = moveX;
        return move;
    }

    @Override
    public MotionEvent onScrollEnd() {
        if (mDownTime == -1) return null;
        long now = SystemClock.uptimeMillis();
        float finalX = mLastX;
        MotionEvent up = MotionEvent.obtain(
                mDownTime,
                now,
                MotionEvent.ACTION_UP,
                finalX, fixY, 0
        );
        dispatchTouchEvent(up);
        up.recycle();
        reset();
        return up;
    }
}
