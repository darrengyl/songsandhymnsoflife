package com.church.psalm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;


import com.church.psalm.MusicService;
import com.church.psalm.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by yuanlong.gu on 10/7/2015.
 */
public class ScoreActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener
        , MediaPlayerControl {
    Toolbar toolbar;
    PhotoViewAttacher mAttacher;
    static int screenWidth;
    static int screenHeight;
    @Bind(R.id.imageView)
    ImageView imageView;
    private int oriImageWidth;
    private int oriImageHeight;
    private MediaController mediacontroller;
    private MusicService musicService;
    private boolean musicBound;
    private boolean paused, playbackPaused;
    private MediaPlayer mediaPlayer;
    private int musicPosition;
    Bitmap bitmap;
    SeekBar seekbar;
    private static String TRACK = "track number";

    private Handler handler = new Handler();

    int trackNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        ButterKnife.bind(this);

        getScreenResolution(this);

        mAttacher = new PhotoViewAttacher(imageView);
        Intent intent = getIntent();
        trackNumber = intent.getIntExtra("trackNumber", 1);


//TODO: imageCache, find solutions to OutOfMemoryError, remove status bar


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediacontroller = new MediaController(this);
        try {
            mediaPlayer.setDataSource(getMusicLink(trackNumber));
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediacontroller.isShowing()) {
                    mediacontroller.hide();
                } else {
                    mediacontroller.show(0);
                }

            }
        });

    }

    public static Intent getLaunchIntent(Context context, int track) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra(TRACK, track);
        return intent;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mediaPlayer.getCurrentPosition());
        outState.putInt("track", trackNumber);

    }

    //Will only be called when activity is destroyed by OS. e.g. rotation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (trackNumber == savedInstanceState.getInt("track")) {
            musicPosition = savedInstanceState.getInt("position");
        } else {
            musicPosition = 0;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediacontroller.isShowing()) {
            mediacontroller.hide();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

/*        if (!imageLoader.isCached(getScoreLink(trackNumber), 0, 0)){

        }*/
        if (bitmap != null && !bitmap.isRecycled()) {
            //bitmap.recycle();
            bitmap = null;
        }
        System.gc();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //TODO: Change to a specific fragment
/*    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/

    /*
                private ServiceConnection musicConnection = new ServiceConnection(){

                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
                        musicService = binder.getService();
                        musicBound = true;
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        musicBound = false;
                    }
                };*/
    private static void getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

    }


    public String getScoreLink(int track) {
        String scoreLink = "http://shengmingshige.net/blog/wp-content/gallery/c-png/cu-"
                + new DecimalFormat("0000").format(track) + ".png";
        return scoreLink;
    }

    public String getMusicLink(int track) {
        String musicLink = "http://shengmingshige.net/hymnal/mp3/"
                + new DecimalFormat("0000").format(track) + ".mp3";
        return musicLink;
    }

    public boolean foundScoreInStorage(int track) {
        File score = new File(this.getFilesDir() + "/Score" + track + ".png");
        return (score.exists());
    }

    public boolean foundMusicInStorage(int track) {
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
            if (save != null) {
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
            if (load != null) {
                load.close();
            }

        }
        return bitmap;
    }



    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        System.out.println("On prepared");
        mediacontroller.setMediaPlayer(this);
        mediacontroller.setAnchorView(imageView);
        mediacontroller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackNumber > 0 && trackNumber < 586) {
                    Intent intent = new Intent(v.getContext(), ScoreActivity.class);
                    intent.putExtra("trackNumber", trackNumber + 1);
                    v.getContext().startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(v, v.getContext()
                                    .getString(R.string.end_of_list)
                            , Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(v.getContext().getResources()
                            .getColor(R.color.colorAccent));
                    snackbar.show();
                }


            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackNumber > 1) {
                    Intent intent = new Intent(v.getContext(), ScoreActivity.class);
                    intent.putExtra("trackNumber", trackNumber - 1);
                    v.getContext().startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(v, v.getContext()
                                    .getString(R.string.beginning_of_list)
                            , Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(v.getContext().getResources()
                            .getColor(R.color.colorAccent));
                    snackbar.show();
                }

            }
        });
        int topContainerId = getResources().getIdentifier("mediacontroller_progress", "id", "android");
        seekbar = (SeekBar) mediacontroller.findViewById(topContainerId);
        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (percent < seekbar.getMax()) {
                    seekbar.setSecondaryProgress(percent);

                }
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediacontroller.setEnabled(true);

                if (musicPosition != 0) {
                    mediaPlayer.seekTo(musicPosition);
                    mediaPlayer.start();
                } else {
                    mediacontroller.show(5000);
                }


            }
        });

    }


}
