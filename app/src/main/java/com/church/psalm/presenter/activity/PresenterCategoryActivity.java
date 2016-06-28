package com.church.psalm.presenter.activity;

import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.view.view.ViewCategoryActivity;
import com.church.psalm.view.view.ViewCategoryFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by darrengu on 6/19/16.
 */
public class PresenterCategoryActivity {
    List<Song> songzaiList;
    List<Song> shenglingList;
    List<Song> jiaohuiList;
    RealmResults<Song> shengmingList;
    RealmResults<Song> jiduList;
    RealmResults<Song> yesuList;
    RealmResults<Song> shifengList;
    RealmResults<Song> zaijiList;
    RealmResults<Song> kewangList;
    RealmResults<Song> zanmeiList;
    RealmResults<Song> jingwenList;
    private Realm _realm;
    private ViewCategoryFragment readyFrag;
    private ViewCategoryActivity viewActivity;

    public PresenterCategoryActivity() {
        _realm = Realm.getDefaultInstance();
        songzaiList = new ArrayList<>();
        shenglingList = new ArrayList<>();
        jiaohuiList = new ArrayList<>();
    }

    public void setReadyFrag(ViewCategoryFragment view) {
        readyFrag = view;
    }

    public void setViewActivity(ViewCategoryActivity view) {
        viewActivity = view;
    }

    public void songzaiReady() {
        if (songzaiList.size() == 0) {
            songzaiList.add(new Song(0, "神圣的三一"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 1, 8)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(0, "父神"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 9, 33)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(0, "子神"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 34, 125)
                    .between(Constants.COLUMN_TRACK_NUMBER, 528, 529)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(0, "在主的桌子前"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 126, 134)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(0, "对救恩的珍赏"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 135, 174)
                    .between(Constants.COLUMN_TRACK_NUMBER, 530, 531)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(songzaiList);
    }

    public void shenglingReady() {
        if (shenglingList.size() == 0) {
            shenglingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 174, 186)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(shenglingList);
    }

    public void jiaohuiReady() {
        if (jiaohuiList.size() == 0) {

        }
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
        viewActivity.getCurrentTab();
    }

    public void tabSelected(int position) {
        switch (position) {
            case 0:
                songzaiReady();
                break;
            case 1:
                shenglingReady();
                break;
        }
    }
}

