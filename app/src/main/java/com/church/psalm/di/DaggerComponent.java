package com.church.psalm.di;

import com.church.psalm.view.activity.CategoryActivity;
import com.church.psalm.view.activity.MainActivity;
import com.church.psalm.view.activity.ScoreActivity;
import com.church.psalm.view.activity.SearchActivity;
import com.church.psalm.view.fragment.ListsFragment;
import com.church.psalm.view.fragment.NumbersFragment;
import com.church.psalm.view.fragment.TabbedFragment;
import com.church.psalm.view.fragment.categories.CatBaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by darrengu on 3/18/16.
 */
@Singleton
@Component(modules = DaggerModule.class)
public interface DaggerComponent {
    void inject(MainActivity mainActivity);
    void inject(SearchActivity searchActivity);
    void inject(ListsFragment listsFrag);
    void inject(NumbersFragment numFrag);
    void inject(ScoreActivity scoreActivity);
    void inject(CatBaseFragment baseFragment);
    void inject(CategoryActivity categoryActivity);
    void inject(TabbedFragment tabbedFragment);
}
