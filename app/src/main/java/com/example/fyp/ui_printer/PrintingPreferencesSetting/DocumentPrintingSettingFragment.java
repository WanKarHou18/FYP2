package com.example.fyp.ui_printer.PrintingPreferencesSetting;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.DocumentPrintingSetting;
import com.example.fyp.domain.DocumentPrintingSettingPreferences;
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


public class DocumentPrintingSettingFragment extends Fragment implements  View.OnClickListener {

    //View
    private View root;
    private Button btn_saveDocPrefSetting;
    private CheckBox cb_colorDocPrefSetting;
    private CheckBox cb_blackWhiteDocPrefSetting;
    private CheckBox cb_potraitDocPrefSetting;
    private CheckBox cb_landscapeDocPrefSetting;
    private CheckBox cb_shortEdgeDocPrefSetting;
    private CheckBox cb_longEdgeDocPrefSetting;
    private CheckBox cb_oneSlidedDocPrefSetting;
    private CheckBox cb_doubleSlidedDocPrefSetting;
    private CheckBox cb_1DocPrefSetting;
    private CheckBox cb_2DocPrefSetting;
    private CheckBox cb_4DocPrefSetting;
    private CheckBox cb_6DocPrefSetting;
    private CheckBox cb_8DocPrefSetting;
    private CheckBox cb_horizontalDocPrefSetting;
    private CheckBox cb_verticalDocPrefSetting;
    private CheckBox cb_availableDocPrefSetting;

    private EditText et_minCopiesDocPrefSetting;
    private EditText et_maxCopiesDocPrefSetting;
    private EditText  et_pricePerPageBWDocPrefSetting;
    private EditText et_pricePerPageColorDocPrefSetting;

    private ProgressDialog dialog;
    //Object
    private DocumentPrintingSettingPreferences documentPrintingSettingPreferences;
    private ArrayList<DocumentPrintingSetting> documentPrintingSetting;
    private DocumentPrintingSetting documentPrintingSettingDefault;
    private DocumentPrintingSetting documentPrintingSetting1;
    private User user;

    //Data
    private ArrayList<String> ColorSelected = new ArrayList<>(); //BlackWhit, Color
    private ArrayList<String> PL= new ArrayList<>();   //Potraint or Landscape
    private ArrayList<String> Edge= new ArrayList<>(); //LongEdge or ShortEdge
    private ArrayList<String> Slided= new ArrayList<>(); //One Slided or Double Slided
    private ArrayList<String> SlidesPerPage= new ArrayList<>(); //1,2,4,6,10
    private ArrayList<String> SlidesArrangement= new ArrayList<>(); //Horizontal, Vertical

    private String minCopies ;
    private String maxCopies ;

    private  String pricePerColorPage; //Prices for one color page
    private  String pricePerBlackWhitePage; //Price for one blackwhite page

    private String available;

    private String data;
    private String action;

    //Manager
   // private FragmentTransaction ft = getParentFragmentManager().beginTransaction();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root =inflater.inflate(R.layout.fragment_document_printing_setting, container, false);
        linkXML();
        initiateData();
        ViewListener();
        return root;
    }

    private void linkXML() {
        btn_saveDocPrefSetting = (Button) root.getRootView().findViewById(R.id.btn_saveDocPrefSetting);
        cb_colorDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_colorDocPrefSetting);
        cb_blackWhiteDocPrefSetting  = (CheckBox) root.getRootView().findViewById(R.id.cb_blackWhiteDocPrefSetting );

        cb_potraitDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_potraitDocPrefSetting);
        cb_landscapeDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_landscapeDocPrefSetting);

        cb_shortEdgeDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_shortEdgeDocPrefSetting);
        cb_longEdgeDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_longEdgeDocPrefSetting);

        cb_oneSlidedDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_oneSlidedDocPrefSetting);
        cb_doubleSlidedDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_doubleSlidedDocPrefSetting);

        cb_1DocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_1DocPrefSetting);
        cb_2DocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_2DocPrefSetting);
        cb_4DocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_4DocPrefSetting);
        cb_6DocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_6DocPrefSetting);
        cb_8DocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_8DocPrefSetting);

        cb_horizontalDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_horizontalDocPrefSetting);
        cb_verticalDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_verticalDocPrefSetting);

        cb_availableDocPrefSetting = (CheckBox) root.getRootView().findViewById(R.id.cb_availableDocPrefSetting);

        et_minCopiesDocPrefSetting  = (EditText) root.getRootView().findViewById(R.id.et_minCopiesDocPrefSetting);
        et_maxCopiesDocPrefSetting = (EditText) root.getRootView().findViewById(R.id.et_maxCopiesDocPrefSetting);

        et_pricePerPageBWDocPrefSetting= (EditText) root.getRootView().findViewById(R.id.et_pricePerPageBWDocPrefSetting);
        et_pricePerPageColorDocPrefSetting= (EditText) root.getRootView().findViewById(R.id.et_pricePerPageColorDocPrefSetting);
    }

    private void initiateData() {

        user = Global.user;

        JSONObject data = new JSONObject();
        try {
            data.put("printer_id",user.getPrinter().getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        printerDocumentPrintigSettingBW printerDocumentPrintigSettingBW = new printerDocumentPrintigSettingBW();
        printerDocumentPrintigSettingBW.execute("read",data.toString());
;
    }

    private void createLayoutView() {
        //Color Selected
        for(int i=0;i<ColorSelected.size();i++){
            if(ColorSelected.get(i).equals("Color")){
                cb_colorDocPrefSetting.setChecked(true);
            }else{
               cb_blackWhiteDocPrefSetting.setChecked(true);
            }

        }
        //PL
        for(int i=0;i<PL.size();i++){
            if(PL.get(i).equals("Potrait")){
                cb_potraitDocPrefSetting.setChecked(true);
            }else{
                cb_landscapeDocPrefSetting.setChecked(true);
            }
        }
        //Edge
        for(int i=0;i<Edge.size();i++){
            if(Edge.get(i).equals("LongEdge")){
                cb_longEdgeDocPrefSetting.setChecked(true);
            }else{
                cb_shortEdgeDocPrefSetting.setChecked(true);
            }
        }

        //Slided
        for(int i =0;i<Slided.size();i++){
            if(Slided.get(i).equals("One-Sided")){
                cb_oneSlidedDocPrefSetting.setChecked(true);
            }else{
                cb_doubleSlidedDocPrefSetting.setChecked(true);
            }

        }
        //SlidesPerPage
        for(int i =0;i<SlidesPerPage.size();i++){
            if(SlidesPerPage.get(i).equals("1")){
                cb_1DocPrefSetting.setChecked(true);
            }else if(SlidesPerPage.get(i).equals("2")){
                cb_2DocPrefSetting.setChecked(true);
            }else if(SlidesPerPage.get(i).equals("4")){
                cb_4DocPrefSetting.setChecked(true);
            }else if(SlidesPerPage.get(i).equals("6")){
                cb_6DocPrefSetting.setChecked(true);
            }
        }

        //Slides Arrangement
        for(int i =0;i<SlidesArrangement.size();i++){
            if(SlidesArrangement.get(i).equals("Vertical")){
                cb_verticalDocPrefSetting.setChecked(true);
            }else{
                cb_horizontalDocPrefSetting.setChecked(true);
            }
        }

        //Availability
        if(available.equals("Yes")){
            cb_availableDocPrefSetting.setChecked(true);
        }

        //minCopes
        et_minCopiesDocPrefSetting.setText(minCopies);
        //MaxCopies
        et_maxCopiesDocPrefSetting.setText(maxCopies);

        et_pricePerPageColorDocPrefSetting.setText(pricePerColorPage);
        et_pricePerPageBWDocPrefSetting.setText(pricePerBlackWhitePage);
        ClearData();
    }

    private void ViewListener() {
        btn_saveDocPrefSetting.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_saveDocPrefSetting:
                fetchDataFromAllViews();
                if(VerificationOfSelection() ) {
                    //form Object-  DocumentPrintingSettingFragment
                    documentPrintingSettingPreferences = new DocumentPrintingSettingPreferences(ColorSelected, PL, Edge, Slided, SlidesPerPage, SlidesArrangement,
                            minCopies, maxCopies, pricePerColorPage, pricePerBlackWhitePage, available);
                    System.out.println(DocumentPrintingSettingPreferences.OBJtoJSON(documentPrintingSettingPreferences));

                    //Set it to document printing setting
                    if(documentPrintingSetting!=null){
                        documentPrintingSetting.get(0).setdocumentPrintingSettingPreferences(documentPrintingSettingPreferences);
                    }

                    JSONObject data = new JSONObject();
                    try {
                        data.put("doc_print_pref_json", DocumentPrintingSettingPreferences.OBJtoJSON(documentPrintingSettingPreferences));
                        data.put("printer_id", user.getPrinter().getPrinter_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    printerDocumentPrintigSettingBW printerDocumentPrintigSettingBW = new printerDocumentPrintigSettingBW();
                    printerDocumentPrintigSettingBW.execute("update", data.toString());
                    ClearData();
                }else{
                    Global.displayToast(getContext(),"Please fill in everything correctly",Toast.LENGTH_SHORT,"yellow");
                }
               // ft.replace(R.id.fl1, new PhotoPrintingSettingFragment());
                break;
        }
    }

    private void ClearData() {
        ColorSelected.clear(); //BlackWhit, Color
        PL.clear();   //Potraint or Landscape
        Edge.clear(); //LongEdge or ShortEdge
        Slided.clear(); //One Slided or Double Slided
        SlidesPerPage.clear(); //1,2,4,6,10
        SlidesArrangement.clear(); //Horizontal, Vertical



         minCopies = null;
         maxCopies = null;
        pricePerColorPage = null; //Prices for one color page
        pricePerBlackWhitePage = null; //Price for one blackwhite page
        available=null;
    }

    public void fetchDataFromAllViews(){
        //Color/BlackWhite
        if(cb_colorDocPrefSetting.isChecked()){
            ColorSelected.add("Color");
        }
        if(cb_blackWhiteDocPrefSetting.isChecked()){
            ColorSelected.add("BlackWhite");
        }
        // PL
        if(cb_potraitDocPrefSetting.isChecked()){
            PL.add("Portrait");
        }
        if(cb_landscapeDocPrefSetting.isChecked()){
            PL.add("Landscape");

        }
        //Edge
        if(cb_shortEdgeDocPrefSetting.isChecked()){
            Edge.add("ShortEdge");
        }

        if(cb_longEdgeDocPrefSetting.isChecked()){
            Edge.add("LongEdge");
        }

        //Slided
        if(cb_oneSlidedDocPrefSetting.isChecked()){
            Slided.add("One-Sided");
        }

        if(cb_doubleSlidedDocPrefSetting.isChecked()){
            Slided.add("Two-Sided");
        }

        //Slides Per Page
        if(cb_1DocPrefSetting.isChecked()){
            SlidesPerPage.add("1");
        }
        if(cb_2DocPrefSetting.isChecked()){
            SlidesPerPage.add("2");
        }
        if(cb_4DocPrefSetting.isChecked()){
            SlidesPerPage.add("4");
        }
        if(cb_6DocPrefSetting.isChecked()){
            SlidesPerPage.add("6");
        }
        if(cb_8DocPrefSetting.isChecked()){
            SlidesPerPage.add("8");
        }

        //Slides Arrangment
        if(cb_horizontalDocPrefSetting.isChecked()){
            SlidesArrangement.add("Horizontal");
        }
        if(cb_verticalDocPrefSetting.isChecked()){
            SlidesArrangement.add("Vertical");
        }

        //Availability
        if(cb_availableDocPrefSetting.isChecked()){
            available="Yes";
        }else{
            available="No";
        }
        minCopies = et_minCopiesDocPrefSetting.getText().toString();
        maxCopies = et_maxCopiesDocPrefSetting.getText().toString();

        pricePerBlackWhitePage = et_pricePerPageBWDocPrefSetting.getText().toString();
        pricePerColorPage=et_pricePerPageColorDocPrefSetting.getText().toString();

    }

    private boolean VerificationOfSelection(){
        boolean valid = false;
        int error =0;
        if(ColorSelected.size()==0){
            error+=1;
        }
        if(PL.size()==0){
            error+=1;
        }
        if(Edge.size()==0){
            error+=1;
        }
        if(Slided.size()==0){
            error+=1;
        }
        if(SlidesArrangement.size()==0){
            error+=1;
        }
        if(SlidesPerPage.size()==0){
            error+=1;
        }
        if(VerificationOfCopies(minCopies,maxCopies)==0){
            error+=1;
        }
        if(VerificationOfPrice(pricePerBlackWhitePage,pricePerColorPage)==0){
            error+=1;
        }

        if(error==0){
            System.out.println(error);
            valid = true;
        }else{
            valid=false;
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

    private int VerificationOfPrice(String pricePerBlackWhitePage,String pricePerColorPage){
       int valid= 0;
        int error=0;
        int isDouble_Color=1;
        int isDouble_BW =1;
        double Double_pricePerBlackWhite=0.00;
        double Double_pricePerColorPage=0.00;
        //PricePerBlackWhite
        try {
            Double_pricePerBlackWhite= Double.parseDouble(pricePerBlackWhitePage);
        } catch (NumberFormatException ignore) {
            isDouble_BW=0;
            error+=1;
        }
        //Verification of price more than 0
        if(isDouble_BW==1){
            if(Double_pricePerBlackWhite>0){
            }else{
                error+=1;
            }
        }
        try {
            Double_pricePerColorPage= Double.parseDouble(pricePerColorPage);
        } catch (NumberFormatException ignore) {
            isDouble_Color=0;
            error+=1;
        }

        //Verification of price more than 0
        if(isDouble_Color==1){
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

    private void initiateVariable() {
        ColorSelected = documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getColorSelected();
        PL=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getPL();
        Edge=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getEdge();
        Slided=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getSlided();
        SlidesPerPage=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getSlidesPerPage();
        SlidesArrangement=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getSlidesArrangement();
        minCopies=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getminCopies();
        maxCopies=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getmaxCopies();
        pricePerColorPage=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getpricePerColorPage();
        pricePerBlackWhitePage =documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getpricePerBlackWhitePage();
        available=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getAvailable();
    }


    private class printerDocumentPrintigSettingBW extends AsyncTask<String,Void,String[]> {
        public printerDocumentPrintigSettingBW() {
            dialog = new ProgressDialog(getActivity()); }


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
            String UserURL = Global.getURL()+"CRUD_printer_documentPreferencesSetting.php";


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
                System.out.println(result[1
                        ]);
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {

                    if(action.equals("read")) {
                        System.out.println(response.getData());
                        documentPrintingSetting = DocumentPrintingSetting.JSONToLIST(response.getData());
                        if ((documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().equals("default"))) {
                            dialog.dismiss();
                            Global.displayToast(getContext(),"Fill up everything to start your services",Toast.LENGTH_SHORT,"blue");

                        }else{
                            initiateVariable();
                            createLayoutView();
                            dialog.dismiss();

                        }
                    }else{

                        //If action is update
                        dialog.dismiss();
                        Global.displayToast(getContext(),"Sucessfully saved",Toast.LENGTH_SHORT,"green");
                    }
                }else{
                    //If fail , create an default document printing setting
                        //  -Means user first time login and display
                   /*
                    documentPrintingSettingPreferences = new DocumentPrintingSettingPreferences(ColorSelected, PL, Edge, Slided, SlidesPerPage, SlidesArrangement,
                            minCopies, maxCopies, pricePerColorPage, pricePerBlackWhitePage, available);
                    documentPrintingSettingDefault.setdocumentPrintingSettingPreferences(documentPrintingSettingPreferences);
                    documentPrintingSetting.add(documentPrintingSettingDefault);

                    */
                    dialog.dismiss();
                }
            }
        }


    }
}