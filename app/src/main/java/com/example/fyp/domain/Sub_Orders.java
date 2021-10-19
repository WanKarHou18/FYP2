package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Sub_Orders implements Serializable {

    @SerializedName("sub_order_id")
    @Expose
    private String subOrderId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("cost")
    @Expose
    private String cost;

    private Product_Printing_Preferences product_printing_preferences;

    private ResourcesRecord resourcesRecord;

    public Sub_Orders(){
    }
    public Sub_Orders(String subOrderId, String orderId,String cost,Product_Printing_Preferences product_printing_preferences,ResourcesRecord resourcesRecord){
        this.subOrderId = subOrderId;
        this.orderId = orderId;
        this.product_printing_preferences = product_printing_preferences;
        this.cost = cost;
        this.resourcesRecord=resourcesRecord;

    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /*
    public String getPrintingPrefId() {
        return printingPrefId;
    }

    public void setPrintingPrefId(String printingPrefId) {
        this.printingPrefId = printingPrefId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }*/

    public Product_Printing_Preferences getProduct_Printing_Preferences() {
        return product_printing_preferences;
    }

    public void setProduct_printing_preferences(Product_Printing_Preferences product_printing_preferences) {
        this.product_printing_preferences = product_printing_preferences;
    }
    public ResourcesRecord getResourcesRecord() {
        return resourcesRecord;
    }

    public void setResourcesRecord(ResourcesRecord resourcesRecord) {
        this.resourcesRecord = resourcesRecord;
    }
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    //JSON to OBJ
    public static Sub_Orders JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Sub_Orders sub_orders = gson.fromJson(json, Sub_Orders.class);
        return sub_orders;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Sub_Orders sub_orders) {
        Gson gson = new Gson();
        String JSON = gson.toJson(sub_orders);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Sub_Orders> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();
        ArrayList<Sub_Orders> sub_orders = gson.fromJson(json, listType);
        return sub_orders;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Sub_Orders> sub_orders) {
        Gson gson = new Gson();
        String JSON = gson.toJson(sub_orders);
        return JSON;
    }
}
