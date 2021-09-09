package ai.tech5.tech5.mvp;

import ai.tech5.tech5.models.EnrollResponce;
import ai.tech5.tech5.models.VerifyResponce;

public interface OnView {

    void showProgress(int drawable);

//    void showProgress();

    void hideProgress();

    void onenrollmentrespone(EnrollResponce response);

    void onauthenticateresponce(VerifyResponce responce);

    void onenrollmentFailed(Throwable error);

    void onauthenticationFailed(Throwable error);

//    void onImageRotated(String path);

//     void onLoginResult(UserManagementResponse userManagementResponse);
//
//     void onLogindelete(UserManagementResponse userManagementResponse);

}
