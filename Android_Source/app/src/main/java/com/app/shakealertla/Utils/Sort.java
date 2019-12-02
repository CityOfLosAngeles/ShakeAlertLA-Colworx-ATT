package com.app.shakealertla.Utils;

import com.app.shakealertla.Models.Earthquakes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sort {
    // Colworx : Sort Earthquakes list based on Time
    public static ArrayList byTime(ArrayList<Earthquakes> arrayList){
        Collections.sort(arrayList, new Comparator<Earthquakes>() {
            @Override
            public int compare(Earthquakes a, Earthquakes b) {
                return Long.compare(a.getTime().getTime(), b.getTime().getTime());//a.getDistance(mLocation).compareTo(b.getName());//change it to miles
            }
        });
        Collections.reverse(arrayList);
        return null;
    }

    // Colworx : Sort Earthquakes list based on Magnitude
    public static ArrayList byMagnitude(ArrayList<Earthquakes> arrayList){
        Collections.sort(arrayList, new Comparator<Earthquakes>() {
            @Override
            public int compare(Earthquakes a, Earthquakes b) {
                return Double.compare(a.getMagnitude(), b.getMagnitude());//a.getDistance(mLocation).compareTo(b.getName());//change it to miles
            }
        });
        Collections.reverse(arrayList);
        return null;
    }
}
