package custom.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.android.libraries.launcherclient.ILauncherOverlay;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.common.util.LogUtil;

public class OverlayWindowCore {
    private Activity activity;
    private View rootView;
    private boolean serviceConnected = false;
    private SafeLauncherOverlay safeLauncherOverlay;

    public OverlayWindowCore(Activity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
    }

    public void bindService() {
        String packageName = "com.google.android.googlequicksearchbox";
        String serviceName = "com.google.android.apps.gsa.nowoverlayservice.DrawerOverlayService";
        boolean serviceExist = isServiceExist(activity, packageName, serviceName);
        LogUtil.debug("bindService serviceExist: " + serviceExist);
        Intent intent = new Intent("com.android.launcher3.WINDOW_OVERLAY");
        intent.setClassName(packageName, serviceName);
        intent.setData(Uri.parse("app://somepath"));
        boolean bindServiceSuccess = activity.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ILauncherOverlay launcherOverlay = ILauncherOverlay.Stub.asInterface(service);
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
                        LogUtil.debug("ILauncherOverlaylauncherOverlay onServiceStatus bundle: " + bundle);
                        int serviceStatus = bundle.getInt("service_status");
                        serviceConnected = true;
                        addScrollListener(rootView);
                    }
                };
                Bundle bundle = buildAttachWindowBundle();
                safeLauncherOverlay = new SafeLauncherOverlay(launcherOverlay);
                safeLauncherOverlay.windowAttached2(bundle, callback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.debug("onServiceDisconnected name: " + name);
                serviceConnected = false;
            }
        }, Context.BIND_AUTO_CREATE);
        LogUtil.debug("bindServiceSuccess: " + bindServiceSuccess);
    }

    /**
     * 判断指定 Service 是否存在（已注册在系统中）
     *
     * @param context     上下文
     * @param packageName 服务所在的包名
     * @param className   服务完整类名（包含包名）
     * @return true = 存在，false = 不存在
     */
    public static boolean isServiceExist(Context context, String packageName, String className) {
        try {
            PackageManager pm = context.getPackageManager();

            // 构造 ComponentName
            ComponentName component = new ComponentName(packageName, className);

            // 查询 ServiceInfo
            ServiceInfo serviceInfo = pm.getServiceInfo(component, 0);

            // 如果能拿到 ServiceInfo，说明服务存在
            return serviceInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            // 包名或类名不存在 → 服务不存在
            return false;
        } catch (Exception e) {
            // 其他异常（如权限问题），也视为不存在
            LogUtil.error("ServiceCheck Check service failed", e);
            return false;
        }
    }


    private void addScrollListener(View rootView) {
        HorizontalScrollMonitor scrollMonitor = new HorizontalScrollMonitor(activity);
        int measuredWidth = rootView.getMeasuredWidth();
        if (measuredWidth == 0) {
            measuredWidth = rootView.getContext().getResources().getDisplayMetrics().widthPixels;
        }
        LogUtil.debug("measuredWidth: " + measuredWidth);
        scrollMonitor.setMaxScrollDistance(measuredWidth);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return scrollMonitor.onTouchEvent(event);
            }
        });
        scrollMonitor.setOnHorizontalScrollListener(new HorizontalScrollMonitor.OnHorizontalScrollListener() {
            @Override
            public void onScrollStart() {
                LogUtil.debug(String.format("onScrollStart"));
                safeLauncherOverlay.startScroll();
            }

            @Override
            public void onScrolling(float deltaX, float progress) {
                LogUtil.debug(String.format("onScrolling deltaX: %f, progress: %f", deltaX, progress));
                safeLauncherOverlay.onScroll(progress);
            }

            @Override
            public void onScrollEnd(float deltaX, float velocityX) {
                LogUtil.debug(String.format("onScrollEnd deltaX: %f, velocityX: %f", deltaX, velocityX));
                safeLauncherOverlay.endScroll();
            }
        });
    }

    private Bundle buildAttachWindowBundle() {
        Bundle bundle = new Bundle();
        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
        bundle.putParcelable("layout_params", attributes);
        Configuration configuration = activity.getResources().getConfiguration();
        bundle.putParcelable("configuration", configuration);
        return bundle;
    }
}
