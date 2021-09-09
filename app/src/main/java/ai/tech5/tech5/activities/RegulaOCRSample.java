package ai.tech5.tech5.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.regula.documentreader.api.DocumentReader;
import com.regula.documentreader.api.completions.IDocumentReaderCompletion;
import com.regula.documentreader.api.completions.IDocumentReaderInitCompletion;
import com.regula.documentreader.api.completions.IDocumentReaderPrepareCompletion;
import com.regula.documentreader.api.enums.DocReaderAction;
import com.regula.documentreader.api.enums.Scenario;
import com.regula.documentreader.api.enums.eGraphicFieldType;
import com.regula.documentreader.api.enums.eVisualFieldType;
import com.regula.documentreader.api.errors.DocumentReaderException;
import com.regula.documentreader.api.results.DocumentReaderResults;
import com.regula.documentreader.api.results.DocumentReaderTextField;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import ai.tech5.tech5.R;

public class RegulaOCRSample extends AppCompatActivity implements IDocumentReaderCompletion {

    private ImageView imgPortrait;
    private NestedScrollView scrollView;
    private Button btnScanDoc;
    private TextView txtName;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regula_o_c_r_sample);


        imgPortrait = findViewById(R.id.id_face_image);
        scrollView = findViewById(R.id.results_view);
        btnScanDoc = findViewById(R.id.btn_scan_doc);
        txtName = findViewById(R.id.txt_name);

        progressDialog = new ProgressDialog(RegulaOCRSample.this);
        progressDialog.setTitle("Initializing SDK");
        progressDialog.setCancelable(false);

        btnScanDoc.setOnClickListener((View v) -> {
            showDemographics(null, false);
            DocumentReader.Instance().showScanner(RegulaOCRSample.this, this);

        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnScanDoc.setEnabled(false);

        if (!DocumentReader.Instance().getDocumentReaderIsReady()) {

            progressDialog.show();

            //Reading the license from raw resource file
            InputStream licInput = null;
            try {
                licInput = getResources().openRawResource(R.raw.regula);
                int available = licInput.available();
                final byte[] license = new byte[available];
                //noinspection ResultOfMethodCallIgnored
                licInput.read(license);

                //preparing database files, it will be downloaded from network only one time and stored on user device
                DocumentReader.Instance().prepareDatabase(RegulaOCRSample.this, "Full", new IDocumentReaderPrepareCompletion() {

                    @Override
                    public void onPrepareProgressChanged(int progress) {
                        progressDialog.setTitle("Downloading database: " + progress + "%");
                    }


                    @Override
                    public void onPrepareCompleted(boolean status, DocumentReaderException error) {

                        //Initializing the reader
                        DocumentReader.Instance().initializeReader(RegulaOCRSample.this, license, new IDocumentReaderInitCompletion() {


                            @Override
                            public void onInitCompleted(boolean success, DocumentReaderException error) {
                                hideProgress();

                                DocumentReader.Instance().customization().edit().setShowHelpAnimation(false).apply();

                                //initialization successful
                                if (success) {
                                    btnScanDoc.setEnabled(true);
                                    DocumentReader.Instance().processParams().scenario = Scenario.SCENARIO_MRZ_OR_OCR;
                                }
                                //Initialization was not successful
                                else {
                                    Toast.makeText(RegulaOCRSample.this, "Init failed:" + error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });


            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    licInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            btnScanDoc.setEnabled(true);
        }
    }


    @Override
    public void onCompleted(int action, DocumentReaderResults results, DocumentReaderException error) {
        //processing is finished, all results are ready
        if (action == DocReaderAction.COMPLETE) {

            displayResults(results);

        } else {
            //something happened before all results were ready
            if (action == DocReaderAction.CANCEL) {
                Toast.makeText(RegulaOCRSample.this, "Scanning was cancelled", Toast.LENGTH_LONG).show();
            } else if (action == DocReaderAction.ERROR) {
                Toast.makeText(RegulaOCRSample.this, "Error:" + error, Toast.LENGTH_LONG).show();
            }
        }
    }


    //show received results on the UI
    private void displayResults(DocumentReaderResults results) {
        if (results != null) {
            String name = results.getTextFieldValueByType(eVisualFieldType.FT_SURNAME_AND_GIVEN_NAMES);


            if (name != null) {
                txtName.setVisibility(View.VISIBLE);
                txtName.setText(name);
            } else {
                txtName.setVisibility(View.GONE);
            }


            HashMap<String, String> demographics = new HashMap<>();
            // through all text fields
            if (results.textResult != null && results.textResult.fields != null) {
                for (DocumentReaderTextField textField : results.textResult.fields) {
                    String value = results.getTextFieldValueByType(textField.fieldType, textField.lcid);
                    Log.d("MainActivity", textField.getFieldName(RegulaOCRSample.this) + "----" + value + "\n");

                    demographics.put(textField.getFieldName(RegulaOCRSample.this), value);
                }
            }

            Log.d("TAG", "demos " + demographics);
            if (demographics.size() > 0) {
                showDemographics(demographics, true);
            }

            Bitmap portrait = results.getGraphicFieldImageByType(eGraphicFieldType.GF_PORTRAIT);
            if (portrait != null) {
                imgPortrait.setImageBitmap(portrait);
            }

//            Bitmap documentImage = results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE);
//            if (documentImage != null) {
//                double aspectRatio = (double) documentImage.getWidth() / (double) documentImage.getHeight();
//                documentImage = Bitmap.createScaledBitmap(documentImage, (int) (480 * aspectRatio), 480, false);
//              //  docImageIv.setImageBitmap(documentImage);
//            }
        }
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

//                    TextView key = resultView.findViewById(R.id.key);
//                    TextView value = resultView.findViewById(R.id.value);
//
//                    key.setText(keyText);
//                    value.setText(demogs.get(keyText));
/////////////////////////////
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
            imgPortrait.setImageBitmap(null);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();

    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }


}