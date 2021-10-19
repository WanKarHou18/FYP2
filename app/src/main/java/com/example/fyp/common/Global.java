package com.example.fyp.common;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.domain.Customer;
import com.example.fyp.domain.DocumentPreferences;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Sub_Orders;
import com.example.fyp.domain.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Global {
    //For House Wifi :  "http://192.168.0.121/";
    //For Mobile Data:"http://192.169.43.162/"
    //For Web Host:http://frenz2021.000webhostapp.com/
    private static String URL ="http://frenz2021.000webhostapp.com/";
    public static SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");

    public static String getURL() {
        return URL;
    }
    public static void setURL(String URL) {
        Global.URL = URL;
    }

    // Object
    //Object--------Login , Register
    public static User user;

    public static Sub_Orders sub_order ;
    public static Orders order;
    public static ArrayList<Sub_Orders> sub_orders = new ArrayList<>();
    public static ArrayList<Orders> orders = new ArrayList<>();

    //String------Customer(Home Fragment)
    public static String printing_type_availability_json;
    //String-------PrinterProfileFragment
    public static String printer_json;

    //String,--------Printer(Home Fragment)
    public static String printer_select_order_json;

    //String,-Profile(PrinterProfileFragment)
    public static String printer_business_setting_json;

    //String,-------PrinterReceivedOrderMainActivity
    public static String order_selected_json;

    //String , ArrayList<> --- File Uploads in CustOrderSecondActivity
    public static ArrayList<Uri> FileURLList = new ArrayList();
    public static ArrayList<String> FileNameList = new ArrayList();
    public static Uri FileURL = null;
    public static String FileRealPath = null;
    public static String FileName =null;
    public static String FileType=null;
    public static int PageCount=0;

    //String,------CustomersViewOrderList
    public static String cust_view_orders_json;
    public static String cust_view_selected_orders_json;
    public static String cust_view_selected_sub_order_json;

    //String,------(Printer) home->HomeFragment
    public  static String printer_view_orders_json;
    public static  String printer_view_selected_orders_json;
    public static String printer_view_selected_sub_order_json;

    //Function
    //**Calculate distance
    public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }
    //Convert Address to Long Lat
    public static LatLng getLongLatFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    //=============================
    //  TOAST DESIGN
    //=============================
    public static void displayToast(Context context, String message,int length,String color){
        Toast toast = Toast.makeText(context,
                message,length);

        /*
        View toastView = toast.getView();
        if(color.equals("red")){
            toastView.setBackgroundResource(R.drawable.toast_background_red);
        }else if(color.equals("yellow")){
            toastView.setBackgroundColor(R.drawable.toast_background_yellow);
        }else if(color.equals("green")){
            toastView.setBackgroundResource(R.drawable.toast_background_green);
        }else if(color.equals("blue")){
            toastView.setBackgroundResource(R.drawable.toast_background_blue);
        }

         */

       // textView.setTextSize(30);
         toast.show();

    }

}
