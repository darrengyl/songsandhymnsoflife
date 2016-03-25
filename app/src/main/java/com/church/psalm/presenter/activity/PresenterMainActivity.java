package com.church.psalm.presenter.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.church.psalm.R;
import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewMainActivity;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by darrengu on 3/18/16.
 */
public class PresenterMainActivity implements Presenter {
    private ViewMainActivity _view;
    private Context _context;
    private boolean _doesDbExist;
    private Subscription _subscription;

    public PresenterMainActivity(Context context) {
        _context = context;
    }

    public void setView(ViewMainActivity view) {
        _view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public void setDatabase(boolean doesDbExist) {
        _doesDbExist = doesDbExist;
    }




}
