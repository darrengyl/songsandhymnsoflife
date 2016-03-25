package com.church.psalm.di;

import android.app.Application;

import com.church.psalm.presenter.fragment.PresenterListsFragment;
import com.church.psalm.presenter.activity.PresenterMainActivity;
import com.church.psalm.presenter.fragment.PresenterNumbersFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by darrengu on 3/18/16.
 */
@Module
public class DaggerModule {
    private Application _app;
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
        return new PresenterNumbersFragment(_app);
    }

/*    @Provides
    @Singleton
    Realm providesRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(_app).build();

    }*/
}
