package com.app.shakealertla.Utils;

import com.app.shakealertla.Models.RecentEarthquakes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortRecentEarthquakeAPI {
    // Colworx : Sort Earthquakes list based on Time
    public static ArrayList byTime(ArrayList<RecentEarthquakes> arrayList){
        Collections.sort(arrayList, new Comparator<RecentEarthquakes>() {
            @Override
            public int compare(RecentEarthquakes a, RecentEarthquakes b) {
                return Long.compare(a.getTime().getTime(), b.getTime().getTime());//a.getDistance(mLocation).compareTo(b.getName());//change it to miles
            }
        });
        Collections.reverse(arrayList);
        return null;
    }

    // Colworx : Sort Earthquakes list based on Magnitude
    public static ArrayList byMagnitude(ArrayList<RecentEarthquakes> arrayList){
        Collections.sort(arrayList, new Comparator<RecentEarthquakes>() {
            @Override
            public int compare(RecentEarthquakes a, RecentEarthquakes b) {
                return Double.compare(a.getMagnitude(), b.getMagnitude());//a.getDistance(mLocation).compareTo(b.getName());//change it to miles
            }
        });
        Collections.reverse(arrayList);
        return null;
    }
}
