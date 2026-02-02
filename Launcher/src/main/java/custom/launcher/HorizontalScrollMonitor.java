package custom.client;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import custom.common.util.LogUtil;

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

        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex(); // 当前动作的指针索引

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:  // 新手指加入
                // 优先使用主指针（ID=0）
                int activePointerId = event.getPointerId(0);
                int activePointerIndex = event.findPointerIndex(activePointerId);

                if (activePointerIndex != -1) {
                    mDownX = event.getX(activePointerIndex);
                    mLastX = mDownX;
                    mDeltaX = 0f;
                    if (mListener != null) {
                        mListener.onScrollStart();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // 跟踪主指针（ID=0）的移动
                int mainPointerId = event.getPointerId(0);
                int mainPointerIndex = event.findPointerIndex(mainPointerId);

                if (mainPointerIndex != -1) {
                    float x = event.getX(mainPointerIndex);
                    float deltaX = x - mLastX;
                    mLastX = x;
                    mDeltaX += deltaX;

                    LogUtil.debug(String.format("mDeltaX: %f, mMaxScrollDistance: %f", mDeltaX, mMaxScrollDistance));

                    float progress = mDeltaX / mMaxScrollDistance;
                    if (mListener != null) {
                        mListener.onScrolling(mDeltaX, progress);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:  // 手指抬起
                // 如果是主指针抬起，且还有其他手指，切换到新主指针
                int upPointerId = event.getPointerId(pointerIndex);
                if (upPointerId == event.getPointerId(0) && event.getPointerCount() > 1) {
                    // 切换到第一个剩余指针作为新主指针
                    int newPointerId = event.getPointerId(0);
                    int newPointerIndex = event.findPointerIndex(newPointerId);
                    if (newPointerIndex != -1) {
                        mLastX = event.getX(newPointerIndex);
                    }
                }

                // 结束时计算速度
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();

                if (mListener != null) {
                    mListener.onScrollEnd(mDeltaX, velocityX);
                }

                if (action == MotionEvent.ACTION_UP) {
                    // 所有手指都抬起了，回收
                    recycleVelocityTracker();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                // 取消事件，直接结束
                if (mListener != null) {
                    mListener.onScrollEnd(mDeltaX, 0f); // 速度为0
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