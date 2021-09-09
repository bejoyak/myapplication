package ai.tech5.tech5.rotation;


public interface FacerotationView {

    void showProgress();

    void hideProgress();

    void onImageRotated(String path);

}