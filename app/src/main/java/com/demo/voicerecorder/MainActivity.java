package com.demo.voicerecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button normalPplay, modifiedPlay, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private LinearLayout ll_parent;

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;

    public static int RECORDER_SAMPLERATE = 8000;
    public static int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    public static int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        record = (Button) findViewById(R.id.recordButton);
        normalPplay = (Button) findViewById(R.id.normalPlayButton);
        modifiedPlay = (Button) findViewById(R.id.modifiedPlayButton);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp3";
        } else {
            new MarshmallowPermission(MainActivity.this, ll_parent).requestStoragePermission();
        }

        normalPplay.setEnabled(false);
        modifiedPlay.setEnabled(false);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio();
            }
        });

        normalPplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalPlayAudio();
            }
        });

        modifiedPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                modifiedPlayAudio();
            }
        });
    }

    private void recordAudio() {
        if (record.getText().toString().trim().equalsIgnoreCase("START RECORDING")) {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

                myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);

                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }

                normalPplay.setEnabled(false);
                modifiedPlay.setEnabled(false);

                record.setText("STOP RECORDING");
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

            } else {
                new MarshmallowPermission(MainActivity.this, ll_parent).requestRecordPermission();
            }

        } else {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;

            modifiedPlay.setEnabled(true);
            normalPplay.setEnabled(true);

            record.setText("START RECORDING");
            Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void normalPlayAudio() throws IllegalArgumentException, SecurityException, IllegalStateException {

        MediaPlayer m = new MediaPlayer();

        try {
            m.setDataSource(outputFile);
            m.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        m.start();
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();



    }

    private void modifiedPlayAudio() throws IllegalArgumentException, SecurityException, IllegalStateException {
        Audio audio=new Audio();
        audio.start();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp3";
            } else {

            }
        }

        if (requestCode == PERMISSION_REQUEST_RECORD_AUDIO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);

                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }

                normalPplay.setEnabled(false);
                modifiedPlay.setEnabled(false);

                record.setText("STOP RECORDING");
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

}
