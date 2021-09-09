package ai.tech5.tech5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hdbarcode.hdbarcodereader.DecdeHDBarcodeActivity;

import ai.tech5.tech5.R;

public class GuidingScreen extends AppCompatActivity {
    Button next_bt, back_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guiding_screen);//backupgudingscreen
        back_bt = findViewById(R.id.back_bt);
        next_bt = findViewById(R.id.next_bt);
        next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuidingScreen.this, ScanKitVaccineBox.class);
                startActivity(i);
                finish();
            }
        });
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuidingScreen.this, DecdeHDBarcodeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        });

    }
}