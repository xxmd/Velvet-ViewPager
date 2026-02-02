package custom;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * 纯横向滑动监听工具类（不继承任何 View）
 * 使用方式：把 View 的 onTouchEvent 事件传进来
 */
public class HorizontalScrollMonitor {

    private final int mTouchSlop;

    private VelocityTracker mVelocityTracker;
    private float mDownX;
    private float mLastX;
    private float mDeltaX = 0f;

    // 用于计算比例的最大滑动距离，可外部设置
    private float mMaxScrollDistance = 1000f;

    private OnHorizontalScrollListener mListener;

    public interface OnHorizontalScrollListener {
        void onScrollStart();
        void onScrolling(float deltaX, float progress);
        void onScrollEnd(float deltaX, float velocityX);
    }

    public HorizontalScrollMonitor(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setOnHorizontalScrollListener(OnHorizontalScrollListener listener) {
        this.mListener = listener;
    }

    public void setMaxScrollDistance(float maxDistance) {
        this.mMaxScrollDistance = Math.max(100f, maxDistance);
    }

    /**
     * 核心方法：外部把触摸事件传进来
     * @return 是否消费了事件（这里始终返回 true，消费所有横向滑动）
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        float x = event.getX();
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mLastX = x;
                mDeltaX = 0f;
                if (mListener != null) {
                    mListener.onScrollStart();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastX;
                mLastX = x;
                mDeltaX += deltaX;
                LogUtil.debug(String.format("mDeltaX: %f, mMaxScrollDistance: %f", mDeltaX, mMaxScrollDistance));
                float progress = mDeltaX / mMaxScrollDistance;
                if (mListener != null) {
                    mListener.onScrolling(mDeltaX, progress);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();

                if (mListener != null) {
                    mListener.onScrollEnd(mDeltaX, velocityX);
                }

                recycleVelocityTracker();
                break;
        }

        return true; // 消费事件，防止穿透
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
        mDeltaX = 0f;
        mLastX = 0f;
        mDownX = 0f;
        recycleVelocityTracker();
    }
}