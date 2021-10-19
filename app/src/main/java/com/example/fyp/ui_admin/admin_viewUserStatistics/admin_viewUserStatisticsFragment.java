package com.example.fyp.ui_admin.admin_viewUserStatistics;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.common.Global;
import com.example.fyp.domain.Response;
import com.example.fyp.domain.User;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

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

public class admin_viewUserStatisticsFragment extends Fragment {
    
    //View
    private View root;
    private TextView tv_customerPieNum;
    private TextView tv_printerPieNum;
    private TextView tv_UserTotalNum;
    
    //Object
    private ArrayList<User> userArrayList;
    private PieChart pieChart;
//    private PieData pieData;
//    private PieDataSet pieDataSet;
    
    //Data
    private String action=" ";
    private String data=" ";
    private int customerNumber;
    private int printerNumber;
    private int userNumber;
    private ArrayList pieEntries;
    private ArrayList PieEntryLabels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_admin_view_user_statistics, container, false);
        linkXML();
        initiateData();
       // createLayoutView();
        ViewListener();
        return root;
    }

    private void linkXML() {

        pieChart = root.getRootView().findViewById(R.id.tv_pieChart);
        tv_customerPieNum = root.getRootView().findViewById(R.id.tv_CustomerPieNum);
        tv_printerPieNum = root.getRootView().findViewById(R.id.tv_PrintePieNum);
        tv_UserTotalNum = root.getRootView().findViewById(R.id.tv_UserTotalNum);


    }

    private void initiateData() {
        ViewUserStatisticsBW viewUserStatisticsBW = new ViewUserStatisticsBW();
        viewUserStatisticsBW.execute("read",data);
    }


    private void createLayoutView() {

        createPieChart();

        tv_customerPieNum.setText(String.valueOf(customerNumber));
        tv_UserTotalNum.setText(String.valueOf(userNumber));
        tv_printerPieNum.setText(String.valueOf(printerNumber));

    }

    private void ViewListener() {

    }

    private void CalculateUserStatistics() {
        userNumber = userArrayList.size();
        for(int i=0; i<userArrayList.size();i++){
            if(userArrayList.get(i).getUser_role().equals("customer")){
                customerNumber+=1;
            }else{
                printerNumber+=1;
            }
        }
    }
    private void createPieChart(){
//        getEntries();
//        pieDataSet = new PieDataSet(pieEntries, "");
//        pieDataSet.setValueTextSize(100f);
//        pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        pieDataSet.setSliceSpace(2f);
//        pieDataSet.setValueTextColor(Color.WHITE);
//        pieDataSet.setValueTextSize(50f);
//        pieDataSet.setSliceSpace(5f);
        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Customer",
                        printerNumber,
                        Color.parseColor("#80CBC4")));
        pieChart.addPieSlice(
                new PieModel(
                        "Printer",
                        customerNumber,
                        Color.parseColor("#9C27B0")));

        // To animate the pie chart
        pieChart.startAnimation();

    }
//    private void getEntries() {
//        pieEntries = new ArrayList<>();
//        pieEntries.add(new PieEntry(customerNumber, "Customer"));
//        pieEntries.add(new PieEntry(printerNumber, "Printer"));
//
//    }

    private class ViewUserStatisticsBW extends AsyncTask<String,Void,String[]> {
        public ViewUserStatisticsBW() { }


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
            String UserURL = Global.getURL()+"CRUD_admin_ViewCustomerStatistics.php";


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
                    userArrayList = new ArrayList<>();
                    System.out.println(response.getData());
                    userArrayList = User.JSONToLIST(response.getData());

                    CalculateUserStatistics();
                    createLayoutView();

                }else{
                    System.out.println("Cant fetch data from server");
                }
            }
        }

    }
}