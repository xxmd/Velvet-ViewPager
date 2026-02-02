package custom.util;

import android.os.Parcel;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class LogUtil {
    private static int step = 0;
    private static final String TAG = LogUtil.class.getSimpleName();

    public static void step(String message) {
        step++;
        debug(String.format("【%d】: %s", step, message));
    }

    public static void debug(int i) {
        Log.d(TAG, "int: " + String.valueOf(i));
    }

    public static void debug(float i) {
        Log.d(TAG, "float: " + String.valueOf(i));
    }

    public static void debug(View view, float f) {
        debug(String.format(Locale.US, "view: %s setTranslationX %f", view, f));
        debug(view);
    }

    public static void debug(boolean b) {
        Log.d(TAG, String.valueOf(b));
    }

    public static void debug(Object object) {
        Log.d(TAG, object == null ? "null" : object.toString());
        if (object == null) {
            return;
        }
//        if (object instanceof View) {
//            View view = (View) object;
//            ViewDumper.dump(view);
//            return;
//        }
//        if (object instanceof ViewGroup.LayoutParams) {
//            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) object;
//            LayoutParamsDumper.dump(layoutParams);
//            return;
//        }
    }

    public static void debug(String message) {
        Log.d(TAG, message == null ? "null" : message);
    }

    public static void debug(int i, Parcel parcel, Parcel parcel2, int i2) {
        String message = String.format("dmjk.dispatchTransaction i: %d, parcel: %s, parcel2: %s, i2: %d", i, parcel, parcel2, i2);
        debug(message);
    }

    public static void error(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }

    public static void stackTrace() {
        debug("\n");
        debug("\n<============stackTrace===============");
        String threadName = Thread.currentThread().getName();
        debug("currentThread: " + threadName);
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            debug(e == null ? "null" : e.toString());
        }
        debug("============stackTrace===============>\n");
        debug("\n");
    }

    public static void main(String[] args) {
        int a = 0;
        LogUtil.debug(a);
        boolean b = true;
        LogUtil.debug(b);
        float c = 1.23f;
        LogUtil.debug(c);
        LogUtil.stackTrace();
    }
}
