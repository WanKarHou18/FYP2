package com.example.fyp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Orders implements Serializable {

    @Expose
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("cust_id")
    @Expose
    private String custId;
    @SerializedName("printer_id")
    @Expose
    private String printerId;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("delivery_mode")
    @Expose
    private String deliveryMode;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("order_code_reference")
    @Expose
    private String orderCodeReference;

    //Array List of sub_orders
    private ArrayList<Sub_Orders> sub_orders = new ArrayList<>();

    //Order_Address
    private Order_Address order_address;

    //Payment
    private Payment payment;

    //Preparation Status
    private Preparation_Status preparation_status;

    //Customer
    private Customer customer;

    //Printer
    private Printer printer;

    public Orders(){
    }

    public Orders(String orderId,String custId,String printerId,String orderStatus,String  deliveryMode,String
                  orderDate,String time,String  createdDatetime,String orderCodeReference,ArrayList<Sub_Orders> sub_orders,Order_Address order_address,Payment payment,Preparation_Status preparation_status,
                  Customer customer , Printer printer){
        this.orderId =orderId;
        this.custId =custId;
        this.printerId =printerId;
        this.orderStatus=orderStatus;
        this.deliveryMode=deliveryMode;
        this.orderDate=orderDate;
        this.time =time;
        this.createdDatetime=createdDatetime;
        this.orderCodeReference=orderCodeReference;
        this.sub_orders=sub_orders;
        this.order_address = order_address;
        this.payment = payment;
        this.preparation_status=preparation_status;
        this.customer=customer;
        this.printer =printer;

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }
/*
    public String getPrepStatusId() {
        return prepStatusId;
    }

    public void setPrepStatusId(String prepStatusId) {
        this.prepStatusId = prepStatusId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderAddId() {
        return orderAddId;
    }

    public void setOrderAddId(String orderAddId) {
        this.orderAddId = orderAddId;
    }

 */

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getOrderCodeReference() {
        return orderCodeReference;
    }

    public void setOrderCodeReference(String orderCodeReference){
        this.orderCodeReference=orderCodeReference;
    }

    public ArrayList<Sub_Orders> getSub_orders(){return sub_orders;}

    public void setSub_orders(ArrayList<Sub_Orders> sub_orders){ this.sub_orders=sub_orders;}

    public Order_Address getOrder_address(){return order_address;}

    public void setOrder_address(Order_Address order_address) {
        this.order_address = order_address;
    }
    public Payment getPayment(){return payment;}

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Preparation_Status getPreparation_status() {
        return preparation_status;
    };

    public void setPreparation_status(Preparation_Status preparation_status) {
        this.preparation_status = preparation_status;
    }
    public Customer getCustomer() {
        return customer;
    };

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Printer getPrinter() {
        return printer;
    };

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
    //JSON to OBJ
    public static Orders JSONToOBJ(String json)  {
        Gson gson = new Gson();
        Orders Orders = gson.fromJson(json, Orders.class);
        return Orders;
    }


    //OBJ to JSON
    public static String OBJtoJSON(Orders Orders) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Orders);
        return JSON;
    }
    //JSON array to OBJ array
    public static ArrayList<Orders> JSONToLIST(String json)  {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Orders>>(){}.getType();
        ArrayList<Orders> Orders = gson.fromJson(json, listType);
        return Orders;
    }

    //OBJ array to JSON array
    public static String LISTtoJSON(ArrayList<Orders> Orders) {
        Gson gson = new Gson();
        String JSON = gson.toJson(Orders);
        return JSON;
    }
}