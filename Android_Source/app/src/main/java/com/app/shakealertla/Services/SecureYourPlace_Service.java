package com.app.shakealertla.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.shakealertla.DatabaseHelper.DatabaseAccess;
import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.ShakeAlertLA;
import com.app.shakealertla.UserInterface.Activities.SecureYourPlace_Activity;

import java.util.ArrayList;

// Colworx : Class for store and retrieve Data from Local SQLtite database
public class SecureYourPlace_Service {
    private static DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShakeAlertLA.getContext());
    private static String TableName = "Secure_Place";
    public static ArrayList<SecureYourPlace_Activity.About> getSecurePlacesList(){
        ArrayList<SecureYourPlace_Activity.About> abouts = new ArrayList<>();
        SQLiteDatabase database = databaseAccess.open();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TableName, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            SecureYourPlace_Activity.About about = new SecureYourPlace_Activity.About();
            about.text = cursor.getString(0);
            about.completed = cursor.getInt(1);
            about.text_es = cursor.getString(2);
            about.ID = cursor.getInt(3);
            abouts.add(about);
            cursor.moveToNext();
        }
        cursor.close();
        databaseAccess.close();
        return abouts;
    }

    public static void updateSecurePlace(SecureYourPlace_Activity.About about){
        SQLiteDatabase sqLiteDatabase = databaseAccess.open();
        ContentValues values = new ContentValues();
//        values.put("Name", about.text);//Only status needed to be update
        values.put("Completed",about.completed);
        sqLiteDatabase.update(TableName, values, "ID" + " = ?", new String[]{String.valueOf(about.ID)});
        sqLiteDatabase.close();
    }
}
