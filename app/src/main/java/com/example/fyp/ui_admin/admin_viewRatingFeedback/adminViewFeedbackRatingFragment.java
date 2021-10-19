package com.example.fyp.ui_admin.admin_viewRatingFeedback;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Comments;
import com.example.fyp.domain.Response;
import com.google.android.material.tabs.TabLayout;

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

public class adminViewFeedbackRatingFragment extends Fragment {
    //View
    private View root;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Object
    private  RFAdapter adapter;


    //Data
    private String action="";
    private String data="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_admin_view_feedback_rating, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        tabLayout=(TabLayout)root.getRootView().findViewById(R.id.tl_ratingFeedback);
        viewPager=(ViewPager)root.getRootView().findViewById(R.id.viewPager);
    }

    private void initiateData() {

    }

    private void createLayoutView() {
        tabLayout.addTab(tabLayout.newTab().setText("Rating"));
        tabLayout.addTab(tabLayout.newTab().setText("Feedback"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void ViewListener() {
       adapter = new RFAdapter(this.getContext(),this.getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    public static class RFAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public RFAdapter (Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    adminViewRatingFragment AdminViewRatingFragment = new adminViewRatingFragment();
                    return AdminViewRatingFragment;
                case 1:
                    adminViewFeedbackFragment AdminViewFeedbackFragment = new adminViewFeedbackFragment();
                    return AdminViewFeedbackFragment;
                default:
                    return null;
            }
        }
        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

}