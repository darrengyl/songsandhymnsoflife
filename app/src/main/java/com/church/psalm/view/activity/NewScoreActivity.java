package com.church.psalm.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;

import com.church.psalm.MusicController;
import com.church.psalm.MusicService;
import com.church.psalm.MusicService.MusicBinder;
import com.church.psalm.R;
import com.church.psalm.presenter.activity.PresenterScoreActivity;
import com.church.psalm.songsandhymnsoflife;
import com.church.psalm.view.view.ViewScoreActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by darrengu on 4/14/16.
 */
public class NewScoreActivity extends AppCompatActivity implements MediaController.MediaPlayerControl, ViewScoreActivity {
    public static final String TRACK = "Track";
    public static final String MUSICBOUNDED = "Music Bound";
    public static final String SCORELINK =
            "http://shengmingshige.net/blog/wp-content/gallery/c-png/cu-%1$s.png";
    public static final String MUSICLINK = "http://shengmingshige.net/hymnal/mp3/%1$s.mp3";
    @Bind(R.id.image_view)
    ImageView imageView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.error_view)
    LinearLayout errorView;

    private int _trackNumber;
    private MusicController _controller;
    private boolean musicBound = false;
    private MusicService _musicService;
    @Inject
    PresenterScoreActivity presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            musicBound = savedInstanceState.getBoolean(MUSICBOUNDED);
        }
        setContentView(R.layout.score_layout);
        ((songsandhymnsoflife) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        _trackNumber = getIntent().getIntExtra(TRACK, -1);
        if (_trackNumber != -1) {
            loadImage();
            setController();
        }
    }

    private void loadImage() {
        Picasso.with(this)
                .load(getScoreLink(_trackNumber))
                .fit()
                .centerInside()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setScaleType(ImageView.ScaleType.CENTER);
                        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        errorView.setVisibility(View.VISIBLE);
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent playIntent = new Intent(getApplicationContext(), MusicService.class);
        if (!musicBound) {
            startService(playIntent);
        }
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MUSICBOUNDED, musicBound);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(musicConnection);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFinishing()) {
            stopService(new Intent(getApplicationContext(), MusicService.class));
            _musicService = null;
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            _musicService = binder.getService();
            //pass list
            if (!musicBound) {
                _musicService.setSong(getMusicLink(_trackNumber));
                _musicService.playSong();
            }
            musicBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

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

    private void setController() {
        _controller = new MusicController(this);
        _controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_trackNumber < 586) {
                    _trackNumber++;
                }
                imageView.setVisibility(View.GONE);
                loadImage();
                _musicService.setSong(getMusicLink(_trackNumber));
                _musicService.playSong();
                _controller.show(0);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_trackNumber > 1) {
                    _trackNumber--;
                }
                imageView.setVisibility(View.GONE);
                loadImage();
                _musicService.setSong(getMusicLink(_trackNumber));
                _musicService.playSong();
                _controller.show(0);
            }
        });
        _controller.setMediaPlayer(this);
        _controller.setAnchorView(imageView);
        _controller.setEnabled(true);
    }

    private String getScoreLink(int trackNumber) {
        return String.format(SCORELINK, new DecimalFormat("0000").format(trackNumber));
    }

    private String getMusicLink(int trackNumber) {
        return String.format(MUSICLINK, new DecimalFormat("0000").format(trackNumber));
    }

    public static Intent getLaunchIntent(Context context, int track) {
        Intent intent = new Intent(context, NewScoreActivity.class);
        intent.putExtra(TRACK, track);
        return intent;
    }
}
