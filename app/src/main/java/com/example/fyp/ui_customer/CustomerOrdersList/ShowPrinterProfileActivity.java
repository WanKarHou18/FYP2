package com.example.fyp.ui_customer.CustomerOrdersList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.fyp.profile.PrinterProfileFragment;
import com.example.fyp.ui_customer.Orders.CustOrderMainActivity;
import com.example.fyp.ui_customer.feedback.FeedbackActivity;
import com.example.fyp.ui_customer.home.HomeViewModel;

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

public class ShowPrinterProfileActivity extends AppCompatActivity implements View.OnClickListener{

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
    private com.example.fyp.domain.businessSetting businessSetting;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_printer_profile);
        verifyPermissions();
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }


    private void linkXML() {
        btn_feedback=findViewById(R.id.btn_feedback_SPPA);
        btn_complaint =findViewById(R.id.btn_complaint_SPPA);
        tvUserName=findViewById(R.id.tvUsername_SPPA);
        tvUserEmail = findViewById(R.id.tv_email_SPPA);
        tvRating = findViewById(R.id.tv_rating_SPPA);
        ratingBar = (RatingBar) findViewById(R.id.rbar_printer_SPPA);
        iv_phone =findViewById(R.id.iv_phone_SPPA);
    }

    private void initiateData() {

        printer_json= Global.printer_json;
        printer = Printer.JSONToOBJ(printer_json);

        user = Global.user;

        getPrinterRating();
    }

    private void createLayoutView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvUserName.setText(printer.getUser().getUsername());
        //tvUserAddress.setText(printer.getUser().getUser_address());
        tvUserEmail.setText(printer.getUser().getUser_email());

    }

    private void ViewListener() {
        btn_feedback.setOnClickListener(this::onClick);
        btn_complaint.setOnClickListener(this::onClick);
        iv_phone.setOnClickListener(this::onClick);

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_feedback_SPPA){
            rating_value = (float) ratingBar.getRating(); // get rating number from a rating bar
            createOrUpdateRating();
            try {
                Thread.sleep(500);
                Intent intent = new Intent(ShowPrinterProfileActivity.this, FeedbackActivity.class);
                startActivity(intent);
                // this.getActivity().finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

          this.finish();

        }else if(view.getId()==R.id.btn_complaint_SPPA){
            rating_value = (float) ratingBar.getRating(); // get rating number from a rating bar
            createOrUpdateRating();
            createComplaintAlertBox();
        }else if(view.getId()==R.id.iv_phone_SPPA){
            Uri number = Uri.parse("tel:"+printer.getUser().getUser_hp());
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowPrinterProfileActivity.this);
        builder.setTitle("Write your Complaint:");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(ShowPrinterProfileActivity.this).inflate(R.layout.alert_box_file_complaint,null, false);

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
                    Toast.makeText(ShowPrinterProfileActivity.this,"Please write out your complaint",Toast.LENGTH_SHORT).show();
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
        int permissionCallPhone = ActivityCompat.checkSelfPermission(ShowPrinterProfileActivity.this, Manifest.permission.CALL_PHONE);

        int permissionExternalMemory = ActivityCompat.checkSelfPermission(ShowPrinterProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);


        int permissionLocation= ActivityCompat.checkSelfPermission(ShowPrinterProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ShowPrinterProfileActivity.this,
                    CALL_PERMISSIONS,
                    1
            );
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                createOrUpdateRating();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            dialog = new ProgressDialog(ShowPrinterProfileActivity.this);
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
                    Toast.makeText(ShowPrinterProfileActivity.this,"Your complaint received by admin",Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("Cant fetch data from server");
                    dialog.dismiss();
                }
            }else{
                dialog.dismiss();
            }
        }

    }

}