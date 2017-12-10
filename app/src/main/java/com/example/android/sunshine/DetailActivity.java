/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//      DONE (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    //  DONE (18) Create a String array containing the names of the desired data columns from our ContentProvider
    private static final String[] PROJECTION = {WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES};
    //  DONE (19) Create constant int values representing each column name's position above
    private static final int INDEX_WEATHER_DATE = 0;
    private static final int INDEX_WEATHER_ID = 1;
    private static final int INDEX_MAX_TEMP = 2;
    private static final int INDEX_MIN_TEMP = 3;
    private static final int INDEX_HUMIDITY = 4;
    private static final int INDEX_PRESSURE = 5;
    private static final int INDEX_WIND_SPEED = 6;
    private static final int INDEX_WIND_DIRECTION = 7;
    //  DONE (20) Create a constant int to identify our loader used in DetailActivity
    private static final int ID_FORECAST_LOADER_DETAIL = 613;
    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  DONE (15) Declare a private Uri field called mUri

    private Uri mUri;

//  DONE (10) Remove the mWeatherDisplay TextView declaration

    //  DONE (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mDateTextView;
    private TextView mDescriptionTextView;
    private TextView mMaxTempTextView;
    private TextView mMinTempTextView;
    private TextView mHumidityxTextView;
    private TextView mWindTextView;
    private TextView mPressureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      DONE (12) Remove mWeatherDisplay TextView

//      DONE (13) Find each of the TextViews by ID
        mDateTextView = (TextView) findViewById(R.id.tv_weather_date);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_weather_description);
        mHumidityxTextView = (TextView) findViewById(R.id.tv_weather_humidity);
        mMaxTempTextView = (TextView) findViewById(R.id.tv_weather_max_temp);
        mMinTempTextView = (TextView) findViewById(R.id.tv_weather_min_temp);
        mPressureTextView = (TextView) findViewById(R.id.tv_weather_pressure);
        mWindTextView = (TextView) findViewById(R.id.tv_weather_wind);
//      DONE (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mUri = intentThatStartedThisActivity.getData();
            if (mUri == null) {
                throw new NullPointerException("Uri null");
            }
        }
//      DONE (16) Use getData to get a reference to the URI passed with this Activity's Intent
//      DONE (17) Throw a NullPointerException if that URI is null
//      DONE (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER_DETAIL, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  DONE (22) Override onCreateLoader
//          DONE(23) If the loader requested is our detail loader, return the appropriate CursorLoader

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_FORECAST_LOADER_DETAIL:
                return new CursorLoader(this,
                        mUri,
                        PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented " + id);
        }
    }

//  done (24) Override onLoadFinished

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

//      DONE (25) Check before doing anything that the Cursor has valid data
        boolean hasValidDate = false;
        if (data != null && data.moveToFirst()) {
            hasValidDate = true;
        }
        if (!hasValidDate) {
            return;
        }

//      DONE (26) Display a readable data string
        long date = data.getLong(INDEX_WEATHER_DATE);
        if (SunshineDateUtils.isDateNormalized(date)) {
            date = SunshineDateUtils.normalizeDate(date);
        }
        String dateText = SunshineDateUtils.getFriendlyDateString(this, date, false);
        mDateTextView.setText(dateText);
//      DONE (27) Display the weather description (using SunshineWeatherUtils)
        String description = SunshineWeatherUtils.getStringForWeatherCondition(this,
                data.getInt(INDEX_WEATHER_ID));
        mDescriptionTextView.setText(description);
//      DONE (28) Display the high temperature
        String highString = SunshineWeatherUtils.formatTemperature(this, data.getFloat(INDEX_MAX_TEMP));
        mMaxTempTextView.setText(highString);
//      DONE (29) Display the low temperature
        String lowString = SunshineWeatherUtils.formatTemperature(this, data.getFloat(INDEX_MIN_TEMP));
        mMinTempTextView.setText(lowString);
//      DONE (30) Display the humidity
        mHumidityxTextView.setText(getString(R.string.format_humidity, data.getFloat(INDEX_HUMIDITY)));
//      DONE (31) Display the wind speed and direction
        mWindTextView.setText(SunshineWeatherUtils.getFormattedWind(this, data.getFloat(INDEX_WIND_SPEED), data
                .getFloat(INDEX_WIND_DIRECTION)));
//      DONE (32) Display the pressure
        mPressureTextView.setText(getString(R.string.format_pressure, data.getFloat(INDEX_PRESSURE)));
//      DONE (33) Store a forecast summary in mForecastSummary
        mForecastSummary = String.format("%s - %s - %s/%s", dateText, description, highString, lowString);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


//  DONE (34) Override onLoaderReset, but don't do anything in it yet

}