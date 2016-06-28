package com.church.psalm.view.fragment.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.church.psalm.R;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.model.Song;
import com.church.psalm.presenter.activity.PresenterCategoryActivity;
import com.church.psalm.view.adapter.CategoryAdapter;
import com.church.psalm.view.view.ViewCategoryFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.realm.RealmResults;

/**
 * Created by darrengu on 6/22/16.
 */
public class CatBaseFragment extends Fragment implements ViewCategoryFragment{
    @Bind(R.id.category_list)
    ListView categoryList;
    @Inject
    PresenterCategoryActivity presenter;
    CategoryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Songsandhymnsoflife) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        adapter = new CategoryAdapter(getContext());
        categoryList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setReadyFrag(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnItemClick(R.id.category_list)
    public void onCategoryItemClicked(int position) {
        presenter.onItemClicked(position);
    }

    @Override
    public void setData(List<Song> data) {
        adapter.setData(data);
    }
}
