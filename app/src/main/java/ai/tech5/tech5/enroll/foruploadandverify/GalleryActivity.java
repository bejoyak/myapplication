package ai.tech5.tech5.enroll.foruploadandverify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.foruploadandverify.utils.DirectoryChooserDialog;
import ai.tech5.tech5.enroll.foruploadandverify.utils.RecyclerItemClickListener;
import ai.tech5.tech5.enroll.hdbarcodedemo.LogUtils;
import ai.tech5.tech5.enroll.preferences.AppSharedPreference;


public class GalleryActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    RecyclerView recyclerView;
    TextView txtEmptyImages;
    Button btnChooseDir;
    EditText edtDirPath;

    ImagesListingAdapter imagesListingAdapter;
    private DirectoryChooserDialog directoryChooserDialog = null;
    private ProgressBar progressBar;

    private String selectedDirectory;
    private AppSharedPreference appSharedPreference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_gallery);
        appSharedPreference = new AppSharedPreference(GalleryActivity.this);


        recyclerView = findViewById(R.id.recyclerview);
        txtEmptyImages = findViewById(R.id.txt_empty_data);
        btnChooseDir = findViewById(R.id.btn_choose);
        edtDirPath = findViewById(R.id.edt_dir_path);
        progressBar = findViewById(R.id.loading_progress);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Home");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.loading), PorterDuff.Mode.SRC_ATOP);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(layoutManager);
        imagesListingAdapter = new ImagesListingAdapter(list);
        recyclerView.setAdapter(imagesListingAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, (View view, int position) -> {
            String path = list.get(position);
            Intent deliverResultIntent = new Intent();
            deliverResultIntent.putExtra("selectedImage", path);
            setResult(RESULT_OK, deliverResultIntent);
            finish();

        }));

        String path = appSharedPreference.getBBCCodesDirPath();
        if (path == null) {
//            path = FingerprintUtils.createExternalDirectory("BBC_CODES").getAbsolutePath();
//              path = "/storage/emulated/0/Documents/Safetynet/BBC_CODES/";
//            path = "/storage/emulated/0/Pictures/BBC_CODES/";
            path = "/storage/emulated/0/Download/Tech5/BBC_CODES/";
            System.out.println("path to gallarry..................................................................." + path);
            appSharedPreference.setBBCCodesDirPath(path);
        }


        selectedDirectory = path;
        loadBarCodesInDirectory(path);
        edtDirPath.setText(selectedDirectory);

        directoryChooserDialog = new DirectoryChooserDialog(GalleryActivity.this, (String chosenDir) -> {
            list.clear();
            imagesListingAdapter.notifyDataSetChanged();
            loadBarCodesInDirectory(chosenDir);
            edtDirPath.setText(chosenDir);
            selectedDirectory = chosenDir;
            appSharedPreference.setBBCCodesDirPath(chosenDir);

        });
        directoryChooserDialog.setNewFolderEnabled(false);
        btnChooseDir.setOnClickListener((View view) -> directoryChooserDialog.chooseDirectory(selectedDirectory));


    }

    private void loadBarCodesInDirectory(String dir) {
        btnChooseDir.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        txtEmptyImages.setVisibility(View.GONE);

        new Thread(() -> {

            final ArrayList<String> images = new ArrayList<>();
            getListOfFiles(dir, images);

            runOnUiThread(() -> {

                btnChooseDir.setEnabled(true);
                progressBar.setVisibility(View.GONE);

                if (images.size() > 0) {
                    txtEmptyImages.setVisibility(View.GONE);
                    LogUtils.debug("TAG", "images in T5BBC/BBC_CODES " + images.size() + " -> " + images);
                    list.addAll(images);
                    imagesListingAdapter.notifyDataSetChanged();
                } else {
                    txtEmptyImages.setVisibility(View.VISIBLE);
                }
            });
        }).start();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getListOfFiles(String directoryName, List<String> files) {

        File directory = new File(directoryName);
        if (!directory.exists() && !directory.isDirectory()) {
            return;
        }

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if (fList != null && fList.length > 0) {
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().contains(".bmp") || file.getName().contains(".png")) {
                        files.add(file.getAbsolutePath());
                    }
                } else if (file.isDirectory()) {
                    getListOfFiles(file.getAbsolutePath(), files);
                }
            }

        }
    }


}
