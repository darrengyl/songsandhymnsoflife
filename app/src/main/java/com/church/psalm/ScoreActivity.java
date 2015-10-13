package com.church.psalm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.church.psalm.MusicService;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


/**
 * Created by yuanlong.gu on 10/7/2015.
 */
public class ScoreActivity extends AppCompatActivity implements MediaPlayerControl{
    Toolbar toolbar;
    SubsamplingScaleImageView imageView;
    ImageLoader imageLoader;
    MaterialProgressBar progressBarScore;
    private MusicController controller;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound;
    String songUrl;
    int trackNumber;
    //int imageHeight;
    //int imageWidth;
    BitmapFactory.Options options;

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        progressBarScore = (MaterialProgressBar)findViewById(R.id.progress_bar_list);

        toolbar = (Toolbar)findViewById(R.id.app_bar_score);

        setSupportActionBar(toolbar);
        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        Intent intent = getIntent();
        trackNumber = intent.getIntExtra("trackNumber", 1);
        musicService.setTrackNumber(trackNumber);
        if (!foundScoreInStorage(trackNumber)){

        }
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        imageLoader = VolleySingleton.getInstance(this).getImageLoader();
        Log.d("score link", getScoreLink(trackNumber));
        imageLoader.get(getScoreLink(trackNumber), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    Bitmap bitmap = getResizedBitmap(response.getBitmap(), 4000, 4000);
                    imageView.setImage(ImageSource.bitmap(bitmap));
                    progressBarScore.setVisibility(View.GONE);

                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        setController();

    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicService = binder.getService();
            musicService.passTrackNum(trackNumber);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        //controller.setAnchorView();
        controller.setEnabled(true);
    }

    public String getScoreLink(int track){
        String scoreLink = "http://shengmingshige.net/blog/wp-content/gallery/c-png/cu-"
                + new DecimalFormat("0000").format(track) + ".png";
        return scoreLink;
    }

/*    public String getMusicLink(int track){
        String musicLink = "http://shengmingshige.net/hymnal/mp3/"
                + new DecimalFormat("0000").format(track) + ".mp3";
        return musicLink;
    }*/

    public boolean foundScoreInStorage(int track){
        File score = new File(this.getFilesDir() + "/Score" + track + ".png");
        return (score.exists());
    }

    public boolean foundMusicInStorage(int track){
        File music = new File(this.getFilesDir() + "/Music" + track + ".mp3");
        return (music.exists());
    }

    public void saveScore(Bitmap bitmap, int track) throws IOException {
        File score = null;
        FileOutputStream save = null;
        try {
            score = new File(this.getFilesDir(), track + ".png");
            save = new FileOutputStream(score);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, save);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (save !=null){
                save.close();
            }
        }

    }

    public Bitmap loadScore(int track) throws IOException {
        Bitmap bitmap = null;
        FileInputStream load = null;
        try {
            load = openFileInput(this.getFilesDir() + "/Score" + track + ".png");
            bitmap = BitmapFactory.decodeStream(load);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (load != null){
                load.close();
            }

        }
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_score_activity, menu);
        MenuItem play = menu.findItem(R.id.play);
        MenuItem previous = menu.findItem(R.id.previous);
        MenuItem next = menu.findItem(R.id.next);
        MenuItem loop = menu.findItem(R.id.loop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            play.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_play_circle_filled_black_24dp, getTheme())));
            previous.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_skip_previous_black_24dp, getTheme())));
            next.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_skip_next_black_24dp, getTheme())));
            loop.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_repeat_one_black_24dp, getTheme())));
        } else {
            play.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_play_circle_filled_black_24dp)));
            previous.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_skip_previous_black_24dp)));
            next.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_skip_next_black_24dp)));
            loop.setIcon(setTintDrawable(getResources()
                    .getDrawable(R.drawable.ic_repeat_one_black_24dp)));
        }




        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.play:
                return true;
            case R.id.previous:

                return true;
            case R.id.next:
                return true;
            case R.id.loop:
                return true;
            case R.id.ac
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Drawable setTintDrawable(Drawable drawable){
        Drawable tintedDrawable = drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tintedDrawable.setTint(getResources().getColor(R.color.colorMenuIcon, getTheme()));
        } else {
            tintedDrawable.setTint(getResources().getColor(R.color.colorMenuIcon));
        }
        return tintedDrawable;

    }


    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
