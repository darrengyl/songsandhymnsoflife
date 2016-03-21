package com.church.psalm;

import android.app.Application;

import com.church.psalm.di.DaggerComponent;
import com.church.psalm.di.DaggerDaggerComponent;
import com.church.psalm.di.DaggerModule;

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
    }

    public DaggerComponent getComponent() {
        return component;
    }
}
