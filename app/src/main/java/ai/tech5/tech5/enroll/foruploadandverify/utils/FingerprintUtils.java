package ai.tech5.tech5.enroll.foruploadandverify.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FingerprintUtils {
    public static Context context;

//    public  FingerprintUtils(){
//      }
    public FingerprintUtils(Context context){
        this.context=context;
    }
    public static List<String> getFileNamesList() {
        List<String> fileNamesList = new ArrayList<>();
        String path = getDirectory();
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileNamesList.add(file.getName());
            }
        }
        return fileNamesList;
    }

    public static List<String> getNewCreatedFiles(List<String> oldFilesList, List<String> newFilesList) {
        newFilesList.removeAll(oldFilesList);
        return newFilesList;
    }

    public static String getDirectory() {
        return Environment.getExternalStorageDirectory().toString() + "/T5BBC/FINGER_CAPTURED";
    }

//    public static void renameFileNames(List<String> renameFileList) {
//        int time = 1000;
//        for (final String fileName : renameFileList) {
//            final Handler handler = new Handler(Looper.getMainLooper());
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        File file = new File(getDirectory() + "/" + fileName);
//                        File file2 = new File(getDirectory() + "/" + Constant.USER_NAME + "_" + getDateTime() + ".jpg");
//                        boolean success = file.renameTo(file2);
//                        Log.d("RENAME STATUS :- ", "" + success);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, time);
//            time += 1000;
//        }
//    }

//
    public static boolean isLicenseFileExists() {
        FileOutputStream outPut = null;
        try {

            File userDIR = createExternalDirectory("finger_sdk");
            if (!userDIR.exists()) {

                userDIR.mkdirs();
            }

            File file = new File(userDIR, "tech5_client_lic");
            return file.exists();

        } catch (Exception e) {

        }

        return false;
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

//    public static  File createExternalDirectory(String fileName) {
//        //create folder
//        File file = new File(Environment.getExternalStorageDirectory(), "11zon");
//        if (!file.mkdirs()) {
//            file.mkdirs();
//        }
//        String filePath = file.getAbsolutePath() + File.separator + fileName;
//        return file;
//    }

    public static File createExternalDirectory(String folder) {
//        File myFile=null;
//        try {
//
//            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            // String path = storageDir.getAbsolutePath() + File.separator + filename;
//            String path = storageDir.getAbsolutePath() + File.separator + "Face_Images" + File.separator + folder;
//            System.out.println("request file path.................." + path);
//            myFile = new File(path);
//            if (!myFile.getParentFile().exists()) {
//                myFile.getParentFile().mkdirs();
//            }
////            if (myFile.exists()) {
////                myFile.delete();
////            }
////            myFile.createNewFile();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return myFile;



//        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        // String path = storageDir.getAbsolutePath() + File.separator + filename;
//        String var1 = storageDir.getAbsolutePath() + File.separator + "Face_Images" + File.separator + folder;

//        //String var0 = context.getExternalFilesDir(null)+ File.separator + "LEW";
//        //  String var1 = getAppDirectory(context) + File.separator + folder;
//      //  System.out.println("from ................." + getAppDirectory(context) + File.separator + folder);
   //    String var1 = context.getExternalFilesDir(null) + File.separator + folder;
        String var1 = getAppDirectory() + File.separator + folder;
//
//        ///storage/emulated/0/Android/data/com.tech5.safetynet/files/T5BBC/folder
        System.out.println("createExternalDirectorypath............." + var1);
        File var2 = new File(var1);
        if (!var2.exists()) {
            var2.mkdirs();
        }

        return var2;
    }

    public static String getAppDirectory() {
//        String storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
//        String var0 = storageDir + File.separator + "T5BBC";
        //String var0 = Environment.getExternalStorageDirectory() + File.separator + "T5BBC";
        String var0 = context.getExternalFilesDir(null).getAbsolutePath()+ File.separator + "T5BBC";
//        ///storage/emulated/0/Android/data/com.tech5.safetynet/files/T5BBC
//        String sample = var0 + File.separator + "adgh";
//        System.out.println("sample..........." + sample);

        System.out.println("getAppDirectorypath..........." + var0);
        File var1 = new File(var0);
        if (!var1.exists()) {
            var1.mkdirs();
        }
        System.out.println("var1..........." + var1.toString());

        return var1.toString();
    }


//    public static void saveFinger(byte[] fingerBytes, String dirName, String fileName, boolean isEnroll, int enrollOrVerifyCount) {
//
//        Log.d("saveFinger", "dirname " + dirName + " filename " + fileName + " isEnroll " + isEnroll + " count " + enrollOrVerifyCount);
//
//        new Thread(() -> {
//
//            File userDIR = FingerprintUtils.createExternalDirectory("FINGER_OUTPUT");
//            try {
//                userDIR = new File(userDIR, dirName);
//                userDIR.mkdirs();
//            } catch (Exception e) {
//                logException("TAG", e);
//            }
//            File dir = userDIR;
//
//
//            if (isEnroll) {
//
//                dir = new File(dir, "Enroll_" + enrollOrVerifyCount);
//
//            } else {
//                dir = new File(dir, "verify_" + enrollOrVerifyCount);
//            }
//
//            dir.mkdirs();
//
//
//            File file = new File(dir, fileName);
//            if (file.exists()) {
//                file.delete();
//            }
//            try {
//                FileOutputStream outPut = new FileOutputStream(file);
//                outPut.write(fingerBytes, 0, fingerBytes.length);
//                outPut.close();
//            } catch (Exception e) {
//
//            }
//
//
//        }).start();
//
//
//    }


//    public static void saveFaceTemplate(byte[] template, String name) {
//
//        FileOutputStream outPut = null;
//        try {
//            File userDIR = FingerprintUtils.createExternalDirectory("Face_Templates");
//            File file = new File(userDIR, name);
//            if (file.exists()) {
//                file.delete();
//            }
//
//            outPut = new FileOutputStream(file);
//            outPut.write(template, 0, template.length);
//            outPut.close();
//        } catch (Exception e) {
//
//        } finally {
//
//            if (outPut != null) {
//                try {
//                    outPut.close();
//                } catch (Exception e) {
//
//                }
//            }
//        }
//
//
//    }


//    public static String getFileNamingConvention(SourceHand hand, SourceFinger finger) {
//        if (hand.equals(SourceHand.RIGHT)) {
//            if (finger.equals(SourceFinger.INDEX)) {
//                return SourceHand.RIGHT + "-" + SourceFinger.INDEX + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.MIDDLE)) {
//                return SourceHand.RIGHT + "-" + SourceFinger.MIDDLE + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.RING)) {
//                return SourceHand.RIGHT + "-" + SourceFinger.RING + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.LITTLE)) {
//                return SourceHand.RIGHT + "-" + SourceFinger.LITTLE + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.THUMB)) {
//                return SourceHand.RIGHT + "-" + SourceFinger.THUMB + "-" + getDateTime();
//            }
//        } else if (hand.equals(SourceHand.LEFT)) {
//            if (finger.equals(SourceFinger.INDEX)) {
//                return SourceHand.LEFT + "-" + SourceFinger.INDEX + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.MIDDLE)) {
//                return SourceHand.LEFT + "-" + SourceFinger.MIDDLE + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.RING)) {
//                return SourceHand.LEFT + "-" + SourceFinger.RING + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.LITTLE)) {
//                return SourceHand.LEFT + "-" + SourceFinger.LITTLE + "-" + getDateTime();
//            } else if (finger.equals(SourceFinger.THUMB)) {
//                return SourceHand.LEFT + "-" + SourceFinger.THUMB + "-" + getDateTime();
//            }
//        }
//        return "";
//    }
//
//
//    public static String getFileNamingConvention(int fingerPos) {
//        if (fingerPos == 1) {
//            return SourceHand.RIGHT + "-" + SourceFinger.THUMB + "-" + getDateTime();
//        } else if (fingerPos == 2) {
//            return SourceHand.RIGHT + "-" + SourceFinger.INDEX + "-" + getDateTime();
//        } else if (fingerPos == 3) {
//            return SourceHand.RIGHT + "-" + SourceFinger.MIDDLE + "-" + getDateTime();
//        } else if (fingerPos == 4) {
//            return SourceHand.RIGHT + "-" + SourceFinger.LITTLE + "-" + getDateTime();
//        } else if (fingerPos == 5) {
//            return SourceHand.RIGHT + "-" + SourceFinger.RING + "-" + getDateTime();
//        } else if (fingerPos == 6) {
//            return SourceHand.LEFT + "-" + SourceFinger.THUMB + "-" + getDateTime();
//        } else if (fingerPos == 7) {
//            return SourceHand.LEFT + "-" + SourceFinger.INDEX + "-" + getDateTime();
//        } else if (fingerPos == 8) {
//            return SourceHand.LEFT + "-" + SourceFinger.MIDDLE + "-" + getDateTime();
//        } else if (fingerPos == 9) {
//            return SourceHand.LEFT + "-" + SourceFinger.LITTLE + "-" + getDateTime();
//        } else if (fingerPos == 10) {
//            return SourceHand.LEFT + "-" + SourceFinger.RING + "-" + getDateTime();
//        }
//        return "";
//    }
//
//    public static int getFingerPosition(CapturedFingerprint fingerprint) {
//
//
//        if (fingerprint.getSourceHand().equals(SourceHand.RIGHT)) {
//            if (fingerprint.getSourceFinger().equals(SourceFinger.THUMB)) {
//                return 1;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.INDEX)) {
//                return 2;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.MIDDLE)) {
//                return 3;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.RING)) {
//                return 4;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.LITTLE)) {
//                return 5;
//            }
//        } else if (fingerprint.getSourceHand().equals(SourceHand.LEFT)) {
//            if (fingerprint.getSourceFinger().equals(SourceFinger.THUMB)) {
//                return 6;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.INDEX)) {
//
//                return 7;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.MIDDLE)) {
//                return 8;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.RING)) {
//                return 9;
//            } else if (fingerprint.getSourceFinger().equals(SourceFinger.LITTLE)) {
//                return 10;
//            }
//        }
//
//
//        return 0;
//    }


    public static String getFingerPositionString(int fingerPos) {


        if (fingerPos == 1) {
            return "r1";
        } else if (fingerPos == 2) {
            return "r2";
        } else if (fingerPos == 3) {
            return "r3";
        } else if (fingerPos == 4) {
            return "r4";
        } else if (fingerPos == 5) {
            return "r5";
        } else if (fingerPos == 6) {
            return "l1";
        } else if (fingerPos == 7) {
            return "l2";
        } else if (fingerPos == 8) {
            return "l3";
        } else if (fingerPos == 9) {
            return "l4";
        } else if (fingerPos == 10) {
            return "l5";
        }
        return "";


    }

    public static void saveConfigFile(byte[] configFile) {

        FileOutputStream outPut = null;
        try {

            File userDIR = createExternalDirectory("Config");
            if (!userDIR.exists()) {

                userDIR.mkdirs();
            }

            File file = new File(userDIR, "working_copy.json");
            if (file.exists()) {
                file.delete();
            }
            outPut = new FileOutputStream(file);
            outPut.write(configFile);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outPut != null) {
                try {
                    outPut.close();
                } catch (Exception e) {

                }
            }
        }

    }

    public static void saveLicenseFile(byte[] configFile) {

        FileOutputStream outPut = null;
        try {

            File userDIR = createExternalDirectory("finger_sdk");
            if (!userDIR.exists()) {

                userDIR.mkdirs();
            }

            File file = new File(userDIR, "tech5_client_lic");
            if (file.exists()) {
                file.delete();
            }
            outPut = new FileOutputStream(file);
            outPut.write(configFile);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outPut != null) {
                try {
                    outPut.close();
                } catch (Exception e) {

                }
            }
        }

    }

    public static boolean isConfigFileExists() {
        FileOutputStream outPut = null;
        try {

            File userDIR = createExternalDirectory("Config");
            if (!userDIR.exists()) {

                userDIR.mkdirs();
            }

            File file = new File(userDIR, "working_copy.json");
            return file.exists();

        } catch (Exception e) {

        }

        return false;
    }

    public  String getConfigurationFilePath() {
        FileOutputStream outPut = null;
        try {

            File userDIR = createExternalDirectory("Config");
            if (!userDIR.exists()) {

                userDIR.mkdirs();
            }

            File file = new File(userDIR, "working_copy.json");
            return file.getAbsolutePath();

        } catch (Exception e) {

        }

        return null;
    }


    public  String getFingerLicenseFilePath() {
        FileOutputStream outPut = null;
        try {

            File userDIR = createExternalDirectory("finger_sdk");
            if (!userDIR.exists()) {

                userDIR.mkdirs();
            }

            File file = new File(userDIR, "tech5_client_lic_old");
            return file.getAbsolutePath();

        } catch (Exception e) {

        }

        return null;
    }
}
