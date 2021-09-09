package ai.tech5.tech5.rotation;

import android.content.Context;

public interface FacerotationPresenter {

    void setView(Context context, FacerotationView view);

    void rotateImage(Context context, ImageRotateParams params);

    void destroy();
}
