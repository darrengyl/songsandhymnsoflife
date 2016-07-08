package com.church.psalm;

import android.app.Application;

import com.church.psalm.di.DaggerComponent;
import com.church.psalm.di.DaggerDaggerComponent;
import com.church.psalm.di.DaggerModule;
import com.church.psalm.model.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by darrengu on 3/18/16.
 */
public class Songsandhymnsoflife extends Application{
    private DaggerComponent component;
    private static Songsandhymnsoflife appInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        component = DaggerDaggerComponent.builder()
                .daggerModule(new DaggerModule(this))
                .build();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .name(Constants.REALM_FILE).build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public DaggerComponent getComponent() {
        return component;
    }

    public static Songsandhymnsoflife getApplicationInstance() {
        return appInstance;
    }
}
