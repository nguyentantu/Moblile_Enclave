package com.example.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Common.LinearLayoutManagerWithSmoothScroller;
import adapter.TeamAdapter;
import model.Engineers;
import model.Teams;

public class TeamActivity extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    private GridLayoutManager gridLayoutManager;
//    private TeamAdapter teamAdapter;
//    private List<Teams> teams;

    RecyclerView recycler_person;
    LinearLayoutManager layoutManager;
    ArrayList<Teams> teams;
    TeamAdapter teamAdapter;
    RequestQueue requestQueue;
    Button btnBack;
    private GridLayoutManager gridLayoutManager;


//    TeamAdapter teamAdapter;
//    ListView lvTeam;
//    Teams teams;
//
    String name, projectName;
    int totalMember;
    //RequestQueue requestQueue;
    TextView txtToolbar;
    int id;
    EditText aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        getSupportActionBar().hide();

//        addControls();

        recycler_person = findViewById(R.id.recycler_team);
        recycler_person.setHasFixedSize(true);
        layoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        recycler_person.setLayoutManager(layoutManager);
        recycler_person.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        teams = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //mProgress = new SpotsDialog(this, R.style.Custom);
        //mProgress.show();

        gridLayoutManager = new GridLayoutManager(this,2);
        recycler_person.setLayoutManager(gridLayoutManager);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        TextView txtNameApp = findViewById(R.id.txt_nameApp);
        txtNameApp.setText("Team List");

        parseJSON();
    }

//    private void addControls() {
//        lvTeam = findViewById(R.id.lv_team);
//        teamAdapter = new TeamAdapter(TeamActivity.this, R.layout.teamsitem);
//        lvTeam.setAdapter(teamAdapter);
//
//        txtToolbar = findViewById(R.id.txt_nameApp);
//        txtToolbar.setText("List Teams");
//
//        Button btnBack = findViewById(R.id.btn_back);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TeamActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }
//        });
//        requestQueue = Volley.newRequestQueue(this);
//        parseJSON();
//    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> sanPhams) {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Engineers> doInBackground(Void... voids) {
            ArrayList<Engineers> dsEngineer = new ArrayList<>();
            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/teams?limit=100&offset=0");// link API
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
                JSONArray array = jsonArray.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject p = (JSONObject) array.get(i);
                     totalMember = p.getInt("totalMember");
                     name = p.getString("name");
                     projectName = p.getString("projectName");
//                     teams = new Teams(name, projectName, totalMember, id);
//                     teamAdapter.add(teams);
                }

                br.close();

            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return dsEngineer;
        }
    }

    private void parseJSON() {
        String url = "http://si-enclave.herokuapp.com/api/v1/teams?limit=100&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int  i=0;i<array.length();i++){
                        JSONObject p = (JSONObject)array.get(i);
                        id = p.getInt("id");
                        totalMember = p.getInt("totalMember");
                        name = p.getString("name");
                        projectName = p.getString("projectName");

                        Teams person = new Teams();
                        teams.add(new Teams(name, projectName, totalMember, id));
                    }

                    teamAdapter = new TeamAdapter(TeamActivity.this, teams);
                    recycler_person.setAdapter(teamAdapter);

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
}
