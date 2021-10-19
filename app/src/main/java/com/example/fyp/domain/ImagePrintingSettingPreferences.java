package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImagePrintingSettingPreferences implements Serializable {

    @SerializedName("PaperSize")
    @Expose
    private ArrayList<String>  PaperSize;

    @SerializedName("PaperType")
    @Expose
    private ArrayList<String>  PaperType;

    @SerializedName("minQuantity")
    @Expose
    private String minQuantity;

    @SerializedName("maxQuantity")
    @Expose
    private String maxQuantity;

    @SerializedName("available")
    @Expose
    private String available;

    @SerializedName("border")
    @Expose
    private ArrayList<String>  border;           //For Wooden Stand Lamination, soOn

    @SerializedName("pricePerPage")
    @Expose
    private String pricePerPage;



    public ImagePrintingSettingPreferences(ArrayList<String>  PaperSize,ArrayList<String>  PaperType,String minQuantity,String maxQuantity,String available,ArrayList<String>  border,
   String pricePerPage ){
        this.PaperSize = PaperSize;
        this.PaperType=PaperType;
        this.minQuantity = minQuantity;
        this.maxQuantity =maxQuantity;
        this.available=available;
        this.pricePerPage= pricePerPage;
        this.border = border;
    }


    public String getpricePerPage() {
        return pricePerPage;
    }
    public void setpricePerPage(String pricePerPage){
        this.pricePerPage = pricePerPage;
    }


    public ArrayList<String>  getPaperType() {
        return PaperType;
    }
    public void setPaperType(ArrayList<String>  PaperType){
        this.PaperType = PaperType;
    }

    public ArrayList<String>  getPaperSize() {
        return PaperSize;
    }
    public void setPaperSize(ArrayList<String>  PaperSize){
        this.PaperSize = PaperSize;
    }

    public String getminQuantity() {
        return minQuantity;
    }
    public void setminQuantity(String minQuantity){
        this.minQuantity = minQuantity;
    }

    public String getmaxQuantity() {
        return maxQuantity;
    }
    public void setmaxQuantity(String maxQuantity){
        this.maxQuantity = maxQuantity;
    }

    public ArrayList<String>  getBorder() {
        return border;
    }
    public void setBorder(ArrayList<String>  border){
        this.border = border;
    }

    public String  getAvailable() {
        return available;
    }
    public void setAvailable(String  available){
        this.available = available;
    }

    //JSON to OBJ
    public static ImagePrintingSettingPreferences JSONToOBJ(String json)  {
        Gson gson = new Gson();
        ImagePrintingSettingPreferences  ImagePrintingSettingPreferences = gson.fromJson(json, ImagePrintingSettingPreferences .class);
        return ImagePrintingSettingPreferences ;
    }

    //OBJ to JSON
    public static String OBJtoJSON(ImagePrintingSettingPreferences  ImagePrintingSettingPreferences ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(ImagePrintingSettingPreferences );
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<ImagePrintingSettingPreferences > JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ImagePrintingSettingPreferences >>(){}.getType();
        ArrayList<ImagePrintingSettingPreferences > ImagePrintingSettingPreferences  = gson.fromJson(json, listType);
        return ImagePrintingSettingPreferences ;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<ImagePrintingSettingPreferences > ImagePrintingSettingPreferences ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(ImagePrintingSettingPreferences );
        return JSON;
    }

}
