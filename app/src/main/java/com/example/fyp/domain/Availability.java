package com.example.fyp.domain;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Availability{

    private String img_available;
    private String doc_available;


    public Availability(){
    }

    public Availability(String img_available,String doc_available){
        this.img_available = img_available;
        this.doc_available = doc_available;
    }

    public String getImg_available(){
        return  this.img_available;
    }
    public String getDoc_available(){
        return  this.doc_available;
    }

    private void setImg_available(String img_available){this.img_available=img_available;}
    private void setDoc_available(String doc_available){this.doc_available=doc_available;}


    //JSON to OBJ
    public static Availability JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Availability Availability = gson.fromJson(json, Availability.class);
        return Availability;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Availability Availability) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Availability);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Availability> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Availability>>(){}.getType();
        ArrayList<Availability> Availability = gson.fromJson(json, listType);
        return Availability;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Availability> Availability) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Availability);
        return JSON;
    }
}
