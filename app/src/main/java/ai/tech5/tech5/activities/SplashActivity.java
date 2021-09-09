package ai.tech5.tech5.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.completions.IDocumentReaderInitCompletion;
import com.regula.documentreader.api.completions.IDocumentReaderPrepareCompletion;
import com.regula.documentreader.api.enums.Scenario;
import com.regula.documentreader.api.errors.DocumentReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import ai.tech5.tech5.BuildConfig;
import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.hdbarcodedemo.Listener;
import ai.tech5.tech5.enroll.hdbarcodedemo.Utilities;
import ai.tech5.tech5.enroll.model.Configuration;
import ai.tech5.tech5.enroll.preferences.AppSharedPreference;
import ai.tech5.tech5.enroll.utils.Logger;
import ai.tech5.tech5.utils.PreferencesHelper;

import static ai.tech5.tech5.enroll.utils.Logger.logException;
import static android.system.Os.setenv;

public class SplashActivity extends Activity {
    private static final int CODE_WRITE_SETTINGS_PERMISSION = 111;
    boolean flag_is_permission_set = false;
    private String[] perms;
    public static final String TAG = SplashActivity.class.getSimpleName();
    private LinkedHashMap<String, Boolean> allPermissions;
    private ArrayList<String> permissionsToRequest = new ArrayList();
    private boolean isBackgroundProcessStarted;
    private PreferencesHelper preferencesHelper;
    private TextView tvAppVersion;
///////////
    private Utilities util;
    private AppSharedPreference appSharedPreference;
    private int compressionLevel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        setContentView(R.layout.splash_new);
        Logger.addToLog(TAG, "Splash screen Started ");
        tvAppVersion = findViewById(R.id.app_version);
        tvAppVersion.setText(" " + BuildConfig.VERSION_NAME);

//        addLog("::::::::::::::::Splash screen Started::::::::::::::: ");
        System.out.println("\"::::::::::::::::Splash screen Started::::::::::::::: \"");

//        TextView tvBuildVersion = findViewById(R.id.tv_build_version);
//        tvBuildVersion.setText(" " + BuildConfig.VERSION_NAME);
        perms = getResources().getStringArray(R.array.all_permissions);

        allPermissions = new LinkedHashMap<>();
        preferencesHelper = new PreferencesHelper(SplashActivity.this);

        compressionLevel = AppSharedPreference.DEFAULT_COMPRESSION_LEVEL;

        util = new Utilities(this);
        appSharedPreference = new AppSharedPreference(this);

        for (String permission : perms) {
            allPermissions.put(permission, false);
        }
        checkPermissionsToRequest();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 200);
            } else {
//                addLog("::::::::::::::::ALL PERMISSIONS GRANTED::::::::::::::: ");
//                LogUtils.debug(TAG, "::::::::::::::::ALL PERMISSIONS GRANTED::::::::::::::: ");
                System.out.println("::::::::::::::::ALL PERMISSIONS GRANTED::::::::::::::: ");
                Logger.addToLog(TAG, "ALL PERMISSIONS GRANTED ");

                startBackgroundProcess();
            }
        } else {
            System.out.println("::::::::::::::::SDK BELOW 23 SO ALL PERMISSIONS GRANTED::::::::::::::: ");
            Logger.addToLog(TAG, "SDK BELOW 23 SO ALL PERMISSIONS GRANTED");

//            addLog("::::::::::::::::SDK BELOW 23 SO ALL PERMISSIONS GRANTED::::::::::::::: ");
//            LogUtils.debug(TAG, "::::::::::::::::SDK BELOW 23 SO ALL PERMISSIONS GRANTED::::::::::::::: ");
            startBackgroundProcess();
        }


    }


    private void checkPermissionsToRequest() {

        for (String permission : allPermissions.keySet()) {
            if (hasPermission(permission)) {
                allPermissions.put(permission, true);
            } else {
                allPermissions.put(permission, false);
            }
        }
        for (String permission : allPermissions.keySet()) {
            if (!allPermissions.get(permission)) {
                permissionsToRequest.add(permission);
            }
        }
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                for (int permission : grantResults) {
                    if (grantResults[permission] == PackageManager.PERMISSION_GRANTED) {
//                        addLog("All Permissions Granted");
//                        LogUtils.debug(TAG, "All Permissions Granted");
//                        addLog("200 startBarchgroundProcess 1");
                        System.err.println("200 startBarchgroundProcess 1");
                        startBackgroundProcess();
                    } else {
//                        addLog("Permissions Denied");
//                        LogUtils.debug(TAG, "Permissions Denied");
                        System.out.println("Permissions Denied");

                    }
                }
                break;
        }
    }

    private void startBackgroundProcess() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            flag_is_permission_set = true;
            System.err.println("start loading 1");
//                addLog("start loading 1");
            startLoading();
            //   }
        } else {
            startLoading();
            System.err.println("start loading 2");
//            addLog("start loading 2");
        }

    }

    private void addLog(String message) {
        Logger.addToLog(TAG, message);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "OnResume.....");
                Logger.addToLog(TAG, "OnResume.....");

                preferencesHelper.setIsOcrEnabled(false);
//                load_image_captureID_btn.setEnabled(false);
//                isbackgroudinitializationdone = false;
                if (!DocumentReader.Instance().getDocumentReaderIsReady()) {
                    Log.d(TAG, "OnResume.....if getDocumentReaderIsReady");
//            progressDialog.show();
////            showDialog(true, "Initializing SDK");
                    //Reading the license from raw resource file
                    InputStream licInput = null;
                    try {
                        licInput = getResources().openRawResource(R.raw.regula);
                        int available = licInput.available();
                        final byte[] license = new byte[available];
                        //noinspection ResultOfMethodCallIgnored
                        licInput.read(license);

                        //preparing database files, it will be downloaded from network only one time and stored on user device
                        DocumentReader.Instance().prepareDatabase(SplashActivity.this, "Full", new IDocumentReaderPrepareCompletion() {

                            @Override
                            public void onPrepareProgressChanged(int progress) {
//                        progressDialog.setTitle("Downloading database: " + progress + "%");
////                        showDialog(true, "Downloading database: " + progress + "%");
                                Log.d(TAG, "OnResume..... onPrepareProgressChanged"+progress);

                            }


                            @Override
                            public void onPrepareCompleted(boolean status, DocumentReaderException error) {

                                //Initializing the reader
                                DocumentReader.Instance().initializeReader(SplashActivity.this, license, new IDocumentReaderInitCompletion() {

                                    @Override
                                    public void onInitCompleted(boolean success, DocumentReaderException error) {
////                                hideProgress();
//                                hideProgresscard();
                                        DocumentReader.Instance().customization().edit().setShowHelpAnimation(false).apply();
                                        Log.d(TAG, "OnResume..... onInitCompleted");

                                        //initialization successful
                                        if (success) {
                                            preferencesHelper.setIsOcrEnabled(true);
                                            Log.d(TAG, "OnResume..... success");
                                            Logger.addToLog(TAG, "OnResume..... success");

//                                            isbackgroudinitializationdone = true;
//                                            load_image_captureID_btn.setEnabled(true);
                                            DocumentReader.Instance().processParams().scenario = Scenario.SCENARIO_MRZ_OR_OCR;
                                        }
                                        //Initialization was not successful
                                        else {
                                            Log.d(TAG, "OnResume..... Init failed");
                                            Logger.addToLog(TAG, "OnResume..... Init failed");

                                            Toast.makeText(SplashActivity.this, "Init failed:" + error, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            licInput.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "OnResume..... last true");
                    Logger.addToLog(TAG, "OnResume..... last true");

                    preferencesHelper.setIsOcrEnabled(true);
//                    isbackgroudinitializationdone = true;
//                    load_image_captureID_btn.setEnabled(true);
                }
            }
        });
    }
private void startLoading() {
    util.log();
    if (!isBackgroundProcessStarted) {
        isBackgroundProcessStarted = true;
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {


                    Configuration config = util.loadConfiguration();

                    if (config != null && config.barcodeGenerationParameters != null) {

                        if (config.barcodeGenerationParameters.compressedFaceImage != null) {
                            compressionLevel = Utilities.getCompressionLevel(config.barcodeGenerationParameters.compressedFaceImage.size);
                        }

                    }


                    boolean flag = util.loadBinFiles();
                    long t2 = System.currentTimeMillis();
//                    System.err.println("Time Taken for BIN LOADERS:::" + (t2 - t1));
//                    addLog("Time Taken for BIN LOADERS:::" + (t2 - t1));
                    System.err.println("FLAG:::" + flag);
                    addLog("FLAG:::" + flag);
                    System.loadLibrary("c++_shared");
                    addLog("c++_shared");
                    System.loadLibrary("face_sdk");
                    addLog("face_sdk");
                    // System.loadLibrary("mxnet");
                    //  addLog("mxnet");
                    System.loadLibrary("T5FaceNativeJNI");
                    addLog("T5FaceNativeJNI");
                    System.loadLibrary("passportizer_jni");
                    addLog("passportizer_jni");
                    //  setenv("FACE_SDK_BIN_ROOT", "/storage/emulated/0/T5BBC/face_sdk", true);

                        setenv("FACE_SDK_BIN_ROOT", "/storage/emulated/0/Android/data/ai.tech5.vido/files/T5BBC/face_sdk", true);

                    //storage/emulated/0/Android/data/com.moha.enroll.digitalid/files/Pictures/BBC_CODES/Divya_2021_02_23_20_02_24.png

                    Listener.initSDK(compressionLevel,getApplicationContext());

                    // Listener.initSmartCompress();
                    System.err.println("SMART COMPRESS INITIALIZED");
                    addLog("SMART COMPRESS INITIALIZED");


                } catch (Exception e) {

                    logException(TAG, e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
//                    if (!appSharedPreference.isFirstTimeInstalled()) {
//                        appSharedPreference.setFirstTimeInstalled(true);
//                        showRestartAppDialog();
//                    } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                //      }
            }
        }.execute();
    }
}
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addLog("CODE_WRITE_SETTINGS_PERMISSION success ");
        Log.d("TAG", "CODE_WRITE_SETTINGS_PERMISSION success ");
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(this)) {
            addLog("CODE_WRITE_SETTINGS_PERMISSION success");
            Log.d("TAG", "CODE_WRITE_SETTINGS_PERMISSION success");
            startLoading();
            addLog("start loading 3");
            System.err.println("start loading 3");
        }
    }

}
