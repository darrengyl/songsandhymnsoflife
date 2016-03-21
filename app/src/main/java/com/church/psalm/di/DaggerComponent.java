package com.church.psalm.di;

import com.church.psalm.view.activity.MainActivity;
import com.church.psalm.view.fragment.ListsFragment;
import com.church.psalm.view.fragment.NumbersFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by darrengu on 3/18/16.
 */
@Singleton
@Component(modules = DaggerModule.class)
public interface DaggerComponent {
    void inject(MainActivity activity);
    void inject(ListsFragment listsFrag);
    void inject(NumbersFragment numFrag);
}
