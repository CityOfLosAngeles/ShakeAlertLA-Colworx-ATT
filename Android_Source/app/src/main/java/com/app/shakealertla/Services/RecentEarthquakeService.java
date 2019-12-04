package com.app.shakealertla.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.DatabaseHelper.DatabaseAccess;
import com.app.shakealertla.HttpUtils.RestAPI;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.Earthquakes;
import com.app.shakealertla.ShakeAlertLA;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.SharedPreferenceManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecentEarthquakeService {
    // Colworx : Rest API for Register Device token and location in DB on Server
    public static void RegisterDevice(String deviceToken, double lat, double lon, final ServiceListener<String, String> listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DeviceID", deviceToken);
            jsonObject.put("LatLong", lat + "," + lon);
//            jsonObject.put("LatLong", "34.145803, -118.904742");//Fake location
            jsonObject.put("Language", SharedPreferenceManager.getLanguage().matches(ConfigConstants.LANGUAGE_ENGLISH) ? "Block-EN-" : "Block-ES-");
            AppLog.e("Register:::::::",jsonObject.toString());
            ////////////////////////////////////////

            RestAPI.PostRequest("RecentEarthquakeService",
                    ConfigConstants.IN_DEV?ConfigConstants.REGISTER_DEV_URL:ConfigConstants.REGISTER_URL,
                    jsonObject, new ServiceListener<JSONObject, VolleyError>() {
                        @Override
                        public void success(JSONObject success) {
                            AppLog.d("result", success.toString());
//                            Success Response: {code:200,msg:"1"}
//                            Error Response: {code:400,msg:"DeviceId and LatLong should be must."}
                            try {
                                if (success.getInt("code") == 200) {
                                    listener.success(success.getString("msg"));
                                }
                            } catch (JSONException e) {
                                AppUtils.Toast(e.getMessage());
                            }
//                            listener.success(shelters);
                        }

                        @Override
                        public void error(VolleyError error) {
                            if (error.getMessage() != null) {
                                if (error.networkResponse.statusCode == -1)
                                    listener.error("Please check your internet connection.");
                                else {
                                    AppLog.d("result", error.getMessage());
                                    listener.error(error.getMessage());
                                }
                            } else {
                                String res = "";
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    res = new String(error.networkResponse.data);
//                                    try {
//                                        JSONObject obj = new JSONObject(res);
                                    listener.error(res);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        listener.error(e.getMessage());
//                                    }
                                }
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Colworx : Rest API for updating device token and segmentID from Earthquake in DB on Server
    public static void setPushRate(String segmentID, final ServiceListener<String,String> listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DeviceID", FirebaseInstanceId.getInstance().getToken());
            jsonObject.put("SegmentID", segmentID);
            AppLog.e("Register:::::::",jsonObject.toString());

            RestAPI.PostRequest("RecentEarthquakeService",
                    ConfigConstants.IN_DEV?ConfigConstants.PUSH_OPEN_RATE_DEV_URL:ConfigConstants.PUSH_OPEN_RATE_URL,
                    jsonObject, new ServiceListener<JSONObject, VolleyError>() {
                        @Override
                        public void success(JSONObject success) {
                            AppLog.d("result", success.toString());
                            try {
                                if (success.getInt("code") == 200) {
                                    listener.success(success.getString("msg"));
                                }
                            } catch (JSONException e) {
                                AppUtils.Toast(e.getMessage());
                            }
                        }

                        @Override
                        public void error(VolleyError error) {
                            if (error.getMessage() != null) {
                                if (error.networkResponse.statusCode == -1)
                                    listener.error("Please check your internet connection.");
                                else {
                                    AppLog.d("result", error.getMessage());
                                    listener.error(error.getMessage());
                                }
                            } else {
                                String res = "";
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    res = new String(error.networkResponse.data);
                                    listener.error(res);
                                }
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShakeAlertLA.getContext());
    private static String TableName = "Notifications";

    // Colworx : To get EarthQuakes from Local SQLite Database
    public static ArrayList<Earthquakes> getEarthQuakes() {
        ArrayList<Earthquakes> earthquaksArray = new ArrayList<>();
        SQLiteDatabase database = databaseAccess.open();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TableName, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
//        while (cursor.moveToNext()) {
            Earthquakes earthquakes = new Earthquakes();
            earthquakes.ID = cursor.getInt(0);
            earthquakes.Category = cursor.getString(1);
            earthquakes.DepthUnit = cursor.getString(2);
            earthquakes.DepthValue = cursor.getString(3);
            earthquakes.EventOriginTimeStampUnit = cursor.getString(4);
            earthquakes.EventOriginTimeStampValue = cursor.getString(5);
            earthquakes.LatitudeUnit = cursor.getString(6);
            earthquakes.LatitudeValue = cursor.getString(7);
            earthquakes.Likelihood = cursor.getString(8);
            earthquakes.LongitudeUnit = cursor.getString(9);
            earthquakes.LongitudeValue = cursor.getString(10);
            earthquakes.MMI = cursor.getString(11);
            earthquakes.MagnitudeUnit = cursor.getString(12);
            earthquakes.MagnitudeValue = new DecimalFormat("#.#").format(Double.valueOf(cursor.getString(13).replace(",",".")));
            earthquakes.MessageOriginSystem = cursor.getString(14);
            earthquakes.TimeStamp = cursor.getString(15);
            earthquakes.Topic = cursor.getString(16);
            earthquakes.Type = cursor.getString(17);
            earthquakes.startTime = cursor.getString(18);
            earthquakes.title = cursor.getString(19);
            earthquakes.body = cursor.getString(20);

            earthquaksArray.add(earthquakes);
            cursor.moveToNext();
        }
        cursor.close();
        databaseAccess.close();
        return earthquaksArray;
    }

    public static void updatePlan(Earthquakes plan) {
        SQLiteDatabase sqLiteDatabase = databaseAccess.open();
        ContentValues values = new ContentValues();
        sqLiteDatabase.update(TableName, values, "ID" + " = ?", new String[]{String.valueOf(plan.ID)});
        sqLiteDatabase.close();
    }

    public static long addEarthQuake(Earthquakes earthquake) {
        SQLiteDatabase sqLiteDatabase = databaseAccess.open();
        ContentValues values = new ContentValues();
//        values.put("ID", earthquake.ID);
        values.put("Category", earthquake.Category);
        values.put("DepthUnit", earthquake.DepthUnit);
        values.put("DepthValue", earthquake.DepthValue);
        values.put("EventOriginTimeStampUnit", earthquake.EventOriginTimeStampUnit);
        values.put("EventOriginTimeStampValue", earthquake.EventOriginTimeStampValue);
        values.put("LatitudeUnit", earthquake.LatitudeUnit);
        values.put("LatitudeValue", earthquake.LatitudeValue);
        values.put("Likelihood", earthquake.Likelihood);
        values.put("LongitudeUnit", earthquake.LongitudeUnit);
        values.put("LongitudeValue", earthquake.LongitudeValue);
        values.put("MMI", earthquake.MMI);
        values.put("MagnitudeUnit", earthquake.MagnitudeUnit);
        values.put("MagnitudeValue", new DecimalFormat("##.##").format(Double.valueOf(earthquake.MagnitudeValue)));
        values.put("MessageOriginSystem", earthquake.MessageOriginSystem);
        values.put("TimeStamp", earthquake.TimeStamp);
        values.put("Topic", earthquake.Topic);
        values.put("Type", earthquake.Type);
        values.put("startTime", earthquake.startTime);
        values.put("title", earthquake.title);
        values.put("body", earthquake.body);
        return sqLiteDatabase.insert(TableName, null, values);
    }
}
