package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

// DONE (5) Create a new class called SunshineSyncIntentService that extends IntentService
//  DONE (6) Create a constructor that calls super and passes the name of this class
//  DONE (7) Override onHandleIntent, and within it, call SunshineSyncTask.syncWeather
public class SunshineSyncIntentService extends IntentService {

    public SunshineSyncIntentService() {
        super(SunshineSyncIntentService.class.getName());
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     *               This may be null if the service is being restarted after
     *               its process has gone away; see
     *               {@link Service#onStartCommand}
     *               for details.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}
