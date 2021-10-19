package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Order_Address implements Serializable {

    @SerializedName("order_add_id")
    @Expose
    private String orderAddId;
    @SerializedName("customer_address")
    @Expose
    private String customer_address;
    @SerializedName("printer_address")
    @Expose
    private String printer_address;
    @SerializedName("distance")
    @Expose
    private String distance;
    @Expose
    @SerializedName("order_id")
    private String orderId;

    public Order_Address(){

    }
    public Order_Address(String orderAddId,String customer_address,String printer_address,String Distance,String orderId){
        this.orderAddId = orderAddId;
        this.printer_address=printer_address;
        this.customer_address = customer_address;
        this.distance = Distance;
        this.orderId=orderId;
    }

    public String getOrderAddId() {
        return orderAddId;
    }

    public void setOrderAddId(String orderAddId) {
        this.orderAddId = orderAddId;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomerAddress(String customer_address) {
        this.customer_address = customer_address;
    }
    public String getPrinter_address() {
        return printer_address;
    }

    public void setPrinterAddress(String printer_address) {
        this.printer_address = printer_address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    //JSON to OBJ
    public static Order_Address JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Order_Address Order_Address = gson.fromJson(json, Order_Address.class);
        return Order_Address;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Order_Address Order_Address) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Order_Address);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Order_Address> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Order_Address>>(){}.getType();
        ArrayList<Order_Address> Order_Address = gson.fromJson(json, listType);
        return Order_Address;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Order_Address> Order_Address) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Order_Address);
        return JSON;
    }
}