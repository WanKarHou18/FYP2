package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class User implements Serializable {
    private int user_id;
    private String username;
    private String user_email;
    private String user_role;
    private String user_address;
    private String user_hp;
    private String user_password;
    private String status;
    private String bank_references;
    private Customer customer;
    private Printer printer;


    public User() {
    }

    public User(int user_id,  String username,String user_email,String user_role,String user_address, String user_hp,String user_password,String
                status,String bank_references,Customer customer,Printer printer) {
        this.user_id = user_id;
        this.username= username;
        this.user_email = user_email;
        this.user_role=user_role;
        this.user_address = user_address;
        this.user_hp = user_hp;
        this.user_password = user_password;
        this.status=status;
        this.bank_references=bank_references;
        this.customer=customer;
        this.printer = printer;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setID(int user_id) {
        this.user_id= user_id;
    }

    public String getUser_password() { return user_password; }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_hp() { return user_hp; }

    public void setUser_hp(String user_hp) {
        this.user_hp = user_hp;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String display_name) {
        this.user_address = user_address;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setRoles(String role) {
        this.user_role = user_role;
    }

    public String getBank_references() {
        return bank_references;
    }

    public void setBank_references(String bank_references) {
        this.bank_references = bank_references;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status= status;
    }

    public Customer getCustomer(){return  customer;}

    public void setCustomer(){this.customer=customer;}

    public Printer getPrinter(){return  printer;}

    public void setPrinter(){this.printer=printer;}

    //JSON to OBJ
    public static User JSONToOBJ(String json)  {
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user;
    }

    //OBJ to JSON
    public static String OBJtoJSON(User user) {
        Gson gson = new Gson();
        String JSON = gson.toJson(user);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<User> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();
        ArrayList<User> user = gson.fromJson(json, listType);
        return user;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<User> user) {
        Gson gson = new Gson();
        String JSON = gson.toJson(user);
        return JSON;
    }
}
