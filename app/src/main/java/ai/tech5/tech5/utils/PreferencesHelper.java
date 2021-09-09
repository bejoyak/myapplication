package ai.tech5.tech5.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;


public class PreferencesHelper {


    private SharedPreferences preferences;
    private static final String KEY_BASE_URL = "url";
//    private static final String DEFAULT_BASE_URL = "http://192.168.31.122:8097/eKYC_MW/";
     private static final String DEFAULT_BASE_URL = "http://ekyc-health-mw.tech5.tech:8080/eKYC_MW/";

     private String IS_OcrInitialization_Done = "IS_OcrInitialization_Done";
    private boolean DEFAULT_IS_OcrInitialization_Done = false;


    public PreferencesHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setBaseURL(@NonNull String value) {
        preferences.edit().putString(KEY_BASE_URL, value).apply();
    }

    public String getBaseURL() {
        return preferences.getString(KEY_BASE_URL, DEFAULT_BASE_URL);
    }

    public boolean isOcrEnabled() {
        return preferences.getBoolean(IS_OcrInitialization_Done, DEFAULT_IS_OcrInitialization_Done);
    }

    public void setIsOcrEnabled(boolean enableTitle) {
        preferences.edit().putBoolean(IS_OcrInitialization_Done, enableTitle).apply();

    }

}
