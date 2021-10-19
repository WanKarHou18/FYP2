package com.example.fyp.ui_customer.Orders;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.DocumentPreferences;
import com.example.fyp.domain.DocumentPrintingSetting;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Product_Printing_Preferences;
import com.example.fyp.domain.ResourcesRecord;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderDocumentPreferencesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

    //View
    View root;
    private RadioGroup rgSelectColor;
    private RadioButton rbBlackWhite;
    private RadioButton rbColor;

    private RadioGroup rgPL;
    private RadioButton rbPotrait;
    private RadioButton rbLandScape;

    private RadioGroup rgEdge;
    private RadioButton rbLongEdge;
    private RadioButton rbShortEdge;

    private RadioGroup rgSlides;
    private RadioButton rbDoubleSlided;
    private RadioButton rbOneSlided;

    private RadioGroup rgSlidesArrange;
    private RadioButton rbHorizontal;
    private RadioButton rbVertical;

    private CheckBox cbAllPages;
    private EditText etPageRange;

    private EditText et_copies;
    private Spinner spinner_slide;
    private Button btnContinue;


    private ProgressDialog dialog;
    //Object
    private Printer printer;

    private ArrayList<DocumentPrintingSetting> documentPrintingSetting = new ArrayList<>();

    private DocumentPreferences documentPreferences;
    private Product_Printing_Preferences product_printing_preferences;
    private Sub_Orders sub_order ;
    private ResourcesRecord  resourcesRecord;
    private ArrayList<Sub_Orders> sub_orders= new ArrayList();

    //Data
    /*For doc print pref of selected order*/
    private String ColorSelected ="default";
    private String PL ="default";
    private String Edge="default";
    private String Slided ="default";
    private String PageRange ="default";
    private String SlidesArrangment="default";
    private String SlidesPerPage = "default";
    private String Copies="default";

    /*For Doc Printing Pref Setting*/
    private ArrayList<String> ColorSelected_setting = new ArrayList<>(); //BlackWhit, Color
    private ArrayList<String> PL_setting = new ArrayList<>();   //Potraint or Landscape
    private ArrayList<String> Edge_setting= new ArrayList<>(); //LongEdge or ShortEdge
    private ArrayList<String> Slided_setting= new ArrayList<>(); //One Slided or Double Slided
    private ArrayList<String> SlidesPerPage_setting= new ArrayList<>(); //1,2,4,6,10
    private ArrayList<String> SlidesArrangement_setting= new ArrayList<>(); //Horizontal, Vertical

    private String minCopies_setting ;
    private String maxCopies_setting ;

    private  String pricePerColorPage_setting; //Prices for one color page
    private  String pricePerBlackWhitePage_setting; //Price for one blackwhite page

    private String available_setting;

    /*For Making Order*/
    private String printing_pref_id ="default";

    private String subOrderId ="default";
    private String orderId ="default";
    private String printingPrefId ="default";
    private String fileName ="default";
    private String fileType ="default";
    private String resources_id ="default";
    private String cost ="default";
    private String file_description ="default";


    /*Others*/
    private String action;
    private String data;
    private String printer_json;

    public OrderDocumentPreferencesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_order_document_preferences, container, false);
        linkXML();
        initiateData();
        ViewListener();


        return root;
    }

    private void linkXML() {
        rgSelectColor = root.findViewById(R.id.rgSelectColor);
        rgPL = root.findViewById(R.id.rgPL);
        rgEdge = root.findViewById(R.id.rgEdge);
        rgSlides = root.findViewById(R.id.rg_Slides);
        rgSlidesArrange=root.findViewById(R.id.rgSlidesArrange);

        cbAllPages = root.findViewById(R.id.cbAllPages);
        rbOneSlided = root.findViewById(R.id.rbOneSlided);
        rbDoubleSlided = root.findViewById(R.id.rbDoubleSlided);
        rbHorizontal = root.findViewById(R.id.rb_horizontal);
        rbVertical=root.findViewById(R.id.rb_vertical);
        rbBlackWhite=root.findViewById(R.id.rbBlackWhite);
        rbColor=root.findViewById(R.id.rbColor);
        rbPotrait=root.findViewById(R.id.rbPortrait);
        rbLandScape=root.findViewById(R.id.rbLandscape);
        rbLongEdge=root.findViewById(R.id.rbLongEdge) ;
        rbShortEdge=root.findViewById(R.id.rbShortEdge);

        btnContinue = root.findViewById(R.id.btnContinue);
        etPageRange = root.findViewById(R.id.etPageRange);
        et_copies = root.findViewById(R.id.et_copies);
        spinner_slide = root.findViewById(R.id.spinner_slide);
    }


    private void initiateData() {
        printer = Printer.JSONToOBJ(Global.printer_json);

        JSONObject data = new JSONObject();
        try {
            data.put("printer_id",printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        printerDocumentPrintigSettingBW printerDocumentPrintigSettingBW = new printerDocumentPrintigSettingBW();
        printerDocumentPrintigSettingBW.execute("read",data.toString());

    }

    private void createLayoutView() {
        createInitialLayoutView();
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,SlidesPerPage_setting);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_slide.setAdapter(aa);
        HandleLayout();
        Global.displayToast(getContext(),"Min Copies:"+minCopies_setting+"  "+"Max Copies:"+maxCopies_setting,Toast.LENGTH_SHORT,"blue");

    }

    private void createInitialLayoutView() {
        //Color Selected
        int BW_count=0;
        int Color_count=0;
        for(int i=0;i<ColorSelected_setting.size();i++){
            if(ColorSelected_setting.get(i).equals("BlackWhite")){
                BW_count+=1;
            }else if(ColorSelected_setting.get(i).equals("Color")){
                Color_count+=1;
            }

            if(i==(ColorSelected_setting.size()-1)){
                if(BW_count==0){
                    rbBlackWhite.setClickable(false);

                }else if(Color_count==0){
                    rbColor.setClickable(false);

                }
            }
        }

        //PL Setting
        int Potrait_count=0;
        int Landscape_count=0;

        for(int i=0;i<PL_setting.size();i++){
            if(PL_setting.get(i).equals("Portrait")){
                Potrait_count+=1;
            }else if(PL_setting.get(i).equals("Landscape")){
                Landscape_count+=1;
            }

            if(i==(PL_setting.size()-1)){
                if(Potrait_count==0){
                    rbPotrait.setClickable(false);

                }else if (Landscape_count==0){
                    rbLandScape.setClickable(false);

                }
            }
        }

        //Slided
        int doubleSlided_count=0;
        int oneSlided_count=0;
        for(int i=0;i<Slided_setting.size();i++){
            if(Slided_setting.get(i).equals("DoubleSlided")){
                doubleSlided_count+=1;
            }else if(Slided_setting.get(i).equals("OneSlided")){
                oneSlided_count+=1;
            }
            if(i==(Slided_setting.size()-1)){
                if(doubleSlided_count==0){
                    rbDoubleSlided.setClickable(false);
                }else if(oneSlided_count==0){
                    rbOneSlided.setClickable(false);
                }
            }
        }

        //Slides Arrangement
        int Horizontal_count=0;
        int Vertical_count=0;
        for(int i=0;i<SlidesArrangement_setting.size();i++){
            if(SlidesArrangement_setting.get(i).equals("Horizontal")){
                Horizontal_count+=1;
            }else if(SlidesArrangement_setting.get(i).equals("Vertical")){
                Vertical_count+=1;
            }
            if(i==(SlidesArrangement_setting.size()-1)){
                if(Horizontal_count==0){
                    rbHorizontal.setClickable(false);
                }else if(Vertical_count==0){
                    rbVertical.setClickable(false);
                }
            }
        }

        //Edge Setting
        int shortEdge_count =0;
        int longEdge_count=0;
        for(int i=0;i<Edge_setting.size();i++){
            if(Edge_setting.get(i).equals("ShortEdge")){
                shortEdge_count+=1;

            }else if(Edge_setting.get(i).equals("LongEdge")){
                longEdge_count+=1;
            }
            if(i==(Edge_setting.size()-1)){
                if(shortEdge_count==0){
                    rbShortEdge.setClickable(false);
                }else if(longEdge_count==0){
                    rbLongEdge.setClickable(false);
                }

            }
        }


    }
    private void ViewListener() {
        btnContinue.setOnClickListener(this::onClick);
        spinner_slide.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }


    private void HandleLayout() {
        //GET ALL DATA FROM ALL RADIO GROUPS

        //
        //RG - BLACK / WHITE
        //

        rgSelectColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbBlackWhite:
                        ColorSelected="BlackWhite";
                        Global.displayToast(getContext(),"Cost per page:RM"+pricePerBlackWhitePage_setting,Toast.LENGTH_SHORT,"blue");
                        break;
                    case R.id.rbColor:
                        ColorSelected = "Color";
                        Global.displayToast(getContext(),"Cost per page:RM"+pricePerColorPage_setting,Toast.LENGTH_SHORT,"blue");
                        break;

                }
            }
        });

    //
    //RG - POTRAIT /LANDSCAPE
    //
    rgPL.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (RadioGroup group,int checkedId) {
            switch (checkedId) {
                case R.id.rbPortrait:
                    PL = "Portrait";
                    break;
                case R.id.rbLandscape:
                    PL = "Landscape";
                    break;
            }
        }
    });
        //
        //RG - LONG EDGE / SHORT EDGE
        //
        rgEdge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                switch (checkedId) {
                    case R.id.rbLongEdge:
                        Edge = "LongEdge";
                        break;
                    case R.id.rbShortEdge:
                        Edge = "ShortEdge";
                        break;

                }
            }
        });

        //
        //RG - ONESLIDED / DOUBLE SLIDED
        //
        rgSlides.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId) {
                switch (checkedId) {
                    case R.id.rbDoubleSlided:
                        Slided = "Two-Sided";
                        break;
                    case R.id.rbOneSlided:
                        Slided = "One-Sided";
                        break;

                }
            }
        });

        //rg - Horizontal/Vertical
        rgSlidesArrange.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId) {
                switch (checkedId) {
                    case R.id.rb_horizontal:
                         SlidesArrangment= "Horizontal";
                        break;
                    case R.id.rb_vertical:
                        SlidesArrangment = "Vertical";
                        break;

                }
            }
        });

    }


    //RG - SELECT ALL PAGES / PAGE RANGE
    private void GetPageRange(){
        if(cbAllPages.isChecked()){
            PageRange = "All";
        }else{
            PageRange = etPageRange.getText().toString();
        }
    }

    //Get copies
    private void GetCopies(){
        Copies = et_copies.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectDocument:
                // *** The Upload File Function is in Activity ***
                break;
            case R.id.btnContinue:
                //Fetch Data
                GetPageRange();
                GetCopies();
                fileName = Global.FileName;
                fileType = Global.FileType;

                //Verify if everything is selected
                if(VerificationOfSelection()) {

                    //
                    //Create JSON for DocumentsPreferences
                    //
                    documentPreferences = new DocumentPreferences(ColorSelected, PL, Edge, Slided, PageRange, SlidesPerPage, SlidesArrangment, Copies);
                    String documentPreferences_json = DocumentPreferences.OBJtoJSON(documentPreferences);
                    System.out.println(documentPreferences_json);

                    //
                    //  Set Printing Preferences at product printing preferences , then set the product printing preferences
                    //  at the sub_order
                    //

                    product_printing_preferences = new Product_Printing_Preferences(printing_pref_id, documentPreferences_json, subOrderId);


                    //Calculate Cost
                    cost = String.valueOf(CalculateCost());
                    //DecimalFormat df = new DecimalFormat("#,##0.00");
                   // cost = df.format(cost);

                    //Create Resources Record
                    resourcesRecord = new ResourcesRecord(resources_id, fileName, fileType, file_description, subOrderId);

                    sub_order = new Sub_Orders(subOrderId, orderId, cost, product_printing_preferences, resourcesRecord);
                    Global.sub_order = sub_order;

                    System.out.println(Sub_Orders.OBJtoJSON(Global.sub_order));
                    Intent i = new Intent(getActivity(), CustOrderThirdActivity.class);
                    startActivity(i);
                    break;
                    }else{
                    Global.displayToast(this.getContext(),"Please fill in everything correctly",Toast.LENGTH_SHORT,"yellow");
                }
                }
    }

    private double CalculateCost(){
        double Cost =  0.00;
        int Page=0;
        if(ColorSelected.equals("BlackWhite")){
            //SlidesPerPage
            Page =Math.round(Global.PageCount/Integer.parseInt(SlidesPerPage));
            //Double Slided or One Slided
            if(Slided.equals("Two-Sided")){
                Page = Page/2;
            }
            System.out.println("Document Page:"+Page);
            Cost = Integer.parseInt(Copies)*Page*Double.parseDouble(pricePerBlackWhitePage_setting);
        }else{
            //SlidesPerPage
            Page =Math.round(Global.PageCount/Integer.parseInt(SlidesPerPage));
            //Double Slided or One Slided
            if(Slided.equals("Two-Sided")){
                Page = Page/2;
            }
            System.out.println("Document Page:"+Page);
            Cost = Integer.parseInt(Copies)*Page*Double.parseDouble(pricePerColorPage_setting);
        }

        //Convert to two decimal places
        Cost = Math.round(Cost* 100.00) / 100.00;
        System.out.println("Cost:"+Cost);

        return Cost;
    }
    //SPINNER PART
    //Performing action onItemSelected and onNothing selected
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
            SlidesPerPage = SlidesPerPage_setting.get(position);
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }
    private int checkforDuplicatedFileName(){
        int valid =1;
        if(Global.FileNameList!=null) {
            for (int i = 0; i < Global.FileNameList.size(); i++) {

                if (Global.FileNameList.get(i).equals(Global.FileName)) {
                    valid = 0;
                    break;
                } else {
                }
            }
        }

        return valid;
    }

    //Verification
    private boolean VerificationOfSelection(){
        boolean valid = true;
        int error=0;
        System.out.println("Selected ITEM:");
        System.out.println(Slided);
        System.out.println(ColorSelected);
        System.out.println(PL);
        System.out.println(SlidesArrangment);
        System.out.println(PageRange);
        System.out.println(fileName);
        System.out.println(SlidesPerPage);
        System.out.println(Copies);


            if((Slided.equals("default"))||(Edge.equals("default"))||(ColorSelected.equals("default"))||(PL.equals("default"))||(SlidesArrangment.equals("default"))
            ||(PageRange.equals("default"))||(fileName==null)||(fileName.equals("default"))||(SlidesPerPage.equals("default"))||(Copies.equals("default"))) {
                System.out.println("A");
                error+=1;
            }else{
                if(PageRange=="All"){
                    System.out.println("B");
                }else{
                    //Verification of Page Range
                    if(VerificationOfPageRange()==1){
                    }else{
                        System.out.println("C");
                        error+=1;
                    }
                }
                //Verification of Copies
                if(VerificationOfCopies()==1){
                }else{
                    System.out.println("D");
                    error+=1;
                }

                //Verification of Page Count
                if(Global.PageCount==0){
                    System.out.println("E");
                    error+=1;
                }
            }

            if(error==0){
                valid = true;
            }else{
                valid = false;
            }
        return valid;
    }
    private int VerificationOfCopies(){
        int valid=0;
        int error=0;
        if(Copies.isEmpty()){
            error+=1;
        }else if(!((Integer.parseInt(Copies)>=Integer.parseInt(minCopies_setting))&&Integer.parseInt(Copies)<=Integer.parseInt(maxCopies_setting))){
            error+=1;
            Global.displayToast(getContext(),"Copies not within range",Toast.LENGTH_SHORT,"red");
        }
        if(checkforDuplicatedFileName()==1){
        }else{
            error+=1;
            Global.displayToast(getContext(),"Cannot have duplicated file name in an order",Toast.LENGTH_SHORT,"red");
        }

        if(error==0){
            valid=1;
        }else{
            valid=0;
        }

        return valid;
    }

    private int VerificationOfPageRange(){
        int valid=0;
        Pattern pattern = Pattern.compile("^\\d+(?:-\\d+)?(?:,\\h*\\d+(?:-\\d+)?)*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(PageRange);
        boolean matchFound = matcher.find();
        if(matchFound) {
            valid =1;
        } else {
            Global.displayToast(getContext(),"Invalid Page Range",Toast.LENGTH_SHORT,"red");
            valid=0;
        }

        return valid;
    }

    private void initiateVariable(){
        ColorSelected_setting = documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getColorSelected();
        PL_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getPL();
        Edge_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getEdge();
        Slided_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getSlided();
        SlidesPerPage_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getSlidesPerPage();
        SlidesArrangement_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getSlidesArrangement();
        minCopies_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getminCopies();
        maxCopies_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getmaxCopies();
        pricePerColorPage_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getpricePerColorPage();
        pricePerBlackWhitePage_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getpricePerBlackWhitePage();
        available_setting=documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().getAvailable();
    }


    private class printerDocumentPrintigSettingBW extends AsyncTask<String,Void,String[]> {
        public printerDocumentPrintigSettingBW() {
            dialog = new ProgressDialog(getActivity());
        }


        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
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
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    if(action.equals("read")) {
                        System.out.println(response.getData());
                        documentPrintingSetting = DocumentPrintingSetting.JSONToLIST(response.getData());
                        if (!(documentPrintingSetting.get(0).getdocumentPrintingSettingPreferences().equals("default"))) {
                            initiateVariable();
                            createLayoutView();
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                }else{
                    dialog.dismiss();
                }
            }
        }


    }
}