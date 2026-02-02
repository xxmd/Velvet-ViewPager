package tesing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.googlequicksearchbox.R;

import custom.OverlayWindowCore;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initOverlayWindow(this);
        TextView tvHello = findViewById(R.id.tv_hello);
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello 被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initOverlayWindow(Activity activity) {
        OverlayWindowCore overlayWindowCore = new OverlayWindowCore(activity, findViewById(android.R.id.content));
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}