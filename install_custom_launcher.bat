@echo off
setlocal enabledelayedexpansion

set APK_SRC=E:\StudioProjects\Velvet-ViewPager\Launcher3QuickStep.apk
set APK_DST=/system_ext/priv-app/Launcher3QuickStep/Launcher3QuickStep.apk

echo ================================
echo 1. Checking adb devices...
echo ================================

adb devices | findstr /R /C:"device$" >nul
if errorlevel 1 (
    echo [ERROR] No adb device found.
    pause
    exit /b 1
)

echo [OK] Device found.

echo ================================
echo 2. adb root
echo ================================
adb root
if errorlevel 1 (
    echo [ERROR] adb root failed.
    pause
    exit /b 1
)

timeout /t 2 >nul

echo ================================
echo 3. adb remount
echo ================================
adb remount

echo ================================
echo 4. adb push Velvet.apk
echo ================================
adb push "%APK_SRC%" "%APK_DST%"
if errorlevel 1 (
    echo [ERROR] adb push failed.
    pause
    exit /b 1
)

echo ================================
echo 5. adb reboot
echo ================================
adb reboot

echo [DONE] All steps executed.
pause
