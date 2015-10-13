package com.church.psalm;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by yuanlong.gu on 10/13/2015.
 */
public class MusicService extends Service implements
        OnPreparedListener, OnErrorListener, OnCompletionListener{
    private MediaPlayer player;
    //private String songUrl;
    private int trackNumber;
    private final IBinder musicBind = new MusicBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void onCreate(){
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
    }
    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }
    public String getMusicLink(int track){
        String musicLink = "http://shengmingshige.net/hymnal/mp3/"
                + new DecimalFormat("0000").format(track) + ".mp3";
        return musicLink;
    }
    public void setTrackNumber(int track){
        trackNumber = track;
    };

    public void playSong(){
        player.reset();
        try {
            player.setDataSource(getMusicLink(trackNumber));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Music databinding", "Error getting data source");
            player.prepareAsync();
        }
    }

    public int getPos(){
        return player.getCurrentPosition();
    }
    public int getDur(){
        return player.getDuration();
    }
    public boolean isPng(){
        return player.isPlaying();
    }
    public void pausePlayer(){
        player.pause();
    }
    public void seek(int pos){
        player.seekTo(pos);
    }
    public void go(){
        player.start();
    }
    public void playPrev(){
        trackNumber--;
        if (trackNumber < 1){
            trackNumber = 586;
        }
        playSong();

    }

    public void playNext(){
        trackNumber++;
        if (trackNumber > 586){
            trackNumber = 1;
        }
        playSong();
    }

    public void passTrackNum(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public class MusicBinder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

}
