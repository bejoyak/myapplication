package ai.tech5.tech5;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hdbarcode.hdbarcodereader.DecdeHDBarcodeActivity;

import java.util.Locale;

import ai.tech5.tech5.activities.EnrollActivity;
import ai.tech5.tech5.activities.ScanKitVaccineBox;
import ai.tech5.tech5.activities.SettingsActivity;
import ai.tech5.tech5.enroll.utils.Logger;

public class MainActivity extends AppCompatActivity{
    private TextView tvAppVersion;
    Button enroll,UserScan,btn_add_barcode;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setContentView(R.layout.mainactivity_new);
        setContentView(R.layout.backupmain);
        tvAppVersion = findViewById(R.id.app_version);
        tvAppVersion.setText(" " + BuildConfig.VERSION_NAME);
        enroll = findViewById(R.id.iv_enroll);
        UserScan=findViewById(R.id.iv_scan);
        btn_add_barcode=findViewById(R.id.btn_add_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.addToLog(TAG, "On enroll clicked" );
                Intent intent = new Intent(MainActivity.this, EnrollActivity.class);
                startActivity(intent);
            }
        });
        UserScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.addToLog(TAG, "On UserScan clicked" );
                Intent intent = new Intent(MainActivity.this, ScanKitVaccineBox.class);
                startActivity(intent);

            }
        });

        btn_add_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.addToLog(TAG, "On Display ID clicked" );
                Intent intent = new Intent(MainActivity.this, DecdeHDBarcodeActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {


            case R.id.action_settings:
                Logger.addToLog(TAG, "On settings clicked" );
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}