package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Payment implements Serializable {

    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payment_cost")
    @Expose
    private String paymentCost;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @Expose
    @SerializedName("order_id")
    private String orderId;


    public Payment(){
    }
    public Payment(String paymentId,String paymentCost,String paymentType,String paymentStatus,String orderId){
        this.paymentId = paymentId;
        this.paymentCost=paymentCost;
        this.paymentType=paymentType;
        this.paymentStatus=paymentStatus;
        this.orderId=orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentCost() {
        return paymentCost;
    }

    public void setPaymentCost(String paymentCost) {
        this.paymentCost = paymentCost;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String PaymentStatus) {
        this.paymentStatus = PaymentStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    //JSON to OBJ
    public static Payment JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Payment Payment = gson.fromJson(json, Payment.class);
        return Payment;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Payment Payment) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Payment);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Payment> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Payment>>(){}.getType();
        ArrayList<Payment> Payment = gson.fromJson(json, listType);
        return Payment;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Payment> Payment) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Payment);
        return JSON;
    }
}
