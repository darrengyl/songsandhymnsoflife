package com.church.psalm.presenter.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.MainThread;
import android.text.TextUtils;

import com.church.psalm.R;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewSearchActivity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by darrengu on 4/3/16.
 */
public class PresenterSearchActivity implements Presenter {
    private ViewSearchActivity _view;
    private boolean _deleteEnabled;
    private Context _context;
    private Realm _realm;
    private RealmResults<Song> _titleResults;
    private RealmResults<Song> _lyricsResults;
    private Subscription _subscription;

    public PresenterSearchActivity(Context context) {
        _context = context;
        _realm = Realm.getDefaultInstance();
    }

    public void setView(ViewSearchActivity view) {
        _view = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    public void onTextChanged(String keyword) {
        if (keyword.length() > 0) {
            _view.enableDeleteButton(true);
            _deleteEnabled = true;
        } else {
            _view.enableDeleteButton(false);
            _deleteEnabled = false;
        }
        search(keyword);
    }

    public void onClickDelete() {
        if (_deleteEnabled) {
            _view.clearQuery();
        }
    }

    public void search(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            _realm.where(Song.class)
                    .beginGroup()
                    .contains(Constants.COLUMN_TITLE, keyword)
                    .or()
                    .contains(Constants.COLUMN_LYRICS, keyword)
                    .endGroup()
                    .findAllSortedAsync(Constants.COLUMN_TRACK_NUMBER)
                    .asObservable()
                    .filter(new Func1<RealmResults<Song>, Boolean>() {
                        @Override
                        public Boolean call(RealmResults<Song> songs) {
                            return songs.isLoaded();
                        }
                    })
                    .first()
                    /*.map(Songs -> {
                        if (Songs.size() > 50) {
                            Songs.s
                            RealmList<Song> filtered = new RealmList<>();
                            filtered.addAll(Songs.subList(0, 50));
                            return filtered.where().findAll();
                        } else {
                            return Songs;
                        }
                    })*/
                    .map(Songs -> {
                        int limit = Songs.size() < Constants.SEARCH_FILTER_NUMBER ? Songs.size() : Constants.SEARCH_FILTER_NUMBER;
                        for (int i = 0; i < limit; i++) {
                            Song song = Songs.get(i);
                            if (song.get_title().contains(keyword)) {
                                _realm.beginTransaction();
                                song.set_firstOccurrence(-1);
                                _realm.commitTransaction();
                            } else {
                                int firstOccurrence = song.get_lyrics().indexOf(keyword);
                                if (firstOccurrence != -1) {
                                    _realm.beginTransaction();
                                    song.set_firstOccurrence(firstOccurrence);
                                    _realm.commitTransaction();
                                }
                            }
                        }
                        return Songs;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(songs -> {
                        _view.updateAdapter(songs);
                    });
        } else {
            _view.showEmptyView();
        }
    }

    private void updateAdapter(RealmResults<Song> results) {
        _view.updateAdapter(results);
    }

    private Observable<RealmResults<Song>> _getSearchObservable(String keyword) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<Song>>() {
            @Override
            public void call(Subscriber<? super RealmResults<Song>> subscriber) {
                if (!TextUtils.isEmpty(keyword)) {
                    _realm.where(Song.class)
                            .beginGroup()
                            .contains(Constants.COLUMN_TITLE, keyword)
                            .or()
                            .contains(Constants.COLUMN_LYRICS, keyword)
                            .endGroup()
                            .findAll()
                            .asObservable()
                            .filter(new Func1<RealmResults<Song>, Boolean>() {
                                @Override
                                public Boolean call(RealmResults<Song> songs) {
                                    return songs.isLoaded();
                                }
                            })
                            .first()
                            .map(new Func1<RealmResults<Song>, RealmResults<Song>>() {
                                @Override
                                public RealmResults<Song> call(RealmResults<Song> songs) {
                                    _titleResults = songs;

                                    return null;
                                }
                            });
                    /*_realm.where(Song.class)
                            .contains("_lyrics", keyword)
                            .findAll()
                            .asObservable()
                            .filter(songs -> songs.isLoaded())
                            .first()
                            .map(songs -> {
                                for (Song song : songs) {
                                    song.set_firstOccurrence(song.get_lyrics().indexOf(keyword));
                                }
                                _titleResults.addAll(songs);
                                return songs;
                            });*/
                    subscriber.onNext(_titleResults);
                }
            }
        });
    }
}
