package com.app.shakealertla.Models;

import java.io.Serializable;
import java.util.ArrayList;

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