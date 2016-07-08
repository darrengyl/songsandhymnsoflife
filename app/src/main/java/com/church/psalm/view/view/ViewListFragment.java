package com.church.psalm.view.view;

import com.church.psalm.model.Song;

import java.util.HashMap;

import io.realm.RealmResults;

/**
 * Created by darrengu on 3/17/16.
 */
public interface ViewListFragment {
    void showProgressDialog();

    void dismissProgressDialog();

    void refreshListData(RealmResults<Song> songs);
    void refreshAdapter();

    void startScoreActivity(int position);

    void showErrorSnackbar();
    void enableFastScroll(boolean enable);
    void setSectionIndexData(HashMap<Character, Integer> map, Character[] sections);
    void showInfoSnackbar(String message);

}
