package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Product_Printing_Preferences implements Serializable {

    @SerializedName("printing_pref_id")
    @Expose
    private String printingPrefId;
    @SerializedName("printing_preferences")
    @Expose
    private String printingPreferences;
    @SerializedName("sub_order_id")
    @Expose
    private String sub_order_id;

    public Product_Printing_Preferences(){
    }
    public Product_Printing_Preferences(String printingPrefId,String printingPreferences,String sub_order_id){
        this.printingPrefId = printingPrefId;
        this.printingPreferences = printingPreferences;
        this.sub_order_id=sub_order_id;
    }
    public String getPrintingPrefId() {
        return printingPrefId;
    }

    public void setPrintingPrefId(String printingPrefId) {
        this.printingPrefId = printingPrefId;
    }

    public String  getSub_order_id() {
        return sub_order_id;
    }

    public void setSub_order_id(String sub_order_id) {
        this.sub_order_id = sub_order_id;
    }

    public String getPrintingPreferences() {
        return printingPreferences;
    }

    public void setPrintingPreferences(String printingPreferences) {
        this.printingPreferences = printingPreferences;
    }
    //JSON to OBJ
    public static Product_Printing_Preferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Product_Printing_Preferences Product_Printing_Preferences = gson.fromJson(json, Product_Printing_Preferences.class);
        return Product_Printing_Preferences;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Product_Printing_Preferences Product_Printing_Preferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Product_Printing_Preferences);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Product_Printing_Preferences> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Product_Printing_Preferences>>(){}.getType();
        ArrayList<Product_Printing_Preferences> Product_Printing_Preferences = gson.fromJson(json, listType);
        return Product_Printing_Preferences;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Product_Printing_Preferences> Product_Printing_Preferences) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Product_Printing_Preferences);
        return JSON;
    }

}