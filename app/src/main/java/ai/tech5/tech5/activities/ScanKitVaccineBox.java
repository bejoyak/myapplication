package ai.tech5.tech5.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.Logger;

public class ScanKitVaccineBox extends AppCompatActivity {
    Button scankit_bt, vaccinebox_bt, next_kitvaccine_bt;
    static TextView scankit_tv;
    static TextView vaccinebox_tv;
    String TAG = "ScanKitVaccineBox Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scakitvaccineactivity_new);//main

        scankit_bt = findViewById(R.id.scankit_bt);
        scankit_tv = findViewById(R.id.scankit_tv);
        vaccinebox_bt = findViewById(R.id.vaccinebox_bt);
        vaccinebox_tv = findViewById(R.id.vaccinebox_tv);
        next_kitvaccine_bt = findViewById(R.id.next_kitvaccine_bt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);
        Logger.addToLog(TAG, "scankitvaccinebox activity ");

        next_kitvaccine_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scankit_tv.getText().toString().length() != 0 && vaccinebox_tv.getText().toString().length() != 0) {
                    Intent intent = new Intent(ScanKitVaccineBox.this, ScanPatientIDFaceAuthentication.class);
                    intent.putExtra("ScanKit", scankit_tv.getText().toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("\\r",""));
                    intent.putExtra("VaccineBox", vaccinebox_tv.getText().toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("\\r",""));
                    startActivity(intent);
                    finish();
                } else if (scankit_tv.getText().toString().length() == 0 && vaccinebox_tv.getText().toString().length() == 0) {
                    Toast.makeText(ScanKitVaccineBox.this, "Please Scan Kit and Scan Vaccine Box", Toast.LENGTH_SHORT).show();
                } else if (scankit_tv.getText().toString().length() == 0) {
                    Toast.makeText(ScanKitVaccineBox.this, "Please ScanKit", Toast.LENGTH_SHORT).show();

                } else if (vaccinebox_tv.getText().toString().length() == 0) {
                    Toast.makeText(ScanKitVaccineBox.this, "Please VaccineBox", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ScanKitVaccineBox.this, "Please Scan Kit and Vaccine Box", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scankit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanKitVaccineBox.this, UserScanning.class);
                intent.putExtra("ScanFrom", "1");
//                startActivityForResult(intent, requestCodeis);
                startActivity(intent);

            }
        });

        vaccinebox_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanKitVaccineBox.this, UserScanning.class);
                intent.putExtra("ScanFrom", "2");
                startActivity(intent);

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

}
