package com.app.shakealertla.Models;

import com.app.shakealertla.R;

import java.io.Serializable;

// Colworx : Model class used for holds some data which retrieved from Rest API or Local DB and then use Model class data in another classes or adapters
public class Plan implements Serializable{
    public int ID;
    public int Kit_ID;
    public String Kit_Name;
    public String Plan, Info, Notes;
    public int Completed;
    public String Plan_es, Info_es;
    public int Item_ID;

    public int getImage() {
        if (Completed == 0)
            return R.mipmap.off;
        else
            return R.mipmap.check;
    }
}