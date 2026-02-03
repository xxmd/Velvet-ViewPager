package tesing;

import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * 将指定 view 的 touchEvent 转成横向滚动事件
 */
public class TouchToScrollConverter {
    private VelocityTracker mVelocityTracker;
    private float downX;
    private float deltaX;
    private float maxScrollDistance = 1000f;

    private HorizontalScrollListener mListener;

    public interface HorizontalScrollListener {
        void onScrollStart();

        void onScrolling(float deltaX, float progress);

        void onScrollEnd(float deltaX, float velocityX);
    }

    public void setHorizontalScrollListener(HorizontalScrollListener listener) {
        this.mListener = listener;
    }

    public void setMaxScrollDistance(float maxDistance) {
        this.maxScrollDistance = Math.max(100f, maxDistance);
    }

    /**
     * 核心方法：外部把触摸事件传进来
     *
     * @return 是否消费了事件（这里始终返回 true，消费所有横向滑动）
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:  // 新手指加入
                downX = event.getX();
                deltaX = 0f;
                if (mListener != null) {
                    mListener.onScrollStart();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int mainPointerId = event.getPointerId(0);
                int mainPointerIndex = event.findPointerIndex(mainPointerId);

                if (mainPointerIndex != -1) {
                    deltaX = event.getX() - downX;
                    float progress = this.deltaX / maxScrollDistance;
                    if (mListener != null) {
                        mListener.onScrolling(this.deltaX, progress);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                if (mListener != null) {
                    mListener.onScrollEnd(deltaX, velocityX);
                }
                reset();
                break;
        }
        return true;
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 可选：手动重置状态（比如页面切换时）
     */
    public void reset() {
        downX = 0f;
        deltaX = 0f;
        recycleVelocityTracker();
    }
}