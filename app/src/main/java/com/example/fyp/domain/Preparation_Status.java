package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Preparation_Status implements Serializable {

    @SerializedName("prep_status_id")
    @Expose
    private String prepStatusID;
    @SerializedName("prep_status")
    @Expose
    private String prep_status;
    @Expose
    @SerializedName("order_id")
    private String orderId;

    public Preparation_Status(){
    }
    public Preparation_Status(String prepStatusID,String prep_status,String orderId){
        this.prepStatusID=prepStatusID;
        this.prep_status=prep_status;
        this.orderId=orderId;
    }

    public String getPrepStatusID() {
        return prepStatusID;
    }

    public void setPrepStatusID(String prepStatusID) {
        this.prepStatusID = prepStatusID;
    }

    public String getPrep_status() {
        return prep_status;
    }

    public void setPrep_status(String prepStatus) {
        this.prep_status = prepStatus;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    //JSON to OBJ
    public static Preparation_Status JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Preparation_Status Preparation_Status = gson.fromJson(json, Preparation_Status.class);
        return Preparation_Status;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Preparation_Status Preparation_Status) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Preparation_Status);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Preparation_Status> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Preparation_Status>>(){}.getType();
        ArrayList<Preparation_Status> Preparation_Status = gson.fromJson(json, listType);
        return Preparation_Status;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Preparation_Status> Preparation_Status) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Preparation_Status);
        return JSON;
    }
}