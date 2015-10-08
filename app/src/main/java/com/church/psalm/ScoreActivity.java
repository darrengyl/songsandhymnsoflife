package com.church.psalm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;


/**
 * Created by yuanlong.gu on 10/7/2015.
 */
public class ScoreActivity extends AppCompatActivity{
    Toolbar toolbar;
    SubsamplingScaleImageView imageView;
    Bitmap bitmapName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        toolbar = (Toolbar)findViewById(R.id.app_bar_score);
        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        Intent intent = getIntent();
        int trackNumber = intent.getIntExtra("trackNumber", 1);

        setSupportActionBar(toolbar);





    }

    public String getScoreLink(int track){
        String scoreLink = "http://shengmingshige.net/blog/wp-content/gallery/c-png/cu-"
                + new DecimalFormat("0000").format(track) + ".png";
        return scoreLink;
    }

    public String getMusicLink(int track){
        String musicLink = "http://shengmingshige.net/hymnal/mp3/"
                + new DecimalFormat("0000").format(track) + ".mp3";
        return musicLink;
    }

    public boolean foundScoreInStorage(int track){
        File score = new File(this.getFilesDir() + "/Score" + track + ".png");
        return (score.exists());
    }

    public boolean foundMusicInStorage(int track){
        File music = new File(this.getFilesDir() + "/Music" + track + ".mp3");
        return (music.exists());
    }

    public void saveScore(Bitmap bitmap, int track) throws IOException {
        File score = null;
        FileOutputStream save = null;
        try {
            score = new File(this.getFilesDir(), track + ".png");
            save = new FileOutputStream(score);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, save);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (save !=null){
                save.close();
            }
        }

    }

    public Bitmap loadScore(int track) throws IOException {
        Bitmap bitmap = null;
        FileInputStream load = null;
        try {
            load = openFileInput(this.getFilesDir() + "/Score" + track + ".png");
            bitmap = BitmapFactory.decodeStream(load);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (load != null){
                load.close();
            }

        }
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_score_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.play:
                return true;
            case R.id.previous:

                return true;
            case R.id.next:
                return true;
            case R.id.loop:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*class LoadScore extends AsyncTask<String, Integer, Void>{

        @Override
        protected void doInBackground(String... params) {

        }
    }*/



}
