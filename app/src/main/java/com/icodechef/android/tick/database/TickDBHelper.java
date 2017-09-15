package com.icodechef.android.tick.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class TickDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tick.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TickDBAdapter.TABLE_NAME + " (" +
                    TickDBAdapter._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TickDBAdapter.COLUMN_NAME_START_TIME + " DATETIME NOT NULL," +
                    TickDBAdapter.COLUMN_NAME_END_TIME + " DATETIME," +
                    TickDBAdapter.COLUMN_NAME_DURATION + " INTEGER NOT NULL," +
                    TickDBAdapter.COLUMN_NAME_DATE_ADD + " DATE NOT NULL" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TickDBAdapter.TABLE_NAME;


    TickDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
