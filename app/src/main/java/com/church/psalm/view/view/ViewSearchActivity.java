package com.church.psalm.view.view;

import com.church.psalm.model.SongMatch;

import java.util.List;

/**
 * Created by darrengu on 4/3/16.
 */
public interface ViewSearchActivity {
    void clearQuery();
    void enableDeleteButton(boolean enable);

    void updateAdapter(List<SongMatch> results);

    void showEmptyView();
    void hideEmptyView();

    void showLimitBanner();

    void hideLimitBanner();
}
