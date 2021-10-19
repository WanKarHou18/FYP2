package com.example.fyp.ui_customer.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Availability;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Response;
import com.example.fyp.profile.PrinterProfileActivity;
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

    // View
    private HomeViewModel homeViewModel;
    private View root;
    private Button btSearchKitchen,btViewMenu;
    private ImageView ivSearchKitchen,ivViewMenu;

    private ListView ListView;
    private ListViewAdapter ListViewAdapter;
    private ListViewAdapter ListViewAdapter1;
    private ConstraintLayout clDiscover,clViewMenu;

    private Button btn_filter_loc;
    private Button btn_all;
    private Button   btn_filter_type;

    private CheckBox cb_filterDelivery;
    private CheckBox cb_filterSelfPickUp;

    private CheckBox cb_filter_PhotoPrinting;
    private CheckBox cb_filter_DocumentPrinting;

    private EditText et_filterDeliveryTime;

    // Object
    private ArrayList<Printer> printers;
    private ArrayList<Printer> printers1;
    private Response response;

    private Printer printer;
    private Availability availability;

    // Data
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String delivery_avail = null;
    private String self_pick_up_avail= null;

    private String document = null;
    private String image =null;

    private String data;
    private String action;
    private String php_link;

    //**In Alert Box
    private String type="default";
    private String color="default";
    private String cost="default";



    // Extra


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();


        return root;
    }

    private void linkXML() {

        NavigationView navigationView = root.getRootView().findViewById(R.id.nav_view);
        ListView = root.getRootView().findViewById(R.id.printer_list);

       btn_filter_type = root.getRootView().findViewById(R.id.btn_filter_type);
        btn_filter_loc = root.getRootView().findViewById(R.id.btn_filter_loc);
        btn_all= root.getRootView().findViewById(R.id.btn_all);

    }

    private void initiateData() {
        printers = new ArrayList<>();
        sharedPreferences=this.getActivity().getSharedPreferences("login_status",0);
        editor=sharedPreferences.edit();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        PrinterBackgroundWorker printerBackgroundWorker =  new PrinterBackgroundWorker();
        printerBackgroundWorker.execute();

    }

    private void createLayoutView() {
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_filter_loc) {

            createFilterDeliveryOptionAlertBox();
        }else if(view.getId()==R.id.btn_filter_type){
            createFilterPrintingTypeAlertBox();
        }else if(view.getId()==R.id.btn_all){
            PrinterBackgroundWorker printerBackgroundWorker =  new PrinterBackgroundWorker();
            printerBackgroundWorker.execute();
            Global.displayToast(getContext(),"All available printers selected",0,"blue");
        }
    }



    public class ListViewAdapter extends ArrayAdapter<Printer> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Printer> printers;

        public ListViewAdapter(Activity context, ArrayList<Printer> printers) {
            super(context, R.layout.printer_menu_list, printers);
            this.context = context;
            this.printers = printers;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.printer_menu_list, null);

            Printer printer = getItem(position);

            ImageView ivIcon = view.findViewById(R.id.icon);
            TextView tvTitle = view.findViewById(R.id.title);
            TextView tvSubtitle = view.findViewById(R.id.subtitle);

            tvTitle.setText(printer.getUser().getUsername());
            tvSubtitle.setText(printer.getUser().getUser_address());
            return view;
        }

        @Override
        public int getCount() {
            return printers.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    private void ViewListener() {
        btn_filter_loc.setOnClickListener(this::onClick);
       btn_filter_type.setOnClickListener(this::onClick);
        btn_all.setOnClickListener(this::onClick);
        
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                  //Intent intent = new Intent(getActivity(), OrderActivity.class);
                // intent.putExtra("printer_json", Printer.OBJtoJSON(printers.get(position)));
                //startActivity(intent);

                //Fragment currentFragment = getActivity().getSupportFragmentManager().getPrimaryNavigationFragment();
                //NavHostFragment.findNavController(currentFragment).navigate(R.id.action_nav_home_to_nav_printer_profile);
                printer = printers.get(position);
                getPrintingTypeAvailability();

                try {
                    Thread.sleep(1000);

                    String printer_json = Printer.OBJtoJSON(printer);
                    Global.printer_json =printer_json;

                    Intent intent = new Intent(getActivity(), PrinterProfileActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });
    }


    private void getPrintingTypeAvailability(){
        JSONObject data1 = new JSONObject();
        try {
            data1.put("printer_id",printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FectchPrinterPrintingPrefSettingBW fectchPrinterPrintingPrefSettingBW = new FectchPrinterPrintingPrefSettingBW();
        fectchPrinterPrintingPrefSettingBW.execute("read",data1.toString());

    }




    private void createFilterDeliveryOptionAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Search by shipping option");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.alert_box_filter_time, (ViewGroup) getView(), false);
        // Set up the input

        cb_filterDelivery = (CheckBox)viewInflated.findViewById(R.id.cb_filterDelivery) ;
        cb_filterSelfPickUp= (CheckBox)viewInflated.findViewById(R.id.cb_filterSelfPick) ;

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(cb_filterDelivery.isChecked()){
                    delivery_avail = "Yes";

                }else{
                    delivery_avail ="No";
                }

                if(cb_filterSelfPickUp.isChecked()){
                    self_pick_up_avail ="Yes";
                }else{
                    self_pick_up_avail ="No";
                }

                if((self_pick_up_avail.equals("No"))&&(delivery_avail.equals("No"))){
                   Global.displayToast(getContext(),"Please checked on at least one box",0,"yellow");
                }else{
                    JSONObject OBJ = new JSONObject();
                    try {
                        OBJ.put("delivery_avail", delivery_avail);
                        OBJ.put("self_pick_up_avail", self_pick_up_avail);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data = OBJ.toString();
                    FilterBW filterBW = new FilterBW();
                    filterBW.execute("read", data, "CRUD_customer_filterShippingOption.php");

                    dialog.dismiss();
                }

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

    private void createFilterPrintingTypeAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Search by Printing Type");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.alert_box_filter_loc, (ViewGroup) getView(), false);
        // Set up the input

        cb_filter_DocumentPrinting = (CheckBox)viewInflated.findViewById(R.id.cb_filter_document) ;
        cb_filter_PhotoPrinting= (CheckBox)viewInflated.findViewById(R.id.cb_filter_photo) ;

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(cb_filter_DocumentPrinting.isChecked()){
                    document = "Yes";

                }else{
                    document ="No";
                }

                if(cb_filter_PhotoPrinting.isChecked()){
                    image="Yes";
                }else{
                    image ="No";
                }

                if((document.equals("No"))&&(image.equals("No"))){
                    Global.displayToast(getContext(),"Please checked on at least one box",0,"yellow");
                }else{
                    JSONObject OBJ = new JSONObject();
                    try {
                        OBJ.put("Document", document);
                        OBJ.put("Image", image);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data = OBJ.toString();
                    FilterBW filterBW = new FilterBW();
                    filterBW.execute("read", data, "CRUD_customer_filterPrintingType.php");
                    dialog.dismiss();
                }

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


    private class PrinterBackgroundWorker extends AsyncTask<String,Void,String[]> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String[] doInBackground(String... params) {
            String printer_url = Global.getURL() +"/fetch_printer.php";


            try {
                URL url = new URL(printer_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }

                String result = sb.toString().trim();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return new String[]{result};
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        //In this method we can update ui of background operation result.
        protected void onPostExecute(String[] result) {
            System.out.println(result[0]);
            response = Response.JSONToOBJ(result[0]);
            if(response.getMessage().equals("Success")){
                printers = new ArrayList<>();
                printers = Printer.JSONToLIST(response.getData());
                System.out.println(Printer.LISTtoJSON(printers));
                ListViewAdapter = new ListViewAdapter(getActivity(), printers);
                ListView.setAdapter(ListViewAdapter);
            }else{
                System.out.println("nothing");
            }
        }

        @Override
        //While doing background operation,
        // if you want to update some information on UI, we can use this method.
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    private class FilterBW extends AsyncTask<String,Void,String[]> {
        public FilterBW() { }
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
            action = params[0];
            data = params[1];
            php_link = params[2];
            String UserURL = Global.getURL()+php_link;
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
                response = Response.JSONToOBJ(result[1]);

                if(response.getMessage().equals("Success")) {
                    //Check if the printers existed
                    ArrayList<Printer> temp_printers =new ArrayList<>();
                    temp_printers =Printer.JSONToLIST(response.getData());
                    if(temp_printers.size()!=0) {
                        //Get Response Message - Printer
                        printers = new ArrayList<>();
                        printers = Printer.JSONToLIST(response.getData());
                        ListViewAdapter = new ListViewAdapter(getActivity(), printers);
                        ListView.setAdapter(ListViewAdapter);
                    }else{
                        Global.displayToast(getContext(),"No service provider found",0,"red");
                    }
                }else{
                    System.out.println("Cant fetch data from server");
                }
            }
        }

    }

    //Background Worker to fetch service provider printing type availability

    private class FectchPrinterPrintingPrefSettingBW extends AsyncTask<String,Void,String[]> {
        public FectchPrinterPrintingPrefSettingBW() { }


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
            String UserURL = Global.getURL()+"CRUD_printer_printingTypeAvailability.php";


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
                    System.out.println("Availability"+response.getData());
                    Global.printing_type_availability_json = response.getData();

                }else{
                    System.out.println("Nothing");
                }
            }
        }

    }

}
