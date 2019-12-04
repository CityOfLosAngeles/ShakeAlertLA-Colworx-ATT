package com.app.shakealertla.Models;

import com.app.shakealertla.Utils.GsonUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// Colworx : Model class used for holds some data which retrieved from Rest API or Local DB and then use Model class data in another classes or adapters
public class Earthquakes implements Serializable {
    public int ID;
    public String Category;
    public String DepthUnit;
    public String DepthValue;
    public String EventOriginTimeStampUnit;
    public String EventOriginTimeStampValue;
    public String LatitudeUnit;
    public String LatitudeValue;
    public String Likelihood;
    public String LongitudeUnit;
    public String LongitudeValue;
    public String MMI;
    public String MagnitudeUnit;
    public String MagnitudeValue;
    public String MessageOriginSystem;
    public String TimeStamp;
    public String Topic;
    public String Type;
    public String startTime;
    public ArrayList<String> Polygon = new ArrayList<>();
    public ArrayList<ArrayList<String>> Polygons = new ArrayList<>();
    public String title;
    public String body;
    public ArrayList<String> Colors = new ArrayList<>();
    public String SegmentID;

    public Earthquakes() {
    }

    public Earthquakes(int ID, String category, String depthUnit, String depthValue, String eventOriginTimeStampUnit,
                       String eventOriginTimeStampValue, String latitudeUnit, String latitudeValue, String likelihood,
                       String longitudeUnit, String longitudeValue, String MMI, String magnitudeUnit, String magnitudeValue,
                       String messageOriginSystem, String timeStamp, String topic, String type, String startTime, String title, String body) {
        this.ID = ID;
        Category = category;
        DepthUnit = depthUnit;
        DepthValue = depthValue;
        EventOriginTimeStampUnit = eventOriginTimeStampUnit;
        EventOriginTimeStampValue = eventOriginTimeStampValue;
        LatitudeUnit = latitudeUnit;
        LatitudeValue = latitudeValue;
        Likelihood = likelihood;
        LongitudeUnit = longitudeUnit;
        LongitudeValue = longitudeValue;
        this.MMI = MMI;
        MagnitudeUnit = magnitudeUnit;
        MagnitudeValue = magnitudeValue;
        MessageOriginSystem = messageOriginSystem;
        TimeStamp = timeStamp;
        Topic = topic;
        Type = type;
        this.startTime = startTime;
        this.title = title;
        this.body = body;
    }

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

    public String toJSON(Earthquakes earthquakes){
        try {
            return GsonUtils.toJSON(earthquakes).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Earthquakes fromJSON(String earthquake){
        return GsonUtils.fromJSON(earthquake, Earthquakes.class);
    }

    public ArrayList<LatLng> getPolygonCordinates(ArrayList<String> strings){
        ArrayList<LatLng> array = new ArrayList<>();
        for (String string : strings) {
            List<String> list = Arrays.asList(string.split(","));
            LatLng latLng = new LatLng(Double.valueOf(list.get(0)),Double.valueOf(list.get(1)));
            array.add(latLng);
        }
        return array;
    }
}