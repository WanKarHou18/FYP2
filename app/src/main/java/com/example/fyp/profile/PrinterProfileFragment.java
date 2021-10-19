package com.example.fyp.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Availability;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;
import com.example.fyp.domain.businessSetting;
import com.example.fyp.ui_customer.Orders.CustOrderMainActivity;
import com.example.fyp.ui_customer.Orders.CustOrderSecondActivity;
import com.example.fyp.ui_customer.feedback.FeedbackActivity;
import com.example.fyp.ui_customer.home.HomeViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//TODO: set phone number on image view ,view type of printing , rating and feedback function
//'boolean java.util.ArrayList.equals(java.lang.Object)' on a null object reference when press on the order button

public class PrinterProfileFragment extends Fragment implements  View.OnClickListener {

    //View
    private Button btnOrder;
    private Button btn_feedback;
    private Button btn_complaint;
    private View root;
    private TextView tvUserName;
    private TextView tv_printing_type_display;
    private TextView tvUserAddress;
    private HomeViewModel homeViewModel;
    private TextView tvUserEmail;
    private RatingBar ratingBar;
    private TextView tvRating;
    private ImageView iv_phone;
    private ProgressDialog dialog;


    //Object
    private Printer printer ;
    private User user;
    private businessSetting businessSetting;
    private ArrayList<Rating> ratingArrayList = new ArrayList<>();
    private Availability availability;

    //Data
    private String action;
    private String data;
    private String printer_json;
    private double rating_value=0;
    private double avg_rating = 0;
    private float ttl_rating=0;
    private int respondent_num=0;
    private String tvRatingDisplayString;
    private static final String[] CALL_PERMISSIONS = {Manifest.permission.CALL_PHONE };

    public PrinterProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_printer_profile, container, false);
        verifyPermissions();
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void linkXML() {
       btnOrder = root.getRootView().findViewById(R.id.btnMakeOrder);
       btn_feedback=root.getRootView().findViewById(R.id.btn_feedback);
       btn_complaint =root.getRootView().findViewById(R.id.btn_complaint);
        tvUserName=root.getRootView().findViewById(R.id.tvUsername);
        tvUserAddress=root.getRootView().findViewById(R.id.tv_address_profile_display);
        tvUserEmail = root.getRootView().findViewById(R.id.tv_email);
        tvRating = root.getRootView().findViewById(R.id.tv_rating);
        tv_printing_type_display=root.getRootView().findViewById(R.id.tv_printing_type_display);
        ratingBar = (RatingBar) root.getRootView().findViewById(R.id.rbar_printer);
        iv_phone =root.getRootView().findViewById(R.id.iv_phone);
    }

    private void initiateData() {

        availability = Availability.JSONToOBJ(Global.printing_type_availability_json);
        printer_json= Global.printer_json;
        printer = Printer.JSONToOBJ(printer_json);

        user = Global.user;

        getPrinterBusinessSetting();
        getPrinterRating();
    }


    private void createLayoutView() {
        tvUserName.setText(printer.getUser().getUsername());
        tvUserAddress.setText(printer.getUser().getUser_address());
        tvUserEmail.setText(printer.getUser().getUser_email());
        setPrintingTypeTextView();

    }

    private void ViewListener() {
            btnOrder.setOnClickListener(this::onClick);
            btn_feedback.setOnClickListener(this::onClick);
            btn_complaint.setOnClickListener(this::onClick);
            iv_phone.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btnMakeOrder) {
                rating_value = (float) ratingBar.getRating(); // get rating number from a rating bar

                //createOrUpdateRating();
            try {
                Thread.sleep(500);
                Intent intent = new Intent(getActivity(), CustOrderMainActivity.class);
                //intent.putExtra("printer_json", Printer.OBJtoJSON(printers.get(position)));
                startActivity(intent);
                // this.getActivity().finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            }else if(view.getId()==R.id.btn_feedback){
            rating_value = (float) ratingBar.getRating(); // get rating number from a rating bar
            createOrUpdateRating();
            try {
                Thread.sleep(500);
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                // this.getActivity().finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.getActivity().finish();

        }else if(view.getId()==R.id.btn_complaint){
            rating_value = (float) ratingBar.getRating(); // get rating number from a rating bar
            //createOrUpdateRating();
            createComplaintAlertBox();
        }else if(view.getId()==R.id.iv_phone){
            Uri number = Uri.parse("tel:"+printer.getUser().getUser_hp());
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        }

    }

    private void setPrintingTypeTextView() {
            StringBuilder stringBuilder = new StringBuilder();

            if(availability.getDoc_available().equals("Yes")) {
                stringBuilder.append("Document Printing");
            }else{
                stringBuilder.append("-");
            }
            if(availability.getImg_available().equals("Yes")){
                stringBuilder.append(",Photo Printing");
            }else{
                stringBuilder.append(",-");
            }
            tv_printing_type_display.setText(stringBuilder.toString());
    }

    //====================================
    //Fetch data of rating,business setting
    //=====================================


    private void getPrinterRating(){
        //Create json for "data"
        JSONObject data =new JSONObject();
        try {
            data.put("cust_id", user.getCustomer().getCustId());
            data.put("printer_id", printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PrinterRatingBW printerRatingBW = new PrinterRatingBW();
        printerRatingBW.execute("read",data.toString());
    }

    private void getPrinterBusinessSetting() {
        JSONObject data = new JSONObject();
        try {
            data.put("printer_id", printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editBusinessSettingBW editBusinessSettingBW= new editBusinessSettingBW();
        editBusinessSettingBW.execute("read",data.toString());
    }


    //====================================
    //Functions for Rating
    //=====================================


    private void checkRatingExist(){
        if(ratingArrayList.size()!=0) {
            ttl_rating=0;
            avg_rating=0;
            respondent_num=0;
            for (int i = 0; i < ratingArrayList.size(); i++) {
                //Calculate average rating
                rating_value =Double.valueOf(ratingArrayList.get(i).getrating_value()).floatValue();
                ttl_rating += rating_value;

                if (ratingArrayList.get(i).getCust_id().equals(user.getCustomer().getCustId()) &&
                        ratingArrayList.get(i).getPrinter_id().equals(String.valueOf(printer.getPrinter_id()))) {
                    //The rating existed for specific cust and printer, then set it at Rating Bar
                    ratingBar.setRating(Float.valueOf(ratingArrayList.get(i).getrating_value()));

                }
            }
            //Average rating
            avg_rating = ttl_rating / ratingArrayList.size();
            avg_rating =Math.round(avg_rating * 100.0) / 100.0;
            //Respondents number
            //*Every row in Table:Rating is unique for certain printer.
            respondent_num = ratingArrayList.size();
        }
    }
    private void createOrUpdateRating() {
        //Create json for "data"

        int update =0;
        JSONObject data =new JSONObject();
        try {
            data.put("cust_id", user.getCustomer().getCustId());
            data.put("printer_id", printer.getPrinter_id());
            data.put("rating_value",rating_value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(ratingArrayList.size()!=0) {
            for (int i = 0; i < ratingArrayList.size(); i++) {
                if (ratingArrayList.get(i).getCust_id().equals(user.getCustomer().getCustId()) &&
                        ratingArrayList.get(i).getPrinter_id().equals(String.valueOf(printer.getPrinter_id()))) {
                    //Update the Database
                    update=1;
                    break;
                }

            }
        }

        if(update==1){
            PrinterRatingBW printerRatingBW = new PrinterRatingBW();
            printerRatingBW.execute("update", data.toString());
        }else{
            PrinterRatingBW printerRatingBW = new PrinterRatingBW();
            printerRatingBW.execute("create", data.toString());
        }
    }

    //====================================
    //Functions for Complaint Alert Box
    //=====================================

    /* Alert Box for Complaint */
    private void createComplaintAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Write your Complaint:");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.alert_box_file_complaint,(ViewGroup) getView(), false);

        // Set up the input
        EditText et_sendComplaint = (EditText) viewInflated.findViewById(R.id.et_sendComplaint);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               //Input json exp: '{"cust_id":"4","printer_id":"4","complaint_content":"Services are shit!!!"}';
                String complaint_content = et_sendComplaint.getText().toString();
                if(complaint_content.equals(null)){
                    Toast.makeText(getContext(),"Please write out your complaint",Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("cust_id", user.getCustomer().getCustId());
                        data.put("printer_id", printer.getPrinter_id());
                        data.put("complaint_content", complaint_content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println(data.toString());
                    CustomerComplaintBW customerComplaintBW = new CustomerComplaintBW();
                    customerComplaintBW.execute("create", data.toString());
                }
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Requesting Permission
    private void verifyPermissions(){
        int permissionCallPhone = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        int permissionExternalMemory = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);


        int permissionLocation= ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    CALL_PERMISSIONS,
                    1
            );
        }


    }




    //====================================
    //Background Workers
    //=====================================

    private class PrinterRatingBW extends AsyncTask<String,Void,String[]> {
        public PrinterRatingBW() { }
        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        // In this method we have to do background operation on background thread.
        // Operations in this method should not touch on any mainthread activities or fragments.
        protected String[] doInBackground(String... params) {
            action = params[0];
            data = params[1];
            String UserURL = Global.getURL()+"CRUD_customer_CustomerRating.php";


            try {
                URL url = new URL(UserURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String post_data =
                        URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8") +"&"
                                +URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String final_result = sb.toString().trim();
                return new String[]{"connection success",final_result};
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (IOException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (Exception e){
                e.printStackTrace();
                return new String[]{"connection fail"};
            }
        }
        @Override
        //While doing background operation,
        // if you want to update some information on UI, we can use this method.
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        //In this method we can update ui of background operation result.
        protected void onPostExecute(String[] result) {
            if(result[0].equals("connection success")){
                System.out.println(result[1]);
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    System.out.println("Rating :"+response.getData());
                    ratingArrayList = Rating.JSONToLIST(response.getData());
                    checkRatingExist();
                    tvRatingDisplayString = avg_rating + "/" + respondent_num;
                    tvRating.setText(tvRatingDisplayString);



                }else{
                    System.out.println("Cant fetch data from server");
                }
            }
        }

    }

    private class CustomerComplaintBW extends AsyncTask<String,Void,String[]> {
        public CustomerComplaintBW() {
            dialog = new ProgressDialog(getActivity());
        }
        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        // In this method we have to do background operation on background thread.
        // Operations in this method should not touch on any mainthread activities or fragments.
        protected String[] doInBackground(String... params) {
            action = params[0];
            data = params[1];
            String UserURL = Global.getURL()+"CRUD_customer_CustomerFileComplaint.php";


            try {
                URL url = new URL(UserURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String post_data =
                        URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8") +"&"
                                +URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String final_result = sb.toString().trim();
                return new String[]{"connection success",final_result};
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (IOException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (Exception e){
                e.printStackTrace();
                return new String[]{"connection fail"};
            }
        }
        @Override
        //While doing background operation,
        // if you want to update some information on UI, we can use this method.
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        //In this method we can update ui of background operation result.
        protected void onPostExecute(String[] result) {
            if(result[0].equals("connection success")){
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    dialog.dismiss();
                    Toast.makeText(getContext(),"Your complaint received by admin",Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("Cant fetch data from server");
                    dialog.dismiss();
                }
            }else{
                dialog.dismiss();
            }
        }

    }
    //Background worker to fetch printer Business Setting

    private class editBusinessSettingBW extends AsyncTask<String,Void,String[]> {
        public editBusinessSettingBW() { }


        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        // In this method we have to do background operation on background thread.
        // Operations in this method should not touch on any mainthread activities or fragments.

        protected String[] doInBackground(String... params) {
            String action = params[0];
            String data = params[1];
            String UserURL = Global.getURL()+"CRUD_printer_businessSetting.php";


            try {
                URL url = new URL(UserURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String post_data =
                        URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode(action,"UTF-8") +"&"
                                +URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String final_result = sb.toString().trim();
                return new String[]{"connection success",final_result};
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (IOException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (Exception e){
                e.printStackTrace();
                return new String[]{"connection fail"};
            }
        }
        @Override
        //While doing background operation,
        // if you want to update some information on UI, we can use this method.
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        //In this method we can update ui of background operation result.
        protected void onPostExecute(String[] result) {
            if(result[0].equals("connection success")){
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    System.out.println("Business Setting"+response.getData());
                    businessSetting = businessSetting.JSONToOBJ(response.getData());
                    Global.printer_business_setting_json = businessSetting.OBJtoJSON(businessSetting);

                }else{
                    System.out.println("Nothing");
                }
            }
        }


    }



}
