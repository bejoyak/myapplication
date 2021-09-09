package ai.tech5.tech5.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++)
            {
                System.out.print((char) bFile[i]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }
    public static byte[] readFileToBytes2(String filePath) throws IOException {

        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        // funny, if can use Java 7, please uses Files.readAllBytes(path)
        try(FileInputStream fis = new FileInputStream(file)){
            fis.read(bytes);
        }
return bytes;
    }
    // file to byte[], old and classic way, before Java 7
    public static byte[] readFileToBytes(String filePath) throws IOException {

        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file);

            //read file into bytes[]
            fis.read(bytes);
return bytes;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

    }
    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }


    public void sharePdf(Context context, String title, File pdfFile) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        //   File fileWithinMyDir = new File(pdfPath);

        if (pdfFile.exists()) {
            try {

                String packageName = context.getApplicationContext().getPackageName();

                Uri pdfUri = FileProvider.getUriForFile(context,
                        packageName + ".fileprovider",
                        pdfFile);
                intentShareFile.setType("application/pdf");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, pdfUri);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        title);
                intentShareFile.putExtra(Intent.EXTRA_TEXT, title);

                context.startActivity(Intent.createChooser(intentShareFile, "Share Search results"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void viewPdf(Context context, File pdfFile) {
        // Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        // File fileWithinMyDir = new File(filepath);

        if (pdfFile.exists()) {

            try {

                String packageName = context.getApplicationContext().getPackageName();

                Uri pdfUri = FileProvider.getUriForFile(context,
                        packageName + ".fileprovider",
                        pdfFile);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pdfUri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "pdf app not there ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //alphanumeric only

    private static final String NIK_PASSPORT_PATTERN = "^[a-zA-Z0-9]*$";

    public static boolean isValidalphanumeric(String s) {

        try {

            Pattern pattern = Pattern.compile(NIK_PASSPORT_PATTERN);
            Matcher matcher = pattern.matcher(s);

            return matcher.matches();

        } catch (Exception e) {

        }

        return false;

    }

    public static void hideKeybord(InputMethodManager inputManager, View view) {
        try {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static byte[] readByteArrayFromFile(String urlString) {
        // Working fine
        DataInputStream dis = null;
        byte[] fileData = null;
        try {
            File file = new File(urlString);
            fileData = new byte[(int) file.length()];
            dis = new DataInputStream((new FileInputStream(file)));
            dis.readFully(fileData);
            dis.close();
        } catch (FileNotFoundException e) {

            Log.d("TAG", "key file not present");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return fileData;
    }


    public static String writeToFile(byte[] data, String path) {

        FileOutputStream fOut = null;
        try {
            File myFile = new File(path);

            if (myFile.exists()) {
                myFile.delete();
            }
            myFile.createNewFile();
            fOut = new FileOutputStream(myFile);
            fOut.write(data);

            return myFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
            } catch (IOException ie) {

            }
        }

        return null;
    }


    public String getAppDirectory(Context context) {
        String var0 = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "Tech5";
        File var1 = new File(var0);
        if (!var1.exists()) {
            var1.mkdirs();
        }

        return var1.toString();
    }


    public File createExternalDirectory(Context context, String folder) {
        String var1 = getAppDirectory(context) + "/" + folder;
        File var2 = new File(var1);
        if (!var2.exists()) {
            var2.mkdirs();
        }

        return var2;
    }

    public static byte[] getByteArray(String inputBase64) {
        return Base64.decode(inputBase64, Base64.DEFAULT);
    }

    public static byte[] getBytesinputstream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public int getExifRotation(Context context, Uri uri) {

        // the URI you've received from the other app
        InputStream in = null;
        int rotation = 0;
        try {
            in = context.getContentResolver().openInputStream(uri);
            ExifInterface exifInterface = new ExifInterface(in);

            // Now you can extract any Exif tag you want
            // Assuming the image is a JPEG or supported raw format

            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;

            }
        } catch (IOException e) {
            // Handle any errors
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        return rotation;
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context,img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei = null;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else {
            try {
                String path=getPath(context,selectedImage);
                ei = new ExifInterface(path);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
            

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageWidth, float maxImageHeight,
                                   boolean filter) {


        float ratio = Math.min(
                maxImageWidth / realImage.getWidth(),
                maxImageHeight / realImage.getHeight());

        ///In order to ensure that the image will only be down scaled, detect if the result ratio is less than 1
        if (ratio < 1) {
            int width = Math.round((float) ratio * realImage.getWidth());
            int height = Math.round((float) ratio * realImage.getHeight());

            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                    height, filter);


            return newBitmap;
        }


        return realImage;
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


    public static byte[] bitmapToByetArray(Bitmap bitmap) {

        ByteArrayOutputStream bos = null;

        try {


            bos = new ByteArrayOutputStream();

            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 75, bos);
            }
            byte[] image = bos.toByteArray();


            Log.d("TAG", "image size " + image.length);


            // write the bytes in file
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
