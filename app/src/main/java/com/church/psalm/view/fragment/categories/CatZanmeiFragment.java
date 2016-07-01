package com.church.psalm.view.fragment.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.church.psalm.R;

import butterknife.ButterKnife;

/**
 * Created by darrengu on 6/19/16.
 */
public class CatZanmeiFragment extends CatBaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("View Created", "Zanmei");
        presenter.zanmeiReady();
    }
}
