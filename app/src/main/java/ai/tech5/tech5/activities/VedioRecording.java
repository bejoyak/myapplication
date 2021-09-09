package ai.tech5.tech5.activities;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ai.tech5.tech5.R;


public class VedioRecording extends AppCompatActivity {
    //public class VedioRecording extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback{
    Button record_bt;

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_recording);
        record_bt = findViewById(R.id.record_bt);
        record_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra("android.intent.extra.durationLimit",10);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent,1);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
//            builder.setView(videoView).show();
        }

    }
}
