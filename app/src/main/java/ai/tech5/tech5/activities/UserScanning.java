package ai.tech5.tech5.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.Logger;

public class UserScanning extends AppCompatActivity {
    Button scan_bt;
    //    private IntentIntegrator qrScan;
    private ListenableFuture cameraproviderfuture;
    private ExecutorService cameraexecutor;
    private PreviewView previewView;
    MyImageAnalyser analyser;
    ImageView imageView;
    Bitmap bm;
    BarcodeScanner scanner;
    String ScanFrom;
    TextView scanmessage;
    Context context;
    public static final String TAG = SplashActivity.class.getSimpleName();

    //    HashMap<String, String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scanning);
        scanmessage = findViewById(R.id.scanmessage);
//        scan_bt = findViewById(R.id.scan_bt);
        previewView = findViewById(R.id.previewview);
        imageView = findViewById(R.id.image);
        this.getWindow().setFlags(1024, 1024);

        cameraexecutor = Executors.newSingleThreadExecutor();
        cameraproviderfuture = ProcessCameraProvider.getInstance(this);
        analyser = new MyImageAnalyser(getSupportFragmentManager());
//         bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
//        bm=getbitmap();
//        scan_qr(imageView);
        ScanFrom = getIntent().getStringExtra("ScanFrom");
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();

        scanner = BarcodeScanning.getClient(options);

        cameraproviderfuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraproviderfuture.get();
                    bindpreview(processCameraProvider);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));

    }

    private void bindpreview(ProcessCameraProvider processCameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        ImageCapture imageCapture = new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraexecutor, analyser);
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    public class MyImageAnalyser implements ImageAnalysis.Analyzer {
        private FragmentManager fragmentManager;

        public MyImageAnalyser(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanbarcode(image);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void scanbarcode(ImageProxy image) {
        @SuppressLint("UnsafeExperimentalUsageError") Image image1 = image.getImage();
        assert image1 != null;
        InputImage inputImage = InputImage.fromMediaImage(image1, image.getImageInfo().getRotationDegrees());
        Task<List<Barcode>> result = scanner.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        // ...
                        scanmessage.setVisibility(View.GONE);
                        Log.d("TAG", "on success......");
                        readerBarcodeData(barcodes);
                        image.close();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.d("TAG", "on failure......");
                        image.close();

                        showtoast("Please Scan Again");

                        e.printStackTrace();
                        System.out.println("exception..........." + e.getLocalizedMessage());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Barcode>> task) {
                        scanmessage.setVisibility(View.VISIBLE);
                        Log.d("TAG", "on addOnCompleteListener......");

                    }
                });


    }


    private void readerBarcodeData(List<Barcode> barcodes) {
        for (Barcode barcode : barcodes) {
            System.out.println("barcodes........." + barcode);
            System.out.println("barcodesgetDisplayValue........." + barcode.getDisplayValue());
            String jsonobjectstring = barcode.getDisplayValue();


            try {

                JSONObject jsonObject = new JSONObject(jsonobjectstring);
                System.out.println("jsonObject1........." + jsonObject.getString("type"));
                System.out.println("jsonObject2........." + jsonObject.getString("id"));

                if (ScanFrom.equals("1")) {
                    if (jsonObject.getString("type").equals("Scan Kit")) {
                        ScanKitVaccineBox.scankit_tv.setText("Id : " + jsonObject.getString("id") + ","
                                + "\r\n" + "Kit Details:" + jsonObject.getString("Kit_Details") + ","
                                + "\r\n" + "Package Date : " + jsonObject.getString("Package_Date"));

                    } else {
                        ScanKitVaccineBox.scankit_tv.setText("");
                        showtoast("Invalid QR");
                    }
                } else if (ScanFrom.equals("2")) {
                    if (jsonObject.getString("type").equals("Scan VaccineBox")) {
                        ScanKitVaccineBox.vaccinebox_tv.setText("Id : " + jsonObject.getString("id") + ","
                                + "\r\n" + "Details Of Contents : " + jsonObject.getString("Deatils_of_contents") + ","
                                + "\r\n" + "Package Date : " + jsonObject.getString("Package_date")
                        );

                    } else {
                        ScanKitVaccineBox.vaccinebox_tv.setText("");
                        showtoast("Invalid QR");
                    }

                } else if (ScanFrom.equals("3")) {
                    if (jsonObject.getString("type").equals("Scan Catridge")) {
                        ScanCatridgeInjectorVedioGenerate.scancatridge_tv.setText("Id : " + jsonObject.getString("id") + ","
                                + "\r\n" + "Package Date : " + jsonObject.getString("Package_Date") + ","
                                + "\r\n" + "Expiry Date : " + jsonObject.getString("Expiry_Date") + ","
                                + "\r\n" + "Manufactured By : " + jsonObject.getString("Manufactured_by"));

                    } else {
                        ScanCatridgeInjectorVedioGenerate.scancatridge_tv.setText("");
                        showtoast("Invalid QR");
                    }
                } else if (ScanFrom.equals("4")) {

                    if (jsonObject.getString("type").equals("Scan Injector")) {
                        ScanCatridgeInjectorVedioGenerate.scanInjector_tv.setText("Id : " + jsonObject.getString("id") + ","
                                + "\r\n" + "Device Packagedby : " + jsonObject.getString("Device_Packagedby")
                        );

                    } else {
                        ScanCatridgeInjectorVedioGenerate.scanInjector_tv.setText("");
                        showtoast("Invalid QR");
                    }

                }
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                showtoast("Invalid QR");
                Logger.addToLog(TAG, "readerBarcodeData " + e.getLocalizedMessage());

            }
        }
    }

    public void showtoast(String message) {
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }
}


