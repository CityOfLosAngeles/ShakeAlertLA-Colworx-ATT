package com.app.shakealertla.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GsonUtils {

    public static <T> JSONObject toJSON(T obj)throws JSONException {
        Gson gson = new Gson();
        return new JSONObject(gson.toJson(obj));
    }

    public static <T> T fromJSON(String json, Class<T> classOfT){
        Gson gson = new Gson();
        return gson.fromJson(json,classOfT);
    }

    public static <T> T fromJSON(JSONObject json, Class<T> classOfT){
        Gson gson = new Gson();
        return gson.fromJson(json.toString(),classOfT);
    }

    public static <T> T fromHashMap(HashMap hashMap, Class<T> classOfT){
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(hashMap);
        return gson.fromJson(jsonElement, classOfT);
    }
}
