package com.church.psalm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by darrengu on 5/1/16.
 */
public class Util {
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) Songsandhymnsoflife
                .getApplicationInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    public static boolean safeIntent(Intent intent) {
        PackageManager packageManager = Songsandhymnsoflife
                .getApplicationInstance()
                .getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }


}
