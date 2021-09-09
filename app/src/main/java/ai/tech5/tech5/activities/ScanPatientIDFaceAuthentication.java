package ai.tech5.tech5.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.face.liveness.LivenessController;
import com.face.liveness.T5LivenessListener;
import com.face.liveness.exception.ExceptionUtility;
import com.face.liveness.model.Tech5LivenessRequestModel;
import com.google.android.material.textfield.TextInputEditText;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.File;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.Logger;
import ai.tech5.tech5.models.EnrollResponce;
import ai.tech5.tech5.models.VerifyResponce;
import ai.tech5.tech5.mvp.OnPresenter;
import ai.tech5.tech5.mvp.OnPresenterImpl;
import ai.tech5.tech5.mvp.OnView;
import ai.tech5.tech5.utils.GifProgressDialog;
import ai.tech5.tech5.utils.Utility;

public class ScanPatientIDFaceAuthentication extends AppCompatActivity implements T5LivenessListener, OnView {
    TextInputEditText et_PatientID;
    Button CaptureFace_bt, authenticate_bt, next_patientAuth_bt, previous_patientAuth_bt;
    ImageView img_face;
    //    //Valid till 30th June 2021//com.tech5.safetynet
    // private static final String license = "eyJna2V5IjoiWDduandpSzk5aTFtbjVraXZxS05pM0MzSjZ5NnlmQW9sMU9XOVhTajJnalBQVGpDV0hnZDB1ZkxpOVhNc1QyNHhqWkJTZUdIakxLbVQ3RHVQeXpGc1JGSmhBMUZKSklPRDNRalA0M1FjNno2MzNjanVjWFwvR3BrZGJ6aTMzK0NrIiwiY24iOiJBTm9BNHdFQUFBQlFBQUFBQUFBQUFBQUFBQUFBQUFBQUJnQUFBQUFBQUFBPSJ9";

    //valid till 31st dec 2021//ai.tech5.vido
    private static final String license = "eyJna2V5IjoiU0lLWFwva2V3QzlMY0xcLzlvdjdrdUJlTTBzOHhTcWFsNldKclM2WGptNzR2ek5lWTNVM1BxNGtUVGtuVzNQdVQxSHJWMTkzWDlxWms4RmNsMTNcL01mVTJGVmFGd2pVdXRBaXVBYnRucVhnM0RSeFN5TzBtZHBOeHBCMXVpXC9jd3FxIiwiY24iOiJJQ3doK3dFQUFBQmhBQUFBQUFBQUFBQUFBQUFBQUFBQUJnQUFBQUFBQUFBPSJ9";
    LivenessController livenessController = null;

    //    private ProgressDialog dialog;
    private static final String TAGFACE = "FACESDK_LOG";
    private static final String TAG = "ScanPatientIDFaceAuthentication Activity";
    String ScanKit, VaccineBox;
    String imagepath;
    byte[] faceimagebytes;
    private OnPresenter enrollverifyPresenter;
    Uri capture_face_uri;
    private SweetAlertDialog sweetAlertDialog;
    boolean isauth;
    String responcefacepath, responce_name, responce_dob, responce_country, responce_email, responce_gender, responce_bloodgroup;
    private GifProgressDialog gifProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_patient_i_d_face_authentication);
        setContentView(R.layout.patientidfaceauthactivity_new);

        et_PatientID = findViewById(R.id.et_PatientID);
        CaptureFace_bt = findViewById(R.id.CaptureFace_bt);
        img_face = findViewById(R.id.img_face);
        authenticate_bt = findViewById(R.id.authenticate_bt);
        next_patientAuth_bt = findViewById(R.id.next_patientAuth_bt);
//        previous_patientAuth_bt = findViewById(R.id.previous_patientAuth_bt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

        livenessController =
                LivenessController.getInstance(getApplicationContext(), license);
        ScanKit = getIntent().getStringExtra("ScanKit");
        VaccineBox = getIntent().getStringExtra("VaccineBox");
        enrollverifyPresenter = new OnPresenterImpl();
        enrollverifyPresenter.setView(ScanPatientIDFaceAuthentication.this, this);

        authenticate_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_PatientID.getText().toString().length() != 0 && capture_face_uri != null) {
                    if (isValidEmail(et_PatientID.getText().toString())) {

//                    showMessage("authenticated");
                        enrollverifyPresenter.authenticate(et_PatientID.getText().toString().trim(), capture_face_uri);

                    } else {
                        showMessage("Please enter valid Patient/Email ID");
                    }
                } else if (et_PatientID.getText().toString().length() == 0 && faceimagebytes == null) {
                    showMessage("Please fill Patient/Email ID and Capture Image");
                } else if (et_PatientID.getText().toString().length() == 0) {
                    showMessage("Please fill Patient/Email ID");
                } else if (capture_face_uri == null) {
                    showMessage("Please Capture Image");
                }
            }
        });

        next_patientAuth_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isauth) {
                    Intent intent = new Intent(ScanPatientIDFaceAuthentication.this, ScanCatridgeInjectorVedioGenerate.class);
                    intent.putExtra("Captured_imagepath", imagepath);//captured path
//                    intent.putExtra("Captured_imagepath", responcefacepath);//responce path
                    intent.putExtra("PatientID", et_PatientID.getText().toString());
                    intent.putExtra("ScanKit", ScanKit);
                    intent.putExtra("VaccineBox", VaccineBox);

                    intent.putExtra("responce_name", responce_name);
                    intent.putExtra("responce_dob", responce_dob);
                    intent.putExtra("responce_country", responce_country);
                    intent.putExtra("responce_email", responce_email);
                    intent.putExtra("responce_gender", responce_gender);
                    intent.putExtra("responce_bloodgroup", responce_bloodgroup);

                    startActivity(intent);
                    finish();
                } else {
                    showMessage("Please Authenticate User");
                }
            }
        });
        gifProgressDialog = new GifProgressDialog(ScanPatientIDFaceAuthentication.this);

    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onSuccess(byte[] faceImage, byte[] bytes1) {

        if (faceImage != null) {
            showDialog(true);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File imagefile = null;
            try {
                imagefile = File.createTempFile(
                        "rotated" + System.currentTimeMillis(),  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                imagepath = imagefile.getAbsolutePath();
                Utility.writeToFile(faceImage, imagefile.getAbsolutePath());
                Uri uri = Uri.fromFile(new File(imagefile.getAbsolutePath()));
                faceimagebytes = faceImage;
                capture_face_uri = uri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showDialog(false);
                            img_face.setVisibility(View.VISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(faceImage, 0, faceImage.length);
                            img_face.setImageBitmap(bitmap);
                        } catch (Exception e) {
//                                reTake();
                            showMessage("failed1.." + e);
                            ExceptionUtility.logError(TAG, "sendResult", e);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Logger.addToLog(TAG, "onsuccess " + e.getLocalizedMessage());

            }


        }

    }

    private void showDialog(boolean isShow) {
        if (isShow) {
            gifProgressDialog.showDialog(R.raw.load, "");
        } else {
            gifProgressDialog.hideDialog();
        }
    }

    private void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onError(int i, String s) {
        Toast.makeText(ScanPatientIDFaceAuthentication.this, "Failed to capture face image " + s, Toast.LENGTH_LONG).show();

    }

    public void Capturingface(View view) {
        Tech5LivenessRequestModel livenessRequestModel = new Tech5LivenessRequestModel.Builder().create();
        livenessRequestModel.setChallengesCount(0);
        livenessRequestModel.setChallengeStartCounterInSec(1);
        livenessRequestModel.setChallengeTimeoutInSec(10);
        livenessRequestModel.setUseBackCamera(false);

        livenessController
                .detectLivenessFace(livenessRequestModel, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress(int drawble) {
        gifProgressDialog.showDialog(R.raw.load, "Verifying User");
    }

    @Override
    public void hideProgress() {
        gifProgressDialog.hideDialog();
    }

    @Override
    public void onenrollmentrespone(EnrollResponce response) {

    }

    @Override
    public void onauthenticateresponce(VerifyResponce responce) {
        if (responce != null) {
            if (responce.getVerificationResult()) {
                isauth = true;
                if (responce.getDemographics() != null) {
                    responce_name = responce.getDemographics().name == null ? "" : responce.getDemographics().name;
                    responce_dob = responce.getDemographics().dateOfBirth == null ? "" : responce.getDemographics().dateOfBirth;
                    responce_country = responce.getDemographics().COUNTRY == null ? "" : responce.getDemographics().COUNTRY;
                    responce_email = responce.getDemographics().DIGITAL_ID == null ? "" : responce.getDemographics().DIGITAL_ID;
                    responce_gender = responce.getDemographics().gender == null ? "" : responce.getDemographics().gender;
                    responce_bloodgroup = responce.getDemographics().bloodGroup == null ? "" : responce.getDemographics().bloodGroup;
                }
                showMessage("Successfully Authenticated");
            } else {
                isauth = false;
                showMessage("Authentication Failed");
            }
        }
    }

    @Override
    public void onenrollmentFailed(Throwable error) {

    }

    @Override
    public void onauthenticationFailed(Throwable error) {
        Log.d("tag", "verify Failed " + error.getLocalizedMessage());
        Toast.makeText(this, "verify Failed" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        error.printStackTrace();
        Logger.addToLog(TAG, "verify Failed " + error.getLocalizedMessage());
    }

    public void storeimage(String faceImage) {
        byte[] faceImag = Base64.decode(faceImage, Base64.NO_WRAP);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imagefile = null;
        try {
            imagefile = File.createTempFile(
                    "responceface" + System.currentTimeMillis(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            responcefacepath = imagefile.getAbsolutePath();
            Utility.writeToFile(faceImag, imagefile.getAbsolutePath());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        hideProgress();
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
        gifProgressDialog.hideDialog();
        enrollverifyPresenter.destroy();
    }

}