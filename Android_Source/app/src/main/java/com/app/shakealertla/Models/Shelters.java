package com.app.shakealertla.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Shelters implements Serializable {
    public ArrayList<Shelter> features;
    public static class Shelter {
        public Attributes attributes;
        public Geometry geometry;
        public String address;

        public static class Attributes {
            public String FacilityName;
        }

        public static class Geometry {
            public double x, y;
        }
    }
}
