package com.church.psalm.view.view;

import com.church.psalm.model.Song;

import io.realm.RealmResults;

/**
 * Created by darrengu on 4/3/16.
 */
public interface ViewSearchActivity {
    void clearQuery();
    void enableDeleteButton(boolean enable);

    void updateAdapter(RealmResults<Song> results);

    void showEmptyView();
}
