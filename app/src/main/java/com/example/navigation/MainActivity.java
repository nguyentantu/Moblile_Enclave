package com.example.navigation;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import adapter.PagerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Engineers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    int totalEn = 0;
    int totalPro = 0;
    int totalTeam = 0;
    int totalManager = 0;
    int id;

    String email, lastName, firstName, Strava;
    CircleImageView avatar;
    TextView txtTotalEngineer, txtProject, txtTeam, txtManager, txtGmail, txtNameAdmin;
    String tokenRead;

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
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setVisibility(View.GONE);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                addBottomDots(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        addControls();
    }

    private void addBottomDots(int position) {

        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[position].setTextColor(getResources().getColor(R.color.dot_active));
    }

    private void addControls() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        layouts = new int[]{
                R.layout.fragment1,
                R.layout.fragment2,
//              R.layout.fragment3
        };

        txtTotalEngineer = findViewById(R.id.txt_engineers);
        txtProject = findViewById(R.id.txt_pro);
        txtTeam = findViewById(R.id.txt_team);
        txtManager = findViewById(R.id.txt_manager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view );
        View headerView = navigationView.getHeaderView(0);
        txtGmail =  headerView.findViewById(R.id.txt_gmail);
        txtNameAdmin = headerView.findViewById(R.id.txt_nameHeader);
        avatar = headerView.findViewById(R.id.img_profile);

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

        LinearLayout llTeams = findViewById(R.id.ll_team);
        llTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llProjects = findViewById(R.id.ll_project);
        llProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Project2Activity.class);
                startActivity(intent);
            }
        });

        MainActivity.ListEngineers task = new MainActivity.ListEngineers();
        task.execute();
    }

    class ListEngineers extends AsyncTask<Void, Void, ArrayList<Engineers>> {

        @Override
        protected void onPreExecute() {
            readData();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> engineers) {
            super.onPostExecute(engineers);
            // Set avatar
            ColorGenerator generator  = ColorGenerator.MATERIAL;

            txtGmail.setText(email);
            txtNameAdmin.setText(firstName+" "+lastName);
            Picasso.with(MainActivity.this).load(Strava).into(avatar);

            ValueAnimator animator = ValueAnimator.ofInt(1, totalEn);
            ValueAnimator animator2 = ValueAnimator.ofInt(1, totalPro);
            ValueAnimator animator3 = ValueAnimator.ofInt(1, totalTeam);
            ValueAnimator animator4 = ValueAnimator.ofInt(0, totalManager);//0 is min number, 600 is max number
            animator.setDuration(2500); //Duration is in milliseconds
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtTotalEngineer.setText(animation.getAnimatedValue().toString());
                }
            });
            animator.start();

            animator2.setDuration(2000); //Duration is in milliseconds
            animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtProject.setText(animation.getAnimatedValue().toString());
                }
            });
            animator2.start();

            animator3.setDuration(2000); //Duration is in milliseconds
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
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/dashboard/total");// link API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", tokenRead);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject jsonObject = new JSONObject(builder.toString());
                    totalEn = jsonObject.getInt("engineer");
                    totalPro = jsonObject.getInt("project");
                    totalTeam = jsonObject.getInt("team");
                    totalManager = jsonObject.getInt("manager");
                try {
                    URL url2 = new URL("http://si-enclave.herokuapp.com/api/v1/engineers/"+id);// link API
                    HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", tokenRead);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                    Strava = jsonArray2.getString("avatar");
                } catch (Exception e){
                }
                br.close();
            } catch (Exception ex) {
            }
            return dsEngineer;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout  mDrawerLayout = findViewById(R.id.drawer_layout);
        if (id == R.id.nav_home) {
            mDrawerLayout.closeDrawers();
        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
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
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void readData()
    {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        tokenRead = preferences.getString("token", "");
    }
}
