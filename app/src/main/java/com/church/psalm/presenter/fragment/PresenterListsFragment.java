package com.church.psalm.presenter.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.church.psalm.R;
import com.church.psalm.Util;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewListFragment;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by darrengu on 3/18/16.
 */
public class PresenterListsFragment implements Presenter {
    private static final int TRACK_ORDER = 0;
    private static final int ALPHA_ORDER = 1;
    private static final int FREQ_ORDER = 2;
    private ViewListFragment _view;
    private Context _context;
    private RealmResults<Song> _data;
    private Realm realm;
    private int _currentOrder;
    private HashMap<Character, Integer> _sectionMap;
    private RealmAsyncTask _transaction;
    private Subscription _subscription;

    public PresenterListsFragment(Context context) {
        _context = context;
        _currentOrder = TRACK_ORDER;
        _sectionMap = new HashMap<>();

    }

    public void setView(ViewListFragment view) {
        _view = view;
    }

    @Override
    public void start() {
        _view.showProgressDialog();
        if (_data != null) {
            _view.refreshListData(_data);
            if (_currentOrder == ALPHA_ORDER) {
                _view.enableFastScroll(true);
            }
            _view.dismissProgressDialog();
        } else {
            if (doesRealmExist()) {
                realm = Realm.getDefaultInstance();
                updateAllSongsAndSort(Constants.COLUMN_TRACK_NUMBER, Sort.ASCENDING);
            } else {
                realm = Realm.getDefaultInstance();
                addUsers();
            }
        }
    }

    @Override
    public void stop() {
        if (_transaction != null && !_transaction.isCancelled()) {
            _transaction.cancel();
        }
        if (!_subscription.isUnsubscribed()) {
            _subscription.unsubscribe();
        }
        realm.close();
    }


    public void onItemClick(int position) {
        if (Util.isNetworkConnected()) {
            System.out.println("Pressed position " + position);
            incrementFreq(position);
            _view.startScoreActivity(_data.get(position).get_trackNumber());
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
    }

    private void reSortData() {
        switch (_currentOrder) {
            case TRACK_ORDER:
                //updateAllSongsAndSort(Constants.COLUMN_TRACK_NUMBER, Sort.ASCENDING);
                sortBy(TRACK_ORDER);
                break;
            case ALPHA_ORDER:
                sortBy(ALPHA_ORDER);
                //updateAllSongsAndSort(Constants.COLUMN_PINYIN, Sort.ASCENDING);
                break;
            case FREQ_ORDER:
                sortBy(FREQ_ORDER);
                //updateAllSongsAndSort(Constants.COLUMN_FREQUENCY, Sort.DESCENDING);
        }
    }

    private void addUsers() {
        _transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String[] titles = _context.getResources().getStringArray(R.array.song_titles);
                String[] lyrics = _context.getResources().getStringArray(R.array.lyrics);
                List<Character> exclusive = Arrays.asList(',', '、', '(', ')', '!');
                HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
                format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
                format.setVCharType(HanyuPinyinVCharType.WITH_V);
                for (int i = 0; i < titles.length; i++) {
                    StringBuilder title = new StringBuilder();
                    for (char c : titles[i].toCharArray()) {
                        if (!exclusive.contains(c)) {
                            try {
                                //pinyin4j caused this
                                Log.d("Pinyin conversion", c + " in " + titles[i]);
                                if (c == '哦') {
                                    title.append("O");
                                    continue;
                                }
                                String[] output = PinyinHelper.toHanyuPinyinStringArray(c, format);
                                title.append(output[0]);
                            } catch (BadHanyuPinyinOutputFormatCombination e) {
                                Log.d("Pinyin Exception", e.toString());
                            }
                        }
                    }
                    Song song = new Song();
                    song.set_id(i);
                    song.set_trackNumber(i + 1);
                    song.set_title(titles[i]);
                    song.set_pinyin(title.toString());
                    song.set_favorite(false);
                    song.set_frequency(0);
                    song.set_lyrics(lyrics[i]);
                    song.set_downloaded(false);
                    realm.copyToRealm(song);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                updateAllSongsAndSort(Constants.COLUMN_TRACK_NUMBER, Sort.ASCENDING);
            }
        });
    }

    private void updateAllSongsAndSort(String sortColumn, Sort sortOrder) {
        _subscription = realm.where(Song.class)
                .findAllSortedAsync(sortColumn, sortOrder)
                .asObservable()
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
        if (which == _currentOrder) {
            return;
        }
        switch (which) {
            case TRACK_ORDER:
                sortByTrackNumber();
                break;
            case ALPHA_ORDER:
                sortByAlphabeticalOrder();
                break;
            case FREQ_ORDER:
                sortByFrequency();
                break;
            default:
                sortByTrackNumber();
                Log.wtf("WTF", "This should never be called");
        }
    }

    private void sortByTrackNumber() {
        _currentOrder = TRACK_ORDER;
        Log.d("Sort by", "Number");
        RealmResults<Song> sortedData = _data.sort(Constants.COLUMN_TRACK_NUMBER);
        _view.refreshListData(sortedData);
        _data = sortedData;
        _view.enableFastScroll(false);
        _view.showInfoSnackbar("Sorted by Track Number");
    }

    private void sortByAlphabeticalOrder() {
        _currentOrder = ALPHA_ORDER;
        Log.d("Sort by", "alphabetic");
        RealmResults<Song> sortedData = _data.sort(Constants.COLUMN_PINYIN);
        _data = sortedData;
        setSectionIndexData();
        _view.refreshListData(sortedData);
        _view.enableFastScroll(true);
        _view.showInfoSnackbar("Sorted by Alphabetical Order");
    }

    private void sortByFrequency() {
        _currentOrder = FREQ_ORDER;
        Log.d("Sort by", "frequency");
        RealmResults<Song> sortedData = _data.sort(Constants.COLUMN_FREQUENCY, Sort.DESCENDING);
        _data = sortedData;
        _view.refreshListData(sortedData);
        _view.enableFastScroll(false);
        _view.showInfoSnackbar("Sorted by Frequency");
    }

    public int getCurrentOrder() {
        return _currentOrder;
    }

    private void setSectionIndexData() {
        if (_sectionMap.size() == 0) {
            int i = 0;
            while (i < _data.size()) {
                char c = _data.get(i).get_pinyin().charAt(0);
                if (_sectionMap.get(c) == null) {
                    _sectionMap.put(c, i);
                }
                i++;
            }
            List<Character> tempList = new ArrayList<>(_sectionMap.keySet());
            Collections.sort(tempList);
            Character[] _sections = tempList.toArray(new Character[_sectionMap.size()]);
            _view.setSectionIndexData(_sectionMap, _sections);
        }
    }

    public void incrementFreq(int position) {
        Song song = _data.get(position);
        realm.beginTransaction();
        Log.d("frequency", String.valueOf(song.get_frequency()));
        song.set_frequency(song.get_frequency() + 1);
        realm.commitTransaction();
        Log.d("frequency", String.valueOf(song.get_frequency()));
        Log.d("frequency", String.valueOf(_data.get(position).get_frequency()));
    }

    private boolean doesRealmExist() {
        File file = new File(_context.getFilesDir(), Constants.REALM_FILE);
        return file.exists();
    }
}
