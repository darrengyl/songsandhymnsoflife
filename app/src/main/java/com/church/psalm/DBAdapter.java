package com.church.psalm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static com.church.psalm.AllSongsContract.AllSongsEntry.COLUMN_NAME_DOWNLOADED;
import static com.church.psalm.AllSongsContract.AllSongsEntry.COLUMN_NAME_FAVORITE;
import static com.church.psalm.AllSongsContract.AllSongsEntry.COLUMN_NAME_FREQUENCY;
import static com.church.psalm.AllSongsContract.AllSongsEntry.COLUMN_NAME_LYRICS;
import static com.church.psalm.AllSongsContract.AllSongsEntry.COLUMN_NAME_TITLE;
import static com.church.psalm.AllSongsContract.AllSongsEntry.COLUMN_NAME_TRACKNUMBER;
import static com.church.psalm.AllSongsContract.AllSongsEntry.ID;
import static com.church.psalm.AllSongsContract.AllSongsEntry.TABLE_NAME;

/**
 * Created by Darren Gu on 8/20/2015.
 */
public class DBAdapter {
    ArrayList<Song> data;
    DBHelper dbHelper;
    Context context;
    SQLiteDatabase db;
    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public boolean getFav(int position){
        boolean favourite = false;
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_FAVORITE}
                , COLUMN_NAME_TRACKNUMBER + " = ?", new String[]{String.valueOf(position+1)}
                , null, null, null);
        if (cursor.moveToNext() && cursor.getCount() == 1){
            int fav = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FAVORITE));
            if (fav != 0){
                favourite = true;
            }
        }
        db.close();
        return favourite;
    }

    public void flipFav(int position){
        boolean booleanFav = getFav(position);
        booleanFav = !booleanFav;
        int integerFave = booleanFav? 1 : 0;
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_FAVORITE, integerFave);
        db = dbHelper.getWritableDatabase();
        db.update(TABLE_NAME, args, COLUMN_NAME_TRACKNUMBER + " = ?"
                , new String[]{String.valueOf(position+1)});
        db.close();
    }

    public int getFreq(int position){
        int freq = 0;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_FREQUENCY}
                , COLUMN_NAME_TRACKNUMBER + " = ?", new String[]{String.valueOf(position+1)}
                , null, null, null);
        if (cursor.moveToNext() && cursor.getCount() == 1){
            freq = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_FREQUENCY));
        }
        db.close();
        return freq;
    }

    public void incrementFreq(int position){
        int freq = getFreq(position);
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_FREQUENCY, freq++);
        db.update(TABLE_NAME, contentValues, COLUMN_NAME_TRACKNUMBER + " = ?"
                , new String[]{String.valueOf(position)});
        db.close();
    }


    public long insertSongData(int track, String title, int freq, int downloaded, String text, int fav){
        db = dbHelper.getWritableDatabase();
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
        long id = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }
    public ArrayList<Song> getAllSongs(){
        data = new ArrayList<Song>();
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
        db.close();

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
