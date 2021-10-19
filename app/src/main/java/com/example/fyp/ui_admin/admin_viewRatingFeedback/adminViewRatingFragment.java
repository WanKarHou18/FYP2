package com.example.fyp.ui_admin.admin_viewRatingFeedback;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;
import com.example.fyp.profile.PrinterProfileActivity;
import com.example.fyp.ui_admin.admin_home.AdminHomeFragment;

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


public class adminViewRatingFragment extends Fragment {
    
    //View
    private View root;
    private ListView lv_adminViewRating;

    //Object

    private User user;

    //Data
    private String action="";
    private String data="";
    private UsersRatingListViewAdapter usersRatingListViewAdapter;
    private ArrayList<Rating> ratingArrayList ;

    private float rating_value;
    private float ttl_rating;
    private float avg_rating;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_admin_view_rating, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        lv_adminViewRating =root.getRootView().findViewById(R.id.lv_adminViewRating);
    }

    private void initiateData() {
        user = Global.user;
        AdminViewRatingBW adminViewRatingBW = new AdminViewRatingBW();
        adminViewRatingBW.execute(action,data);


    }

    private void createLayoutView() {
    }

    private void ViewListener() {
        lv_adminViewRating.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), admin_viewPrinterRatingProfileActivity.class);
                System.out.println(Rating.OBJtoJSON(ratingArrayList.get(position)));
                intent.putExtra("rating_json", Rating.OBJtoJSON(ratingArrayList.get(position)));
                intent.putExtra("avgRatingValue", rating_value);

                startActivity(intent);

            }
        });
    }
    private class UsersRatingListViewAdapter extends ArrayAdapter<Rating> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Rating> ratings;

        public UsersRatingListViewAdapter(Activity context, ArrayList<Rating> ratings) {
            super(context, R.layout.rating_for_admin_list,ratings);
            this.context = context;
            this.ratings = ratings;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.rating_for_admin_list, null);

            Rating rating = getItem(position);

            ImageView ivIcon = view.findViewById(R.id.iv_userIMG);
            TextView title = view.findViewById(R.id.admin_viewRatingTitle);
            TextView subtitle = view.findViewById(R.id.admin_viewRatingSubtitle);
            TextView rating_title = view.findViewById(R.id.tv_admin_printerRating);

            title.setText(rating.getPrinter().getUser().getUsername());
            subtitle.setText(rating.getPrinter().getUser().getUser_hp());
            rating_title.setText(rating.getrating_value());
            return view;
        }
        @Override
        public int getCount() {
            return ratings.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    private class AdminViewRatingBW extends AsyncTask<String,Void,String[]> {
        public AdminViewRatingBW() { }


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
            String UserURL = Global.getURL()+"CRUD_admin_ViewRating.php";


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
                    ratingArrayList = new ArrayList<>();
                    System.out.println(response.getData());
                    ratingArrayList = Rating.JSONToLIST(response.getData());
                    System.out.println(ratingArrayList.get(0).getrating_value());
                    System.out.println(ratingArrayList.get(0).getCustomer().getUser().getUsername());
                    usersRatingListViewAdapter = new UsersRatingListViewAdapter(getActivity(),ratingArrayList);
                    lv_adminViewRating.setAdapter(usersRatingListViewAdapter);
                    CalAvgRating();


                }else{
                    System.out.println("Nothing");
                }
            }
        }

    }
    //Function: Calculate Average Rating By Printer
    private void CalAvgRating(){
        for(int i=0;i<ratingArrayList.size();i++){
            //Calculate average rating
            rating_value=  Float.valueOf(ratingArrayList.get(i).getrating_value()).floatValue();
            ttl_rating+= rating_value;

        }
        //Average rating
        avg_rating=ttl_rating/ratingArrayList.size();

    }
}