package ai.tech5.tech5.idcaptureidcard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

import androidx.camera.core.impl.utils.Exif;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Utility {


    public static void writeToFile(byte[] data, String path) {
        try {


            File myFile = new File(path);


            if (myFile.exists()) {
                myFile.delete();
            }
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            fOut.write(data);

            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean cropImage(File sourceFile, File destFile) {


        try {

            @SuppressLint("RestrictedApi") Exif exif = Exif.createFromFile(sourceFile);
            @SuppressLint("RestrictedApi") int rotation = exif.getRotation();

            Log.d("TAG", "size rotation " + rotation);

            Bitmap source = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());

            Matrix mat = new Matrix();
            mat.postRotate(rotation);

            Bitmap rotated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), mat, true);


            Log.d("TAG", "size of image  " + rotated.getWidth() + " height " + rotated.getHeight());

            Point centerOfCanvas = new Point(rotated.getWidth() / 2, rotated.getHeight() / 2);

            Log.d("TAG", "size centre of canvas " + centerOfCanvas);

            int widthOfRect = (int) (rotated.getWidth() * 0.9);
            int heightOfRect = (widthOfRect / 3) * 2;

            Log.d("TAG", "size of rect  " + widthOfRect + " height " + heightOfRect);


            int startX = (int) (centerOfCanvas.x - (widthOfRect / 2));
            int startY = (int) (centerOfCanvas.y - (heightOfRect / 2));


            Log.d("TAG", "size startX  " + startX + " startY " + startY);

            Bitmap cropped = Bitmap.createBitmap(rotated, startX, startY, widthOfRect, heightOfRect);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            cropped.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            cropped.recycle();

            Utility.writeToFile(byteArray, destFile.getAbsolutePath());

            source.recycle();
            rotated.recycle();


            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
