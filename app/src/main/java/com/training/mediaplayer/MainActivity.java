package com.training.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button play, pause, rewind, forward;
    TextView songName, songTime;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;

    Handler handler = new Handler();

    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int rewindTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play_button);
        pause = findViewById(R.id.pause_button);
        rewind = findViewById(R.id.rewind_button);
        forward = findViewById(R.id.forward_button);

        songName = findViewById(R.id.song_title);
        songTime = findViewById(R.id.time_left_text);

        seekBar = findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this,
                R.raw.song
        );

        seekBar.setClickable(false);

        songName.setText(getResources().getIdentifier(
                "song",
                "raw",
                getPackageName()
        ));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't Jump Forward",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;;
                if ((temp - rewindTime) > 0) {
                    startTime = startTime - rewindTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't Jump Backward",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        songTime.setText(String.format(
                "% d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
        ));

        seekBar.setProgress((int)startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            songTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)-
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));

            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };
}