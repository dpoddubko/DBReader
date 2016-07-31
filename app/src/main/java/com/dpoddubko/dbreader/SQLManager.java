package com.dpoddubko.dbreader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class SQLManager {
    SQLiteDatabase mDB;

    public SQLManager(SQLiteDatabase mDB) {
        this.mDB = mDB;
    }

    public Cursor performQuery(String query) {
        Cursor c;
        try {
            c = mDB.rawQuery(query, null);
        } catch (SQLiteException e) {
            throw new RuntimeException("Query is not correct!", e);
        }
        return c;
    }
}
