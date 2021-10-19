package com.example.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.common.Global;
import com.example.fyp.domain.User;
import com.example.fyp.profile.UserLoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserMainActivity extends AppCompatActivity {

    //View
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView user_name_display;
    private TextView user_email_display;

    // Object
    private User user;

    // Data
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private String login_mode;
    private String login_json;
    private String user_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("login_status", 0);
        editor = sharedPreferences.edit();
        login_json = sharedPreferences.getString("userlogin_json", "");
        if (Global.user.getUser_role().equals("customer")) {
            custFunction();
        } else if(Global.user.getUser_role().equals("admin")){
            adminFunction();

        }else{
            printerFunction();
        }

    }
    ///////////////////////
    // Admin function
    ///////////////////////
    private void adminFunction() {
        setContentView(R.layout.admin_activity_main);
        linkXMLAdmin();
        createLayoutViewAdmin();
        ViewListenerAdmin();
    }

    private void  linkXMLAdmin() {
        toolbar = findViewById(R.id.toolbar_admin);
        drawer = findViewById(R.id.drawer_layout_admin);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_admin);
        navController = navHostFragment.getNavController();
        navigationView = findViewById(R.id.nav_view_admin);
        user_name_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserName);
        user_email_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserEmail);
    }

    private void createLayoutViewAdmin() {
        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_admin)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        updateNav_HeaderUI();
    }


    private void ViewListenerAdmin() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.nav_logout) {
                Toast.makeText(getApplicationContext(),
                        "Logout", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem, navController);
            //This is for c losing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }


    ///////////////////////
    // Customer function
    ///////////////////////
    private void custFunction() {
        setContentView(R.layout.customer_activity_main);
        linkXML();
        createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        navigationView = findViewById(R.id.nav_view);
        user_name_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserName);
        user_email_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserEmail);
    }

    private void createLayoutView() {
        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_orders, R.id.nav_toolslist,R.id.nav_cust_profile)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        updateNav_HeaderUI();
    }


    private void ViewListener() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.nav_logout) {

                Toast.makeText(getApplicationContext(),
                        "Logout", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem, navController);
            //This is for c losing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    ///////////////////////
    // Printer function
    ///////////////////////
    private void printerFunction() {
        setContentView(R.layout.printer_activity_main);
        linkXMLPrinter();
        createLayoutViewPrinter();
        ViewListenerPrinter();
    }

    private void linkXMLPrinter() {
        toolbar = findViewById(R.id.toolbar_printer);
        drawer = findViewById(R.id.drawer_layout_printer);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_printer);
        navController = navHostFragment.getNavController();
        navigationView = findViewById(R.id.nav_view_printer);
        user_name_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserName);
        user_email_display = navigationView.getHeaderView(0).findViewById(R.id.tvDisplayUserEmail);
    }

    private void createLayoutViewPrinter() {
        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_printer, R.id.nav_printer_order,R.id.nav_printer_setting,R.id.nav_printer_document_printing_setting,R.id.nav_printer_image_printing_setting)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        updateNav_HeaderUI();
    }


    private void ViewListenerPrinter() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.nav_logout_printer) {
                Toast.makeText(getApplicationContext(),
                        "Logout", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem, navController);
            //This is for c losing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }


    ///////////////////////
    // Common function
    ///////////////////////
    private void updateNav_HeaderUI() {
        if (!login_json.equals("")) {
            Global.user = User.JSONToOBJ(login_json);
            user_name_display.setText(Global.user.getUsername());
            user_email_display.setText(Global.user.getUser_email());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
//        if (Global.user.getUser_role().equals("customer")) {
//            if (login_mode.equals("customer")) {
//                navController = navHostFragment.getNavController();
//            } else if (login_mode.equals("printer")) {
//                navController = navHostFragment.getNavController();
//            }
//        } else {
//              }
            navController = navHostFragment.getNavController();

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}