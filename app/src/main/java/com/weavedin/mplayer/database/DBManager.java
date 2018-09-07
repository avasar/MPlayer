package com.weavedin.mplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


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

    public void insert(String value) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.KEY_VALUE, value);
        database.insert(DatabaseHelper.SEARCH_LIST_TABLE, null, contentValue);
    }

    public String[] getAllSearchTerms(String value) {
        try {
            String terms[] = null;
            //Open connection to read only
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT DISTINCT * FROM " + DatabaseHelper.SEARCH_LIST_TABLE +
                " WHERE " +  DatabaseHelper.KEY_VALUE + "  LIKE  '%" +value + "%' ";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    terms = new String[cursor.getCount()];
                    int i= 0;
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

}
