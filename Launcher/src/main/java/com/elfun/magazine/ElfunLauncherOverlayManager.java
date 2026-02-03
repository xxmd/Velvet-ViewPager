package com.elfun.magazine;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.launcher3.Launcher;
import com.android.systemui.plugins.shared.LauncherOverlayManager;
import com.google.android.libraries.gsa.launcherclient.LauncherClient;
import com.google.android.libraries.gsa.launcherclient.LauncherClientCallbacks;

import java.io.PrintWriter;

public class ElfunLauncherOverlayManager implements LauncherOverlayManager, LauncherOverlayManager.LauncherOverlay, LauncherClientCallbacks, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_ENABLE_MINUS_ONE = "pref_enable_minus_one";
    private final LauncherClient mClient;
    private final Launcher mLauncher;
    private LauncherOverlayManager.LauncherOverlayCallbacks mLauncherOverlayCallbacks;
    private boolean mWasOverlayAttached = false;

    public ElfunLauncherOverlayManager(Launcher launcher) {
        this.mLauncher = launcher;
        this.mClient = new LauncherClient(launcher, this, getClientOptions(null));
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void onDeviceProvideChanged() {
        this.mClient.reattachOverlay();
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void onAttachedToWindow() {
        this.mClient.onAttachedToWindow();
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void onDetachedFromWindow() {
        this.mClient.onDetachedFromWindow();
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void dump(String prefix, PrintWriter w) {
        this.mClient.dump(prefix, w);
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void openOverlay() {
        this.mClient.showOverlay(true);
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void hideOverlay(boolean animate) {
        this.mClient.hideOverlay(animate);
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public void hideOverlay(int duration) {
        this.mClient.hideOverlay(duration);
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager
    public boolean startSearch(byte[] config, Bundle extras) {
        return false;
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        this.mClient.onStart();
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        this.mClient.onResume();
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        this.mClient.onPause();
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        this.mClient.onStop();
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    // com.android.systemui.plugins.shared.LauncherOverlayManager, android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        this.mClient.onDestroy();
//        this.mLauncher.getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (KEY_ENABLE_MINUS_ONE.equals(key)) {
            this.mClient.setClientOptions(getClientOptions(prefs));
        }
    }

    @Override // com.google.android.libraries.gsa.launcherclient.LauncherClientCallbacks
    public void onServiceStateChanged(boolean overlayAttached, boolean hotwordActive) {
        if (overlayAttached != this.mWasOverlayAttached) {
            this.mWasOverlayAttached = overlayAttached;
            this.mLauncher.setLauncherOverlay(overlayAttached ? this : null);
        }
    }

    @Override // com.google.android.libraries.gsa.launcherclient.LauncherClientCallbacks
    public void onOverlayScrollChanged(float progress) {
        LauncherOverlayManager.LauncherOverlayCallbacks launcherOverlayCallbacks = this.mLauncherOverlayCallbacks;
        if (launcherOverlayCallbacks != null) {
//            launcherOverlayCallbacks.onOverlayScrollChanged(progress);
            launcherOverlayCallbacks.onScrollChanged(progress);
        }
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager.LauncherOverlay
    public void onScrollInteractionBegin() {
        this.mClient.startMove();
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager.LauncherOverlay
    public void onScrollInteractionEnd() {
        this.mClient.endMove();
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager.LauncherOverlay
    public void onScrollChange(float progress, boolean rtl) {
        this.mClient.updateMove(progress);
    }

    @Override // com.android.systemui.plugins.shared.LauncherOverlayManager.LauncherOverlay
    public void setOverlayCallbacks(LauncherOverlayManager.LauncherOverlayCallbacks callbacks) {
        this.mLauncherOverlayCallbacks = callbacks;
    }

    private LauncherClient.ClientOptions getClientOptions(SharedPreferences prefs) {
//        return new LauncherClient.ClientOptions(prefs.getBoolean(KEY_ENABLE_MINUS_ONE, true), true, true);
        return new LauncherClient.ClientOptions(true, true, true);
    }
}
