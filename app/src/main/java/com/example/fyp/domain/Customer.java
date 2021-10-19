package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {

    @SerializedName("cust_id")
    @Expose
    private String custId;
    @SerializedName("user_id")
    @Expose
    private String userId;

    //User
    private User user;

    public Customer(){
    }

    public Customer(String custId, String userId,User user){
        this.custId=custId;
        this.userId=userId;
        this.user=user;
    }
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser(){ return user;}

    public void setUser(User user){this.user=user;}


    //JSON to OBJ
    public static Customer JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Customer Customer = gson.fromJson(json, Customer.class);
        return Customer;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Customer Customer) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Customer);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Customer> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Customer>>(){}.getType();
        ArrayList<Customer> Customer = gson.fromJson(json, listType);
        return Customer;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Customer> Customer) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Customer);
        return JSON;
    }
}