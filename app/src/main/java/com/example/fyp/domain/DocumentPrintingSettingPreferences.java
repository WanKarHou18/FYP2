package com.example.fyp.domain;

import android.graphics.Color;

import com.example.fyp.ui_printer.PrintingPreferencesSetting.DocumentPrintingSettingFragment;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DocumentPrintingSettingPreferences implements Serializable {
    @SerializedName("ColorSelected")
    @Expose
    private ArrayList<String> ColorSelected; //BlackWhit, Color

    @SerializedName("PL")
    @Expose
    private ArrayList<String> PL;   //Potraint or Landscape

    @SerializedName("Edge")
    @Expose
    private ArrayList<String> Edge; //LongEdge or ShortEdge

    @SerializedName("Slided")
    @Expose
    private ArrayList<String> Slided; //One Slided or Double Slided

    @SerializedName("SlidesPerPage")
    @Expose
    private ArrayList<String> SlidesPerPage; //1,2,4,6,10

    @SerializedName("SlidesArrangement")
    @Expose
    private ArrayList<String> SlidesArrangement; //Horizontal, Vertical

    @SerializedName("minCopies")
    @Expose
    private String minCopies ;

    @SerializedName("maxCopies")
    @Expose
    private String maxCopies ;

    @SerializedName("pricePerColorPage")
    @Expose
    private String pricePerColorPage; //Prices for one color page

    @SerializedName("pricePerBlackWhitePage")
    @Expose
    private String pricePerBlackWhitePage; //Price for one blackwhite page

    @SerializedName("available")
    @Expose
    private String available;

    public DocumentPrintingSettingPreferences() {
    }

    public DocumentPrintingSettingPreferences(ArrayList<String> ColorSelected, ArrayList<String> PL, ArrayList<String> Edge,ArrayList<String> Slided,ArrayList<String> SlidesPerPage,ArrayList<String> SlidesArrangement,
    String minCopies,String maxCopies,String  pricePerColorPage, String pricePerBlackWhitePage,String available){
        this.ColorSelected = ColorSelected;
        this.PL = PL;
        this.Edge = Edge;
        this.Slided =Slided;
        this.SlidesPerPage = SlidesPerPage;
        this.SlidesArrangement = SlidesArrangement;
        this.minCopies=minCopies;
        this.maxCopies =maxCopies;
        this.pricePerColorPage = pricePerColorPage;
        this.pricePerBlackWhitePage =pricePerBlackWhitePage;
        this.available = available;
    }

    public ArrayList<String> getColorSelected() {
        return ColorSelected;
    }
    public void setColorSelected(ArrayList<String> ColorSelected){
        this.ColorSelected = ColorSelected;
    }

    public ArrayList<String> getPL() {
        return PL;
    }
    public void setPL(ArrayList<String> PL){
        this.PL = PL;
    }

    public ArrayList<String> getEdge() {
        return Edge;
    }
    public void setEdge(ArrayList<String> Edge){
        this.Edge = Edge;
    }

    public ArrayList<String> getSlided() {
        return Slided;
    }
    public void setSlided(ArrayList<String> Slided){
        this.Slided = Slided;
    }

    public String getminCopies() {
        return minCopies;
    }
    public void setminCopies(String minCopies){
        this.minCopies = minCopies;
    }

    public String getmaxCopies() {
        return maxCopies;
    }
    public void setmaxCopies(String maxCopies){
        this.maxCopies = maxCopies;
    }

    public ArrayList<String> getSlidesPerPage() {
        return SlidesPerPage;
    }
    public void setSlidesPerPage(ArrayList<String> SlidesPerPage){
        this.SlidesPerPage = SlidesPerPage;
    }

    public ArrayList<String> getSlidesArrangement() {
        return SlidesArrangement;
    }
    public void setSlidesArrangement(ArrayList<String> SlidesArrangement){
        this.SlidesArrangement = SlidesArrangement;
    }

    public String  getpricePerColorPage() {
        return pricePerColorPage;
    }
    public void setpricePerColorPage(String pricePerColorPage){
        this.pricePerColorPage = pricePerColorPage;
    }

    public String getpricePerBlackWhitePage() {
        return pricePerBlackWhitePage;
    }
    public void setpricePerBlackWhitePage(String pricePerBlackWhitePage){
        this.pricePerBlackWhitePage = pricePerBlackWhitePage;
    }
    public String getAvailable() {
        return available;
    }
    public void setAvailable(String available){
        this.available = available;
    }


    //JSON to OBJ
    public static DocumentPrintingSettingPreferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        DocumentPrintingSettingPreferences DocumentPrintingSettingPreferences = gson.fromJson(json, DocumentPrintingSettingPreferences.class);
        return DocumentPrintingSettingPreferences;
    }

    //OBJ to JSON
    public static String OBJtoJSON(DocumentPrintingSettingPreferences DocumentPrintingSettingPreferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(DocumentPrintingSettingPreferences);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<DocumentPrintingSettingPreferences> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DocumentPrintingSettingPreferences>>(){}.getType();
        ArrayList<DocumentPrintingSettingPreferences> DocumentPrintingSettingPreferences= gson.fromJson(json, listType);
        return DocumentPrintingSettingPreferences;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<DocumentPrintingSettingPreferences> DocumentPrintingSettingPreferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(DocumentPrintingSettingPreferences);
        return JSON;
    }
}
