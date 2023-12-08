package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //initialize variable



    TextView playerPosition,playerDuration;
    SeekBar seekBar;
    ImageView btRew,btplay,btpause,btFf;
    MediaPlayer mediaPlayer;
    Handler handler=new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerPosition=findViewById(R.id.player_position);
        playerDuration=findViewById(R.id.player_duration);
        seekBar=findViewById(R.id.seek_bar);
        btRew=findViewById(R.id.bt_rewind);
        btplay=findViewById(R.id.bt_play);
        btpause=findViewById(R.id.bt_pause);
        //initialize media player

        mediaPlayer= MediaPlayer.create(this,R.raw.music);
        runnable=new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };

        int duration=mediaPlayer.getDuration();

        String sDuration=convertFormat(duration);

        playerDuration.setText(sDuration);
        btplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide play button
                btplay.setVisibility(View.GONE);
                //Show pause button
                btpause.setVisibility(View.VISIBLE);
                //Start mediaplayer
                mediaPlayer.start();
                //set maximum seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable,0);
            }
        });
        btpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btpause.setVisibility(View.GONE);
                btplay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });



        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPositin = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPositin){
                    currentPositin = currentPositin+5000;
                    playerPosition.setText(convertFormat(currentPositin));
                    mediaPlayer.seekTo(currentPositin);

                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPositin = mediaPlayer.getCurrentPosition();

                if (mediaPlayer.isPlaying() && currentPositin > 5000){

                    currentPositin = currentPositin - 5000;
                    playerPosition.setText(convertFormat(currentPositin));
                    mediaPlayer.seekTo(currentPositin);

                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser){

                    mediaPlayer.seekTo(i);
                }

               playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btpause.setVisibility(View.GONE);
                btplay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);

            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }


}