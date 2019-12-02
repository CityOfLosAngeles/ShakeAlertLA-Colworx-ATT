package com.app.shakealertla.Services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.android.volley.error.VolleyError;
import com.app.shakealertla.HttpUtils.RestAPI;
import com.app.shakealertla.Listener.ServiceListener;
import com.app.shakealertla.Models.Shelters;
import com.app.shakealertla.Utils.AppLog;
import com.app.shakealertla.Utils.AppUtils;
import com.app.shakealertla.Utils.ConfigConstants;
import com.app.shakealertla.Utils.GsonUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShelterService {
    // Colworx : Rest API for get Shelters List
    public static void getSheltersList(final Context context, final ServiceListener<Shelters,VolleyError> listener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("f", "json");
            jsonObject.put("where", "FCODE LIKE 'Shelter - Activated'");
            jsonObject.put("outSr", "4326");
//            String oldURL = "https://services7.arcgis.com/aFfS9FqkIRSo0Ceu/ArcGIS/rest/services/EQ_Early_Warning_Mass_Care_Locations_(Demo)/FeatureServer/0/query";
//            String url = "https://services7.arcgis.com/aFfS9FqkIRSo0Ceu/ArcGIS/rest/services/EQ_Early_Warning_Mass_Care_Locations_(Demo)/FeatureServer/0/query";
//            String oldUrl2 = "https://services7.arcgis.com/aFfS9FqkIRSo0Ceu/arcgis/rest/services/EQ_Early_Warning_Mass_Care_Locations_Public_View/FeatureServer/0/query"; // For Production

            RestAPI.PostUrlEncodedRequest("FindAshelterActivity",
                    ConfigConstants.IN_DEV?ConfigConstants.SHELTER_DEV_URL:ConfigConstants.SHELTER_URL,
                    jsonObject, new ServiceListener<JSONObject, VolleyError>() {
                        @Override
                        public void success(JSONObject success) {
                            AppLog.d("result",success.toString());
                            Shelters shelters = GsonUtils.fromJSON(success,Shelters.class);
                            for (Shelters.Shelter feature : shelters.features) {
                                feature.address = getLocation(context,new LatLng(feature.geometry.y,feature.geometry.x));
                            }
                            listener.success(shelters);
                        }

                        @Override
                        public void error(VolleyError error) {
                            AppLog.d("result",error.getMessage());
                            listener.error(error);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Colworx : Get Current location and return address, city, state, country based on location
    public static String getLocation(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
