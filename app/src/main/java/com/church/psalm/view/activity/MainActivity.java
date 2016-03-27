package com.church.psalm.view.activity;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((songsandhymnsoflife)getApplication()).getComponent().inject(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Hymns of Life");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        listsFragment = new ListsFragment();
        numbersFragment = new NumbersFragment();
        viewPagerAdapter.addFragment(listsFragment, "LIST");
        viewPagerAdapter.addFragment(numbersFragment, "NUMBER");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupFloatingButton() {
    }



/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }*/


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
