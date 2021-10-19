package com.example.fyp.ui_customer.CustomerOrdersList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Response;
import com.example.fyp.ui_printer.Order.PrinterViewOrderDetailsActivity;

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

public class CustOrdersMainListFragment extends Fragment implements View.OnClickListener {
    //View
    private View root;
    private ListView lv_custOrdersCVO;
    private CustViewOrdersListViewAdapter custViewOrdersListViewAdapter;


    //Object
    private Orders orders;
    private ArrayList<Orders> ordersArrayList= new ArrayList<>();
    private ArrayList<Orders> ordersArrayList_Pending = new ArrayList<>();

    //Data

    //Manager
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_menu_custorderslist, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        lv_custOrdersCVO = root.getRootView().findViewById(R.id.lv_custOrdersCVO);

    }

    private void initiateData() {
        JSONObject data = new JSONObject();
        try {
            data.put("cust_id",Global.user.getCustomer().getCustId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       CustomerOrderBackgroundWorker customerOrderBackgroundWorker = new CustomerOrderBackgroundWorker();
        customerOrderBackgroundWorker.execute("read",data.toString());


    }

    private void createLayoutView() {
        if(ordersArrayList.size()!=0){
            custViewOrdersListViewAdapter = new CustViewOrdersListViewAdapter(getActivity(), ordersArrayList);
            lv_custOrdersCVO.setAdapter(custViewOrdersListViewAdapter);
        }
    }

    private void ViewListener() {
        lv_custOrdersCVO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String order_selected_json = Orders.OBJtoJSON(ordersArrayList.get(position));
                Global.cust_view_selected_orders_json = order_selected_json;
                Intent intent = new Intent(getActivity(), CustViewOrderDetailActivity.class);
                startActivity(intent);


            }
        });

    }

    @Override
    public void onClick(View view) {

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


            tv_PrinterName.setText(order.getPrinter().getUser().getUsername());
            tv_OrderCost.setText(order.getPayment().getPaymentCost());
            tv_HP.setText(order.getPrinter().getUser().getUser_hp());

            dateTime=order.getOrderDate()+" "+order.getTime();
            tv_DateTime.setText(dateTime);
            tv_deliverymode.setText(order.getDeliveryMode());

            if(order.getDeliveryMode().equals("SelfPick")) {
                tv_Address.setText(order.getOrder_address().getPrinter_address());
            }else{
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
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    if(!(response.getData().equals(""))){
                        Global.cust_view_orders_json = response.getData();
                        System.out.println("HERE"+response.getData());
                        ordersArrayList = Orders.JSONToLIST(Global.cust_view_orders_json);

                        //checkForPendingOrder();
                        createLayoutView();

                    }

                }else{
                    System.out.println("Nothing");
                }
            }
        }

    }
    //Arrange Orders
    private ArrayList<Orders>ArrangeOrders() {
        ArrayList<Orders> ordersArr = new ArrayList<>();
        if(!(Global.cust_view_orders_json.equals(""))) {
            ordersArrayList = Orders.JSONToLIST(Global.cust_view_orders_json);
            if(ordersArrayList.size()!=0) {
                for (int i = 0; i < ordersArrayList.size(); i++) {
                    if (ordersArrayList.get(i).getPreparation_status().getPrep_status().equals("Pending")) {
                        ordersArrayList_Pending.add(ordersArrayList.get(i));
                    }
                }
            }
        }
        return ordersArr;
    }





}