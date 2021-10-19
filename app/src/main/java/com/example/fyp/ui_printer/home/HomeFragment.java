package com.example.fyp.ui_printer.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Response;
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

public class HomeFragment extends Fragment implements View.OnClickListener {

    //View
    private View root ;
    private ListView lv_newOrders;
    private ConstraintLayout CL_bottom_printer;

    //Object
    private Response response;
    private ArrayList<Orders> ordersArrayList = new ArrayList<>();
    private ArrayList<Orders> ordersArrayList_Pending = new ArrayList<>();
    private Orders orders;
    private CustViewOrdersListViewAdapter custViewOrdersListViewAdapter;

    //Data
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home_printer, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }
    private void linkXML() {
        NavigationView navigationView = root.getRootView().findViewById(R.id.nav_view_printer);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_printer);

        lv_newOrders = root.findViewById(R.id.lv_newOrders);
        //CL_bottom_printer = root.findViewById(R.id.CL_bottom_printer_new_order);
    }

    private void initiateData() {
        sharedPreferences=this.getActivity().getSharedPreferences("login_status",0);
        editor=sharedPreferences.edit();

        JSONObject data = new JSONObject();
        try {
            data.put("printer_id",Global.user.getPrinter().getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomerOrderBackgroundWorker customerOrderBackgroundWorker = new CustomerOrderBackgroundWorker();
        customerOrderBackgroundWorker.execute("read",data.toString());

    }

    private void createLayoutView() {
        if(ordersArrayList_Pending.size()!=0){
            custViewOrdersListViewAdapter = new CustViewOrdersListViewAdapter(getActivity(), ordersArrayList_Pending);
            lv_newOrders.setAdapter(custViewOrdersListViewAdapter);
        }

    }

    private void ViewListener() {
        lv_newOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String order_selected_json = Orders.OBJtoJSON(ordersArrayList_Pending.get(position));
                Global.printer_view_selected_orders_json = order_selected_json;
                System.out.println(order_selected_json);
                Intent intent = new Intent(getActivity(), PrinterReceivedOrderMainActivity.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
    private class CustViewOrdersListViewAdapter extends ArrayAdapter<Orders> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Orders> orders;

        public CustViewOrdersListViewAdapter(Activity context , ArrayList<Orders> orders) {
            super(context, R.layout.cust_view_orders_list, orders);
            this.context = context;
            this.orders = orders;
            inflater = (LayoutInflater.from(context));
        }


        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.cust_view_orders_list, null);

            String dateTime="";
            Orders order = getItem(position);

            TextView tv_orderStatus = view.findViewById(R.id.tv_orderStatusCVO);
            TextView tv_PrinterName = view.findViewById(R.id.tv_printerNameCVO);
            TextView tv_HP = view.findViewById(R.id.tv_custHPCVO);
            TextView tv_DateTime  = view.findViewById(R.id.tv_dateTimeCVO);
            TextView tv_Address=  view.findViewById(R.id.tv_orderAddressCVO);
            TextView tv_deliverymode = view.findViewById(R.id.tv_deliveryModeCVO);
            TextView tv_PaymentMethod= view.findViewById(R.id.tv_paymentMethodCVO);
            TextView tv_OrderCost = view.findViewById(R.id.tv_ttlCostCVO);


            tv_PrinterName.setText(order.getCustomer().getUser().getUsername());
            tv_OrderCost.setText(order.getPayment().getPaymentCost());
            tv_HP.setText(order.getCustomer().getUser().getUser_hp());

            dateTime=order.getOrderDate()+" "+order.getTime();
            tv_DateTime.setText(dateTime);
            tv_deliverymode.setText(order.getDeliveryMode());

            if(order.getDeliveryMode().equals("SelfPick")) {
                tv_Address.setText(order.getOrder_address().getPrinter_address());
            }else{
                //Delivery
                tv_Address.setText(order.getOrder_address().getCustomer_address());
            }

            if(order.getPreparation_status().getPrep_status().equals("Pending")) {
                tv_orderStatus.setText(order.getPreparation_status().getPrep_status());
            }else{
                tv_orderStatus.setText(order.getPreparation_status().getPrep_status());}

            tv_PaymentMethod.setText(order.getPayment().getPaymentType());
            return view;
        }


        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }


    private class CustomerOrderBackgroundWorker extends AsyncTask<String,Void,String[]> {
        public CustomerOrderBackgroundWorker() { }


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
            String UserURL = Global.getURL()+"CRUD_printer_order.php";


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
                    if(!(response.getData().equals(""))){
                        Global.printer_view_orders_json = response.getData();
                        System.out.println("HERE"+response.getData());
                        //ordersArrayList_Pending = ordersArrayList;
                        checkForPendingOrder();
                        if(ordersArrayList_Pending.size()!=0) {
                            createLayoutView();
                        }else{

                        }
                    }

                }else{
                    System.out.println("Nothing");
                }
            }
        }

    }

    private void checkForPendingOrder() {
        if(!(Global.printer_view_orders_json.equals(""))) {
            ordersArrayList = Orders.JSONToLIST(Global.printer_view_orders_json);
            if(ordersArrayList.size()!=0) {
                for (int i = 0; i < ordersArrayList.size(); i++) {
                    if (ordersArrayList.get(i).getPreparation_status().getPrep_status().equals("Pending")) {
                        ordersArrayList_Pending.add(ordersArrayList.get(i));
                    }
                }
            }
        }
    }



}