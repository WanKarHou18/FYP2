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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Comments;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;

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


public class adminViewFeedbackFragment extends Fragment {
    //View
    private View root;
    private ListView lv_feedback_admin;

    //Object
    private ArrayList<Comments> feedbackArrayList = new ArrayList<>();
    private UsersFeedbackListViewAdapter usersFeedbackListViewAdapter;

    //Data
    private String action="";
    private String data="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_admin_view_feedback, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {
        lv_feedback_admin = root.getRootView().findViewById(R.id.lv_feedback_admin);

    }

    private void initiateData() {
        AdminViewFeedbackBW adminViewFeedbackBW = new AdminViewFeedbackBW();
        adminViewFeedbackBW.execute(action, data);

    }

    private void createLayoutView() {
    }

    private void ViewListener() {
        lv_feedback_admin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), admin_ViewPrinterFeedbackProfileActivity.class);
                intent.putExtra("feedback_json", Comments.OBJtoJSON(feedbackArrayList.get(position)));

                startActivity(intent);

            }
        });
    }
        private class UsersFeedbackListViewAdapter extends ArrayAdapter<Comments> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Comments> comments;

        private UsersFeedbackListViewAdapter(Activity context, ArrayList<Comments> comments) {
            super(context, R.layout.feedback_for_admin_list,comments);
            this.context = context;
            this.comments = comments;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.feedback_for_admin_list, null);

           Comments comments = getItem(position);

            ImageView ivIcon = view.findViewById(R.id.iv_userIMG);
            TextView title = view.findViewById(R.id.admin_viewFeedbackTitle);
            TextView subtitle = view.findViewById(R.id.admin_viewFeedbackSubtitle);
            TextView  hp_title = view.findViewById(R.id.tv_admin_printerFeedback);

            title.setText(comments.getCommentCustomer().getUser().getUsername()+"(Customer)");
            subtitle.setText(comments.getCommentPrinter().getUser().getUsername()+"(Service Provider)");
            hp_title.setText(comments.getComment_content());

            return view;
        }
    }


    private class AdminViewFeedbackBW extends AsyncTask<String,Void,String[]> {
        public AdminViewFeedbackBW() { }


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
            String UserURL = Global.getURL()+"CRUD_admin_ViewComments.php";


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
                    feedbackArrayList = new ArrayList<>();
                    System.out.println(response.getData());
                    feedbackArrayList = Comments.JSONToLIST(response.getData());

                    usersFeedbackListViewAdapter = new UsersFeedbackListViewAdapter(getActivity(),feedbackArrayList);
                    lv_feedback_admin.setAdapter(usersFeedbackListViewAdapter);

                }else{
                    System.out.println("Nothing");
                }
            }
        }

    }

}