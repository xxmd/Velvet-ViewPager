// ILauncherOverlayCallback.aidl
package com.google.android.libraries.launcherclient;

// Declare any non-default types here with import statements

import android.os.Bundle;

interface ILauncherOverlayCallback {
    oneway void overlayScrollChanged(float progress); // Overlay 主动滑动的进度，Overlay 通过此接口回调给 Launcher
    oneway void overlayStatusChanged(int status); // Overlay 的滑动状态，比如开始滑动等，Overlay 通过此接口回调给 Launcher
    oneway void onServiceStatus(in Bundle bundle); // 未知
}