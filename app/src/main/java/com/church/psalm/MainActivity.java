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

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager mPager;
    SlidingTabLayout mTabs;
    DBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(this);
        //long id1 = dbAdapter.insertSongData(3, "test1", 2, 0, "123", 1);
        //long id2 = dbAdapter.insertSongData(4, "test2", 5, 0, "324", 0);
        //dbAdapter.getAllSongs();


        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mPager = (ViewPager)findViewById(R.id.pager);
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);

        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabs.setViewPager(mPager);
        //mTabs.setDistributeEvenly(true);
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

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs=getResources().getStringArray(R.array.tabs);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    NumbersFragment numbersFragment = NumbersFragment.getInstance(position);
                    return numbersFragment;
                default:
                    ListsFragment listsFragment = new ListsFragment();
                    return listsFragment;
            }




        }

        @Override
        public CharSequence getPageTitle(int position){
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
