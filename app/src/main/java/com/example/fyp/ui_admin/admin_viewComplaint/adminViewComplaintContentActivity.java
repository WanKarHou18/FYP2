package com.example.fyp.ui_admin.admin_viewComplaint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Comments;
import com.example.fyp.domain.Complaint;
import com.example.fyp.domain.Response;
import com.example.fyp.ui_customer.Orders.CustOrderForthActivity;

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

public class adminViewComplaintContentActivity extends AppCompatActivity implements View.OnClickListener {

    //View
    private TextView tv_spName;
    private TextView tv_spHP;
    private TextView tv_spAddress;
    private TextView  tv_spUserID;
    private TextView tv_spEmail;

    private TextView tv_custName_complaint;
    private TextView tv_custHP_complaint;
    private TextView tv_custAddress_complaint;
    private TextView tv_custEmail_complaint;
    private TextView tv_custUserID_complaint;
    private TextView tv_custCompalintContent;

    private Button btn_ignoreComplaint;
    private Button btn_blockSP;

    private ProgressDialog dialog;

    //Object
    private Complaint complaint;

    //Data
    private String complaint_json;

    //Extra
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_complaint_content);

        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        tv_spName = findViewById(R.id.tv_spName);
        tv_spHP   = findViewById(R.id.tv_spHP);
        tv_spAddress= findViewById(R.id.tv_spAddress);
        tv_spUserID= findViewById(R.id.tv_spUserID);
        tv_spEmail = findViewById(R.id.tv_spEmail);

        tv_custAddress_complaint= findViewById(R.id.tv_custAddress_complaint);
        tv_custCompalintContent= findViewById(R.id.tv_custComplaintContent);
        tv_custEmail_complaint= findViewById(R.id.tv_custEmail_complaint);
        tv_custHP_complaint= findViewById(R.id.tv_custHP_complaint);
        tv_custName_complaint= findViewById(R.id.tv_custName_complaint);
        tv_custUserID_complaint= findViewById(R.id.tv_custUserID_complaint);

        btn_blockSP = findViewById(R.id.btn_blockSP);
        btn_ignoreComplaint = findViewById(R.id.btn_ignoreComplaint);

    }

    private void initiateData() {
        intent = getIntent();
        complaint_json  = intent.getStringExtra("complaint_json");
        complaint= Complaint.JSONToOBJ(complaint_json);
    }

    private void createLayoutView() {
        //Set service provider details
        tv_spName.setText(complaint.getPrinter().getUser().getUsername());
        tv_spAddress.setText(complaint.getPrinter().getUser().getUser_address());
        tv_spEmail.setText(complaint.getPrinter().getUser().getUser_email());
        tv_spHP.setText(complaint.getPrinter().getUser().getUser_hp());
        tv_spUserID.setText(String.valueOf(complaint.getPrinter().getuser_id()));

        //Set customer details
        tv_custName_complaint.setText(complaint.getCustomer().getUser().getUsername());
        tv_custHP_complaint.setText(complaint.getCustomer().getUser().getUser_hp());
        tv_custEmail_complaint.setText(complaint.getCustomer().getUser().getUser_email());
        tv_custAddress_complaint.setText(complaint.getCustomer().getUser().getUser_address());
        tv_custUserID_complaint.setText(String.valueOf(complaint.getCustomer().getCustId()));
        tv_custCompalintContent.setText(complaint.getComplaint_content());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ViewListener() {
        btn_ignoreComplaint.setOnClickListener(this::onClick);
        btn_blockSP.setOnClickListener(this::onClick);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_blockSP) {

            //Change Status in complaint record
            JSONObject obj1 = new JSONObject();
            try {
                obj1.put("complaint_rec_id",complaint.getComplaint_rec_id());
                obj1.put("complaint_status","read");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomerComplaintBW customerComplaintBW = new CustomerComplaintBW();
            customerComplaintBW.execute("update",obj1.toString());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JSONObject obj = new JSONObject();
            try {
                obj.put("user_id",complaint.getPrinter().getUser().getUser_id());
                obj.put("status","Blocked");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker = new AdminCRUDUserAccessBackgroundWorker();
            adminCRUDUserAccessBackgroundWorker.execute("update",obj.toString());

            Intent  i = new Intent(adminViewComplaintContentActivity.this, UserMainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }else if(view.getId()== R.id.btn_ignoreComplaint) {
            //Change Status in complaint record
            JSONObject obj1 = new JSONObject();
            try {
                obj1.put("complaint_rec_id",complaint.getComplaint_rec_id());
                obj1.put("complaint_status","read");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomerComplaintBW customerComplaintBW = new CustomerComplaintBW();
            customerComplaintBW.execute("update",obj1.toString());

            Intent  i = new Intent(adminViewComplaintContentActivity.this, UserMainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
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
        public AdminCRUDUserAccessBackgroundWorker() {
            dialog = new ProgressDialog(adminViewComplaintContentActivity.this);
        }


        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Updating");
            dialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        // In this method we have to do background operation on background thread.
        // Operations in this method should not touch on any mainthread activities or fragments.
        protected String[] doInBackground(String... params) {
            String action = params[0];
            String data = params[1];
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
                    dialog.dismiss();

                }else{
                    System.out.println("WARN:CANT UPDATE");
                    dialog.dismiss();
                }
            }
        }

    }

    private class CustomerComplaintBW extends AsyncTask<String,Void,String[]> {
        public CustomerComplaintBW () {

        }


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
            String UserURL = Global.getURL()+"CRUD_admin_ViewComplaints.php";


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


                }else{

                    System.out.println("WARN:CANT UPDATE");
                }
            }
        }

    }
}