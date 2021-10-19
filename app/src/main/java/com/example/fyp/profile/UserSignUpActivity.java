package com.example.fyp.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // View
    private EditText etUserEmail;
    private EditText etUserPassword;
    private EditText etUserAddress;
    private EditText etUserPhoneNum;
    private EditText etUserUsername;
    private EditText et_signup_PBE;
    private RadioGroup rgUserRole;
    private RadioButton rbRoleSelected;
    private Button btUserReg;
    private ProgressDialog progressDialog;

    private TextView tv_signup_username_errorMessage,tv_signup_userEmail_errorMessage,tv_signup_userhp_errorMessage,tv_signup_address_errorMessage,tv_signup_password_errorMessage,tv_signup_PBE_errorMessage;

    // Data
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String status = "Approved";
    private String bank_references="default";

    private String role_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        sharedPreferences = getSharedPreferences("login_status", 0);
        editor = sharedPreferences.edit();

        linkXML();
        createLayoutView();
        ViewListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void linkXML() {
        etUserUsername =findViewById(R.id.etUserUsername);
        etUserEmail= findViewById(R.id.etUserEmail);
        etUserPassword=findViewById(R.id.etUserPassword);
        etUserAddress= findViewById(R.id.etUserAddress);
        etUserPhoneNum=findViewById(R.id.etUserhp);
        et_signup_PBE=findViewById(R.id.et_signup_PBE);

        tv_signup_username_errorMessage=findViewById(R.id.tv_signup_username_errorMessage);
        tv_signup_userEmail_errorMessage=findViewById(R.id.tv_signup_userEmail_errorMessage);
        tv_signup_userhp_errorMessage=findViewById(R.id.tv_signup_userhp_errorMessage);
        tv_signup_address_errorMessage=findViewById(R.id.tv_signup_address_errorMessage);
        tv_signup_password_errorMessage=findViewById(R.id.user_signup_password_errorMessage);
        tv_signup_PBE_errorMessage =findViewById(R.id.tv_signup_PBE_errorMessage);

        rgUserRole=(RadioGroup)findViewById(R.id.rgUserRole);

        btUserReg=findViewById(R.id.btUserReg);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void createLayoutView() {

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_signup_PBE.setVisibility(View.GONE);
        tv_signup_PBE_errorMessage.setVisibility(View.GONE);
    }

    private void ViewListener() {


        btUserReg.setOnClickListener(this);
        rgUserRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_printer:
                        et_signup_PBE.setVisibility(View.VISIBLE);
                        tv_signup_PBE_errorMessage.setVisibility(View.VISIBLE);

                        break;
                    case R.id.rb_customer:
                            et_signup_PBE.setVisibility(View.GONE);
                            tv_signup_PBE_errorMessage.setVisibility(View.GONE);

                        break;

                }
            }
        });



    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btUserReg:
                    setRgUserRole(rgUserRole);
                    registerUser();
                break;


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


    private void setRgUserRole(View v){
        int selectedId = rgUserRole.getCheckedRadioButtonId();
        rbRoleSelected = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){
            role_selected = "Nothing";
        } else{
            if((selectedId)==R.id.rb_customer){
                role_selected = "customer";
            } else{
                role_selected = "printer";
            }
        }

    }

        // handle empty username, email and password
//        if(TextUtils.isEmpty(user_email) || android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches() == false){
//            progressDialog.dismiss();
//            Toast.makeText(this,"Please enter valid email",Toast.LENGTH_LONG).show();
//        }


    private void registerUser() {

        String user_email= etUserEmail.getText().toString().trim();
        String username = etUserUsername.getText().toString().trim();
        String user_password= etUserPassword.getText().toString().trim();
        String user_hp = etUserPhoneNum.getText().toString().trim();
        String user_address = etUserAddress.getText().toString().trim();
        String user_role = role_selected;
        bank_references= et_signup_PBE.getText().toString().trim();

        tv_signup_PBE_errorMessage.setText("");
        tv_signup_userhp_errorMessage.setText("");
        tv_signup_address_errorMessage.setText("");
        tv_signup_password_errorMessage.setText("");
        tv_signup_username_errorMessage.setText("");
        tv_signup_userEmail_errorMessage.setText("");



//        progressDialog = new ProgressDialog(UserSignUpActivity.this);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();


        //String username,String user_email,String user_role,String user_address,String user_hp, String user_password
        if(validateForm(username,user_email, user_role,user_address,user_hp, user_password,bank_references)) {


            //Create json to send to php
            JSONObject data = new JSONObject();
            try {
                data.put("username", username);
                data.put("user_email", user_email);
                data.put("user_role", user_role);
                data.put("user_address", user_address);
                data.put("user_hp", user_hp);
                data.put("user_password", user_password);
                data.put("status", status);
                data.put("bank_references", bank_references);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(data.toString());
            UserRegisterWorker UserRegisterWorker = new UserRegisterWorker();
            UserRegisterWorker.execute(data.toString());
        }else{
            Global.displayToast(this,"Please fill in everything correctly",Toast.LENGTH_SHORT,"yellow");
        }
    }


    private boolean validateForm(String username,String user_email,String user_role,String user_address,String user_hp, String user_password,String PBE){
        boolean valid = false;
        int error =0;

        // handle empty username, email and password
        if(TextUtils.isEmpty(user_email) || android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches() == false){
            tv_signup_userEmail_errorMessage.setText("Please enter valid email");
            error+=1;
        }
        if(username.length()<6){
            tv_signup_username_errorMessage.setText("Username too short, enter minimum 6 characters");
            error+=1;
        }

        if(TextUtils.isEmpty(user_password)){
            tv_signup_password_errorMessage.setText("Please fill in username");
            error+=1;
        }

        if(user_password.length() < 6){
            tv_signup_password_errorMessage.setText("Password too short, enter minimum 6 characters");
            error+=1;
        }
        if(TextUtils.isEmpty(user_address)){
            tv_signup_address_errorMessage.setText("Please enter valid address");
            //Patterns.PHONE.matcher(user_hp).matches() == false
            error+=1;
        }
        if(TextUtils.isEmpty(user_hp)|| VerificationOfPhoneNumberFormat(user_hp)==0){
            tv_signup_userhp_errorMessage.setText("Please enter valid phone number");
            error+=1;
        }


        if(user_role.equals("default")){
            error+=1;
        }else{
            //Verification of PBE
            if(user_role.equals("printer")){
                if(VerificationOfPBENumber(PBE)==0){
                    tv_signup_PBE_errorMessage.setText("Must be number and minimum length of 10");
                    error+=1;
                }
            }
        }

        if(error==0){
            valid=true;
            System.out.println("TRUE HERE");
        }else{
            valid=false;
        }

        return valid;
    }

    private int VerificationOfPhoneNumberFormat(String sPhoneNumber){
        int valid =1;
        Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
        Matcher matcher = pattern.matcher(sPhoneNumber);

        if (matcher.matches()) {
            valid=1;
        }
        else
        {
            valid=0;
        }
        return valid;
    }

    private int VerificationOfPBENumber(String PBE){
        int valid =1;
        int error=0;

        if(PBE.length()>10){
            error+=1;
        }

        if(PBE.isEmpty()){
            error+=1;
        }

        if(!(PBE.matches("[0-9]+"))){
            error+=1;
        }

        if(error==0){
            valid=1;
        }else{
            valid=0;
        }

        return valid;
    }


    private class UserRegisterWorker extends AsyncTask<String, Void, String[]> {

        public UserRegisterWorker(){
            progressDialog = new ProgressDialog(UserSignUpActivity.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }

        protected String[] doInBackground(String... params) {
            try {
                String register_url = Global.getURL()+"user.php";
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =
                        URLEncoder.encode("action","UTF-8")+"="+URLEncoder.encode("register","UTF-8")+"&"
                                +URLEncoder.encode("data","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")
                               ;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String result;
                while ((result = bufferedReader.readLine()) != null) {
                    sb.append(result + "\n");
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

        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            // result[]
            // 0 - server connection status, 1 - return result [json object with message and data(json)]
            progressDialog.dismiss();

            // Check server connection
            // [ If server connection success then get return result, else display error server connection fail]
            if(result[0].equals("connection success")){
                Response response = Response.JSONToOBJ(result[1]);
                if (response!= null){
                    //
                    // If the username and password is correct and existing, logged in the user based on their role
                    //
                    if(response.getMessage().equals("Success")) {
                        System.out.println("REGISTER"+response.getMessage());
                        Global.displayToast(UserSignUpActivity.this, "Register successfully!", Toast.LENGTH_SHORT,"yellow");
                        editor.putBoolean("login_state", true);
                        editor.putString("userlogin_json",  response.getData());
                        editor.commit();
                          Global.user = User.JSONToOBJ(sharedPreferences.getString("userlogin_json", ""));
                        Intent intent;
                        intent = new Intent(UserSignUpActivity.this, UserLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        String login_mode = sharedPreferences.getString("login_mode", "Default");
//                        if(Global.user.getUser_role().equals("printer")){
//                            intent.putExtra("login_mode",login_mode);
//                        } else {
//                            intent.putExtra("login_mode",sharedPreferences.getString("login_mode", "Customer"));
//                        }
                        startActivity(intent);
                        UserSignUpActivity.this.finish();
                    }else if(response.getMessage().equals("Email Duplicated")) {
                        Global.displayToast(UserSignUpActivity.this,"Please enter new email address",Toast.LENGTH_LONG,"yellow");
                    }
                        else
                     {
                        Global.displayToast(UserSignUpActivity.this, response.getMessage(), Toast.LENGTH_SHORT,"yellow");
                    }
                }
            } else if(result[0].equals("connection fail")){
                Global.displayToast(UserSignUpActivity.this, "Fail", Toast.LENGTH_SHORT,"red");
            }
        }
    }
}