package com.example.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import model.Engineers;

public class DetailProjectActivity extends AppCompatActivity {
    TextView txtNameProject, txtTechnology, txtStatus, txtDescription, txtStartDay, txtTeam;
    int id, earning, earningPerMonth;
    String nameProject, technology, status, startDay, team, description;
    CardView cardTeam;
    ProgressBar progressBar;
    String[] mChartLabel = new String[]{"earning", "earningPerMonth", "", "", "", "", "", "", "", "", "", ""};
    PieChart pieChart;
    LinearLayout llPro;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);
        getSupportActionBar().hide();

        addControls();
    }

    private void addControls() {
        //txtNameProject = findViewById(R.id.txt_nameProject);
        txtDescription = findViewById(R.id.txt_description);
        txtTechnology = findViewById(R.id.txt_technologyProject);
        txtStartDay = findViewById(R.id.txt_startDay);
        txtStatus = findViewById(R.id.txt_status);
        txtTeam = findViewById(R.id.txt_team);
        cardTeam = findViewById(R.id.cardTem);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectActivity.this, ProjectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        runOnUiThread(new Runnable()
        {
            public void run()
            {
                DetailProjectActivity.DanhSachSanPhamTask task = new DetailProjectActivity.DanhSachSanPhamTask();
                task.execute();
            }
        });

        chart();
    }

    private void chart() {
        pieChart = (PieChart) findViewById(R.id.pieChart);

        progressBar = findViewById(R.id.progressBar);
        llPro = findViewById(R.id.ll_pro);

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

    private void setData(List<Float> amounts) {
        try {
            ArrayList<PieEntry> entries = new ArrayList<>();

            for (int i = 0; i < amounts.size(); i++) {
                entries.add(new PieEntry(amounts.get(i),
                        mChartLabel[i % mChartLabel.length]));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Project earning");

            dataSet.setDrawIcons(true);
            dataSet.setSliceSpace(2f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            // Disable Chart Value View
            dataSet.setDrawValues(true);

            // Add colors for Chart Items
            ArrayList<Integer> colors = new ArrayList<>();//
            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);

            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(12f);
            //data.setValueTextColor(Color.BLACK);
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

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> sanPhams) {

            TextView txtNameApp = findViewById(R.id.txt_nameApp);
            txtNameApp.setText(nameProject);
            //txtNameApp.setBackgroundColor(Color.RED);


            List<Float> arrAmount = new ArrayList<>();
            // Add Fix 2 Items into Chart
            arrAmount.add(Float.parseFloat(earning+"")); //99000000f
            arrAmount.add(Float.parseFloat(earningPerMonth+""));
            setData(arrAmount);

            //txtNameProject.setText(nameProject);
            txtDescription.setText(description);
            txtStartDay.setText(startDay);
            txtStatus.setText(status.toUpperCase());
            txtTeam.setText(team);
            txtTechnology.setText(technology);

            if (status.equals("pending")){
//                txtNameProject.setBackgroundColor(Color.YELLOW);
                cardTeam.setVisibility(View.GONE);
            } else if (status.equals("done")){
  //              txtNameProject.setBackgroundColor(Color.RED);
            } else if (status.equals("inProgress")){
//                txtNameProject.setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Engineers> doInBackground(Void... voids) {
            ArrayList<Engineers> dsEngineer = new ArrayList<>();
            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/projects/"+id);// link API
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
                JSONObject jsonArray = new JSONObject(builder.toString());
                nameProject = jsonArray.getString("name");
                technology = jsonArray.getString("technology");
                description = jsonArray.getString("description");
                startDay = jsonArray.getString("start");
                status = jsonArray.getString("status");
                earning = jsonArray.getInt("earning");
                earningPerMonth = jsonArray.getInt("earningPerMonth");

                JSONObject jsonObject = jsonArray.getJSONObject("team");
                team = jsonObject.getString("name");

                } catch (RuntimeException e){

            } catch (Exception ex) {
            }
            return dsEngineer;
        }
    }
}
