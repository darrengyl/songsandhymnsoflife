package com.church.psalm;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Darren Gu on 10/8/2015.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static ImageLoader imageLoader;

    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
/*                new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> cache = new LruCache<>((int) Runtime.getRuntime()
                    .maxMemory() / 1024 / 8);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        }*/

    }

    public static VolleySingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleySingleton(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }


}
