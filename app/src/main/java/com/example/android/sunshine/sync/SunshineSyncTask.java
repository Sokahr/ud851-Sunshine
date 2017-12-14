package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

//  Done (1) Create a class called SunshineSyncTask
//  Done (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      DONE (3) Within syncWeather, fetch new weather data
//      DONE (4) If we have valid results, delete the old data and insert the new
public class SunshineSyncTask {
    synchronized public static void syncWeather(Context context) {

        try {
            URL wheatherUrl = NetworkUtils.getUrl(context);
            String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(wheatherUrl);
            ContentValues[] contentValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, responseFromHttpUrl);

            if (contentValues != null && contentValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,
                        null, null);
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}