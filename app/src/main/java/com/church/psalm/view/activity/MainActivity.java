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
import com.church.psalm.presenter.activity.PresenterMainActivity;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.view.adapter.ViewPagerAdapter;
import com.church.psalm.view.fragment.ListsFragment;
import com.church.psalm.view.fragment.NumbersFragment;
import com.church.psalm.view.view.ViewMainActivity;


import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.psdev.licensesdialog.LicensesDialog;

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
        ((Songsandhymnsoflife) getApplication()).getComponent().inject(this);
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
                if (_sort != null) {
                    if (tab.getPosition() == 0) {
                        _sort.setVisible(true);
                    } else {
                        _sort.setVisible(false);
                    }
                }
            }
        });
    }

    @OnClick(R.id.tool_bar)
    public void onClickToolbar() {
        if (listsFragment != null) {
            listsFragment.scrollToFirst();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        _sort = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by:
                if (viewPager.getCurrentItem() == 0) {
                    listsFragment.onClickSort();
                }
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.acknowledgements:
                showLicenseDialog();
                break;
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

    private void showLicenseDialog() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

    public void incrementFreq(int position) {
        listsFragment.incrementFreq(position);
    }
}
