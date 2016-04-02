package com.church.psalm.view.view;

import com.church.psalm.model.Song;

import java.util.HashMap;
import java.util.List;

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
    void updateItem(int position);

    void enableFastScroll(boolean enable);
    void setSectionIndexData(HashMap<Character, Integer> map, Character[] sections);
}
