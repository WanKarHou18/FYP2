package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class businessSetting implements Serializable {
    @SerializedName("delivery_time_setting")
    @Expose
    private ArrayList<delivery_time_setting> delivery_time_setting;

    @SerializedName("shipping_option")
    @Expose
    private ArrayList<shipping_option> shipping_option;


    @SerializedName("delivery_zone")
    @Expose
    private ArrayList<delivery_zone> delivery_zone;

    @SerializedName("advanced_feature_setting")
    @Expose
    private ArrayList<advanced_feature_setting> advanced_feature_setting;

    @SerializedName("paymentSetting")
    @Expose
    private ArrayList<PaymentSetting> paymentSettings;


    public ArrayList<delivery_time_setting> getDelivery_time_setting() {
        return delivery_time_setting;
    }

    public void setDelivery_time_setting(ArrayList<delivery_time_setting> delivery_time_setting) {
        this.delivery_time_setting=delivery_time_setting;
    }

    public ArrayList<shipping_option> getShipping_option(){
        return shipping_option;
    }
    public void setShipping_option(ArrayList<shipping_option> shipping_option){
        this.shipping_option = shipping_option;
    }

    public void setDelivery_zone(ArrayList<delivery_zone>delivery_zone){
        this.delivery_zone = delivery_zone;
    }
    public ArrayList<delivery_zone> getDelivery_zone(){
        return delivery_zone;
    }

    public void setPaymentSettings(ArrayList<PaymentSetting>paymentSettings){
        this.paymentSettings = paymentSettings;
    }
    public ArrayList<PaymentSetting> getPaymentSettings(){
        return paymentSettings;
    }

    public void setAdvanced_feature_setting(ArrayList<advanced_feature_setting>advanced_feature_setting){
        this.advanced_feature_setting= advanced_feature_setting;
    }
    public ArrayList<advanced_feature_setting> getAdvanced_feature_setting(){
        return advanced_feature_setting;
    }
    //JSON to OBJ
    public static businessSetting JSONToOBJ(String json)  {
        Gson gson = new Gson();
        businessSetting businessSetting = gson.fromJson(json, businessSetting.class);
        return businessSetting;
    }

    //OBJ to JSON
    public static String OBJtoJSON(businessSetting businessSetting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(businessSetting);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<businessSetting> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<businessSetting>>(){}.getType();

        ArrayList<businessSetting> businessSetting= gson.fromJson(json, listType);
        return businessSetting;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<businessSetting> businessSetting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(businessSetting);
        return JSON;
    }



}
