package ai.tech5.tech5.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.Logger;
import ai.tech5.tech5.enroll.view.EnrollUserActivity;

public class ScanCatridgeInjectorVedioGenerate extends AppCompatActivity {
    Button ScanCatridge_bt, ScanInjector_bt, capturevedio_bt, next_inCatridgeInjVG, previous_inCatridgeInjVG;
    static TextView scancatridge_tv, scanInjector_tv, capturevedioPATH_tv;
    String ScanKit, VaccineBox, Captured_imagepath, PatientID,responce_name,responce_dob,responce_country,responce_email,responce_gender,responce_bloodgroup;
    public static final String TAG = ScanCatridgeInjectorVedioGenerate.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catridgeinjectorvedioactivity_new);
        ScanCatridge_bt = findViewById(R.id.ScanCatridge_bt);
        ScanInjector_bt = findViewById(R.id.ScanInjector_bt);
        capturevedio_bt = findViewById(R.id.capturevedio_bt);
        next_inCatridgeInjVG = findViewById(R.id.next_inCatridgeInjVG);
        scancatridge_tv = findViewById(R.id.scancatridge_tv);
        scanInjector_tv = findViewById(R.id.scanInjector_tv);
        capturevedioPATH_tv = findViewById(R.id.capturevedioPATH_tv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

        ScanKit = getIntent().getStringExtra("ScanKit");
        VaccineBox = getIntent().getStringExtra("VaccineBox");
        Captured_imagepath = getIntent().getStringExtra("Captured_imagepath");
        PatientID = getIntent().getStringExtra("PatientID");


        responce_name = getIntent().getStringExtra("responce_name");
        responce_dob = getIntent().getStringExtra("responce_dob");
        responce_country = getIntent().getStringExtra("responce_country");
        responce_email = getIntent().getStringExtra("responce_email");
        responce_gender = getIntent().getStringExtra("responce_gender");
        responce_bloodgroup = getIntent().getStringExtra("responce_bloodgroup");
        Logger.addToLog(TAG, "readerBarcodeData ");

        next_inCatridgeInjVG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (scancatridge_tv.getText().toString().length() != 0 && scanInjector_tv.getText().toString().length() != 0 && capturevedioPATH_tv.getText().toString().length() != 0) {

                    Intent intent = new Intent(ScanCatridgeInjectorVedioGenerate.this, EnrollUserActivity.class);
                    intent.putExtra("Captured_imagepath", Captured_imagepath);
                    intent.putExtra("PatientID", PatientID);
                    intent.putExtra("ScanKit", ScanKit);
                    intent.putExtra("VaccineBox", VaccineBox);

                    intent.putExtra("scancatridge", scancatridge_tv.getText().toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("\\r",""));
                    intent.putExtra("scanInjector", scanInjector_tv.getText().toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("\\r",""));
                    intent.putExtra("capturevedioPATH_tv", capturevedioPATH_tv.getText().toString());


                    intent.putExtra("responce_name", responce_name);
                    intent.putExtra("responce_dob", responce_dob);
                    intent.putExtra("responce_country", responce_country);
                    intent.putExtra("responce_email", responce_email);
                    intent.putExtra("responce_gender", responce_gender);
                    intent.putExtra("responce_bloodgroup", responce_bloodgroup);

                    startActivity(intent);

                    finish();
                } else if (scancatridge_tv.getText().toString().length() == 0 && scanInjector_tv.getText().toString().length() == 0 && capturevedioPATH_tv.getText().toString().length() == 0) {
                    showMessage("Please Scan Catridge , Injector and Capture Vedio");
                } else if (scancatridge_tv.getText().toString().length() == 0) {
                    showMessage("Please Scan Catridge");
                } else if (scanInjector_tv.getText().toString().length() == 0) {
                    showMessage("Please Scan Injector");
                } else if (capturevedioPATH_tv.getText().toString().length() == 0) {
                    showMessage("Please Capture Vedio");
                }
            }
        });
        ScanCatridge_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanCatridgeInjectorVedioGenerate.this, UserScanning.class);
                intent.putExtra("ScanFrom", "3");
                startActivity(intent);

            }
        });

        ScanInjector_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanCatridgeInjectorVedioGenerate.this, UserScanning.class);
                intent.putExtra("ScanFrom", "4");
                startActivity(intent);

            }
        });
        capturevedio_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra("android.intent.extra.durationLimit", 10);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent, 1);

            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();
            String path = getRealPathFromURI(ScanCatridgeInjectorVedioGenerate.this, data.getData());
            System.out.println("file path.............." + path);
            capturevedioPATH_tv.setText("VedioPath : " + path);
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

}

