package ai.tech5.tech5.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import com.face.liveness.LivenessController;
import com.face.liveness.T5LivenessListener;
import com.face.liveness.exception.ExceptionUtility;
import com.face.liveness.model.Tech5LivenessRequestModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.completions.IDocumentReaderCompletion;
import com.regula.documentreader.api.enums.DocReaderAction;
import com.regula.documentreader.api.enums.eGraphicFieldType;
import com.regula.documentreader.api.enums.eVisualFieldType;
import com.regula.documentreader.api.errors.DocumentReaderException;
import com.regula.documentreader.api.results.DocumentReaderResults;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.GlideRequests;
import ai.tech5.tech5.enroll.utils.Logger;
import ai.tech5.tech5.idcaptureidcard.CaptureIdCard;
import ai.tech5.tech5.models.EnrollResponce;
import ai.tech5.tech5.models.VerifyResponce;
import ai.tech5.tech5.mvp.OnPresenter;
import ai.tech5.tech5.mvp.OnPresenterImpl;
import ai.tech5.tech5.mvp.OnView;
import ai.tech5.tech5.rotation.FacerotationPresenter;
import ai.tech5.tech5.rotation.FacerotationPresenterImplementation;
import ai.tech5.tech5.rotation.FacerotationView;
import ai.tech5.tech5.rotation.ImageRotateParams;
import ai.tech5.tech5.utils.DatePickerFragment;
import ai.tech5.tech5.utils.GifProgressDialog;
import ai.tech5.tech5.utils.PreferencesHelper;
import ai.tech5.tech5.utils.Utility;

public class EnrollActivity extends AppCompatActivity implements T5LivenessListener, FacerotationView, IDocumentReaderCompletion, OnView {

    private static final String TAG = "EnrollActivity";
    TextInputEditText et_name, et_Dob, et_country, et_email;
    Spinner sp_gender, sp_blood_group;
    Button btn_capture_face, btn_enroll, load_image_captureID_btn;
    ImageView img_face, loadedImage_captureID;
    //    //Valid till 30th June 2021//com.tech5.safetynet
//    private static final String license = "eyJna2V5IjoiWDduandpSzk5aTFtbjVraXZxS05pM0MzSjZ5NnlmQW9sMU9XOVhTajJnalBQVGpDV0hnZDB1ZkxpOVhNc1QyNHhqWkJTZUdIakxLbVQ3RHVQeXpGc1JGSmhBMUZKSklPRDNRalA0M1FjNno2MzNjanVjWFwvR3BrZGJ6aTMzK0NrIiwiY24iOiJBTm9BNHdFQUFBQlFBQUFBQUFBQUFBQUFBQUFBQUFBQUJnQUFBQUFBQUFBPSJ9";
    //valid till 31st dec 2021//ai.tech5.vido
    private static final String license = "eyJna2V5IjoiU0lLWFwva2V3QzlMY0xcLzlvdjdrdUJlTTBzOHhTcWFsNldKclM2WGptNzR2ek5lWTNVM1BxNGtUVGtuVzNQdVQxSHJWMTkzWDlxWms4RmNsMTNcL01mVTJGVmFGd2pVdXRBaXVBYnRucVhnM0RSeFN5TzBtZHBOeHBCMXVpXC9jd3FxIiwiY24iOiJJQ3doK3dFQUFBQmhBQUFBQUFBQUFBQUFBQUFBQUFBQUJnQUFBQUFBQUFBPSJ9";
    LivenessController livenessController = null;
    private static final int REQUEST_CAMERA = 201;
    private static final int PICK_FROM_GALLERY = 301;

    private Uri photoURI;
    private String currentPhotoPath;
    RadioGroup rad_group_choose_from;
    RadioButton rad_btn_Enter_Details, rad_btn_CaptureID;
    LinearLayout LL_Details, LL_CaptureID;
    private static final String TAGFACE = "FACESDK_LOG";
    private FacerotationPresenter presenter;
    Uri IDimageUri;
    DatePickerDialog.OnDateSetListener onDobSetListener, IDonDobSetListener;
    private NestedScrollView scrollView;
    private TextView txtName;
    TextInputEditText et_name_card;
    TextInputEditText et_Dob_card;
    TextInputEditText et_country_card;
    TextInputEditText et_email_card;
    TextInputEditText et_gender_card;
    TextInputEditText et_blood_group_card;
    LinearLayout LL_Details_card;
    TextInputLayout name_hint, dob_hint, country_hint, email_hint, gender_hint, bloodgroup_hint;
    Spinner sp_gender_card, sp_blood_group_card;
    java.text.DateFormat appDateFormat;
    String gender_card_final, bloodgroup_card_final;
    private OnPresenter enrollverifyPresenter;
    Uri enterdetails_capture_face_uri, enterdetails_capture_id_uri;
    boolean bloodgrp_boolean, gender_boolean;
    private SweetAlertDialog sweetAlertDialog;
    private PreferencesHelper preferencesHelper;
    Button btn_load_image_captureID_inidcard, btn_capture_face_inidcard;
    ImageView loadedImage_captureID_inidcard, img_face_inidcard;
    private GifProgressDialog gifProgressDialog;
    private int captureidcardrequestcode=11;
    private GlideRequests request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollactivity_new);
        btn_load_image_captureID_inidcard = findViewById(R.id.btn_load_image_captureID_inidcard);
        loadedImage_captureID_inidcard = findViewById(R.id.loadedImage_captureID_inidcard);
        btn_capture_face_inidcard = findViewById(R.id.btn_capture_face_inidcard);
        img_face_inidcard = findViewById(R.id.img_face_inidcard);
        scrollView = findViewById(R.id.results_view);
        name_hint = findViewById(R.id.name_hint);
        dob_hint = findViewById(R.id.dob_hint);
        country_hint = findViewById(R.id.country_hint);
        email_hint = findViewById(R.id.email_hint);
        gender_hint = findViewById(R.id.et_gender_hint);
        bloodgroup_hint = findViewById(R.id.et_bloodgroup_hint);
        LL_Details_card = findViewById(R.id.LL_Details_card);
        sp_gender_card = findViewById(R.id.sp_gender_card);
        sp_blood_group_card = findViewById(R.id.sp_blood_group_card);
        txtName = findViewById(R.id.txt_name);
        et_name_card = findViewById(R.id.et_name_card);
        et_Dob_card = findViewById(R.id.et_Dob_card);
        et_country_card = findViewById(R.id.et_country_card);
        et_email_card = findViewById(R.id.et_email_card);
        et_gender_card = findViewById(R.id.et_gender_card);
        et_blood_group_card = findViewById(R.id.et_blood_group_card);
       rad_group_choose_from = findViewById(R.id.rad_group_choose_from);
        rad_btn_Enter_Details = findViewById(R.id.rad_btn_Enter_Details);
        rad_btn_CaptureID = findViewById(R.id.rad_btn_CaptureID);
        LL_Details = findViewById(R.id.LL_Details);
        LL_CaptureID = findViewById(R.id.LL_CaptureID);
        et_name = findViewById(R.id.et_name);
        et_Dob = findViewById(R.id.et_Dob);
        et_country = findViewById(R.id.et_country);
        et_email = findViewById(R.id.et_email);
        sp_gender = findViewById(R.id.sp_gender);
        sp_blood_group = findViewById(R.id.sp_blood_group);
        btn_capture_face = findViewById(R.id.btn_capture_face);
        btn_enroll = findViewById(R.id.btn_enroll);
        img_face = findViewById(R.id.img_face);
        load_image_captureID_btn = findViewById(R.id.load_image_captureID);
        loadedImage_captureID = findViewById(R.id.loadedImage_captureID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

        livenessController =
                LivenessController.getInstance(getApplicationContext(), license);


        preferencesHelper = new PreferencesHelper(EnrollActivity.this);
        enrollverifyPresenter = new OnPresenterImpl();
        enrollverifyPresenter.setView(EnrollActivity.this, this);

        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rad_btn_Enter_Details.isChecked()) {
                    if (et_name.getText().toString().length() != 0 && et_Dob.getText().toString().length() != 0 && et_country.getText().toString().length() != 0 && et_email.getText().toString().length() != 0 && sp_gender.getSelectedItemPosition() != 0 && sp_blood_group.getSelectedItemPosition() != 0 && enterdetails_capture_face_uri != null && enterdetails_capture_id_uri != null) {
                        if (isValidEmail(et_email.getText().toString())) {

                            enrollverifyPresenter.enrolldetails(enterdetails_capture_face_uri, enterdetails_capture_id_uri, et_name.getText().toString().trim(), et_Dob.getText().toString().trim(), et_country.getText().toString().trim(), et_email.getText().toString().trim(), sp_gender.getSelectedItem().toString().trim(), sp_blood_group.getSelectedItem().toString().trim());
//                            showMessage("Enrolled");
                        } else {
                            showMessage("Please Enter Valid Email");
                        }
                    } else if (et_name.getText().toString().length() == 0 && et_Dob.getText().toString().length() == 0 && et_country.getText().toString().length() == 0 && et_email.getText().toString().length() == 0 && sp_gender.getSelectedItemPosition() == 0 && sp_blood_group.getSelectedItemPosition() == 0) {
                        showMessage("Please fill all fields");
                    } else if (et_name.getText().toString().length() == 0) {
                        showMessage("Please enter Name");
                    } else if (et_Dob.getText().toString().length() == 0) {
                        showMessage("Please select DOB");
                    } else if (et_country.getText().toString().length() == 0) {
                        showMessage("Please enter Country");
                    } else if (et_email.getText().toString().length() == 0) {
                        showMessage("Please enter Email");
                    } else if (sp_gender.getSelectedItemPosition() == 0) {
                        showMessage("Please select gender");
                    } else if (sp_blood_group.getSelectedItemPosition() == 0) {
                        showMessage("Please select bloodgroup");
                    } else if (enterdetails_capture_face_uri == null) {
                        showMessage("Please Capture Face");
                    } else if (enterdetails_capture_id_uri == null) {
                        showMessage("Please Capture ID");
                    }
                } else {
                    if (enterdetails_capture_face_uri != null && enterdetails_capture_id_uri != null) {

//                    if (enterdetails_capture_id_uri != null) {
                        if (bloodgrp_boolean) {
                            if (sp_blood_group_card.getSelectedItemPosition() != 0) {
                                System.out.println("blood.............2 :" + bloodgroup_card_final);
                                bloodgroup_card_final = sp_blood_group_card.getSelectedItem().toString();
                            }
//                            showMessage("Please select bloodgroup");
                            System.out.println("blood.............3 :" + bloodgroup_card_final);
                        }
                        if (gender_boolean) {
                            if (sp_gender_card.getSelectedItemPosition() != 0) {
                                gender_card_final = sp_gender_card.getSelectedItem().toString();
                            }
                        }

                        if (et_name_card.getText().toString().length() != 0 && et_Dob_card.getText().toString().length() != 0 && et_country_card.getText().toString().length() != 0 && et_email_card.getText().toString().length() != 0 && gender_card_final != null && bloodgroup_card_final != null) {
                            if (isValidEmail(et_email_card.getText().toString())) {
                                enrollverifyPresenter.enrolldetails(enterdetails_capture_face_uri, enterdetails_capture_id_uri, et_name_card.getText().toString().trim(), et_Dob_card.getText().toString().trim(), et_country_card.getText().toString().trim(), et_email_card.getText().toString().trim(), gender_card_final.toString().trim(), bloodgroup_card_final.toString().trim());

//                                showMessage("Enrolled");

                            } else {
                                showMessage("Please Enter Valid Email");
                            }
                        } else if (et_name_card.getText().toString().length() == 0 && et_Dob_card.getText().toString().length() == 0 && et_country_card.getText().toString().length() == 0 && et_email_card.getText().toString().length() == 0 && gender_card_final == null && bloodgroup_card_final == null) {
                            showMessage("Please fill all fields");
                        } else if (et_name_card.getText().toString().length() == 0) {
                            showMessage("Please enter Name");
                        } else if (et_Dob_card.getText().toString().length() == 0) {
                            showMessage("Please select DOB");
                        } else if (et_country_card.getText().toString().length() == 0) {
                            showMessage("Please enter Country");
                        } else if (et_email_card.getText().toString().length() == 0) {
                            showMessage("Please enter Email");
                        } else if (gender_card_final == null) {
                            showMessage("Please select gender");
                        } else if (bloodgroup_card_final == null) {
                            showMessage("Please Select Bloodgroup");
                            System.out.println("blood.............1 :" + bloodgroup_card_final);
                        }
                    } else if (enterdetails_capture_face_uri == null && enterdetails_capture_id_uri == null) {
                        showMessage("Please capture face and IDcard");
                    } else if (enterdetails_capture_face_uri == null) {
                        showMessage("Please Capture Face");
                    } else if (enterdetails_capture_id_uri == null) {
                        showMessage("Please Capture ID");
                    }
                }
            }
        });

        rad_group_choose_from.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rad_btn_Enter_Details) {
                    LL_Details.setVisibility(View.VISIBLE);
                    LL_CaptureID.setVisibility(View.GONE);
                    resetcaptureIDdetails();
                } else if (checkedId == R.id.rad_btn_CaptureID) {
                    LL_Details.setVisibility(View.GONE);
                    LL_CaptureID.setVisibility(View.VISIBLE);
                    resetenterdetails();
                }
            }
        });

        rad_btn_Enter_Details.setChecked(true);

        presenter = new FacerotationPresenterImplementation();
        presenter.setView(getApplicationContext(), this);


        //1987-11-13
        //  DateFormat outFormatter =
        appDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        onDobSetListener = (DatePicker view1, int year, int monthOfYear,
                            int dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            et_Dob.setText(appDateFormat.format(newDate
                    .getTime()));

        };
        et_Dob.setOnClickListener(v -> {
            try {
                Date date = appDateFormat.parse(et_Dob.getText().toString());
                showDatePicker(date, onDobSetListener);
            } catch (ParseException pe) {
                showDatePicker(null, onDobSetListener);
            }
        });

        IDonDobSetListener = (DatePicker view1, int year, int monthOfYear,
                              int dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            et_Dob_card.setText(appDateFormat.format(newDate
                    .getTime()));

        };
        gifProgressDialog = new GifProgressDialog(EnrollActivity.this);
    }

    public void resetenterdetails() {
        et_name.setText("");
        et_Dob.setText("");
        et_country.setText("");
        et_email.setText("");
        sp_gender.setSelection(0);
        sp_blood_group.setSelection(0);
        img_face.setVisibility(View.GONE);
        img_face_inidcard.setVisibility(View.GONE);
        enterdetails_capture_face_uri = null;
        loadedImage_captureID.setVisibility(View.GONE);
        loadedImage_captureID_inidcard.setVisibility(View.GONE);
        enterdetails_capture_id_uri = null;
//        btn_rotateright_id_details.setVisibility(View.GONE);
    }

    public void resetcaptureIDdetails() {
        et_name_card.setText("");
        et_Dob_card.setText("");
        et_country_card.setText("");
        et_email_card.setText("");
        et_gender_card.setText("");
        et_blood_group_card.setText("");
        sp_gender_card.setSelection(0);
        sp_blood_group_card.setSelection(0);
        img_face.setVisibility(View.GONE);
        img_face_inidcard.setVisibility(View.GONE);
        enterdetails_capture_face_uri = null;
        loadedImage_captureID.setVisibility(View.GONE);
        loadedImage_captureID_inidcard.setVisibility(View.GONE);
        enterdetails_capture_id_uri = null;
        LL_Details_card.setVisibility(View.GONE);
        txtName.setText("");
//        btn_rotateright_id_details.setVisibility(View.GONE);
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showDatePicker(Date date, DatePickerDialog.OnDateSetListener dateSetListener) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = null;
        if (date == null) {
            calender = Calendar.getInstance();
        } else {
            calender = Calendar.getInstance();
            calender.setTime(date);
        }

        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        datePickerFragment.setArguments(args);

        /**
         * Set Call back to capture selected date
         */
        datePickerFragment.setCallBack(dateSetListener);
        datePickerFragment.show(getSupportFragmentManager(), "Date Picker");
    }


    public void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_library),
                getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(EnrollActivity.this);
        builder.setTitle(getResources().getString(R.string.take_photo));

        builder.setItems(items, (DialogInterface dialog, int item) -> {

            if (items[item].equals(getResources().getString(R.string.take_photo))) {
                cameraIntent();
            } else if (items[item].equals(getResources().getString(R.string.choose_from_library))) {
                galleryIntent();
            } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                dialog.dismiss();
            }

        });
        builder.show();
    }

    private void cameraIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoURI = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Logger.addToLog(TAG, "cameraintent" + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                String packageName = getPackageName();

                photoURI = FileProvider.getUriForFile(EnrollActivity.this,
                        packageName + ".fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }


    private void galleryIntent() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_GALLERY);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();


        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult() requestCode " + requestCode + " resultCode " + resultCode + " ok " + RESULT_OK);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            try {

                IDimageUri = data.getData();
                enterdetails_capture_id_uri = IDimageUri;
                int rotation = new Utility().getExifRotation(EnrollActivity.this, IDimageUri);

                System.out.println("rotation before.............." + rotation);
                if (rotation != 0) {
                    presenter.rotateImage(EnrollActivity.this, new ImageRotateParams(rotation, IDimageUri));
                    System.out.println("rotation in.............." + rotation);
                } else {
                    System.out.println("........................");

                    if (rad_btn_Enter_Details.isChecked()) {
                        loadedImage_captureID.setVisibility(View.VISIBLE);
                        loadedImage_captureID.setImageURI(IDimageUri);

                    } else {
                        loadedImage_captureID_inidcard.setVisibility(View.VISIBLE);
                        loadedImage_captureID_inidcard.setImageURI(IDimageUri);

                    }
                }
            } catch (Exception e) {
                Logger.addToLog(TAG, "onactivityresult pickfromgallery" + e.getLocalizedMessage());
            }

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            try {

                File capturedImageFile = new File(currentPhotoPath);
                // File capturedImageFile = new File(path);
                if (capturedImageFile.exists()) {
                    enterdetails_capture_id_uri = Uri.fromFile(capturedImageFile);
                    int rotation = new Utility().getExifRotation(EnrollActivity.this, Uri.fromFile(capturedImageFile));

                    System.out.println("rotation before.............." + rotation);
                    if (rotation != 0) {
                        presenter.rotateImage(EnrollActivity.this, new ImageRotateParams(rotation, Uri.fromFile(capturedImageFile)));
                        System.out.println("rotation in.............." + rotation);
                    } else {

                        System.out.println("////////////........................");
                        if (rad_btn_Enter_Details.isChecked()) {
                            loadedImage_captureID.setVisibility(View.VISIBLE);
                            loadedImage_captureID.setImageURI(Uri.fromFile(capturedImageFile));

                        } else {
                            loadedImage_captureID_inidcard.setVisibility(View.VISIBLE);
                            loadedImage_captureID_inidcard.setImageURI(Uri.fromFile(capturedImageFile));

                        }
                    }


                }
            } catch (Exception e) {
                System.out.println(",,,,,,,,,,,,,,,,........................");
                Logger.addToLog(TAG, "onactivityresult fromcamera" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        else if (requestCode == captureidcardrequestcode && resultCode == RESULT_OK) {

           String filepath= data.getStringExtra("captureidfileresult");
           if (filepath!=null) {
               System.out.println("capture id file..................."+filepath);

                   enterdetails_capture_id_uri = Uri.fromFile(new File(filepath));
                   if (rad_btn_Enter_Details.isChecked()) {
                       loadedImage_captureID.setVisibility(View.VISIBLE);

                               loadedImage_captureID.setImageURI(enterdetails_capture_id_uri);
                   } else {
                       loadedImage_captureID_inidcard.setVisibility(View.VISIBLE);
                       loadedImage_captureID_inidcard.setImageURI(enterdetails_capture_id_uri);
                   }

           }
        }
    }

   private void beginCrop(Uri source) throws IOException {

        Log.d(TAG, "source  " + source);

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File destination = File.createTempFile(
                "cropped_img_" + System.currentTimeMillis(),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
//        Uri destination = Uri.fromFile(new File(getExternalFilesDir(), "cropped_img_" + System.currentTimeMillis()));
        Crop.of(source, Uri.fromFile(destination)).start(EnrollActivity.this);
    }

    @Override
    public void onSuccess(byte[] faceImage, byte[] bytes1) {

        if (faceImage != null) {
            showDialog(true, getResources().getString(R.string.please_wait));
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File imagefile = null;
            try {
                imagefile = File.createTempFile(
                        "rotated" + System.currentTimeMillis(),  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                Utility.writeToFile(faceImage, imagefile.getAbsolutePath());
                Uri uri = Uri.fromFile(new File(imagefile.getAbsolutePath()));
                enterdetails_capture_face_uri = uri;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showDialog(false, getResources().getString(R.string.please_wait));
                            if (rad_btn_Enter_Details.isChecked()) {

                                img_face.setVisibility(View.VISIBLE);//
                                Bitmap bitmap = BitmapFactory.decodeByteArray(faceImage, 0, faceImage.length);
                                img_face.setImageBitmap(bitmap);

                            } else {

                                img_face_inidcard.setVisibility(View.VISIBLE);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(faceImage, 0, faceImage.length);
                                img_face_inidcard.setImageBitmap(bitmap);
                            }
                        } catch (Exception e) {
                            showMessage("failed1.." + e);
                            ExceptionUtility.logError(TAG, "sendResult", e);
                        }
                    }
                });


            } catch (Exception e) {
                Logger.addToLog(TAG, "onsuccess " + e.getLocalizedMessage());

                e.printStackTrace();
            }


        }

    }

    private void showDialog(boolean isShow, String message) {
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
        Toast.makeText(EnrollActivity.this, "Failed to capture face image " + s, Toast.LENGTH_LONG).show();
        Logger.addToLog(TAG, "onerror failed to capture face image" + s);

    }


    public void OnCaptureFace(View view) {
        Tech5LivenessRequestModel livenessRequestModel = new Tech5LivenessRequestModel.Builder().create();
        livenessRequestModel.setChallengesCount(0);
        livenessRequestModel.setChallengeStartCounterInSec(1);
        livenessRequestModel.setChallengeTimeoutInSec(10);
        livenessRequestModel.setUseBackCamera(false);

        livenessController
                .detectLivenessFace(livenessRequestModel, this);

    }

    @Override
    public void showProgress() {

        gifProgressDialog.showDialog(R.raw.load, "");
    }

    @Override
    public void showProgress(int drawable) {
        gifProgressDialog.showDialog(drawable, "Registering User");
    }

    @Override
    public void hideProgress() {
        gifProgressDialog.hideDialog();

    }

    @Override
    public void onenrollmentrespone(EnrollResponce response) {
        if (response != null) {
            System.out.println("response.................." + response);
            System.out.println("response2.................." + response.getErrorMessage());
            if (response.getErrorMessage().equals("SUCCESS")) {
                showSuccessAlert("Successfully Enrolled");
            } else if (response.getErrorMessage().equals("Digial Id already exists")) {
                showFailureAlert("EmailID already exists");
            } else if (response.getErrorMessage().equals("ID card face mismatch")) {
                showFailureAlert("ID card face mismatch");
            } else {
                showFailureAlert("Enrollment failed" + response.getErrorMessage());
                Log.d("tag", "enrollfailed" + response.getErrorMessage());
            }
        }
    }

    @Override
    public void onauthenticateresponce(VerifyResponce responce) {

    }

    @Override
    public void onenrollmentFailed(Throwable error) {
        Log.d("tag", "enrollmentFailed " + error.getLocalizedMessage());
        Toast.makeText(this, "failed enrollment" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        error.printStackTrace();
        Logger.addToLog(TAG, "enrollmentFailed " + error.getLocalizedMessage());

    }

    @Override
    public void onauthenticationFailed(Throwable error) {

    }

    @Override
    public void onImageRotated(String path) {

        if (path != null && new File(path).exists()) {

            Uri uri = Uri.fromFile(new File(path));
            enterdetails_capture_id_uri = uri;
            if (rad_btn_Enter_Details.isChecked()) {
                loadedImage_captureID.setVisibility(View.VISIBLE);
                loadedImage_captureID.setImageURI(enterdetails_capture_id_uri);

            } else {
                loadedImage_captureID_inidcard.setVisibility(View.VISIBLE);
                loadedImage_captureID_inidcard.setImageURI(enterdetails_capture_id_uri);

            }

        }
    }

    public byte[] URItogetBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void setThumbnail(Uri uri) {

        try {
            int targetW;
            int targetH;
            // Get the dimensions of the View
            if (rad_btn_Enter_Details.isChecked()) {
                targetW = loadedImage_captureID.getWidth();
                targetH = loadedImage_captureID.getHeight();//

            } else {
                targetW = loadedImage_captureID_inidcard.getWidth();
                targetH = loadedImage_captureID_inidcard.getHeight();

            }

            Log.d(TAG, "imageview dimensions " + targetW + " X " + targetH);

            InputStream input = getApplicationContext().getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();

            Log.d(TAG, "image dimensions " + onlyBoundsOptions.outWidth + " X " + onlyBoundsOptions.outHeight);

            int scaleFactor = Math.min(onlyBoundsOptions.outWidth / targetW, onlyBoundsOptions.outHeight / targetH);

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = scaleFactor;
            input = getApplicationContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();

            Log.d(TAG, "thumb generated");


            if (rad_btn_Enter_Details.isChecked()) {
                if (bitmap != null) {

                    loadedImage_captureID.setImageBitmap(bitmap);
                    Log.d(TAG, "thumb width " + bitmap.getWidth() + " height " + bitmap.getHeight());
                } else {
                    // request.load(uri).into(faceImage);

                    loadedImage_captureID.setImageBitmap(null);
                }

            } else {
                if (bitmap != null) {

                    loadedImage_captureID_inidcard.setImageBitmap(bitmap);
                    Log.d(TAG, "thumb width " + bitmap.getWidth() + " height " + bitmap.getHeight());
                } else {
                    // request.load(uri).into(faceImage);

                    loadedImage_captureID_inidcard.setImageBitmap(null);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.addToLog(TAG, "setthumbnile 1" + e.getLocalizedMessage());

            if (rad_btn_Enter_Details.isChecked()) {
                loadedImage_captureID.setImageBitmap(null);
            } else {
                loadedImage_captureID_inidcard.setImageBitmap(null);
            }
            //
        } catch (Exception e) {
            e.printStackTrace();
            Logger.addToLog(TAG, "setthumbnail 2 " + e.getLocalizedMessage());

            if (rad_btn_Enter_Details.isChecked()) {
                loadedImage_captureID.setImageBitmap(null);
            } else {
                loadedImage_captureID_inidcard.setImageBitmap(null);
            }
        }

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
    protected void onPause() {
        super.onPause();
        hideProgress();
    }

    @Override
    public void onCompleted(int action, DocumentReaderResults results, DocumentReaderException error) {
        //processing is finished, all results are ready
        if (action == DocReaderAction.COMPLETE) {

            displayResults(results);

        } else {
            //something happened before all results were ready
            if (action == DocReaderAction.CANCEL) {
                Toast.makeText(EnrollActivity.this, "Scanning was cancelled", Toast.LENGTH_LONG).show();
            } else if (action == DocReaderAction.ERROR) {
                Toast.makeText(EnrollActivity.this, "Error:" + error, Toast.LENGTH_LONG).show();
            }
        }
    }

    //show received results on the UI
    private void displayResults(DocumentReaderResults results) {
        if (results != null) {
            LL_Details_card.setVisibility(View.VISIBLE);
            String name = results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME_AND_GIVEN_NAMES);

            if (name != null) {
                txtName.setVisibility(View.VISIBLE);
                txtName.setText(name);
            } else {
                txtName.setVisibility(View.GONE);
            }

            String name_card = results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME_AND_GIVEN_NAMES);
            if (name_card != null) {
                et_name_card.setVisibility(View.VISIBLE);
                et_name_card.setText(name_card);
            } else {
                et_name_card.setText("");
            }

            String DOB_card = results.getTextFieldValueByType(eVisualFieldType.FT_DATE_OF_BIRTH);

            if (DOB_card != null) {
                et_Dob_card.setVisibility(View.VISIBLE);
                et_Dob_card.setText(DOB_card);
            } else {

                et_Dob_card.setOnClickListener(v -> {
                    try {
                        Date date = appDateFormat.parse(et_Dob_card.getText().toString());
                        showDatePicker(date, IDonDobSetListener);
                    } catch (ParseException pe) {
                        showDatePicker(null, IDonDobSetListener);
                    }
                });
            }
            String country_card = results.getTextFieldValueByType(eVisualFieldType.FT_NATIONALITY);
            if (country_card != null) {
                et_country_card.setVisibility(View.VISIBLE);
                et_country_card.setText(country_card);
            } else {
                et_country_card.setText("");
            }
            String gender_card = results.getTextFieldValueByType(eVisualFieldType.FT_SEX);
            if (gender_card != null) {
                gender_boolean = false;
                sp_gender_card.setVisibility(View.GONE);
                et_gender_card.setVisibility(View.VISIBLE);
                et_gender_card.setText(gender_card);
                gender_card_final = et_gender_card.getText().toString();
            } else {
                gender_boolean = true;
                et_gender_card.setVisibility(View.GONE);
                gender_hint.setVisibility(View.GONE);
                sp_gender_card.setVisibility(View.VISIBLE);

            }
            String blood_card = results.getTextFieldValueByType(eVisualFieldType.FT_BLOOD_GROUP);
            if (blood_card != null) {
                bloodgrp_boolean = false;
                sp_blood_group_card.setVisibility(View.GONE);
                et_blood_group_card.setVisibility(View.VISIBLE);
                et_blood_group_card.setText(blood_card);
                bloodgroup_card_final = et_blood_group_card.getText().toString();
            } else {
                bloodgrp_boolean = true;
                et_blood_group_card.setVisibility(View.GONE);
                bloodgroup_hint.setVisibility(View.GONE);
                sp_blood_group_card.setVisibility(View.VISIBLE);

            }
        Bitmap portrait = results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT);
            if (portrait != null) {
                enterdetails_capture_id_uri = getImageUri(EnrollActivity.this, portrait);

                if (rad_btn_Enter_Details.isChecked()) {
                    loadedImage_captureID.setVisibility(View.VISIBLE);
                    loadedImage_captureID.setImageBitmap(portrait);
                } else {

                    loadedImage_captureID_inidcard.setVisibility(View.VISIBLE);//loadedImage_captureID_inidcard
                    loadedImage_captureID_inidcard.setImageBitmap(portrait);
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    private void showDemographics(HashMap<String, String> demogs, boolean show) {
        scrollView.removeAllViews();
        scrollView.setVisibility(View.VISIBLE);

        if (show) {

            Log.d("TAG", "show demographics called");

            if (null != demogs && demogs.size() > 0) {

                LinearLayout outerLayout = new LinearLayout(getApplicationContext());
                outerLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                outerLayout.setLayoutParams(LLParams);

                LayoutInflater inflater;


                for (String keyText : demogs.keySet()) {
                    LinearLayout innerLayout = new LinearLayout(getApplicationContext());

                    innerLayout.setOrientation(LinearLayout.VERTICAL);
                    innerLayout.setLayoutParams(LLParams);
                    inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View resultView = inflater.inflate(R.layout.result_template, null);
                    View resultView = inflater.inflate(R.layout.cardresult, null);

                    TextView key = resultView.findViewById(R.id.key);
                    EditText value = resultView.findViewById(R.id.value);

                    key.setText(keyText);
//                    value.setHint(keyText);
                    value.setText(demogs.get(keyText));

                    innerLayout.addView(resultView);
                    outerLayout.addView(innerLayout);
                }
                scrollView.addView(outerLayout);
            } else {
                scrollView.setVisibility(View.GONE);
            }
        } else {

            scrollView.setVisibility(View.GONE);
            txtName.setText("");
            if (rad_btn_Enter_Details.isChecked()) {
                loadedImage_captureID.setImageBitmap(null);

            } else {
                loadedImage_captureID_inidcard.setImageBitmap(null);

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
        gifProgressDialog.hideDialog();

        presenter.destroy();
        enrollverifyPresenter.destroy();
    }

    private void hideProgresscard() {
        gifProgressDialog.hideDialog();

    }

    public void captureIDCard(View view) {
        Intent intent = new Intent(EnrollActivity.this, CaptureIdCard.class);
        startActivityForResult(intent, captureidcardrequestcode);


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

                                    Intent i = new Intent(EnrollActivity.this, GuidingScreen.class);
                                    startActivity(i);
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

    public void OnCaptureFace_inIdcard(View view) {
        Tech5LivenessRequestModel livenessRequestModel = new Tech5LivenessRequestModel.Builder().create();
        livenessRequestModel.setChallengesCount(0);
        livenessRequestModel.setChallengeStartCounterInSec(1);
        livenessRequestModel.setChallengeTimeoutInSec(10);
        livenessRequestModel.setUseBackCamera(false);

        livenessController
                .detectLivenessFace(livenessRequestModel, this);

    }

    public void captureIDCard_inidcard(View view) {

        if (preferencesHelper.isOcrEnabled()) {
            showDemographics(null, false);
            DocumentReader.Instance().showScanner(EnrollActivity.this, this);
        } else {
            showMessage("Initializing Sdk.Please Wait");
        }
    }

    public void Onrotateright(View view) {
        if (enterdetails_capture_id_uri != null) {
            //rotate 90 degrees right
            presenter.rotateImage(this, new ImageRotateParams(90, enterdetails_capture_id_uri));

        } else {
            showMessage("Image Not Added");

        }

    }
}