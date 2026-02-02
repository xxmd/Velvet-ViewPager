package custom.aidl;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.WindowManager;

import com.google.android.libraries.launcherclient.ILauncherOverlay;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.util.LogUtil;


public class DefaultLauncherOverlayImpl extends ILauncherOverlay.Stub {
    @Override
    public void startScroll() throws RemoteException {
        LogUtil.debug("LauncherOverlay startScroll");
    }

    @Override
    public void onScroll(float progress) throws RemoteException {
        LogUtil.debug("LauncherOverlay onScroll progress: " + progress);
    }

    @Override
    public void endScroll() throws RemoteException {
        LogUtil.debug("LauncherOverlay endScroll");
    }

    @Override
    public void windowAttached(WindowManager.LayoutParams lp, ILauncherOverlayCallback cb, int flags) throws RemoteException {
        LogUtil.debug("LauncherOverlay windowAttached");
    }

    @Override
    public void windowDetached(boolean isChangingConfigurations) throws RemoteException {
        LogUtil.debug("LauncherOverlay windowDetached isChangingConfigurations: " + isChangingConfigurations);
    }

    @Override
    public void closeOverlay(int flags) throws RemoteException {
        LogUtil.debug("LauncherOverlay closeOverlay flags: " + flags);
    }

    @Override
    public void onPause() throws RemoteException {
        LogUtil.debug("LauncherOverlay onPause");
    }

    @Override
    public void onResume() throws RemoteException {
        LogUtil.debug("LauncherOverlay onResume");
    }

    @Override
    public void openOverlay(int flags) throws RemoteException {
        LogUtil.debug("LauncherOverlay openOverlay flags: " + flags);
    }

    @Override
    public void requestVoiceDetection(boolean start) throws RemoteException {
        LogUtil.debug("LauncherOverlay requestVoiceDetection start: " + start);
    }

    @Override
    public String getVoiceSearchLanguage() throws RemoteException {
        LogUtil.debug("LauncherOverlay getVoiceSearchLanguage");
        return "";
    }

    @Override
    public boolean isVoiceDetectionRunning() throws RemoteException {
        LogUtil.debug("LauncherOverlay isVoiceDetectionRunning");
        return false;
    }

    @Override
    public boolean hasOverlayContent() throws RemoteException {
        LogUtil.debug("LauncherOverlay hasOverlayContent");
        return false;
    }

    @Override
    public void windowAttached2(Bundle bundle, ILauncherOverlayCallback callback) {
    }

    @Override
    public void unusedMethod() throws RemoteException {
        LogUtil.debug("LauncherOverlay unusedMethod");
    }

    @Override
    public void setActivityState(int flags) throws RemoteException {
        LogUtil.debug("LauncherOverlay setActivityState flags: " + flags);
    }

    @Override
    public boolean startSearch(byte[] data, Bundle bundle) throws RemoteException {
        LogUtil.debug("LauncherOverlay startSearch bundle: " + bundle);
        return false;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
