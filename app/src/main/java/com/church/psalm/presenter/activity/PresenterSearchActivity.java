package com.church.psalm.presenter.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.model.SongMatch;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewSearchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

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
    private int _count;

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
            _view.updateAdapter(new ArrayList<>());
        }
        search(keyword);
    }

    public void onClickDelete() {
        if (_deleteEnabled) {
            _view.clearQuery();
            _view.hideLimitBanner();
        }
    }

    public void search(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            _count = 0;
            Observable<SongMatch> titleQuery = _realm.where(Song.class)
                    .contains(Constants.COLUMN_TITLE, keyword)
                    .findAllAsync()
                    .asObservable()
                    //.subscribeOn(Schedulers.io())
                    .filter(songs -> songs.isLoaded())
                    .first()
                    .flatMap(songs -> Observable.from(songs))
                    .take(Constants.SEARCH_FILTER_NUMBER)
                    .flatMap(new Func1<Song, Observable<SongMatch>>() {
                        @Override
                        public Observable<SongMatch> call(Song song) {
                            Log.d("Thread_Title", String.valueOf(Thread.currentThread().getId()));
                            _count++;
                            SongMatch songMatch = new SongMatch();
                            songMatch.setInTitle(true);
                            songMatch.setKeyword(keyword);
                            songMatch.setSong(song);
                            int start = song.get_title().indexOf(keyword);
                            //exclusive
                            int end = start + keyword.length();
                            songMatch.setStart(start);
                            songMatch.setEnd(end);
                            return Observable.just(songMatch);
                        }
                    });

            if (_count < Constants.SEARCH_FILTER_NUMBER) {
                Observable<SongMatch> lyricsQuery = _realm.where(Song.class)
                        .contains(Constants.COLUMN_LYRICS, keyword)
                        .beginGroup()
                        .not()
                        .contains(Constants.COLUMN_TITLE, keyword)
                        .endGroup()
                        .findAllAsync()
                        .asObservable()
                        //.subscribeOn(Schedulers.io())
                        .filter(songs -> songs.isLoaded())
                        .first()
                        .flatMap(songs -> Observable.from(songs))
                        .take(Constants.SEARCH_FILTER_NUMBER - _count)
                        .flatMap(new Func1<Song, Observable<SongMatch>>() {
                            @Override
                            public Observable<SongMatch> call(Song song) {
                                Log.d("Thread_Lyrics", String.valueOf(Thread.currentThread().getId()));
                                SongMatch songMatch = new SongMatch();
                                songMatch.setInTitle(false);
                                songMatch.setKeyword(keyword);
                                songMatch.setSong(song);
                                String lyrics = song.get_lyrics();
                                int firstOccurrence = lyrics.indexOf(keyword);
                                int startOfSentence = lyrics.lastIndexOf("\n", firstOccurrence - 1) + 1;
                                int endOfSentence = lyrics.indexOf("\n", firstOccurrence);
                                if (endOfSentence == -1) {
                                    endOfSentence = lyrics.length();
                                }
                                if (startOfSentence < endOfSentence && startOfSentence * endOfSentence != 1) {
                                    String partialLyrics = lyrics.substring(startOfSentence, endOfSentence);
                                    songMatch.setPartialLyrics(partialLyrics);
                                    int start = partialLyrics.indexOf(keyword);
                                    //exclusive
                                    int end = start + keyword.length();
                                    songMatch.setStart(start);
                                    songMatch.setEnd(end);
                                }
                                return Observable.just(songMatch);
                            }
                        });

                Observable.merge(titleQuery, lyricsQuery)
                        //.subscribeOn(Schedulers.io())
                        //.observeOn(AndroidSchedulers.mainThread())
                        .take(Constants.SEARCH_FILTER_NUMBER)
                        .toSortedList(new Func2<SongMatch, SongMatch, Integer>() {
                            @Override
                            public Integer call(SongMatch songMatch, SongMatch songMatch2) {
                                return songMatch.compareTo(songMatch2);
                            }
                        })
                        .subscribe(songs -> {
                            if (songs.size() == Constants.SEARCH_FILTER_NUMBER) {
                                _view.showLimitBanner();
                            } else {
                                _view.hideLimitBanner();
                            }
                            _view.updateAdapter(songs);
                        });
            } else {
                titleQuery
                        .toSortedList(new Func2<SongMatch, SongMatch, Integer>() {
                            @Override
                            public Integer call(SongMatch songMatch, SongMatch songMatch2) {
                                return songMatch.compareTo(songMatch2);
                            }
                        })
                        .subscribe(songs -> {
                            if (songs.size() == Constants.SEARCH_FILTER_NUMBER) {
                                _view.showLimitBanner();
                            } else {
                                _view.hideLimitBanner();
                            }
                            _view.updateAdapter(songs);
                        });
            }
        } else {
            _view.hideLimitBanner();
            _view.showEmptyView();
        }
    }
}
