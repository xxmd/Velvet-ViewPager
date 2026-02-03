package tesing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.googlequicksearchbox.R;

import custom.common.util.LogUtil;
import custom.launcher.OverlayWindowCore;

public class MainActivity extends Activity {

    private OverlayWindowCore overlayWindowCore;
    private HorizontalScrollView horizontalScrollView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initOverlayWindow(this);
        this.horizontalScrollView = findViewById(R.id.horizontal_scroll_view);
//        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
//            private float startX;
//            private boolean isAtLeftEdge = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getActionMasked();
//                float x = event.getX();
//
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        startX = x;
//                        isAtLeftEdge = false;
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        float deltaX = x - startX;
//
//                        // 判断是否已经到最左边（允许 5px 误差）
//                        boolean atLeft = !horizontalScrollView.canScrollHorizontally(-1);
//                        LogUtil.debug("atLeft: " + atLeft);
//                        // deltaX > 0 表示从左往右滑动（试图拉更左）
//                        if (atLeft && deltaX > 0) {
//                            isAtLeftEdge = true;
//                            // 这里就是你想要的时刻：到最左了，用户还在往右拉
//                            LogUtil.debug("已到最左，用户继续从左往右拉，deltaX: " + deltaX);
//
//                            // 你可以在这里做想做的事：
//                            // 1. 拉出左侧隐藏内容（类似负一屏加载更多）
//                            // 2. 改变透明度/位移（模拟拉伸效果）
//                            // 3. 记录初始拉伸位置，用于后续计算
//                            // 示例：让某个左侧隐藏 View 跟随拉动出现
//                            // leftHiddenView.setTranslationX(deltaX * 0.5f); // 半速跟随
//                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                        } else {
//                            isAtLeftEdge = false;
//                        }
//
//                        // 更新起始点（保持连续性）
//                        startX = x;
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        if (isAtLeftEdge) {
//                            LogUtil.debug("手指抬起，已到最左时的拉伸结束");
//                            // 松手后的处理：回弹动画、加载数据、收起隐藏内容等
//                            // 示例：平滑回弹
//                            // leftHiddenView.animate().translationX(0).setDuration(300).start();
//                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                        }
//                        isAtLeftEdge = false;
//                        break;
//                }
//
//                // 返回 false：让 HorizontalScrollView 继续处理自己的滚动
//                return false;
//            }
//        });
//        TextView tvHello = findViewById(R.id.tv_hello);
//        tvHello.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Hello 被点击", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initOverlayWindow(Activity activity) {
        overlayWindowCore = new OverlayWindowCore(activity);
        overlayWindowCore.bindService();
    }

//    private void immersiveStatusBar() {
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }


    @Override
    protected void onResume() {
        super.onResume();
//        View contentView = findViewById(android.R.id.content);
        View contentView = findViewById(R.id.root);
        overlayWindowCore.addScrollListener(contentView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}