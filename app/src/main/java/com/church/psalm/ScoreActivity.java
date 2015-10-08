package com.church.psalm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


/**
 * Created by yuanlong.gu on 10/7/2015.
 */
public class ScoreActivity extends AppCompatActivity{
    Toolbar toolbar;
    SubsamplingScaleImageView imageView;
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

    /*class LoadScore extends AsyncTask<String, Integer, Void>{

        @Override
        protected void doInBackground(String... params) {

        }
    }*/



}
