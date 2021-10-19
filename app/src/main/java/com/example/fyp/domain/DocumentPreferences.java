package com.example.fyp.domain;

import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DocumentPreferences {

    private String ColorSelected;
    private String PL;
    private String Edge;
    private String Slided;
    private String PageRange;
    private String SlidePerPage;
    private String SlidesArrangment;
    private String Copies;


    public DocumentPreferences(String ColorSelected,String PL,String Edge,String Slided,String PageRange,String SlidePerPage,String SlidesArrangement,String Copies){
        this.ColorSelected = ColorSelected;
        this.PL = PL;
        this.Edge = Edge;
        this.Slided = Slided;
        this.PageRange = PageRange;
        this.SlidePerPage = SlidePerPage;
        this.SlidesArrangment = SlidesArrangement;
        this.Copies=Copies;

    }
    public DocumentPreferences(){
    }

    public String getColorSelected() {
        return ColorSelected;
    }
    public void setColorSelected(String ColorSelected){
        this.ColorSelected = ColorSelected;
    }

    public String getPL() {
        return PL;
    }
    public void setPL(String PL){
        this.PL = PL;
    }
    public String getEdge() {
        return Edge;
    }
    public void setEdge(String Edge){
        this.Edge = Edge;
    }
    public String getSlided() {
        return Slided;
    }
    public void setSlided(String Slided){
        this.Slided =Slided;
    }
    public String getPageRange() {
        return PageRange;
    }

    public void setPageRange(String SlidePerPage){
        this.SlidePerPage= SlidePerPage;
    }
    public String getSlidePerPage() {
        return SlidePerPage;
    }
    public void setSlidePerPage(String SlidePerPage){
        this.SlidePerPage= SlidePerPage;
    }

    public String getSlidesArrangment() {
        return SlidesArrangment;
    }
    public void setSlidesArrangment(String SlidesArrangment){
        this.SlidesArrangment= SlidesArrangment;
    }
    public String getCopies() {
        return Copies;
    }
    public void setCopies(String Copies){
        this.Copies= Copies;
    }


    //JSON to OBJ
    public static DocumentPreferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        DocumentPreferences DocumentPreferences = gson.fromJson(json, DocumentPreferences.class);
        return DocumentPreferences;
    }

    //OBJ to JSON
    public static String OBJtoJSON(DocumentPreferences DocumentPreferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(DocumentPreferences);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<DocumentPreferences> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DocumentPreferences>>(){}.getType();
        ArrayList<DocumentPreferences> DocumentPreferences = gson.fromJson(json, listType);
        return DocumentPreferences;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<DocumentPreferences> DocumentPreferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(DocumentPreferences);
        return JSON;
    }

}
