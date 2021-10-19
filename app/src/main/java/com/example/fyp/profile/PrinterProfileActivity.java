package com.example.fyp.profile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;


import android.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Orders;
import com.example.fyp.domain.Printer;

public class PrinterProfileActivity extends AppCompatActivity {

    //VIEW

    //DATA

    //OBJECT
    private Orders order;

    //MANAGER
    FragmentTransaction ft ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_profile);

        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

        ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FLprinter_profile,new PrinterProfileFragment());
        ft.commit();


    }

    private void linkXML() {

    }

    private void initiateData() {
    }

    private void createLayoutView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ViewListener() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                //Delete every order data when back to home fragment
                //Clear the sub_orders of order
                order = Global.order;
                if (order != null) {
                    order =null;
                    Global.FileName = null;
                    Global.FileURLList = null;
                    Global.sub_order = null;
                }
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}