package tesing.impl;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

import custom.common.util.LogUtil;

public class DefaultLauncherOverlayCallback extends ILauncherOverlayCallback.Stub {
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
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
