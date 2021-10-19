package com.example.fyp.ui_customer.Setting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;
import com.example.fyp.profile.UserSignUpActivity;
import com.example.fyp.ui_customer.home.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    // View
    private View root;
    private Button btn_saveEditProfile;
    private EditText et_editProfileName;
    private EditText et_editEmail;
    private EditText et_editHomeAddress;
    private EditText et_editPhoneNumber;
    private EditText et_editPassword;
    private TextView user_name_display;
    private TextView user_email_display;

    private TextView tv_username_errorMessage,tv_emailAddress_errorMessage,tv_homeAddress_ErrorMessage,tv_hp_errorMessage,tv_password_errorMessage;

    private ProgressDialog progressDialog;

    private NavHostFragment navHostFragment;
    private NavController navController;
    private NavigationView navigationView;

    private ProgressDialog dialog;

    //Object
    private User user;
    private ArrayList<User> userArrayList = new ArrayList<>();

    //Data
    private String data;
    private String action;

    private String username;
    private String user_email;
    private String user_address;
    private String user_hp;
    private String user_password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

        return root;
    }

    private void linkXML() {
        btn_saveEditProfile = root.findViewById(R.id.btn_saveEditProfile);
        et_editEmail = root.findViewById(R.id.et_editEmail);
        et_editHomeAddress = root.findViewById(R.id.et_editHomeAddress);
        et_editPassword=root.findViewById(R.id.et_editPassword);
        et_editPhoneNumber=root.findViewById(R.id.et__editPhoneNumber);
        et_editProfileName = root.findViewById(R.id.et_editProfileName);

        tv_emailAddress_errorMessage= root.findViewById(R.id.tv_emailAddress_errorMessage);
        tv_homeAddress_ErrorMessage= root.findViewById(R.id.tv_homeAddress_ErrorMessage);
        tv_hp_errorMessage= root.findViewById(R.id.tv_hp_errorMessage);
        tv_password_errorMessage = root.findViewById(R.id.tv_password_errorMessage);
        tv_username_errorMessage= root.findViewById(R.id.tv_username_errorMessage);


//        navController = navHostFragment.getNavController();
//        navigationView = root.getRootView().findViewById(R.id.nav_view_admin);
//        user_name_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserName);
//        user_email_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserEmail);

    }

    private void initiateData() {
        user = Global.user;
        System.out.println(User.OBJtoJSON(user));
    }

    private void createLayoutView() {

        et_editPhoneNumber.setText(user.getUser_hp());
        et_editProfileName.setText(user.getUsername());
        et_editPassword.setText(user.getUser_password());
        et_editHomeAddress.setText(user.getUser_address());
        et_editEmail.setText(user.getUser_email());

//        user_name_display.setText(Global.user.getUsername());
//        user_email_display.setText(Global.user.getUser_email());

        }

    private void ViewListener() {
            btn_saveEditProfile.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_saveEditProfile) {
           username = et_editProfileName.getText().toString();
           user_email =et_editEmail.getText().toString();
           user_address=et_editHomeAddress.getText().toString();
           user_hp=et_editPhoneNumber.getText().toString();
           user_password = et_editPassword.getText().toString();

           tv_username_errorMessage.setText("");
           tv_hp_errorMessage.setText("");
           tv_homeAddress_ErrorMessage.setText("");
           tv_emailAddress_errorMessage.setText("");
           tv_password_errorMessage.setText("");

            if(validateForm(username, user_email,user_address, user_hp, user_password)) {
                System.out.println("BYPASSS");
                //Create data
                JSONObject data = new JSONObject();
                try {
                    data.put("user_id", user.getUser_id());
                    data.put("username", et_editProfileName.getText());
                    data.put("user_email", et_editEmail.getText());
                    data.put("user_role", user.getUser_role());
                    data.put("user_address", et_editHomeAddress.getText());
                    data.put("user_hp", et_editPhoneNumber.getText());
                    data.put("user_password", et_editPassword.getText());
                    data.put("status", user.getStatus());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(data.toString());
                custEditProfileBW custEditProfileBW = new custEditProfileBW();
                custEditProfileBW.execute("update", data.toString());
            }
        }
    }


    private boolean validateForm(String username,String user_email,String user_address,String user_hp, String user_password){
        boolean valid = false;
        int error =0;

        // handle empty username, email and password
        if(TextUtils.isEmpty(user_email) || android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches() == false){
            tv_emailAddress_errorMessage.setText("Please enter valid email");
            error+=1;
        }
        if(username.length()<6){
            tv_username_errorMessage.setText("Password too short, enter minimum 6 characters");
            error+=1;
        }

        if(TextUtils.isEmpty(user_password)){
            tv_username_errorMessage.setText("Please fill in username");
            error+=1;
        }

        if(user_password.length() < 6){
            tv_password_errorMessage.setText("Password too short, enter minimum 6 characters");
            error+=1;
        }
        if(TextUtils.isEmpty(user_address)){
            tv_homeAddress_ErrorMessage.setText("Please enter valid address");
            //Patterns.PHONE.matcher(user_hp).matches() == false
            error+=1;
        }
        if(TextUtils.isEmpty(user_hp)|| VerificationOfPhoneNumberFormat(user_hp)==0){
            tv_hp_errorMessage.setText("Please enter valid phone number");
            error+=1;
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

    private class custEditProfileBW extends AsyncTask<String,Void,String[]> {
        public custEditProfileBW() {
            dialog = new ProgressDialog(getActivity());
        }


        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("SAVING.");
            dialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        // In this method we have to do background operation on background thread.
        // Operations in this method should not touch on any mainthread activities or fragments.
        protected String[] doInBackground(String... params) {
            action = params[0];
            data = params[1];
            String UserURL = Global.getURL()+"CRUD_user_EditProfile.php";


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
                    userArrayList =User.JSONToLIST(response.getData());
                    user = userArrayList.get(0);
                    Global.user= user;
                    createLayoutView();
                    dialog.dismiss();
                    Global.displayToast(getContext(),"Successfully saved",Toast.LENGTH_SHORT,"green");
                }else if(response.getMessage().equals("Email Duplicated")) {
                    dialog.dismiss();
                    Global.displayToast(getContext(),"Please enter new email address",Toast.LENGTH_LONG,"yellow");
                }else{
                    dialog.dismiss();
                    System.out.println("Cant fetch data from server");
                }
            }
        }

    }

}