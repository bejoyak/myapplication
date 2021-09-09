package ai.tech5.tech5.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ai.tech5.tech5.R;
import ai.tech5.tech5.utils.PreferencesHelper;


public class SettingsActivity extends AppCompatActivity {

    private PreferencesHelper preferencesHelper;
    EditText edtUrl;
Button btnSubmit;
    Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        edtUrl = findViewById(R.id.edt_url);

        btnSubmit = findViewById(R.id.btn_submit);
        toolbar = findViewById(R.id.toolbar);
        preferencesHelper = new PreferencesHelper(SettingsActivity.this);

        edtUrl.setText(preferencesHelper.getBaseURL());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData()) {
                    preferencesHelper.setBaseURL(edtUrl.getText().toString().trim());

                    Toast.makeText(SettingsActivity.this, "User Settings Changed Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    finish();

//                } else {
//                    Toast.makeText(SettingsActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
//                }
                }
            }
        });


    }


    private boolean isValidData() {
        boolean isValid = true;
        if (isEmpty(edtUrl)) {
            isValid = false;
            edtUrl.setError("Base url should not be empty");
        } else if (getString(edtUrl).charAt(getString(edtUrl).length() - 1) != '/') {
            isValid = false;
            edtUrl.setError("Base url should end with /");
        }

        return isValid;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private String getString(EditText editText) {
        return editText.getText().toString().trim();
    }

}
