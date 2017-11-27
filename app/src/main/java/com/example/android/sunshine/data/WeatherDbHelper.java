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
package com.example.android.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for weather data.
 */
// DONE (11) Extend SQLiteOpenHelper from WeatherDbHelper
public class WeatherDbHelper extends SQLiteOpenHelper {

    //  done (12) Create a public static final String called DATABASE_NAME with value "weather.db"
    public static final String DATABASE_NAME = "weather.db";
    //  done (13) Create a private static final int called DATABASE_VERSION and set it to 1
    public static final int DATABASE_VERSION = 1;
//  DONE (14) Create a constructor that accepts a context and call through to the superclass constructor

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//  DONE (15) Override onCreate and create the weather table from within it

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTableSQL = "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" +
                WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WeatherContract.WeatherEntry.COLUMN_DATE + " TEXT NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_DEGREES + " DOUBLE NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " FLOAT NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + " DOUBLE NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP + " DOUBLE NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_PRESSURE + " FLOAT NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INT NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " FLOAT NOT NULL" +
                "); ";
        db.execSQL(createTableSQL);
    }

//  DONE (16) Override onUpgrade, but don't do anything within it yet

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}