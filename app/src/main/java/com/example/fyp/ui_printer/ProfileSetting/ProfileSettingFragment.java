package com.example.fyp.ui_printer.ProfileSetting;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;
import com.example.fyp.ui_customer.Setting.EditProfileFragment;

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

public class ProfileSettingFragment extends Fragment implements View.OnClickListener {

    //VIEW
    private View root;
    private Button btn_printerSaveEditProfile;
    private EditText et_printerEditProfileName;
    private EditText et_printerEditEmail;
    private EditText et_printerEditHomeAddress;
    private EditText et_printerEditPhoneNumber;
    private EditText et_printerEditPassword;
    private EditText et_printerEditBankReferences;
    private TextView user_name_display;
    private TextView user_email_display;

    private ProgressDialog dialog;

    private TextView tv_printer_username_errorMessage,tv_printer_emailAddress_errorMessage,tv_printer_homeAddress_ErrorMessage,tv_printer_hp_errorMessage,tv_printer_password_errorMessage,tv_printer_PBE_errorMessage;

    //OBJECT
    private User user;
    private ArrayList<User> userArrayList = new ArrayList<>();

    //DATA
    private String data="";
    private String action="";


    private String username;
    private String user_email;
    private String user_address;
    private String user_hp;
    private String user_password;
    private String PBE;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.fragment_printer_setting, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

        return root;
    }

    private void linkXML() {
        btn_printerSaveEditProfile = root.getRootView().findViewById(R.id.btn_printerSaveEditProfile);
        et_printerEditEmail=root.getRootView().findViewById(R.id.et_printerEditEmail);
        et_printerEditHomeAddress=root.getRootView().findViewById(R.id.et_printerEditHomeAddress);
        et_printerEditPassword=root.getRootView().findViewById(R.id.et_printerEditPassword);
        et_printerEditPhoneNumber=root.getRootView().findViewById(R.id.et_printerEditPhoneNumber);
        et_printerEditProfileName=root.getRootView().findViewById(R.id.et_printerEditProfileName);
        et_printerEditBankReferences=root.getRootView().findViewById(R.id.et_printerEditBankReferences);

        tv_printer_emailAddress_errorMessage= root.findViewById(R.id.tv_printer_emailAddress_errorMessage);
        tv_printer_homeAddress_ErrorMessage= root.findViewById(R.id.tv_printer_homeAddress_ErrorMessage);
        tv_printer_hp_errorMessage= root.findViewById(R.id.tv_printer_hp_errorMessage);
        tv_printer_password_errorMessage = root.findViewById(R.id.tv_printer_password_errorMessage);
        tv_printer_username_errorMessage= root.findViewById(R.id.tv_printer_username_errorMessage);
        tv_printer_PBE_errorMessage= root.findViewById(R.id.tv_printer_PBE_errorMessage);

    }

    private void initiateData() {
        user = Global.user;
        System.out.println(User.OBJtoJSON(user));
    }

    private void createLayoutView() {
        et_printerEditProfileName.setText(user.getUsername());
        et_printerEditBankReferences.setText(user.getBank_references());
        et_printerEditPassword.setText(user.getUser_password());
        et_printerEditHomeAddress.setText(user.getUser_address());
        et_printerEditEmail.setText(user.getUser_email());
        et_printerEditPhoneNumber.setText(user.getUser_hp());

    }

    private void ViewListener() {
        btn_printerSaveEditProfile.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_printerSaveEditProfile) {
            username = et_printerEditProfileName.getText().toString();
            user_email =et_printerEditEmail.getText().toString();
            user_address=et_printerEditHomeAddress.getText().toString();
            user_hp=et_printerEditPhoneNumber.getText().toString();
            user_password = et_printerEditPassword.getText().toString();
            PBE =et_printerEditBankReferences.getText().toString();

            tv_printer_username_errorMessage.setText("");
            tv_printer_hp_errorMessage.setText("");
            tv_printer_homeAddress_ErrorMessage.setText("");
            tv_printer_emailAddress_errorMessage.setText("");
            tv_printer_password_errorMessage.setText("");
            tv_printer_PBE_errorMessage.setText("");
            if(validateForm(username, user_email,user_address, user_hp, user_password,PBE)){
                //Create data
                JSONObject data = new JSONObject();
                try {
                    data.put("user_id", user.getUser_id());
                    data.put("username",username );
                    data.put("user_email",user_email);
                    data.put("user_role", user.getUser_role());
                    data.put("user_address", user_address);
                    data.put("user_hp",user_hp);
                    data.put("user_password",user_password );
                    data.put("bank_references",PBE);
                    data.put("status", user.getStatus());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(data.toString());
                printerEditProfileBW printerEditProfileBW= new printerEditProfileBW();
                printerEditProfileBW.execute("update", data.toString());
            }
        }
    }

    private boolean validateForm(String username,String user_email,String user_address,String user_hp, String user_password,String PBE){
        boolean valid = false;
        int error =0;

        // handle empty username, email and password
        if(TextUtils.isEmpty(user_email) || android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches() == false){
            tv_printer_emailAddress_errorMessage.setText("Please enter valid email");
            error+=1;
        }
        if(username.length()<6){
            tv_printer_username_errorMessage.setText("Password too short, enter minimum 6 characters");
            error+=1;
        }

        if(TextUtils.isEmpty(username)){
            tv_printer_username_errorMessage.setText("Please fill in username");
            error+=1;
        }

        if(user_password.length() < 6){
            tv_printer_password_errorMessage.setText("Password too short, enter minimum 6 characters");
            error+=1;
        }
        if(TextUtils.isEmpty(user_address)){
            tv_printer_homeAddress_ErrorMessage.setText("Please enter valid address");
            //Patterns.PHONE.matcher(user_hp).matches() == false
            error+=1;
        }
        if(TextUtils.isEmpty(user_hp)|| VerificationOfPhoneNumberFormat(user_hp)==0){
            tv_printer_hp_errorMessage.setText("Please enter valid phone number");
            error+=1;
        }

        if(VerificationOfPBENumber(PBE)==0){
            tv_printer_PBE_errorMessage.setText("Must be number and minimum length of 10");
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


    private class printerEditProfileBW extends AsyncTask<String,Void,String[]> {
        public  printerEditProfileBW() {
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