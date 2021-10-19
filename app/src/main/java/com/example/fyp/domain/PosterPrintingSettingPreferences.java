package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PosterPrintingSettingPreferences {
    private ArrayList<String> paperSize;
    private ArrayList<String> PaperType;
    private ArrayList<String> pricePerPaper;

    public PosterPrintingSettingPreferences(ArrayList<String> paperSize,ArrayList<String> PaperType,ArrayList<String> pricePerPaper){
        this.paperSize = paperSize;
        this.PaperType = PaperType;
        this.pricePerPaper = pricePerPaper;
    }
    public ArrayList<String> getpaperSize() {
        return paperSize;
    }
    public void setpaperSize(ArrayList<String> paperSize){
        this.paperSize = paperSize;
    }

    public ArrayList<String> getPaperType() {
        return PaperType;
    }
    public void setPaperType(ArrayList<String> PaperType){
        this.PaperType = PaperType;
    }

    public ArrayList<String> getpricePerPaper() {
        return pricePerPaper;
    }
    public void setpricePerPaper(ArrayList<String> pricePerPaper){
        this.pricePerPaper = pricePerPaper;
    }



    //JSON to OBJ
    public static PosterPrintingSettingPreferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        PosterPrintingSettingPreferences  PosterPrintingSettingPreferences = gson.fromJson(json, PosterPrintingSettingPreferences .class);
        return PosterPrintingSettingPreferences ;
    }

    //OBJ to JSON
    public static String OBJtoJSON(PosterPrintingSettingPreferences  PosterPrintingSettingPreferences ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(PosterPrintingSettingPreferences );
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<PosterPrintingSettingPreferences > JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<PosterPrintingSettingPreferences >>(){}.getType();
        ArrayList<PosterPrintingSettingPreferences > PosterPrintingSettingPreferences  = gson.fromJson(json, listType);
        return PosterPrintingSettingPreferences ;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<PosterPrintingSettingPreferences > PosterPrintingSettingPreferences ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(PosterPrintingSettingPreferences );
        return JSON;
    }

}
