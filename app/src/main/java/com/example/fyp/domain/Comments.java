package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Comments implements Serializable{

    @SerializedName("comment_id")
    @Expose
    private String comment_id;

    @SerializedName("cust_id")
    @Expose
    private String cust_id;

    @SerializedName("printer_id")
    @Expose
    private String printer_id;

    @SerializedName("comment_content")
    @Expose
    private String comment_content;

    private Customer customer;

    private Printer printer;

    public Comments(){

    }
    public Comments(String comment_id, String cust_id,String printer_id,String comment_content,Customer customer,Printer printer){
        this.comment_id=comment_id;
        this.printer_id=printer_id;
        this.cust_id=cust_id;
        this.comment_content=comment_content;
        this.customer=customer;
        this.printer=printer;
    }
    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCommentCust_id() {
        return cust_id;
    }

    public void setCommentCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCommentPrinter_id() {
        return printer_id;
    }

    public void setCommentPrinter_id(String printer_id) {
        this.printer_id = printer_id;
    }


    public String getComment_content() {
        return comment_content;
    }
    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public void setCommentPrinter(Printer printer) {
        this.printer = printer;
    }


    public Printer getCommentPrinter() {
        return printer;
    }

    public void setCommentCustomer(Customer customer) {
        this.customer = customer;
    }


    public Customer getCommentCustomer() {
        return customer;
    }



    //JSON to OBJ
    public static Comments JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Comments comments= gson.fromJson(json, Comments.class);
        return comments;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Comments comments) {
        Gson gson = new Gson();
        String JSON = gson.toJson(comments);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Comments> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Comments>>(){}.getType();
        ArrayList<Comments> comments= gson.fromJson(json, listType);
        return comments;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Comments> Comments) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Comments);
        return JSON;
    }
}
