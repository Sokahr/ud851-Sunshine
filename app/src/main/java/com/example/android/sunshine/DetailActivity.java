package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView mForecast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // DONE (2) Display the weather forecast that was passed from MainActivity
        mForecast = (TextView) findViewById(R.id.tv_weather_detail);
        Intent displayIntent = getIntent();

        if (displayIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mForecast.setText(displayIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}