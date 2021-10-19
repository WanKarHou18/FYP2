package com.example.fyp.ui_admin.admin_viewRatingFeedback;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;

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

public class admin_viewPrinterRatingProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //VIEW
    private TextView tv_apRatingProfile_userID;
    private TextView  tv_apRatingProfile_hp;
    private TextView tv_apRatingProfile_name;
    private TextView tv_apRatingProfile_bankRef;
    private TextView tv_apRatingProfile_address;
    private TextView tv_apRatingProfile_email;
    private TextView tv_apRatingProfile_rating;
    private Button btn_apRatingProfile_approve;
    private Button btn_apRatingProfile_call;

    //OBJECT
    private Intent intent;
    private Rating rating;

    //DATA
    private String rating_json="";
    private String printerName="";
    private String userID="";
    private String printerAddress ="";
    private String printerHp="";
    private String bankRef="";
    private String printerEmail="";
    private String rating_value="";

    private int cust_userID;
    private String cust_hp;

    private String action;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_printer_rating_profile);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        tv_apRatingProfile_address = findViewById(R.id.tv_apRatingProfile_userAddress);
        tv_apRatingProfile_bankRef=findViewById(R.id.tv_apRatingProfile_bankRef);
        tv_apRatingProfile_email = findViewById(R.id.tv_apRatingProfile_email);
        tv_apRatingProfile_hp = findViewById(R.id.tv_apRatingProfile_hp);
        tv_apRatingProfile_name = findViewById(R.id.tv_apRatingProfile_name);
        tv_apRatingProfile_userID=findViewById(R.id.tv_apRatingProfile_userID);
        tv_apRatingProfile_rating=findViewById(R.id.tv_apRatingProfile_rating);

        btn_apRatingProfile_approve=findViewById(R.id.btn_apRatingProfile_approve );
        btn_apRatingProfile_call =findViewById(R.id.btn_apRatingProfile_Call);

    }

    private void initiateData() {
        intent = getIntent();
        rating_json  = intent.getStringExtra("rating_json");
        rating = Rating.JSONToOBJ(rating_json);
        rating_value = intent.getStringExtra("avgRatingValue");

        userID = String.valueOf(rating.getPrinter().getUser().getUser_id());
        bankRef =rating.getPrinter().getUser().getBank_references();
        printerName = rating.getPrinter().getUser().getUsername();
        printerEmail=rating.getPrinter().getUser().getUser_email();
        printerAddress=rating.getPrinter().getUser().getUser_address();
        printerHp=rating.getPrinter().getUser().getUser_hp();

        cust_hp = rating.getCustomer().getUser().getUser_hp();
        cust_userID= rating.getCustomer().getUser().getUser_id();

    }

    private void createLayoutView() {
        tv_apRatingProfile_userID.setText(printerName);
        tv_apRatingProfile_name.setText(printerName);
        tv_apRatingProfile_hp.setText(printerHp);
        tv_apRatingProfile_bankRef.setText(bankRef);
        tv_apRatingProfile_address.setText(printerAddress);
        tv_apRatingProfile_email.setText(printerEmail);
        tv_apRatingProfile_rating.setText(rating_value);

        btn_apRatingProfile_approve.setText(rating.getCustomer().getUser().getStatus());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void ViewListener() {
        btn_apRatingProfile_call.setOnClickListener(this::onClick);
        btn_apRatingProfile_approve.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
       if(view.getId()==R.id.btn_apFeedbackProfile_approve){

           JSONObject data = new JSONObject();
           try {
               data.put("user_id",cust_userID);
           } catch (JSONException e) {
               e.printStackTrace();
           }

           AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker = new AdminCRUDUserAccessBackgroundWorker();
           adminCRUDUserAccessBackgroundWorker.execute("update",data.toString());
       }else if(view.getId()==R.id.btn_apFeedbackProfile_Call){
           Intent callIntent = new Intent(Intent.ACTION_CALL);
           callIntent.setData(Uri.parse(cust_hp));
           startActivity(callIntent);
       }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AdminCRUDUserAccessBackgroundWorker extends AsyncTask<String,Void,String[]> {
        public AdminCRUDUserAccessBackgroundWorker() { }


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
            String UserURL = Global.getURL()+"CRUD_AdminBlockOrApprovedUsers.php";


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
                    System.out.println(response.getData());
                    btn_apRatingProfile_approve.setText(rating.getCustomer().getUser().getStatus());
                }else{
                    System.out.println("WARN:CANT UPDATE");
                }
            }
        }

    }

}