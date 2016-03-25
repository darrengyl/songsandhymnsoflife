package com.church.psalm;

import android.app.Application;

import com.church.psalm.di.DaggerComponent;
import com.church.psalm.di.DaggerDaggerComponent;
import com.church.psalm.di.DaggerModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by darrengu on 3/18/16.
 */
public class songsandhymnsoflife extends Application{
    private DaggerComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerDaggerComponent.builder()
                .daggerModule(new DaggerModule(this))
                .build();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .name("database.realm").build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public DaggerComponent getComponent() {
        return component;
    }
}
