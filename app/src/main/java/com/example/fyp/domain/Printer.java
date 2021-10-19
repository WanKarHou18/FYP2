package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Printer implements Serializable {
    private int printer_id;
    private int user_id;
    private User user;

    public Printer() {
    }

    public Printer(int printer_id,int user_id,User User){
        this.printer_id = printer_id;
        this.user_id = user_id;
        this.user = User;
    }

    public int getPrinter_id() {
        return printer_id;
    }
    public void setPrinter_id(int printer_id) {
        this.printer_id = printer_id;
    }

    public int getuser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //JSON to OBJ
    public static Printer JSONToOBJ(String json)  {
        Gson gson = new Gson();

        Printer printer = gson.fromJson(json, Printer.class);

        return printer;
    }

    //OBJ to JSON
    public static String OBJtoJSON(Printer printer) {
        Gson gson = new Gson();

        String JSON = gson.toJson(printer);

        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<Printer> JSONToLIST(String json)  {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Printer>>(){}.getType();
        ArrayList<Printer> printer = gson.fromJson(json, listType);

        return printer;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Printer> printer) {
        Gson gson = new Gson();

        String JSON = gson.toJson(printer);

        return JSON;
    }

}