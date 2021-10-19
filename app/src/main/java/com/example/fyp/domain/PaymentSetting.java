package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PaymentSetting implements Serializable {

    @SerializedName("payment_setting_id")
    @Expose
    private String payment_setting_id;
    @SerializedName("online_payment")
    @Expose
    private String online_payment;
    @SerializedName("COD")
    @Expose
    private String COD;
    @SerializedName("printer_id")
    @Expose
    private String printer_id;

    public PaymentSetting(){

    }

    public PaymentSetting(String payment_setting_id,String online_payment,String COD,String printer_id){
        this.payment_setting_id=payment_setting_id;
        this.online_payment=online_payment;
        this.COD =COD;
        this.printer_id=printer_id;
    }

    public String getOnline_payment() {
        return online_payment;
    }
    public void setOnline_payment(String online_payment) {
        this.online_payment = online_payment;
    }
    public String getpayment_setting_id() {
        return payment_setting_id;
    }

    public void setpayment_setting_id(String payment_setting_id) {
        this.payment_setting_id = payment_setting_id;
    }
    public String getCOD() {
        return COD;
    }

    public void setCOD(String COD) {
        this.COD = COD;
    }

    public String getprinter_id() {
        return printer_id;
    }

    public void setprinter_id(String  printer_id) {
        this.printer_id = printer_id;
    }

    //JSON to OBJ
    public static PaymentSetting JSONToOBJ(String json)  {
        Gson gson = new Gson();
        PaymentSetting PaymentSetting = gson.fromJson(json, PaymentSetting.class);
        return PaymentSetting;
    }


    //OBJ to JSON
    public static String OBJtoJSON(PaymentSetting PaymentSetting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(PaymentSetting);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<PaymentSetting> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<PaymentSetting>>(){}.getType();
        ArrayList<PaymentSetting> PaymentSetting = gson.fromJson(json, listType);
        return PaymentSetting;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<PaymentSetting> PaymentSetting) {
        Gson gson = new Gson();
        String JSON = gson.toJson(PaymentSetting);
        return JSON;
    }

}
