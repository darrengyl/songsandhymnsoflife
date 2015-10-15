package com.church.psalm;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by yuanlong.gu on 10/13/2015.
 */
public class MusicService extends Service implements
        OnPreparedListener, OnErrorListener, OnCompletionListener {
    private MediaPlayer player;
    //private String songUrl;
    private int trackNumber;
    private final IBinder musicBind = new MusicBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        trackNumber = intent.getIntExtra("trackNumber", 1);
        Log.d("trackNum in service", String.valueOf(trackNumber));
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.getDuration();
        mp.getCurrentPosition();
        mp.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public String getMusicLink(int track) {
        String musicLink = "http://shengmingshige.net/hymnal/mp3/"
                + new DecimalFormat("0000").format(track) + ".mp3";
        return musicLink;
    }


    public void playSong() {
        player.reset();
        try {
            player.setDataSource(getMusicLink(trackNumber));
            Log.e("DataSource is found","");

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Music databinding", "Error getting data source");

        }
        player.prepareAsync();
    }

    public int getPos() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int pos) {
        player.seekTo(pos);
    }

    public void go() {
        player.start();
    }

    public void playPrev() {
        trackNumber--;
        if (trackNumber < 1) {
            trackNumber = 586;
        }
        playSong();

    }

    public void playNext() {
        trackNumber++;
        if (trackNumber > 586) {
            trackNumber = 1;
        }
        playSong();
    }

    public void passTrackNum(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

}
