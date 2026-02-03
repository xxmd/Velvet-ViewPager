package custom.velvet.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import custom.common.util.LogUtil;

/**
 * 负一屏根布局：继承 FrameLayout
 * 内部使用 ViewPager2 实现左右两页
 * 左边：外部传入的自定义 View
 * 右边：居中显示“右边”
 * 支持滑动监听 + 获取左右两侧的 View
 */
public class MinusOneLayout extends FrameLayout {

    private View mCustomLeftView;
    private ViewPager2 mViewPager;
    private OnScrollListener mScrollListener;

    // 新增：保存左右两侧的容器（方便获取子 View）
    private FrameLayout mLeftContainer;
    private FrameLayout mRightContainer;
    private int leftItemIndex = 0;
    private int rightItemIndex = 1;

    public MinusOneLayout(Context context) {
        this(context, null);
    }

    public MinusOneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinusOneLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.parseColor("#33000000"));

        mViewPager = new ViewPager2(context);
        LayoutParams pagerParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        mViewPager.setLayoutParams(pagerParams);

        mViewPager.setAdapter(new SimplePagerAdapter());

        // 默认显示右边页面
        mViewPager.setCurrentItem(1, false);

        // 滑动监听（保持原样）
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 计算整体进度：0.0f = 完全左边，1.0f = 完全右边
                float progress = position + positionOffset;
                if (mScrollListener != null) {
                    mScrollListener.onScrolling(progress);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 只有在滑动完全停止（IDLE）时才判断是否“完全展示”
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    LogUtil.debug("onPageScrollStateChanged state: " + state);
                    if (mScrollListener != null) {
                        int currentItem = mViewPager.getCurrentItem();
                        switch (currentItem) {
                            case 0:
                                mScrollListener.onLeftFullyShown();
                                break;
                            case 1:
                                mScrollListener.onRightFullyShown();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mScrollListener != null) {
                }
            }
        });

        addView(mViewPager);
    }

    /**
     * 设置左边自定义视图
     */
    public void setCustomLeftView(View customLeftView) {
        this.mCustomLeftView = customLeftView;
        if (mLeftContainer != null) {
            mLeftContainer.removeAllViews();
            if (customLeftView != null) {
                if (customLeftView.getParent() != null) {
                    ((ViewGroup) customLeftView.getParent()).removeView(customLeftView);
                }
                mLeftContainer.addView(customLeftView);
            } else {
                addCenterText(mLeftContainer, "左边自定义内容（请传入 View）", Color.LTGRAY);
            }
        }
    }

    /**
     * 获取左边页面的当前 View（可能是自定义 View 或占位 TextView）
     */
    
    public View getLeftView() {
        if (mLeftContainer != null && mLeftContainer.getChildCount() > 0) {
            return mLeftContainer.getChildAt(0);
        }
        return null;
    }

    /**
     * 获取右边页面的当前 View（居中显示“右边”的 TextView）
     */
    
    public View getRightView() {
        if (mRightContainer != null && mRightContainer.getChildCount() > 0) {
            return mRightContainer.getChildAt(0);
        }
        return null;
    }

    /**
     * 设置滑动监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }

    public boolean isLeftFullShown() {
        if (mViewPager == null) {
            return false;
        }
        boolean isScrollingNow = (mViewPager.getScrollState() != ViewPager2.SCROLL_STATE_IDLE);
        return mViewPager.getCurrentItem() == 0 && !isScrollingNow;
    }

    public void scrollToRight() {
        if (mViewPager == null) {
            return;
        }
        mViewPager.setCurrentItem(rightItemIndex, true);
    }

    public interface OnScrollListener {
        void onScrolling(float progress);

        void onLeftFullyShown();

        void onRightFullyShown();
    }

    /**
     * 简易适配器，只有两页
     */
    private class SimplePagerAdapter extends RecyclerView.Adapter<SimplePagerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout container = new FrameLayout(parent.getContext());
            container.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            return new ViewHolder(container);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FrameLayout container = holder.container;
            container.removeAllViews();

            if (position == 0) {
                mLeftContainer = container;
                mLeftContainer.setBackgroundColor(Color.RED);
                mLeftContainer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "左边区域被点击", Toast.LENGTH_SHORT).show();
                    }
                });
                if (mCustomLeftView != null) {
                    if (mCustomLeftView.getParent() != null) {
                        ((ViewGroup) mCustomLeftView.getParent()).removeView(mCustomLeftView);
                    }
                    container.addView(mCustomLeftView);
                } else {
                    addCenterText(container, "左边自定义内容（请传入 View）", Color.LTGRAY);
                }
            } else {
                mRightContainer = container;
                mRightContainer.setBackgroundColor(Color.GREEN);
                addCenterText(container, "右边", Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final FrameLayout container;

            ViewHolder(FrameLayout itemView) {
                super(itemView);
                this.container = itemView;
            }
        }
    }

    private void addCenterText(FrameLayout container, String text, int textColor) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setTextSize(28);
        tv.setTextColor(textColor);
        tv.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        container.addView(tv, params);
    }
}