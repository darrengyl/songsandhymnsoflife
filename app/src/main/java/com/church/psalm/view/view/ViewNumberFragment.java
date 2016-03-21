package com.church.psalm.view.view;

/**
 * Created by darrengu on 3/17/16.
 */
public interface ViewNumberFragment {
    void updateDisplay(String s);
    void startScoreActivity(int trackNumber);

    void showNumberError();
    void showNetworkError();
}
