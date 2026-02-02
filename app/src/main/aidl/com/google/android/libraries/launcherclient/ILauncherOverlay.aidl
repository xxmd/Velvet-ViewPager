// ILauncherOverlay.aidl
package com.google.android.libraries.launcherclient;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback;

interface ILauncherOverlay {
    oneway void startScroll(); // Launcher 主动开始滑动，调此方法通知 Overlay
    oneway void onScroll(in float progress); // Launcher 滑动的进度
    oneway void endScroll(); // Launcher 停止滑动，调此方法通知 Overlay
    oneway void windowAttached(in LayoutParams lp, in ILauncherOverlayCallback cb, in int flags);
    oneway void windowDetached(in boolean isChangingConfigurations);
    oneway void closeOverlay(in int flags);
    oneway void onPause();
    oneway void onResume();
    oneway void openOverlay(in int flags);
    oneway void requestVoiceDetection(in boolean start);
    String getVoiceSearchLanguage();
    boolean isVoiceDetectionRunning();
    boolean hasOverlayContent();
    oneway void windowAttached2(in Bundle bundle, in ILauncherOverlayCallback cb);
    oneway void unusedMethod();
    oneway void setActivityState(in int flags);
    boolean startSearch(in byte[] data, in Bundle bundle);
}

