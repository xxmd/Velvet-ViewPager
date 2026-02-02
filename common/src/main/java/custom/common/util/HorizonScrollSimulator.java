package custom.common.util;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

public class HorizonScrollSimulator {
    private final int fixY = 0;
    private View targetView;
    private long mDownTime;
    private float mLastX;
    private MotionEvent mLastMoveEvent;

    public View getTargetView() {
        return targetView;
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }

    public void simulateDown() {
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
    }

    public void simulateMove(float x) {
        long now = SystemClock.uptimeMillis();
        float y = 0f;  // y 始终为 0
        MotionEvent move = MotionEvent.obtain(
                mDownTime,
                now,
                MotionEvent.ACTION_MOVE,
                x, fixY, 0
        );
        dispatchTouchEvent(move);
        if (mLastMoveEvent != null) {
            mLastMoveEvent.recycle();
        }
        mLastMoveEvent = move;
        // 更新最后 x，用于 UP 事件
        mLastX = x;
    }

    public void simulateUp() {
        if (mDownTime == -1) return;
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
    }

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
}
