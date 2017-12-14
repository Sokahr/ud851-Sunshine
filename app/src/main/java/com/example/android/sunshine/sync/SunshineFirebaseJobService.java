package com.example.android.sunshine.sync;/*
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
// DONE (2) Make sure you've imported the jobdispatcher.JobService, not job.JobService

import android.os.AsyncTask;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

// DONE (3) Add a class called SunshineFirebaseJobService that extends jobdispatcher.JobService
public class SunshineFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    /**
     * The entry point to your Job. Implementations should offload work to another thread of execution
     * as soon as possible because this runs on the main thread. If work was offloaded, call {@link
     * JobService#jobFinished(JobParameters, boolean)} to notify the scheduling service that the work
     * is completed.
     * <p>
     * <p>If a job with the same service and tag was rescheduled during execution {@link
     * JobService#onStopJob(JobParameters)} will be called and the wakelock will be released. Please
     * make sure that all reschedule requests happen at the end of the job.
     *
     * @param job
     * @return {@code true} if there is more work remaining in the worker thread, {@code false} if the
     * job was completed.
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param voids The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected Void doInBackground(Void... voids) {
                SunshineSyncTask.syncWeather(getApplicationContext());
                return null;
            }

            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             * <p>
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param aVoid The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        }.execute();
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job, most
     * likely because the runtime constraints associated with the job are no longer satisfied. The job
     * must stop execution.
     *
     * @param job
     * @return true if the job should be retried
     */
    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchWeatherTask != null)
            mFetchWeatherTask.cancel(true);
        return true;
    }
}
//  done (4) Declare an ASyncTask field called mFetchWeatherTask

//  DONE (5) Override onStartJob and within it, spawn off a separate ASyncTask to sync weather data
//              DONE (6) Once the weather data is sync'd, call jobFinished with the appropriate arguments

//  DONE (7) Override onStopJob, cancel the ASyncTask if it's not null and return true
