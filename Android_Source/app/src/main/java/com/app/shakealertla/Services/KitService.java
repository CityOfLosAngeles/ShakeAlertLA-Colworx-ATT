package com.app.shakealertla.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.shakealertla.DatabaseHelper.DatabaseAccess;
import com.app.shakealertla.Models.Kits;
import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.ShakeAlertLA;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;

// Colworx : Class for store and retrieve Data from Local SQLtite database
public class KitService {
    private static DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShakeAlertLA.getContext());
    //    SELECT * FROM Kits INNER JOIN Items on Kits.ID = Items.Kit_ID
    private static String ITEMS_TABLE = "Items";
    private static String KITS_TABLE = "Kits";

    public static ArrayList<Kits> getKits() {
        SQLiteDatabase database = databaseAccess.open();
        Cursor cursor = database.rawQuery("SELECT ID,Kit,Item_Name,Info,Notes,Completed,Kits_es,Item_Name_es,Item_ID FROM " + KITS_TABLE +
                        " INNER JOIN " + ITEMS_TABLE + " on " + KITS_TABLE + ".ID = " + ITEMS_TABLE + ".Kit_ID ORDER BY ID ASC"
                , null);
        cursor.moveToFirst();
        ArrayList<Kits> kitsArray = new ArrayList<>();
        int id = -1;
        while (!cursor.isAfterLast()) {
            Plan plan = new Plan();
            plan.Kit_ID = cursor.getInt(0);
            if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_ENGLISH)) {
                plan.Kit_Name = cursor.getString(1);
                plan.Plan = cursor.getString(2);
            }else {
                plan.Kit_Name = cursor.getString(6);
                plan.Plan = cursor.getString(7);
            }
            plan.Info = cursor.getString(3);
            plan.Notes = cursor.getString(4);
            plan.Completed = cursor.getInt(5);
            plan.Item_ID = cursor.getInt(8);
            if (id != plan.Kit_ID) {
                id = plan.Kit_ID;
                ArrayList<Plan> plans = new ArrayList<>();
                plans.add(plan);
                Kits kit = new Kits(id,plan.Kit_Name,plans);
                kitsArray.add(kit);
            } else {
                Kits kit = kitsArray.get(id-1);
                kit.items.add(plan);
            }
            cursor.moveToNext();
        }
        cursor.close();
        databaseAccess.close();
        return kitsArray;
    }

    public static void updateKitPlan(Plan plan) {
        SQLiteDatabase sqLiteDatabase = databaseAccess.open();
        ContentValues values = new ContentValues();
        values.put("Notes", plan.Notes);
        values.put("Completed", plan.Completed);
        sqLiteDatabase.update(ITEMS_TABLE, values, "Item_ID" + " = ?", new String[]{String.valueOf(plan.Item_ID)});
        sqLiteDatabase.close();
    }
}
