package com.example.fyp.ui_customer.Orders;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Availability;
import com.example.fyp.domain.DocumentPrintingSetting;
import com.example.fyp.domain.ImagePrintingSetting;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.businessSetting;
import com.example.fyp.profile.UserLoginActivity;
import com.example.fyp.pytorch.Constants;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

import static com.example.fyp.pytorch.Utils.assetFilePath;

public class CustOrderSecondActivity extends AppCompatActivity implements View.OnClickListener{

    //VIEW
    private RadioGroup rg_select_printing_type;
    private RadioButton rb_doc;
    private RadioButton rb_image;
    private Fragment current_Fragment;

    //DATA
    private String print_type_selected;
    private int PICK_FILE= 1;
    private Bitmap bitmap;
    private double d0;

    private String data;
    private String action;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String printer_json;
    private String doc_json;
    private String image_json;

    private int pageCount;
    private String  imgDecodableString;


    private static final int EX_FILE_PICKER_RESULT = 1;


    //OBJECT
    private DocumentPrintingSetting documentPrintingSetting;
    private ImagePrintingSetting imagePrintingSetting;
    private businessSetting businessSetting;
    private Printer printer;
    private Availability availability;
    private Gson gson;

    //MANAGER
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_order_second);
        linkXML();
        initiateData();
        ViewListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void linkXML() {

        rg_select_printing_type = findViewById(R.id.rg_select_printing_type);
        rb_doc=findViewById(R.id.rb_doc);
        rb_image= findViewById(R.id.rb_image);

    }

    private void initiateData(){

        sharedPreferences = getSharedPreferences("ImageResolution", 0);
        editor = sharedPreferences.edit();

        printer_json= Global.printer_json;
        printer = Printer.JSONToOBJ(printer_json);


        JSONObject data1 = new JSONObject();
        try {
            data1.put("printer_id",printer.getPrinter_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FectchPrinterPrintingPrefSettingBW  fectchPrinterPrintingPrefSettingBW = new FectchPrinterPrintingPrefSettingBW();
        fectchPrinterPrintingPrefSettingBW.execute("read",data1.toString());

    }

    private void createLayoutView() {
       if(!(availability.getDoc_available().equals("Yes"))){
          rb_doc.setClickable(false);
       }

       if(!(availability.getImg_available().equals("Yes"))){
           rb_image.setClickable(false);
           rb_doc.setClickable(false);
       }

    }

    private void ViewListener() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //
    //Click on Three Button and Triggered
    //
    public void onClick_rb_image(View view) {
        //Check and Detach Current Fragment , to allow open of new fragment
        checkANDdetachFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        print_type_selected = "image";
        System.out.println("image");
        ft.replace(R.id.FLprinting_pref, new OrderImagePreferencesFragment());
        ft.commit();

    }

    public void onClick_rb_doc(View view) {

        //Check and Detach Current Fragment , to allow open of new fragment
        checkANDdetachFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        print_type_selected = "document";
        System.out.println("document");
        ft.replace(R.id.FLprinting_pref, new OrderDocumentPreferencesFragment());
        ft.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                ClearVariable();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkANDdetachFragment(){
        current_Fragment= this.getSupportFragmentManager().findFragmentById(R.id.FLprinting_pref);
        if(current_Fragment!=null) {
            // FragmentSelected(rg_select_printing_type);
            ft.hide(current_Fragment);
            ft.detach(current_Fragment);
            //  ft.commitAllowingStateLoss();
        }
    }


    @Override
    public void onClick(View view) {

    }


    //===================================================
    //  ACTIVATE UPLOADED FILE FUNCTION
    //===================================================
    public void FileUpload(View view) {
        if(print_type_selected.equals("document")){
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            startActivityForResult(intent, 2);

        }else{
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select Document"), EX_FILE_PICKER_RESULT);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE) {
            if (resultCode == RESULT_OK) {
                //Uri Link of Image
                Uri FileUri = data.getData();

                // File Name of Uploaded File
                Global.FileName = FileUri.getLastPathSegment();
                Global.FileURL = FileUri;
                //File Type
                Global.FileType = "Image";

                //===================================================
                //GET REAL PATH OF IMAGE
                //===================================================

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(FileUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(imgDecodableString);
                Global.FileRealPath = file.getAbsolutePath();
                if(file.exists() && !file.isDirectory()) {
                    System.out.println("Exist");

                }else{
                    System.out.println("Not Exist");
                }
                //===================================================
                // CHECK THE RESOLUTION OF IMAGE , DPI OF IMAGE
                //===================================================

                    try {
                        bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), FileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int imageWidth=bitmap.getWidth();
                    int imageHeight=bitmap.getHeight();

                    //d0 = pixels that fit on the diagonal.
                    d0 = Math.sqrt((Math.pow(imageHeight,2)+Math.pow(imageWidth,2)));
                    System.out.println(d0);
                    editor.putString("d0", String.valueOf(d0));
                    editor.commit();


        }

        }else if(requestCode==2){
            //If request code is for pdf file
            if (resultCode == RESULT_OK) {

                //Link of File
                // Uri.parse("/storage/3092-68D9/FYP2/UCCD3053_FA_Question_Paper.pdf");
                Uri FileUri =data.getData();
                Uri tempUri;
                Global.FileURL = FileUri;
                Global.FileRealPath = getPath(this,FileUri);
                tempUri = Uri.parse(Global.FileRealPath);
                // File Name of Uploaded File
                Global.FileName = tempUri.getLastPathSegment();
                //File Type
                Global.FileType = "Document";

                //Calculate page of file
                File tempFile = new File(Global.FileRealPath);
                Global.PageCount = PageCount(tempFile);

            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int PageCount(File pdfFile) {
            int pageCount=0;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            pageCount = renderer.getPageCount();
            System.out.println("HERE Page Count:"+pageCount);

                // close the renderer
                renderer.close();
             }catch (Exception ex) {
            ex.printStackTrace();
        }
        return pageCount;
    }


    private void ClearVariable(){
        Global.FileName =null;
        Global.FileURLList=null;

        Global.FileNameList=null;
        Global.sub_order=null;
        Global.order=null;
        Global.FileName=null;
        Global.FileType=null;
        Global.FileURL=null;
        Global.PageCount=0;

    }


    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract
                            .getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory()
                                + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri
                                    .parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract
                            .getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor
                        .getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }





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
                    System.out.println(response.getData());
                    //Convert JSON to OBJ
                    gson = new Gson();
                    availability = gson.fromJson(response.getData(), Availability.class);
                    createLayoutView();

                }else{
                    //If fails means printer not yet set anything
                    rb_image.setClickable(false);
                    rb_doc.setClickable(false);
                    System.out.println("Nothing");
                }
            }
        }

    }

}