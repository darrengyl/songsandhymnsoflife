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

import io.realm.Realm;
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
                    .contains(Constants.COLUMN_TITLE, keyword)
                    .findAll()
                    .asObservable()
                    .filter(new Func1<RealmResults<Song>, Boolean>() {
                        @Override
                        public Boolean call(RealmResults<Song> songs) {
                            return songs.isLoaded();
                        }
                    })
                    .first()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(songs -> {
                        _view.updateAdapter(songs);
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
                        return songs;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(songs -> {
                        _lyricsResults = songs;
                    });*/
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
                            .contains("_title", keyword)
                            .findAll()
                            .asObservable()
                            .filter(new Func1<RealmResults<Song>, Boolean>() {
                                @Override
                                public Boolean call(RealmResults<Song> songs) {
                                    return songs.isLoaded();
                                }
                            })
                            .first()
                            .map(songs -> _titleResults = songs);
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
