package com.church.psalm.presenter.activity;

import com.church.psalm.presenter.Presenter;
import com.church.psalm.view.view.ViewMainActivity;
import com.church.psalm.view.view.ViewMainFragment;

/**
 * Created by darrengu on 7/1/16.
 */
public class PresenterMainActivity implements Presenter {
    private ViewMainActivity _viewActivity;
    private ViewMainFragment _viewFragment;

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public void setViewActivity(ViewMainActivity view) {
        _viewActivity = view;
    }

    public void setViewFragment(ViewMainFragment view) {
        _viewFragment = view;
    }

    public void onTabSelected(int position) {
        if (_viewActivity != null) {
            _viewActivity.onTabSelected(position);
        }
    }

    public void onToolbarClick() {
        _viewFragment.onToolbarClick();
    }

    public void onClickSortBy() {
        _viewFragment.onClickSortBy();
    }
}
