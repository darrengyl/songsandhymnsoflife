package com.church.psalm.view.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.church.psalm.R;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.presenter.activity.PresenterMainActivity;
import com.church.psalm.view.adapter.ViewPagerAdapter;
import com.church.psalm.view.fragment.ListsFragment;
import com.church.psalm.view.fragment.NumbersFragment;
import com.church.psalm.view.fragment.TabbedFragment;
import com.church.psalm.view.view.ViewMainActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.psdev.licensesdialog.LicensesDialog;

public class MainActivity extends AppCompatActivity implements ViewMainActivity {
    private static final int MY_PERMISSIONS_REQUEST = 1;
    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.container)
    FrameLayout frameLayout;
    @Inject
    PresenterMainActivity presenter;

    private MaterialDialog _dialog;
    private MenuItem _sort;
    private MenuItem _search;
    private MenuItem _category;
    private boolean isTabbed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((Songsandhymnsoflife) getApplication()).getComponent().inject(this);
        setSupportActionBar(toolBar);
        presenter.setViewActivity(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new MaterialDialog.Builder(this)
                        .title(R.string.permission_needed)
                        .content(R.string.permission_content)
                        .positiveText(R.string.ok)
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
            showTabbedFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showTabbedFragment();
            } else {
                showNumberFragment();
                removeMenuItem();
            }
        }
    }

    private void showTabbedFragment() {
        TabbedFragment fragment = new TabbedFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
        presenter.setViewTabbed(fragment);
        if (Build.VERSION.SDK_INT >= 21) {
            toolBar.setElevation(0);
        }
        isTabbed = true;
    }

    private void showNumberFragment() {
        NumbersFragment fragment = new NumbersFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
        if (Build.VERSION.SDK_INT >= 21) {
            toolBar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                    getResources().getDisplayMetrics()));
        }
        isTabbed = false;
    }

    @OnClick(R.id.tool_bar)
    public void onClickToolbar() {
        if (isTabbed) {
            presenter.onToolbarClick();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        _sort = menu.getItem(0);
        _search = menu.getItem(1);
        _category = menu.getItem(2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by:
                if (isTabbed) {
                    presenter.onClickSortBy();
                }
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
                .title(R.string.loading_data)
                .content(R.string.please_wait)
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
    public void onTabSelected(int position) {
        if (_sort != null) {
            if (position == 1) {
                _sort.setVisible(true);
            } else {
                _sort.setVisible(false);
            }
            _search.setVisible(true);
            _category.setVisible(true);
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
