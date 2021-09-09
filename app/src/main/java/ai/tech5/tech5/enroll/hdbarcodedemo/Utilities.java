package ai.tech5.tech5.enroll.hdbarcodedemo;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.foruploadandverify.utils.FingerprintUtils;
import ai.tech5.tech5.enroll.model.Configuration;
import ai.tech5.tech5.enroll.utils.AppUtils;
import ai.tech5.tech5.enroll.utils.Logger;

import static ai.tech5.tech5.enroll.utils.Logger.logException;


public class Utilities {
    //private ProgressDialog progressDialog;
    private static Activity parentActivity;
    public static final String TAG =Utilities.class.getSimpleName();

    private FingerprintUtils fingerprintUtils;

    public Utilities(Activity parentActivity) {
        this.parentActivity = parentActivity;
//        progressDialog = new ProgressDialog(parentActivity);
//        progressDialog.setMessage(parentActivity.getResources().getString(R.string.please_wait));
//        progressDialog.setCancelable(false);

        fingerprintUtils = new FingerprintUtils(parentActivity);
    }

    public static String writeToFile(Bitmap bm, String path) {
        FileOutputStream fOut = null;

        try {
            File myFile = new File(path);

            if (myFile.exists()) {
                myFile.delete();
            }
            myFile.createNewFile();
            fOut = new FileOutputStream(myFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();

            return path;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException ie) {

            }
        }

        return null;
    }


//    private static void resize(InputStream input, Path target,
//                               int width, int height) throws IOException {
//
//        // read an image to BufferedImage for processing
//        BufferedImage originalImage = ImageIO.read(input);
//
//        // create a new BufferedImage for drawing
//        BufferedImage newResizedImage
//                = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = newResizedImage.createGraphics();
//
//        //g.setBackground(Color.WHITE);
//        //g.setPaint(Color.WHITE);
//
//        // background transparent
//        g.setComposite(AlphaComposite.Src);
//        g.fillRect(0, 0, width, height);
//
//        /* try addRenderingHints()
//        // VALUE_RENDER_DEFAULT = good tradeoff of performance vs quality
//        // VALUE_RENDER_SPEED   = prefer speed
//        // VALUE_RENDER_QUALITY = prefer quality
//        g.setRenderingHint(RenderingHints.KEY_RENDERING,
//                              RenderingHints.VALUE_RENDER_QUALITY);
//
//        // controls how image pixels are filtered or resampled
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                              RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//
//        // antialiasing, on
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                              RenderingHints.VALUE_ANTIALIAS_ON);*/
//
//        Map<RenderingHints.Key,Object> hints = new HashMap<>();
//        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.addRenderingHints(hints);
//
//        // puts the original image into the newResizedImage
//        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
//        g.dispose();
//
//        // get file extension
//        String s = target.getFileName().toString();
//        String fileExtension = s.substring(s.lastIndexOf(".") + 1);
//
//        // we want image in png format
//        ImageIO.write(newResizedImage, fileExtension, target.toFile());
//
//    }

    /**
     *
     * @param srcBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap srcBitmap, int newWidth, int newHeight)
    {
        try
        {
            return Bitmap.createScaledBitmap(srcBitmap, newWidth, newHeight, true);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }
    public static Bitmap getBitmap(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math
                .abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);
        return output;
    }
//    public Bitmap getBitmap(String path) {
//        Bitmap bitmap=null;
//        try {
//            File f= new File(path);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
////            image.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap ;
//    }
    /**
     *
     * @param path
     * @param sampleSize 1 = 100%, 2 = 50%(1/2), 4 = 25%(1/4), ...
     * @return
     */
    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize)
    {
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, options);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //Image resolution is based on 480x800
        float hh = 800f;//The height is set as 800f here
        float ww = 480f;//Set the width here to 480f
        //Zoom ratio. Because it is a fixed scale, only one data of height or width is used for calculation
        int be = 1;//be=1 means no scaling
        if (originalWidth > originalHeight && originalWidth > ww) {//If the width is large, scale according to the fixed size of the width
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//If the height is high, scale according to the fixed size of the width
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //Proportional compression
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//Set scaling
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//Mass compression again
    }
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Quality compression method, here 100 means no compression, store the compressed data in the BIOS
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //Cycle to determine if the compressed image is greater than 100kb, greater than continue compression
            baos.reset();//Reset the BIOS to clear it
            //First parameter: picture format, second parameter: picture quality, 100 is the highest, 0 is the worst, third parameter: save the compressed data stream
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//Here, the compression options are used to store the compressed data in the BIOS
            options -= 10;//10 less each time
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//Store the compressed data in ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//Generate image from ByteArrayInputStream data
        return bitmap;
    }
    public LinkedHashMap<String, String> unzip(String zipFilePath, String destDir) {

        LinkedHashMap<String, String> fileList = new LinkedHashMap<>();
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                Logger.addToLog(TAG, "Unzipping to " + newFile.getAbsolutePath());

                System.err.println("&&&&" + newFile.getName());
                Logger.addToLog(TAG, "&&&&" + newFile.getName());
                fileList.put(newFile.getName(), newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            logException(TAG, e);
        }
        return fileList;
    }

    public static byte[] readFileBytes(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            logException(TAG, e);
        } catch (IOException e) {
            logException(TAG, e);
        }
        return bytes;
    }

//    public void showProgressDialog(boolean flag) {
//        if (flag) {
//            progressDialog.show();
//        } else {
//            progressDialog.dismiss();
//        }
//
//    }

    public byte[] getFileBytes(Activity parentActivity, int fileName) {
        byte[] buffer = null;
        try {
            InputStream inputStream = parentActivity.getResources().openRawResource(fileName);
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
        } catch (IOException e) {
            logException(TAG, e);
        }
        return buffer;
    }

    public String getDeviceManufacturerName() {
        return Build.MANUFACTURER;
    }

    public byte[] processImage(byte[] faceBytes, int cameraFacing) {


        byte[] processedBytes = null;
        try {
            if (getDeviceManufacturerName().equalsIgnoreCase(Constants.SAMSUNG_DEVICE)) {
                Bitmap capturedBitmap = BitmapFactory.decodeByteArray(faceBytes, 0, faceBytes.length);
                Matrix mat = new Matrix();
                if (cameraFacing == 0) {
                    LogUtils.debug(TAG, "Back Facing Camera....");
                    Logger.addToLog(TAG, "Back Facing Camera....");
                    mat.postRotate(90);// for back facing camera
                } else {
                    LogUtils.debug(TAG, "Front Facing Camera....");
                    Logger.addToLog(TAG, "Front Facing Camera....");
                    mat.postRotate(270);// for front facing camera
                }
                Bitmap bmpRotate = Bitmap.createBitmap(capturedBitmap, 0, 0, capturedBitmap.getWidth(), capturedBitmap.getHeight(), mat, true);
                final ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                bmpRotate.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                processedBytes = stream2.toByteArray();
            } else {
                if (cameraFacing == 0) {
                    Bitmap capturedBitmap = BitmapFactory.decodeByteArray(faceBytes, 0, faceBytes.length);
                    Matrix mat = new Matrix();
                    LogUtils.debug(TAG, "Back Facing Camera....");
                    Logger.addToLog(TAG, "Back Facing Camera....");
                    mat.postRotate(180);// for back facing camera
                    Bitmap bmpRotate = Bitmap.createBitmap(capturedBitmap, 0, 0, capturedBitmap.getWidth(), capturedBitmap.getHeight(), mat, true);
                    final ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bmpRotate.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                    processedBytes = stream2.toByteArray();
                } else {
                    return faceBytes;
                }
            }
        } catch (Exception e) {
            logException(TAG, e);
            LogUtils.debug(TAG, "Exception while detecting faces in face detection task :: " + e.getMessage());
            Logger.addToLog(TAG, "Exception while detecting faces in face detection task :: " + e.getMessage());
        }
        return processedBytes;
    }

    public File getFilePath(Intent data) {

        String actualfilepath = "";
        String fullerror = "";
        File myFile = null;
        try {
            Uri imageuri = data.getData();
            InputStream stream = null;
            String tempID = "", id = "";
            Uri uri = data.getData();
            Log.e(TAG, "file auth is " + uri.getAuthority());
            Logger.addToLog(TAG, "file auth is " + uri.getAuthority());
            fullerror = fullerror + "file auth is " + uri.getAuthority();
            if (imageuri.getAuthority().equals("media")) {
                tempID = imageuri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/") + 1);
                id = tempID;
                Uri contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String selector = MediaStore.Images.Media._ID + "=?";
                actualfilepath = getColunmData(contenturi, selector, new String[]{id});
            } else if (imageuri.getAuthority().equals("com.android.providers.media.documents")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    tempID = DocumentsContract.getDocumentId(imageuri);
                }
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                Uri contenturi = null;
                if (type.equals("image")) {
                    contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (type.equals("video")) {
                    contenturi = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (type.equals("audio")) {
                    contenturi = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selector = "_id=?";
                actualfilepath = getColunmData(contenturi, selector, new String[]{id});
            } else if (imageuri.getAuthority().equals("com.android.providers.downloads.documents")) {
                tempID = imageuri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/") + 1);
                id = tempID;
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                // String selector = MediaStore.Images.Media._ID+"=?";
                actualfilepath = getColunmData(contenturi, null, null);
            } else if (imageuri.getAuthority().equals("com.android.externalstorage.documents")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    tempID = DocumentsContract.getDocumentId(imageuri);
                }
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                Uri contenturi = null;
                if (type.equals("primary")) {
                    actualfilepath = Environment.getExternalStorageDirectory() + "/" + id;
                }
            }
            myFile = new File(actualfilepath);
            // MessageDialog dialog = new MessageDialog(HomeActivity.this, " file details --"+actualfilepath+"\n---"+ uri.getPath() );
            // dialog.displayMessageShow();
            String temppath = uri.getPath();
            if (temppath.contains("//")) {
                temppath = temppath.substring(temppath.indexOf("//") + 1);
            }
            Log.e(TAG, " temppath is " + temppath);
            fullerror = fullerror + "\n" + " file details -  " + actualfilepath + "\n --" + uri.getPath() + "\n--" + temppath;
            if (actualfilepath.equals("") || actualfilepath.equals(" ")) {
                myFile = new File(temppath);
            } else {
                myFile = new File(actualfilepath);
            }
            //File file = new File(actualfilepath);
            //Log.e(TAG, " actual file path is "+ actualfilepath + "  name ---"+ file.getName());
//                    File myFile = new File(actualfilepath);
            Log.e(TAG, " myfile is " + myFile.getAbsolutePath());
            Logger.addToLog(TAG, " myfile is " + myFile.getAbsolutePath());

            // lyf path  - /storage/emulated/0/kolektap/04-06-2018_Admin_1528088466207_file.xls
        } catch (Exception e) {
            logException(TAG, e);
            Log.e(TAG, " read errro " + e.toString());
        }
        return myFile;
    }

    public String getColunmData(Uri uri, String selection, String[] selectarg) {

        String filepath = "";
        Cursor cursor = null;
        String colunm = "_data";
        String[] projection = {colunm};
        cursor = parentActivity.getContentResolver().query(uri, projection, selection, selectarg, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Log.e(TAG, " file path is " + cursor.getString(cursor.getColumnIndex(colunm)));
            Logger.addToLog(TAG, " file path is " + cursor.getString(cursor.getColumnIndex(colunm)));
            filepath = cursor.getString(cursor.getColumnIndex(colunm));
        }
        if (cursor != null)
            cursor.close();
        return filepath;
    }

    public void unzipNew(InputStream inputStream, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();//////////////////**check
        }
        ZipInputStream zipIn = new ZipInputStream(inputStream);
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();//////////////////*check
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {

        Log.d("TAG Filepath...",filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public void log() {
        try {
//            createT5Folder();

            //        File fileName = new File(FingerprintUtils.createExternalDirectory("Logs") + File.separator + "T5HDBarcodeLogger.log");

            String storageDir = fingerprintUtils.createExternalDirectory("Logs").getAbsolutePath();

            System.out.println("path................" + storageDir);
//
//            String userDIR = storageDir + File.separator + "Safetynet" + File.separator + "Logs"+File.separator+"T5HDBarcodeLogger.log";
            String userDIR = storageDir + File.separator + "T5HDBarcodeLogger.log";

            System.out.println("path......................" + userDIR);
            File var2 = new File(userDIR);
            if (!var2.exists()) {
                var2.mkdirs();
            }
//
//            String parent = fileName.getParent();
//            File parentFile = new File(parent);
//
//            if (!parentFile.exists()) {
//                parentFile.mkdirs();
//            }
//
//
//            fileName.createNewFile();
//            String cmd = "logcat -d -f " + fileName.getAbsolutePath();
            String cmd = "logcat -d -f " + userDIR;
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            logException(TAG, e);
        }
    }

    public void createFolder() {
        try {
            String appName = parentActivity.getResources().getString(R.string.folder_name);
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception e) {
            logException(TAG, e);
        }
    }

    public void createT5Folder() {
        try {
            String appName = "T5BBC/Logs";
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception e) {
            logException(TAG, e);
        }
    }

//    public void deleteFolder() {
//        String appName = parentActivity.getResources().getString(R.string.folder_name);
//        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + appName);
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                new File(dir, children[i]).delete();
//            }
//        }
//    }

    public String writeZipToFile(byte data[], int skip, int fileSize, File pathToTempFiles) {
        try {
            /* Save the embedded file */
            System.out.println("############ unZIP ############");


            String fileName = "Embedded.zip";
            File zipFile = new File(pathToTempFiles, fileName);
            FileOutputStream outputStream = new FileOutputStream(zipFile);
            outputStream.write(data, skip, fileSize);
            outputStream.close();
            System.err.println("pathToTempFiles:::" + pathToTempFiles);
            return "Embedded.zip";
        } catch (Exception e) {
            logException(TAG, e);
            Log.e("HDBarcode", "HDBarcode writeZipToFile:Exception" + e.getMessage());
            return (null);
        }
    }

    public void writeZipToFile2(byte data[], int skip, int fileSize, File pathToTempFiles) {
        try {

            /* Save the embedded file */
            System.out.println("############ unZIP ############");
            Logger.addToLog(TAG, "############ unZIP ############");
            String fileName = "EmbeddedA.dat";
            File zipFile = new File(pathToTempFiles, fileName);
            FileOutputStream outputStream = new FileOutputStream(zipFile);
            outputStream.write(data, 1, fileSize - 1);
            outputStream.close();
            System.err.println("pathToTempFiles:::" + pathToTempFiles);
            Logger.addToLog(TAG, "pathToTempFiles:::" + pathToTempFiles);
        } catch (Exception e) {
            logException(TAG, e);
            Log.e("HDBarcode", "HDBarcode writeZipToFile:Exception" + e.getMessage());
        }
    }
//resultactivity,newscanbarcodeactivity,scanhdbarcodeactivity
//    public int getCurrentBrightness() {
//        int brightness = -1;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(parentActivity)) {
//                logger = new Logger(parentActivity);
//
//                ContentResolver cResolver = parentActivity.getContentResolver();
//                try {
//                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//                    brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
//
//                    System.err.println("Current Brightness :::" + brightness);
//                    Logger.addToLog(TAG, "Current Brightness :::" + brightness);
//                } catch (Exception e) {
//                    Log.e("Error", "Cannot access system brightness");
//                    Logger.addToLog(TAG, "Cannot access system brightness");
//                    logException(TAG, e);
//                }
//            } else {
//                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + parentActivity.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                parentActivity.startActivityForResult(intent, 2442);
//            }
//        }
//        return brightness;
//    }
//newscanbarcodeactivity,scanhdbarcodeactivity,decdehdbarcodeactivity,resultactivity
//    public boolean setScreenBrightness(int brightness) {
//        boolean flag = false;
//        try {
//            Window window = parentActivity.getWindow();
//            ContentResolver cResolver = parentActivity.getContentResolver();
//            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
//            WindowManager.LayoutParams layoutParams = window.getAttributes();
//            layoutParams.screenBrightness = brightness / (float) 255;
//            window.setAttributes(layoutParams);
//            flag = true;
//        } catch (Exception e) {
//            logException(TAG, e);
//            flag = false;
//        }
//        return flag;
//    }

    public String getIssueDate(String issueDate) {


        String formattedDate = "";
        if (issueDate == null || issueDate.isEmpty()) {
            Logger.addToLog(TAG, "----Input issue date is empty or null---- ");
            return "";
        }
        try {
            Logger.addToLog(TAG, "----Input issue date---- " + issueDate);
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.US);
            Date formattedIssueDate = dateFormat.parse(issueDate);
            Format f = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            formattedDate = f.format(formattedIssueDate);
            Logger.addToLog(TAG, "----formatted issue date---- " + formattedDate);
        } catch (ParseException e) {
            logException(TAG, e);
        }
        return formattedDate;
    }

    public String getExpiryDate(String issueDate, String numberOfDays) {


        String expiryDate = "";
        try {
            Logger.addToLog(TAG, "----expiryDate Input issue date---- " + issueDate + "---Number of days---" + numberOfDays);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            Date test = sdf.parse(issueDate);
            Calendar c = Calendar.getInstance();
            c.setTime(test);
            c.add(Calendar.DATE, Integer.parseInt(numberOfDays));

            expiryDate = sdf.format(c.getTime());
            Logger.addToLog(TAG, "----expiryDate---- " + expiryDate);
        } catch (ParseException e) {
            logException(TAG, e);
        }
        return expiryDate;
    }

    public boolean isBarcodeValid(String issueDate, String numberOfDays) {


        //for now we are allowing all users to use barcode, Please remove for release
        boolean isBarcodeValid = true;
        if (issueDate == null || issueDate.isEmpty()) {
            Logger.addToLog(TAG, "----isBarcodeValid Input issue date is empty or null---- ");
            return isBarcodeValid;
        }

        if (numberOfDays == null || numberOfDays.isEmpty()) {
            Logger.addToLog(TAG, "----isBarcodeValid Input issue validity days is empty or null---- ");
            return isBarcodeValid;
        }
        try {
            Logger.addToLog(TAG, "----isBarcodeValid Input issue date---- " + issueDate + "---Number of days---" + numberOfDays);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            Date test = sdf.parse(issueDate);
            Calendar c = Calendar.getInstance();
            c.setTime(test);
            c.add(Calendar.DATE, Integer.parseInt(numberOfDays));
            Date today = new Date();
            if (today.before(c.getTime())) {
                System.err.println("Valid HD Barcode");
                Logger.addToLog(TAG, "Valid HD Barcode");
                isBarcodeValid = true;
            } else {
                System.err.println("HD Barcode Expired! please contact tech5.ai");
                Logger.addToLog(TAG, "HD Barcode Expired! please contact tech5.ai");
                isBarcodeValid = false;
            }
        } catch (ParseException e) {
            logException(TAG, e);
        }
        return isBarcodeValid;
    }

    public void playBeep() {
        LogUtils.debug(TAG, "playing beep");
//        MediaPlayer mp = MediaPlayer.create(parentActivity, R.raw.beep);
//        mp.start();
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, 100);
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_S, 100);
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                toneGenerator.release();
            }
        }, 150);


    }

//    public void writeToFile(byte[] capturedBytes, String fileName) {
//        try {
//            String appName = parentActivity.getResources().getString(R.string.folder_name);
//            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + appName);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//            FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + File.separator + fileName);
//            fos.write(capturedBytes);
//            fos.close();
//        } catch (Exception e) {
//            logException(TAG, e);
//        }
//    }

    public static byte[] getBytesFromImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            return imageBytes;
        } catch (IOException e) {
            logException(TAG, e);
            return null;
        }
    }

    public boolean loadBinFiles() {
        String destDir = fingerprintUtils.getAppDirectory();
        File f = new File(destDir + File.separator + "face_sdk");
        if (!f.exists()) {
            try {
                InputStream inputStream = parentActivity.getAssets().open("face_sdk.zip");
                String faceSdkLocation = fingerprintUtils.getAppDirectory();
                System.out.println("faceSdkLocation..........." + faceSdkLocation);

                unzipNew(inputStream, faceSdkLocation);
                return true;
            } catch (Exception e) {
                logException(TAG, e);
                return false;
            }
        }
        return false;
    }


//    public boolean loadBinFiles() {
//        String destDir = fingerprintUtils.getAppDirectory();
//        System.out.println("destDir.........."+destDir);
////        File f = null;
////        try {
//            File f1 = new File(destDir + File.separator + "face_sdk");
//            System.out.println("facesdk.........."+f1.getAbsolutePath());
////            if (!f1.exists()) {
////                f1.mkdirs();
////            }
////            System.out.println("facesdk1111..........." + f1.toString());
//            if (!f1.exists()) {
//                try {
//                    InputStream inputStream = parentActivity.getAssets().open("face_sdk.zip");
//                    String faceSdkLocation = fingerprintUtils.getAppDirectory();
//                    System.out.println("faceSdkLocation..........." + faceSdkLocation);
//                    unzipNew(inputStream, faceSdkLocation);
//                    return true;
//                } catch (Exception e) {
//                    logException(TAG, e);
//                    e.printStackTrace();
//                    return false;
//                }
//            }
////        }catch (Exception e){
////
////        }
//
//
//        return false;
//    }


//
//    public boolean loadBinFiles() {
//        // String destDir = "/storage/emulated/0/T5BBC/face_sdk";
//
//        String destDir = "/storage/emulated/0/Android/data/ai.tech5.vido/files/T5BBC/face_sdk";
//        ////storage/emulated/0/Android/data/com.moha.enroll.digitalid/files/Pictures/BBC_CODES/Divya_2021_02_23_20_02_24.png
//        File f = new File(destDir);
//        if (!f.exists()) {
//            try {
//                InputStream inputStream = parentActivity.getAssets().open("face_sdk.zip");
//                String faceSdkLocation = "/storage/emulated/0/Android/data/ai.tech5.vido/files/T5BBC";
//
//                //                String faceSdkLocation = "/storage/emulated/0/Android/data/com.tech5.safetynet/files/Pictures/T5BBC";
//                unzipNew(inputStream, faceSdkLocation);
//                return true;
//            } catch (Exception e) {
//                logException(TAG, e);
//                return false;
//            }
//        }
//        return false;
//    }


//    public boolean loadBinFiles() {
//        String destDir = "/storage/emulated/0/T5BBC/face_sdk";
//        File f = new File(destDir);
//        if (!f.exists()) {
//            try {
//                InputStream inputStream = parentActivity.getAssets().open("face_sdk.zip");
//                String faceSdkLocation = "/storage/emulated/0/T5BBC";
//                unzipNew(inputStream, faceSdkLocation);
//                return true;
//            } catch (Exception e) {
//                logException(TAG, e);
//                return false;
//            }
//        }
//        return false;
//    }

    public boolean deleteSDKDir(String dirName) {
        String storageDir = parentActivity.getExternalFilesDir(null).getAbsolutePath();

//                    String storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
//        String destDir = Environment.getExternalStorageDirectory() + "/" + dirName;
        String destDir = storageDir + "/" + dirName;
        File dir = new File(destDir);
        if (dir.exists()) {
            try {
                return AppUtils.deleteFileOrDirectory(dir);
            } catch (Exception e) {
                logException(TAG, e);
                return false;
            }
        }
        return false;
    }


    public static String getSystemArchitecture() {

        String arch = System.getProperty("os.arch");

        return arch;
    }


    public static byte[] upscaleImageToDouble(byte[] image) {


        // Get the dimensions of the View

//        Logger.addToLog(TAG, "upscaleImageToDouble() called");
        Logger.addToLog(TAG, "upscaleImageToDouble() called");

        Bitmap bitmap = null;
        try {

//            // Get the dimensions of the bitmap
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inJustDecodeBounds = true;
//            Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length, bmOptions);
//            int photoW = bmOptions.outWidth;
//            int photoH = bmOptions.outHeight;
//
//
//
//
//            // Determine how much to scale down the image
//             int scaleFactor = Math.min(photoW / (photoW * 2), photoH / (photoH * 2));
//
//            //  Log.d(TAG, "scaleFactor  " + scaleFactor);
//
//            // Decode the image file into a Bitmap sized to fill the View
//            bmOptions.inJustDecodeBounds = false;
//            bmOptions.inSampleSize = scaleFactor;
//            bmOptions.inPurgeable = true;
//
//            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, bmOptions);
//
//            BitmapFactory.cr
//
//            Log.d(TAG, "image dims before upscale " + photoW + "x" + photoH + " after " + bitmap.getWidth() + "X" + bitmap.getHeight());

            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            Log.d(TAG, "image dims before upscale " + bitmap.getWidth() + "x" + bitmap.getHeight());

            bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 2, bitmap.getHeight() * 2, false);


            Log.d(TAG, "image dims after upscale " + bitmap.getWidth() + "x" + bitmap.getHeight());

            return getBytesFromImage(bitmap);


        } catch (Exception e) {
            e.printStackTrace();

            logException(TAG, e);
        }
        return null;

    }

    public String readFileFromAssets(String fileName) {


        String tContents = null;
        InputStream stream = null;
        try {
            stream = parentActivity.getAssets().open(fileName);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);

            tContents = new String(buffer);
        } catch (IOException e) {

        } finally {
            try {
                stream.close();
            } catch (Exception e) {

            }
        }

        return tContents;


    }

    public static byte[] readFileFromAssetsasBytes(Context context, String fileName) {


        String tContents = null;
        InputStream stream = null;
        try {
            stream = context.getAssets().open(fileName);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);

            return buffer;
        } catch (IOException e) {

        } finally {
            try {
                stream.close();
            } catch (Exception e) {

            }
        }

        return null;


    }

    public byte[] readFileFromAssetsasBytes(String fileName) {


        String tContents = null;
        InputStream stream = null;
        try {
            stream = parentActivity.getAssets().open(fileName);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);

            return buffer;
        } catch (IOException e) {

        } finally {
            try {
                stream.close();
            } catch (Exception e) {

            }
        }

        return null;


    }


    public Configuration loadConfiguration() {
        String configJson = null;


        try {
//            if (fingerprintUtils.isConfigFileExists()) {
//
//                Log.d("TAG", "config file exists " + fingerprintUtils.getConfigurationFilePath());
//
//                configJson = new String(readFileBytes(fingerprintUtils.getConfigurationFilePath()));
//
//
//            } else {

                Log.d("TAG", "config file not exists reading from assets ");

                configJson = readFileFromAssets("working_copy.json");


//            }

            return new Gson().fromJson(configJson, Configuration.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String readFileFromAssets(Context context, String fileName) {


        String tContents = null;
        InputStream stream = null;
        try {
            stream = context.getAssets().open(fileName);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);

            tContents = new String(buffer);
        } catch (IOException e) {

        } finally {
            try {
                stream.close();
            } catch (Exception e) {

            }
        }

        return tContents;


    }


    public static int getCompressionLevel(int faceCompressinSize) {

        int compressionLevel = 1;

        try {

            if (faceCompressinSize == 800) {
                compressionLevel = 1;
            } else if (faceCompressinSize == 900) {
                compressionLevel = 2;
            } else if (faceCompressinSize == 1000) {
                compressionLevel = 3;
            } else if (faceCompressinSize == 1100) {
                compressionLevel = 4;
            } else if (faceCompressinSize == 1200) {
                compressionLevel = 5;
            }

        } catch (Exception e) {

        }

        return compressionLevel;
    }

    public static String getHardwareAndSoftwareInfo() {

        try {

            return "SERIAL:" + " " + Build.SERIAL + "\n" +
                    "MODEL:" + " " + Build.MODEL + "\n" +
                    "ID:" + " " + Build.ID + "\n" +
                    "MANUFACTURER:" + " " + Build.MANUFACTURER + "\n" +
                    "BRAND:" + " " + Build.BRAND + "\n" +
                    "TYPE:" + " " + Build.TYPE + "\n" +
                    "USER:" + " " + Build.USER + "\n" +
                    "BASE:" + " " + Build.VERSION_CODES.BASE + "\n" +
                    "ICREMENTAL:" + " " + Build.VERSION.INCREMENTAL + "\n" +
                    "SDK:" + " " + Build.VERSION.SDK + "\n" +
                    "BOARD:" + " " + Build.BOARD + "\n" +
                    "HOST:" + " " + Build.HOST + "\n" +
                    "FINGERPRINT:" + " " + Build.FINGERPRINT + "\n" +
                    "VERSION CODE:" + " " + Build.VERSION.RELEASE;


        } catch (Exception e) {

        }

        return "";

    }
    //////////////////////////////////////////////////////////////////////////
    private final int BMP_WIDTH_OF_TIMES = 4;
    private final int BYTE_PER_PIXEL = 3;

    /**
     * Android Bitmap Object to Window's v3 24bit Bmp Format File
     * @param orgBitmap
     * @param filePath
     * @return file saved result
     */
    public boolean saveByteBuffer(Bitmap orgBitmap, String filePath){

        if(orgBitmap == null){
            return false;
        }

        if(filePath == null){
            return false;
        }

        boolean isSaveSuccess = true;

        //image size
        int width = orgBitmap.getWidth();
        int height = orgBitmap.getHeight();

        //image dummy data size
        //reason : bmp file's width equals 4's multiple
        int dummySize = 0;
        byte[] dummyBytesPerRow = null;
        boolean hasDummy = false;
        if(isBmpWidth4Times(width)){
            hasDummy = true;
            dummySize = BMP_WIDTH_OF_TIMES - (width % BMP_WIDTH_OF_TIMES);
            dummyBytesPerRow = new byte[dummySize * BYTE_PER_PIXEL];
            for(int i = 0; i < dummyBytesPerRow.length; i++){
                dummyBytesPerRow[i] = (byte)0xFF;
            }
        }

        int[] pixels = new int[width * height];
        int imageSize = pixels.length * BYTE_PER_PIXEL + (height * dummySize * BYTE_PER_PIXEL);
        int imageDataOffset = 0x36;
        int fileSize = imageSize + imageDataOffset;

        //Android Bitmap Image Data
        orgBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        //ByteArrayOutputStream baos = new ByteArrayOutputStream(fileSize);
        ByteBuffer buffer = ByteBuffer.allocate(fileSize);

        try {
            /**
             * BITMAP FILE HEADER Write Start
             */
            buffer.put((byte)0x42);
            buffer.put((byte)0x4D);

            //size
            buffer.put(writeInt(fileSize));

            //reserved
            buffer.put(writeShort((short)0));
            buffer.put(writeShort((short)0));

            //image data start offset
            buffer.put(writeInt(imageDataOffset));

            /** BITMAP FILE HEADER Write End */

            //*******************************************

            /** BITMAP INFO HEADER Write Start */
            //size
            buffer.put(writeInt(0x28));

            //width, height
            buffer.put(writeInt(width));
            buffer.put(writeInt(height));

            //planes
            buffer.put(writeShort((short)1));

            //bit count
            buffer.put(writeShort((short)24));

            //bit compression
            buffer.put(writeInt(0));

            //image data size
            buffer.put(writeInt(imageSize));

            //horizontal resolution in pixels per meter
            buffer.put(writeInt(0));

            //vertical resolution in pixels per meter (unreliable)
            buffer.put(writeInt(0));

            //컬러 사용 유무
            buffer.put(writeInt(0));

            //중요하게 사용하는 색
            buffer.put(writeInt(0));

            /** BITMAP INFO HEADER Write End */

            int row = height;
            int col = width;
            int startPosition = 0;
            int endPosition = 0;

            while( row > 0 ){

                startPosition = (row - 1) * col;
                endPosition = row * col;

                for(int i = startPosition; i < endPosition; i++ ){
                    buffer.put(write24BitForPixcel(pixels[i]));

                    if(hasDummy){
                        if(isBitmapWidthLastPixcel(width, i)){
                            buffer.put(dummyBytesPerRow);
                        }
                    }
                }
                row--;
            }

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(buffer.array());
            fos.close();

        } catch (IOException e1) {
            e1.printStackTrace();
            isSaveSuccess = false;
        }
        finally{

        }

        return isSaveSuccess;
    }

    /**
     * Is last pixel in Android Bitmap width
     * @param width
     * @param i
     * @return
     */
    private boolean isBitmapWidthLastPixcel(int width, int i) {
        return i > 0 && (i % (width - 1)) == 0;
    }

    /**
     * BMP file is a multiples of 4?
     * @param width
     * @return
     */
    private boolean isBmpWidth4Times(int width) {
        return width % BMP_WIDTH_OF_TIMES > 0;
    }

    /**
     * Write integer to little-endian
     * @param value
     * @return
     * @throws IOException
     */
    private byte[] writeInt(int value) throws IOException
    {
        byte[] b = new byte[4];

        b[0] = (byte)(value & 0x000000FF);
        b[1] = (byte)((value & 0x0000FF00) >> 8);
        b[2] = (byte)((value & 0x00FF0000) >> 16);
        b[3] = (byte)((value & 0xFF000000) >> 24);

        return b;
    }

    /**
     * Write integer pixel to little-endian byte array
     * @param value
     * @return
     * @throws IOException
     */
    private byte[] write24BitForPixcel(int value) throws IOException
    {
        byte[] b = new byte[3];

        b[0] = (byte)(value & 0x000000FF);
        b[1] = (byte)((value & 0x0000FF00) >> 8);
        b[2] = (byte)((value & 0x00FF0000) >> 16);

        return b;
    }

    /**
     * Write short to little-endian byte array
     * @param value
     * @return
     * @throws IOException
     */
    private byte[] writeShort(short value) throws IOException
    {
        byte[] b = new byte[2];

        b[0] = (byte)(value & 0x00FF);
        b[1] = (byte)((value & 0xFF00) >> 8);

        return b;
    }
}