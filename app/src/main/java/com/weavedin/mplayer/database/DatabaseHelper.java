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
    public static final String FAVORITE_TABLE = "favorite";

    // Table columns
    public static final String KEY_VALUE = "value";
    public static final String KEY_ID = "_id";
    public static final String KEY_TRACK_ID = "t_id";
    public static final String KEY_TRACK_TITLE = "t_title";
    public static final String KEY_ARTIST_TITLE = "a_title";
    public static final String KEY_COLLECTION_TITLE = "c_title";
    public static final String KEY_IMAGE_URL = "i_url";
    public static final String KEY_PREVIEW_URL = "p_url";

    // Database Information
    static final String DB_NAME = "MPLAYER_SEARCH.DB";

    // database version
    static final int DB_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + SEARCH_LIST_TABLE + "(" + KEY_VALUE + " TEXT NOT NULL UNIQUE);");
        db.execSQL(" CREATE TABLE " +  DatabaseHelper.FAVORITE_TABLE + " (" +
                DatabaseHelper.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseHelper.KEY_TRACK_ID + " INTEGER, " +
                DatabaseHelper.KEY_TRACK_TITLE + " TEXT NOT NULL, " +
                DatabaseHelper.KEY_ARTIST_TITLE + " TEXT NOT NULL, " +
                DatabaseHelper.KEY_COLLECTION_TITLE + " TEXT NOT NULL, " +
                DatabaseHelper.KEY_IMAGE_URL + " TEXT NOT NULL, " +
                DatabaseHelper.KEY_PREVIEW_URL + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        onCreate(db);
    }

}
