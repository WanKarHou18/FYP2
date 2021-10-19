package com.example.fyp.ui_printer.Order;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Customer;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.Sub_Orders;
import com.example.fyp.ui_customer.CustomerOrdersList.CustViewOrderDetailActivity;

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

public class PrinterViewOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    //View
    private ListView lv_custDetails;
    private ListView lv_subOdersPVODA;
    private ListView lv_ordersDetailsPVODA;
    private TextView tv_currentStatus_PVODA;
    private ImageView iv_prepState;
    private ImageView iv_currentStatus;
    private TextView tv_prepStatusDescription;
    private TextView tv_nextStage;
    private TextView tv_nextStageStatus;
    private Fragment current_Fragment;

    private Button btn_A;
    private Button btn_B;

    //Object
    private Orders order;
    private ArrayList<Sub_Orders> sub_orders;
    private Customer customer;
    private Printer printer;
    private ArrayList<ListViewData> ListViewDataCustomerDetails = new ArrayList<>();
    private ArrayList<ListViewData> ListViewDataOrderDetails = new ArrayList<>();
    private ListViewAdapter orderlistViewAdapter;
    private ListViewAdapter custlistViewAdapter;
    private Sub_OrderListViewAdapter sub_orderListViewAdapter;

    //Data
    private String printer_view_selected_orders_json;
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
        setContentView(R.layout.activity_printer_view_order_details);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        lv_custDetails = findViewById(R.id.lv_custDetails_PVODA);
        lv_subOdersPVODA = findViewById(R.id.lv_subOrders_PVODA);
        lv_ordersDetailsPVODA = findViewById(R.id.lv_orderDetails_PVODA);
        iv_prepState = findViewById(R.id.iv_prepState);
        iv_currentStatus = findViewById(R.id.iv_currentStatus);
        tv_currentStatus_PVODA = findViewById(R.id.tv_currentStatus_PVODA);

        tv_prepStatusDescription = findViewById(R.id.tv_prepStatusDescription);
        tv_nextStage = findViewById(R.id.tv_nextStage);
        tv_nextStageStatus = findViewById(R.id.tv_nextStageStatus);

        btn_A = findViewById(R.id.btn_A_PVODA);
        btn_B = findViewById(R.id.btn_B_PVODA);

    }

    private void initiateData() {
        printer_view_selected_orders_json = Global.printer_view_selected_orders_json;

        order = Orders.JSONToOBJ(printer_view_selected_orders_json);
        sub_orders = order.getSub_orders();
        customer= order.getCustomer();
        printer =order.getPrinter();


        //Create data for listView Printer
        CreatePrinterListViewData();

        //Create data for listView Order
        CreateOrderListViewData();


    }

    private void createLayoutView() {
        //Create List View for Order
        orderlistViewAdapter = new ListViewAdapter(this, ListViewDataOrderDetails);

        //Create List View for Printer
        custlistViewAdapter = new ListViewAdapter(this, ListViewDataCustomerDetails);

        //Create List View for Sub Orders
        sub_orderListViewAdapter = new Sub_OrderListViewAdapter(this, sub_orders);

        lv_custDetails.setAdapter(custlistViewAdapter);
        lv_subOdersPVODA.setAdapter(sub_orderListViewAdapter);
        lv_ordersDetailsPVODA.setAdapter(orderlistViewAdapter);

        //Create Layout for order process
        CreateOrderProcessLayout();

    }

    private void ViewListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_A.setOnClickListener(this::onClick);
        btn_B.setOnClickListener(this::onClick);

        lv_subOdersPVODA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String sub_order_json = Sub_Orders.OBJtoJSON(sub_orders.get(position));
                Global.printer_view_selected_sub_order_json = sub_order_json;
                System.out.println(sub_order_json);
                Intent i = new Intent(getApplicationContext(), PrinterViewSubOrderDetailsActivity.class);
                startActivity(i);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.FL_orderDetails, new CustViewSubOrderDetailsFragment());
//                ft.commit();

            }
        });
    }

    private void CreatePrinterListViewData() {
        for (int i = 0; i < 3; i++) {
            ListViewData listViewdata = new ListViewData("-", "-");
            ListViewDataCustomerDetails.add(listViewdata);
        }
        ListViewDataCustomerDetails.get(0).setAll("Name", customer.getUser().getUsername());
        ListViewDataCustomerDetails.get(1).setAll("HP", customer.getUser().getUser_hp());
        ListViewDataCustomerDetails.get(2).setAll("Address", customer.getUser().getUser_address());
    }

    private void CreateOrderListViewData() {
        for (int i = 0; i < 7; i++) {
            ListViewData listViewdata = new ListViewData("-", "-");
            ListViewDataOrderDetails.add(listViewdata);
        }
        ListViewDataOrderDetails.get(0).setAll("Order Ref Number", order.getOrderCodeReference());
        ListViewDataOrderDetails.get(1).setAll("Status", order.getPreparation_status().getPrep_status());
        ListViewDataOrderDetails.get(2).setAll("Date and Time", order.getOrderDate() + "      " + order.getTime());
        ListViewDataOrderDetails.get(3).setAll("Delivery Mode", order.getDeliveryMode());
        ListViewDataOrderDetails.get(4).setAll("Payment Method", order.getPayment().getPaymentType());
        if(order.getDeliveryMode().equals("Delivery")) {
            ListViewDataOrderDetails.get(5).setAll("Delivery Address",order.getOrder_address().getCustomer_address());
        }else{
            ListViewDataOrderDetails.get(5).setAll("Delivery Address",order.getOrder_address().getPrinter_address());
        }

        if (order.getPayment().getPaymentType().equals("OnlinePayment")) {
            paymentDetails = "FRENZ:" + order.getOrderCodeReference();
           // ListViewDataOrderDetails.get(6).setAll("Beneficiary No  :", printer.getUser().getBank_references());
            ListViewDataOrderDetails.get(6).setAll("Payment Details", paymentDetails);
        } else {
            //ListViewDataOrderDetails.remove(6);
            ListViewDataOrderDetails.remove(7);
        }
    }

    private void CreateOrderProcessLayout() {

        orderStatus = order.getPreparation_status().getPrep_status();
        if (orderStatus.equals("Accepted")) {
            //Set as Current order Prep Statu"s
            tv_currentStatus_PVODA.setText("Accepted");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            //Set the next Stage Prep Status
            tv_nextStageStatus.setText("Printed Out");
            tv_prepStatusDescription.setText("Have you printed out those documents?");

            //Set drawable
            uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);

            btn_A.setVisibility(View.GONE);
            btn_B.setText("Yes");

        } else if (orderStatus.equals("Cancelled")) {
            //Set as Current order Prep Statu"s
            tv_currentStatus_PVODA.setText("Cancelled");

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


        } else if (orderStatus.equals("Printed Out")) {
            //Set as Current order Prep Status
            tv_currentStatus_PVODA.setText("Printed Out");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            if (order.getDeliveryMode().equals("SelfPick")) {

                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("On the way");
                tv_prepStatusDescription.setText("Please wait for customer while he or she is on the way");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setVisibility(View.GONE);

            } else {
                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("On the way");
                tv_prepStatusDescription.setText("Are you ready to deliver the order?");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setText("Yes");

            }


        } else if (orderStatus.equals("On the Way")) {
            //Set as Current order Prep Status
            tv_currentStatus_PVODA.setText("On the Way");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);

            if (order.getDeliveryMode().equals("SelfPick")) {

                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("Arrived");
                tv_prepStatusDescription.setText("Be patience while waiting for customer to arrive your location");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setVisibility(View.GONE);

            } else {
                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("Arrived");
                tv_prepStatusDescription.setText("Have you arrived customer location?");
                btn_A.setVisibility(View.GONE);
                btn_B.setText("Yes");

            }


        } else if (orderStatus.equals("Arrived")) {
            //Set as Current order Prep Status
            tv_currentStatus_PVODA.setText("Arrived");

            //Set the next Stage Prep Status
            uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
            int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource1);
            iv_currentStatus.setImageDrawable(res);


            //Set the next Stage Prep Status
            tv_nextStageStatus.setText("Completed");
            tv_prepStatusDescription.setText("Waiting for customer to mark order Received");

            //Set drawable
            uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            res = getResources().getDrawable(imageResource);
            iv_prepState.setImageDrawable(res);

            btn_A.setVisibility(View.GONE);
            btn_B.setVisibility(View.GONE);


        } else if (orderStatus.equals("Completed")) {
            //Set as Current order Prep Status
            tv_currentStatus_PVODA.setText("Completed");

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
        if (view.getId() == R.id.btn_B_PVODA) {
            if (orderStatus.equals("Pending")) {
            } else if (orderStatus.equals("Accepted")) {
                orderStatus="Printed Out";

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
                tv_currentStatus_PVODA.setText("Printed Out");
                //Set the next Stage Prep Status
                uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
                int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource1);
                iv_currentStatus.setImageDrawable(res);

                if(order.getDeliveryMode().equals("SelfPick")){

                    //Set the next Stage Prep Status
                    tv_nextStageStatus.setText("On the way");
                    tv_prepStatusDescription.setText("Please wait for customer while he or she is on the way");

                    //Set drawable
                    uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    res = getResources().getDrawable(imageResource);
                    iv_prepState.setImageDrawable(res);

                    btn_A.setVisibility(View.GONE);
                    btn_B.setVisibility(View.GONE);

                }else{
                    //Set the next Stage Prep Status
                    tv_nextStageStatus.setText("Delivery");
                    tv_prepStatusDescription.setText("Are you on the way to customer location?");

                    //Set drawable
                    uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    res = getResources().getDrawable(imageResource);
                    iv_prepState.setImageDrawable(res);

                    btn_A.setVisibility(View.GONE);
                    btn_B.setText("Yes");


                }

            } else if (orderStatus.equals("Cancelled")) {
            } else if (orderStatus.equals("Printed Out")) {
                orderStatus = "On the Way";
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
                tv_currentStatus_PVODA.setText("On the Way");
                //Set the next Stage Prep Status
                uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
                int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource1);
                iv_currentStatus.setImageDrawable(res);

                if (order.getDeliveryMode().equals("SelfPick")) {

                    //Set the next Stage Prep Status
                    tv_nextStageStatus.setText("Arrived");
                    tv_prepStatusDescription.setText("Be patience while waiting for customer to arrived your location");

                    //Set drawable
                    uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    res = getResources().getDrawable(imageResource);
                    iv_prepState.setImageDrawable(res);

                    btn_A.setVisibility(View.GONE);
                    btn_B.setVisibility(View.GONE);

                } else {
                    //Set drawable
                    uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    res = getResources().getDrawable(imageResource);
                    iv_prepState.setImageDrawable(res);

                    //Set the next Stage Prep Status
                    tv_nextStageStatus.setText("Arrived");
                    tv_prepStatusDescription.setText("Have you arrived customer location?");
                    btn_A.setVisibility(View.GONE);
                    btn_B.setText("Yes");

                }

            } else if (orderStatus.equals("On the Way")) {
                orderStatus = "Arrived";
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
                tv_currentStatus_PVODA.setText("Arrived");

                //Set the next Stage Prep Status
                uri = "@drawable/ic_check";  // where myresource (without the extension) is the file
                int imageResource1 = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource1);
                iv_currentStatus.setImageDrawable(res);


                //Set the next Stage Prep Status
                tv_nextStageStatus.setText("Completed");
                tv_prepStatusDescription.setText("Waiting for customer to mark order Received");

                //Set drawable
                uri = "@drawable/ic_loading";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                iv_prepState.setImageDrawable(res);

                btn_A.setVisibility(View.GONE);
                btn_B.setVisibility(View.GONE);


            } else if (orderStatus.equals("Arrived")) {
                //Back to previous  - UserMainActivity.class

                Intent i = new Intent(PrinterViewOrderDetailsActivity.this, UserMainActivity.class);
                startActivity(i);


            } else {
                //Completed
            }
        } else if (view.getId() == R.id.btn_A_PVODA) {


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

            TextView pp_title = view.findViewById(R.id.pp_title1);
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

            TextView tv_fileNamePVODA = view.findViewById(R.id.tv_fileNameCVODA);
            TextView tv_fileTypePVODA = view.findViewById(R.id.tv_fileTypeCVODA);
            TextView tv_costPVODA = view.findViewById(R.id.tv_costCVODA);

            tv_fileNamePVODA.setText(sub_orders.getResourcesRecord().getfile_name());
            tv_fileTypePVODA.setText(sub_orders.getResourcesRecord().getFile_type());
            tv_costPVODA.setText(sub_orders.getCost());


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

    private class ListViewData {
        private String key;
        private String value;

        public ListViewData() {
        }

        public ListViewData(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public void setAll(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String getKey() {
            return key;
        }

        private String getValue() {
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

    private class updatePreparationStatusBW extends AsyncTask<String, Void, String[]> {
        public updatePreparationStatusBW() {
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
            String UserURL = Global.getURL() + "CRUD_user_markPreparationStatus.php";


            try {
                URL url = new URL(UserURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String post_data =
                        URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8") + "&"
                                + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
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
                return new String[]{"connection success", final_result};
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (IOException e) {
                e.printStackTrace();
                return new String[]{"connection fail"};
            } catch (Exception e) {
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
            if (result[0].equals("connection success")) {
                Response response = Response.JSONToOBJ(result[1]);
                if (response.getMessage().equals("Success")) {
                    System.out.println(response.getData());
                } else {
                    System.out.println("Nothing");
                }
            }
        }

    }
}