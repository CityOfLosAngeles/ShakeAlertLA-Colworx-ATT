package com.app.shakealertla.HttpUtils;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RestAPI {

    public static void PostUrlEncodedRequest(String TAG, String apiEndpoint, final JSONObject obj, final ServiceListener<JSONObject, VolleyError> listener) {
        StringRequest objectRequest = new StringRequest(Request.Method.POST,
                ConfigConstants.API_BASE_URL + apiEndpoint
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listener.success(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error(error);
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //noinspection unchecked
                return GsonUtils.fromJSON(obj,HashMap.class);
            }
        };

        HttpRequestHandler.getInstance().addToRequestQueue(objectRequest,TAG);
    }

    public static void GetUrlEncodedRequest(String TAG, String apiEndpoint, final ServiceListener<JSONObject, VolleyError> listener) {
        StringRequest objectRequest = new StringRequest(Request.Method.GET,
                ConfigConstants.API_BASE_URL + apiEndpoint
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listener.success(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error(error);
            }
        });

        HttpRequestHandler.getInstance().addToRequestQueue(objectRequest,TAG);
    }

    public static void PostRequest(String TAG, String apiEndpoint, JSONObject jsonObj, final ServiceListener<JSONObject, VolleyError> listener) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ConfigConstants.API_BASE_URL + apiEndpoint,
                jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error(error);
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        HttpRequestHandler.getInstance().addToRequestQueue(objectRequest,TAG);
    }

    public static void MultipartPostReq(String TAG, String apiEndpoint, String imagePath, final ServiceListener<JSONObject, VolleyError> listener){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, ConfigConstants.API_BASE_URL + apiEndpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            listener.success(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error(error);
            }
        });

        smr.addFile("file", imagePath);
        HttpRequestHandler.getInstance().addToRequestQueue(smr, TAG);
    }

    public static void GetRequest(String TAG, String apiEndpoint, final ServiceListener<JSONObject, VolleyError> listener){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ConfigConstants.API_BASE_URL + apiEndpoint,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error(error);
            }
        });

        HttpRequestHandler.getInstance().addToRequestQueue(objectRequest,TAG);
    }
}
