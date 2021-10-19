package com.example.fyp.ui_admin.admin_home;

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

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;

import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class AdminHomeFragment extends Fragment implements View.OnClickListener {

    //View
    private View root ;
    private ListView lv_users_byadmin ;

    // Data
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String accessStatus;

    private String data;
    private String action;

    //Object
    private UsersListViewAdapter usersListViewAdapter;
    private ArrayList<User> usersArrList ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences=this.getActivity().getSharedPreferences("login_status",0);
        editor=sharedPreferences.edit();
        root= inflater.inflate(R.layout.fragment_admin_home, container, false);
        linkXML();
        initiateData();
        createLayoutView();
        ViewListener();

        return root;
    }

    private void linkXML() {
        //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_admin);
        lv_users_byadmin = root.getRootView().findViewById(R.id.lv_ShowUserByAdmin);
    }

    private void initiateData() {
       AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker =new AdminCRUDUserAccessBackgroundWorker();
        adminCRUDUserAccessBackgroundWorker.execute("read","");
    }

    private void createLayoutView() {
    }

    private void ViewListener() {
        lv_users_byadmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String user_json = User.OBJtoJSON(usersArrList.get(position));

                Intent intent = new Intent(getActivity(), AdminMarkAccessStatus.class);
                intent.putExtra("user_json",user_json);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private class UsersListViewAdapter extends ArrayAdapter<User> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<User> users;

        public UsersListViewAdapter(Activity context, ArrayList<User> users) {
            super(context, R.layout.users_for_admin_list, users);
            this.context = context;
            this.users = users;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.users_for_admin_list, null);

            User users = getItem(position);

            ImageView ivIcon = view.findViewById(R.id.iv_userIMG);
            TextView tv_username = view.findViewById(R.id.tv_username_byadmin);
            TextView tv_userid = view.findViewById(R.id.tv_userid_byadmin);
            TextView tv_userhp = view.findViewById(R.id.tv_userhp_byadmin);
            TextView tv_userEmail = view.findViewById(R.id.tv_useremail_byadmin);
            TextView tv_userRole = view.findViewById(R.id.tv_UserRole_byadmin);
            Button btn_userstatus = view.findViewById(R.id.btn_userstatus_byadmin);

            tv_username.setText(users.getUsername());
            tv_userEmail.setText(users.getUser_email());
            tv_userhp.setText(users.getUser_hp());
            tv_userid.setText(String.valueOf(users.getUser_id()));
            tv_userRole.setText(users.getUser_role());
            btn_userstatus.setText(users.getStatus());
          //  ivIcon.setBackgroundResource(R.drawable.avatar_logo);

            btn_userstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(users.getStatus().equals("Approved")){
                        accessStatus="Blocked";
                        users.setStatus(accessStatus);
                        btn_userstatus.setText("Blocked");
                    }else{
                        accessStatus="Approved";
                        users.setStatus(accessStatus);
                        btn_userstatus.setText("Approved");
                    }

                    System.out.println(User.OBJtoJSON(users));

                    AdminCRUDUserAccessBackgroundWorker adminCRUDUserAccessBackgroundWorker1 =new AdminCRUDUserAccessBackgroundWorker();
                    adminCRUDUserAccessBackgroundWorker1.execute("update",User.OBJtoJSON(users));
                }
            });
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
                System.out.println(result[1]);
                Response response = Response.JSONToOBJ(result[1]);
                if(response.getMessage().equals("Success")) {
                    if(action.equals("read")) {
                        //ordersArrayList = new ArrayList<>();
                        System.out.println(response.getData());
                        usersArrList = new ArrayList();
                        usersArrList = User.JSONToLIST(response.getData());
                        usersListViewAdapter = new UsersListViewAdapter(getActivity(), usersArrList);
                        lv_users_byadmin.setAdapter(usersListViewAdapter);
                    }
                   // Intent i = new Intent(getActivity(),AdminMarkAccessStatus.class);
                   // startActivity(i);
                }else{
                    System.out.println("WARN:Admin Home Fragment- No ListView");
                }
            }
        }

    }



}