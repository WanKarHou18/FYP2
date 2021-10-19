package com.example.fyp.ui_printer.PrintingPreferencesSetting;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.DocumentPrintingSettingPreferences;
import com.example.fyp.domain.ImagePrintingSetting;
import com.example.fyp.domain.ImagePrintingSettingPreferences;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;

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

public class PhotoPrintingSettingFragment extends Fragment implements  View.OnClickListener{

    //View
    private View root;
    private CheckBox cb_3rPhotoPrefSetting ;
    private CheckBox cb_4rPhotoPrefSetting;
    private CheckBox cb_5rPhotoPrefSetting;

    private CheckBox cb_glossyPhotoPrefSetting;
    private CheckBox cb_mattePhotoPrefSetting;
    private CheckBox cb_cardStackPhotoPrefSetting;

    private CheckBox cb_noBorder_setting;
    private CheckBox cb_5Border_setting;
    private CheckBox cb_10Border_setting;

    private CheckBox cb_availabilty;


    private EditText et_pricePhotoPrefSetting;

    private EditText et_minCopiesPhotoPrefSetting;
    private EditText et_maxCopiesPhotoPrefSetting;

    private Button btn_continuePhotoPrefSetting;

    private ProgressDialog dialog;

    //Object
    private User user;
    private ImagePrintingSettingPreferences imagePrintingSettingPreferences;
    private ArrayList<ImagePrintingSetting> imagePrintingSetting = new ArrayList<>();

    //Data
    private ArrayList<String>  PaperSize = new ArrayList<>();
    private ArrayList<String>  PaperType= new ArrayList<>();
    private String minQuantity;
    private String maxQuantity;
    private String available;
    private String pricePerPage;

    private ArrayList<String>  border= new ArrayList<>();           //For Wooden Stand Lamination, soOn

    private String data;
    private String action;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_photo_printing_setting, container, false);
        linkXML();
        InitialData();
        ViewListener();
        return root;
    }

    private void linkXML() {
        cb_3rPhotoPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_3rPhotoPrefSetting);
        cb_4rPhotoPrefSetting= (CheckBox) root.getRootView().findViewById(R.id.cb_4rPhotoPrefSetting);
        cb_5rPhotoPrefSetting= (CheckBox) root.getRootView().findViewById(R.id.cb_5rPhotoPrefSetting);

        cb_glossyPhotoPrefSetting= (CheckBox) root.getRootView().findViewById(R.id.cb_glossyhotoPrefSetting);
        cb_mattePhotoPrefSetting= (CheckBox) root.getRootView().findViewById(R.id.cb_mattePhotoPrefSetting);
        cb_cardStackPhotoPrefSetting= (CheckBox) root.getRootView().findViewById(R.id.cb_cardStackPhotoPrefSetting);

        cb_noBorder_setting= (CheckBox) root.getRootView().findViewById(R.id.cb_noBorder_setting);
        cb_5Border_setting= (CheckBox) root.getRootView().findViewById(R.id.cb_5Border_setting);
        cb_10Border_setting= (CheckBox) root.getRootView().findViewById(R.id.cb_10Border_setting);

        cb_availabilty= (CheckBox) root.getRootView().findViewById(R.id.cb_availability);

        et_pricePhotoPrefSetting = (EditText) root.getRootView().findViewById(R.id.et_pricePhotoPrefSetting);

        et_minCopiesPhotoPrefSetting= (EditText) root.getRootView().findViewById(R.id.et_minCopiesPhotoPrefSetting);
        et_maxCopiesPhotoPrefSetting= (EditText) root.getRootView().findViewById(R.id.et_maxCopiesPhotoPrefPrinting);

        btn_continuePhotoPrefSetting=(Button) root.getRootView().findViewById(R.id.btn_continuePhotoPrefSetting);  }

    private void InitialData() {
        user = Global.user;
        JSONObject data = new JSONObject();
        try {
            data.put("printer_id",user.getPrinter().getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        photoPrintingSettingBW photoPrintingSettingBW = new photoPrintingSettingBW();
        photoPrintingSettingBW.execute("read",data.toString());

    }

    private void CreateLayoutView() {
        //PaperSize
        for(int i=0;i<PaperSize.size();i++){
            if(PaperSize.get(i).equals("3.5x5(3R)")){
                cb_3rPhotoPrefSetting.setChecked(true);
            }else if(PaperSize.get(i).equals("4X6(4R)")){
                cb_4rPhotoPrefSetting.setChecked(true);
            }else if(PaperSize.get(i).equals("5x5(5R)")){
                cb_5rPhotoPrefSetting.setChecked(true);
            }
        }

        //Paper Type
        for(int i=0;i<PaperType.size();i++){
            if(PaperType.get(i).equals("CardStack")){
                cb_cardStackPhotoPrefSetting.setChecked(true);

            }else if(PaperType.get(i).equals("Glossy")){
                cb_glossyPhotoPrefSetting.setChecked(true);
            }else if(PaperType.get(i).equals("Matte")){
                cb_mattePhotoPrefSetting.setChecked(true);
            }
        }

        //border
        for(int i=0;i<border.size();i++){
            if(border.get(i).equals("No Border")){
                cb_noBorder_setting.setChecked(true);
            }else if(border.get(i).equals("5cm x 5cm")){
               cb_5Border_setting.setChecked(true);
            }else if(border.get(i).equals("10cm x 10cm")){
               cb_10Border_setting.setChecked(true);
            }
        }


        //minCopies
        et_minCopiesPhotoPrefSetting.setText(minQuantity);

        //maxCopies
        et_maxCopiesPhotoPrefSetting.setText(maxQuantity);

        //price per photo
        if(pricePerPage!=null) {
            et_pricePhotoPrefSetting.setText(pricePerPage);
        }

        //available
        if(available.equals("Yes")) {
            cb_availabilty.setChecked(true);
        }
        clearData();
    }

    private void ViewListener() {
        btn_continuePhotoPrefSetting.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_continuePhotoPrefSetting:
                fetchAllDataFromViews();

                if(VerificationOfSelection()) {
                    //Convert to Object
                    imagePrintingSettingPreferences = new ImagePrintingSettingPreferences(PaperSize, PaperType, minQuantity, maxQuantity, available, border,
                            pricePerPage);

                    JSONObject data = new JSONObject();
                    try {
                        data.put("img_print_pref_json", ImagePrintingSettingPreferences.OBJtoJSON(imagePrintingSettingPreferences));
                        data.put("printer_id", user.getPrinter().getPrinter_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    photoPrintingSettingBW photoPrintingSettingBW = new photoPrintingSettingBW();
                    photoPrintingSettingBW.execute("update", data.toString());
                    clearData();
                }else{
                    Global.displayToast(getContext(),"Please fill in everything correctly",Toast.LENGTH_SHORT,"yellow");
                }

        }
    }

    private boolean VerificationOfSelection() {
        boolean valid =false;
        int error =0;
        //PaperSize, PaperType, minQuantity, maxQuantity, available, border,
          //      pricePerPage
        if(PaperSize.size()==0){
            error+=1;
        }

        if(PaperType.size()==0){
            error+=1;
        }

        if(border.size()==0){
            error+=1;
        }
        if(VerificationOfCopies(minQuantity,maxQuantity)==0){
            error+=1;
        }

        if(VerificationOfPrice(pricePerPage)==0){
            Global.displayToast(getContext(),"Invalid price entered",Toast.LENGTH_SHORT,"yellow");
            error+=1;
        }

        if(error==0){
            valid = true;
        }else{
            valid = false;
        }
        return valid;
    }
    private int VerificationOfCopies(String minCopies,String maxCopies){
        //Verify if it is number
        int valid = 0;
        int error= 0;
        if((!(minCopies.matches("[0-9]+")))&&(!(maxCopies.matches("[0-9]+")))){
            error+=1;
            Global.displayToast(getContext(),"Min Copies and Max Copies must be number",Toast.LENGTH_SHORT,"yellow");
        }else if(Integer.parseInt(minCopies)>Integer.parseInt(maxCopies)){
            error+=1;
            Global.displayToast(getContext(),"Min Copies cannot be more than max copies",Toast.LENGTH_SHORT,"yellow");
        }

        if(error==0){
            valid = 1;
        }else{
            valid = 0;
        }

        return valid;
    }

    private int VerificationOfPrice(String pricePerColorPage){
        int valid= 1;
        int error=0;
        int isDouble=1;
        double Double_pricePerColorPage=0.00;

        try {
            Double_pricePerColorPage= Double.parseDouble(pricePerColorPage);
        } catch (NumberFormatException ignore) {
            isDouble =0;
            error+=1;
        }
        //Verification of price more than 0
        if(isDouble==1){
            if(Double_pricePerColorPage>0){
            }else{
                error+=1;
            }
        }
        if(error==0){
            valid = 1;
        }else{
            valid =0;
        }
        return valid;
    }


    private void fetchAllDataFromViews() {
        //Size
        if (cb_3rPhotoPrefSetting.isChecked()) {
            PaperSize.add("3.5x5(3R)");
        }
        if (cb_4rPhotoPrefSetting.isChecked()) {
            PaperSize.add("4X6(4R)");
        }
        if (cb_5rPhotoPrefSetting.isChecked()) {
            PaperSize.add("5X5(5R)");
        }

        //PaperType
        if (cb_cardStackPhotoPrefSetting.isChecked()) {
            PaperType.add("CardStack");
        }

        if (cb_glossyPhotoPrefSetting.isChecked()) {
            PaperType.add("Glossy");
        }
        if (cb_mattePhotoPrefSetting.isChecked()) {
            PaperType.add("Matte");
        }

        //Additional
        if (cb_noBorder_setting.isChecked()) {
            border.add("No Border");
        }
        if (cb_5Border_setting.isChecked()) {
            border.add("5cm x 5cm");
        }

        if (cb_10Border_setting.isChecked()) {
            System.out.println("BORDER CHECKED");
            border.add("10cm x 10cm");
        }

        //Copies
        maxQuantity = et_maxCopiesPhotoPrefSetting.getText().toString();
        minQuantity = et_minCopiesPhotoPrefSetting.getText().toString();

        //availability
        if(cb_availabilty.isChecked()){
            available="Yes";
        }else{
            available="No";
        }
        pricePerPage= et_pricePhotoPrefSetting.getText().toString();

    }

    private void clearData() {
        PaperSize.clear();
        PaperType.clear();
        border.clear();
        pricePerPage=null;
        minQuantity=null;
        maxQuantity =null;
        available=null;

    }
    private void InitiateVariable(){
        PaperSize = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getPaperSize();
        PaperType = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getPaperType();
        border = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getBorder();
        pricePerPage = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getpricePerPage();
        minQuantity=imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getminQuantity();
        maxQuantity = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getmaxQuantity();
        available=imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getAvailable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class photoPrintingSettingBW extends AsyncTask<String,Void,String[]> {
        public photoPrintingSettingBW() {
            dialog = new ProgressDialog(getActivity());
        }


        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading");
            dialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        // In this method we have to do background operation on background thread.
        // Operations in this method should not touch on any mainthread activities or fragments.
        protected String[] doInBackground(String... params) {
            action = params[0];
            data = params[1];
            String UserURL = Global.getURL()+"CRUD_printer_imagesPreferencesSetting.php";


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
            dialog.setMessage("Loading");
            dialog.show();
        }

        @Override
        //In this method we can update ui of background operation result.
        protected void onPostExecute(String[] result) {
            if(result[0].equals("connection success")){
                System.out.println(result[1]);
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    if(action.equals("read")) {
                        System.out.println(response.getData());
                        imagePrintingSetting = ImagePrintingSetting.JSONToLIST(response.getData());
                        if ((imagePrintingSetting.get(0).getImagePrintingSettingPreferences().equals("default"))) {
                            dialog.dismiss();
                            Global.displayToast(getContext(),"Fill up everything to start your services",Toast.LENGTH_SHORT,"blue");
                        }else{
                            InitiateVariable();
                            CreateLayoutView();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        Global.displayToast(getContext(), "Sucessfully saved", Toast.LENGTH_SHORT,"green");
                    }
                }else{
                    dialog.dismiss();
                    System.out.println("Cant fetch data from server");
                }
            }
        }

    }

}