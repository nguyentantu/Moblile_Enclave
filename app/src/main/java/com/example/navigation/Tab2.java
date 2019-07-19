package com.example.navigation;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
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

public class Tab2 extends Fragment {

    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Integer> list2 = new ArrayList<>();

    private BarChart mChart;
    int totalEn, totalPro;
    String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

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
//        txt2 = view.findViewById(R.id.txt2);
//        txt2.setText(aaa);
//        txt2.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart = view.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
//
//        final ArrayList<String> xLabels = new ArrayList<>();
//        xLabels.add("Jan");
//        xLabels.add("Feb");
//        xLabels.add("Mar");
//        xLabels.add("Apr");
//        xLabels.add("May");
//        xLabels.add("Jun");
//        xLabels.add("Jul");
//        xLabels.add("Aug");
//        xLabels.add("Sep");
//        xLabels.add("Oct");
//        xLabels.add("Nov");
//        xLabels.add("Dec");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setVisibleXRangeMaximum(3);


//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xLabels.get((int) value);
//            }
//        });
//
//        List<Float> arrAmount2 = new ArrayList<>();
//        setData2(arrAmount2);
//        mChart.setFitBars(true);
    }

    private void setData2(List<Float> arrAmount2) {

//        ArrayList<PieEntry> entries = new ArrayList<>();
//
//        for (int i = 0; i < arrAmount2.size(); i++) {
//            entries.add(new PieEntry(arrAmount2.get(i),
//                    mChartLabel[i % mChartLabel.length]));
//        }

        
    }

    class ListEngineers extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {

            ArrayList<BarEntry> yVals = new ArrayList<>();
            for (int i=0;i<list.size();i++) {

                float y = (float)list.get(i);

                yVals.add(new BarEntry(i, y));
            }
            Log.e("vvvvvvvvv", yVals+"");

//            yVals.add(new BarEntry(1, Float.parseFloat(totalPro + "")));
//            yVals.add(new BarEntry(2, Float.parseFloat(totalEn + "")));
//            yVals.add(new BarEntry(3, Float.parseFloat(totalPro + "")));
//            yVals.add(new BarEntry(4, Float.parseFloat(totalEn + "")));
//            yVals.add(new BarEntry(5, Float.parseFloat(totalPro + "")));
//            yVals.add(new BarEntry(6, Float.parseFloat(totalEn + "")));
//            yVals.add(new BarEntry(7, Float.parseFloat(totalPro + "")));
//            yVals.add(new BarEntry(8, Float.parseFloat(totalEn + "")));
//            yVals.add(new BarEntry(9, Float.parseFloat(totalPro + "")));
//            yVals.add(new BarEntry(10, Float.parseFloat(totalEn + "")));
//            yVals.add(new BarEntry(11, Float.parseFloat(totalPro + "")));


            ArrayList<BarEntry> yVals2 = new ArrayList<>();
            for (int i=0;i<list2.size();i++) {

                float y = (float)list2.get(i);

                yVals2.add(new BarEntry(i, y));
            }

            // number of column
            BarDataSet set = new BarDataSet(yVals, "cash in(million VND)");
            set.setColors(Color.RED);
            set.setDrawValues(true);

            BarDataSet set2 = new BarDataSet(yVals2, "cash out(million VND)");
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

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/dashboard/cashflow/2018");// link API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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

//                JSONObject jsonObject = new JSONObject(builder.toString());
//                totalEn = jsonObject.getInt("engineer");
//                totalPro = jsonObject.getInt("project");

                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}