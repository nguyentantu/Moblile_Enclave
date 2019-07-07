package com.example.navigation;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Engineers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //public static final int[] MATERIAL_COLORS = {rgb("#20b1c3"), rgb("#6968b2"), rgb("#e6892e"), rgb("#202024"), rgb("#7d7d7f")};
    protected String[] mChartLabel = new String[]{"Male", "Female", "aaa", "hhh"};
    private PieChart pieChart;

    int totalEn = 0;
    int totalPro = 0;
    int totalTeam = 0;
    int totalManager = 0;
    int id;

    String email, lastName, firstName;

    TextView txtTotalEngineer, txtProject, txtTeam, txtManager, txtGmail, txtNameAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enclave App");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        addControls();
    }

    private void addControls() {

        txtTotalEngineer = findViewById(R.id.txt_engineers);
        txtProject = findViewById(R.id.txt_pro);
        txtTeam = findViewById(R.id.txt_team);
        txtManager = findViewById(R.id.txt_manager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view );
        View headerView = navigationView.getHeaderView(0);
        txtGmail =  headerView.findViewById(R.id.txt_gmail);
        txtNameAdmin = headerView.findViewById(R.id.txt_nameHeader);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        LinearLayout llEngineer = findViewById(R.id.ll_engineer);
        llEngineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListEngineerActivity.class);
                startActivity(intent);
            }
        });

        MainActivity.ListEngineers task = new MainActivity.ListEngineers();
        task.execute();


//        Switch aSwitch = findViewById(R.id.switch1);
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (b){
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                } else {
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
//            }
//        });

        pieChart = (PieChart) findViewById(R.id.linechart);

        createChart();
    }

    private void createChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawEntryLabels(true); // disable label items

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

        List<Float> arrAmount = new ArrayList<>();
        // Add Fix 3 Items into Chart
        arrAmount.add(50f); //99000000f
        arrAmount.add(30f);
//        arrAmount.add(20f);
//        arrAmount.add(10f);
        setData(arrAmount);
    }

    private void setData(List<Float> amounts) {
        try {
            ArrayList<PieEntry> entries = new ArrayList<>();

            for (int i = 0; i < amounts.size(); i++) {
                entries.add(new PieEntry(amounts.get(i),
                        mChartLabel[i % mChartLabel.length]));
            }

            PieDataSet dataSet = new PieDataSet(entries, "...............");

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ListEngineers extends AsyncTask<Void, Void, ArrayList<Engineers>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> engineers) {
            super.onPostExecute(engineers);

            txtGmail.setText(email);
            txtNameAdmin.setText(firstName+" "+lastName);

            //txtProject.setText(totalPro+"");
            ValueAnimator animator = ValueAnimator.ofInt(1, totalEn);
            ValueAnimator animator2 = ValueAnimator.ofInt(1, totalPro);
            ValueAnimator animator3 = ValueAnimator.ofInt(1, totalTeam);
            ValueAnimator animator4 = ValueAnimator.ofInt(0, totalManager);//0 is min number, 600 is max number
            animator.setDuration(3000); //Duration is in milliseconds
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtTotalEngineer.setText(animation.getAnimatedValue().toString());
                }
            });
            animator.start();

            animator2.setDuration(600); //Duration is in milliseconds
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtProject.setText(animation.getAnimatedValue().toString());
                }
            });
            animator2.start();

            animator3.setDuration(500); //Duration is in milliseconds
            animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtTeam.setText(animation.getAnimatedValue().toString());
                }
            });
            animator3.start();

            animator4.setDuration(300); //Duration is in milliseconds
            animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtManager.setText(animation.getAnimatedValue().toString());
                }
            });
            animator4.start();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Engineers> doInBackground(Void... voids) {
            ArrayList<Engineers> dsEngineer = new ArrayList<>();
            try {
                URL url = new URL("https://cool-demo-api.herokuapp.com/api/v1/dashboard");// link API
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
                    totalEn = jsonArray.getInt("engineer");
                    totalPro = jsonArray.getInt("project");
                    totalTeam = jsonArray.getInt("team");
                    totalManager = jsonArray.getInt("manager");

                try {
                    URL url2 = new URL("https://cool-demo-api.herokuapp.com/api/v1/engineers/"+id);// link API
                    HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                    InputStreamReader isr2 = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader br2 = new BufferedReader(isr2);
                    StringBuilder builder2 = new StringBuilder();
                    String line2 = null;
                    while ((line2 = br2.readLine()) != null) {
                        builder2.append(line2);
                    }
                    JSONObject jsonArray2 = new JSONObject(builder2.toString());
                    email = jsonArray2.getString("email");
                    firstName = jsonArray2.getString("firstName");
                    lastName = jsonArray2.getString("lastName");
                } catch (Exception e){

                }
                br.close();

            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return dsEngineer;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem= menu.findItem(R.id.myswitch);
        View view = MenuItemCompat.getActionView(menuItem);
        final Switch switcha = (Switch) view.findViewById(R.id.switchForActionBar);
        switcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout  mDrawerLayout = findViewById(R.id.drawer_layout);
        if (id == R.id.nav_home) {
            mDrawerLayout.closeDrawers();
        }
//        } else if (id == R.id.nav_profile) {
//
//        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent1);
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();
        } else if (id == R.id.nav_share) {
            Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
            webPageIntent.setData(Uri.parse("https://www.facebook.com/enclaveit/"));
            startActivity(webPageIntent);
        } else if (id == R.id.nav_send) {
            Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
            webPageIntent.setData(Uri.parse("http://enclaveit.com/"));
            startActivity(webPageIntent);
        } else if (id == R.id.myswitch){

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
