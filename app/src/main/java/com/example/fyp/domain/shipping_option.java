package com.example.fyp.domain;



import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class shipping_option implements Serializable {

    @SerializedName("ship_option_id")
    @Expose
    private String shipOptionId;
    @SerializedName("delivery_avail")
    @Expose
    private String deliveryAvail;
    @SerializedName("self_pick_up_avail")
    @Expose
    private String selfPickUpAvail;
    @SerializedName("printer_id")
    @Expose
    private String printerId;

    private Printer printer;

    public String getShipOptionId() {
        return shipOptionId;
    }

    public void setShipOptionId(String shipOptionId) {
        this.shipOptionId = shipOptionId;
    }

    public String getDeliveryAvail() {
        return deliveryAvail;
    }

    public void setDeliveryAvail(String deliveryAvail) {
        this.deliveryAvail = deliveryAvail;
    }

    public String getSelfPickUpAvail() {
        return selfPickUpAvail;
    }

    public void setSelfPickUpAvail(String selfPickUpAvail) {
        this.selfPickUpAvail = selfPickUpAvail;
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
    public static shipping_option JSONToOBJ(String json)  {
        Gson gson = new Gson();
        shipping_option shipping_option = gson.fromJson(json, shipping_option.class);
        return shipping_option;
    }

    //OBJ to JSON
    public static String OBJtoJSON(shipping_option shipping_option) {
        Gson gson = new Gson();
        String JSON = gson.toJson(shipping_option);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<shipping_option> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<shipping_option>>(){}.getType();
        ArrayList<shipping_option> shipping_option= gson.fromJson(json, listType);
        return shipping_option;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<shipping_option> shipping_option) {
        Gson gson = new Gson();
        String JSON = gson.toJson(shipping_option);
        return JSON;
    }


}