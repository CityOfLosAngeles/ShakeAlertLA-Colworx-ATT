package com.app.shakealertla.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Colworx : Main Database Access class (SQLite Local DB)
 */
public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    public SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public SQLiteDatabase open() {
        this.database = openHelper.getWritableDatabase();
        return this.database;
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
//    public List<String> getQuotes() {
//        List<String> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT * FROM quotes", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            list.add(cursor.getString(0));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return list;
//    }
//
//    String TableName = "quotes";
//    public void addModel(String model) {
//        open();
//        SQLiteDatabase sqLiteDatabase = database;
//
//        ContentValues values = new ContentValues();
//        values.put("quote", model);
//        sqLiteDatabase.insert(TableName, null, values);
//        close();
//    }
}