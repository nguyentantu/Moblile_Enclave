package com.example.navigation;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tab1 extends Fragment {

    int inProgress, pending, done;
    ProgressBar progressBar;
    LinearLayout llPro;

    String[] mChartLabel = new String[]{"Inprogress", "Pending", "Done", "", "", "", "", "", "", "", "", ""};
    PieChart pieChart;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String txtaa;
    private String mParam2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

//        parseJSON();

        ListEngineers task = new ListEngineers();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        pieChart = (PieChart) view.findViewById(R.id.pieChart);

        progressBar = view.findViewById(R.id.progressBar);
        llPro = view.findViewById(R.id.ll_pro);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawEntryLabels(false); // disable label items

        // Disable Legend Chart View
        Legend l = pieChart.getLegend();
        l.setEnabled(true);

        // Hole View
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad); // Rotate Event
        // Start Rotation View
        pieChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

    }

    private void parseJSON() {
        final List<Float> arrAmount = new ArrayList<>();
        String url = "http://si-enclave.herokuapp.com/api/v1/dashboard/projects";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject array = response.getJSONObject(toString());
                    int inProgress = array.getInt("inProgress");
                    int pending = array.getInt("pending");
                    int done = array.getInt("done");
                    arrAmount.add(Float.parseFloat(inProgress+"")); //99000000f
                    arrAmount.add(Float.parseFloat(pending+""));
                    arrAmount.add(Float.parseFloat(done+""));
                    setData(arrAmount);

                    } catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void setData(List<Float> amounts) {
        try {
            ArrayList<PieEntry> entries = new ArrayList<>();

            for (int i = 0; i < amounts.size(); i++) {
                entries.add(new PieEntry(amounts.get(i),
                        mChartLabel[i % mChartLabel.length]));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Project statistical");

            dataSet.setDrawIcons(true);

            dataSet.setSliceSpace(2f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            // Disable Chart Value View
            dataSet.setDrawValues(true);

            // Add colors for Chart Items
            ArrayList<Integer> colors = new ArrayList<>();//
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            pieChart.setData(data);
            // undo all highlights
            pieChart.highlightValues(null);
            pieChart.invalidate();
            if (pieChart.getData() != null){
                llPro.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ListEngineers extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            List<Float> arrAmount = new ArrayList<>();
            // Add Fix 2 Items into Chart
            arrAmount.add(Float.parseFloat(inProgress+"")); //99000000f
            arrAmount.add(Float.parseFloat(pending+""));
            arrAmount.add(Float.parseFloat(done+""));
            setData(arrAmount);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/dashboard/projects");// link API
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
                JSONObject jsonObject = new JSONObject(builder.toString());
                inProgress = jsonObject.getInt("inProgress");
                pending = jsonObject.getInt("pending");
                done = jsonObject.getInt("done");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}