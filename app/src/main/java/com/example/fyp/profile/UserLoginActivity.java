package com.example.fyp.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.animation.splash;
import com.example.fyp.common.Global;
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

/*
TODO:
  1.Change the name of php
 */


public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private EditText etUserLoginUser;
    private EditText etUserLoginPassword;
    private TextView tvUserSignUp;
    private Button btnUserLoginEmail;
    private ProgressDialog progressDialog;

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private Toolbar toolbar;
    // Data
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        sharedPreferences = getSharedPreferences("login_status", 0);
        editor = sharedPreferences.edit();
        linkXML();
        ViewListener();
    }
    private void linkXML() {
        etUserLoginUser = findViewById(R.id.etUserLoginUser);
        etUserLoginPassword = findViewById(R.id.etUserLoginPassword);
        tvUserSignUp = findViewById(R.id.tvUserSignUp);
        btnUserLoginEmail = findViewById(R.id.btnUserLoginEmail);


    }
    private void ViewListener() {

        tvUserSignUp.setOnClickListener(this);
        btnUserLoginEmail.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvUserSignUp: {
                Intent i = new Intent(UserLoginActivity.this, UserSignUpActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btnUserLoginEmail: {
                LoginUserWithUserEmail();
                UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker();
                progressDialog.dismiss();
                break;
            }

        }

    }
    //
    //  Validate the email and password if the textview is empty
    private boolean validateForm(String email,String password){
        boolean valid = false;

        // handle empty username, email and password
        if(TextUtils.isEmpty(email)){
            Global.displayToast(this,"Please enter email and password",Toast.LENGTH_LONG,"yellow");
            progressDialog.dismiss();
        } else if(TextUtils.isEmpty(password)){
            Global.displayToast(this,"Please enter password",Toast.LENGTH_LONG,"yellow");
            progressDialog.dismiss();
        } else
            valid = true;

        return valid;
    }



    private void LoginUserWithUserEmail() {
        String user_email = etUserLoginUser.getText().toString().trim();
        String password = etUserLoginPassword.getText().toString().trim();

        progressDialog = new ProgressDialog(UserLoginActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        if(validateForm(user_email,password)) {
            UserBackgroundWorker UserBackgroundWorker=new UserBackgroundWorker();
            UserBackgroundWorker.execute("LoginUserWithUserEmail",user_email,password);
        }
    }
    public class UserBackgroundWorker extends AsyncTask<String,Void,String[]> {
        public UserBackgroundWorker() { }

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
            String login_type = params[0];
            String UserURL = Global.getURL()+"user.php";
//            String UserURL ="http://frenz2021.000webhostapp.com/user.php";
            // login_type, email, password
            if(login_type.equals("LoginUserWithUserEmail")) {
                String login_userEmail = params[1];
                String login_password = params[2];

                //Create json to send to php
                JSONObject data=new JSONObject();
                try {
                    data.put("user_email", login_userEmail);
                    data.put("user_password", login_password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                            URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode("login","UTF-8") +"&"
                                    +URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(data.toString(),"UTF-8");
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
                    return new String[]{"connection success",login_type,final_result};
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
            } else
                return new String[]{"connection fail"};

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
            // result[]
            // 0 - server connection status , 1 - worker type, 2 - return result [json object with message and data(json)]
            super.onPostExecute(result);
           // progressDialog.dismiss();
            //
            // Check server connection [ If server connection success then authenticate user, else display error server connection fail]
            //
            if(result[0].equals("connection success")){
                //
                // Check the username and password ( validate the authentication)
                //
                System.out.println(result[2]);
                if (result[1].equals("LoginUserWithUserEmail")) {
                    Response response = Response.JSONToOBJ(result[2]);
                    if (response!= null){
                        //
                        // If the username and password is correct and existing, logged in the user
                        //
                        if(response.getMessage().equals("Success")) {
                            System.out.println("LOGIN"+response.getMessage());
                            Global.displayToast(UserLoginActivity.this, "Login Success", Toast.LENGTH_SHORT,"green");
                            //
                            // Add in the json of Response to share preferences
                            //

                            editor.putString("userlogin_json", response.getData());
                            editor.commit();
                            Global.user = User.JSONToOBJ(sharedPreferences.getString("userlogin_json", ""));
                            Intent intent;
                            intent = new Intent(UserLoginActivity.this, UserMainActivity.class);
                            if(Global.user.getUser_role().equals("admin")){
                                intent.putExtra("login_role","admin");
                                startActivity(intent);
                                UserLoginActivity.this.finish();
                            } else if(Global.user.getUser_role().equals("customer")) {
                                intent.putExtra("login_role","customer");
                                startActivity(intent);
                                UserLoginActivity.this.finish();
                            }
                            else{

                                intent.putExtra("login_role","printer");
                                startActivity(intent);
                                UserLoginActivity.this.finish();
                            }


                        } else {
                            Global.displayToast(UserLoginActivity.this, "Login Fail", Toast.LENGTH_SHORT,"red");
                        }
                    }
                }
            } else if(result[0].equals("connection fail")){
                Global.displayToast(UserLoginActivity.this, "connection fail", Toast.LENGTH_SHORT,"red");

            }
        }
    }

}