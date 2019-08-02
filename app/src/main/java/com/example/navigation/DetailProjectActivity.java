package com.example.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;

import Common.Common;
import Common.LinearLayoutManagerWithSmoothScroller;
import adapter.ListEngineerAdapter;
import model.Engineers;
import model.Person;

public class DetailProjectActivity extends AppCompatActivity {
    TextView txtNameProject, txtTechnology, txtStatus, txtDescription, txtStartDay, txtTeam, txtEarning, txtEndDay;
    int id, earning, earningPerMonth;
    String nameProject, technology, status, startDay, team, description, endDay;
    String name, skype, avatar, role;
    CardView cardTeam;
    ProgressBar progressBar;
    LinearLayout llPro;
    Button btnBack;
    int idTeam;
    TextView txtInform, teamvv;

    RecyclerView recycler_person;
    LinearLayoutManager layoutManager;
    ArrayList<Person> people;
    ListEngineerAdapter personAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recycler_person = findViewById(R.id.recycler_personProject);
        recycler_person.setHasFixedSize(true);
        layoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        recycler_person.setLayoutManager(layoutManager);
        recycler_person.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        people = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        personAdapter = new ListEngineerAdapter(DetailProjectActivity.this, people);
        recycler_person.setAdapter(personAdapter);

        addControls();
    }


    private void addControls() {
        //txtNameProject = findViewById(R.id.txt_nameProject);
        txtDescription = findViewById(R.id.txt_description);
        txtTechnology = findViewById(R.id.txt_technologyProject);
        txtStartDay = findViewById(R.id.txt_startDay);
        txtStatus = findViewById(R.id.txt_status);
        txtTeam = findViewById(R.id.txt_team);
        txtEarning = findViewById(R.id.txt_earning);
        txtEndDay = findViewById(R.id.txt_endDay);
        //cardTeam = findViewById(R.id.cardTem);
        teamvv = findViewById(R.id.team);

        txtInform = findViewById(R.id.txt_informTeam);
//        txtInform.setVisibility(View.GONE);

//        btnBack = findViewById(R.id.btn_back);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DetailProjectActivity.this, ProjectActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }
//        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        idTeam = intent.getIntExtra("idTeam", 0);
        DetailProjectActivity.DanhSachSanPhamTask task = new DetailProjectActivity.DanhSachSanPhamTask();
        task.execute();
    }

//    private void parseJSON() {
//        String url = "http://si-enclave.herokuapp.com/api/v1/projects/" + id;
//        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject jsonArray = response.getJSONObject(response.toString());
//                    nameProject = jsonArray.getString("name");
//                    technology = jsonArray.getString("technology");
//                    description = jsonArray.getString("description");
//                    startDay = jsonArray.getString("start");
//                    endDay = jsonArray.getString("end");
//                    status = jsonArray.getString("status");
//                    earning = jsonArray.getInt("earning");
//                    earningPerMonth = jsonArray.getInt("earningPerMonth");
//
//                    JSONObject jsonObject = jsonArray.getJSONObject("team");
//                    team = jsonObject.getString("name");
//                    idTeam = jsonObject.getInt("id");
//
//
//
//                } catch (Exception e){
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(request);
//    }

    private void parseJSON2() {
        String url = "http://si-enclave.herokuapp.com/api/v1/teams/"+idTeam;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject jsonObject1 = new JSONObject(builder2.toString());
                    JSONArray array = response.getJSONArray("engineers");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject p = (JSONObject) array.get(i);
                        int id = p.getInt("id");
                        name = p.getString("firstName");
                        skype = p.getString("email");
                        avatar = p.getString("avatar");
                        role = p.getString("role");
                        people.add(new Person(name, skype, id, avatar, role));
                    }
                    DetailProjectActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            personAdapter.updatePersonList(people);
                        }
                    });


                } catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> sanPhams) {

            Log.e("peopleDetail", name + avatar + skype);

//            TextView txtNameApp = findViewById(R.id.txt_nameApp);
//            txtNameApp.setText(nameProject);



            getSupportActionBar().setTitle(nameProject);


            //txtNameProject.setText(nameProject);
            txtDescription.setText(description);
            txtStartDay.setText(startDay.substring(0,10));
            txtStatus.setText(status.toUpperCase());
            txtTeam.setText(team);
            txtTechnology.setText(technology);
            NumberFormat currentLocale = NumberFormat.getInstance();
            String earninG = currentLocale.format(earning);
            txtEarning.setText(earninG+" VNĐ");
            txtEndDay.setText(endDay.substring(0,10));

            if (team == null){
                txtTeam.setVisibility(View.GONE);
                teamvv.setVisibility(View.GONE);
                txtInform.setVisibility(View.VISIBLE);
            }

            if (team != null ){
                parseJSON2();
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
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/projects/" + id);// link API
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
                final JSONObject jsonArray = new JSONObject(builder.toString());
                nameProject = jsonArray.getString("name");
                technology = jsonArray.getString("technology");
                description = jsonArray.getString("description");
                startDay = jsonArray.getString("start");
                endDay = jsonArray.getString("end");
                status = jsonArray.getString("status");
                earning = jsonArray.getInt("earning");
                earningPerMonth = jsonArray.getInt("earningPerMonth");

                JSONObject jsonObject = jsonArray.getJSONObject("team");
                team = jsonObject.getString("name");
                idTeam = jsonObject.getInt("id");


//                try {
//                    URL url2 = new URL("http://si-enclave.herokuapp.com/api/v1/teams/"+idTeam);// link API
//                    HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
//                    connection2.setRequestMethod("GET");
//                    connection2.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//
//                    InputStreamReader isr2 = new InputStreamReader(connection2.getInputStream(), "UTF-8");
//                    BufferedReader br2 = new BufferedReader(isr2);
//                    StringBuilder builder2 = new StringBuilder();
//                    String line2 = null;
//                    while ((line2 = br2.readLine()) != null) {
//                        builder2.append(line2);
//                    }
//                    JSONObject jsonObject1 = new JSONObject(builder2.toString());
//                    JSONArray array = jsonObject1.getJSONArray("engineers");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject p = (JSONObject) array.get(i);
//                        int id = p.getInt("id");
//                        name = p.getString("firstName");
//                        skype = p.getString("email");
//                        avatar = p.getString("avatar");
//                        role = p.getString("role");
//                        people.add(new Person(name, skype, id, avatar, role));
//
//                        //people.add(new Person(name, skype, id, avatar));
//                    }
////                    people = Common.sortList(people); // sort
////                    people = Common.addAlphabet(people);
//                    personAdapter = new ListEngineerAdapter(DetailProjectActivity.this, people);
//                    recycler_person.setAdapter(personAdapter);

            } catch (RuntimeException e) {
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dsEngineer;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Common.RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String group_character_clicked = data.getStringExtra("result");
                int position = Common.findPositionWithName(group_character_clicked, people);
                recycler_person.smoothScrollToPosition(position);
            } else {
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailProjectActivity.this, ProjectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DetailProjectActivity.this, ProjectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
                startActivity(intent);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
