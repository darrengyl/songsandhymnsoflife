package com.church.psalm.presenter.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.model.AllSongsContract;
import com.church.psalm.model.Song;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.activity.ScoreActivity;
import com.church.psalm.view.view.ViewListFragment;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by darrengu on 3/18/16.
 */
public class PresenterListsFragment implements Presenter {
    private ViewListFragment _view;
    private Context _context;
    private List<Song> _data;
    private RealmChangeListener realmChangeListener;
    private RealmChangeListener singleItemChangeListener;
    private Realm realm;

    public PresenterListsFragment(Context context) {
        _context = context;
        realm = Realm.getDefaultInstance();


    }

    public void setView(ViewListFragment view) {
        _view = view;
    }

    @Override
    public void start() {
        realm.where(Song.class).findFirstAsync().asObservable()
                .filter(song -> song.isLoaded())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(song -> {
                    if (!song.isValid()) {
                        addUsers();
                    } else {
                        updateAllSongs();
                    }
                });
        //!doesDatabaseExist()
        /*if (!doesDatabaseExist()) {
            _view.showProgressDialog();
            Observable<Boolean> createDatabase = Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    try {
                        boolean error = insertSongs();
                        subscriber.onNext(error);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            });
            createDatabase.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean error) {
                            if (error) {
                                Log.d("Database", "Song insertions finished with error(s)");
                            }
                            _data = _dbAdapter.getAllSongs();
                            _view.refreshListData(_data);
                            _view.dismissProgressDialog();
                        }
                    });
        } else {
            _data = _dbAdapter.getAllSongs();
            _view.refreshListData(_data);
        }*/
    }

    @Override
    public void stop() {
        realm.close();
    }

/*    private boolean insertSongs() {
        boolean error = false;
        String[] title = _context.getResources().getStringArray(R.array.song_titles);
        for (int i = 0; i < title.length; i++) {
            if (_dbAdapter.insertSongData(i + 1, title[i], 0, 0, "", 0) == -1) {
                Log.d("Database error", "error occurred when inserting" + String.valueOf(i + 1) + title[i]);
                error = true;
            }
        }
        return error;
    }*/

    private boolean doesDatabaseExist() {
        File dbFile = _context.getDatabasePath(AllSongsContract.AllSongsEntry.TABLE_NAME);
        return dbFile.exists();
    }

/*    public void onItemClick(int position) {
        if (isNetworkConnected()) {
            System.out.println("Pressed position " + position);
            _dbAdapter.incrementFreq(position);
            int freqInt = _dbAdapter.getFreq(position);
            Song song = _data.get(position);
            //song.setFrequency(freqInt);
            _data.set(position, song);
            _view.startScoreActivity(position);
        } else {
            _view.showErrorSnackbar();
        }
    }

    public void onFavStarClicked(int position) {
        _dbAdapter.flipFav(position);
        int favInt = _dbAdapter.getFav(position) ? 1 : 0;
        _view.setFavStar(position, favInt);
    }*/

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(
                _context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    private void addUsers() {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String[] titles = _context.getResources().getStringArray(R.array.song_titles);
                for (int i = 0; i < titles.length; i++) {
                    Song song = new Song();
                    song.set_id(i);
                    song.set_trackNumber(i + 1);
                    song.set_title(titles[i]);
                    song.set_favorite(false);
                    song.set_frequency(0);
                    song.set_lyrics("");
                    song.set_downloaded(false);
                    realm.copyToRealm(song);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                updateAllSongs();
            }
        });
    }

    private void updateAllSongs() {
        realm.where(Song.class).findAllSortedAsync("_id").asObservable()
                .filter(song -> song.isLoaded())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    if (_view != null) {
                        _view.refreshListData(songs);
                    }
                });
    }
}
