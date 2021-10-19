package com.example.fyp.domain;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Complaint implements Serializable {

    @SerializedName("complaint_rec_id")
    @Expose
    private String complaint_rec_id;

    @SerializedName("cust_id")
    @Expose
    private String cust_id;

    @SerializedName("printer_id")
    @Expose
    private String printer_id;

    @SerializedName("complaint_content")
    @Expose
    private String complaint_content;

    @SerializedName("complaint_status")
    @Expose
    private String complaint_status;

    private Customer customer;
    private Printer printer;

    public Complaint(){

    }
    public Complaint(String complaint_rec_id, String cust_id,String printer_id,String complaint_content,String complaint_status,Customer customer,Printer printer){
        this.complaint_rec_id=complaint_rec_id;
        this.cust_id=cust_id;
        this.printer_id=printer_id;
        this.complaint_content=complaint_content;
        this.complaint_status = complaint_status;
        this.customer=customer;
        this.printer=printer;
    }
    public String getComplaint_rec_id() {
        return complaint_rec_id;
    }
    public void setComplaint_rec_id(String complaint_rec_id) {
        this.complaint_rec_id = complaint_rec_id;
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


    public String getComplaint_content() {
        return complaint_content;
    }
    public void setComplaint_content(String complaint_content) {
        this.complaint_content = complaint_content;
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

    public void setComplaint_status(String complaint_status) {
        this.printer = printer;
    }
    public String getComplaint_status() {
        return complaint_status;
    }



    //JSON to OBJ
    public static Complaint JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Complaint complaint = gson.fromJson(json, Complaint.class);
        return complaint;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Complaint complaint) {
        Gson gson = new Gson();
        String JSON = gson.toJson(complaint);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<Complaint> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Complaint>>(){}.getType();
        ArrayList<Complaint> complaint= gson.fromJson(json, listType);
        return complaint;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Complaint> complaint) {
        Gson gson = new Gson();
        String JSON = gson.toJson(complaint);
        return JSON;
    }
}
