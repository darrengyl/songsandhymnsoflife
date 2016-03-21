package com.church.psalm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.church.psalm.model.AllSongsContract;
import com.church.psalm.model.Song;

import java.util.ArrayList;
import java.util.List;

import static com.church.psalm.model.AllSongsContract.AllSongsEntry.COLUMN_NAME_DOWNLOADED;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.COLUMN_NAME_FAVORITE;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.COLUMN_NAME_FREQUENCY;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.COLUMN_NAME_LYRICS;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.COLUMN_NAME_TITLE;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.COLUMN_NAME_TRACKNUMBER;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.ID;
import static com.church.psalm.model.AllSongsContract.AllSongsEntry.TABLE_NAME;

/**
 * Created by Darren Gu on 8/20/2015.
 */
public class DbAdapter {
    List<Song> data;
    DBHelper dbHelper;
    Context context;
    SQLiteDatabase db;
    public DbAdapter(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }
    public List<Song> getKeywordSongs(String keyword){
        data = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_TRACKNUMBER
                    , COLUMN_NAME_TITLE, COLUMN_NAME_FREQUENCY, COLUMN_NAME_DOWNLOADED
                    , COLUMN_NAME_LYRICS, COLUMN_NAME_FAVORITE}
                    , COLUMN_NAME_TITLE + " LIKE ?", new String[]{"%" + keyword + "%"}, null, null
                    , COLUMN_NAME_TRACKNUMBER);
            while (cursor.moveToNext()){
                int track = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TRACKNUMBER));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                int freq = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FREQUENCY));
                String dwnld = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOWNLOADED));
                String lyrics = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LYRICS));
                int favourite = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAVORITE));
                //System.out.println("" + track + title + freq + dwnld + lyrics);
                Song song = new Song(track, title, freq, dwnld, lyrics, favourite);
                data.add(song);
            }
        } finally {
            db.close();
        }
        return data;
    }
    public List<Song> getMostFavSongs(){
        data = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_TRACKNUMBER
                            , COLUMN_NAME_TITLE, COLUMN_NAME_FREQUENCY, COLUMN_NAME_DOWNLOADED
                            , COLUMN_NAME_LYRICS, COLUMN_NAME_FAVORITE}
                            , COLUMN_NAME_FAVORITE + "= 1", null, null, null
                            , COLUMN_NAME_FREQUENCY + " DESC");
            while (cursor.moveToNext()){
                int track = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TRACKNUMBER));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                int freq = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FREQUENCY));
                String dwnld = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOWNLOADED));
                String lyrics = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LYRICS));
                int favourite = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAVORITE));
                //System.out.println("" + track + title + freq + dwnld + lyrics);
                Song song = new Song(track, title, freq, dwnld, lyrics, favourite);
                data.add(song);
            }
        } finally {
            db.close();
        }
        return data;
    }

    public List<Song> getMostPlayedSongs(){
        data = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_TRACKNUMBER
                    , COLUMN_NAME_TITLE, COLUMN_NAME_FREQUENCY, COLUMN_NAME_DOWNLOADED
                    , COLUMN_NAME_LYRICS, COLUMN_NAME_FAVORITE}
                    , null, null, null, null
                    , COLUMN_NAME_FREQUENCY + " DESC");
            while (cursor.moveToNext()){
                int track = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TRACKNUMBER));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                int freq = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FREQUENCY));
                String dwnld = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOWNLOADED));
                String lyrics = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LYRICS));
                int favourite = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAVORITE));
                //System.out.println("" + track + title + freq + dwnld + lyrics);
                Song song = new Song(track, title, freq, dwnld, lyrics, favourite);
                data.add(song);
            }
        } finally {
            db.close();
        }
        return data;
    }
    //position is one based
    public boolean getFav(int position){
        boolean favourite = false;
        try {
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_FAVORITE}
                    , COLUMN_NAME_TRACKNUMBER + " = ?", new String[]{String.valueOf(position)}
                    , null, null, null);
            if (cursor.moveToNext() && cursor.getCount() == 1){
                int fav = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAVORITE));
                if (fav != 0){
                    favourite = true;
                }
            }
        } finally {
            db.close();
        }


        return favourite;
    }

    public void flipFav(int position){
        boolean booleanFav = getFav(position);
        booleanFav = !booleanFav;
        int integerFave = booleanFav? 1 : 0;
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_FAVORITE, integerFave);
        try {
            db = dbHelper.getWritableDatabase();
            db.update(TABLE_NAME, args, COLUMN_NAME_TRACKNUMBER + " = ?"
                    , new String[]{String.valueOf(position)});
        } finally {
            db.close();
        }


    }

    public int getFreq(int position){
        int freq = 0;
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_FREQUENCY}
                    , COLUMN_NAME_TRACKNUMBER + " = ?", new String[]{String.valueOf(position)}
                    , null, null, null);
            if (cursor.moveToNext() && cursor.getCount() == 1){
                freq = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FREQUENCY));
            }
        } finally {
            db.close();
        }
        return freq;
    }

    public void incrementFreq(int position){
        int freq = getFreq(position);
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_FREQUENCY, ++freq);
            System.out.println(db.update(TABLE_NAME, contentValues, COLUMN_NAME_TRACKNUMBER + " = ?"
                    , new String[]{String.valueOf(position)}));
        } finally {
            db.close();
        }

    }


    public long insertSongData(int track, String title, int freq, int downloaded, String text, int fav){
        long id = -1L;
        /*if (db != null){
            System.out.println("get db successfully");
        }*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TRACKNUMBER, track);
        contentValues.put(COLUMN_NAME_TITLE, title);
        contentValues.put(COLUMN_NAME_FREQUENCY, freq);
        contentValues.put(COLUMN_NAME_DOWNLOADED, downloaded);
        contentValues.put(COLUMN_NAME_LYRICS, text);
        contentValues.put(COLUMN_NAME_FAVORITE, fav);
        try {
            db = dbHelper.getWritableDatabase();
            id = db.insert(TABLE_NAME, null, contentValues);
        } finally {
            db.close();
        }


        return id;
    }
    public List<Song> getAllSongs(){
        data = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_TRACKNUMBER, COLUMN_NAME_TITLE,
                            COLUMN_NAME_FREQUENCY, COLUMN_NAME_DOWNLOADED, COLUMN_NAME_LYRICS,
                            COLUMN_NAME_FAVORITE},
                    null, null, null, null, COLUMN_NAME_TRACKNUMBER);
            while (cursor.moveToNext()){
                int track = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TRACKNUMBER));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                int freq = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FREQUENCY));
                String dwnld = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DOWNLOADED));
                String lyrics = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LYRICS));
                int favourite = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAVORITE));
                //System.out.println("" + track + title + freq + dwnld + lyrics);
                Song song = new Song(track, title, freq, dwnld, lyrics, favourite);
                data.add(song);
            }
        } finally {
            db.close();
        }
        return data;


    }

    class DBHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "AllSongs.db";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME_TRACKNUMBER + " INTEGER NOT NULL," +
                        COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                        COLUMN_NAME_FREQUENCY + " INTEGER," +
                        COLUMN_NAME_DOWNLOADED + " INTEGER NOT NULL," +
                        COLUMN_NAME_LYRICS + " TEXT, " +
                        COLUMN_NAME_FAVORITE + " INTEGER" +
                        ")";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AllSongsContract.AllSongsEntry.TABLE_NAME;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(SQL_CREATE_ENTRIES);
            } catch (SQLException e){
                e.printStackTrace();
            }

        }


        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
     }


}
