package ai.tech5.tech5.enroll.foruploadandverify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.face.liveness.BuildConfig;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hdbarcode.hdbarcodereader.DecdeHDBarcodeActivity;

import java.io.File;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.dialog.RestartDialog;
import ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo.Result;
import ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo.ResultObject;
import ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo.TaskListener;
import ai.tech5.tech5.enroll.hdbarcodedemo.Listener;
import ai.tech5.tech5.enroll.hdbarcodedemo.Utilities;
import ai.tech5.tech5.enroll.model.Configuration;
import ai.tech5.tech5.enroll.preferences.AppSharedPreference;
import ai.tech5.tech5.enroll.utils.AppUtils;
import ai.tech5.tech5.enroll.utils.Logger;

import static ai.tech5.tech5.enroll.utils.Logger.logException;


public class ResultActivity extends AppCompatActivity implements TaskListener {


    private final static String TAG = "ResultActivity";
    private ScrollView resultScrollView;
    private ImageView face;
    private TextView matchResult, name, designation, tvAppVersion, heading;
    private static int cameraFacing;
    private Utilities utilities;
    private int capturedFinger = 0;
    private String formattedDate = null;
    private android.app.AlertDialog verifyOptionsAlertDialog = null;
    private ImageView verify_with_fingerprint;
    private ImageView verify_with_face;

    private int failedCount = 0;
    public static int brightness;
    //  private boolean isDebugModeEnabled;
    // BiometricsGestureController biometricsGestureController;
    private boolean isFingerClientInitialized = false;
    private final String LIC_EXT = ".lic";
    public final int TYPE_UNKNOWN = -1;
    public final int TYPE_WSQ = 0;
    public final int TYPE_BMP = 1;
    public final int TYPE_JP2 = 2;
    public final int TYPE_JPEG = 3;
    private double matchScore;

    private TextView tvLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 123;

    private String currentLatLong = "NA";
    private AppSharedPreference appSharedPreference;
    private AppUtils appUtils;

    private float fingerThreshold = 0.10f;

    private int maxTemplateSize = 180;

    static final int REQUEST_SCAN = 20;
//jss

    private Uri photoURI;
    private static final int REQUEST_CAMERA = 222;
    private static final int SELECT_FILE = 333;
    private String selfieImagePath = null;
    //    ImageView img_face;
    private Uri currentImageUri;
    private byte[] selfieBytes;
    //    private FacerotationPresenter presenter;
    private ProgressDialog progressDialog;
    //    FingerprintUtils fingerprintUtils;
    public static final String FROM_WHERE = "fromWhere";
    String selectedbbccfile;
    ImageView img_bar_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        img_bar_code = findViewById(R.id.img_bar_code);
        heading = findViewById(R.id.heading);
        System.err.println(":::ResultActivity:::");
        Bundle bundle = getIntent().getExtras();
        selectedbbccfile = bundle.getString("selectedbbccfile");

        if (selectedbbccfile != null) {
            File faceImageFile = new File(selectedbbccfile);
            if (faceImageFile.exists()) {
                img_bar_code.setImageURI(Uri.fromFile(new File(faceImageFile.getAbsolutePath())));
            } else {
                img_bar_code.setImageBitmap(null);
            }
        }


        appUtils = new AppUtils(this);
//        fingerprintUtils = new FingerprintUtils(com.example.verifyexample.ResultActivity.this);

        progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        tvLocation = findViewById(R.id.tvLocation);
        tvLocation.setVisibility(View.GONE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        appSharedPreference = new AppSharedPreference(this);
        utilities = new Utilities(this);
        resultScrollView = findViewById(R.id.resultScrollView);
        face = findViewById(R.id.face);
        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
        tvAppVersion = findViewById(R.id.tv_app_version);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

//        presenter = new FacerotationPresenterImplementation();
//        presenter.setView(getApplicationContext(), this);

//        livenessController =
//                LivenessController.getInstance(getApplicationContext(), license);


        tvAppVersion.setText("Build version " + BuildConfig.VERSION_NAME);
        //   brightness = utilities.getCurrentBrightness();

        matchResult = findViewById(R.id.matchResult);
//        Button back = findViewById(R.id.scan_again);

        showDemographics(true);//jsschange..........

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Result.reset();
//                    Intent intent = null;
//                    String fromWhere = bundle.getString(FROM_WHERE);
//                    if (fromWhere != null && fromWhere.equalsIgnoreCase("Decode")) {
//                        intent = new Intent(ResultActivity.this, DecdeHDBarcodeActivity.class);
//                    } else {
////                        intent = new Intent(ResultActivity.this, NewScanBarCodeActivity.class);
//
//                    }
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    //intent.putExtra(IS_DEBUG_MODE, isDebugModeEnabled);
//                    startActivity(intent);
//                    finish();
//                } catch (Exception e) {
//                    logException(TAG, e);
//                }
//            }
//        });
//        Button match_with_template = findViewById(R.id.match_with_template);
//        match_with_template.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "Button match_with_template() clicked");
//
//                try {
////                    if (null != Result.demographics.get("Barcode Validity")) {
//                    formattedDate = (null != Result.demographics && null != Result.demographics.get("Date Of Issue")) ? utilities.getIssueDate(Result.demographics.get("Date Of Issue")) : "";
//                    String numDays = (null != Result.demographics && null != Result.demographics.get("Barcode Validity")) ? Result.demographics.get("Barcode Validity") : "";
//                    if (utilities.isBarcodeValid(formattedDate, numDays)) {
//                        if ((null != Result.faceTemplate || null != Result.compressedImage) && (null != Result.fingerprints && Result.fingerprints.size() > 0)) {
//                            verifyOptions();
//                        } else if ((null != Result.faceTemplate || null != Result.compressedImage)) {
//                            showDemographics(false);
//                            System.err.println(":::::::::::MATCH FACE TASK STARTED::::::::::::");
//                            Logger.addToLog(TAG, ":::::::::::MATCH FACE TASK STARTED::::::::::::");
//                            //utilities.setScreenBrightness(255);
//                            matchResult.setVisibility(View.GONE);
//
//                            captureFace();
//
//
//                            // Intent intent = new Intent(ResultActivity.this, CamActivity.class);
//                            //startActivityForResult(intent, 200);
//                        } else {
//                            AlertDialog alertDialog = new AlertDialog.Builder(ResultActivity.this).create();
//                            alertDialog.setTitle("Tech5 HDBarcode");
//                            alertDialog.setMessage("HD Barcode does't contain face or fingerprint template !");
//                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                            alertDialog.show();
//                        }
//                    } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
//                        builder.setTitle("Tech5 HDBarcode");
//                        builder.setMessage("Barcode Expired! Please contact tech5.ai");
//                        builder.setCancelable(false);
//                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Result.reset();
//                                Intent intent = null;
//                                String fromWhere = bundle.getString(FROM_WHERE);
//                                if (fromWhere != null && fromWhere.equalsIgnoreCase("Decode")) {
//                                    intent = new Intent(ResultActivity.this, DecdeHDBarcodeActivity.class);
//                                } else {
////                                    intent = new Intent(ResultActivity.this, NewScanBarCodeActivity.class);
//                                }
//                                // intent.putExtra(IS_DEBUG_MODE, isDebugModeEnabled);
//                                startActivity(intent);
//                            }
//                        });
//                        builder.show();
//                    }
////                    } else {
////                        AlertDialog alertDialog = new AlertDialog.Builder(ResultActivity.this).create();
////                        alertDialog.setTitle("Tech5 HDBarcode");
////                        alertDialog.setMessage("HD Barcode does't contain face or fingerprint template !");
////                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
////                                new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        dialog.dismiss();
////                                    }
////                                });
////                        alertDialog.show();
////                    }
//                } catch (Exception e) {
//                    logException(TAG, e);
//                }
//            }
//        });

        if (Listener.decompressor != null) {
            try {
                System.out.println("1..................");
                if (Result.compressedImage == null) {
                    System.out.println("2..................");
                    Intent intent = new Intent(ResultActivity.this, DecdeHDBarcodeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    System.out.println("3..................");
                    byte[] jpegData = Listener.decompressor.decompressImage(Result.compressedImage);
//                //      byte[] jpegData = Result.compressedImage;
//                DecompressorConfig decompressorConfig = new DecompressorConfig();
//                decompressorConfig.decompressorVersion = 100;
//                FaceDecompressor decompressor = new FaceDecompressor(decompressorConfig);
//                byte[] jpegData = decompressor.decompressImage(Result.compressedImage);
                    Result.decompressedImage = jpegData;
                    if (null != jpegData) {
                        System.out.println("4..................");
                        System.out.println("jpegData.................." + jpegData.length);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);
//                    byte[] byteImage = encodeToBase64(bitmap);
//                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteImage, 0, jpegData.length);
                        // Bitmap is= getResizedBitmap(bitmap,500);
                        // MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "bitmapis" , "description");
                        face.setImageBitmap(bitmap);
                    } else {
                        System.out.println("5..................");
                        face.setImageDrawable(getResources().getDrawable(R.drawable.user_avatar));
                    }
                    System.out.println("6..................");
                }
            } catch (Exception e) {
                System.out.println("7..................");
                logException(TAG, e);
                face.setImageDrawable(getResources().getDrawable(R.drawable.user_avatar));
            }
        } else {
            RestartDialog.showRestartAppDialog(ResultActivity.this);
        }


//        //getSdkInstance();
//        prepareVerifyOptionsDialog();

        Configuration configuration = utilities.loadConfiguration();


        if (configuration != null && configuration.barcodeGenerationParameters != null && configuration.barcodeGenerationParameters.fingerprintBiometricTemplates != null) {
            maxTemplateSize = configuration.barcodeGenerationParameters.fingerprintBiometricTemplates.size;
            if (maxTemplateSize < 180 || maxTemplateSize > 500) {
                maxTemplateSize = 180;
            }
        }
    }

//    public byte[] compressByteArray(byte[] bytes) {
//
//        ByteArrayOutputStream baos = null;
//        Deflater dfl = new Deflater();
//        dfl.setLevel(Deflater.BEST_COMPRESSION);
//        dfl.setInput(bytes);
//        dfl.finish();
//        baos = new ByteArrayOutputStream();
//        byte[] tmp = new byte[4 * 1024];
//        try {
//            while (!dfl.finished()) {
//                int size = dfl.deflate(tmp);
//                baos.write(tmp, 0, size);
//            }
//        } catch (Exception ex) {
//
//        } finally {
//            try {
//                if (baos != null) baos.close();
//            } catch (Exception ex) {
//            }
//        }
//
//        return baos.toByteArray();
//    }

//    public byte[] decompressByteArray(byte[] bytes) {
//
//        ByteArrayOutputStream baos = null;
//        Inflater iflr = new Inflater();
//        iflr.setInput(bytes);
//        baos = new ByteArrayOutputStream();
//        byte[] tmp = new byte[4 * 1024];
//        try {
//            while (!iflr.finished()) {
//                int size = iflr.inflate(tmp);
//                baos.write(tmp, 0, size);
//            }
//        } catch (Exception ex) {
//
//        } finally {
//            try {
//                if (baos != null) baos.close();
//            } catch (Exception ex) {
//            }
//        }
//
//        return baos.toByteArray();
//    }

//    public static byte[] decompress(byte[] in) {
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            InflaterOutputStream infl = new InflaterOutputStream(out);
//            infl.write(in);
//            infl.flush();
//            infl.close();
//
//            return out.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(150);
//            return null;
//        }
//    }

//    public static byte[] encodeToBase64(Bitmap image) {
//        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 25, byteArrayOS);
//        byte[] byteArray = byteArrayOS.toByteArray();
//        return byteArray;
//    }

//    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        float bitmapRatio = (float) width / (float) height;
//        if (bitmapRatio > 1) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ACCESS_COARSE_LOCATION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        Log.i(TAG, "getLastLocation");
        if (checkPermissions()) {
            Log.i(TAG, "isLocationEnabled: " + isLocationEnabled());
            Logger.addToLog(TAG, "isLocationEnabled: " + isLocationEnabled());
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();

                                Log.i(TAG, "onComplete");
                                Logger.addToLog(TAG, "onComplete");
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    currentLatLong = location.getLatitude() + ", " + location.getLongitude();
//                                    tvLocation.setText(currentLatLong);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            currentLatLong = mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude();
            tvLocation.setText(currentLatLong);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume() called");
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() called");
        super.onPause();
    }


//    private void matchFaces() {
//        Logger.addToLog(TAG, "matchFaces() called ");
//        try {
//            matchResult.setText("");
//            if (null == Result.capturedFaceBytes) {
//                Toast.makeText(this, "Please capture face image from Camera", Toast.LENGTH_SHORT).show();
//            } else {
//
//                MatchWithTemplateTask task = new MatchWithTemplateTask(ResultActivity.this, Result.faceTemplate, Result.capturedFaceBytes, Result.decompressedImage);
//                task.execute();
//
//            }
//        } catch (Exception e) {
//            logException(TAG, e);
//        }
//
//    }

    private void showDemographics(boolean show) {
        resultScrollView.removeAllViews();
        resultScrollView = null;
        resultScrollView = findViewById(R.id.resultScrollView);
        if (show) {
            if (null != Result.demographics) {
                System.out.println("Result.demographics..............................." + Result.demographics);
                Logger.addToLog(TAG, Result.demographics.toString());
                if (null != Result.demographics.get("Name")) {
                    name.setVisibility(View.VISIBLE);
                    name.setText(Result.demographics.get("Name"));
                } else if (null != Result.demographics.get("FirstName") && null != Result.demographics.get("LastName")) {
                    name.setText(Result.demographics.get("FirstName") + " " + Result.demographics.get("LastName"));
                    System.out.println("@@@@@@@@@@@@" + Result.demographics.get("FirstName"));
                    Logger.addToLog(TAG, "@@@@@@@@@@@@" + Result.demographics.get("FirstName"));
                    System.out.println("@@@@@@@@@@@@" + Result.demographics.get("LastName"));
                    Logger.addToLog(TAG, "@@@@@@@@@@@@" + Result.demographics.get("LastName"));
                    name.setVisibility(View.VISIBLE);
                } else {
                    name.setVisibility(View.GONE);
                }
                if (null != Result.demographics.get("Designation")) {
                    designation.setVisibility(View.VISIBLE);
                    designation.setText(Result.demographics.get("Designation"));
                } else {
                    designation.setVisibility(View.GONE);
                }

                heading.setVisibility(View.VISIBLE);
                heading.setText("VACCINATION DETAILS");
            }
            if (null != Result.demographics && Result.demographics.size() > 0) {

                LinearLayout outerLayout = new LinearLayout(getApplicationContext());
                outerLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                outerLayout.setLayoutParams(LLParams);

                LayoutInflater inflater;

//                Result.demographics.put("GPS Location", currentLatLong);
                resultScrollView.setVisibility(View.VISIBLE);
                for (String keyText : Result.demographics.keySet()) {
                    if (keyText != "Country" && keyText != "Gender" && keyText != "Blood Group") {

                        LinearLayout innerLayout = new LinearLayout(getApplicationContext());

                        innerLayout.setOrientation(LinearLayout.VERTICAL);
                        innerLayout.setLayoutParams(LLParams);
                        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View resultView = inflater.inflate(R.layout.result_template, null);

                        TextView key = resultView.findViewById(R.id.key);
                        TextView value = resultView.findViewById(R.id.value);
//
//                    if ("Date Of Issue".equalsIgnoreCase(keyText)) {
//                        formattedDate = (null != Result.demographics.get(keyText)) ? utilities.getIssueDate(Result.demographics.get(keyText)) : "";
//                        key.setText(keyText);
//                        value.setText(null != formattedDate ? formattedDate : "-");
//
//
//                    } else if ("Valid upto".equalsIgnoreCase(keyText)) {
//
//                        formattedDate = (null != Result.demographics.get(keyText)) ? utilities.getIssueDate(Result.demographics.get(keyText)) : "";
//                        key.setText(keyText);
//                        value.setText(null != formattedDate ? formattedDate : "-");
//
//                    } else if ("Barcode Validity".equalsIgnoreCase(keyText)) {
//                        String expiryDate = (null != Result.demographics.get(keyText) && null != formattedDate) ? utilities.getExpiryDate(formattedDate, Result.demographics.get(keyText)) : "";
//                        key.setText(keyText);
//                        value.setText(null != expiryDate ? expiryDate : "-");
//                    } else {
//
                        if (keyText != "Date Of Birth") {
                            key.setText(keyText);
                            value.setText(": " + Result.demographics.get(keyText));
//                    }
                        }

                        if (keyText == "Cartridge ID") {
                            LinearLayout innerLayout2 = new LinearLayout(getApplicationContext());

                            innerLayout2.setOrientation(LinearLayout.VERTICAL);
                            innerLayout2.setLayoutParams(LLParams);
                            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View resultView2 = inflater.inflate(R.layout.result_template, null);

                            innerLayout2.addView(resultView2);
                            outerLayout.addView(innerLayout2);

                        }

//                        else {
//                            key.setText(keyText);
//                            value.setText(": " + Result.demographics.get(keyText));
////
//                        }

//                        else {
//                            View v = new View(this);
//                            v.setLayoutParams(new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.MATCH_PARENT,
//                                    5
//                            ));
//                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
//                            innerLayout.addView(v);
//
//                            key.setText("Scanning Details");
//                            key.setTextColor(Color.parseColor("#FF000000"));
//                            key.setTypeface(key.getTypeface(), Typeface.BOLD);
//                            key.setGravity(Gravity.CENTER);
////                            /////////////////////////////     trailllllllll
////                            LinearLayout innerLayout2 = new LinearLayout(getApplicationContext());
////
////                            innerLayout2.setOrientation(LinearLayout.VERTICAL);
////                            innerLayout2.setLayoutParams(LLParams);
////                            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                            View resultView2 = inflater.inflate(R.layout.result_template, null);
////
////                            TextView key1 = resultView2.findViewById(R.id.key);
//////                            TextView value = resultView2.findViewById(R.id.value);
//////
////
////                            ////////////////////////////
////                            key1.setText("Scanning Details");
////                            key1.setTextColor(Color.parseColor("#FF000000"));
////                            key1.setTypeface(key.getTypeface(), Typeface.BOLD);
////                            key1.setGravity(Gravity.CENTER);
////
////                            innerLayout2.addView(resultView2);
////                            outerLayout.addView(innerLayout2);
//
//                        }
                        innerLayout.addView(resultView);
                        outerLayout.addView(innerLayout);

                    }
                }
                resultScrollView.addView(outerLayout);
            } else {
                resultScrollView.setVisibility(View.GONE);
            }
        } else {
            name.setVisibility(View.GONE);
            designation.setVisibility(View.GONE);
            resultScrollView.setVisibility(View.GONE);
        }
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void processFinish(ResultObject result) {

        Logger.addToLog(TAG, "processFinish() called :score  " + result.getMatchingScore());


        matchResult.setVisibility(View.VISIBLE);
        if (result.getMatchingScore() > 5) {
            matchResult.setTextColor(getResources().getColor(R.color.green));
            matchResult.setText("Match (score " + String.format("%.2f", result.getMatchingScore()) + ")");
            showDemographics(true);
        } else {
            showDemographics(false);
            matchResult.setTextColor(getResources().getColor(R.color.light_red));
            matchResult.setText("No Match (score " + String.format("%.2f", result.getMatchingScore()) + ")");
        }
    }

//    public void verifyOptions() {
//
//        Log.d(TAG, "verifyOptions() called");
//
//        if (verifyOptionsAlertDialog != null && verifyOptionsAlertDialog.isShowing()) {
//            verifyOptionsAlertDialog.dismiss();
//        }
//
//
//        verify_with_fingerprint.setEnabled(true);
//        verify_with_face.setEnabled(true);
//        verifyOptionsAlertDialog.show();
//    }

//    public void prepareVerifyOptionsDialog() {
//
//        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.verify_options_activity, null);
//        verify_with_face = dialogView.findViewById(R.id.verify_with_face);
//
//        verify_with_face.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.d(TAG, "verify_with_face() clicked");
//                verifyOptionsAlertDialog.dismiss();
//                verify_with_fingerprint.setEnabled(false);
//                verify_with_face.setEnabled(false);
//
//                try {
//                    showDemographics(false);
//                    matchResult.setVisibility(View.GONE);
//
//                    System.err.println(":::::::::::MATCH FACE TASK STARTED::::::::::::");
//                    Logger.addToLog(TAG, ":::::::::::MATCH FACE TASK STARTED::::::::::::");
//                    //  Intent intent = new Intent(ResultActivity.this, CamActivity.class);
//
//                    captureFace();
//                    // startActivityForResult(intent, 200);
//                } catch (Exception e) {
//                    logException(TAG, e);
//                }
//
//            }
//        });
//
//        dialogBuilder.setView(dialogView);
//        verifyOptionsAlertDialog = dialogBuilder.create();
//        verifyOptionsAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        verifyOptionsAlertDialog.setCancelable(true);
//
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CAMERA && resultCode == AppCompatActivity.RESULT_OK) {
//            Log.d("TAG", "onActivityResult() called   REQUEST_CAMERA");
//            onCaptureImageResult(data);
//        }
//    }

//    public byte[] URItogetBytes(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//
//        int len = 0;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//        return byteBuffer.toByteArray();
//    }

//    private void onCaptureImageResult(Intent data) {
//
////        File cameraPhoto = new File(currentPhotoPath);
//        File cameraPhoto = new File(selfieImagePath);
//
//        if (cameraPhoto.exists()) {
//
//
//            int rotation = new Utility().getExifRotation(this, photoURI);
//            if (rotation != 0) {
////                presenter.rotateImage(getApplicationContext(), new ImageRotateParams(rotation, photoURI));
//            } else {
//
////            img_face.setImageBitmap(null);
//                currentImageUri = photoURI;
////            img_face.setVisibility(View.VISIBLE);
////            img_face.setImageURI(currentImageUri);
//                //  request.load(photoURI).into(faceImage);
//
//                InputStream iStream = null;
//                try {
//                    iStream = getContentResolver().openInputStream(currentImageUri);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    selfieBytes = URItogetBytes(iStream);
////                compressFaceImage(selfieBytes);
//                    Logger.addToLog(TAG, "Face image size : " + selfieBytes.length);
//
//                    Result.capturedFaceBytes = selfieBytes;
//
//                    matchFaces();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//
//        }
//
//
//    }


//    private void showDialog(boolean isShow) {
//        this.runOnUiThread(() -> {
//            if (isShow) {
//                showProgress();
//            } else {
//                hideProgress();
//            }
//        });
//    }

//    private void showMessage(String message) {
//        this.runOnUiThread(() -> {
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//        });
//    }

//    private void captureFace() {
//        Logger.addToLog(TAG, "captureFace() called");
//        cameraIntent();
//        Logger.addToLog(TAG, "captureFace() called end");
//
//
//    }

//    private void cameraIntent() {
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            photoURI = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ex.printStackTrace();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//
//                String packageName = this.getPackageName();
//
//                photoURI = FileProvider.getUriForFile(this,
//                        packageName + ".fileprovider",
//                        photoFile);
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
//            }
//        }
//    }

//    private void galleryIntent() {
//
//        Log.d("TAG", "galleryIntent");
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
//    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
//        String imageFileName = timeStamp + "_";
//        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//
//        // Save a file: path for use with ACTION_VIEW intents
//        //  currentPhotoPath = image.getAbsolutePath();
//        selfieImagePath = image.getAbsolutePath();
//
//
//        return image;
//    }


//    private String getDateTime() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
//        Date date = new Date();
//        return dateFormat.format(date);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (verifyOptionsAlertDialog != null && verifyOptionsAlertDialog.isShowing()) {
            verifyOptionsAlertDialog.dismiss();
        }
//        presenter.destroy();
    }


//    private String getDirName() {
//        if (null != Result.demographics && null != Result.demographics.get("name")) {
//            return Result.demographics.get("name") + "_" + getDateTime();
//        } else {
//            return appUtils.getUserDirectoryName();
//        }
//    }


    //    @Override
    public void showProgress() {
        progressDialog.show();
    }

    //
//    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
//
//    @Override
//    public void onImageRotated(String path) {
//
//        if (path != null && new File(path).exists()) {
////            img_face.setImageBitmap(null);
////
//            Uri uri = Uri.fromFile(new File(path));
//            //  request.load(uri).into(faceImage);
//            setThumbnail(uri);
//            currentImageUri = uri;
//
//            InputStream iStream = null;
//            try {
//                iStream = getContentResolver().openInputStream(currentImageUri);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            try {
//                selfieBytes = URItogetBytes(iStream);
//                Logger.addToLog(TAG, "Face image size : " + selfieBytes.length);
//
//                Result.capturedFaceBytes = selfieBytes;
//                // face.setImageBitmap(null);
//                matchFaces();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }

//    public void setThumbnail(Uri uri) {
//
//        try {
//            // Get the dimensions of the View
//            int targetW = face.getWidth();
//            int targetH = face.getHeight();
//
//            Log.d(TAG, "imageview dimensions " + targetW + " X " + targetH);
//
//            InputStream input = getApplicationContext().getContentResolver().openInputStream(uri);
//            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
//            onlyBoundsOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
//            input.close();
//
//            Log.d(TAG, "image dimensions " + onlyBoundsOptions.outWidth + " X " + onlyBoundsOptions.outHeight);
//
//            int scaleFactor = Math.min(onlyBoundsOptions.outWidth / targetW, onlyBoundsOptions.outHeight / targetH);
//
//            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            bitmapOptions.inSampleSize = scaleFactor;
//            input = getApplicationContext().getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//            input.close();
//
//            Log.d(TAG, "thumb generated");
//
//
//            if (bitmap != null) {
//
//                // face.setImageBitmap(bitmap);
//                Log.d(TAG, "thumb width " + bitmap.getWidth() + " height " + bitmap.getHeight());
//            } else {
//                // request.load(uri).into(faceImage);
//
//                //    face.setImageBitmap(null);
//            }
//
//        } catch (FileNotFoundException e) {
//            //  face.setImageBitmap(null);
//        } catch (Exception e) {
//            //face.setImageBitmap(null);
//        }
//
//    }


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
