package com.example.fyp.ui_customer.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Sub_Orders;


import java.util.ArrayList;

public class CustOrderMainActivity extends AppCompatActivity implements View.OnClickListener {
    //View
    private Button btnAddOrder;
    private Button btnClearOrder;
    private Button btnConfirmOrder;

    private ListView lvAllCustOrder;
    private TextView tv_totalCostDisplay;

    //Object
    private Orders order;
    private ArrayList<Sub_Orders> sub_orders = new ArrayList<>();

    //Data
    private String ttlcost;

    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //Adapter
    private ListViewAdapter listViewAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_order);
        verifyPermissions();
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

    }

    private void initiateData() {
        order = Global.order;
        if(order!=null){
            sub_orders = order.getSub_orders();
            ttlcost= String.valueOf(CalculateTotalCost(sub_orders));
        }

    }
    private void linkXML() {
        btnAddOrder = findViewById(R.id.btnAddOrder);
        btnClearOrder = findViewById(R.id.btnClearOrder);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        lvAllCustOrder = findViewById(R.id.lvAllCustOrder);
        tv_totalCostDisplay = findViewById(R.id.tv_totalCostDisplay);

    }

    private void createLayoutView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(sub_orders.size()!=0) {
            HandleLayout();
        }
    }

    private void HandleLayout() {
        //  PRINTING PREFERENCES
        System.out.println(Orders.OBJtoJSON(order));
        listViewAdapter = new ListViewAdapter(this, sub_orders);
        lvAllCustOrder.setAdapter(listViewAdapter);
        tv_totalCostDisplay.setText(ttlcost);


    }

    private void ViewListener() {
        btnAddOrder.setOnClickListener(this::onClick);
        btnClearOrder.setOnClickListener(this::onClick);
        btnConfirmOrder.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnAddOrder: {

                Intent i = new Intent(this, CustOrderSecondActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btnConfirmOrder: {

                if(order!=null) {
                    Intent i = new Intent(this, CustOrderForthActivity.class);
                    i.putExtra("ttlcost", ttlcost);
                    startActivity(i);
                    break;
                }
            }
            case R.id.btnClearOrder:{
                //Clear List View
                lvAllCustOrder.setAdapter(null);

                //Clear sub_orders, FileName List, FileUriList, FileName, FileUri
                sub_orders = null;
                tv_totalCostDisplay.setText("0.00");
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:


                onBackPressed();
                ClearVariable();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ListViewAdapter extends ArrayAdapter<Sub_Orders> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Sub_Orders> sub_orders;

        public ListViewAdapter(Activity context,ArrayList<Sub_Orders> sub_orders) {
            super(context, R.layout.printing_mainorder_list,sub_orders);
            this.context = context;
            this.sub_orders =sub_orders;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.printing_mainorder_list, null);
            Sub_Orders sub_order = getItem(position);

            TextView FileName= view.findViewById(R.id.tv_main_FileName);
            TextView Cost= view.findViewById(R.id.tv_main_cost);
            TextView FileType = view.findViewById(R.id.tv_fileType);
            ImageView iv_doc_type = view.findViewById(R.id.iv_doctype_icon);

            FileName.setText(sub_order.getResourcesRecord().getfile_name());
            Cost.setText(sub_order.getCost());
            FileType.setText(sub_order.getResourcesRecord().getFile_type());

            if(sub_order.getResourcesRecord().getFile_type().equals("Document")){
                iv_doc_type.setBackgroundResource(R.drawable.document_logo);
            }else{
                iv_doc_type.setBackgroundResource(R.drawable.icon_camera);
            }

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

    //Calculate Total Cost
    private double CalculateTotalCost(ArrayList<Sub_Orders> sub_orders){
        double ttlCost = 0.00;
        if(sub_orders.size()!=0){
            for(int i=0;i<sub_orders.size();i++){
                ttlCost+= (Double.parseDouble(sub_orders.get(i).getCost()));
                ttlCost = Math.round(ttlCost * 100.0) / 100.0;
            }
        }
        return ttlCost;
    }

    private void ClearVariable(){
        Global.FileName =null;
        Global.FileURLList=null;
        Global.FileNameList.clear();
        Global.sub_order=null;
        Global.sub_orders=null;
        Global.orders=null;
        Global.order=null;
        Global.FileName=null;
        Global.FileType=null;
        Global.FileURL =null;
        Global.PageCount=0;
        Global.FileRealPath=null;

    }

    private void verifyPermissions(){
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(CustOrderMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    CustOrderMainActivity.this,
                    STORAGE_PERMISSIONS,
                    1
            );
        }

    }

}