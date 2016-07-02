package com.church.psalm.view.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.church.psalm.R;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.view.adapter.ViewPagerAdapter;
import com.church.psalm.view.fragment.ListsFragment;
import com.church.psalm.view.fragment.NumbersFragment;
import com.church.psalm.view.view.ViewMainActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.psdev.licensesdialog.LicensesDialog;

public class MainActivity extends AppCompatActivity implements ViewMainActivity {
    private static final int MY_PERMISSIONS_REQUEST = 1;
    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager viewPager;

    private MaterialDialog _dialog;
    private ListsFragment listsFragment;
    private MenuItem _sort;
    private MenuItem _search;
    private MenuItem _category;
    private ViewPagerAdapter _viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((Songsandhymnsoflife) getApplication()).getComponent().inject(this);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hymns of Life");
        }
        _viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        NumbersFragment numbersFragment = new NumbersFragment();
        _viewPagerAdapter.addFragment(numbersFragment, "NUMBER");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new MaterialDialog.Builder(this)
                        .title("Permission Needed")
                        .content("Without the permission to write data to your storage, none of song info can be stored. " +
                                "In that case not all functions will be available")
                        .positiveText("OK")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
        } else {
            listsFragment = new ListsFragment();
            _viewPagerAdapter.addFragment(listsFragment, "LIST");
            viewPager.setAdapter(_viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            setOnTabSelectedListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listsFragment = new ListsFragment();
                _viewPagerAdapter.addFragment(listsFragment, "LIST");
                setOnTabSelectedListener();
            } else {
                removeMenuItem();
            }
            viewPager.setAdapter(_viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setOnTabSelectedListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (_sort != null) {
                    if (tab.getPosition() == 1) {
                        _sort.setVisible(true);
                    } else {
                        _sort.setVisible(false);
                    }
                    _search.setVisible(true);
                    _category.setVisible(true);
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
        _search = menu.getItem(1);
        _category = menu.getItem(2);
        if (tabLayout != null && tabLayout.getSelectedTabPosition() == 1) {
            _sort.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by:
                listsFragment.onClickSort();
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.category:
                startActivity(new Intent(this, CategoryActivity.class));
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

    private void showLicenseDialog() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

    private void removeMenuItem() {
        _sort.setVisible(false);
        _search.setVisible(false);
        _category.setVisible(false);
    }
}
