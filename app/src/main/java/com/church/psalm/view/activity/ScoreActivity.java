package com.church.psalm.view.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.church.psalm.MusicService;
import com.church.psalm.MusicService.MusicBinder;
import com.church.psalm.R;
import com.church.psalm.Util;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.view.view.ViewScoreActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.church.psalm.R.id.share;

/**
 * Created by darrengu on 4/14/16.
 */
public class ScoreActivity extends AppCompatActivity implements ViewScoreActivity {
    public static final String TRACK = "Track";
    public static final String SCORELINK =
            "http://shengmingshige.net/blog/wp-content/gallery/c-png/cu-%1$s.png";
    public static final String MUSICLINK = "http://shengmingshige.net/hymnal/mp3/%1$s.mp3";
    @Bind(R.id.image_view)
    ImageView imageView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.error_view)
    TextView errorView;
    @Bind(R.id.tool_bar)
    Toolbar toolbar;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.lyrics_textview)
    TextView lyricsTextView;
    private Realm _realm;
    private int _trackNumber;
    private CustomMediaController _controller;
    private boolean musicBound = false;
    private MusicService _musicService;
    private ActionBar _toolbar;
    private Target _imageTarget;
    private MenuItem _lyricsMenuItem;
    private MenuItem _share;
    private String _lyrics;
    private PhotoViewAttacher _attacher;
    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Songsandhymnsoflife) getApplication()).getComponent().inject(this);
        if (savedInstanceState != null) {
            musicBound = true;
        }
        setContentView(R.layout.score_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                _realm = Realm.getDefaultInstance();
                _toolbar = getSupportActionBar();
                _toolbar.setDisplayShowTitleEnabled(false);
                _toolbar.setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().hide();
            }
        }

        _imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                imageView.setImageBitmap(scaledBitmap);
                if (ContextCompat.checkSelfPermission(ScoreActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    saveBitmapInBackground(scaledBitmap);
                }
                errorView.setVisibility(View.GONE);
                _attacher = new PhotoViewAttacher(imageView);
                _attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        Log.d("onclick", "photoview is clicked");
                        setOnClickListener();
                    }

                    @Override
                    public void onOutsidePhotoTap() {

                    }
                });
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                errorView.setVisibility(View.VISIBLE);
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        _trackNumber = getIntent().getIntExtra(TRACK, -1);
        if (_trackNumber != -1) {
            loadImage();
            setController();
        }
    }

    private void saveBitmapInBackground(Bitmap scaledBitmap) {
        Observable.just(scaledBitmap)
                .map(new Func1<Bitmap, Object>() {
                    @Override
                    public Object call(Bitmap bitmap) {
                        file = new File(getExternalFilesDir(null), Constants.SCORE_NAME);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (_share != null) {
                            _share.setVisible(true);
                        } else {
                            Log.d("Share", "Not ready yet");
                        }
                    }
                });
    }

    private void loadImage() {
        Picasso.with(this)
                .load(getScoreLink(_trackNumber))
                .into(_imageTarget);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            getMenuInflater().inflate(R.menu.menu_score_activity, menu);
            _share = menu.findItem(R.id.share);
            _lyricsMenuItem = menu.findItem(R.id.text_only);
        }
        return true;
    }

    @OnClick(R.id.lyrics_textview)
    public void onClickLyrics() {
        Log.d("onclick", "lyrics is clicked");
        setOnClickListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.text_only:
                if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    if (_lyrics != null) {
                        lyricsTextView.setText(_lyrics);
                    } else {
                        setLyricsTextView();
                    }
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
                flipIcon();
                break;
            case share:
                shareScore();
                break;
            case android.R.id.home:
                _controller.hide();
                stopService(new Intent(getApplicationContext(), MusicService.class));
                _musicService = null;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void flipIcon() {
        if (imageView.getVisibility() == View.VISIBLE) {
            _lyricsMenuItem.setIcon(R.drawable.ic_text_format_white_24dp);
        } else {
            _lyricsMenuItem.setIcon(R.drawable.ic_music_note_white_24dp);
        }
    }

    private void shareScore() {
        Intent share = new Intent(Intent.ACTION_SEND);
        if (imageView.getVisibility() == View.VISIBLE) {
            Uri imageUri = Uri.fromFile(file);
            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
        } else {
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, _lyrics);
        }
        if (Util.safeIntent(share)) {
            startActivity(share);
        }
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

    private void setController() {
        _controller = new CustomMediaController(this);
        _controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_trackNumber < 586) {
                    _trackNumber++;
                    onPrevNextClicked();
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_trackNumber > 1) {
                    _trackNumber--;
                    onPrevNextClicked();
                }
            }
        });
        _controller.setMediaPlayer(_controller);
        _controller.setAnchorView(imageView);
        _controller.setEnabled(true);
    }

    private void onPrevNextClicked() {
        _share.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        _toolbar.hide();
        _controller.hide();
        _attacher.setOnPhotoTapListener(null);
        loadImage();
        _musicService.setSong(getMusicLink(_trackNumber));
        _musicService.playSong();
    }

    private String getScoreLink(int trackNumber) {
        return String.format(SCORELINK, new DecimalFormat("0000").format(trackNumber));
    }

    private String getMusicLink(int trackNumber) {
        return String.format(MUSICLINK, new DecimalFormat("0000").format(trackNumber));
    }

    public static Intent getLaunchIntent(Context context, int track) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra(TRACK, track);
        return intent;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && _toolbar != null) {
            if (_toolbar.isShowing()) {
                _toolbar.hide();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void setLyricsTextView() {
        _realm.where(Song.class)
                .equalTo(Constants.COLUMN_TRACK_NUMBER, _trackNumber)
                .findFirstAsync()
                .asObservable()
                .filter(song -> song.isLoaded())
                .first()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(song -> {
                    _lyrics = ((Song) song).get_lyrics();
                    lyricsTextView.setText(_lyrics);
                });
    }

    private void setOnClickListener() {
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        if (_controller.isShowing()) {
            _controller.hide();
            if (granted) {
                _toolbar.hide();
            }
        } else {
            _controller.show(0);
            if (granted) {
                _toolbar.show();
            }
        }
    }

    private class CustomMediaController extends android.widget.MediaController implements android.widget.MediaController.MediaPlayerControl {

        public CustomMediaController(Context context) {
            super(context);
        }

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
    }
}
