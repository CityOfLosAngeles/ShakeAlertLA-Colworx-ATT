package com.app.shakealertla.Models;

import java.io.Serializable;
import java.util.ArrayList;

// Colworx : Model class used for holds some data which retrieved from Rest API or Local DB and then use Model class data in another classes or adapters
public class Kits implements Serializable {
    public ArrayList<Plan> items;
    public String kitName;
    public int kitID;

    public Kits( int kitID, String kitName, ArrayList<Plan> items) {
        this.kitID = kitID;
        this.kitName = kitName;
        this.items = items;
    }
}