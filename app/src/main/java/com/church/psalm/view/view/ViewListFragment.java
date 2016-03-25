package com.church.psalm.view.view;

import com.church.psalm.model.Song;

import java.util.List;

/**
 * Created by darrengu on 3/17/16.
 */
public interface ViewListFragment {
    void showProgressDialog();

    void dismissProgressDialog();

    void refreshListData(List<Song> songs);
    void refreshAdapter();

    void startScoreActivity(int position);

    void setFavStar(int position, int fav);

    void showErrorSnackbar();
}
