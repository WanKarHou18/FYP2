package com.example.fyp.ui_printer.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.DocumentPreferences;
import com.example.fyp.domain.ImagePreferences;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Product_Printing_Preferences;
import com.example.fyp.domain.Sub_Orders;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PrinterViewSubOrderDetailsActivity extends AppCompatActivity implements  View.OnClickListener{
    //View
    private View root;
    private TextView tv_FileName_display_PVSODA1;
    private TextView tv_fileType_PVSODA1;
    private ListView lv_subOrderPrintPref_PVSODA1;
    private TextView tv_cost_PVSODA1;
    private ImageView iv_downloadFile;


    //Object
    private Orders order;
    private Sub_Orders sub_order;
    private Product_Printing_Preferences product_printing_preferences=null;
    private DocumentPreferences documentPreferences=null;
    private ImagePreferences imagePreferences=null;
    private ArrayList<ListViewData> listViewDataArrayList= new ArrayList<>();
    private ListViewAdapter listViewAdapter ;

    //Data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_view_sub_order_details);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
    }
    private void linkXML() {
        tv_FileName_display_PVSODA1 = findViewById(R.id.tv_FileName_display_PVSODA1);
        tv_fileType_PVSODA1= findViewById(R.id.tv_fileType_PVSODA1);
        lv_subOrderPrintPref_PVSODA1= findViewById(R.id.lv_subOrderPrintPref_PVSODA1);
        tv_cost_PVSODA1= findViewById(R.id.tv_cost_PVSODA1);
        iv_downloadFile = findViewById(R.id.iv_downloadFile);
    }

    private void initiateData() {
        order = Orders.JSONToOBJ(Global.printer_view_selected_orders_json);
        sub_order = Sub_Orders.JSONToOBJ(Global.printer_view_selected_sub_order_json);
        product_printing_preferences = sub_order.getProduct_Printing_Preferences();
    }

    private void createLayoutView() {
        tv_FileName_display_PVSODA1.setText(sub_order.getResourcesRecord().getfile_name());
        tv_fileType_PVSODA1.setText(sub_order.getResourcesRecord().getFile_type());
        tv_cost_PVSODA1.setText(sub_order.getCost());

        //List View Info Data
        if(sub_order.getResourcesRecord().getFile_type().equals("Document")){
            documentPreferences = DocumentPreferences.JSONToOBJ(product_printing_preferences.getPrintingPreferences());

            for(int i=0; i<7;i++){
               ListViewData listViewdata =  new ListViewData("-","-");
                listViewDataArrayList.add(listViewdata);
            }

            listViewDataArrayList.get(0).setAll("Color",documentPreferences.getColorSelected());
            listViewDataArrayList.get(1).setAll("Potrait/Landscape",documentPreferences.getPL());
            listViewDataArrayList.get(2).setAll("Slides",documentPreferences.getSlided());
            listViewDataArrayList.get(3).setAll("Edge",documentPreferences.getEdge());
            listViewDataArrayList.get(4).setAll("Page Range",documentPreferences.getPageRange());
            listViewDataArrayList.get(5).setAll("SlidesPerPage",documentPreferences.getSlidePerPage());
            listViewDataArrayList.get(6).setAll("Copies",documentPreferences.getCopies());
        }else{
            imagePreferences = ImagePreferences.JSONToOBJ(product_printing_preferences.getPrintingPreferences());
            for(int i=0; i<4;i++){
               ListViewData listViewdata =  new ListViewData("-","-");
                listViewDataArrayList.add(listViewdata);
            }

            listViewDataArrayList.get(0).setAll(" PaperSize",imagePreferences.getPaperSize());
            listViewDataArrayList.get(1).setAll("Border",imagePreferences.getborder());
            listViewDataArrayList.get(2).setAll("PaperType",imagePreferences.getPaperType());
            listViewDataArrayList.get(3).setAll("Copies",imagePreferences.getCopies());

        }
        handleLayout();

    }

    private void handleLayout() {
        //  FILE INFO AND COST
        tv_FileName_display_PVSODA1.setText(sub_order.getResourcesRecord().getfile_name());
        tv_fileType_PVSODA1.setText(sub_order.getResourcesRecord().getFile_type());
        tv_cost_PVSODA1.setText(sub_order.getCost());

        //  PRINTING PREFERENCES
        listViewAdapter = new ListViewAdapter(this, listViewDataArrayList);
        lv_subOrderPrintPref_PVSODA1.setAdapter(listViewAdapter);


    }

    private void ViewListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iv_downloadFile.setOnClickListener(this::onClick);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_downloadFile:
                downloadFile(sub_order.getResourcesRecord().getfile_name());
                Global.displayToast(PrinterViewSubOrderDetailsActivity.this,"File Downloaded", Toast.LENGTH_SHORT,"blue");
        }

    }

    //========================================================
    // Download File from Firebase Storage
    //========================================================

    private void downloadFile(String FileName) {
        //CREATE FolderName
        //<"Order ID">@<"Sub Order ID">@<"Resources Record ID">
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(order.getOrderId());
        stringBuilder.append("@");
        stringBuilder.append(sub_order.getSubOrderId());
        stringBuilder.append("@");
        stringBuilder.append(sub_order.getResourcesRecord().getresources_id());

        String folderName =stringBuilder.toString();

        String fileURL= "FYP/Document of printer/"+folderName+"/"+FileName;
        System.out.println(fileURL);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
       storageReference.child(fileURL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                String url = uri.toString();
                System.out.println(url);
                downloadFiles(getApplicationContext(),FileName, DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void downloadFiles(Context context, String fileName, String destinationDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        downloadManager.enqueue(request);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);

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