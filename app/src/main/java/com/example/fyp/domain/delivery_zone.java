package com.example.fyp.domain;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class delivery_zone implements Serializable {

        @SerializedName("dev_zone_id")
        @Expose
        private String devZoneId;
        @SerializedName("delivery_zone_dist")
        @Expose
        private String deliveryZoneDist;
        @SerializedName("printer_id")
        @Expose
        private String printerId;

        private Printer printer;

        public String getDevZoneId() {
            return devZoneId;
        }

        public void setDevZoneId(String devZoneId) {
            this.devZoneId = devZoneId;
        }

        public String getDeliveryZoneDist() {
            return deliveryZoneDist;
        }

        public void setDeliveryZoneDist(String deliveryZoneDist) {
            this.deliveryZoneDist = deliveryZoneDist;
        }

        public String getPrinterId() {
            return printerId;
        }

        public void setPrinterId(String printerId) {
            this.printerId = printerId;
        }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }



    //JSON to OBJ
    public static delivery_zone JSONToOBJ(String json)  {
        Gson gson = new Gson();
        delivery_zone delivery_zone = gson.fromJson(json, delivery_zone.class);
        return delivery_zone;
    }

    //OBJ to JSON
    public static String OBJtoJSON(delivery_zone delivery_zone) {
        Gson gson = new Gson();
        String JSON = gson.toJson(delivery_zone);
        return JSON;
    }

    //JSON array to OBJ array
    public static ArrayList<delivery_zone> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<delivery_zone>>(){}.getType();
        ArrayList<delivery_zone> delivery_zone= gson.fromJson(json, listType);
        return delivery_zone;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<delivery_zone> delivery_zone) {
        Gson gson = new Gson();
        String JSON = gson.toJson(delivery_zone);
        return JSON;
    }
    }

