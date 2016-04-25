package com.church.psalm.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import butterknife.OnClick;
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
    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    private int _trackNumber;
    private MediaController _controller;
    private boolean musicBound = false;
    private MusicService _musicService;
    private ActionBar _toolbar;
    @Inject
    PresenterScoreActivity presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((songsandhymnsoflife) getApplication()).getComponent().inject(this);
        if (savedInstanceState != null) {
            musicBound = true;
        }
        setContentView(R.layout.score_layout);
        ButterKnife.bind(this);
        presenter.setView(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            _toolbar = getSupportActionBar();
            _toolbar.setDisplayShowTitleEnabled(false);
            _toolbar.setDisplayHomeAsUpEnabled(true);
        }
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
                        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                            @Override
                            public void onViewTap(View view, float x, float y) {
                                if (imageView.getVisibility() == View.VISIBLE) {
                                    if (_controller.isShowing()) {
                                        _controller.hide();
                                    } else {
                                        _controller.show(0);
                                    }
                                }
                                if (_toolbar.isShowing()) {
                                    _toolbar.hide();
                                } else {
                                    _toolbar.show();
                                }
                            }
                        });
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
    protected void onStop() {
        super.onStop();
        unbindService(musicConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_score_activity, menu);
        MenuItem share = menu.findItem(R.id.share);
        share.getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorScoreActivityToolbarIcon)
                , PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.share):
                if (imageView.getVisibility() == View.VISIBLE) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver()
                            , ((BitmapDrawable) imageView.getDrawable()).getBitmap(), "Score", null);
                    //String path = getScoreLink(_trackNumber);
                    if (path != null) {
                        Uri imageUri = Uri.parse(path);
                        share.setType("image/png");
                        share.putExtra(Intent.EXTRA_STREAM, imageUri);
                        startActivity(Intent.createChooser(share, "Share via..."));
                    }
                } else {
                    View view = findViewById(android.R.id.content);
                    if (view != null) {
                        Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG);
                    }
                }
                break;
            case android.R.id.home:
                /*stopService(new Intent(getApplicationContext(), MusicService.class));
                //_musicService = null;
                NavUtils.navigateUpFromSameTask(this);*/
                break;
        }
        return super.onOptionsItemSelected(item);
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
        _musicService.go();
    }

    @Override
    public void pause() {
        _musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        return _musicService.getDur();
    }

    @Override
    public int getCurrentPosition() {
        return _musicService.getPosn();
    }

    @Override
    public void seekTo(int pos) {
        _musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        return _musicService.isPng();
    }

    @Override
    public int getBufferPercentage() {
        return _musicService.getBufferPercentage();
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
        return 0;
    }

    private void setController() {
        _controller = new MediaController(this);
        _controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_trackNumber < 586) {
                    _trackNumber++;
                }
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
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
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                loadImage();
                _musicService.setSong(getMusicLink(_trackNumber));
                _musicService.playSong();
                _controller.show(0);
            }
        });
        _controller.setMediaPlayer(this);
        _controller.setAnchorView(imageView);
        _controller.setEnabled(true);
        //_controller.show(0);
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (_toolbar.isShowing()) {
                _toolbar.hide();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
