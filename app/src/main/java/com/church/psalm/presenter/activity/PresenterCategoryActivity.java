package com.church.psalm.presenter.activity;

import android.view.View;

import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.view.view.ViewCategoryActivity;
import com.church.psalm.view.view.ViewCategoryFragment;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by darrengu on 6/19/16.
 */
public class PresenterCategoryActivity {
    RealmResults<Song> songzaiList;
    RealmResults<Song> shenglingList;
    RealmResults<Song> jiaohuiList;
    RealmResults<Song> shengmingList;
    RealmResults<Song> jiduList;
    RealmResults<Song> yesuList;
    RealmResults<Song> shifengList;
    RealmResults<Song> zaijiList;
    RealmResults<Song> kewangList;
    RealmResults<Song> zanmeiList;
    RealmResults<Song> jingwenList;
    private Realm _realm;
    private ViewCategoryFragment viewFrag;
    private ViewCategoryActivity viewActivity;

    public PresenterCategoryActivity() {
        _realm = Realm.getDefaultInstance();
        /*_realm.where(Song.class)
                .findAllSortedAsync(Constants.COLUMN_TRACK_NUMBER)*/

    }

    public void setViewFrag(ViewCategoryFragment view) {
        viewFrag = view;
    }

    public void setViewActivity(ViewCategoryActivity view) {
        viewActivity = view;
    }

    public void songzaiReady() {

    }

    public void shenglingReady() {

    }

    public void jiaohuiReady() {

    }

    public void shengmingReady() {

    }

    public void jiduReady() {

    }

    public void yesuReady() {

    }

    public void shifengReady() {

    }

    public void zaijiReady() {

    }

    public void kewangReady() {

    }

    public void zanmeiReady() {

    }

    public void jingwenReady() {

    }

    public void onItemClicked(int position) {

    }
}
