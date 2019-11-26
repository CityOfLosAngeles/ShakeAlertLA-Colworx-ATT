package com.app.shakealertla.Models;

import com.app.shakealertla.Utils.GsonUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RecentEarthquakes implements Serializable {
    public String LatitudeValue;
    public String LongitudeValue;
    public String MagnitudeValue;
    public String Topic;
    public String startTime;
    public String body;

    public Date getTime() {
        Date date = new Date(Long.valueOf(startTime));
        return date;
    }

    public double getMagnitude() {
        try {
            return Double.valueOf(this.MagnitudeValue.replace(",","."));
        }catch (NumberFormatException e){
            return 0;
        }
    }

}