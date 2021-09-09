package ai.tech5.tech5.enroll.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreference {
    private SharedPreferences sharedPref;
    private String BASE_URL = "BASE_URL";
    private String API_CREATE_REQUEST = "API_CREATE_REQUEST";
    public static String API_BASE_URL = "https://idencode.tech5-sa.com/v1/";
    private String CREATE_REQUEST = "enroll";

    private static final String IS_BACKEND_PROCESSING = "isBackendProcessing";
    public static final boolean DEFAULT_IS_BACKEND_PROCESSING = true;

    private static final String BBC_CODES_DIR_PATH = "BBC_CODES_DIR_PATH";

    private String IS_TITLE_ENABLED = "IS_TITLE_ENABLED";
    private boolean DEFAULT_IS_TITLE_ENABLED = true;

    private String APP_VERSION_CODE = "APP_VERSION_CODE";
    private SharedPreferences.Editor editor;
    private String IS_RESOURCE_DELETED = "IS_RESOURCE_DELETED";
    public static final int DEFAULT_COMPRESSION_LEVEL = 1;

    //production
//    public static final String IDENCODE_DEFAULT_BASE_URL = "http://118.97.153.219:8087/multipart/";
//    private static final String MIDDLEWARE_DEFAULT_BASE_URL = "http://118.97.153.219:8085/MohaFEWService/api/";


    //stagging
    public static final String IDENCODE_DEFAULT_BASE_URL = "https://idencode.tech5-sa.com/v1/";
    private static final String MIDDLEWARE_DEFAULT_BASE_URL = "http://182.76.43.76/MohaFRService/api/";

    public AppSharedPreference(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getBaseUrl() {
        return (sharedPref.getString(BASE_URL, API_BASE_URL));
    }


    public String getApiCreateRequest() {
        return (sharedPref.getString(API_CREATE_REQUEST, CREATE_REQUEST));
    }


    public boolean isTitleEnabled() {
        return sharedPref.getBoolean(IS_TITLE_ENABLED, DEFAULT_IS_TITLE_ENABLED);
    }

    public void setIsTitleEnabled(boolean enableTitle) {
        sharedPref.edit().putBoolean(IS_TITLE_ENABLED, enableTitle).apply();

    }


    public boolean isBackEndProcessing() {
        return sharedPref.getBoolean(IS_BACKEND_PROCESSING, DEFAULT_IS_BACKEND_PROCESSING);
    }

    public void setIsBackendProcessing(boolean isBackendProcessing) {

        sharedPref.edit().putBoolean(IS_BACKEND_PROCESSING, isBackendProcessing).apply();
    }

    public void setBBCCodesDirPath(String path) {
        sharedPref.edit().putString(BBC_CODES_DIR_PATH, path).apply();
    }

    public String getBBCCodesDirPath() {
        return sharedPref.getString(BBC_CODES_DIR_PATH, null);
    }

    public int getAppVersionCode() {
        return (sharedPref.getInt(APP_VERSION_CODE, 0));
    }

    public void setResourceDeleted(boolean isResourceDeleted) {
        editor = sharedPref.edit();
        editor.putBoolean(IS_RESOURCE_DELETED, isResourceDeleted);
        editor.apply();
    }

    public boolean isResourceDeleted() {
        return (sharedPref.getBoolean(IS_RESOURCE_DELETED, false));
    }

    public void setVersionCode(int versionCode) {
        editor = sharedPref.edit();
        editor.putInt(APP_VERSION_CODE, versionCode);
        editor.apply();
    }
}
