package com.example.fyp.ui_customer.CustomerOrdersList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.Sub_Orders;
import com.example.fyp.ui_customer.Orders.CustOrderMainActivity;

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

public class CustViewOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //View
    private ListView lv_spDetails;
    private ListView lv_subOdersCVODA;
    private ListView lv_ordersDetailsCVODA;
    private TextView tv_currentStatus_CVODA;
    private ImageView iv_prepState;
    private ImageView iv_currentStatus;
    private TextView tv_prepStatusDescription;
    private TextView tv_nextStage;
    private TextView tv_nextStageStatus;
    private Fragment current_Fragment;
    private ImageButton ib_view_profile;

    private Button btn_A;
    private Button btn_B;

    //Object
    private Orders order;
    private ArrayList<Sub_Orders> sub_orders;
    private Printer printer;
    private ArrayList<ListViewData> ListViewDataPrinterDetails= new ArrayList<>();
    private ArrayList<ListViewData> ListViewDataOrderDetails= new ArrayList<>();
    private ListViewAdapter orderlistViewAdapter;
    private ListViewAdapter printerlistViewAdapter;
    private Sub_OrderListViewAdapter sub_orderListViewAdapter;

    //Data
    private String cust_view_selected_orders_json;
    private String paymentDetails;
    private String orderStatus;
    private Drawable res;
    private String uri;
    private int imageResource;
    private String new_prep_status;

    //MANAGER
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_view_order_detail);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        lv_spDetails = findViewById(R.id.lv_custDetails_CVODA);
        lv_subOdersCVODA = findViewById(R.id.lv_subOrders_CVODA);
        lv_ordersDetailsCVODA = findViewById(R.id.lv_orderDetails_CVODA);
        iv_prepState= findViewById(R.id.iv_prepState);
        iv_currentStatus = findViewById(R.id.iv_currentStatus);
        tv_currentStatus_CVODA = findViewById(R.id.tv_currentStatus_CVODA);

        tv_prepStatusDescription = findViewById(R.id.tv_prepStatusDescription);
        tv_nextStage = findViewById(R.id.tv_nextStage);
        tv_nextStageStatus = findViewById(R.id.tv_nextStageStatus);

        ib_view_profile = findViewById(R.id.ib_view_profile);
        btn_A = findViewById(R.id.btn_A_CVODA);
        btn_B = findViewById(R.id.btn_B_CVODA);

    }

    private void initiateData() {
        cust_view_selected_orders_json = Global.cust_view_selected_orders_json;
        System.out.println(cust_view_selected_orders_json);
        order = Orders.JSONToOBJ(cust_view_selected_orders_json);
        sub_orders = order.getSub_orders();

        printer = order.getPrinter();

        //Create data for listView Printer
        CreatePrinterListViewData();

        //Create data for listView Order
        CreateOrderListViewData();


    }

    private void createLayoutView() {
        //Create List View for Order
        orderlistViewAdapter = new ListViewAdapter(this,ListViewDataOrderDetails);

        //Create List View for Printer
        printerlistViewAdapter = new ListViewAdapter(this,ListViewDataPrinterDetails);

        //Create List View for Sub Orders
        sub_orderListViewAdapter = new Sub_OrderListViewAdapter(this,sub_orders);

        lv_spDetails.setAdapter(printerlistViewAdapter);
        lv_subOdersCVODA.setAdapter(sub_orderListViewAdapter);
        lv_ordersDetailsCVODA.setAdapter(orderlistViewAdapter);

        //Create Layout for order process
        CreateOrderProcessLayout();

    }

    private void ViewListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ib_view_profile.setOnClickListener(this::onClick);

        btn_A.setOnClickListener(this::onClick);
        btn_B.setOnClickListener(this::onClick);

        lv_subOdersCVODA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String sub_order_json = Sub_Orders.OBJtoJSON(sub_orders.get(position));
                Global.cust_view_selected_sub_order_json = sub_order_json;
                Intent i = new Intent(getApplicationContext(),CustViewSubOrderDetailsActivity.class);
                startActivity(i);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.FL_orderDetails, new CustViewSubOrderDetailsFragment());
//                ft.commit();

            }
        });
    }

    private void CreatePrinterListViewData() {
        for(int i=0;i<3;i++){
            ListViewData listViewdata =  new ListViewData("-","-");
            ListViewDataPrinterDetails.add(listViewdata);
        }
        ListViewDataPrinterDetails.get(0).setAll("Name",printer.getUser().getUsername());
        ListViewDataPrinterDetails.get(1).setAll("HP",printer.getUser().getUser_hp());
        ListViewDataPrinterDetails.get(2).setAll("Address",printer.getUser().getUser_address());
    }

    private void CreateOrderListViewData() {
        for(int i=0;i<7;i++){
            ListViewData listViewdata =  new ListViewData("-","-");
            ListViewDataOrderDetails.add(listViewdata);
        }
        ListViewDataOrderDetails.get(0).setAll("Order Ref Number", order.getOrderCodeReference());
        //ListViewDataOrderDetails.get(1).setAll("Status",order.getOrderStatus());
        ListViewDataOrderDetails.get(1).setAll("Date and Time",order.getOrderDate()+"      "+order.getTime());
        ListViewDataOrderDetails.get(2).setAll("Delivery Mode",order.getDeliveryMode());
        ListViewDataOrderDetails.get(3).setAll("Payment Method",order.getPayment().getPaymentType());
        if(order.getDeliveryMode().equals("Delivery")) {
            ListViewDataOrderDetails.get(4).setAll("Delivery Address",order.getOrder_address().getCustomer_address());
        }else{
            ListViewDataOrderDetails.get(4).setAll("Self Pick Address",order.getOrder_address().getPrinter_address());
        }


        if(order.getPayment().getPaymentType().equals("OnlinePayment")){
            paymentDetails = "FRENZ:"+order.getOrderCodeReference();
            ListViewDataOrderDetails.get(5).setAll("Beneficiary No",printer.getUser().getBank_references());
            ListViewDataOrderDetails.get(6).setAll("Payment Details",paymentDetails);
        }else{
            ListViewDataOrderDetails.remove(5);
            ListViewDataOrderDetails.remove(6);
        }
    }

    private void CreateOrderProcessLayout() {

        orderStatus = order.getPreparation_status().getPrep_status();
        if(orderStatus.equals("Pending")){

            //Set as Current order Prep Status
            tv_currentStatus_CVODA.setText("Pending");


            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            tv_nextStageStatus.setText("Accepted/Cancelled");
            tv_prepStatusDescription.setText("Waiting for service provider to accept your order");

            //Set drawable
             uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);

            btn_A.setVisibility(View.GONE);
            btn_B.setVisibility(View.GONE);



        }else if(orderStatus.equals("Accepted")){
            //Set as Current order Prep Statu"s
            tv_currentStatus_CVODA.setText("Accepted");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            //Set the next Stage Prep Status
            tv_nextStageStatus.setText("Printed Out");
            tv_prepStatusDescription.setText("Be patience while waiting service provider to print document out and mark status");

            //Set drawable
            uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);

            btn_A.setVisibility(View.GONE);
            btn_B.setVisibility(View.GONE);

        }else if(orderStatus.equals("Cancelled")){
            //Set as Current order Prep Statu"s
            tv_currentStatus_CVODA.setText("Cancelled");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            //Set the next Stage Prep Status
            tv_nextStage.setVisibility(View.GONE);
            tv_prepStatusDescription.setText("Oops! Look like this order had been cancelled");

            //Set drawable
            uri = "@drawable/ic_exclamation_mark";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);


            btn_A.setVisibility(View.GONE);
            btn_B.setVisibility(View.GONE);


        }else if(orderStatus.equals("Printed Out")){
            //Set as Current order Prep Status
            tv_currentStatus_CVODA.setText("Printed Out");
            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            if(order.getDeliveryMode().equals("SelfPick")){

                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("On the way");
                tv_prepStatusDescription.setText("Are you ready to pick your order at service provider location?");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setText("Yes");

            }else{
                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("On the way");
                tv_prepStatusDescription.setText("Be patience while waiting for service provider to deliver your order");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setVisibility(View.GONE);

            }



        }else if(orderStatus.equals("On the Way")){
            //Set as Current order Prep Status
            tv_currentStatus_CVODA.setText("On the Way");
            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            if(order.getDeliveryMode().equals("SelfPick")){

                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("Arrived");
                tv_prepStatusDescription.setText("Have you arrived service provider location?");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setText("Yes");

            }else{
                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("Arrived");
                tv_prepStatusDescription.setText("Be patience while waiting for service provider to arrive to your location");
                btn_A.setVisibility(View.GONE);
                btn_B.setVisibility(View.GONE);

            }


        }else if(orderStatus.equals("Arrived")){
            //Set as Current order Prep Status
            tv_currentStatus_CVODA.setText("Arrived");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);


            //Set the next Stage Prep Status
            tv_nextStageStatus.setText("Completed");
            tv_prepStatusDescription.setText("Have you received your order?");

            //Set drawable
            uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);

            btn_A.setVisibility(View.GONE);
            btn_B.setText("Yes");
           

        }else if(orderStatus.equals("Completed")){
            //Set as Current order Prep Status
            tv_currentStatus_CVODA.setText("Completed");

            //Set the current Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            //Set the next Stage Prep Status
            tv_nextStage.setVisibility(View.GONE);
            tv_nextStageStatus.setVisibility(View.GONE);
            tv_prepStatusDescription.setText("The process of this order is finished");

            //Set drawable
            uri = "@drawable/ic_celebrate";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);


            btn_A.setVisibility(View.GONE);
            btn_B.setVisibility(View.GONE);


        }

    }


    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_B_CVODA) {
            if(orderStatus.equals("Pending")){
            }else if(orderStatus.equals("Accepted")){
            }else if(orderStatus.equals("Cancelled")){
            }else if(orderStatus.equals("Printed Out")){
                //For next stage
                 orderStatus= "On the Way";
                JSONObject data = new JSONObject();
                try {
                    data.put("prep_status_id",order.getPreparation_status().getPrepStatusID());
                    data.put("prep_status", orderStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updatePreparationStatusBW updatePreparationStatusBW = new updatePreparationStatusBW();
                updatePreparationStatusBW.execute("update",data.toString());

                //Set as Current order Prep Status
                tv_currentStatus_CVODA.setText(orderStatus);
                //Set the next Stage Prep Status
                uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
                int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource1);
                iv_currentStatus.setImageDrawable(res);

                if(order.getDeliveryMode().equals("SelfPick")){

                    //Set the next Stage Prep Status
                    tv_nextStageStatus.setText("Arrived");
                    tv_prepStatusDescription.setText("Have you arrived service provider location?");

                    //Set drawable
                    uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    res = getResources().getDrawable(imageResource);
                    iv_prepState.setImageDrawable(res);

                    btn_A.setVisibility(View.GONE);
                    btn_B.setText("Yes");

                }else{
                    //Set drawable
                    uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    res = getResources().getDrawable(imageResource);
                    iv_prepState.setImageDrawable(res);

                    //Set the next Stage Prep Status
                    tv_nextStageStatus.setText("Arrived");
                    tv_prepStatusDescription.setText("Be patience while waiting for service provider to arrive to your location");
                    btn_A.setVisibility(View.GONE);
                    btn_B.setVisibility(View.GONE);

                }


            }else if(orderStatus.equals("On the Way")){
                //For next stage
                orderStatus="Arrived";
                JSONObject data = new JSONObject();
                try {
                    data.put("prep_status_id",order.getPreparation_status().getPrepStatusID());
                    data.put("prep_status", orderStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updatePreparationStatusBW updatePreparationStatusBW = new updatePreparationStatusBW();
                updatePreparationStatusBW.execute("update",data.toString());

                //Set as Current order Prep Status
                tv_currentStatus_CVODA.setText("Arrived");
                //Set the current Stage Prep Status
                uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
                int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource1);
                iv_currentStatus.setImageDrawable(res);


                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("Completed");
                tv_prepStatusDescription.setText("Have you received your order?");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setText("Yes");



            }else if(orderStatus.equals("Arrived")){
                orderStatus = "Completed";

                JSONObject data = new JSONObject();
                try {
                    data.put("prep_status_id",order.getPreparation_status().getPrepStatusID());
                    data.put("prep_status", orderStatus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updatePreparationStatusBW updatePreparationStatusBW = new updatePreparationStatusBW();
                updatePreparationStatusBW.execute("update",data.toString());

                //Set as Current order Prep Status
                tv_currentStatus_CVODA.setText("Completed");

                //Set the current Stage Prep Status
                uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
                int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource1);
                iv_currentStatus.setImageDrawable(res);


                //Set the next Stage Prep Status
                tv_nextStageStatus.setVisibility(View.GONE);
                tv_prepStatusDescription.setText("The process of this order is finished");

                //Set drawable
                uri = "@drawable/ic_celebrate";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setVisibility(View.GONE);

                //Back to previous  - UserMainActivity.class

                Intent i = new Intent(CustViewOrderDetailActivity.this, UserMainActivity.class);
                startActivity(i);




            }else{
                //Completed
            }


        }else if(view.getId()==R.id.btn_A_CVODA){

        }else if(view.getId()==R.id.ib_view_profile){
            Global.printer_json =Printer.OBJtoJSON(printer);
            Intent i = new Intent(CustViewOrderDetailActivity.this,ShowPrinterProfileActivity.class);
            startActivity(i);
        }
    }


    private class ListViewAdapter extends ArrayAdapter<ListViewData> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<ListViewData> listViewDataArr;

        public ListViewAdapter(Activity context, ArrayList<ListViewData> listViewDataArr) {
            super(context, R.layout.order_details_list, listViewDataArr);
            this.context = context;
            this.listViewDataArr = listViewDataArr;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.order_details_list, null);

            ListViewData listViewData = getItem(position);

            TextView pp_title= view.findViewById(R.id.pp_title1);
            TextView pp_subtitle = view.findViewById(R.id.pp_subtitle1);

            pp_title.setText(listViewData.getKey());
            pp_subtitle.setText(listViewData.getValue());
            return view;
        }

        @Override
        public int getCount() {
            return listViewDataArr.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }


    private class Sub_OrderListViewAdapter extends ArrayAdapter<Sub_Orders> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Sub_Orders> sub_orders;

        public Sub_OrderListViewAdapter(Activity context, ArrayList<Sub_Orders> sub_orders) {
            super(context, R.layout.sub_order_details_list, sub_orders);
            this.context = context;
            this.sub_orders = sub_orders;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.sub_order_details_list, null);

            Sub_Orders sub_orders = getItem(position);

            TextView tv_fileNameCVODA = view.findViewById(R.id.tv_fileNameCVODA);
            TextView tv_fileTypeCVODA = view.findViewById(R.id.tv_fileTypeCVODA);
            TextView tv_costCVODA = view.findViewById(R.id.tv_costCVODA);

            tv_fileNameCVODA.setText(sub_orders.getResourcesRecord().getfile_name());
            tv_fileTypeCVODA.setText(sub_orders.getResourcesRecord().getFile_type());
            tv_costCVODA.setText(sub_orders.getCost());


            return view;
        }

        @Override
        public int getCount() {
            return sub_orders.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    private class ListViewData{
        private String key;
        private String value;

        public ListViewData(){
        }
        public ListViewData(String key , String value){
            this.key = key;
            this.value = value;
        }
        public void setAll(String key , String value){
            this.key = key;
            this.value = value;
        }
        private String getKey(){
            return key;
        }
        private String getValue(){
            return value;
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

    //Background worker to update Preparation Status of an order

    private class updatePreparationStatusBW extends AsyncTask<String,Void,String[]> {
        public updatePreparationStatusBW() { }


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
            String UserURL = Global.getURL()+"CRUD_user_markPreparationStatus.php";


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
                    System.out.println("Nothing");
                }
            }
        }



    }
 /*
    private class OrderProcessListViewData{
        private String status;
        private String status_title;
        private String status_description;
        private String button_text;

        public OrderProcessListViewData(){
        }
        public OrderProcessListViewData(String status, String status_title , String status_description,String button_text){
            this.status = status;
            this.status_description = status_description;
            this.status_title= status_title;
            this.button_text =button_text;
        }
        public void setAll(String status, String status_title , String status_description){
            this.status = status;
            this.status_description = status_description;
            this.status_title= status_title;
            this.button_text=button_text;
        }
        private String getStatus(){
            return status;
        }
        private String getStatus_title(){return status_title;}
        private String getStatus_description(){return status_description;}
        private String getButton_text(){return button_text;}

        }

  */


/*
    private class OrderProcessListViewAdapter extends ArrayAdapter<ListViewData> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<ListViewData> listViewDataArr;

        public OrderProcessListViewAdapter(Activity context, ArrayList<ListViewData> listViewDataArr) {
            super(context, R.layout.order_process_list, listViewDataArr);
            this.context = context;
            this.listViewDataArr = listViewDataArr;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.order_process_list, null);

            ListViewData listViewData = getItem(position);

            TextView pp_title= view.findViewById(R.id.pp_title1);
            TextView pp_subtitle = view.findViewById(R.id.pp_subtitle1);

            pp_title.setText(listViewData.getKey());
            pp_subtitle.setText(listViewData.getValue());
            return view;
        }

        @Override
        public int getCount() {
            return listViewDataArr.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

 */
    /*

        if(order.getDeliveryMode().equals("SelfPick")){

            if (orderStatus.equals("Completed")) {
                status = "Current";
                status_description = "Order Completed";
                status_title = "Completed";
                button_text = null;
            }else{
                status = "In-Completed";
                status_description = "Order Completed";
                status_title = "Completed";
                button_text = null;
            }

            if(orderStatus.equals("Arrived")){
                status = "Current";
                status_description = "You had arrived destination.";
                status_title = "Arrived";
                button_text = null;
            }

            if(orderStatus.equals("OnTheWay")) {
                status = "Current";
                status_description = "On the way to service provider destination.";
                status_title = "OnTheWay";
                button_text = null;
            }

        }else{
            if(orderStatus.equals("Arrived")){
                status = "Current";
                status_description = "Service Provider had arrived your destination.";
                status_title = "Arrived";
                button_text = null;
            }

            if(orderStatus.equals("OnDelivery")){
                status = "Current";
                status_description = "Service provider is on the way to your location.";
                status_title = "On Delivery";
                button_text = null;
            }
        }


        if(orderStatus.equals("Printed Out")){
            status = "Current";
            status_description="Your order had been printed out by service provider";
            status_title="Pending";
            button_text =null;
        }


        if(orderStatus.equals("Accepted")){
            status = "Current";
            status_description="Your order had been accepted by service provider";
            status_title="Accepted";
            button_text =null;
        }

        //Pending
        if(orderStatus.equals("Pending")){
            status = "Current";
            status_description="Your order currently waiting for service provider to be accepted";
            status_title="Pending";
            button_text =null;

        }

     */
    }


