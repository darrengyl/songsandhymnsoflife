package com.church.psalm.model;

import android.provider.BaseColumns;

/**
 * Created by Darren Gu on 8/20/2015.
 */
public final class AllSongsContract {
    public AllSongsContract(){}
    public static abstract class AllSongsEntry implements BaseColumns{
        public static final String ID = "_id";
        public static final String TABLE_NAME = "AllSongs";
        public static final String COLUMN_NAME_TRACKNUMBER = "TrackNumber";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_FREQUENCY = "Frequency";
        public static final String COLUMN_NAME_DOWNLOADED = "Downloaded";
        public static final String COLUMN_NAME_LYRICS = "Lyrics";
        public static final String COLUMN_NAME_FAVORITE = "Favorite";




    }

}
