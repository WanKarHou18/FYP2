package com.example.fyp.ui_admin.admin_viewComplaint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Comments;
import com.example.fyp.domain.Complaint;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.adminViewFeedbackFragment;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.adminViewRatingFragment;
import com.example.fyp.ui_admin.admin_viewRatingFeedback.admin_ViewPrinterFeedbackProfileActivity;

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


public class adminViewComplaintFragment extends Fragment {

    //VIEW
    private View root;
    private ListView lv_adminComplaint;

    //OBJECT
    private ArrayList<Complaint> complaintArrayList;
    private UserComplaintAdapter userComplaintAdapter;

    //DATA
    private String action="";
    private String data="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_admin_view_complaint, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        lv_adminComplaint = root.getRootView().findViewById(R.id.lv_adminComplaint);

    }

    private void initiateData() {
        AdminViewComplaintBW adminViewRatingBW = new AdminViewComplaintBW();
        adminViewRatingBW.execute("read",data);

    }

    private void createLayoutView() {
    }

    private void ViewListener() {
      lv_adminComplaint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), adminViewComplaintContentActivity.class);
                intent.putExtra("complaint_json", Complaint.OBJtoJSON(complaintArrayList.get(position)));

                startActivity(intent);

            }
        });
    }

    private class UserComplaintAdapter extends ArrayAdapter<Complaint> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Complaint> complaints;

        private UserComplaintAdapter(Activity context, ArrayList<Complaint> complaints) {
            super(context, R.layout.complaint_for_admin_list, complaints);
            this.context = context;
            this.complaints = complaints;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.complaint_for_admin_list, null);

            Complaint complaint = getItem(position);

            ImageView ivIcon = view.findViewById(R.id.iv_userIMG);
            TextView title = view.findViewById(R.id.admin_viewComplaintTitle);
            TextView subtitle = view.findViewById(R.id.admin_viewComplaintSubtitle);
            TextView complaint_content = view.findViewById(R.id.tv_admin_customerComplaint);

            title.setText(complaint.getCustomer().getUser().getUsername());
            subtitle.setText(complaint.getCustomer().getUser().getUser_email());
            complaint_content.setText(complaint.getComplaint_content());

            return view;
        }
    }
    private class AdminViewComplaintBW extends AsyncTask<String,Void,String[]> {
        public AdminViewComplaintBW() { }


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
            String UserURL = Global.getURL()+"CRUD_admin_ViewComplaints.php";


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
                   complaintArrayList = new ArrayList<>();
                    System.out.println(response.getData());
                    complaintArrayList = Complaint.JSONToLIST(response.getData());
                    userComplaintAdapter = new UserComplaintAdapter(getActivity(),complaintArrayList);
                    lv_adminComplaint.setAdapter(userComplaintAdapter);

                }else{
                    System.out.println("Nothing");
                }
            }
        }

    }



}