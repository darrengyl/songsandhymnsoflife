package com.church.psalm.presenter.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.church.psalm.DbAdapter;
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
    private DbAdapter _dbAdapter;

    public PresenterMainActivity(Context context) {
        _context = context;
    }

    public void setView(ViewMainActivity view) {
        _view = view;
    }

    @Override
    public void start() {
        _dbAdapter = new DbAdapter(_context);
        if (!_doesDbExist) {
            _view.showProgressDialog();
            Observable createDatabase = Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    try {
                        boolean error = insertSongs();
                        subscriber.onNext(error);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        //TODO:Snackbar
                        _view.dismissProgressDialog();
                    }
                }
            });
            createDatabase.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean error) {
                            if (!error) {
                                _view.refreshListData();
                            }
                            _view.dismissProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void stop() {

    }

    public void setDatabase(boolean doesDbExist) {
        _doesDbExist = doesDbExist;
    }




}
