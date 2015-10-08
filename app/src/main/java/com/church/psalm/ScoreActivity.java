package com.church.psalm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by yuanlong.gu on 10/7/2015.
 */
public class ScoreActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        Intent intent = getIntent();
        int trackNumber = intent.getIntExtra("trackNumber", 1);
        String title = intent.getStringExtra("title");


    }

    class LoadScore extends AsyncTask<String, Integer, Void>{

        @Override
        protected void doInBackground(String... params) {

        }
    }



}
