package com.example.fyp.ui_admin.admin_viewRatingFeedback;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.fyp.common.Global;
import com.example.fyp.domain.Comments;
import com.example.fyp.domain.Rating;
import com.example.fyp.domain.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

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

public class admin_ViewPrinterFeedbackProfileActivity extends AppCompatActivity implements View.OnClickListener{

    //VIEW
    private TextView tv_apFeedbackProfile_userID;
    private TextView  tv_apFeedbackProfile_hp;
    private TextView tv_apFeedbackProfile_name;
    private TextView tv_apFeedbackProfile_bankRef;
    private TextView tv_apFeedbackProfile_address;
    private TextView tv_apFeedbackProfile_email;
    private TextView tv_apFeedbackProfile_rating;
    private Button btn_apFeedbackProfile_approve;
    private Button btn_apFeedbackProfile_call;

    //OBJECT
    private Intent intent;
    private Comments comment;

    //DATA
    private String comment_json="";
    private String printerName="";
    private String userID="";
    private String printerAddress ="";
    private String printerHp="";
    private String bankRef="";
    private String printerEmail="";
    private String rating_value="";

    private  UsersFeedbackListViewAdapter usersFeedbackListViewAdapter;

    private String data;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__view_printer_feedback_profile);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

    }

    private void linkXML() {
        tv_apFeedbackProfile_address = findViewById(R.id.tv_apFeedbackProfile_userAddress);
        tv_apFeedbackProfile_bankRef=findViewById(R.id.tv_apFeedbackProfile_bankRef);
        tv_apFeedbackProfile_email = findViewById(R.id.tv_apFeedbackProfile_email);
        tv_apFeedbackProfile_hp = findViewById(R.id.tv_apFeedbackProfile_hp);
        tv_apFeedbackProfile_name = findViewById(R.id.tv_apFeedbackProfile_name);
        tv_apFeedbackProfile_userID=findViewById(R.id.tv_apFeedbackProfile_userID);


        btn_apFeedbackProfile_approve=findViewById(R.id.btn_apFeedbackProfile_approve );
        btn_apFeedbackProfile_call =findViewById(R.id.btn_apFeedbackProfile_Call);

    }

    private void initiateData() {
        intent = getIntent();
        comment_json  = intent.getStringExtra("feedback_json");
        comment = Comments.JSONToOBJ(comment_json);

        userID = String.valueOf(comment.getCommentPrinter().getUser().getUser_id());
        bankRef =comment.getCommentPrinter().getUser().getBank_references();
        printerName = comment.getCommentPrinter().getUser().getUsername()+"(Service Provider)";
        printerEmail=comment.getCommentPrinter().getUser().getUser_email();
        printerAddress=comment.getCommentPrinter().getUser().getUser_address();
        printerHp=comment.getCommentPrinter().getUser().getUser_hp();
    }

    private void createLayoutView() {
        tv_apFeedbackProfile_userID.setText(printerName);
        tv_apFeedbackProfile_name.setText(printerName);
        tv_apFeedbackProfile_hp.setText(printerHp);
        tv_apFeedbackProfile_bankRef.setText(bankRef);
        tv_apFeedbackProfile_address.setText(printerAddress);
        tv_apFeedbackProfile_email.setText(printerEmail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void ViewListener() {
        btn_apFeedbackProfile_approve.setOnClickListener(this::onClick);
        btn_apFeedbackProfile_call.setOnClickListener(this::onClick);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_apFeedbackProfile_approve:

                JSONObject data = new JSONObject();
                try {
                    data.put("user_id",userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker = new AdminCRUDUserAccessBackgroundWorker();
                adminCRUDUserAccessBackgroundWorker.execute("update",data.toString());

            case R.id.btn_apFeedbackProfile_Call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(printerHp));
                startActivity(callIntent);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            TextView tv_printerName = view.findViewById(R.id.admin_viewFeedbackTitle);
            TextView tv_customerName= view.findViewById(R.id.admin_viewFeedbackSubtitle);
            TextView  content = view.findViewById(R.id.tv_admin_printerFeedback);

            tv_printerName.setText(comments.getCommentPrinter().getUser().getUsername());
            tv_customerName.setText(comments.getCommentPrinter().getUser().getUser_email());
            content.setText(comments.getCommentPrinter().getUser().getUser_hp());

            return view;
        }
    }

    private class AdminCRUDUserAccessBackgroundWorker extends AsyncTask<String,Void,String[]> {
        public AdminCRUDUserAccessBackgroundWorker() { }


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
            action = params[0];
            data = params[1];
            String UserURL = Global.getURL()+"CRUD_AdminBlockOrApprovedUsers.php";


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
                    System.out.println(response.getData());

                }else{
                    System.out.println("WARN:CANT UPDATE");
                }
            }
        }

    }
}