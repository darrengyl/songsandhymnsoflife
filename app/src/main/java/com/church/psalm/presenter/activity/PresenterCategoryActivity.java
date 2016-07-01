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
    List<Song> shengmingList;
    List<Song> jiduList;
    List<Song> yesuList;
    List<Song> shifengList;
    List<Song> zaijiList;
    List<Song> kewangList;
    List<Song> zanmeiList;
    List<Song> jingwenList;
    private Realm _realm;
    private ViewCategoryFragment readyFrag;
    private ViewCategoryActivity viewActivity;

    public PresenterCategoryActivity() {
        _realm = Realm.getDefaultInstance();
        songzaiList = new ArrayList<>();
        shenglingList = new ArrayList<>();
        jiaohuiList = new ArrayList<>();
        shengmingList = new ArrayList<>();
        jiduList = new ArrayList<>();
        yesuList = new ArrayList<>();
        shifengList = new ArrayList<>();
        zaijiList = new ArrayList<>();
        kewangList = new ArrayList<>();
        zanmeiList = new ArrayList<>();
        jingwenList = new ArrayList<>();
    }

    public void setReadyFrag(ViewCategoryFragment view) {
        readyFrag = view;
    }

    public void setViewActivity(ViewCategoryActivity view) {
        viewActivity = view;
    }

    public void songzaiReady() {
        if (songzaiList.size() == 0) {
            songzaiList.add(new Song(Constants.HEADER_ID, "神圣的三一"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 1, 8)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(Constants.HEADER_ID, "父神"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 9, 33)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(Constants.HEADER_ID, "子神"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 34, 125)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 528, 529)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(Constants.HEADER_ID, "在主的桌子前"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 126, 134)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            songzaiList.add(new Song(Constants.HEADER_ID, "对救恩的珍赏"));
            songzaiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 135, 173)
                    .or()
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
            jiaohuiList.add(new Song(Constants.HEADER_ID, "聚会"));
            jiaohuiList.add(_realm.where(Song.class)
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 187).findFirst());
            jiaohuiList.add(new Song(Constants.HEADER_ID, "彼此的交通"));
            jiaohuiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 188, 192)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 532, 536)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            jiaohuiList.add(new Song(Constants.HEADER_ID, "基督的身体"));
            jiaohuiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 193, 194)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            jiaohuiList.add(new Song(Constants.HEADER_ID, "其实际与定义"));
            jiaohuiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 195, 199)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 537, 544)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(jiaohuiList);
    }

    public void shengmingReady() {
        if (shengmingList.size() == 0) {
            shengmingList.add(new Song(Constants.HEADER_ID, "经历基督的爱"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 200, 210)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 545, 547)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "我们对基督的爱"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 211, 219)
                    .or()
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 548)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "对神和基督更深的渴慕"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 220, 249)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 549, 554)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "羡慕降服于基督"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 250, 280)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 555, 557)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "经历鼓励和试炼中的安慰"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 281, 310)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "经历十字架"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 311, 322)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 558, 559)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "与基督是一"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 323, 330)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 560, 561)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "灵程中的寻求"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 331, 356)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 562, 564)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "经历并享受基督"));
            shengmingList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 357, 374)
                    .or()
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 565)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            shengmingList.add(new Song(Constants.HEADER_ID, "羡慕生命的成长"));
            shengmingList.add(_realm.where(Song.class)
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 375)
                    .findFirst());
        }
        readyFrag.setData(shengmingList);
    }

    public void jiduReady() {
        if (jiduList.size() == 0) {
            jiduList.add(new Song(Constants.HEADER_ID, "享受主话"));
            jiaohuiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 376, 385)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            jiduList.add(new Song(Constants.HEADER_ID, "祷告的生活"));
            jiaohuiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 386, 392)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            jiduList.add(new Song(Constants.HEADER_ID, "与主交通"));
            jiduList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 393, 414)
                    .or()
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 566)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            jiduList.add(new Song(Constants.HEADER_ID, "献上自己跟随主"));
            jiduList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 415, 420)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 567, 569)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            jiduList.add(new Song(Constants.HEADER_ID, "彰显主"));
            jiduList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 421, 424)
                    .or()
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 570)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(jiaohuiList);
    }

    public void yesuReady() {
        if (yesuList.size() == 0) {
            yesuList.add(new Song(Constants.HEADER_ID, "传扬福音"));
            yesuList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 425, 431)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            yesuList.add(new Song(Constants.HEADER_ID, "大喜信息"));
            yesuList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 432, 485)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 571, 581)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
            yesuList.add(new Song(Constants.HEADER_ID, "受浸"));
            yesuList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 486, 486)
                    .or()
                    .equalTo(Constants.COLUMN_TRACK_NUMBER, 582)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(yesuList);
    }

    public void shifengReady() {
        if (shifengList.size() == 0) {
            shifengList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 488, 492)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(shifengList);
    }

    public void zaijiReady() {
        if (zaijiList.size() == 0) {
            zaijiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 493, 506)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(zaijiList);
    }

    public void kewangReady() {
        if (kewangList.size() == 0) {
            kewangList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 507, 517)
                    .or()
                    .between(Constants.COLUMN_TRACK_NUMBER, 583, 586)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(kewangList);
    }

    public void zanmeiReady() {
        if (zanmeiList.size() == 0) {
            zanmeiList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 518, 523)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(zanmeiList);
    }

    public void jingwenReady() {
        if (jingwenList.size() == 0) {
            jingwenList.addAll(_realm.where(Song.class)
                    .between(Constants.COLUMN_TRACK_NUMBER, 524, 527)
                    .findAllSorted(Constants.COLUMN_TRACK_NUMBER));
        }
        readyFrag.setData(jingwenList);
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

