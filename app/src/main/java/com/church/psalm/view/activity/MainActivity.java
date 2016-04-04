package com.church.psalm.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

import com.church.psalm.R;
import com.church.psalm.model.AllSongsContract;
import com.church.psalm.presenter.activity.PresenterMainActivity;
import com.church.psalm.songsandhymnsoflife;
import com.church.psalm.view.adapter.ViewPagerAdapter;
import com.church.psalm.view.fragment.ListsFragment;
import com.church.psalm.view.fragment.NumbersFragment;
import com.church.psalm.view.view.ViewMainActivity;


import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewMainActivity {
    private static final String TAG_SORT_NUMBER = "sortNum";
    private static final String TAG_SORT_FREQ = "sortFreq";
    private static final String TAG_SORT_FAV = "sortFav";
    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Inject
    PresenterMainActivity presenterMainActivity;
    private MaterialDialog _dialog;
    private ListsFragment listsFragment;
    private NumbersFragment numbersFragment;
    private MenuItem _sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((songsandhymnsoflife) getApplication()).getComponent().inject(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Hymns of Life");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        listsFragment = new ListsFragment();
        numbersFragment = new NumbersFragment();
        viewPagerAdapter.addFragment(listsFragment, "LIST");
        viewPagerAdapter.addFragment(numbersFragment, "NUMBER");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (_sort == null) {
                    return;
                }
                if (tab.getPosition() == 0) {
                    _sort.setVisible(true);
                } else {
                    _sort.setVisible(false);
                }
            }
        });
    }

    public void setupFloatingButton() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        _sort = menu.findItem(R.id.sort_by);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by:
                if (viewPager.getCurrentItem() == 0) {
                    listsFragment.onClickSort();
                }
                return true;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showProgressDialog() {
        _dialog = new MaterialDialog.Builder(this)
                .title("Loading data")
                .content("Please wait")
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissProgressDialog() {
        if (_dialog != null && _dialog.isShowing()) {
            _dialog.dismiss();
        }
    }

    @Override
    public void refreshListData() {

    }

}
