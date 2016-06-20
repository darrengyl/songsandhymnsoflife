package com.church.psalm.view.fragment.categories;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class CatSongzaiFragment extends Fragment {
    @Bind(R.id.category_list)
    ListView listView;
    CategoryAdapter _adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        _adapter = new CategoryAdapter(getContext(), null);
        listView.setAdapter(_adapter);
        return view;
    }


}
