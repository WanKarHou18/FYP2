package com.example.fyp.ui_customer.Tools;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.fyp.R;
import com.example.fyp.ui_customer.Orders.CustOrderMainActivity;

public class ToolsFragment extends Fragment implements View.OnClickListener{

    //View
    private View root;
    private ImageButton ib_grayScale;
    private ImageButton ib_blurry;

    //Data
    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_tools_list, container, false);
        linkXML();
        initiateData();
        CreateLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        ib_grayScale = root.getRootView().findViewById(R.id.ib_grayScale);
        ib_blurry = root.getRootView().findViewById(R.id.ib_blurry);
    }

    private void initiateData() {
        verifyPermissions();
    }

    private void CreateLayoutView() {
    }

    private void ViewListener() {
        ib_grayScale.setOnClickListener(this::onClick);
        ib_blurry.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_blurry:
                Intent i1 = new Intent(getActivity(),BlurrActitvity.class);
                startActivity(i1);
            case R.id.ib_grayScale:
                Intent i = new Intent(getActivity(),GrayScaleActivity.class);
                startActivity(i);

        }

    }
    private void verifyPermissions(){
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    STORAGE_PERMISSIONS,
                    1
            );
        }

    }
}