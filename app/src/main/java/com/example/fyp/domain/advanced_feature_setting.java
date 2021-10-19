package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class advanced_feature_setting implements Serializable {

    @SerializedName("adv_feature_id")
    @Expose
    private String advFeatureId;
    @SerializedName("detect_blurry")
    @Expose
    private String detectBlurry;
    @SerializedName("detect_adult")
    @Expose
    private String detectAdult;
    @SerializedName("detect_size")
    @Expose
    private String detectSize;
    @SerializedName("printer_id")
    @Expose
    private String printerId;

    private Printer printer;

    public String getAdvFeatureId() {
        return advFeatureId;
    }

    public void setAdvFeatureId(String advFeatureId) {
        this.advFeatureId = advFeatureId;
    }

    public String getDetectBlurry() {
        return detectBlurry;
    }

    public void setDetectBlurry(String detectBlurry) {
        this.detectBlurry = detectBlurry;
    }

    public String getDetectAdult() {
        return detectAdult;
    }

    public void setDetectAdult(String detectAdult) {
        this.detectAdult = detectAdult;
    }

    public String getDetectSize() {
        return detectSize;
    }

    public void setDetectSize(String detectSize) {
        this.detectSize = detectSize;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }
    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    //JSON to OBJ
    public static advanced_feature_setting JSONToOBJ(String json)  {
        Gson gson = new Gson();
        advanced_feature_setting advanced_feature_setting = gson.fromJson(json, advanced_feature_setting.class);
        return advanced_feature_setting;
    }

    //OBJ to JSON
    public static String OBJtoJSON(advanced_feature_setting advanced_feature_setting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(advanced_feature_setting);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<advanced_feature_setting> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<advanced_feature_setting>>(){}.getType();
        ArrayList<advanced_feature_setting> advanced_feature_setting= gson.fromJson(json, listType);
        return advanced_feature_setting;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<advanced_feature_setting> advanced_feature_setting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(advanced_feature_setting);
        return JSON;
    }

}