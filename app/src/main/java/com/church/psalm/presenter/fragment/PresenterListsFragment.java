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
import io.realm.Sort;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by darrengu on 3/18/16.
 */
public class PresenterListsFragment implements Presenter {
    private ViewListFragment _view;
    private Context _context;
    private RealmResults<Song> _data;
    private Realm realm;
    private boolean isChecked;
    private int _currentOrder;

    public PresenterListsFragment(Context context) {
        _context = context;
        realm = Realm.getDefaultInstance();
        _currentOrder = 0;
    }

    public void setView(ViewListFragment view) {
        _view = view;
    }

    @Override
    public void start() {
        _view.showProgressDialog();
        if (isChecked) {
            _view.refreshListData(_data);
            _view.dismissProgressDialog();
        } else {
            realm.where(Song.class).findFirstAsync().asObservable()
                    .filter(song -> song.isLoaded())
                    .first()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(song -> {
                        if (!song.isValid()) {
                            addUsers();
                        } else {
                            updateAllSongs();
                        }
                    });
            isChecked = true;
        }
    }

    @Override
    public void stop() {
        realm.close();
    }


    public void onItemClick(int position) {
        if (isNetworkConnected()) {
            System.out.println("Pressed position " + position);
            Song song = _data.get(position);
            realm.beginTransaction();
            song.set_frequency(song.get_frequency() + 1);
            realm.commitTransaction();
            _view.updateItem(position);
            //_view.startScoreActivity(position);
        } else {
            _view.showErrorSnackbar();
        }
    }

    public void onFavStarClicked(int position) {
        Song song = _data.get(position);
        boolean isFav = song.is_favorite();
        realm.beginTransaction();
        song.set_favorite(!isFav);
        realm.commitTransaction();
        updateItem(position);
    }

    private void updateItem(int position) {
        if (_view != null) {
            _view.updateItem(position);
        }
    }

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
                .first()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    if (_view != null) {
                        _view.refreshListData(songs);
                        _view.dismissProgressDialog();
                    }
                    _data = songs;
                });
    }

    public void sortBy(int which) {
        if (which != _currentOrder) {
            switch (which) {
                case 0:
                    sortByTrackNumber();
                    break;
                case 1:
                    sortByAlphabeticalOrder();
                    break;
                case 2:
                    sortByFrequency();
                    break;
                default:
                    sortByTrackNumber();
            }
        }
    }

    private void sortByTrackNumber() {
        _currentOrder = 0;
        Log.d("Sort by", "Number");
        _data.sort("_id");
        _view.refreshListData(_data);
    }

    private void sortByAlphabeticalOrder() {
        _currentOrder = 1;
    }

    private void sortByFrequency() {
        _currentOrder = 2;
        Log.d("Sort by", "frequency");
        _data.sort("_frequency", Sort.DESCENDING);
        //_view.refreshAdapter();
        _view.refreshListData(_data);

    }

    public int getCurrentOrder() {
        return _currentOrder;
    }
}
