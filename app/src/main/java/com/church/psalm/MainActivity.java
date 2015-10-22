package com.church.psalm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ViewPager mPager;
    PagerSlidingTabStrip mTabs;
    DBAdapter dbAdapter;
    MyPagerAdapter mAdapter;
    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;
    SubActionButton button1;
    SubActionButton button2;
    SubActionButton button3;


    private static final String TAG_SORT_NUMBER = "sortNum";
    private static final String TAG_SORT_FREQ = "sortFreq";
    private static final String TAG_SORT_FAV = "sortFav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = new DBAdapter(this);
        //long id1 = dbAdapter.insertSongData(3, "test1", 2, 0, "123", 1);
        //long id2 = dbAdapter.insertSongData(4, "test2", 5, 0, "324", 0);
        //dbAdapter.getAllSongs();


        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mPager = (ViewPager) findViewById(R.id.pager);
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    actionButton.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                } else {
                    actionButton.setVisibility(View.GONE);
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                }
            }
        };
        mPager.addOnPageChangeListener(pageChangeListener);
        setupFloatingButton();
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //mTabs.setCustomTabView(R.layout.custom_tab_title, R.id.tabText);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        //mTabs.setDistributeEvenly(true);
        //mTabs.set
/*        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });*/
        mTabs.setViewPager(mPager);


    }

    public void setupFloatingButton() {
        ImageView floatButton = new ImageView(this);
        floatButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_sort_white_24dp));


        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(floatButton)
                .setBackgroundDrawable(R.drawable.circle)
                .build()
        ;
        actionButton.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5
                , getResources().getDisplayMetrics()));
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView sortByFav = new ImageView(this);
        ImageView sortByFreq = new ImageView(this);
        ImageView sortByTrack = new ImageView((this));
        sortByFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_grey600_24dp));
        sortByFreq.setImageDrawable(getResources().getDrawable(R.drawable
                .ic_playlist_play_grey600_24dp));
        sortByTrack.setImageDrawable(getResources().getDrawable(R.drawable
                .ic_sort_numeric_grey600_24dp));
        button1 = itemBuilder.setContentView(sortByFav).build();
        button2 = itemBuilder.setContentView(sortByFreq).build();
        button3 = itemBuilder.setContentView(sortByTrack).build();
        button1.setTag(TAG_SORT_FAV);
        button2.setTag(TAG_SORT_FREQ);
        button3.setTag(TAG_SORT_NUMBER);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int tab = mPager.getCurrentItem();
        if (tab == 0) {
            Fragment fragment = (Fragment) mAdapter.instantiateItem(mPager, tab);
            if (v.getTag().equals(TAG_SORT_FAV)) {
                ((sortListener) fragment).onSortByFav();
            }
            if (v.getTag().equals(TAG_SORT_FREQ)) {
                ((sortListener) fragment).onSortByFreq();
            }
            if (v.getTag().equals(TAG_SORT_NUMBER)) {
                ((sortListener) fragment).onSortByTrack();
            }
        }
        actionMenu.close(true);


    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    NumbersFragment numbersFragment = NumbersFragment.getInstance(position);
                    return numbersFragment;
                case 0:
                    ListsFragment listsFragment = new ListsFragment();
                    return listsFragment;
                default:
                    ListsFragment listsFragmentDefault = new ListsFragment();
                    return listsFragmentDefault;
            }


        }

        @Override
        public CharSequence getPageTitle(int position) {
/*            Drawable drawable = getDrawable(icons[position]);
            drawable.setBounds(0 ,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" " + tabs[position]);
            spannableString.setSpan(imageSpan, 0, 1
                    , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;*/
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }


    }
}
