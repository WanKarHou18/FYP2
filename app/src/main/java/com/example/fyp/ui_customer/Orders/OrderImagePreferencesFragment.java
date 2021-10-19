package com.example.fyp.ui_customer.Orders;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.DocumentPrintingSetting;
import com.example.fyp.domain.ImagePreferences;
import com.example.fyp.domain.ImagePrintingSetting;
import com.example.fyp.domain.ImagePrintingSettingPreferences;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Product_Printing_Preferences;
import com.example.fyp.domain.ResourcesRecord;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.Sub_Orders;
import com.example.fyp.domain.businessSetting;
import com.example.fyp.pytorch.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

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
import java.util.List;

import static com.example.fyp.pytorch.Utils.assetFilePath;


public class
OrderImagePreferencesFragment extends Fragment implements View.OnClickListener {
    //View
    private View root ;
    private Spinner spinner_paperSize,spinner_border,spinner_PaperType;
    private Button btn_upload_image , btn_continue_ImagePref;
    private EditText etCopies;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog dialog;

    //Object
    private businessSetting  businessSetting;
    private ImagePreferences imagePreferences;
    private Product_Printing_Preferences product_printing_preferences;
    private Sub_Orders sub_order;
    private Printer printer;
    private ArrayList<ImagePrintingSetting> imagePrintingSetting= new ArrayList<>();
    private ResourcesRecord resourcesRecord;

    //Data
    //**For Spinners
    private String fileName = "default";
    private String fileType="default";

    //**For Spinners**//

    private ArrayList<String> PaperSizeArr = new ArrayList<>();

    private ArrayList<String> PaperTypeArr= new ArrayList<>();

    //**For Image Preferences printing setting**
    private ArrayList<String>  PaperSize_setting = new ArrayList<>();
    private ArrayList<String>  PaperType_setting = new ArrayList<>();
    private String minQuantity_setting;
    private String maxQuantity_setting;
    private String available_setting;
    private ArrayList<String>  border_setting = new ArrayList<>();
    private String pricePerPage_setting;

   private double[] Diagonals ={16,8.6,12.6,5};

   private int PictureSizePosition = 0 ;

   //**For Image Preferences of Order
     private String image_pref_json = "default";
    private String PaperSize = "default";
    private String PaperType = "default";
    private String Copies = "default";
    private String border ="default";
    private double cost;
    private String costStr="default";
    //**For ppp
    private String printing_pref_id = "default";

    //**For Sub Orders;
    private String orderId ="default";
    private String subOrderId = "default";

    //**For ResourcesRecord
    private String resources_id="default";
    private String file_description="default";

    private String action;
    private String data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order_image_preferences_fragment, container, false);
        linkXML();
        initiateData();
        ViewListener();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void linkXML() {

        sharedPreferences = getActivity().getSharedPreferences("ImageResolution", 0);
        editor = sharedPreferences.edit();

        spinner_paperSize = root.getRootView().findViewById(R.id.spinner_paperSize);
        spinner_border = root.getRootView().findViewById(R.id.spinner_border);
        spinner_PaperType = root.getRootView().findViewById(R.id.spinner_paperType);

        etCopies = root.getRootView().findViewById(R.id.et_imgCopies);
        btn_upload_image = root.getRootView().findViewById(R.id.btn_uploadImage);
        btn_continue_ImagePref = root.getRootView().findViewById(R.id.btn_continue_ImagePref);
    }

    private void initiateData() {
        initLoadOpenCV();
        businessSetting = businessSetting.JSONToOBJ(Global.printer_business_setting_json);
        printer = Printer.JSONToOBJ(Global.printer_json);

        JSONObject data = new JSONObject();
        try {
            data.put("printer_id",printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        printerImagePrintingSettingBW printerImagePrintingSettingBW = new printerImagePrintingSettingBW();
        printerImagePrintingSettingBW.execute("read",data.toString());

    }

    private void createLayoutView() {
        //Paper Size
        PaperSize_setting.add(0,"Select Size of Picture");
//        for(int i=0;i<PaperSize_setting.size();i++){
//            PaperSizeArr.add(PaperSize_setting.get(i));
//        }

        //Border
       border_setting.add(0,"Select Border");


        //Paper Types

        PaperType_setting.add(0,"Select types of paper you want to print on");
        System.out.println(PaperSize_setting.size());
//        for(int i=0;i<PaperType_setting.size();i++){
//             PaperTypeArr.add(PaperSize_setting.get(i));
//        }



        //Build Up ArrayAdapter
        ArrayAdapter aa_paperSize = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,PaperSize_setting);
        aa_paperSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter aa_border = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,border_setting);
        aa_border.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter aa_PaperType= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,PaperType_setting);
        aa_PaperType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Setting the ArrayAdapter data on the Spinner
        spinner_paperSize.setAdapter(aa_paperSize);
        spinner_border.setAdapter(aa_border);
        spinner_PaperType.setAdapter(aa_PaperType);

        HandleLayout();
    }

    private void HandleLayout() {
        //Display Max and Min Copies available
        Global.displayToast(getContext(),"Min Copies:"+minQuantity_setting+"  "+"Max Copies:"+maxQuantity_setting,Toast.LENGTH_SHORT,"blue");
        Global.displayToast(getContext(),"Cost per photo:RM"+pricePerPage_setting,Toast.LENGTH_SHORT,"blue");

        spinner_paperSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //If position not at 0
                if(i!=0){
                    // Get the spinner selected item text
                    PaperSize = (String) adapterView.getItemAtPosition(i);

                }else{
                    PaperSize="default";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_PaperType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(i!=0){
               PaperType = (String) adapterView.getItemAtPosition(i);

               } else{
                   PaperType="default";
               }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_border.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                   border = (String) adapterView.getItemAtPosition(i);

                } else{
                   border="default";
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



    }

    private void ViewListener() {
        btn_continue_ImagePref.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_uploadImage:
                // *** The Upload File Function is in Activity ***
                break;
            case R.id.btn_continue_ImagePref:



                Copies = etCopies.getText().toString();
                if(Copies.matches("")){
                    Copies="default";
                }
                fileName = Global.FileName;
                fileType = Global.FileType;

                System.out.println("Selected item");
                System.out.println(border);
                System.out.println(PaperType);
                System.out.println(PaperSize);
                System.out.println(Copies);
                System.out.println(fileName);



                if((VerificationOfSelection(fileName,PaperType,PaperSize, Copies,border)==1)) {

                    if (VerificationOfAdvancedSetting() == 1) {

                        //ImagePreferences JSON
                        imagePreferences = new ImagePreferences(PaperSize, PaperType, Copies, border);
                        image_pref_json = ImagePreferences.OBJtoJSON(imagePreferences);
                        System.out.println(image_pref_json);

                        //Calculate Cost
                        cost = CalculateImagePrintingCost(imagePreferences, imagePrintingSetting.get(0).getImagePrintingSettingPreferences());
                        costStr = String.valueOf(cost);
                    //    DecimalFormat df = new DecimalFormat("#,##0.00");
                   //     costStr = df.format(costStr);
                        //Create PPP
                        product_printing_preferences = new Product_Printing_Preferences(printing_pref_id, image_pref_json, subOrderId);

                        //Create ResourcesRecord
                        resourcesRecord = new ResourcesRecord(resources_id, fileName, fileType, file_description, subOrderId);

                        //Create SubOrder
                        sub_order = new Sub_Orders(subOrderId, orderId, costStr, product_printing_preferences, resourcesRecord);
                        Global.sub_order = sub_order;

                        //Add FileName and FileType to List
                        Global.FileURLList.add(Global.FileURL);
                        Global.FileNameList.add(Global.FileName);

                        System.out.println(Sub_Orders.OBJtoJSON(Global.sub_order));

                        Intent intent = new Intent(getActivity(), CustOrderThirdActivity.class);
                        startActivity(intent);
                        break;
//                }else{
                        }else{

                    }
//
                    } else {
                        Global.displayToast(this.getContext(), "Please fill up everything correctly", Toast.LENGTH_SHORT,"yellow");
                    }

        }
    }
    //Verification
    private int VerificationOfSelection(String fileName,String PaperType,String PaperSize,String Copies,String border) {
        int valid=1;
        int error =0;
        if((fileName==null)||(PaperType.equals("default"))||(PaperSize.equals("default"))||(Copies.equals("default"))
                ||(border.equals("default"))||VerificationOfCopies(Copies)==0){
            valid =0;
        }else{
            if(Global.FileNameList!=null) {
                if (checkforDuplicatedFileName() == 0) {
                    valid =0;
                    Global.displayToast(getContext(),"Cannot have duplicated name of file for one order",Toast.LENGTH_SHORT,"red");
                } else {
                }
            }else{
                //If Global.FileNameList is zero,means filenot duplicated
            }


        }
        return valid;
    }

    private int VerificationOfCopies(String copies){
        //Verify if it is number
        int valid = 0;
        int error= 0;



        if((!(copies.matches("[0-9]+")))){
            error+=1;
            Global.displayToast(getContext(),"Copies must be number",Toast.LENGTH_SHORT,"red");
        }else if(!((Integer.parseInt(minQuantity_setting)<Integer.parseInt(copies))&&(Integer.parseInt(maxQuantity_setting)>Integer.parseInt(copies)))){
            error+=1;
            Global.displayToast(getContext(),"copies is not in range",Toast.LENGTH_SHORT,"red");
        }else{

        }

        if(error==0){
            valid = 1;
        }else{
            valid = 0;
        }

        return valid;
    }
    private int VerificationOfAdvancedSetting(){
        int valid =1;
        int error =0;
        if(businessSetting.getAdvanced_feature_setting().get(0).getDetectSize().equals("Yes")){
            if(VerificationOfPPI()==1){
                //Suitable for printing
            }else{
                //Not Suitable for printing
                error+=1;
            }
        }else{
            //If no set detect size,means it will directly pass.;
        }

        if(businessSetting.getAdvanced_feature_setting().get(0).getDetectAdult().equals("Yes")){
            if(VerificationOfAC()==1){
              //No adult content
            }else{
                error+=1;
            }
        }else{
            //If no set detection of adult content, it will directly
        }


        if(businessSetting.getAdvanced_feature_setting().get(0).getDetectBlurry().equals("Yes")){
            if(VerificationOfBlurry()==1){
                //Not Blurry
            }else{
              error+=1;
            }
        }else{
        }
        if(error==0){
            valid =1;
        }else{
            valid =0;
        }

        return valid;
    }
    private int VerificationOfBlurry(){
        int valid =1;
        double LaplaVar =0.0;
        //=================================
        //Comparing Blurriness
        //=================================
        Mat srcMat1 = Imgcodecs.imread(Global.FileRealPath);
        Mat dst = new Mat();
        // Applying GaussianBlur on the Image
        Imgproc.Laplacian(srcMat1, dst, 10);
        MatOfDouble median = new MatOfDouble();
        MatOfDouble std= new MatOfDouble();
        Core.meanStdDev(dst, median , std);
        System.out.println("Laplacian :"+Math.pow(std.get(0,0)[0],2));
        LaplaVar = Math.pow(std.get(0,0)[0],2);

        if(LaplaVar>12.5){
            valid =1;
        }else{
            Global.displayToast(getContext(),"Image is blurry",Toast.LENGTH_SHORT,"red");
            valid =0;
        }

        return valid;
    }

    private int VerificationOfAC(){
        int valid =1;
        Bitmap bitmap = BitmapFactory.decodeFile(Global.FileRealPath);
        if(convert(bitmap).equals("1")){
            //If result of prediction is 1, means it is consists of adult content
            valid =0;
           Global.displayToast(getContext(),"Image consits of adult content",Toast.LENGTH_SHORT,"red");
        }else{
            valid =1;
        }

        return valid;
    }

    /*
       3R:
       8.89X12.7
       Diagonal:15.5023
       4R:
       10.2 x 15.2
       Diagonal:18.305
        */
    //Calculate ppi
    private int VerificationOfPPI(){
        int valid = 1; //True = 1, False =0
        double diagonal = 0.0;
        double d0 =0.0;
        if(PaperSize.equals("3R")){
            diagonal = 15.5023;
        }else if(PaperSize.equals("4R")){
            diagonal=18.31;
        }else{
            diagonal =21.87;
        }
        d0 =  Double.parseDouble(sharedPreferences.getString("d0", ""));
        double ppi = d0 / diagonal;
        System.out.println("PPI"+ppi);

        //Check if image are in 300ppi
        if(ppi>300){
            valid =1;
        }else{
            valid=0;
            Global.displayToast(getActivity(),"Not Suitable For Printing in this size ,might appear blurry",Toast.LENGTH_SHORT,"red");
        }
        return valid;
    }
        //Calculate dpi
        //double dpi = 1/ppi;

        //Check if the dpi got 300dpi = 0.084667
//                    System.out.println("DPI is"+ dpi);
//                    if(dpi>0.084667){
//                        System.out.println("Suitable For Printing In Full Size");
//                    }else{
//                        System.out.println("Not Suitable For Printing In Full Size");
//                    }


    private double CalculateImagePrintingCost(ImagePreferences imagePreferences,ImagePrintingSettingPreferences imagePrintingSettingPreferences){
        double cost = 0.00;
        cost = Double.parseDouble(imagePreferences.getCopies())*Double.parseDouble(imagePrintingSettingPreferences.getpricePerPage());

        //Convert to two decimal places
        cost = Math.round(cost * 100.00) / 100.00;
        return cost;
    }
    private int checkforDuplicatedFileName(){
        //1 = Valid, 0 =Not Valid
        int valid =1;
        for(int i=0;i<Global.FileNameList.size();i++){
            if(Global.FileNameList.get(i).equals(Global.FileName)){
                //Duplicated Name Here
                valid =0;
                break;
            }else{
                valid =1;
            }
        }

        return valid;
    }


    private String convert(Bitmap bitmap){

        Module module = Module.load(assetFilePath(getContext(), "mobilenet-v2.pt"));

        Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();

        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }
        String className = Constants.IMAGENET_CLASSES[maxScoreIdx];
        System.out.println("ClassName"+className);
        return className;

    }
    private void initLoadOpenCV() {
        boolean isDebug = OpenCVLoader.initDebug();
        if (isDebug) {
            Log.i("init Opencv", "init openCV success!!");
        } else {
            Log.e("init Opencv", "init openCV failure!!");
        }
    }

    public void requestPermissionForReadExtertalStorage() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                    requestPermissionForReadExtertalStorage();
                }
                break;
        }
    }



    private class printerImagePrintingSettingBW extends AsyncTask<String,Void,String[]> {
        public printerImagePrintingSettingBW() {
            dialog = new ProgressDialog(getActivity());
        }


        @Override
        // Before doing background operation we should show something on screen like progressbar or any animation to user.
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait.");
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
        }

        @Override
        //In this method we can update ui of background operation result.
        protected void onPostExecute(String[] result) {
            if(result[0].equals("connection success")){
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    if(action.equals("read")) {
                        System.out.println(response.getData());
                        imagePrintingSetting = ImagePrintingSetting.JSONToLIST(response.getData());
                        if (!(imagePrintingSetting.get(0).getImagePrintingSettingPreferences().equals("default"))) {
                            initiateVariable();
                            createLayoutView();
                            dialog.dismiss();
                        }
                    }
                }else{
                    System.out.println("Cant fetch data from server");
                }
            }
        }

        private void initiateVariable() {
            PaperSize_setting = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getPaperSize();
            PaperType_setting=imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getPaperType();
            border_setting= imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getBorder();
            maxQuantity_setting = imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getmaxQuantity();
            minQuantity_setting =imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getminQuantity();
            pricePerPage_setting=imagePrintingSetting.get(0).getImagePrintingSettingPreferences().getpricePerPage();
        }


    }
}