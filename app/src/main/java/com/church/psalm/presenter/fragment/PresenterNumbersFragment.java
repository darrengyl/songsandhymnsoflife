package com.church.psalm.presenter.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewNumberFragment;

/**
 * Created by darrengu on 3/18/16.
 */
public class PresenterNumbersFragment implements Presenter {
    private Context _context;
    private StringBuilder stringBuilder;
    private ViewNumberFragment _view;

    public PresenterNumbersFragment(Context context) {
        _context = context;
        stringBuilder = new StringBuilder();
    }

    public void setView(ViewNumberFragment view) {
        _view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public void numberClicked(CharSequence num) {
        if (stringBuilder.length() < 3) {
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
        int trackNumber = Integer.valueOf(stringBuilder.toString());
        if (isNetworkConnected()) {
            if (trackNumber > 0 && trackNumber < 587) {
                _view.startScoreActivity(trackNumber);
                stringBuilder.setLength(0);
            } else {
                _view.showNumberError();
            }
        } else {
            _view.showNetworkError();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) _context.getSystemService(
                        _context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }
}
