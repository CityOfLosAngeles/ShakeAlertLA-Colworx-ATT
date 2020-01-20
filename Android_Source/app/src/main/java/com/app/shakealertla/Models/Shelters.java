package com.app.shakealertla.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Colworx : Model class used for holds some data which retrieved from Rest API or Local DB and then use Model class data in another classes or adapters
 */
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
