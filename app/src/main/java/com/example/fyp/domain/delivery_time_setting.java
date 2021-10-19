package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class delivery_time_setting {

    @SerializedName("delivery_time_id")
    @Expose
    private String deliveryTimeId;
    @SerializedName("delivery_time_start")
    @Expose
    private String deliveryTimeStart;
    @SerializedName("printer_id")
    @Expose
    private String printerId;
    @SerializedName("delivery_time_end")
    @Expose
    private String deliveryTimeEnd;

    private Printer printer;

    public String getDeliveryTimeId() {
        return deliveryTimeId;
    }

    public void setDeliveryTimeId(String deliveryTimeId) {
        this.deliveryTimeId = deliveryTimeId;
    }

    public String getDeliveryTimeStart() {
        return deliveryTimeStart;
    }

    public void setDeliveryTimeStart(String deliveryTimeStart) {
        this.deliveryTimeStart = deliveryTimeStart;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getDeliveryTimeEnd() {
        return deliveryTimeEnd;
    }

    public void setDeliveryTimeEnd(String deliveryTimeEnd) {
        this.deliveryTimeEnd = deliveryTimeEnd;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    //JSON to OBJ
    public static delivery_time_setting JSONToOBJ(String json)  {
        Gson gson = new Gson();
        delivery_time_setting delivery_time_setting = gson.fromJson(json, delivery_time_setting.class);
        return delivery_time_setting;
    }

    //OBJ to JSON
    public static String OBJtoJSON(delivery_time_setting delivery_time_setting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(delivery_time_setting);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<delivery_time_setting> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<delivery_time_setting>>(){}.getType();
        ArrayList<delivery_time_setting> delivery_time_setting= gson.fromJson(json, listType);
        return delivery_time_setting;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<delivery_time_setting> delivery_time_setting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(delivery_time_setting);
        return JSON;
    }

}