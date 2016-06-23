package com.church.psalm.view.fragment.categories;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.church.psalm.R;
import com.church.psalm.view.adapter.CategoryAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by darrengu on 6/19/16.
 */
public class CatSongzaiFragment extends CatBaseFragment {

    public CatSongzaiFragment() {

    }
/*    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        //adapter = new CategoryAdapter(getContext(), null);
        return view;
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("View Created", "Songzai");
    }


}
