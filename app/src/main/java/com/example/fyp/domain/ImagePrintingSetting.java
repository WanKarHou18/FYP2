package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImagePrintingSetting implements Serializable {

    @SerializedName("img_printing_id")
    @Expose
    private String img_printing_id;

    @SerializedName("img_print_pref_json")
    @Expose
    private ImagePrintingSettingPreferences img_print_pref_json;


    public ImagePrintingSetting(String img_printing_id,ImagePrintingSettingPreferences imagePrintingSettingPreferences){
        this.img_printing_id = img_printing_id;
        this.img_print_pref_json=imagePrintingSettingPreferences;
    }
    public String getImg_printing_id() {
        return img_printing_id;
    }
    public void setImg_printing_id(String img_printing_id){
        this.img_printing_id = img_printing_id;
    }

    public ImagePrintingSettingPreferences getImagePrintingSettingPreferences() {
        return img_print_pref_json;
    }
    public void setImagePrintingSettingPreferences(ImagePrintingSettingPreferences imagePrintingSettingPreferences){
        this.img_print_pref_json=imagePrintingSettingPreferences;
    }

    //JSON to OBJ
    public static ImagePrintingSetting JSONToOBJ(String json)  {
        Gson gson = new Gson();
        ImagePrintingSetting  ImagePrintingSetting = gson.fromJson(json, ImagePrintingSetting .class);
        return ImagePrintingSetting ;
    }

    //OBJ to JSON
    public static String OBJtoJSON(ImagePrintingSetting  ImagePrintingSetting ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(ImagePrintingSetting );
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<ImagePrintingSetting > JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ImagePrintingSetting >>(){}.getType();
        ArrayList<ImagePrintingSetting > ImagePrintingSetting  = gson.fromJson(json, listType);
        return ImagePrintingSetting ;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<ImagePrintingSetting > ImagePrintingSetting ) {
        Gson gson = new Gson();
        String JSON = gson.toJson(ImagePrintingSetting );
        return JSON;
    }


}
