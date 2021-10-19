 package com.example.fyp.ui_customer.Orders;

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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Order_Address;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Payment;
import com.example.fyp.domain.Preparation_Status;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.Sub_Orders;
import com.example.fyp.domain.User;
import com.example.fyp.domain.businessSetting;
import com.example.fyp.profile.UserLoginActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.util.Calendar;
import java.util.Random;

 /*
     TODO:
     1.  Generate Code for folder in Firebase and upload file into it.
     2.  Cost of order
     3.  Cannot have duplicated name of file in one order.
     4.  Online Payment
  */
 public class CustOrderForthActivity extends AppCompatActivity implements
        View.OnClickListener {

    //View
    private RadioGroup rg_delivery;
    private RadioButton rb_delivery;
    private RadioButton rb_selfPick;
    private RadioGroup rg_payment;
    private RadioButton rb_CODselection;
    private RadioButton rb_onlinePaymentSelection;
    private ImageView iv_date,iv_time;
    private Button btn_continueOrder;
    private TextView txtDate, txtTime,tv_dateTimeDetails;
    private EditText et_newLoc;
    private CheckBox cb_own_loc;
    private ProgressDialog dialog;
    private LinearLayout ll_addressFillUp;
    private LinearLayout ll_banner_delivery;

    //Object
    private ArrayList<Orders> ordersArrayList ;
    private Orders order;
    private Order_Address order_address;
    private Payment payment;
    private Preparation_Status preparation_status;
    private Printer printer ;
    private ArrayList<Uri> FileURI = new ArrayList<>();
    private User user;
    private Sub_Orders sub_order;
    //**For Business Setting
    private businessSetting businessSetting;

    //Data
    private int mYear, mMonth, mDay, mHour, mMinute=0;
    private String Date = "default" ;
    private String Time   = "default" ;
    private String DateStr = "default" ;
    private String TimeStr = "default";
    private String DeliveryMode = "default" ;
    private String DateTimeDetails= "default";
    private String FileName= "default";
    private String beforeTime= "default";
    private String afterTime= "default";


    //**For Convert addres to LongLat
    private LatLng custLatLng, printerLatLng;

    //**Error Message
     private ArrayList<String> errorMessage = new ArrayList<>();

     //**Validation
     private int loc_timeValidation =0;

    //**For Order
    private String cost="default";
    private String orderID="default";
    private String orderReferenceNumber="default";
    //***For Order_Address
    private String order_addID = "default" ;
    private String distance =  "default";
    private String cust_loc = "default" ;
    private String printer_loc="default";
    private Uri document_URI;
    private String printer_address="default";

    //***For Payment
    private String paymentID= "default" ;
    private String paymentCost="default" ;
    private String paymentType = "default" ;
    private String paymentStatus ="Pending";

    //**For Preparation_Status
    private String prepStatusID ="default";
    private String prepStatus = "Pending";

   //**Others
    private String action="default";
    private String data="default";

    //**For Permissions
    private static final String[]  LOCATION_PERMISSIONS={
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_order_main_2);
        verifyPermissions();
        linkXML();
        initiateData();
        //createLayoutView();
        ViewListener();
    }

    private void linkXML() {

        iv_date=(ImageView) findViewById(R.id.iv_date);
        iv_time=(ImageView) findViewById(R.id.iv_time);
        btn_continueOrder = (Button)findViewById(R.id.btn_continueOrder);
        txtDate=(TextView)findViewById(R.id.tv_date);
        txtTime=(TextView) findViewById(R.id.tv_time);
        tv_dateTimeDetails =(TextView) findViewById(R.id.tv_banner_delivery);
        rg_delivery = (RadioGroup)findViewById(R.id.rg_delivery);
        rb_delivery =(RadioButton)findViewById(R.id.rb_delivery);
        rb_selfPick =(RadioButton)findViewById(R.id.rb_selfPick);
        cb_own_loc= (CheckBox) findViewById(R.id.cb_own_loc);
        et_newLoc = (EditText) findViewById(R.id.et_newLoc);
        rg_payment =(RadioGroup)findViewById(R.id.rg_payment);
        rb_CODselection=(RadioButton) findViewById(R.id.rb_CODselection);
        rb_onlinePaymentSelection=(RadioButton) findViewById(R.id.rb_onlinePaymentSelection);
        ll_addressFillUp=(LinearLayout) findViewById(R.id.ll_addressFillUp);
        ll_banner_delivery=(LinearLayout) findViewById(R.id.ll_banner_delivery);

    }

    private void initiateData() {
        printer = Printer.JSONToOBJ(Global.printer_json);
        order = Global.order;
        user = Global.user;
        sub_order=Global.sub_order;

        Intent intent = getIntent();
        paymentCost = intent.getExtras().getString("ttlcost");

        printer_loc = printer.getUser().getUser_address();

        JSONObject data = new JSONObject();
        try {
            data.put("printer_id",printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editBusinessSettingBW editBusinessSettingBW =  new editBusinessSettingBW();
        editBusinessSettingBW.execute("read",data.toString());
    }

    private void createLayoutView() {
        //When activity started , make it gone first
        ll_addressFillUp.setVisibility(View.GONE);
        ll_banner_delivery.setVisibility(View.GONE);

        if(!(businessSetting.getShipping_option().get(0).getDeliveryAvail().equals("Yes"))){
            rb_delivery.setVisibility(View.GONE);
        }

        if(!(businessSetting.getShipping_option().get(0).getSelfPickUpAvail().equals("Yes"))){
            rb_selfPick.setVisibility(View.GONE);
        }

        if(!(businessSetting.getPaymentSettings().get(0).getCOD().equals("Yes"))){
            rb_CODselection.setVisibility(View.GONE);
        }

        if(!(businessSetting.getPaymentSettings().get(0).getOnline_payment().equals("Yes"))){
            rb_onlinePaymentSelection.setVisibility(View.GONE);
        }
    }

    private void ViewListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iv_time.setOnClickListener(this);
        iv_date.setOnClickListener(this);
        btn_continueOrder.setOnClickListener(this);

        //If selected Delivery or Self Pick
        rg_delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_delivery:
                        DeliveryMode ="Delivery";

                        tv_dateTimeDetails.setVisibility(View.VISIBLE);
                        //WRITE ON tv_dateTimeDetails
                        DateTimeDetails = "Delivery service:"+"  "+beforeTime+"  "+" till "+afterTime;
                        tv_dateTimeDetails.setText(DateTimeDetails);
                        ll_banner_delivery.setVisibility(View.VISIBLE);
                        ll_addressFillUp.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_selfPick:
                        DeliveryMode = "SelfPick";
                        ll_banner_delivery.setVisibility(View.GONE);
                        ll_addressFillUp.setVisibility(View.GONE);
                        break;

                }
            }
        });
        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_COD:
                         paymentType="COD";

                        break;
                    case R.id.rb_onlinePaymentSelection:
                        paymentType="OnlinePayment";

                        break;

                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == iv_date) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);



            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {


                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            Date = txtDate.getText().toString();
                            DateStr= String.valueOf(dayOfMonth)+(monthOfYear+1)+year;
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == iv_time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                            TimeStr = String.valueOf(hourOfDay)+minute;

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v ==btn_continueOrder) {

            //***For Location
            //Handle select location
            cust_loc = Global.user.getUser_address();
            cust_loc = et_newLoc.getText().toString();

            //Verification of selection
            if(VerificationOfSelection()) {

                ////Verification of Delivery Location and Time
                if(DeliveryMode.equals("Delivery")){
                    errorMessage = ValidateLocationAndTime();
                    if(errorMessage.size()==0){
                        loc_timeValidation=1;
                    }else{
                        loc_timeValidation=0;
                    }
                }else{
                    loc_timeValidation=1;
                    distance =String.valueOf(0);

                }

                if (loc_timeValidation == 1) {

                    //Generate Order Reference Code
                    orderReferenceNumber = generateOrderReferenceCode(String.valueOf(printer.getPrinter_id()), user.getCustomer().getCustId(), sub_order.getResourcesRecord().getFile_type(), DateStr, TimeStr);

                    //***For Payment

                    payment = new Payment(paymentID, paymentCost, paymentType, paymentStatus, orderID);

                    //***For Preparation Status
                    preparation_status = new Preparation_Status(prepStatusID, prepStatus, orderID);

                    //**For Order Address
                    order_address = new Order_Address(order_addID, cust_loc, printer_loc, distance, orderID);

                    //***For Order
                    order.setOrderCodeReference(orderReferenceNumber);
                    order.setCustId(Global.user.getCustomer().getCustId());
                    order.setOrderDate(Date);
                    order.setTime(TimeStr);
                    order.setDeliveryMode(DeliveryMode);
                    order.setOrderStatus("Pending");
                    order.setPrinterId(String.valueOf(printer.getPrinter_id()));

                    order.setOrder_address(order_address);
                    order.setPayment(payment);
                    order.setPreparation_status(preparation_status);

                    System.out.println(Orders.OBJtoJSON(order));

                    CustomerOrderBackgroundWorker customerOrderBackgroundWorker = new CustomerOrderBackgroundWorker();
                    customerOrderBackgroundWorker.execute("create", Orders.OBJtoJSON(order));
                    createPaymentAlertBox();



                } else {
                    //Display all error message
                    for (int i = 0; i < errorMessage.size(); i++) {
                        Global.displayToast(this, errorMessage.get(i), Toast.LENGTH_SHORT,"red");
                    }
                }
            }else{
                Global.displayToast(this, "Please fill in everything correctly", Toast.LENGTH_SHORT,"yellow");
            }

        }
    }
  private ArrayList<String> ValidateLocationAndTime(){
        ArrayList<String> message = new ArrayList<String>();
        //Verification of customer location and delivery time

        for(int i=0;i<2;i++){
            if(i==0) {
                //First round is to check for validation of address
                if(rb_delivery.isChecked()) {
                    if (VerifyLocation(cust_loc) == 1) {
                        System.out.println("Location is valid");
                    } else {
                        message.add("Invalid Address Provided");
                        break;
                    }
                }
            }else{
                if(rb_delivery.isChecked()) {
                    //Check for distance and time
                    for(int j=0;j<2;j++) {
                        if(j==0) {
//                            Double distanceSetting=Double.parseDouble(businessSetting.getDelivery_zone().get(0).getDeliveryZoneDist());
//
//                            if (CalculateDistance(cust_loc, printer_loc)<=distanceSetting) {
//
//                                    distance = String.valueOf(CalculateDistance(cust_loc, printer_loc));
//
//                            }else{
//                                double distanceDiff = (distanceSetting-CalculateDistance(cust_loc, printer_loc))*-1;
//                                String error_message = "Your location is "+distanceDiff+" km from printer delivery zone";
//                                message.add(error_message);
//                            }
                        }else{
                            if (CheckValidDeliveryTime(TimeStr, beforeTime, afterTime) == 1) {
                            }else{
                                String error_message = "Time selected not in delivery time zone";
                                message.add(error_message);
                            }
                        }
                    }
                }else{
                }
            }
        }

        return message;

  }


  private int CheckValidDeliveryTime(String selected_time, String start_Time, String end_Time){
      int valid =0;
      int selectedTime = Integer.parseInt(selected_time);
      int startTime = Integer.parseInt(start_Time);
      int endTime=Integer.parseInt(end_Time);
      if((selectedTime>=startTime)&&(selectedTime<=endTime)){
          valid=1;
      }else{
          valid =0;
      }
      return valid;
  }

  private double CalculateDistance(String cust_loc,String printer_loc){
        double distance=0.00;
        //Convert address to coordinate
        custLatLng = Global.getLongLatFromAddress(this,cust_loc);
        printerLatLng = Global.getLongLatFromAddress(this,printer_loc);

        distance = Global.distanceBetween(custLatLng,printerLatLng);
        return distance;
  }

  private int VerifyLocation(String address){
        int valid =0;
        if(cb_own_loc.isChecked()){
            valid=1;
        }else{
            if(Global.getLongLatFromAddress(this,address)!=null) {
                System.out.println("Address@" + Global.getLongLatFromAddress(this, address));
                valid = 1;
            }
        }

        return valid;
  }

  private boolean VerificationOfSelection(){
        boolean valid = true;
        if(DeliveryMode!="default"){
            if(DeliveryMode=="Delivery"){
                //For Delivery Validation
                if((paymentType!="default")&&(cust_loc!="default")
                        &&(printer_loc!="default")&&(Date!="default")&&(TimeStr!="default")){
                    valid = true;
                }else{
                    valid=false;
                }
            }else{
                //For self Pick Validation
                if((paymentType!="default") &&(printer_loc!="default")&&(Date!="default")&&(TimeStr!="default")) {
                    valid = true;
                }else{
                    valid=false;
                }
            }
        }else{
            valid = false;
        }
      return  valid;

  }

  private String generateOrderReferenceCode(String printer_id,String customer_id,String FileType,String Date,String Time){
      String orderReferenceCode = "";
      String FileCode="";

      //Generate Random String
      Random generator = new Random();
      StringBuilder randomAlpabet = new StringBuilder();
      int randomLength = generator.nextInt(3);
      char tempChar;
      for (int i = 0; i < randomLength; i++){
          tempChar = (char) (generator.nextInt(96) + 32);
          randomAlpabet.append(tempChar);
      }

      //Get File Type Code
      if(FileType.equals("Image")){
          FileCode="IMG";
      }else{
          FileCode="DOC";
      }
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(printer_id);
      stringBuilder.append(randomAlpabet);
      stringBuilder.append(customer_id);
      stringBuilder.append(FileCode);
      stringBuilder.append(Date);
      stringBuilder.append(Time);

      orderReferenceCode=stringBuilder.toString();
      return orderReferenceCode;
  }

  private void UploadFileToFirebase(){
        String foldername="";
        Uri FileURL=null;

      //========================================================
      // Create folder and add file to Firebase Storage
      //========================================================
      for(int i=0;i<order.getSub_orders().size();i++){
          int position=0;
          //Get the location of file link from list
          for(int j=0;j<Global.FileNameList.size();j++){
              position = j;
              if(order.getSub_orders().get(i).getResourcesRecord().getfile_name().equals(Global.FileNameList.get(j))){
                  break;
              }
          }
          document_URI = Global.FileURLList.get(position);
          FileName = order.getSub_orders().get(i).getResourcesRecord().getfile_name();

          //CREATE FolderName
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(order.getOrderId());
          stringBuilder.append("@");
          stringBuilder.append(order.getSub_orders().get(i).getSubOrderId());
          stringBuilder.append("@");
          stringBuilder.append(order.getSub_orders().get(i).getResourcesRecord().getresources_id());

          foldername=stringBuilder.toString();

          //Store it in firebase
          StorageReference Folder = FirebaseStorage.getInstance().getReference()
                  .child("FYP").child("Document of printer").child(foldername);

          final StorageReference file_name = Folder.child(order.getSub_orders().get(i).getResourcesRecord().getfile_name());
          FileURL = Global.FileURLList.get(position);
          file_name.putFile(FileURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {

                      }
                  });

              }
          });

      }

      /*
                FileURI = Global.FileURLList;
                FileName=Global.FileName;

                for(int i=0;i<FileURI.size();i++) {
                    document_URI = FileURI.get(i);
                    StorageReference Folder = FirebaseStorage.getInstance().getReference()
                            .child("FYP").child("Printer Document").child(String.valueOf(printer.getPrinter_id()));
                       final StorageReference file_name = Folder.child(FileName);
                    file_name.putFile(document_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                }
                        });

                    }
                    });
                }
                
       */

  }
  private void createPaymentAlertBox(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Payment Reminder");

      LayoutInflater inflater = this.getLayoutInflater();
      View dialogView = inflater.inflate(R.layout.alert_box_payment_reminder, null);
      builder.setView(dialogView);

      TextView tv_benefitAccNo =(TextView) dialogView.findViewById(R.id.tv_benefitAccNo);
      TextView tv_paymentDetails=(TextView) dialogView.findViewById(R.id.tv_paymentDetails);

      tv_benefitAccNo.setText(printer.getUser().getBank_references());

      //Create Payment Details
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("FRENZ:");
      stringBuilder.append(orderReferenceNumber);

      tv_paymentDetails.setText(stringBuilder.toString());
      // Set up the buttons
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              //Bring back to home fragement page
              Intent i = new Intent(CustOrderForthActivity.this, UserMainActivity.class);
              i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);

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

  private void clearVariable(){
      //Clear the sub_orders of order

  }

  //Request Permissions
  private void verifyPermissions(){
      int permissionLocation= ActivityCompat.checkSelfPermission(CustOrderForthActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);


      if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(
                  CustOrderForthActivity.this,
                  LOCATION_PERMISSIONS,
                  1
          );
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
    private class CustomerOrderBackgroundWorker extends AsyncTask<String,Void,String[]> {
        public CustomerOrderBackgroundWorker() {
            dialog = new ProgressDialog(CustOrderForthActivity.this);
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
            String UserURL = Global.getURL()+"CRUD_customer_makeOrder.php";

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
                    if(action.equals("create")) {
                       // Global.cust_view_orders_json = result[1];
                        //Update the order
                        ordersArrayList = Orders.JSONToLIST(response.getData());
                        order = ordersArrayList.get(0);
                        UploadFileToFirebase();
                        dialog.dismiss();
                        if(paymentType.equals("OnlinePayment")) {
                            createPaymentAlertBox();
                            if (order != null) {
                                order.getSub_orders().clear();
                                Global.FileName = null;
                                Global.FileURLList = null;
                                Global.sub_order = null;
                                Global.order =null;
                                Global.sub_order=null;
                            }
                        }else{
                            if (order != null) {
                                order.getSub_orders().clear();
                                Global.FileName = null;
                                Global.FileURLList = null;
                                Global.sub_order = null;
                                Global.order =null;
                                Global.sub_order=null;
                            }
                            //Bring back to home fragement page
                            Intent i = new Intent(CustOrderForthActivity.this, UserMainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }



                    }
                }
            }
        }

    }

    //Background worker to fetch printer Business Setting

    private class editBusinessSettingBW extends AsyncTask<String,Void,String[]> {
        public editBusinessSettingBW() {

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
                    System.out.println(response.getData());
                    businessSetting = businessSetting.JSONToOBJ(response.getData());
                    initiateVariable();
                    createLayoutView();



                }else{
                    System.out.println("Nothing");
                }
            }
        }

        private void initiateVariable() {
            afterTime=businessSetting.getDelivery_time_setting().get(0).getDeliveryTimeEnd();
            beforeTime=businessSetting.getDelivery_time_setting().get(0).getDeliveryTimeStart();
        }

    }
}