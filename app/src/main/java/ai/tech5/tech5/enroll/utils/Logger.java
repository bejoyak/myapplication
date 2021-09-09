package ai.tech5.tech5.enroll.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;

public class Logger {
//    public static Context context;
//
//    public Logger(Context context) {
//        this.context = context;
//    }

    public Logger() {
    }

    private final static String TAG = "Logger";
    public static FileHandler logger = null;
    public static final String LOG_FILE_NAME = "Tech5.txt";

    static boolean isExternalStorageAvailable = false;
    static boolean isExternalStorageWriteable = false;
    static String state = Environment.getExternalStorageState();
//    public static FingerprintUtils fingerprintUtils;

    //    public static void addRecordToLog(String tag, String message)

    public static void logException(String tag, Exception e) {
        e.printStackTrace();
        addToLog(tag, Log.getStackTraceString(e));
    }

    /**
     * Adding message to the Log file
     *
     * @param tag     tag
     * @param message message
     */
    public static void addToLog(String tag, String message) {

        if (tag == null) {
            tag = "Tech5";
        }

        Log.i(tag, message);


        String storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        System.out.println("path................" + storageDir);

//        String userDIR = storageDir +File.separator+"KM"+ File.separator + "BBC_LOGS";
        String userDIR = storageDir +File.separator+"Tech5";

        System.out.println("path......................" + userDIR);
        File var2 = new File(userDIR);
        if (!var2.exists()) {
            var2.mkdirs();
        }
        File logFile = new File(userDIR, LOG_FILE_NAME);

        if (!logFile.exists()) {
            try {
                Log.i(TAG, "File created ");
                logFile.createNewFile();
            } catch (IOException e) {
                // Log.e(TAG, e.getLocalizedMessage());
            }
        }
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.US);
            String currentDateandTime = sdf.format(new Date());

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

            buf.write(currentDateandTime + "   " + tag + "   " + message + "\r\n");
//            buf.append(message);
            buf.newLine();
            buf.flush();
            buf.close();
        } catch (IOException e) {
            // Log.e(TAG, e.getLocalizedMessage());
        }

    }
}