package com.example.fyp.ui_customer.Orders;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.ui_customer.home.HomeFragment;

import java.io.File;
import java.util.ArrayList;

public class DisplayDocumentFragment extends Fragment implements View.OnClickListener {

   //View
    private View root;
    private ImageView iv_displayDocument;
    //Data
    private ArrayList<Bitmap> BitmapArr = new ArrayList<>();
    private File myObj;
    private Uri FileURL;


    public DisplayDocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       root = inflater.inflate(R.layout.fragment_display_document, container, false);
       initiateData();
       linkXML();
       CreateLayoutView();
       ViewListener();
       return root;
    }

    private void initiateData() {
        FileURL =Global.FileURL;
    }

    private void linkXML() {
        iv_displayDocument = root.getRootView().findViewById(R.id.iv_displayDocument);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void CreateLayoutView() {

        myObj = new File(Global.FileRealPath);

        try {
            BitmapArr = pdfToBitmap(myObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        iv_displayDocument.setImageBitmap(BitmapArr.get(0));
        System.out.println(BitmapArr.size());
    }

    private void ViewListener() {

    }


    @Override
    public void onClick(View view) {

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ArrayList<Bitmap> pdfToBitmap(File pdfFile) throws Exception, IllegalStateException {

        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            final int pageCount = renderer.getPageCount();
            System.out.println("HERE Page Count"+pageCount);
            Bitmap bitmap;

            if(pageCount!=0) {
                for (int i = 0; i <1; i++) {
                    PdfRenderer.Page page = renderer.openPage(i);

                    int width = page.getWidth();
                    int height = page.getHeight();

                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    bitmaps.add(bitmap);
                    page.close();
                }    // close the renderer
            }
            System.out.println("SIZE:"+bitmaps.size());
            renderer.close();}catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmaps;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                Global.sub_order =null;
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}