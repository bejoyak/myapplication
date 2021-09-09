package ai.tech5.tech5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hdbarcode.hdbarcodereader.DecdeHDBarcodeActivity;

import ai.tech5.tech5.BuildConfig;
import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.utils.Logger;

public class LoginActivity extends AppCompatActivity {
    Button login_bt;
    private TextView tvAppVersion;
    public static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_activity_login);
        login_bt = findViewById(R.id.login_bt);
        tvAppVersion = findViewById(R.id.app_version);
        tvAppVersion.setText(" " + BuildConfig.VERSION_NAME);
        Logger.addToLog(TAG, "Login activity.." );

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DecdeHDBarcodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}