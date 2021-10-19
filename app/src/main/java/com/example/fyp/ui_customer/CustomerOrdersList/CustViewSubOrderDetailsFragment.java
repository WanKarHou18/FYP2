package com.example.fyp.ui_customer.CustomerOrdersList;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.DocumentPreferences;
import com.example.fyp.domain.ImagePreferences;
import com.example.fyp.domain.Product_Printing_Preferences;
import com.example.fyp.domain.Sub_Orders;

import java.util.ArrayList;


public class CustViewSubOrderDetailsFragment extends Fragment {

    //View
    private View root;
    private TextView tv_FileName_display_CVSOD;
    private TextView tv_fileType_CVSOD;
    private ListView lv_subOrderPrintPref_CVSOD;
    private TextView tv_cost_CVSOD;


    //Object
    private Sub_Orders sub_order;
    private Product_Printing_Preferences product_printing_preferences=null;
    private DocumentPreferences documentPreferences=null;
    private ImagePreferences imagePreferences=null;
    private ArrayList<ListViewData> listViewDataArrayList= new ArrayList<>();
    private ListViewAdapter listViewAdapter ;

    //Data



    public CustViewSubOrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_cust_view_sub_order_details, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        tv_FileName_display_CVSOD = root.findViewById(R.id.tv_FileName_display_PVSODA1);
        tv_fileType_CVSOD= root.findViewById(R.id.tv_fileType_CVSOD);
        lv_subOrderPrintPref_CVSOD= root.findViewById(R.id.lv_subOrderPrintPref_CVSOD);
        tv_cost_CVSOD= root.findViewById(R.id.tv_cost_PVSODA1);
    }

    private void initiateData() {
        sub_order = Sub_Orders.JSONToOBJ(Global.cust_view_selected_sub_order_json);
        product_printing_preferences = sub_order.getProduct_Printing_Preferences();
    }

    private void createLayoutView() {
        tv_FileName_display_CVSOD.setText(sub_order.getResourcesRecord().getfile_name());
        tv_fileType_CVSOD.setText(sub_order.getResourcesRecord().getFile_type());
        tv_cost_CVSOD.setText(sub_order.getCost());

        //List View Info Data
        if(sub_order.getResourcesRecord().getFile_type().equals("Document")){
            documentPreferences = DocumentPreferences.JSONToOBJ(product_printing_preferences.getPrintingPreferences());

            for(int i=0; i<6;i++){
               ListViewData listViewdata =  new ListViewData("-","-");
                listViewDataArrayList.add(listViewdata);
            }

            listViewDataArrayList.get(0).setAll("Color:",documentPreferences.getColorSelected());
            listViewDataArrayList.get(1).setAll("Potrait/Landscape:",documentPreferences.getPL());
            listViewDataArrayList.get(2).setAll("Slides:",documentPreferences.getSlided());
            listViewDataArrayList.get(3).setAll("Edge:",documentPreferences.getEdge());
            listViewDataArrayList.get(4).setAll("Page Range:",documentPreferences.getPageRange());
            listViewDataArrayList.get(5).setAll("SlidesPerPage:",documentPreferences.getSlidePerPage());
        }else{
            imagePreferences = ImagePreferences.JSONToOBJ(product_printing_preferences.getPrintingPreferences());
            for(int i=0; i<4;i++){
               ListViewData listViewdata =  new ListViewData("-","-");
                listViewDataArrayList.add(listViewdata);
            }

            listViewDataArrayList.get(0).setAll(" PaperSize:",imagePreferences.getPaperSize());
            listViewDataArrayList.get(1).setAll("Additional Option:",imagePreferences.getborder());
            listViewDataArrayList.get(2).setAll("PaperType:",imagePreferences.getPaperType());
            listViewDataArrayList.get(3).setAll("Copies:",imagePreferences.getCopies());

        }
        handleLayout();

    }

    private void handleLayout() {
            //  FILE INFO AND COST
            tv_FileName_display_CVSOD.setText(sub_order.getResourcesRecord().getfile_name());
            tv_fileType_CVSOD.setText(sub_order.getResourcesRecord().getFile_type());
            tv_cost_CVSOD.setText(sub_order.getCost());

            //  PRINTING PREFERENCES
            listViewAdapter = new ListViewAdapter(getActivity(), listViewDataArrayList);
            lv_subOrderPrintPref_CVSOD.setAdapter(listViewAdapter);


    }

    private void ViewListener() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public class ListViewAdapter extends ArrayAdapter<ListViewData> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<ListViewData> listViewDataArr;

        private ListViewAdapter(Activity context, ArrayList<ListViewData> listViewDataArr) {
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
        public String getKey(){
            return key;
        }
        public String getValue(){
            return value;
        }
    }

}