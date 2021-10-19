package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImagePreferences {

    private String PaperSize;
    private String PaperType;
    private String Copies;
    private String border;

    public ImagePreferences (String PaperSize,String PaperType,String Copies, String border){
        this.PaperSize = PaperSize;
        this.PaperType = PaperType;
        this.Copies = Copies;
        this.border = border;
    }

    public String getPaperSize() {
        return PaperSize;
    }
    public void setPaperSize(String PaperSize){
        this.PaperSize = PaperSize;
    }

    public String getPaperType() {
        return PaperType;
    }
    public void setPaperType(String PaperType){
        this.PaperType = PaperType;
    }
    public String getborder() {
        return border;
    }
    public void setborder(String border){
        this.border= border;
    }
    public String getCopies() {
        return Copies;
    }
    public void setCopies(String Copies){
        this.Copies= Copies;
    }



    //JSON to OBJ
    public static ImagePreferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        ImagePreferences ImagePreferences = gson.fromJson(json, ImagePreferences.class);
        return ImagePreferences;
    }

    //OBJ to JSON
    public static String OBJtoJSON(ImagePreferences ImagePreferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(ImagePreferences);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<ImagePreferences> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ImagePreferences>>(){}.getType();
        ArrayList<ImagePreferences> ImagePreferences = gson.fromJson(json, listType);
        return ImagePreferences;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<ImagePreferences> ImagePreferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(ImagePreferences);
        return JSON;
    }

}
