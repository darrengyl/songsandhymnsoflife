package com.church.psalm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by yuanlong.gu on 10/13/2015.
 */
public class MusicController extends MediaController {
    private Context mContext;

    public MusicController(Context context) {
        super(context);
        mContext = context;
    }

}
