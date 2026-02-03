package tesing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.android.googlequicksearchbox.R;
import com.google.android.libraries.launcherclient.ILauncherOverlay;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.common.util.LogUtil;
import tesing.impl.DefaultLauncherOverlayCallback;
import tesing.impl.SafeLauncherOverlay;


public class MainActivity extends Activity {
    private boolean serviceConnected = false;
    private TouchToScrollConverter scrollConverter;
    private SafeLauncherOverlay safeLauncherOverlay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    private void bindService() {
        Intent intent = buildOverlayWindowIntent();
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                try {
                    ILauncherOverlay launcherOverlay = ILauncherOverlay.Stub.asInterface(binder);
                    safeLauncherOverlay = new SafeLauncherOverlay(launcherOverlay);
                    Bundle bundle = buildAttachWindowBundle(MainActivity.this);
                    ILauncherOverlayCallback callback = new ILauncherOverlayCallback.Stub() {
                        @Override
                        public void overlayScrollChanged(float progress) throws RemoteException {

                            LogUtil.debug("ILauncherOverlayCallback overlayScrollChanged progress: " + progress);
                        }

                        @Override
                        public void overlayStatusChanged(int status) throws RemoteException {
                            LogUtil.debug("ILauncherOverlayCallback overlayStatusChanged status: " + status);
                        }

                        @Override
                        public void onServiceStatus(Bundle bundle) throws RemoteException {
                            LogUtil.debug("ILauncherOverlayCallback onServiceStatus bundle: " + bundle);
                            serviceConnected = true;
                            monitorViewScroll(findViewById(android.R.id.content));
                        }
                    };
                    launcherOverlay.windowAttached2(bundle, callback);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceConnected = false;
            }
        }, Service.BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (serviceConnected) {
            monitorViewScroll(findViewById(android.R.id.content));
        }
    }

    private void monitorViewScroll(View view) {
        if (view == null || view.getMeasuredWidth() == 0) {
            return;
        }
        if (scrollConverter != null) {
            return;
        }
        scrollConverter = new TouchToScrollConverter();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return scrollConverter.onTouchEvent(event);
            }
        });
        scrollConverter.setMaxScrollDistance(view.getMeasuredWidth());
        scrollConverter.setHorizontalScrollListener(new TouchToScrollConverter.HorizontalScrollListener() {
            @Override
            public void onScrollStart() {
                safeLauncherOverlay.startScroll();
            }

            @Override
            public void onScrolling(float deltaX, float progress) {
                safeLauncherOverlay.onScroll(progress);
            }

            @Override
            public void onScrollEnd(float deltaX, float velocityX) {
                safeLauncherOverlay.endScroll();
            }
        });
    }

    private Intent buildOverlayWindowIntent() {
        Intent intent = new Intent("com.android.launcher3.WINDOW_OVERLAY");
        intent.setPackage("com.google.android.googlequicksearchbox");
        intent.setData(Uri.parse("app://some_path"));
        return intent;
    }

    private Bundle buildAttachWindowBundle(Activity activity) {
        Bundle bundle = new Bundle();
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        bundle.putParcelable("layout_params", attributes);
        Configuration configuration = activity.getResources().getConfiguration();
        bundle.putParcelable("configuration", configuration);
        return bundle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}