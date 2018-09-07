package com.weavedin.mplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by avasar on 06,September,2018
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String SEARCH_LIST_TABLE = "search_entries";

    // Table columns
    public static final String KEY_VALUE = "value";

    // Database Information
    static final String DB_NAME = "MPLAYER_SEARCH.DB";

    // database version
    static final int DB_VERSION = 1;


    // Creating table query
    private static final String CREATE_TABLE = "create table " + SEARCH_LIST_TABLE + "(" + KEY_VALUE + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_LIST_TABLE);
        onCreate(db);
    }

}
