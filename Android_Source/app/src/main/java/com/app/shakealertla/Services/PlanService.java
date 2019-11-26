package com.app.shakealertla.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.shakealertla.DatabaseHelper.DatabaseAccess;
import com.app.shakealertla.Models.Plan;
import com.app.shakealertla.ShakeAlertLA;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;

import java.util.ArrayList;

public class PlanService {
    private static DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShakeAlertLA.getContext());
    private static String TableName = "Plans";

    public static ArrayList<Plan> getPlans() {
        ArrayList<Plan> plans = new ArrayList<>();
        SQLiteDatabase database = databaseAccess.open();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TableName, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Plan plan = new Plan();
            plan.Plan = cursor.getString(0);
            plan.Info = cursor.getString(1);
            plan.Notes = cursor.getString(2);
            plan.Completed = cursor.getInt(3);//4 is extra column
            if (SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_SPANISH)) {
                plan.Plan = cursor.getString(5);
                plan.Info = cursor.getString(6);
            }
            plan.ID = cursor.getInt(7);
            plans.add(plan);
            cursor.moveToNext();
        }
        cursor.close();
        databaseAccess.close();
        return plans;
    }

    public static void updatePlan(Plan plan) {
        SQLiteDatabase sqLiteDatabase = databaseAccess.open();
        ContentValues values = new ContentValues();
        values.put("Notes", plan.Notes);
        values.put("Completed", plan.Completed);
        sqLiteDatabase.update(TableName, values, "ID" + " = ?", new String[]{String.valueOf(plan.ID)});
        sqLiteDatabase.close();
    }
}
