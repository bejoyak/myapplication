package ai.tech5.tech5.rotation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import java.io.File;

import ai.tech5.tech5.utils.Utility;


public class ImageRotator {

    private static final String TAG = ImageRotator.class.getSimpleName();

    private int MAX_IMAGE_SIZE_WIDTH = 800;
    private int MAX_IMAGE_SIZE_HEIGHT = 800;

    public ImageRotator() {

    }

    private Bitmap rotateBitmap(Bitmap source, float angle) throws Exception {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
//        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public String rotateImage(Context context, ImageRotateParams params) {


        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), params.getImageUri());


            Log.d(TAG,"bmp size before scaling "+bitmap.getWidth()+"X"+bitmap.getHeight());
            try {

                bitmap = Utility.scaleDown(bitmap, MAX_IMAGE_SIZE_WIDTH, MAX_IMAGE_SIZE_HEIGHT, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG,"bmp size after scaling "+bitmap.getWidth()+"X"+bitmap.getHeight());


            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(
                    "rotated" + System.currentTimeMillis(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            Bitmap bmp = rotateBitmap(bitmap, params.getAngle());

            Log.d(TAG,"bmp size after rotate "+bmp.getWidth()+"X"+bmp.getHeight());

            if (bmp != null) {
                String path1 = Utility.writeToFile(bmp, image.getAbsolutePath());
                return path1;
            }

        } catch (Exception e) {

        }
        return null;
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


}