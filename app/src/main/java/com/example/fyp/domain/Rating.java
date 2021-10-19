package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Rating implements Serializable {

    @SerializedName("rating_id")
    @Expose
    private String rating_id;
    @SerializedName("cust_id")
    @Expose
    private String cust_id;
    @SerializedName("printer_id")
    @Expose
    private String printer_id;
    @SerializedName("rating_value")
    @Expose
    private String rating_value;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("printer")
    @Expose
    private Printer printer;

    public Rating(){

    }
    public Rating(String rating_id, String cust_id,String printer_id,String rating_value,Customer customer,Printer printer){
        this.rating_id=rating_id;
        this.cust_id=cust_id;
        this.printer_id=printer_id;
        this.rating_value=rating_value;
        this.customer=customer;
        this.printer=printer;
    }
    public String getRating_id() {
        return rating_id;
    }
    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public String getCust_id() {
        return cust_id;
    }
    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getPrinter_id() {
        return printer_id;
    }
    public void setPrinter_id(String printer_id) {
        this.printer_id = printer_id;
    }


    public String getrating_value() {
        return rating_value;
    }
    public void setrating_value(String rating_value) {
        this.rating_value = rating_value;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
    public Printer getPrinter() {
        return printer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Customer getCustomer() {
        return customer;
    }



    //JSON to OBJ
    public static Rating JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Rating rating = gson.fromJson(json, Rating.class);
        return rating;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Rating rating) {
        Gson gson = new Gson();
        String JSON = gson.toJson(rating);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Rating> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Rating>>(){}.getType();
        ArrayList<Rating> rating= gson.fromJson(json, listType);
        return rating;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Rating> Rating) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Rating);
        return JSON;
    }
}
