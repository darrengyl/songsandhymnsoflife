package com.church.psalm.di;

import android.app.Application;

import com.church.psalm.presenter.activity.PresenterScoreActivity;
import com.church.psalm.presenter.activity.PresenterSearchActivity;
import com.church.psalm.presenter.fragment.PresenterListsFragment;
import com.church.psalm.presenter.activity.PresenterMainActivity;
import com.church.psalm.presenter.fragment.PresenterNumbersFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by darrengu on 3/18/16.
 */
@Module
public class DaggerModule {
    private static Application _app;
    public DaggerModule(Application app) {
        _app = app;
    }

    @Provides
    @Singleton
    PresenterListsFragment providesPresenterListsFragment() {
        return new PresenterListsFragment(_app);
    }

    @Provides
    @Singleton
    PresenterMainActivity providesPresenterMainActivity() {
        return new PresenterMainActivity(_app);
    }

    @Provides
    @Singleton
    PresenterNumbersFragment providesPresenterNumbersFragment() {
        return new PresenterNumbersFragment();
    }

    @Provides
    @Singleton
    PresenterSearchActivity providesPresenterSearchActivity() {
        return new PresenterSearchActivity();
    }

    @Provides
    @Singleton
    PresenterScoreActivity providesPresenterScoreActivity() {
        return new PresenterScoreActivity();
    }
}
