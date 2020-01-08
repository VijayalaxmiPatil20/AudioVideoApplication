package com.example.audiovideoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import static android.widget.SeekBar.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    //UI components.
    private VideoView myVideoView;
    private Button btnPlayVideo, btnPlayMusic, btnPauseMusic;
    private SeekBar volumeSeekBar, moveBackAndForthSeekBar;

    private MediaController mediaController; //controls for media
    private MediaPlayer mediaPlayer; //play sound
    private AudioManager audioManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        myVideoView = findViewById(R.id.videoView);

        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        volumeSeekBar = findViewById(R.id.seekBarVolume);
        moveBackAndForthSeekBar = findViewById(R.id.seekBarMove);

        btnPlayVideo.setOnClickListener(MainActivity.this); //main class is implementing the onclick listener
        btnPlayMusic.setOnClickListener(this);
        btnPauseMusic.setOnClickListener(this);
        moveBackAndForthSeekBar.setOnSeekBarChangeListener(this);
        mediaController = new MediaController(MainActivity.this); //having backward pause and forward view on the video.

        mediaPlayer = MediaPlayer.create(this,R.raw.cowsound);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolOfUserDevice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int curentVolOfUserDevice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maxVolOfUserDevice);
        volumeSeekBar.setProgress(curentVolOfUserDevice);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    //Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_SHORT).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }

            }

            //gets called when we start to touch and drag the seekbar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //gets called when we stop dragging the seekBar and release the touch.
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        moveBackAndForthSeekBar.setOnSeekBarChangeListener(this);
        moveBackAndForthSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(this); //when music ends this interface gets executed

    }

    @Override
    public void onClick(View buttonView) {


        switch(buttonView.getId()) {

            case R.id.btnPlayVideo :
                Uri videoUri = Uri.parse("android.resource://" +getPackageName() + "/" + R.raw.video);

                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mediaController);

                mediaController.setAnchorView(myVideoView);
                myVideoView.requestFocus();
                myVideoView.start();
                break;
            case R.id.btnPlayMusic :
                mediaPlayer.start();
                myTimer();
                break;
            case R.id.btnPauseMusic :
                mediaPlayer.pause();
                timer.cancel();
                break;
        }



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
//            Toast.makeText(this,Integer.toString(progress),Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(progress);
            myTimer();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.start();
    }

    //gets executed when the music is finished
    @Override
    public void onCompletion(MediaPlayer mp) {

            timer.cancel();
            Toast.makeText(this,"Music is ended",Toast.LENGTH_SHORT).show();
//        } else if(moveBackAndForthSeekBar.getProgress() ) {
//
//            Toast.makeText(this, "Music is has not ended", Toast.LENGTH_SHORT).show();
//
//        }
    }
    private void myTimer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moveBackAndForthSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 100);
    }
}
