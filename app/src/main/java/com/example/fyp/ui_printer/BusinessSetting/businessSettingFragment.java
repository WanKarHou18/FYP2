package com.example.fyp.ui_printer.BusinessSetting;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;
import com.example.fyp.domain.businessSetting;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.adminViewRatingFragment;

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


public class businessSettingFragment extends Fragment implements View.OnClickListener {
    //View
    private View root;
    private EditText et_bS_startTime;
    private EditText et_bS_endTime;
    private EditText et_bS_distance;
    private CheckBox cb_bS_delivery;
    private CheckBox cb_bS_selfPick;
    private CheckBox cb_bS_rejectBlurry;
    private CheckBox cb_bS_rejectAdult;
    private CheckBox cb_bS_rejectPPI;
    private CheckBox cb_onlinePayment;
    private CheckBox cb_CODsetting;
    private Button btn_bS_save;

    private TextView tv_endTime_errorMessage,tv_startTime_errorMessage,tv_distance_errorMessage;

    private ProgressDialog dialog;
    //Object
    private businessSetting businessSetting;
    private User user;



    //Data
    private String delivery;
    private String selfPick;
    private String startTime;
    private String endTime;
    private String distance;
    private String reject_blurry;
    private String reject_adult;
    private String reject_size;
    private String COD;
    private String onlinePayment;

    private String data;
    private String action;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_setting, container, false);
        linkXML();
        initiateData();
     //   createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        cb_bS_delivery = root.getRootView().findViewById(R.id.cb_bS_delivery);
        cb_bS_selfPick = root.getRootView().findViewById(R.id.cb_bS_selfPick);
        cb_bS_rejectAdult  = root.getRootView().findViewById(R.id.cb_bS_rejectAdult);
        cb_bS_rejectBlurry  = root.getRootView().findViewById(R.id.cb_bS_RejectBlurry);
        cb_bS_rejectPPI = root.getRootView().findViewById(R.id.cb_bS_rejectPPI);
        cb_onlinePayment=root.getRootView().findViewById(R.id.cb_onlinePaymentSetting);
        cb_CODsetting=root.getRootView().findViewById(R.id.cb_CODsetting);

        et_bS_distance  = root.getRootView().findViewById(R.id.et_bS_distance);
        et_bS_endTime = root.getRootView().findViewById(R.id.et_bS_endTime);
        et_bS_startTime  = root.getRootView().findViewById(R.id.et_bS_startTime);

        tv_endTime_errorMessage = root.getRootView().findViewById(R.id.tv_endTime_errorMessage);
        tv_startTime_errorMessage = root.getRootView().findViewById(R.id.tv_startTime_errorMessage);
        tv_distance_errorMessage = root.getRootView().findViewById(R.id.tv_distance_errorMessage);

        btn_bS_save  = root.getRootView().findViewById(R.id.btn_bS_save);
    }

    private void initiateData() {
        user = Global.user;

        //Create data
        JSONObject data = new JSONObject();
        try {
            data.put("user_id", user.getUser_id());
            data.put("printer_id",user.getPrinter().getPrinter_id());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        editBusinessSettingBW editBusinessSettingBW = new editBusinessSettingBW();
        editBusinessSettingBW.execute("read",data.toString());

    }

    private void createLayoutView() {

        //If 1 means not first time login
        if(VerificationOfAllEmpty()==1) {
            if (delivery.equals("Yes")) {
                cb_bS_delivery.setChecked(true);
                et_bS_startTime.setText(startTime);
                et_bS_endTime.setText(endTime);
                et_bS_distance.setText(distance);
            }else if(delivery.equals("default")){
                et_bS_startTime.setText("");
                et_bS_endTime.setText("");
                et_bS_distance.setText("");
            } else{
                et_bS_startTime.setVisibility(View.GONE);
                et_bS_endTime.setVisibility(View.GONE);
                et_bS_distance.setVisibility(View.GONE);
            }

            if (selfPick.equals("Yes")) {
                cb_bS_selfPick.setChecked(true);
            }

            if (reject_size.equals("Yes")) {
                cb_bS_rejectPPI.setChecked(true);
            }
            if (reject_blurry.equals("Yes")) {
                cb_bS_rejectBlurry.setChecked(true);
            }
            if (reject_adult.equals("Yes")) {
                cb_bS_rejectAdult.setChecked(true);
            }

            //Payment Setting
            if (COD.equals("Yes")) {
                cb_CODsetting.setChecked(true);
            }
            if (onlinePayment.equals("Yes")) {
                cb_onlinePayment.setChecked(true);
            }
        }else{
            et_bS_distance.setText("");
            et_bS_endTime.setText("");
            et_bS_startTime.setText("");

            et_bS_startTime.setVisibility(View.GONE);
            et_bS_endTime.setVisibility(View.GONE);
            et_bS_distance.setVisibility(View.GONE);
        }

    }
    private void ViewListener() {
        btn_bS_save.setOnClickListener(this::onClick);

        cb_bS_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    et_bS_startTime.setVisibility(View.VISIBLE);
                    et_bS_endTime.setVisibility(View.VISIBLE);
                    et_bS_distance.setVisibility(View.VISIBLE);

                    et_bS_startTime.setText(startTime);
                    et_bS_endTime.setText(endTime);
                    et_bS_distance.setText(distance);
                }else{
                    et_bS_startTime.setVisibility(View.GONE);
                    et_bS_endTime.setVisibility(View.GONE);
                    et_bS_distance.setVisibility(View.GONE);

                    tv_endTime_errorMessage.setVisibility(View.GONE);
                    tv_startTime_errorMessage.setVisibility(View.GONE);
                    tv_distance_errorMessage.setVisibility(View.GONE);
                }
            }
        }
        );
    }


    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_bS_save) {
            fetchAllData();
            if(VerificationOfSelection()) {
                businessSetting.getShipping_option().get(0).setDeliveryAvail(delivery);
                businessSetting.getShipping_option().get(0).setSelfPickUpAvail(selfPick);
                businessSetting.getDelivery_time_setting().get(0).setDeliveryTimeStart(startTime);
                businessSetting.getDelivery_time_setting().get(0).setDeliveryTimeEnd(endTime);
                businessSetting.getDelivery_zone().get(0).setDeliveryZoneDist(distance);
                businessSetting.getAdvanced_feature_setting().get(0).setDetectAdult(reject_adult);
                businessSetting.getAdvanced_feature_setting().get(0).setDetectBlurry(reject_blurry);
                businessSetting.getAdvanced_feature_setting().get(0).setDetectSize(reject_size);
                businessSetting.getPaymentSettings().get(0).setOnline_payment(onlinePayment);
                businessSetting.getPaymentSettings().get(0).setCOD(COD);

                data = businessSetting.OBJtoJSON(businessSetting);
                System.out.println(data);
                editBusinessSettingBW editBusinessSettingBW = new editBusinessSettingBW();
                editBusinessSettingBW.execute("update", data);
            }else{
                Global.displayToast(getContext(),"Please fill in everything correctly",Toast.LENGTH_SHORT,"yellow");
            }
        }

    }

    private void fetchAllData() {
        if(cb_bS_delivery.isChecked()){
            delivery ="Yes";
            startTime = et_bS_startTime.getText().toString();
            endTime = et_bS_endTime.getText().toString();
            distance = et_bS_distance.getText().toString();
        }else{
            delivery ="No";
        }
        if(cb_bS_selfPick.isChecked()){
            selfPick="Yes";
        }else{
            selfPick ="No";
        }
        if(cb_bS_rejectAdult.isChecked()){
            reject_adult ="Yes";
        }else{
            reject_adult="No";
        }
        if(cb_bS_rejectBlurry.isChecked()){
            reject_blurry ="Yes";
        }else{
            reject_blurry="No";
        }
        if(cb_bS_rejectPPI.isChecked()){
            reject_size="Yes";
        }else{
            reject_size="No";
        }
        if(cb_CODsetting.isChecked()){
            COD="Yes";
        }else{
            COD="No";
        }

        if(cb_onlinePayment.isChecked()){
            onlinePayment="Yes";
        }else{
            onlinePayment="No";
        }
    }

    private boolean VerificationOfSelection(){
        boolean valid = false;
        int error =0;

        if(COD.equals("Yes")||onlinePayment.equals("Yes")){
        }else{
            error+=1;
        }

        if(delivery.equals("Yes")||selfPick.equals("Yes")){
        }else{
            error+=1;
        }


        if(delivery.equals("Yes")) {
            if (VerificationOfDistanceAndTime() == 0) {
                error += 1;
            }
        }

        if(error==0){
            valid=true ;
        }else{
            valid=false;
        }
        return valid;
    }

    private int VerificationOfDistanceAndTime(){
        int valid=0;
        int error=0;
        int int_startTime=0;
        int int_endTime=0;
        double double_distance=0.0;
        //Verify if it is number
        if(startTime.isEmpty()||endTime.isEmpty()){
            if(startTime.isEmpty()){
                tv_startTime_errorMessage.setText("Please fill up");
            }
            if(endTime.isEmpty()){
                tv_endTime_errorMessage.setText("Please fill up");
            }
            error+=1;
        }else if((!(startTime.matches("[0-9]+")))||(!(endTime.matches("[0-9]+")))){
            if((!(startTime.matches("[0-9]+")))){
                tv_startTime_errorMessage.setText("Must be number and length of 4");
            }
            if(!(endTime.matches("[0-9]+"))){
                tv_endTime_errorMessage.setText("Must be number and length of 4");
            }
            error+=1;
        }else if(!(startTime.length()==4)||(!(endTime.length()==4))){
            //Verify it length
            if((!(startTime.length()==4))){
                tv_startTime_errorMessage.setText("Must be length of 4");
            }

            if((!(endTime.length()==4))){
                tv_endTime_errorMessage.setText("Must be length of 4");
            }
            error+=1;
        }else{
            //Verify if it is integer
            try {
                int_startTime= Integer.parseInt(startTime);
                //Verify if it is between 0 and 2400
                if((int_startTime>0)&&(int_startTime<2359)){

                }else{
                    tv_startTime_errorMessage.setText("Must be between 0000 and 2359");
                    error+=1;
                }
            } catch (NumberFormatException ignore) {
                error+=1;
            }

            try {
                int_endTime= Integer.parseInt(endTime);
                //Verify if it is between 0 and 2400
                if((int_endTime>0)&&(int_endTime<2400)){

                }else{
                    tv_endTime_errorMessage.setText("Must be between 0000 and 2359");
                    error+=1;
                }
            } catch (NumberFormatException ignore) {
                error+=1;
            }

            //Verify if endTime is bigger than startTime
            if(int_endTime>int_startTime){

            }else{
                Global.displayToast(getContext(),"End Time must be bigger than Start Time",Toast.LENGTH_SHORT,"yellow");
                error+=1;
            }
        }



        //Verify if distance is in double
        if(distance.isEmpty()) {
            tv_distance_errorMessage.setText("Please fill up");
            error+=1;
        }else{
            try {
                double_distance= Integer.parseInt(distance);

            } catch (NumberFormatException ignore) {
                tv_distance_errorMessage.setText("Invalid Input");
                error+=1;
            }
        }

        if(error==0){
            valid =1;
        }else{
            valid = 0;
        }
        return valid;
    }


    private class editBusinessSettingBW extends AsyncTask<String,Void,String[]> {
        public editBusinessSettingBW() {
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

                    if (action.equals("read")) {
                        //Action should be read
                        System.out.println(response.getData());
                        businessSetting = businessSetting.JSONToOBJ(response.getData());
                        initiateVariable();
                        VerificationOfAllEmpty();
                        createLayoutView();

                        dialog.dismiss();

                    } else {
                        dialog.dismiss();
                        Global.displayToast(getContext(), "Successfully saved", Toast.LENGTH_SHORT, "blue");

                    }
                }else{
                    //If action is update
                    Global.displayToast(getContext(), "Successfully saved", Toast.LENGTH_SHORT, "blue");

                }
            }
        }

    }
    private int VerificationOfAllEmpty(){
        int valid=0;
        if(((delivery.equals("default"))&&(endTime.equals("default"))&&(startTime.equals("default") )&&(distance.equals("default"))&&(selfPick.equals("default"))&&(reject_adult.equals("default"))&&(reject_blurry.equals("default"))&&(reject_size.equals("default") )&&(COD.equals("default"))&&(onlinePayment.equals("default")))){
            valid =0;
        }else{
            valid =1;
        }
        return valid;
    }
    private void initiateVariable(){
        delivery = businessSetting.getShipping_option().get(0).getDeliveryAvail();
        endTime =businessSetting.getDelivery_time_setting().get(0).getDeliveryTimeEnd();
        startTime = businessSetting.getDelivery_time_setting().get(0).getDeliveryTimeStart();
        distance = businessSetting.getDelivery_zone().get(0).getDeliveryZoneDist();
        selfPick = businessSetting.getShipping_option().get(0).getSelfPickUpAvail();
        reject_adult = businessSetting.getAdvanced_feature_setting().get(0).getDetectAdult();
        reject_blurry=businessSetting.getAdvanced_feature_setting().get(0).getDetectBlurry();
        reject_size =businessSetting.getAdvanced_feature_setting().get(0).getDetectSize();
        COD = businessSetting.getPaymentSettings().get(0).getCOD();
        onlinePayment = businessSetting.getPaymentSettings().get(0).getOnline_payment();


    }
}