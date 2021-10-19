package com.example.fyp.ui_printer.PrintingPreferencesSetting;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp.R;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.adminViewFeedbackFragment;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.adminViewFeedbackRatingFragment;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.adminViewRatingFragment;
import com.google.android.material.tabs.TabLayout;


public class PrintingPreferencesSettingFragment extends Fragment {

    //View
    private View root;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PSAdapter adapter;
    //Object

    //Data

    //Manager
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_printing_preferences_setting, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;

    }

    private void linkXML() {
        tabLayout=(TabLayout)root.getRootView().findViewById(R.id.tl_ratingFeedback1);
        viewPager=(ViewPager)root.getRootView().findViewById(R.id.viewPager1);
    }

    private void initiateData() {
    }

    private void createLayoutView() {
        tabLayout.addTab(tabLayout.newTab().setText("Document Printing"));
        tabLayout.addTab(tabLayout.newTab().setText("Photo Printing"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void ViewListener() {
        adapter = new PSAdapter(this.getContext(),this.getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public static class PSAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public PSAdapter (Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DocumentPrintingSettingFragment documentPrintingSettingFragment = new DocumentPrintingSettingFragment();
                    return documentPrintingSettingFragment;
                case 1:
                    PhotoPrintingSettingFragment photoPrintingSettingFragment = new PhotoPrintingSettingFragment();
                    return photoPrintingSettingFragment;

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