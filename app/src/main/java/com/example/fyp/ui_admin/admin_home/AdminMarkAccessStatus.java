package com.example.fyp.ui_admin.admin_home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;


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

public class AdminMarkAccessStatus extends AppCompatActivity implements View.OnClickListener {

    //Data
    private String user_json;

    //View
    private Button btn_admin_block;
    private Button btn_admin_approve;

    //Object
   Intent intent = getIntent();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mark_access_status);

        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

    }

    private void linkXML() {
            btn_admin_approve = (Button) findViewById(R.id.btn_admin_approved);
            btn_admin_block = (Button) findViewById(R.id.btn_admin_block);

    }

    private void initiateData() {
            user_json = intent.getStringExtra("user_json");

    }

    private void createLayoutView() {
    }

    private void ViewListener() {
    }

    public void onClick(View view) {
        if (view.getId()== R.id.btn_admin_approved) {
            System.out.println("HERE: Approved");

            user = User.JSONToOBJ(user_json);
            user.setStatus("Approved");
            user_json = User.OBJtoJSON(user);
            AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker = new AdminCRUDUserAccessBackgroundWorker();
            adminCRUDUserAccessBackgroundWorker.execute("update",user_json);
        }
        if (view.getId()== R.id.btn_admin_block) {
            System.out.println("HERE: Block");
            user = User.JSONToOBJ(user_json);
            user.setStatus("Blocked");
            user_json = User.OBJtoJSON(user);
            AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker = new AdminCRUDUserAccessBackgroundWorker();
            adminCRUDUserAccessBackgroundWorker.execute("update",user_json);
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
                    //ordersArrayList = new ArrayList<>();
                    System.out.println(response.getData());

                    // Intent i = new Intent(getActivity(),AdminMarkAccessStatus.class);
                    // startActivity(i);
                }else{
                    System.out.println("WARN:CANT UPDATE");
                }
            }
        }

    }
}