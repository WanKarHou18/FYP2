package com.example.fyp.ui_printer.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.UserMainActivity;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Customer;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.Sub_Orders;

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

public class PrinterReceivedOrderMainActivity extends AppCompatActivity implements  View.OnClickListener{

    //View
    private ListView lv_custDetails_CROMA ;
    private ListView lv_subOders_CROMA;
    private ListView lv_ordersDetails_CROMA;

    private Button btn_Accepted_CROMA;
    private Button btn_cancelled_CROMA;

    //Object
    private Orders order = null;
    private Customer customer;
    private ArrayList<Sub_Orders>sub_orders=new ArrayList<>();

    private ArrayList<ListViewData> ListViewDataPrinterDetails= new ArrayList<>();
    private ArrayList<ListViewData> ListViewDataOrderDetails= new ArrayList<>();
    private ListViewAdapter orderlistViewAdapter;
    private ListViewAdapter printerlistViewAdapter;
    private Sub_OrderListViewAdapter sub_orderListViewAdapter;

    //Data
    private String paymentDetails;
    private JSONObject data = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_received_order_main);


        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        lv_custDetails_CROMA = findViewById(R.id.lv_custDetails_CROMA);
        lv_subOders_CROMA= findViewById(R.id.lv_subOrders_CROMA);
        lv_ordersDetails_CROMA = findViewById(R.id.lv_orderDetails_CROMA);

        btn_Accepted_CROMA = findViewById(R.id.btn_Accepted_CROMA);
        btn_cancelled_CROMA = findViewById(R.id.btn_cancelled_CROMA);
    }

    private void initiateData() {
        order = Orders.JSONToOBJ(Global.printer_view_selected_orders_json);
        sub_orders = order.getSub_orders();


        customer = order.getCustomer();

        //Create data for listView Printer
        CreatePrinterListViewData();

        //Create data for listView Order
        CreateOrderListViewData();

    }

    private void createLayoutView() {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Create List View for Order
        orderlistViewAdapter = new ListViewAdapter(this,ListViewDataOrderDetails);

        //Create List View for Printer
        printerlistViewAdapter = new ListViewAdapter(this,ListViewDataPrinterDetails);

        //Create List View for Sub Orders
        sub_orderListViewAdapter = new Sub_OrderListViewAdapter(this,sub_orders);

        lv_custDetails_CROMA.setAdapter(printerlistViewAdapter);
        lv_subOders_CROMA.setAdapter(sub_orderListViewAdapter);
        lv_ordersDetails_CROMA.setAdapter(orderlistViewAdapter);

    }

    private void ViewListener() {

        btn_cancelled_CROMA.setOnClickListener(this::onClick);
        btn_Accepted_CROMA.setOnClickListener(this::onClick);

        lv_subOders_CROMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String sub_order_selected_json = Sub_Orders.OBJtoJSON(sub_orders.get(position));
                Global.printer_view_selected_sub_order_json = sub_order_selected_json;
                System.out.println(sub_order_selected_json);
                Intent intent = new Intent(getApplicationContext(), PrinterViewReceivedSubOrderDetailsActivity.class);
                startActivity(intent);


            }
        });


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_Accepted_CROMA:

                try {
                    data.put("prep_status_id",order.getPreparation_status().getPrepStatusID());
                    data.put("prep_status", "Accepted");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updatePreparationStatusBW updatePreparationStatusBW = new updatePreparationStatusBW();
                updatePreparationStatusBW.execute("update",data.toString());

                Intent i = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(i);
                break;
            case R.id.btn_cancelled_CROMA:

                try {
                    data.put("prep_status_id",order.getPreparation_status().getPrepStatusID());
                    data.put("prep_status", "Cancelled");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updatePreparationStatusBW updatePreparationStatusBW1 = new updatePreparationStatusBW();
                updatePreparationStatusBW1.execute("update",data.toString());

                Intent j = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(j);

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


    private void CreatePrinterListViewData() {
        for(int i=0;i<3;i++){
            ListViewData listViewdata =  new ListViewData("-","-");
            ListViewDataPrinterDetails.add(listViewdata);
        }
        ListViewDataPrinterDetails.get(0).setAll("Name",customer.getUser().getUsername());
        ListViewDataPrinterDetails.get(1).setAll("HP ",customer.getUser().getUser_hp());
        ListViewDataPrinterDetails.get(2).setAll("Address",customer.getUser().getUser_address());
    }

    private void CreateOrderListViewData() {
        for(int i=0;i<7;i++){
            ListViewData listViewdata =  new ListViewData("-","-");
            ListViewDataOrderDetails.add(listViewdata);
        }
        ListViewDataOrderDetails.get(0).setAll("Order Ref Number", order.getOrderCodeReference());
        ListViewDataOrderDetails.get(1).setAll("Status",order.getOrderStatus());
        ListViewDataOrderDetails.get(2).setAll("Date and Time",order.getOrderDate()+"      "+order.getTime());
        ListViewDataOrderDetails.get(3).setAll("Delivery Mode",order.getDeliveryMode());
        ListViewDataOrderDetails.get(4).setAll(" Payment Method",order.getPayment().getPaymentType());
        if(order.getDeliveryMode().equals("Delivery")) {
            ListViewDataOrderDetails.get(5).setAll("Delivery Address",order.getOrder_address().getCustomer_address());
        }else{
            ListViewDataOrderDetails.get(5).setAll("Delivery Address",order.getOrder_address().getPrinter_address());
        }


        if(order.getPayment().getPaymentType().equals("OnlinePayment")){
            paymentDetails = "FRENZ:"+order.getOrderCodeReference();
            //ListViewDataOrderDetails.get(6).setAll("Beneficiary No",customer.getUser().getBank_references());
            ListViewDataOrderDetails.get(6).setAll("Payment Details",paymentDetails);
        }else{
            //ListViewDataOrderDetails.remove(6);
            ListViewDataOrderDetails.remove(6);
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

}