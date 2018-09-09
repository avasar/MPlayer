package com.weavedin.mplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.weavedin.mplayer.models.Favorites;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by avasar on 06,September,2018
 */
public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertSearchTerm(String value) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.KEY_VALUE, value);
        database.insert(DatabaseHelper.SEARCH_LIST_TABLE, null, contentValue);
    }

    public void insertFavoriteTrack(Integer trackId, String trackTitle, String artistName, String collectionName, String imageUrl, String previewUrl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_TRACK_ID, trackId);
        contentValues.put(DatabaseHelper.KEY_TRACK_TITLE, trackTitle);
        contentValues.put(DatabaseHelper.KEY_ARTIST_TITLE, artistName);
        contentValues.put(DatabaseHelper.KEY_COLLECTION_TITLE, collectionName);
        contentValues.put(DatabaseHelper.KEY_IMAGE_URL, imageUrl);
        contentValues.put(DatabaseHelper.KEY_PREVIEW_URL, previewUrl);
        database.insert(DatabaseHelper.FAVORITE_TABLE, null, contentValues);
    }

    public String[] getSearchTerms(String value) {
        try {
            String terms[] = null;
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery = "SELECT DISTINCT * FROM " + DatabaseHelper.SEARCH_LIST_TABLE +
                    " WHERE " + DatabaseHelper.KEY_VALUE + "  LIKE  '%" + value + "%' ";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    terms = new String[cursor.getCount()];
                    int i = 0;
                    do {
                        terms[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            return terms;
        } catch (Exception e) {
            return null;
        }

    }

    public boolean isFavorite(int trackId) {
        boolean isFavorite = false;
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.FAVORITE_TABLE
                + " where " + DatabaseHelper.KEY_TRACK_ID + " = " + trackId, new String[]{});
        if (cursor != null && cursor.getCount() > 0) {
            isFavorite = true;
        } else {
            isFavorite = false;

        }
        cursor.close();
        return isFavorite;

    }


    public List<Favorites> geFavorites() {
        List<Favorites> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.FAVORITE_TABLE  +" ORDER BY "+DatabaseHelper.KEY_ID + " DESC", null);
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(new Favorites(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    public void deleteFavorite(int trackId) {
        database.delete(DatabaseHelper.FAVORITE_TABLE, DatabaseHelper.KEY_TRACK_ID + "=" + trackId, null);
    }

}
