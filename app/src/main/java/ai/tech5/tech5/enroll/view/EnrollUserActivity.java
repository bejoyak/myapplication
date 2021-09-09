package ai.tech5.tech5.enroll.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.tech5.passportizer.OneShotResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ai.tech5.tech5.R;
import ai.tech5.tech5.activities.VerifyResult;
import ai.tech5.tech5.databinding.ActivityEnrollUserBinding;
import ai.tech5.tech5.enroll.hdbarcodedemo.Listener;
import ai.tech5.tech5.enroll.hdbarcodedemo.LogUtils;
import ai.tech5.tech5.enroll.hdbarcodedemo.Utilities;
import ai.tech5.tech5.enroll.model.BarCodeResponse;
import ai.tech5.tech5.enroll.model.BarcodeParams;
import ai.tech5.tech5.enroll.model.BarcodeTitle;
import ai.tech5.tech5.enroll.model.Configuration;
import ai.tech5.tech5.enroll.model.EmailParams;
import ai.tech5.tech5.enroll.model.FaceParams;
import ai.tech5.tech5.enroll.model.Pipeline;
import ai.tech5.tech5.enroll.network.GetDataService;
import ai.tech5.tech5.enroll.network.RetrofitClientInstance;
import ai.tech5.tech5.enroll.preferences.AppSharedPreference;
import ai.tech5.tech5.enroll.utils.AppUtils;
import ai.tech5.tech5.enroll.utils.Logger;
import ai.tech5.tech5.utils.GifProgressDialog;
import ai.tech5.tech5.utils.Utility;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ai.tech5.tech5.enroll.utils.AppUtils.showMessage;
import static ai.tech5.tech5.enroll.utils.Logger.logException;
import static ai.tech5.tech5.enroll.utils.Utility.isValidEmail;

public class EnrollUserActivity extends AppCompatActivity {


    ActivityEnrollUserBinding activityEnrollUserBinding;
    //    private static ProgressDialog dialog;
    private AppUtils appUtils;
    private Utilities utilities;
    private String TAG = "EnrollUserActivity";
    private AppSharedPreference appSharedPreference;
    private final static String NA = "N/A";
    private SweetAlertDialog sweetAlertDialog;

    LinkedHashMap<Integer, byte[]> capturedFingerTemplates = new LinkedHashMap<Integer, byte[]>();
    private SimpleDateFormat issueDateFormat = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
    private SimpleDateFormat barcodeTitleFormat = new SimpleDateFormat("ddMMyy", Locale.ENGLISH);

    private Configuration configuration = null;
    private int maxTemplateSize = 180;
    //    FingerprintUtils fingerprintUtils;
//    private ProgressDialog progressDialog;
    String bbccodepath;

    Calendar c;
    Date today;

    ImageView face_image;
    TextInputEditText patientid_ed, scankit_ed, vaccinebox_ed, scancatridge_ed, scaninjector_ed, email_ed,
            responce_name_ed, responce_dob_ed, responce_country_ed, responce_gender_ed, responce_bloodgroup_ed;
    Button generate_bt;
    String ScanKit, VaccineBox, Captured_imagepath, PatientID, scancatridge, scanInjector, capturevedioPATH_tv, responce_name, responce_dob, responce_country, responce_email, responce_gender, responce_bloodgroup;
    byte[] faceImage;
    private GifProgressDialog gifProgressDialog;
    private Utility utility;

    public void getToday30() {

        today = new Date();
        Calendar cal = new GregorianCalendar();

        cal.clear();
        cal.setTimeZone(TimeZone.getTimeZone("EDT"));
        cal.setTime(today);

        System.out.println("30days............" + today);
//        cal.add(Calendar.DAY_OF_MONTH, -30);
//        today30 = cal.getTime();
//        System.out.println("30days............" + today30);
        cal.add(Calendar.DAY_OF_MONTH, -60);
        Date today60 = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, -90);
        Date today90 = cal.getTime();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Listener.t5OneshotProcessor " + Listener.t5OneshotProcessor);
        super.onCreate(savedInstanceState);
        activityEnrollUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_enroll_user);
        appUtils = new AppUtils(this);
        utilities = new Utilities(this);
//        fingerprintUtils = new FingerprintUtils(EnrollUserActivity.this);
        appSharedPreference = new AppSharedPreference(this);
        utility = new Utility();

//        if (dialog == null) {
//            dialog = new ProgressDialog(this);
//        }
        // faceImageCallback();
        responce_dob_ed = findViewById(R.id.responce_dob_ed);
        responce_name_ed = findViewById(R.id.responce_name_ed);
        responce_country_ed = findViewById(R.id.responce_country_ed);
        responce_gender_ed = findViewById(R.id.responce_gender_ed);
        responce_bloodgroup_ed = findViewById(R.id.responce_bloodgroup_ed);
        email_ed = findViewById(R.id.email_ed);
        face_image = findViewById(R.id.face_image);
        patientid_ed = findViewById(R.id.patientid_ed);
        scankit_ed = findViewById(R.id.scankit_ed);
        vaccinebox_ed = findViewById(R.id.vaccinebox_ed);
        scancatridge_ed = findViewById(R.id.scancatridge_ed);
        scaninjector_ed = findViewById(R.id.scaninjector_ed);
//        vediopath_ed = findViewById(R.id.vediopath_ed);
        generate_bt = findViewById(R.id.generate_bt);
        ScanKit = getIntent().getStringExtra("ScanKit");
        VaccineBox = getIntent().getStringExtra("VaccineBox");
        Captured_imagepath = getIntent().getStringExtra("Captured_imagepath");
        PatientID = getIntent().getStringExtra("PatientID");

        scancatridge = getIntent().getStringExtra("scancatridge");
        scanInjector = getIntent().getStringExtra("scanInjector");
        capturevedioPATH_tv = getIntent().getStringExtra("capturevedioPATH_tv");

        responce_name = getIntent().getStringExtra("responce_name");
        responce_dob = getIntent().getStringExtra("responce_dob");
        responce_country = getIntent().getStringExtra("responce_country");
        responce_email = getIntent().getStringExtra("responce_email");
        responce_gender = getIntent().getStringExtra("responce_gender");
        responce_bloodgroup = getIntent().getStringExtra("responce_bloodgroup");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

        patientid_ed.setText((PatientID == null ? "" : PatientID));

        scankit_ed.setText((ScanKit == null ? "" : ScanKit));

        vaccinebox_ed.setText((VaccineBox == null ? "" : VaccineBox));

        scancatridge_ed.setText((scancatridge == null ? "" : scancatridge));

        scaninjector_ed.setText((scanInjector == null ? "" : scanInjector));

        email_ed.setText((responce_email == null ? "" : responce_email));

        responce_name_ed.setText((responce_name == null ? "" : responce_name));

        responce_dob_ed.setText((responce_dob == null ? "" : responce_dob));

        responce_country_ed.setText((responce_country == null ? "" : responce_country));

        responce_bloodgroup_ed.setText((responce_bloodgroup == null ? "" : responce_bloodgroup));

        responce_gender_ed.setText((responce_gender == null ? "" : responce_gender));

//        vediopath_ed.setText((capturevedioPATH_tv == null ? "" : capturevedioPATH_tv));
        if (Captured_imagepath != null) {

            File faceImageFile = new File(Captured_imagepath);
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(Uri.fromFile(new File(faceImageFile.getAbsolutePath())));
                faceImage = getBytes(iStream);

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (faceImageFile.exists()) {
                face_image.setImageURI(Uri.fromFile(new File(faceImageFile.getAbsolutePath())));
            } else {
//                faceImage.setImageBitmap(null);
                face_image.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
            }
        }


        Log.d("TAG", "One shot processor " + Listener.t5OneshotProcessor);

        configuration = utilities.loadConfiguration();

        boolean hasFingerCapture = true;

        if (configuration != null && configuration.barcodeGenerationParameters != null && configuration.barcodeGenerationParameters.fingerprintBiometricTemplates != null) {

            maxTemplateSize = configuration.barcodeGenerationParameters.fingerprintBiometricTemplates.size;

            if (maxTemplateSize < 180 || maxTemplateSize > 500) {
                maxTemplateSize = 180;
            }
        }


//        progressDialog = new ProgressDialog(EnrollUserActivity.this);
//        progressDialog.setMessage("Wait");
//        progressDialog.setCancelable(false);

        getToday30();
        gifProgressDialog = new GifProgressDialog(EnrollUserActivity.this);

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onResume() {
        System.gc();
        super.onResume();
    }


    public void generateZip(View view) {
        if (isNetworkConnected()) {
            if (isValidData() && faceImage != null) {

//            if (isValidData()) {
                showDialog(true);
                AsyncTask.execute(() -> {

                    try {
                        Logger.addToLog(TAG, "Uploading data ");

                        String demographic = getDemographicData();

                        OneShotResult result = null;

                        Log.d(TAG, "demographic " + demographic);
                        showDialog(false);
                        uploadDataNew(demographic, result);
                    } catch (Exception e) {
                        //showMessage(EnrollUserActivity.this, "" + e.getLocalizedMessage());
                        showDialog(false);
                        showFailureAlert(e.getLocalizedMessage() == null ? getString(R.string.enroll_error) : e.getLocalizedMessage());
                        logException(TAG, e);
                        Logger.addToLog(TAG, "Enroll task failed " + e.getLocalizedMessage());

                    }
                });
            } else {

                if (!isValidData()) {
                    showMessage(EnrollUserActivity.this, getString(R.string.fill_data));
                } else if (faceImage == null) {
                    showMessage(EnrollUserActivity.this, "Please add face image");
                }
            }
        } else {
            showMessage(EnrollUserActivity.this, "Please check your Internet Connection");
        }
    }

    private String getFileName() {
        return formatNameForBarcode(getString(activityEnrollUserBinding.patientidEd))
                //  + "_" + getString(activityEnrollUserBinding.etCompany)
                + "_" + getDateTime();
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getString(EditText editText) {
        return editText.getText().toString().trim();
    }


    private boolean isValidData() {
        boolean isValid = true;
        if (isEmpty(activityEnrollUserBinding.patientidEd)) {
            isValid = false;
            activityEnrollUserBinding.patientidEd.setError("Patientid should not be empty");
        }

        if (!isValidEmail(getString(activityEnrollUserBinding.emailEd))) {
            isValid = false;
            activityEnrollUserBinding.emailEd.setError("Please enter valid Email Id");
        }

        ////////////////////////////////
        if (isEmpty(activityEnrollUserBinding.scankitEd)) {
            isValid = false;
            activityEnrollUserBinding.scankitEd.setError("ScanKit should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.vaccineboxEd)) {
            isValid = false;
            activityEnrollUserBinding.vaccineboxEd.setError("ScanVacccineBox should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.scancatridgeEd)) {
            isValid = false;
            activityEnrollUserBinding.scancatridgeEd.setError("ScanCatridge should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.scaninjectorEd)) {
            isValid = false;
            activityEnrollUserBinding.scaninjectorEd.setError("ScanInjector should not be empty");
        }


        if (isEmpty(activityEnrollUserBinding.responceNameEd)) {
            isValid = false;
            activityEnrollUserBinding.responceNameEd.setError("Name should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.responceDobEd)) {
            isValid = false;
            activityEnrollUserBinding.responceDobEd.setError("Date Of Birth should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.responceCountryEd)) {
            isValid = false;
            activityEnrollUserBinding.responceCountryEd.setError("Country should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.responceBloodgroupEd)) {
            isValid = false;
            activityEnrollUserBinding.responceBloodgroupEd.setError("BloodGroup should not be empty");
        }
        if (isEmpty(activityEnrollUserBinding.responceGenderEd)) {
            isValid = false;
            activityEnrollUserBinding.responceGenderEd.setError("Gender should not be empty");
        }
        ////////////////////////////////////
        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private String getDemographicData() {
        // Demographic demographic = new Demographic();
        List<String> demographics = new ArrayList<>();
        demographics.add(getString(activityEnrollUserBinding.patientidEd));
        demographics.add(issueDateFormat.format(new Date()));

        demographics.add(getString(activityEnrollUserBinding.responceNameEd) == "" ? NA : getString(activityEnrollUserBinding.responceNameEd));
        demographics.add(getString(activityEnrollUserBinding.emailEd) == "" ? NA : getString(activityEnrollUserBinding.emailEd));
        demographics.add(getString(activityEnrollUserBinding.responceDobEd) == "" ? NA : getString(activityEnrollUserBinding.responceDobEd));
        demographics.add(getString(activityEnrollUserBinding.responceCountryEd) == "" ? NA : getString(activityEnrollUserBinding.responceCountryEd));
        demographics.add(getString(activityEnrollUserBinding.responceGenderEd) == "" ? NA : getString(activityEnrollUserBinding.responceGenderEd));
        demographics.add(getString(activityEnrollUserBinding.responceBloodgroupEd) == "" ? NA : getString(activityEnrollUserBinding.responceBloodgroupEd));
////////////////////////////////////////////////         KIT
        String kit = getString(activityEnrollUserBinding.scankitEd) == "" ? NA : getString(activityEnrollUserBinding.scankitEd);
        Log.d("scankitdetailsentering", kit);
        String kitid = null;
        String separator = ":";

        try {
            Log.d(TAG, "kit id json......................." + kit.split(",")[0].indexOf(":"));
            int kitidsepPos = kit.split(",")[0].indexOf(separator);
            if (kitidsepPos == -1) {
                System.out.println("");
            }
            kitid = kit.split(",")[0].substring(kitidsepPos + separator.length());
            System.out.println("1kitidSubstring after separator = " + kitid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        demographics.add(kitid == "" ? NA : kitid);

/////////////////////////////
        String kitdetails = null;
        try {
            Log.d(TAG, "kit details json......................." + kit.split(",")[1].indexOf(":"));
            int kitdetailssepPos = kit.split(",")[1].indexOf(separator);
            if (kitdetailssepPos == -1) {
                System.out.println("");
            }
            kitdetails = kit.split(",")[1].substring(kitdetailssepPos + separator.length());
            System.out.println("1kitdetailsSubstring after separator = " + kitdetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
        demographics.add(kitdetails == "" ? NA : kitdetails);

//////////////////////////
        String kitpackagedate = null;
        try {
            Log.d(TAG, "kit packagedate json......................." + kit.split(",")[2].indexOf(":"));
//                    String separator =":";
            int kitpackagedatesepPos = kit.split(",")[2].indexOf(separator);
            if (kitpackagedatesepPos == -1) {
                System.out.println("");
            }
            kitpackagedate = kit.split(",")[2].substring(kitpackagedatesepPos + separator.length());
            System.out.println("1kitpackagedateSubstring after separator = " + kitpackagedate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(kitpackagedate == "" ? NA : kitpackagedate);

//////////////
////////////////////////////////////////////////         VACCINEBOX

//        demographics.add(getString(activityEnrollUserBinding.vaccineboxEd) == "" ? NA : getString(activityEnrollUserBinding.vaccineboxEd));
        String vacc = getString(activityEnrollUserBinding.vaccineboxEd) == "" ? NA : getString(activityEnrollUserBinding.vaccineboxEd);
        Log.d("scanvaccineboxentering", vacc);
///////////////////////
        String vaccineboxid = null;
        try {
            Log.d(TAG, "vaccinebox id json......................." + vacc.split(",")[0].indexOf(":"));
            int vaccineboxidsepPos = vacc.split(",")[0].indexOf(separator);
            if (vaccineboxidsepPos == -1) {
                System.out.println("");
            }
            vaccineboxid = vacc.split(",")[0].substring(vaccineboxidsepPos + separator.length());
            System.out.println("1vaccineboxidSubstring after separator = " + vaccineboxid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(vaccineboxid == "" ? NA : vaccineboxid);

////////////
        String vaccineboxdetails = null;
        try {
            Log.d(TAG, "vaccinebox id json......................." + vacc.split(",")[1].indexOf(":"));
            int vaccineboxdetailssepPos = vacc.split(",")[1].indexOf(separator);
            if (vaccineboxdetailssepPos == -1) {
                System.out.println("");
            }
            vaccineboxdetails = vacc.split(",")[1].substring(vaccineboxdetailssepPos + separator.length());
            System.out.println("1vaccineboxdetailsSubstring after separator = " + vaccineboxdetails);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(vaccineboxdetails == "" ? NA : vaccineboxdetails);

///////////
        String vaccineboxpackagedate = null;
        try {
            Log.d(TAG, "vaccinebox packagedate json......................." + vacc.split(",")[2].indexOf(":"));
            int vaccineboxpackagedatesepPos = vacc.split(",")[2].indexOf(separator);
            if (vaccineboxpackagedatesepPos == -1) {
                System.out.println("");
            }
            vaccineboxpackagedate = vacc.split(",")[2].substring(vaccineboxpackagedatesepPos + separator.length());
            System.out.println("1vaccineboxdetailsSubstring after separator = " + vaccineboxpackagedate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        demographics.add(vaccineboxpackagedate == "" ? NA : vaccineboxpackagedate);

        //////////////////////////////////////////////////\
////////////////////////////////////////////////         CARTRIDGE

//        demographics.add(getString(activityEnrollUserBinding.scancatridgeEd) == "" ? NA : getString(activityEnrollUserBinding.scancatridgeEd));
        String cart = getString(activityEnrollUserBinding.scancatridgeEd) == "" ? NA : getString(activityEnrollUserBinding.scancatridgeEd);
        Log.d("scancartrdetailsenterin", cart);
///////////////////////////////////
        String cartridgeid = null;
        try {
            Log.d(TAG, "catrige id json......................." + cart.split(",")[0].indexOf(":"));
            int catridgeidsepPos = cart.split(",")[0].indexOf(separator);
            if (catridgeidsepPos == -1) {
                System.out.println("");
            }
            cartridgeid = cart.split(",")[0].substring(catridgeidsepPos + separator.length());
            System.out.println("1cartridgeidSubstring after separator = " + cartridgeid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(cartridgeid == "" ? NA : cartridgeid);

////////////////
        String cartridgepakcagedate = null;
        try {
            Log.d(TAG, "cartridge pakcagedate json......................." + cart.split(",")[1].indexOf(":"));
            int catridgepakcagedatesepPos = cart.split(",")[1].indexOf(separator);
            if (catridgepakcagedatesepPos == -1) {
                System.out.println("");
            }
            cartridgepakcagedate = cart.split(",")[1].substring(catridgepakcagedatesepPos + separator.length());
            System.out.println("1cartridgepakcagedateSubstring after separator = " + cartridgepakcagedate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        demographics.add(cartridgepakcagedate == "" ? NA : cartridgepakcagedate);

//////////////
        String cartridgeexpiry = null;
        try {
            Log.d(TAG, "catridge expiry json......................." + cart.split(",")[2].indexOf(":"));
            int catridgeexpirysepPos = cart.split(",")[2].indexOf(separator);
            if (catridgeexpirysepPos == -1) {
                System.out.println("");
            }
            cartridgeexpiry = cart.split(",")[2].substring(catridgeexpirysepPos + separator.length());
            System.out.println("1cartridgeexpirySubstring after separator = " + cartridgeexpiry);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(cartridgeexpiry == "" ? NA : cartridgeexpiry);

//////////
        String cartridgemanufacture = null;
        try {
            Log.d(TAG, "cartridge manufacture json......................." + cart.split(",")[3].indexOf(":"));
            int catridgemanufacturesepPos = cart.split(",")[3].indexOf(separator);
            if (catridgemanufacturesepPos == -1) {
                System.out.println("");
            }
            cartridgemanufacture = cart.split(",")[3].substring(catridgemanufacturesepPos + separator.length());
            System.out.println("1cartridgeexpirySubstring after separator = " + cartridgemanufacture);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(cartridgemanufacture == "" ? NA : cartridgemanufacture);

////////////////////////////////////////////////         INJECTOR

//        demographics.add(getString(activityEnrollUserBinding.scaninjectorEd) == "" ? NA : getString(activityEnrollUserBinding.scaninjectorEd));
        String inje = getString(activityEnrollUserBinding.scaninjectorEd) == "" ? NA : getString(activityEnrollUserBinding.scaninjectorEd);
        Log.d("scaninjedetailsentering", inje);
////////////////////////////////
        String injectorid = null;
        try {
            Log.d(TAG, "injector id json......................." + inje.split(",")[0].indexOf(":"));
            int injectoridsepPos = inje.split(",")[0].indexOf(separator);
            if (injectoridsepPos == -1) {
                System.out.println("");
            }
            injectorid = inje.split(",")[0].substring(injectoridsepPos + separator.length());
            System.out.println("1injectoridSubstring after separator = " + injectorid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(injectorid == "" ? NA : injectorid);

//////////////
        String injectorpakcagedate = null;
        try {
            Log.d(TAG, "injector pakcagedate json......................." + inje.split(",")[1].indexOf(":"));
            int injectorpakcagedatesepPos = inje.split(",")[1].indexOf(separator);
            if (injectorpakcagedatesepPos == -1) {
                System.out.println("");
            }
            injectorpakcagedate = inje.split(",")[1].substring(injectorpakcagedatesepPos + separator.length());
            System.out.println("1injectorpakcagedateSubstring after separator = " + injectorpakcagedate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        demographics.add(injectorpakcagedate == "" ? NA : injectorpakcagedate);
////////////

        ////////////////////////////////
        return TextUtils.join(",", demographics);

    }

    private void uploadDataNew(String requestModel, OneShotResult result) {


        ArrayList<MultipartBody.Part> parts = new ArrayList<>();

        boolean hasFingers = false;

        Pipeline pipeline = new Pipeline();

        boolean includeFaceTemplate = true;
        boolean includeCompressedImage = true;
        int compressionLevel = 1;
        boolean includeDemographics = true;

        if (configuration != null && configuration.barcodeGenerationParameters != null && configuration.barcodeGenerationParameters.compressedFaceImage != null) {
            includeCompressedImage = configuration.barcodeGenerationParameters.compressedFaceImage.qty > 0;
            compressionLevel = Utilities.getCompressionLevel(configuration.barcodeGenerationParameters.compressedFaceImage.size);
        }

        if (configuration != null && configuration.barcodeGenerationParameters != null && configuration.barcodeGenerationParameters.faceBiometricTemplate != null) {
            includeFaceTemplate = configuration.barcodeGenerationParameters.faceBiometricTemplate.qty > 0;
        }

        if (configuration != null && configuration.barcodeGenerationParameters != null && configuration.barcodeGenerationParameters.demographicData != null) {

            includeDemographics = configuration.barcodeGenerationParameters.demographicData.qty > 0;
        }


        if (appSharedPreference.isBackEndProcessing()) {

            parts.add(AppUtils.byteArrayToMultipartFile("face_image", faceImage, "face_img.jpg", "image/jpeg"));

            if (includeCompressedImage || includeFaceTemplate) {
                FaceParams faceParams = new FaceParams(includeFaceTemplate, 0.6f, 1, includeCompressedImage, compressionLevel);
                pipeline.facePipeline = faceParams;
            }

        } else if (result != null) {

            if (includeCompressedImage && result.compressedData != null) {
                LogUtils.debug(TAG, "compressed face length " + result.compressedData.length);
                parts.add(AppUtils.byteArrayToMultipartFile("compressed_image", result.compressedData, "compressed_image.dat", "application/octet-stream"));
            }

            if (includeFaceTemplate && result.template != null) {
                LogUtils.debug(TAG, " face template length " + result.template.length);
                parts.add(AppUtils.byteArrayToMultipartFile("face_template", result.template, "face_template.dat", "application/octet-stream"));
            }

            //
        }

        MultipartBody.Part demographics = null;

        if (includeDemographics && requestModel != null) {
            LogUtils.debug(TAG, "demo length " + requestModel.getBytes().length);
            demographics = AppUtils.byteArrayToMultipartFile("demog", requestModel.getBytes(), "demo.json", "application/octet-stream");
            parts.add(demographics);

        }

        BarcodeParams par = new BarcodeParams();
        // Size size = calculateBarcodeSize();
        Size size = new Size(14, 14);
        if (configuration != null && configuration.barcodeGenerationParameters != null) {
            size = new Size(configuration.barcodeGenerationParameters.blockRows, configuration.barcodeGenerationParameters.blockCols);
        }
        par.blockRows = size.getWidth();
        par.blockCols = size.getHeight();

//        showMessage(EnrollUserActivity.this, "Rows X Cols " + par.blockRows + " x " + par.blockCols);

        int errorCorrection = 0;
        int gridSize = 6;
        int thickness = 1;
        boolean hasExpiryDate = true;

        if (configuration != null && configuration.barcodeGenerationParameters != null) {

            gridSize = configuration.barcodeGenerationParameters.gridSize;
            thickness = configuration.barcodeGenerationParameters.thickness;

            if (configuration.barcodeGenerationParameters.style != null && configuration.barcodeGenerationParameters.style.equalsIgnoreCase("Dots")) {
                thickness = -thickness;
            }

            if (configuration.barcodeGenerationParameters.errorCorrection != null) {
                errorCorrection = configuration.barcodeGenerationParameters.errorCorrection.value;
            }

            if (configuration.barcodeGenerationParameters.expirationDate != null) {
                hasExpiryDate = configuration.barcodeGenerationParameters.expirationDate.enabled;
            }

        }

        par.errorCorrection = errorCorrection;
        par.gridSize = gridSize;
        par.thickness = thickness;


        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 30);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (hasExpiryDate) {
                par.expirationDate = sdf.format(c.getTime());
            }

        } catch (Exception e) {

        }

        if (appSharedPreference.isTitleEnabled()) {

            BarcodeTitle title = new BarcodeTitle();
            title.text = "Health ID";
//            title.text = formatNameForBarcode(getString(activityEnrollUserBinding.patientidEd)) + barcodeTitleFormat.format(new Date()) + formatTo2Digits(par.blockRows) + formatTo2Digits(par.blockCols) + formatTo2Digits(par.gridSize) + formatTo2Digits(par.thickness);
            title.alignment = "center";
            title.location = "bottom";
            title.offset = 10;

            if (configuration != null && configuration.barcodeGenerationParameters != null && configuration.barcodeGenerationParameters.title != null) {
                title.alignment = configuration.barcodeGenerationParameters.title.alignment;
                title.location = configuration.barcodeGenerationParameters.title.location;
                title.offset = configuration.barcodeGenerationParameters.title.offset;
                ;
            }
            par.title = title;

        }


        EmailParams emailParams = new EmailParams();
        emailParams.emailTol = getString(activityEnrollUserBinding.emailEd);
        emailParams.subject = "Your Tech5 ID";

        pipeline.barcodeGenerationParameters = par;
        pipeline.emailParams = emailParams;

        String pipelineJson = new Gson().toJson(pipeline);


        LogUtils.debug(TAG, pipelineJson);

        //


        LogUtils.debug(TAG, "barcode params length " + pipelineJson.getBytes().length);
        MultipartBody.Part pipelinePart = AppUtils.byteArrayToMultipartFile("pipeline", pipelineJson.getBytes(), "pipeline.json", "application/json");

        parts.add(pipelinePart);

        if (parts.size() > 1) {

            showDialog(true);
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
//            File file = utility.createExternalDirectory(EnrollUserActivity.this, "cache");
//
//            utility.writeToFile(new Gson().toJson(parts.toString()).getBytes(), file.getAbsolutePath() + File.separator + "cryptograph.json");

            LogUtils.debug(TAG, getApiUrl());
            Call<BarCodeResponse> call = service.createBarCode(getApiUrl(), parts);


            call.enqueue(new Callback<BarCodeResponse>() {
                @Override
                public void onResponse(Call<BarCodeResponse> call, Response<BarCodeResponse> response) {
                    showDialog(false);

                    if (response != null) {

                        if (response.code() == 400) {

                            try {
                                String errorResp = response.errorBody().string();
                                showFailureAlert(errorResp);
                            } catch (IOException e) {

                                showFailureAlert(getString(R.string.network_activity_error));
                            }

                        } else if (response.code() == 200 && response.body() != null && response.body().image != null) {
                            saveBBCImage(response.body().image);
                        } else {
                            showFailureAlert(getString(R.string.network_activity_error));
                        }

                    } else {

                        LogUtils.debug("TAG", "unable to get response from server");
                        showFailureAlert(getString(R.string.network_activity_error));
                    }
                }

                @Override
                public void onFailure(Call<BarCodeResponse> call, Throwable t) {

                    showDialog(false);
                    LogUtils.debug("TAG", "msg -> " + t.getLocalizedMessage());
                    showFailureAlert(t.getLocalizedMessage() != null ? t.getLocalizedMessage() : getResources().getString(R.string.network_activity_error));
                }
            });

        } else {
            Toast.makeText(EnrollUserActivity.this, "Empty Data", Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(boolean isShow) {
        runOnUiThread(() -> {

            if (isShow) {
                showProgress();
            } else {
                hideProgress();
            }
        });
    }

    private String getApiUrl() {
        return appSharedPreference.getBaseUrl() + appSharedPreference.getApiCreateRequest();

    }


    public Bitmap resizeBitmap(byte[] bytes, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bmOptions);
        int originalWidth = bmOptions.outWidth;
        int originalHeight = bmOptions.outHeight;
        int targetW = (originalWidth * targetH) / originalHeight;
        //int[] newHW = getOptimalDimensions(originalWidth, originalHeight, targetW, targetH);
        Bitmap resized = Bitmap.createScaledBitmap(AppUtils.convertByteArrayToBitmap(bytes), targetW, targetH, true);
        return resized;
    }


    private void saveBBCImage(String base64Image) {
        showDialog(true);
        AsyncTask.execute(() -> {

            String storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            System.out.println("path................" + storageDir);
            bbccodepath = storageDir + File.separator + "Tech5" + File.separator + "BBC_CODES" + File.separator + getFileName() + ".png";

            galleryAddPic(bbccodepath);
            System.out.println("bbccodepath path......................" + bbccodepath);
            File myFile = new File(bbccodepath);
            if (!myFile.getParentFile().exists()) {
                myFile.getParentFile().mkdirs();
            }
            try {
                if (myFile.exists()) {
                    myFile.delete();
                }
                FileOutputStream outPut = new FileOutputStream(myFile);
                outPut.write(AppUtils.getByteArray(base64Image));
                outPut.close();

                showSuccessAlert("Health ID created successfully");
                showDialog(false);

            } catch (Exception e) {
                showDialog(false);
//                showFailureAlert(getString(R.string.bbc_file_creation_error));
//                Logger.addToLog(TAG, getString(R.string.bbc_file_creation_error) + e.getLocalizedMessage());
                showFailureAlert("Error In Health ID Creation");
                Logger.addToLog(TAG, "Error In Health ID Creation" + e.getLocalizedMessage());

            }
        });
    }

    private void galleryAddPic(String currentbarcodePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentbarcodePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void showSuccessAlert(String message) {
        runOnUiThread(() -> {
                    sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("Tech5 ID")
                            .setContentText(message)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    Intent intent = new Intent(EnrollUserActivity.this, VerifyResult.class);
                                    intent.putExtra("Captured_imagepath", Captured_imagepath);
                                    intent.putExtra("PatientID", PatientID);
                                    intent.putExtra("ScanKit", ScanKit);
                                    intent.putExtra("VaccineBox", VaccineBox);

                                    intent.putExtra("scancatridge", scancatridge);
                                    intent.putExtra("scanInjector", scanInjector);
                                    intent.putExtra("responce_name", responce_name_ed.getText().toString());
                                    intent.putExtra("responce_dob", responce_dob_ed.getText().toString());
                                    intent.putExtra("responce_country", responce_country_ed.getText().toString());
                                    intent.putExtra("responce_email", email_ed.getText().toString());
                                    intent.putExtra("responce_gender", responce_gender_ed.getText().toString());
                                    intent.putExtra("responce_bloodgroup", responce_bloodgroup_ed.getText().toString());

                                    intent.putExtra("bbccodepath", bbccodepath);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                }
        );
    }


    private void showFailureAlert(String message) {

        runOnUiThread(() -> {
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

            sweetAlertDialog.setTitleText("Tech5 ID")
                    .setContentText(message)
                    .show();
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
        hideProgress();
    }


    @SuppressLint("DefaultLocale")
    private String formatTo2Digits(int num) {
        return String.format("%02d", num);
    }

    private String formatNameForBarcode(String name) {
        if (name.length() <= 6) {
            return name;
        } else {
            return name.substring(0, 6);
        }
    }


    public void showProgress() {
        gifProgressDialog.showDialog(R.raw.load, "");
    }
//    @Override
    public void hideProgress() {
        gifProgressDialog.hideDialog();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
}
