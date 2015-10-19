package com.church.psalm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager mPager;
    PagerSlidingTabStrip mTabs;
    DBAdapter dbAdapter;


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
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //mTabs.setCustomTabView(R.layout.custom_tab_title, R.id.tabText);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        //mTabs.setDistributeEvenly(true);
        //mTabs.set
/*        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });*/
        mTabs.setViewPager(mPager);

        //mPager.setd


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] tabs;
        int icons[] = {R.drawable.ic_dialpad_white_24dp, R.drawable.ic_list_white_24dp};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    NumbersFragment numbersFragment = NumbersFragment.getInstance(position);
                    return numbersFragment;
                case 1:
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
