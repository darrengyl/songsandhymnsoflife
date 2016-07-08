package com.church.psalm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.church.psalm.R;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.presenter.activity.PresenterMainActivity;
import com.church.psalm.view.adapter.ViewPagerAdapter;
import com.church.psalm.view.view.ViewTabbedFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by darrengu on 7/1/16.
 */
public class TabbedFragment extends Fragment implements ViewTabbedFragment{
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager viewPager;
    private ListsFragment listsFragment;
    @Inject
    PresenterMainActivity presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);
        ButterKnife.bind(this, view);
        ((Songsandhymnsoflife) getActivity().getApplication()).getComponent().inject(this);
        ViewPagerAdapter _viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        NumbersFragment numbersFragment = new NumbersFragment();
        _viewPagerAdapter.addFragment(numbersFragment, getString(R.string.numbers));
        listsFragment = new ListsFragment();
        _viewPagerAdapter.addFragment(listsFragment, getString(R.string.list));
        viewPager.setAdapter(_viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setOnTabSelectedListener();
        return view;
    }

    private void setOnTabSelectedListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                presenter.onTabSelected(tab.getPosition());
            }
        });
    }

    @Override
    public void onToolbarClick() {
        if (listsFragment != null) {
            listsFragment.scrollToFirst();
        }
    }

    @Override
    public void onClickSortBy() {
        listsFragment.showSortByDialog();
    }
}
