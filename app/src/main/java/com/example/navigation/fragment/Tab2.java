package com.example.navigation.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.navigation.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Tab2 extends Fragment {

    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Integer> list2 = new ArrayList<>();

    private BarChart mChart;
    String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String tokenRead;
    ProgressBar progressBar;
    LinearLayout llPro;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ListEngineers task = new ListEngineers();
        task.execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        llPro = view.findViewById(R.id.ll_pro);

        mChart = view.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setVisibleXRangeMaximum(3);

        Legend legend = mChart.getLegend();
        legend.setFormSize(15f);
        legend.setTextSize(15f);
    }

    private void setData2(List<Float> arrAmount2) {
    }

    class ListEngineers extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            readData();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            ArrayList<BarEntry> yVals = new ArrayList<>();
            for (int i=0;i<list.size();i++) {
                float y = (float)list.get(i);
                yVals.add(new BarEntry(i, y));
            }

            ArrayList<BarEntry> yVals2 = new ArrayList<>();
            for (int i=0;i<list2.size();i++) {
                float y = (float)list2.get(i);
                yVals2.add(new BarEntry(i, y));
            }
            // number of column
            BarDataSet set = new BarDataSet(yVals, "Cash In(Million VND)");
            set.setColors(Color.RED);
            set.setDrawValues(true);

            BarDataSet set2 = new BarDataSet(yVals2, "Cash Out(Million VND)");
            set2.setColors(Color.BLUE);
            set2.setDrawValues(true);

            BarData data = new BarData();
            data.addDataSet(set);
            data.addDataSet(set2);
            mChart.setData(data);
            float barSpace = 0f;
            float groupSpace = 0.35f;
            data.setBarWidth(0.3f);
            mChart.getXAxis().setAxisMinimum(0);
            mChart.groupBars(0, groupSpace, barSpace);
            mChart.invalidate();
            mChart.animateY(500);

            if (mChart.getData() != null){
                llPro.setVisibility(View.GONE);
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/dashboard/cashflow/2018");// link API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", tokenRead);
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj1 = jsonArray.getJSONObject(i);
                    int  cashin =  obj1.getInt("cashIn");
                    int  cashout =  obj1.getInt("cashOut");
                    list.add(cashin/1000000);
                    list2.add(cashout/1000000);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void readData()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("token", MODE_PRIVATE);
        tokenRead = preferences.getString("token", "");
    }
}