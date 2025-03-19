package com.nhaccuatui.musicapplication.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nhaccuatui.musicapplication.UserActivity.MainActivity;
import com.nhaccuatui.musicapplication.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageView artworkView, playPauseButton;
    private ImageButton backButton;
    private TextView titleView, artistView, currentTimeView, totalTimeView;
    private SeekBar durationSeekBar;

    private boolean isPlaying = false;
    private Handler handler = new Handler();
    private Runnable updateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Bind views
        artworkView = findViewById(R.id.artworkView);
        titleView = findViewById(R.id.titleView);
        artistView = findViewById(R.id.artistView);
        playPauseButton = findViewById(R.id.playPauseButton);
        currentTimeView = findViewById(R.id.currentTimeView);
        totalTimeView = findViewById(R.id.totalTimeView);
        durationSeekBar = findViewById(R.id.durationSeekBar);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            // Navigate back to the MainActivity
            Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the stack if needed
            startActivity(intent);
            finish();
        });

        // Get song details from intent
        String songTitle = getIntent().getStringExtra("songTitle");
        String songArtist = getIntent().getStringExtra("songArtist");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String mp3Url = getIntent().getStringExtra("mp3Url");

        // Set song details
        titleView.setText(songTitle);
        artistView.setText(songArtist);

        // Load artwork
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_music)
                .error(R.drawable.default_music)
                .into(artworkView);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mp3Url);
            mediaPlayer.setOnPreparedListener(mp -> {
                totalTimeView.setText(formatTime(mediaPlayer.getDuration())); // Set total duration
                durationSeekBar.setMax(mediaPlayer.getDuration());
                playPauseButton.setImageResource(R.drawable.ic_play); // Show play button
                setupUpdateRunnable(); // Setup time update runnable
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                playPauseButton.setImageResource(R.drawable.ic_play); // Reset to play icon
                durationSeekBar.setProgress(0);
                currentTimeView.setText("0:00");
                handler.removeCallbacks(updateRunnable); // Stop updates
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load song", Toast.LENGTH_SHORT).show();
        }

        // Play/Pause button click listener
        playPauseButton.setOnClickListener(view -> togglePlayPause());

        // SeekBar change listener
        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    currentTimeView.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.ic_play); // Show play icon
                handler.removeCallbacks(updateRunnable); // Stop updates
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.ic_pause); // Show pause icon
                handler.post(updateRunnable); // Start updates
            }
            isPlaying = !isPlaying;
        }
    }

    private void setupUpdateRunnable() {
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    durationSeekBar.setProgress(currentPosition);
                    currentTimeView.setText(formatTime(currentPosition));
                    handler.postDelayed(this, 1000); // Update every second
                }
            }
        };
    }

    private String formatTime(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}
