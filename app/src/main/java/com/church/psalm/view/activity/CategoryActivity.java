package com.church.psalm.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.church.psalm.R;
import com.church.psalm.Songsandhymnsoflife;
import com.church.psalm.presenter.activity.PresenterCategoryActivity;
import com.church.psalm.view.adapter.ViewPagerAdapter;
import com.church.psalm.view.fragment.categories.CatJiaohuiFragment;
import com.church.psalm.view.fragment.categories.CatJiduFragment;
import com.church.psalm.view.fragment.categories.CatJingwenFragment;
import com.church.psalm.view.fragment.categories.CatKewangFragment;
import com.church.psalm.view.fragment.categories.CatShenglingFragment;
import com.church.psalm.view.fragment.categories.CatShengmingFragment;
import com.church.psalm.view.fragment.categories.CatShifengFragment;
import com.church.psalm.view.fragment.categories.CatSongzaiFragment;
import com.church.psalm.view.fragment.categories.CatYesuFragment;
import com.church.psalm.view.fragment.categories.CatZaijiFragment;
import com.church.psalm.view.fragment.categories.CatZanmeiFragment;
import com.church.psalm.view.view.ViewCategoryActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity implements ViewCategoryActivity{

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Inject
    PresenterCategoryActivity presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        ((Songsandhymnsoflife) getApplication()).getComponent().inject(this);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("Tab selected", String.valueOf(tab.getPosition()));
                presenter.tabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CatSongzaiFragment(), "颂赞的歌声");
        adapter.addFragment(new CatShenglingFragment(), "圣灵的丰满");
        adapter.addFragment(new CatJiaohuiFragment(), "教会");
        adapter.addFragment(new CatShengmingFragment(), "生命旅程的经历");
        adapter.addFragment(new CatJiduFragment(), "基督徒生活的实行");
        adapter.addFragment(new CatYesuFragment(), "耶稣基督的福音");
        adapter.addFragment(new CatShifengFragment(), "事奉的生活");
        adapter.addFragment(new CatZaijiFragment(), "在基督里凯旋");
        adapter.addFragment(new CatKewangFragment(), "渴望主的再来");
        adapter.addFragment(new CatZanmeiFragment(), "赞美短歌");
        adapter.addFragment(new CatJingwenFragment(), "经文诗歌");

        viewPager.setAdapter(adapter);
    }

    @Override
    public int getCurrentTab() {
        return tabLayout.getSelectedTabPosition();
    }
}
