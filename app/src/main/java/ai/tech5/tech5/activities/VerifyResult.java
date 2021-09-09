package ai.tech5.tech5.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.hdbarcode.hdbarcodereader.DecdeHDBarcodeActivity;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import ai.tech5.tech5.R;
import ai.tech5.tech5.databinding.ActivityEnrollUserBinding;
import ai.tech5.tech5.enroll.hdbarcodedemo.Utilities;
import ai.tech5.tech5.enroll.model.Configuration;
import ai.tech5.tech5.enroll.preferences.AppSharedPreference;
import ai.tech5.tech5.enroll.utils.AppUtils;

public class VerifyResult extends AppCompatActivity {

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

    ImageView face_image, img_bar_code;
    TextInputEditText patientid_ed, scankit_ed, vaccinebox_ed, scancatridge_ed, scaninjector_ed, email_ed,
            responce_name_ed, responce_dob_ed, responce_country_ed, responce_gender_ed, responce_bloodgroup_ed;
    Button Home_bt;
    String ScanKit, VaccineBox, Captured_imagepath, PatientID, scancatridge, scanInjector, responce_name, responce_dob, responce_country, responce_email, responce_gender, responce_bloodgroup, responce_bbccodepath;
    byte[] faceImage;


    ScrollView resultScrollView;
    TextView heading, nameheading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_result);
        resultScrollView = findViewById(R.id.resultScrollView);
        heading = findViewById(R.id.heading);
        nameheading = findViewById(R.id.name);
        appUtils = new AppUtils(this);
        utilities = new Utilities(this);
        appSharedPreference = new AppSharedPreference(this);
        img_bar_code = findViewById(R.id.img_bar_code);
        face_image = findViewById(R.id.face_image);
        Home_bt = findViewById(R.id.Home_bt);
        ScanKit = getIntent().getStringExtra("ScanKit");
        VaccineBox = getIntent().getStringExtra("VaccineBox");
        Captured_imagepath = getIntent().getStringExtra("Captured_imagepath");
        PatientID = getIntent().getStringExtra("PatientID");

        scancatridge = getIntent().getStringExtra("scancatridge");
        scanInjector = getIntent().getStringExtra("scanInjector");

        responce_name = getIntent().getStringExtra("responce_name");
        responce_dob = getIntent().getStringExtra("responce_dob");
        responce_country = getIntent().getStringExtra("responce_country");
        responce_email = getIntent().getStringExtra("responce_email");
        responce_gender = getIntent().getStringExtra("responce_gender");
        responce_bloodgroup = getIntent().getStringExtra("responce_bloodgroup");
        responce_bbccodepath = getIntent().getStringExtra("bbccodepath");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);
        heading.setVisibility(View.VISIBLE);
        heading.setText("VACCINATION DETAILS");
        nameheading.setText((responce_name == null ? "" : responce_name));

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

        if (responce_bbccodepath != null) {

            File faceImageFile = new File(responce_bbccodepath);

            if (faceImageFile.exists()) {
                img_bar_code.setImageURI(Uri.fromFile(new File(faceImageFile.getAbsolutePath())));
            } else {
                img_bar_code.setImageBitmap(null);
//                img_bar_code.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
            }
        }
        Home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyResult.this, DecdeHDBarcodeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();


            }
        });

        showDemographics(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(VerifyResult.this, DecdeHDBarcodeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDemographics(boolean show) {
        resultScrollView.removeAllViews();
        resultScrollView = null;
        resultScrollView = findViewById(R.id.resultScrollView);
        if (show) {

            LinearLayout outerLayout = new LinearLayout(getApplicationContext());
            outerLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            outerLayout.setLayoutParams(LLParams);

            LayoutInflater inflater;
            resultScrollView.setVisibility(View.VISIBLE);
            LinearLayout innerLayout = new LinearLayout(getApplicationContext());

            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setLayoutParams(LLParams);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View resultView = inflater.inflate(R.layout.verification_result_template, null);

            TextView patientid_ed = resultView.findViewById(R.id.key);
            TextView patientid_edvalue = resultView.findViewById(R.id.value);

            TextView scankit_ed = resultView.findViewById(R.id.scankit_ed);
            TextView scankit_edvalue = resultView.findViewById(R.id.scankit_edvalue);

            TextView vaccinebox_ed = resultView.findViewById(R.id.vaccinebox_ed);
            TextView vaccinebox_edvalue = resultView.findViewById(R.id.vaccinebox_edvalue);

            TextView scancatridge_ed = resultView.findViewById(R.id.scancatridge_ed);
            TextView scancatridge_edvalue = resultView.findViewById(R.id.scancatridge_edvalue);

            TextView scaninjector_ed = resultView.findViewById(R.id.scaninjector_ed);
            TextView scaninjector_edvalue = resultView.findViewById(R.id.scaninjector_edvalue);

            TextView responce_name_ed = resultView.findViewById(R.id.responce_name_ed);
            TextView responce_name_edvalue = resultView.findViewById(R.id.responce_name_edvalue);

            TextView responce_dob_ed = resultView.findViewById(R.id.responce_dob_ed);
            TextView responce_dob_edvalue = resultView.findViewById(R.id.responce_dob_edvalue);

            TextView responce_country_ed = resultView.findViewById(R.id.responce_country_ed);
            TextView responce_country_edvalue = resultView.findViewById(R.id.responce_country_edvalue);

            TextView responce_gender_ed = resultView.findViewById(R.id.responce_gender_ed);
            TextView responce_gender_edvalue = resultView.findViewById(R.id.responce_gender_edvalue);

            TextView responce_bloodgroup_ed = resultView.findViewById(R.id.responce_bloodgroup_ed);
            TextView responce_bloodgroup_edvalue = resultView.findViewById(R.id.responce_bloodgroup_edvalue);

            TextView responce_mail_ed = resultView.findViewById(R.id.responce_mail_ed);
            TextView responce_mail_edvalue = resultView.findViewById(R.id.responce_mail_edvalue);

//            patientid_ed.setText("Patient ID");
//            patientid_edvalue.setText("Value");
////////////////////////////////////////////
            patientid_ed.setText("Pateint ID");
            patientid_edvalue.setText(": " + (PatientID == null ? "" : PatientID));

            scankit_ed.setText("Scan Kit Data ");
            scankit_edvalue.setText(": " + (ScanKit == null ? "" : ScanKit));

            vaccinebox_ed.setText("Scan VaccineBox Data");
            vaccinebox_edvalue.setText(": " + (VaccineBox == null ? "" : VaccineBox));

            scancatridge_ed.setText("Scan Cartridge Data");
            scancatridge_edvalue.setText(": " + (scancatridge == null ? "" : scancatridge));

            scaninjector_ed.setText("Scan Injector Data");
            scaninjector_edvalue.setText(": " + (scanInjector == null ? "" : scanInjector));

//            email_ed.setText((responce_email == null ? "" : responce_email));

            responce_name_ed.setText("Name");
            responce_name_edvalue.setText(": " + (responce_name == null ? "" : responce_name));

            responce_dob_ed.setText("Date Of Birth");
            responce_dob_edvalue.setText(": " + (responce_dob == null ? "" : responce_dob));

            responce_country_ed.setText("Country");
            responce_country_edvalue.setText(": " + (responce_country == null ? "" : responce_country));

            responce_bloodgroup_ed.setText("Blood Group");
            responce_bloodgroup_edvalue.setText(": " + (responce_bloodgroup == null ? "" : responce_bloodgroup));

            responce_gender_ed.setText("Gender");
            responce_gender_edvalue.setText(": " + (responce_gender == null ? "" : responce_gender));


            responce_mail_ed.setText("Email");
            responce_mail_edvalue.setText(": " + (responce_email == null ? "" : responce_email));

            innerLayout.addView(resultView);
            outerLayout.addView(innerLayout);

            resultScrollView.addView(outerLayout);
        } else {
            resultScrollView.setVisibility(View.GONE);
        }
    }

}