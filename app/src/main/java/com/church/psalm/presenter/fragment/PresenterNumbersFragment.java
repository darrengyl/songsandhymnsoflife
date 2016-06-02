package com.church.psalm.presenter.fragment;

import android.text.TextUtils;

import com.church.psalm.Util;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewNumberFragment;

import io.realm.Realm;

/**
 * Created by darrengu on 3/18/16.
 */
public class PresenterNumbersFragment implements Presenter {
    private StringBuilder stringBuilder;
    private ViewNumberFragment _view;

    public PresenterNumbersFragment() {
        stringBuilder = new StringBuilder();
    }

    public void setView(ViewNumberFragment view) {
        _view = view;
    }

    @Override
    public void start() {
        if (_view != null) {
            _view.updateDisplay(stringBuilder.toString());
        }
    }

    @Override
    public void stop() {

    }

    public void numberClicked(CharSequence num) {
        if (stringBuilder.length() < 4) {
            stringBuilder.append(num);
            _view.updateDisplay(stringBuilder.toString());
        } else {
            _view.showNumberError();
        }
    }

    public void clearDisplay() {
        stringBuilder.setLength(0);
        _view.updateDisplay("");
    }

    public void backspaceClicked() {
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
            _view.updateDisplay(stringBuilder.toString());
        }
    }

    public void acceptClicked() {
        int trackNumber = -1;
        if (!TextUtils.isEmpty(stringBuilder.toString())) {
            trackNumber = Integer.valueOf(stringBuilder.toString());
        }
        if (Util.isNetworkConnected()) {
            if (trackNumber > 0 && trackNumber < 587) {
                _view.startScoreActivity(trackNumber);
                _view.incrementFreq(trackNumber);
                stringBuilder.setLength(0);
                _view.updateDisplay(stringBuilder.toString());
            } else {
                _view.showNumberError();
            }
        } else {
            _view.showNetworkError();
        }
    }

    public void incrementFreq(int trackNumber) {
        Realm.getDefaultInstance()
                .executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Song song = realm.where(Song.class)
                                .equalTo(Constants.COLUMN_TRACK_NUMBER, trackNumber)
                                .findFirst();
                        song.set_frequency(song.get_frequency() + 1);
                    }
                });
    }
}
