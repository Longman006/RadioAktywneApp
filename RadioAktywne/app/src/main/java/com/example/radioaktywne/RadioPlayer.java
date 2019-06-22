package com.example.radioaktywne;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import static android.media.session.PlaybackState.ACTION_PLAY;

public class RadioPlayer extends Service implements MediaPlayer.OnPreparedListener {
    private MediaPlayer mMediaPlayer;
    // The audio url to play
    private String audioUrl = "https://listen.radioaktywne.pl:8443/ramp3";

    //TODO add OnError() for MediaPlayer

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("tag","onStartCommand");

        if (intent.getAction().equals(Intent.ACTION_MAIN)) {
            createMediaPlayerIfNeeded(); // initialize it here
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(this);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("tag","onPrepared");
        mp.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null)
            mMediaPlayer.release();
            mMediaPlayer =null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            // Make sure the media player will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            //mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            try {
                mMediaPlayer.setDataSource(audioUrl);
            } catch (IOException e) {
                Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            mMediaPlayer.reset();
        }
    }

}
