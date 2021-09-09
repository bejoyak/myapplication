package com.hdbarcode.hdbarcodereader;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import ai.tech5.tech5.BuildConfig;
import ai.tech5.tech5.R;
import ai.tech5.tech5.activities.EnrollActivity;
import ai.tech5.tech5.activities.ScanKitVaccineBox;
import ai.tech5.tech5.activities.SettingsActivity;
import ai.tech5.tech5.enroll.foruploadandverify.GalleryActivity;
import ai.tech5.tech5.enroll.foruploadandverify.ResultActivity;
import ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo.Result;
import ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder.ITLVDecoder;
import ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder.ITLVRecord;
import ai.tech5.tech5.enroll.foruploadandverify.tlvdecoder.TLVDecoderImplementation;
import ai.tech5.tech5.enroll.foruploadandverify.utils.FingerprintUtils;
import ai.tech5.tech5.enroll.hdbarcodedemo.LogUtils;
import ai.tech5.tech5.enroll.hdbarcodedemo.Utilities;
import ai.tech5.tech5.enroll.utils.AppUtils;
import ai.tech5.tech5.enroll.utils.Logger;

import static ai.tech5.tech5.enroll.foruploadandverify.ResultActivity.FROM_WHERE;
import static ai.tech5.tech5.enroll.utils.Logger.logException;
import static com.hdbarcode.hdbarcodereader.HDBarcodeReaderApp.DecodeHDBarcode;

public class DecdeHDBarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DecdeHDBarcodeActivity.class.getSimpleName();
    byte[] bgData = null;
    private String decodedText = null;
    private int bgRetCode;

    private static final int SELECT_FILE = 333;

    File pathToHDBarcode = null;
    File pathToTempFiles = null;
    private Utilities utilities;
    boolean ZIPFOUND = false;

    private static int skip = 0;

    String fileToDisplay = null;
    //  OpenFileDialog openFileDialog = null;

    private Button btnAddBarcode;
//            btnDecodeBarcode;
//    private TextView txtBarcodeName;
//    private ImageView imgBarcode;

    private File selectedFile = null;
    private String errorMessage = null;

    FingerprintUtils fingerprintUtils;
//////////
    private TextView tvAppVersion;
    Button enroll,UserScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.decode_barcode_activity);
        setContentView(R.layout.backupmain);
        tvAppVersion = findViewById(R.id.app_version);
        tvAppVersion.setText(" " + BuildConfig.VERSION_NAME);
        enroll = findViewById(R.id.iv_enroll);
        UserScan=findViewById(R.id.iv_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
////////////////////////
        fingerprintUtils = new FingerprintUtils(DecdeHDBarcodeActivity.this);
        fingerprintUtils.getAppDirectory();

        Result.reset();

        utilities = new Utilities(this);

        String appName = getResources().getString(R.string.folder_name);
        File folder = fingerprintUtils.createExternalDirectory(appName);

        pathToTempFiles = folder;
        pathToHDBarcode = fingerprintUtils.createExternalDirectory("SCANNED_IMAGES");
        //need to decode authcode before decoding the actual decode
        new Thread(() -> decodeAuthBarCodeFromAssets("Authcode_Dec2021.bmp", 148, 148)).start();

        btnAddBarcode = findViewById(R.id.btn_add_barcode);
//        btnDecodeBarcode = findViewById(R.id.btn_decode);
//        btnDecodeBarcode.setOnClickListener(this);
        btnAddBarcode.setOnClickListener(this);

//        txtBarcodeName = findViewById(R.id.txt_barcode_name);
//        imgBarcode = findViewById(R.id.img_bar_code);

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.addToLog(TAG, "On enroll clicked" );
                Intent intent = new Intent(DecdeHDBarcodeActivity.this, EnrollActivity.class);
                startActivity(intent);
            }
        });
        UserScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.addToLog(TAG, "On UserScan clicked" );
                Intent intent = new Intent(DecdeHDBarcodeActivity.this, ScanKitVaccineBox.class);
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
                startActivity(new Intent(DecdeHDBarcodeActivity.this, SettingsActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private int decodeAuthBarCodeFromAssets(String fileName, int sizex, int sizeY) {


        int SizeX = sizex;  // this must be a mu;ltiple of 4, and could be read directly from the file with a little more work
        int SizeY = sizeY;
        byte[] Buffer = new byte[SizeX * SizeY];
        ByteBuffer directBuffer;
        directBuffer = ByteBuffer.allocateDirect(SizeX * SizeY);
        LoadBitmap(Buffer, SizeX, SizeY, fileName);
        //  LoadBitmap(Buffer, SizeX, SizeY, "AssetDemo.bmp");
        directBuffer.put(Buffer, 0, SizeX * SizeY);
        int RetCode = DecodeHDBarcode(directBuffer, SizeX, SizeY);

        Log.e("HDBarcode", "Preload " + fileName + " returned " + RetCode);

        return RetCode;

    }


    private void LoadBitmap(byte[] Buffer, int SizeX, int SizeY, String Filename) {
        try {
            InputStream is = getAssets().open(Filename);
            is.read(Buffer, 0, 54 + 1024);
            for (int Y = 0; Y < SizeY; Y++) {
                is.read(Buffer, (SizeY - 1 - Y) * SizeX, SizeX);
            }
            is.close();
        } catch (Exception e) {
            Log.e("HDBarcode", "HDBarcode LoadBitmap: " + e.getMessage());
        }
    }

    public void decodeProductionBarCode(File barcodeFile) {

        try {

            byte[] productionBarCodeBytes = AppUtils.readByteArrayFromFile(barcodeFile.getAbsolutePath());

            LogUtils.debug(TAG, "productionBarCodeBytes " + productionBarCodeBytes.length);

            Bitmap bitmap = BitmapFactory.decodeByteArray(productionBarCodeBytes, 0, productionBarCodeBytes.length);

            LogUtils.debug(TAG, "bitmap " + bitmap);


            bgData = getRawPixels(bitmap);


            LogUtils.debug(TAG, "bgData " + bgData);

            int previewSizeX = bitmap.getWidth();
            int previewSizeY = bitmap.getHeight();

            LogUtils.debug(TAG, "bitmap  wXH " + previewSizeX + " X " + previewSizeY);

            ByteBuffer directBuffer = ByteBuffer.allocateDirect(bgData.length);
            directBuffer.put(bgData, 0, bgData.length);

            LogUtils.debug(TAG, "before Decoding...");
            int retCode = DecodeHDBarcode(directBuffer, previewSizeX, previewSizeY);

            Log.e("HDBarcode", "production cryptograph retCode     : " + retCode);

            /* If failure, just reset bgData */
            if (retCode <= 0) {
                errorMessage = "Unable to decode Barcode";
                bgData = null;
                return;
            }

            /* Get result */
            directBuffer.rewind();
            directBuffer.get(bgData, 0, retCode);

            /* Build result */
            int Spot = 0;
            decodedText = "";
            while (bgData[Spot] != 0) {
                decodedText = decodedText + (char) (bgData[Spot++] & 0xFF);
            }

            /* Handle it */
            bgRetCode = retCode;

        } catch (Exception e) {
            logException("TAG", e);
            Log.e("HDBarcode", "HDBarcode directBuffer: " + e.getMessage());
            errorMessage = "Something went wrong " + e.getLocalizedMessage();
        }
    }

    private byte[] getRawPixels(Bitmap bitmap) {


        int width = bitmap.getWidth();

        int height = bitmap.getHeight();


        int[] pixels = new int[width * height];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] pixelArray = new byte[pixels.length];

        for (int i = 0; i < pixels.length; i++) {

            int pixel = pixels[i];

            int A = Color.alpha(pixel);

            int R = Color.red(pixel);

            int G = Color.green(pixel);

            int B = Color.blue(pixel);

            pixelArray[i] = (byte) (0.2989 * R + 0.5870 * G + 0.1140 * B);

        }

        return pixelArray;
    }

    class BackgroundPostDecodeTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {


            bgData = null;
            Result.reset();
            ZIPFOUND = false;
            errorMessage = null;
            fileToDisplay = null;

            super.onPreExecute();
            progressDialog = new ProgressDialog(DecdeHDBarcodeActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            decodeProductionBarCode(selectedFile);

            LogUtils.debug(TAG, "after decoding bgretcode " + bgRetCode + " bgData " + bgData);

            if (bgData != null && bgRetCode > 0) {

                byte[] data = bgData;

                /* Get RetCode */
                int retCode = bgRetCode;


                Log.i("HDBarcode", "HDBarcode postDecodeTask running with bgRetCode = " + bgRetCode + " and decodedText = " + decodedText);
                Logger.addToLog(TAG, "HDBarcode postDecodeTask running with bgRetCode = " + bgRetCode + " and decodedText = " + decodedText);

                /* See if there is a picture */
                boolean looksLikeZIP = false;
                int tilde;
                String fileToBrowse = null;

                /* Look for magic ~ character */
                tilde = decodedText.indexOf('~');
                if (tilde >= 0) {
                    /* Look in assets first, and look in folder second */
                    String[] list;
                    AssetManager manager = getAssets();
                    InputStream input = null;
                    for (int places = 0; places < 2; places++) {
                        try {
                            if (places == 0) {
                                list = manager.list("");
                            } else {
                                list = pathToTempFiles.list();
                            }
                            for (String filename : list) {
                                if (filename.substring(0, tilde + 1).contentEquals(decodedText.substring(0, tilde + 1))) {
                                    if (places == 0) {
                                        input = manager.open(filename);
                                    } else {
                                        input = new FileInputStream(filename);
                                    }
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            logException(TAG, e);
                            input = null;
                        }
                        if (input != null) {
                            break;
                        }
                    }
                    if (input != null) {
                        byte[] buffer = new byte[65536];

                        data = new byte[retCode - tilde - 1];
                        System.arraycopy((decodedText + "\0").getBytes(), tilde + 1, data, 0, retCode - tilde - 1);
                        decodedText = decodedText.substring(tilde + 1);

                        for (; ; ) {
                            int length;
                            try {
                                length = input.read(buffer);
                            } catch (Exception e) {
                                logException(TAG, e);
                                length = -1;
                            }
                            if (length < 0) {
                                break;
                            }
                            byte[] both = new byte[data.length + buffer.length];
                            System.arraycopy(data, 0, both, 0, data.length);
                            System.arraycopy(buffer, 0, both, data.length, length);
                            data = both;
                        }
                        try {
                            input.close();
                        } catch (Exception e) {
                            logException(TAG, e);
                            /* do nothing */
                        }
                        retCode = data.length;
                    }
                }

                skip = decodedText.length() + 1;
                int fileSize = retCode - skip;
                if (fileSize > 0) {

                    //without expiration date
                    looksLikeZIP = ((data[skip] & 0xFF) == 0x50) && ((data[skip + 1] & 0xFF) == 0x4B);

                    if (!looksLikeZIP) {
                        //with expiration date
                        looksLikeZIP = ((data[skip] & 0xFF) == 0xFF) && ((data[skip + 1] & 0x55) == 0x55);
                    }


                }


                //saveFailedImage(data);
                System.err.println("looksLikeZIP ::: " + looksLikeZIP);
                Logger.addToLog(TAG, "looksLikeZIP ::: " + looksLikeZIP);
                if (looksLikeZIP) {
                    if (!pathToTempFiles.exists()) {
                        pathToTempFiles.mkdirs();
                    }

                    fileToBrowse = utilities.writeZipToFile(data, skip, fileSize, pathToTempFiles);
                    ZIPFOUND = true;//TODO
                    if (fileToBrowse == null) {
                        decodedText = "Internal zip error";
                    } else if (!fileToBrowse.startsWith("*")) {
                        decodedText = "<html><img src=\"" + fileToBrowse + "\"></html>";
                    } else {
                        decodedText = "";
                        fileToBrowse = fileToBrowse.substring(1);
                    }
                } else if (fileSize > 0) {
                    decodedText = "Unknown file format";
                    fileToBrowse = null;
                }
                /* Display on next image */
                fileToDisplay = fileToBrowse;
                /* Reset flag */
                bgData = null;
                Log.i("HDBarcode", "HDBarcode postDecodeTask stopping with fileToDisplay = " + fileToDisplay);
                Logger.addToLog(TAG, "HDBarcode postDecodeTask stopping with fileToDisplay = " + fileToDisplay);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            LogUtils.debug(TAG, "onPostExecute() " + ZIPFOUND + " error " + errorMessage);

            if (ZIPFOUND) {
                utilities.playBeep();
                try {
                    byte[] bytes = utilities.readFileBytes(new File(pathToTempFiles.getAbsolutePath() + "/Embedded.zip").getAbsolutePath());


                    ITLVDecoder decoder = new TLVDecoderImplementation();
                    List<ITLVRecord> recordList = decoder.decode(bytes);


                    if (recordList != null && recordList.size() > 0) {

                        for (ITLVRecord record : recordList) {
                            if (record.getType() == ITLVRecord.IDEncodeFieldType.Demog) {
                                Log.d("TAG", "typer " + record.getType() + " data " + new String(record.getData()));
                            }
                        }

                        Result.setResultsFromTlvRecords(recordList);


                        if (Result.compressedImage != null) {

                            Intent intent = new Intent(DecdeHDBarcodeActivity.this, ResultActivity.class);
                            // intent.putExtra(IS_DEBUG_MODE, true);
                            intent.putExtra(FROM_WHERE, "Decode");
                            intent.putExtra("selectedbbccfile", selectedFile.getAbsolutePath());
                            startActivity(intent);
//                            finish();

                        } else {

                            showAlertDialog("Face not found!!. Please try with valid Cryptograph.");
                        }

                    } else {

                        showAlertDialog("Failed to Scan Barcode - HD Domain null");
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    showAlertDialog("Something went wrong " + e.getLocalizedMessage());

                }
            } else {

                if (errorMessage != null) {
                    showAlertDialog(errorMessage);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_FILE) {
            String selectedFilePath = data.getStringExtra("selectedImage");

            File file = new File(selectedFilePath);
            if (file.exists()) {

//                imgBarcode.setImageURI(Uri.fromFile(file));
//                txtBarcodeName.setText(file.getName());
                selectedFile = file;


            }
/////////////////////jsschange
            if (selectedFile != null) {

                new BackgroundPostDecodeTask().execute();
            } else {
                showAlertDialog("Please add Cryptograph");
            }
//////////////////////
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_add_barcode:

                Intent gallery = new Intent(DecdeHDBarcodeActivity.this, GalleryActivity.class);
                startActivityForResult(gallery, SELECT_FILE);

                break;
//            case R.id.btn_decode:
//
//                if (selectedFile != null) {
//
//                    new BackgroundPostDecodeTask().execute();
//                } else {
//                    showAlertDialog("Please add Cryptograph");
//                }
//                break;
        }

    }


    public void showAlertDialog(String message) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DecdeHDBarcodeActivity.this);
        builder.setTitle("Tech5 HD Barcode");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNeutralButton("Ok", (DialogInterface dialog, int which) -> {
            dialog.dismiss();

        });
        builder.show();
    }


}
