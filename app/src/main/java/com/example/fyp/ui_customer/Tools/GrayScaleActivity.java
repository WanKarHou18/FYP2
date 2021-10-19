package com.example.fyp.ui_customer.Tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class GrayScaleActivity extends AppCompatActivity implements View.OnClickListener{
    //View
    private ImageView iv_grayScale;
    private Button btn_selectFile;
    private Button btn_convertGrayScale;
    private Button btn_save;

    //Data
    private Uri FileUri;
    private  File file;
    private int position = 0;
    private String FileName ="";
    private String newFileResultPath;
    private int EX_FILE_PICKER_RESULT=1;
    private int saveValid=0;
    private String  imgDecodableString;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gray_scale);
        linkXML();
        initiateData();
        CreateLayoutView();
        ViewListener();
    }

    private void linkXML() {
        iv_grayScale = findViewById(R.id.iv_grayscale);
        btn_selectFile= findViewById(R.id.btn_selectFile);

    }
    private void initiateData() {
        initLoadOpenCV();
    }

    private void CreateLayoutView() {
    }

    private void ViewListener() {
        //btn_selectFile.setOnClickListener(this::onClick);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){

        }

    }


    private void GrayScale() {
        // read the picture
        Mat imsrc = Imgcodecs.imread(file.getAbsolutePath());

        int rows = imsrc.rows();
        int cols = imsrc.cols();

        //Creating the empty destination matrix
        Mat dst = new Mat();

        //Converting the image to gray sacle and saving it in the dst matrix
        Imgproc.cvtColor(imsrc, dst, Imgproc.COLOR_RGB2GRAY);

        // Write the image after adding the effect to the 10001.jpg file

        Imgcodecs.imwrite( Environment.getExternalStorageDirectory()+ "/GrayScale"+FileName+".jpg", dst);

        newFileResultPath = Environment.getExternalStorageDirectory() + "/GrayScale"+FileName+".jpg";

        // Create a bitmap of ARGB_8888 or RGB_565
        Bitmap bitmap = Bitmap.createBitmap(cols, rows, Bitmap.Config.RGB_565);
        //Mat converts to Bitmap
        Utils.matToBitmap(dst, bitmap, true);
        // put on the ImageView
        iv_grayScale.setImageBitmap(bitmap);

    }
    public void SelectFileForGrayScale(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        // startActivityForResult(intent,PICK_FILE);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), EX_FILE_PICKER_RESULT);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EX_FILE_PICKER_RESULT) {
            if (resultCode == RESULT_OK) {
                // ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
                FileUri = data.getData();

                FileUri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(FileUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                // Uri tempUri = Uri.parse(imgDecodableString);
                cursor.close();

                // String path =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/"+tempUri.getLastPathSegment();

                file = new File(imgDecodableString);
                if(file.exists() && !file.isDirectory()) {
                    System.out.println("Exist");
                }else{
                    System.out.println("Not Exist");
                }


                iv_grayScale.setImageURI(FileUri);
                GrayScale();
                Global.displayToast(this,"Image had been saved",Toast.LENGTH_SHORT,"blue");

            }
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
    private void initLoadOpenCV() {
        boolean isDebug = OpenCVLoader.initDebug();
        if (isDebug) {
            Log.i("init Opencv", "init openCV success!!");
        } else {
            Log.e("init Opencv", "init openCV failure!!");
        }
    }
    public void requestPermissionForReadExtertalStorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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


}