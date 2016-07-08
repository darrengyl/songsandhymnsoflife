package com.church.psalm.presenter.activity;

import com.church.psalm.view.view.ViewMainActivity;
import com.church.psalm.view.view.ViewTabbedFragment;

/**
 * Created by darrengu on 7/1/16.
 */
public class PresenterMainActivity {
    private ViewMainActivity _viewActivity;
    private ViewTabbedFragment _viewTabbed;

    public void setViewActivity(ViewMainActivity view) {
        _viewActivity = view;
    }

    public void setViewTabbed(ViewTabbedFragment view) {
        _viewTabbed = view;
    }

    public void onTabSelected(int position) {
        if (_viewActivity != null) {
            _viewActivity.onTabSelected(position);
        }
    }

    public void onToolbarClick() {
        _viewTabbed.onToolbarClick();
    }

    public void onClickSortBy() {
        _viewTabbed.onClickSortBy();
    }
}
