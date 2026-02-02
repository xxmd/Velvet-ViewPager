package com.google.android.apps.gsa.nowoverlayservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import custom.aidl.LauncherOverlayImpl;

public class DrawerOverlayService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LauncherOverlayImpl(getApplication());
    }
}
