package ai.tech5.tech5.mvp;

import android.content.Context;
import android.net.Uri;

public interface OnPresenter {

    void setView(Context context, OnView view);

    void enrolldetails(Uri capturedfaceImage, Uri Idcardfaceimage,String name, String Dateofbirth,String country,String mail,String gender, String bloodgroup);

    void authenticate(String patientid, Uri faceimage);

    void destroy();

   // void identifyFingerSearch1(HashMap<Integer, String> fingers, float fingerThreshold, int maxResults);


//    void doLogin(String name, String passwd, String requesttype);
//
//    void doCreateNewUserManager(String name, String email, String Phnnumber, String authority, String requesttype);
//
//    void doUserList(String requesttype);
//
//    void doChangePassword(String name,String oldpassword, String newpassword, String requesttype);
//
//    void doDeleteuser(String name,String usertoken,String requesttype);

}
