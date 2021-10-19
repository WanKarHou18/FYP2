package com.example.fyp.ui_customer.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Customer;
import com.example.fyp.domain.DocumentPreferences;
import com.example.fyp.domain.ImagePreferences;
import com.example.fyp.domain.Order_Address;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Payment;
import com.example.fyp.domain.Preparation_Status;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Product_Printing_Preferences;
import com.example.fyp.domain.Sub_Orders;

import java.util.ArrayList;

public class CustOrderThirdActivity extends AppCompatActivity implements View.OnClickListener{

    //View
    private  ImageView iv_filelogo ;
    private  TextView tv_FileName_display;
    private  TextView tv_cost_display;
    private  TextView tv_FileType_display;
    private TextView tv_pages_display;
    private  ListView lv_printingPref;
    private  Button btn_AddOrder_3;
    private  Button btn_preview;

    private LinearLayout ll_pages;

    private DisplayImageFragment displayImageFragment = new DisplayImageFragment();
    private  DisplayDocumentFragment displayDocumentFragment = new DisplayDocumentFragment();

    //Object
    private Orders order=null;
    private Sub_Orders sub_order=null;
    private Order_Address order_address=null;
    private Payment payment=null;
    private Preparation_Status preparation_status = null;
    private Customer customer = null;
    private Printer printer=null;
    private ArrayList<Sub_Orders> sub_orders_arr = new ArrayList<>();
    private Product_Printing_Preferences product_printing_preferences=null;
    private DocumentPreferences documentPreferences=null;
    private ImagePreferences imagePreferences=null;
    private ArrayList<ListViewData> listViewDataArrayList= new ArrayList<>();
    private ListViewAdapter listViewAdapter;


    //Data
    //***For Order
    private String orderId = "default";
    private String custId = "default";
    private String printerId = "default";
    private String prepStatusId = "default";
    private String paymentId = "default";
    private String orderAddId = "default";
    private String orderStatus = "default";
    private String deliveryMode = "default";
    private String createdDatetime = "default";
    private String orderCodeReference = "default";
    private String orderDate = "default";
    private String Time ="default";



    //?Add in cost for sub-order?//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_order_third);


        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        tv_FileName_display = findViewById(R.id.tv_FileName_display);
        tv_FileType_display = findViewById(R.id.tv_FileType_display1);
        tv_cost_display = findViewById(R.id.tv_cost_display);
        btn_AddOrder_3 = findViewById(R.id.btnAddOrder_3);
        btn_preview = findViewById(R.id.btn_preview);
        lv_printingPref = findViewById(R.id.lv_printing_pref);
        tv_pages_display = findViewById(R.id.tv_Pages_display1);
        ll_pages =findViewById(R.id.ll_Pages);
    }

    private void initiateData() {
            // Get the lastest added in sub-order
             order = Global.order;
             sub_orders_arr = Global.sub_orders;
             sub_order = Global.sub_order;
             product_printing_preferences = Global.sub_order.getProduct_Printing_Preferences();


    }

    private void createLayoutView() {
        //List View Info Data
        if(sub_order.getResourcesRecord().getFile_type().equals("Document")){
            documentPreferences = DocumentPreferences.JSONToOBJ(product_printing_preferences.getPrintingPreferences());

            for(int i=0; i<7;i++){
                ListViewData listViewdata =  new ListViewData("-","-");
                listViewDataArrayList.add(listViewdata);
            }

            listViewDataArrayList.get(0).setAll("Color",documentPreferences.getColorSelected());
            listViewDataArrayList.get(1).setAll("Portrait/Landscape",documentPreferences.getPL());
            listViewDataArrayList.get(2).setAll("Slides",documentPreferences.getSlided());
            listViewDataArrayList.get(3).setAll("Edge",documentPreferences.getEdge());
            listViewDataArrayList.get(4).setAll("Page Range",documentPreferences.getPageRange());
            listViewDataArrayList.get(5).setAll("Slides Per Page",documentPreferences.getSlidePerPage());
            listViewDataArrayList.get(6).setAll("Copies",documentPreferences.getCopies());
        }else{
            imagePreferences = ImagePreferences.JSONToOBJ(product_printing_preferences.getPrintingPreferences());
            for(int i=0; i<4;i++){
                ListViewData listViewdata =  new ListViewData("-","-");
                listViewDataArrayList.add(listViewdata);
            }

            listViewDataArrayList.get(0).setAll("PaperSize",imagePreferences.getPaperSize());
            listViewDataArrayList.get(1).setAll("Border",imagePreferences.getborder());
            listViewDataArrayList.get(2).setAll("PaperType",imagePreferences.getPaperType());
            listViewDataArrayList.get(3).setAll("Copies",imagePreferences.getCopies());

        }
        handleLayout();
    }

    private void handleLayout() {

        if(sub_order.getResourcesRecord().getFile_type().equals("Document")) {

            //  FILE INFO AND COST
            tv_FileName_display.setText(sub_order.getResourcesRecord().getfile_name());
            tv_FileType_display.setText(sub_order.getResourcesRecord().getFile_type());
            tv_pages_display.setText(String.valueOf(Global.PageCount));
            tv_cost_display.setText(sub_order.getCost());

            //  PRINTING PREFERENCES
           listViewAdapter = new ListViewAdapter(this, listViewDataArrayList);
           lv_printingPref.setAdapter(listViewAdapter);

        }else{
            //  FILE INFO AND COST
            tv_FileName_display.setText(sub_order.getResourcesRecord().getfile_name());
            tv_FileType_display.setText(sub_order.getResourcesRecord().getFile_type());
            tv_cost_display.setText(sub_order.getCost());
            ll_pages.setVisibility(View.GONE);

            //  PRINTING PREFERENCES
            listViewAdapter = new ListViewAdapter(this, listViewDataArrayList);
            lv_printingPref.setAdapter(listViewAdapter);
        }
    }

    private void ViewListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_AddOrder_3.setOnClickListener(this::onClick);
        btn_preview.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnAddOrder_3:


                sub_order = Global.sub_order;
                sub_orders_arr.add(sub_order);

               order = new Orders(orderId, custId, printerId, orderStatus,deliveryMode,orderDate,Time,createdDatetime,orderCodeReference, sub_orders_arr,order_address,payment,preparation_status,customer,printer);
               Global.order = order;
               System.out.println("Order HERE"+Orders.OBJtoJSON(order));

                Intent i = new Intent(this, CustOrderMainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_preview:
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if(sub_order.getResourcesRecord().getFile_type().equals("Document")) {
                    transaction.add(R.id.FL_printPrefDisplay,displayDocumentFragment );
                }else{
                    transaction.add(R.id.FL_printPrefDisplay, displayImageFragment);
                }
                transaction.addToBackStack(null);
                transaction.commit();
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



    public class ListViewAdapter extends ArrayAdapter<ListViewData> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<ListViewData> listViewDataArr;

        public ListViewAdapter(Activity context, ArrayList<ListViewData> listViewDataArr) {
            super(context, R.layout.printing_preferences_list, listViewDataArr);
            this.context = context;
            this.listViewDataArr = listViewDataArr;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.printing_preferences_list, null);

            ListViewData listViewData = getItem(position);

            TextView pp_title= view.findViewById(R.id.pp_title);
            TextView pp_subtitle = view.findViewById(R.id.pp_subtitle);

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

    public class ListViewData{
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
        public String getKey(){
            return key;
        }
        public String getValue(){
            return value;
        }
    }


}