package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PosterPreferences {
    private String paperSize;
    private String Quantity;
    private String PaperType;

    public PosterPreferences(String paperSize,String Quantity, String PaperType){
        this.paperSize =paperSize;
        this.Quantity =Quantity;
        this.PaperType = PaperType;

    }

    public String getPaperType() {
        return PaperType;
    }
    public void setPaperType(String PaperType){
        this.PaperType = PaperType;
    }

    public String getQuantity() {
        return Quantity;
    }
    public void setQuantity(String Quantity){
        this.Quantity = Quantity;
    }

    public String getpaperSize() {
        return paperSize;
    }
    public void setpaperSize(String paperSize){
        this.paperSize = paperSize;
    }


    //JSON to OBJ
    public static PosterPreferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        PosterPreferences  PosterPreferences = gson.fromJson(json, PosterPreferences .class);
        return PosterPreferences ;
    }

    //OBJ to JSON
    public static String OBJtoJSON(PosterPreferences  PosterPreferences ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(PosterPreferences );
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<PosterPreferences > JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<PosterPreferences >>(){}.getType();
        ArrayList<PosterPreferences > PosterPreferences  = gson.fromJson(json, listType);
        return PosterPreferences ;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<PosterPreferences > PosterPreferences ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(PosterPreferences );
        return JSON;
    }
}

