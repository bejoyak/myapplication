package ai.tech5.tech5.idcaptureidcard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ai.tech5.tech5.R;

public class CaptureIdCard extends AppCompatActivity {


    /**
     * Blocking camera operations are performed using this executor
     */
    private ExecutorService cameraExecutor;

    private PreviewView viewFinder;
    private ImageView ivTransparentView = null;
    private ImageView captureImage;

    // private int displayId = -1;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;

    private Preview preview = null;
    private ImageCapture imageCapture = null;
    private Camera camera = null;
    // CameraSelector
    CameraSelector cameraSelector = null;
    int startX, endX, startY, endY;

    private ProgressDialog progressDialog = null;


    private ProcessCameraProvider cameraProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_idcard);

        setScreenBrightnessFul();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);


        viewFinder = findViewById(R.id.view_finder);
        ivTransparentView = findViewById(R.id.iv_transparent_view);
        captureImage = findViewById(R.id.capture_image);
        captureImage.setOnClickListener((View v) -> {
            try {
                captureFace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Initialize our background executor
        //  cameraExecutor = Executors.newSingleThreadExecutor();
        cameraExecutor = Executors.newFixedThreadPool(1);


        // Wait for the views to be properly laid out
        viewFinder.post(() ->

        {
            // Keep track of the display in which this view is attached
            //displayId = viewFinder.getDisplay().getDisplayId();

            Log.d("TAG", "width " + viewFinder.getWidth() + " height " + viewFinder.getHeight());


            // Set up the camera and its use cases
            if (checkPermissions()) {
                setUpCamera();
            } else {
                Toast.makeText(CaptureIdCard.this, "Camera permission not granted", Toast.LENGTH_LONG).show();
            }


        });


    }


    /**
     * Initialize CameraX, and prepare to bind the camera use cases
     */
    private void setUpCamera() {


        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(CaptureIdCard.this);
        cameraProviderFuture.addListener(() -> {

            // CameraProvider
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

            // Build and bind the camera use cases
            bindCameraUseCases();

            draw();

        }, ContextCompat.getMainExecutor(CaptureIdCard.this));


    }


    private void draw() {
        try {

            Log.d("TAG", "width " + viewFinder.getWidth() + " height " + viewFinder.getHeight());


            int width = viewFinder.getWidth();
            int height = viewFinder.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(viewFinder.getWidth(), viewFinder.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(Color.WHITE);


            Point centerOfCanvas = new Point(width / 2, height / 2);

            int widthOfRect = (int) (width * 0.9);
            int heightOfRect = (widthOfRect / 3) * 2;

            Log.d("TAG", "size of rect width  " + widthOfRect + " height " + heightOfRect);


            startX = (int) (centerOfCanvas.x - (widthOfRect / 2));
            endX = (int) (centerOfCanvas.x + (widthOfRect / 2));


            startY = (int) (centerOfCanvas.y - (heightOfRect / 2));
            endY = (int) (centerOfCanvas.y + (heightOfRect / 2));


            canvas.drawRect(startX, startY, endX, endY, paint);

            ivTransparentView.setImageBitmap(bitmap);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * Declare and bind preview, capture and analysis use cases
     */
    @SuppressLint("RestrictedApi")
    private void bindCameraUseCases() {

        int rotation = viewFinder.getDisplay().getRotation();

        // CameraSelector
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();


        // Preview
        preview = new Preview.Builder()
                // We request aspect ratio but no resolution
                // .setTargetResolution(targetResolution)
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build();


//        // ImageCapture
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                // We request aspect ratio but no resolution to match preview config, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                //  .setBufferFormat(ImageFormat.YUV_420_888)

                // .setTargetResolution(targetResolution)

                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build();


        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll();

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo


            camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture);


            // Attach the viewfinder's surface provider to preview use case
            preview.setSurfaceProvider(viewFinder.getSurfaceProvider());


            autoFocus();

        } catch (Exception exc) {


        }

    }


    private void captureFace() throws IOException {

        File imageFile = createImageFile();

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(imageFile).build();
        imageCapture.takePicture(outputFileOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                showProgreess();
                runOnUiThread(() -> cameraProvider.unbindAll());
                new Thread(() -> {
                    try {
                        File destination = createImageFile();
                        boolean isCropped = Utility.cropImage(imageFile, destination);
                        if (isCropped){

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("captureidfileresult", destination.getAbsolutePath());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();

                        }
                        hideProgress();
                        Log.d("TAG", "cropped image saved to " + destination.getAbsolutePath());
                    } catch (Exception e) {
                        hideProgress();
                    }
                }).start();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {


            }


        });

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
        return image;


    }


    private void autoFocus() {
        try {

            MeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory(viewFinder.getWidth(), viewFinder.getHeight());

            int centerWidth = viewFinder.getWidth() / 2;
            int centreHeight = viewFinder.getHeight() / 2;

            MeteringPoint autoFocusPoint = factory.createPoint(centerWidth, centreHeight);

            FocusMeteringAction.Builder builder =
                    new FocusMeteringAction.Builder(autoFocusPoint,
                            FocusMeteringAction.FLAG_AF |
                                    FocusMeteringAction.FLAG_AE);

            builder.setAutoCancelDuration(1, TimeUnit.SECONDS);
            camera.getCameraControl().startFocusAndMetering(builder.build());

        } catch (Exception e) {


        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(CaptureIdCard.this,
                Manifest.permission.CAMERA);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void setScreenBrightnessFul() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        getWindow().setAttributes(params);
    }

    private void showProgreess() {
        runOnUiThread(() -> progressDialog.show());
    }

    private void hideProgress() {
        runOnUiThread(() -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Shut down our background executor
        cameraExecutor.shutdown();

        hideProgress();
    }
}