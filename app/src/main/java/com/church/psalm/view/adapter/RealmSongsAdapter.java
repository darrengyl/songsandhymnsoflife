package com.church.psalm.view.adapter;

import android.content.Context;

import com.church.psalm.model.Song;

import io.realm.RealmResults;

/**
 * Created by darrengu on 3/26/16.
 */
public class RealmSongsAdapter extends RealmModelAdapter<Song> {
    public RealmSongsAdapter(Context context, RealmResults<Song> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
