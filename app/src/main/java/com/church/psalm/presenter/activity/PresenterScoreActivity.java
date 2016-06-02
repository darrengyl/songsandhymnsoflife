package com.church.psalm.presenter.activity;

import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewScoreActivity;

/**
 * Created by darrengu on 3/21/16.
 */
public class PresenterScoreActivity implements Presenter {
    private ViewScoreActivity _view;

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public void setView(ViewScoreActivity view) {
        _view = view;
    }
}
