package com.example.fyp.ui_customer.Orders;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fyp.R;
import com.example.fyp.common.Global;

public class DisplayImageFragment extends Fragment {

    //View
    private View root;
    private ImageView iv_showImage;

    //Data
    private Uri FileUri;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_display_image, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        iv_showImage = root.getRootView().findViewById(R.id.iv_showImage);
    }

    private void initiateData() {
        FileUri =Global.FileURL;
        System.out.println(FileUri.toString());
    }

    private void createLayoutView() {
        iv_showImage.setImageURI(FileUri);
    }

    private void ViewListener() {
     //   root.getRootView().getActionBar().setDisplayHomeAsUpEnabled(true);
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
}