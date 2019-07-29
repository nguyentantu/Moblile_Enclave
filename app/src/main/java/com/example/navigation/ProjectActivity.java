package com.example.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

import adapter.ProjectAdapter;
import model.Project;

public class ProjectActivity extends AppCompatActivity {

    ProjectAdapter projectAdapter;
    ListView lvProject;
    Project project;
    ArrayList<Project> projects;

    String nameProject, techonology, category, status;
    int earning, id;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        getSupportActionBar().hide();

        addControls();
    }

    private void addControls() {
        lvProject = findViewById(R.id.lv_project);
        lvProject.setAdapter(new ProjectAdapter(ProjectActivity.this, R.layout.itemsproject));
        projectAdapter = new ProjectAdapter(ProjectActivity.this, R.layout.itemsproject);
        lvProject.setAdapter(projectAdapter);

        TextView txtToolbar = findViewById(R.id.txt_nameApp);
        txtToolbar.setText("List Projects");
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        requestQueue = Volley.newRequestQueue(this);
//        ProjectActivity.DanhSachSanPhamTask task = new ProjectActivity.DanhSachSanPhamTask();
//        task.execute();
        parseJSON();
    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/projects?limit=50&offset=0");// link API
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
                    id = p.getInt("id");
                    earning = p.getInt("earning");
                    nameProject = p.getString("name");
                    techonology = p.getString("technology");
                    status = p.getString("status");

                   JSONObject jsonObject =p.getJSONObject("category");
                    category = jsonObject.getString("name");
                    project = new Project(nameProject, techonology, category, status, earning, id);
                    projectAdapter.add(project);
                }

                br.close();

            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return null;
        }
    }

    private void parseJSON() {
        String url = "http://si-enclave.herokuapp.com/api/v1/projects?limit=10&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int  i=0;i<array.length();i++){
                        JSONObject p = (JSONObject)array.get(i);
                        id = p.getInt("id");
                        earning = p.getInt("earning");
                        nameProject = p.getString("name");
                        techonology = p.getString("technology");
                        status = p.getString("status");

                        JSONObject jsonObject =p.getJSONObject("category");
                        category = jsonObject.getString("name");
                        project = new Project(nameProject, techonology, category, status, earning, id);
                        projectAdapter.add(project);
                    }

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
        //mProgress.dismiss();
    }
}
