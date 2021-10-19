package com.example.fyp.ui_customer.feedback;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Comments;
import com.example.fyp.domain.Printer;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

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

public class FeedbackActivity extends AppCompatActivity  implements View.OnClickListener {

    //View
    private ListView lv_custFeedback;
    private Button btn_saveCustFeedback;
    private EditText et_CustomerFeedback;

    //Object
    private ArrayList<Comments> FeedbackAL = new ArrayList<>();
    private FeedbackListViewAdapter feedbackListViewAdapter;
    private User user;
    private Printer printer;
    //Data
    private String data;
    private String this_customer_comment_id;
    private String comment_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        linkXML();
        initiateData();
      //createLayoutView();
        ViewListener();
    }

    private void linkXML() {
        lv_custFeedback = findViewById(R.id.lv_custFeedback);
        et_CustomerFeedback = findViewById(R.id.et_CustomerFeedback) ;
        btn_saveCustFeedback=findViewById(R.id.btn_saveCustFeedback);
    }

    private void initiateData() {
        user = Global.user;
        printer = Printer.JSONToOBJ(Global.printer_json);
        //Create json for "data"
        JSONObject data =new JSONObject();

        try {
            data.put("cust_id", user.getCustomer().getCustId());
            data.put("printer_id",printer.getPrinter_id() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        custFeedbackBW custFeedbackBW = new custFeedbackBW();
        custFeedbackBW.execute("read",data.toString());

    }

    private void createLayoutView() {
        if(FeedbackAL!=null) {
            feedbackListViewAdapter = new FeedbackListViewAdapter(this, FeedbackAL);
            lv_custFeedback.setAdapter(feedbackListViewAdapter);
//        System.out.println(FeedbackArrayList.get(0).getComment_content());
            fetchThisCustomerComment();
        }
    }

    private void ViewListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_saveCustFeedback.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_saveCustFeedback) {

            comment_content = et_CustomerFeedback.getText().toString();

            if(comment_content!=null) {
                //Create json for "data"
                JSONObject data = new JSONObject();
                try {
                    data.put("comment_id", this_customer_comment_id);
                    data.put("cust_id", user.getCustomer().getCustId());
                    data.put("printer_id", printer.getPrinter_id() );
                    data.put("comment_content", comment_content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(fetchThisCustomerComment()==1) {
                    custFeedbackBW custFeedbackBW = new custFeedbackBW();
                    custFeedbackBW.execute("update", data.toString());
                }else{
                    custFeedbackBW custFeedbackBW = new custFeedbackBW();
                    custFeedbackBW.execute("create", data.toString());
                }
            }else{
                Global.displayToast(this,"Please write down your feedback",Toast.LENGTH_SHORT,"yellow");
            }
        }
    }

    private class FeedbackListViewAdapter extends ArrayAdapter<Comments> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<Comments>commentsArr = new ArrayList<>();

        public FeedbackListViewAdapter(Activity context, ArrayList<Comments> commentsArr) {
            super(context, R.layout.feedback_list, FeedbackAL);
            this.context = context;
            this.commentsArr = FeedbackAL;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.feedback_list, null);
            Comments comments = getItem(position);

            TextView tv_FeedbackCustomerComment= view.findViewById(R.id.tv_feedbackCustomerComment);
            TextView tv_feedbackCustName= view.findViewById(R.id.tv_feedbackCustName);

            tv_FeedbackCustomerComment.setText(comments.getComment_content());
            tv_feedbackCustName.setText(comments.getCommentCustomer().getUser().getUsername());


            return view;
        }

        @Override
        public int getCount() {
            return commentsArr.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    private int fetchThisCustomerComment(){
        int exist = 0;
        //Check if the comment existes based on specific cust id and printer id
        for(int i=0;i<FeedbackAL.size();i++){
            System.out.println("HERE");
            if(FeedbackAL.get(i).getCommentCust_id().equals(user.getCustomer().getCustId())){
                //Update the Database
                this_customer_comment_id = FeedbackAL.get(i).getComment_id();
                et_CustomerFeedback.setText(FeedbackAL.get(i).getComment_content());
                exist =1;
                break;
            }
        }
        return exist;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // ?How to make the return more nice?
            case android.R.id.home:
               this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class custFeedbackBW extends AsyncTask<String,Void,String[]> {
        public custFeedbackBW() { }


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
            String UserURL = Global.getURL()+"CRUD_customer_CustomerComment.php";


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
                System.out.println(result[1]);
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    System.out.println(response.getData());
                    FeedbackAL = Comments.JSONToLIST(response.getData());
                    if((FeedbackAL.size()!=0)) {
                        createLayoutView();
                    }

                }else{
                    System.out.println("Cant connect to server");
                }
            }
        }

    }


}