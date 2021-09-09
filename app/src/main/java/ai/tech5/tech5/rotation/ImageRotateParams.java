package ai.tech5.tech5.rotation;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naidu.galla on 05/04/2017.
 */

public class ImageRotateParams {


    @SerializedName("angle")
    private int angle;
    @SerializedName("imageUri")
    private Uri imageUri;

    public ImageRotateParams(int angle, Uri imageUri) {
        this.angle = angle;
        this.imageUri = imageUri;

    }

    public int getAngle() {
        return angle;
    }

    public Uri getImageUri() {
        return
                imageUri;
    }
}
