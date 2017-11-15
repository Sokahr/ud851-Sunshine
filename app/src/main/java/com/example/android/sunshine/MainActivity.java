/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // DONE (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.


        // DONE (3) Delete the for loop that populates the TextView with dummy data

        // DONE (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // DONE (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it
    // loadWeatherData
    protected void loadWeatherData() {
        String preferredLocation = SunshinePreferences.getPreferredWeatherLocation(this);
        new RequestWeatherTask().execute(preferredLocation);
    }
    // Done (5) Create a class that extends AsyncTask to perform network requests
    // DONE (6) Override the doInBackground method to perform your network requests
    // DONE (7) Override the onPostExecute method to display the results of the network request

    public class RequestWeatherTask extends AsyncTask<String, Void, String[]> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String[] doInBackground(String... params) {
            if(params.length == 0)
                return null;

            String location = params[0];
            URL url = NetworkUtils.buildUrl(location);

            String[] weatherReport=null;
            try {
                String jsonWeatherReport = NetworkUtils.getResponseFromHttpUrl(url);
                weatherReport = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,
                        jsonWeatherReport);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weatherReport;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param weatherData The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String[] weatherData) {
            if(weatherData != null) {
                for (String weatherString:weatherData
                     ) {
                    mWeatherTextView.append(weatherString+"\n\n\n");
                }
            }                    
        }
    }
}