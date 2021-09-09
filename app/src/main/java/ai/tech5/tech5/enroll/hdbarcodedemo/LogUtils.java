package ai.tech5.tech5.enroll.hdbarcodedemo;

import android.util.Log;

import ai.tech5.tech5.BuildConfig;


public class LogUtils {
    public static void debug(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
