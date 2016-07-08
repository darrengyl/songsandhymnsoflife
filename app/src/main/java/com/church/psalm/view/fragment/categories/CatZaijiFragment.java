package com.church.psalm.view.fragment.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * Created by darrengu on 6/19/16.
 */
public class CatZaijiFragment extends CatBaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("View Created", "Zaiji");
        presenter.zaijiReady();
    }
}
