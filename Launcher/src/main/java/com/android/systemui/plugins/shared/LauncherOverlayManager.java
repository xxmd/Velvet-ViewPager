package com.android.systemui.plugins.shared;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.io.PrintWriter;

/* loaded from: classes2.dex */
public interface LauncherOverlayManager extends Application.ActivityLifecycleCallbacks {

    public interface LauncherOverlay {
        void onScrollChange(float f, boolean z);

        void onScrollInteractionBegin();

        void onScrollInteractionEnd();

        void setOverlayCallbacks(LauncherOverlayCallbacks launcherOverlayCallbacks);
    }

    public interface LauncherOverlayCallbacks {
        void onScrollChanged(float f);
    }

    default void onDeviceProvideChanged() {
    }

    default void onAttachedToWindow() {
    }

    default void onDetachedFromWindow() {
    }

    default void dump(String prefix, PrintWriter w) {
    }

    default void openOverlay() {
    }

    default void hideOverlay(boolean animate) {
        hideOverlay(animate ? 200 : 0);
    }

    default void hideOverlay(int duration) {
    }

    default boolean startSearch(byte[] config, Bundle extras) {
        return false;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivityStopped(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    default void onActivityDestroyed(Activity activity) {
    }
}