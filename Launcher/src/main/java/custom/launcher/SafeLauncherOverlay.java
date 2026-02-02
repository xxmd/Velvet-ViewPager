package custom.launcher;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.WindowManager;

import com.google.android.libraries.launcherclient.ILauncherOverlay;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

public class SafeLauncherOverlay implements ILauncherOverlay {
    private ILauncherOverlay innerLauncherOverlay;

    public SafeLauncherOverlay(ILauncherOverlay innerLauncherOverlay) {
        this.innerLauncherOverlay = innerLauncherOverlay;
    }

    @Override
    public void startScroll() {
        try {
            innerLauncherOverlay.startScroll();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onScroll(float progress)  {
        try {
            innerLauncherOverlay.onScroll(progress);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endScroll()  {
        try {
            innerLauncherOverlay.endScroll();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void windowAttached(WindowManager.LayoutParams lp, ILauncherOverlayCallback cb, int flags)  {
        try {
            innerLauncherOverlay.windowAttached(lp, cb, flags);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void windowDetached(boolean isChangingConfigurations)  {
        try {
            innerLauncherOverlay.windowDetached(isChangingConfigurations);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeOverlay(int flags)  {
        try {
            innerLauncherOverlay.closeOverlay(flags);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPause()  {
        try {
            innerLauncherOverlay.onPause();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume()  {
        try {
            innerLauncherOverlay.onResume();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openOverlay(int flags)  {
        try {
            innerLauncherOverlay.openOverlay(flags);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestVoiceDetection(boolean start)  {

    }

    @Override
    public String getVoiceSearchLanguage()  {
        return "";
    }

    @Override
    public boolean isVoiceDetectionRunning()  {
        return false;
    }

    @Override
    public boolean hasOverlayContent()  {
        return false;
    }

    @Override
    public void windowAttached2(Bundle bundle, ILauncherOverlayCallback cb)  {
        try {
            innerLauncherOverlay.windowAttached2(bundle, cb);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unusedMethod()  {

    }

    @Override
    public void setActivityState(int flags)  {

    }

    @Override
    public boolean startSearch(byte[] data, Bundle bundle)  {
        return false;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
