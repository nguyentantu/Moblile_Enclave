package com.example.navigation;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.Project2Adapter;
import dmax.dialog.SpotsDialog;
import model.Project;

public class Project2Activity extends AppCompatActivity {

    String nameProject, techonology, category, status;
    int earning, id, total, idTeam;

    private ArrayList<Project> mExampleList;
    private RecyclerView mRecyclerView;
    private Project2Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AlertDialog mProgress;
    RequestQueue requestQueue;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Project List");

        requestQueue = Volley.newRequestQueue(this);

        mProgress = new SpotsDialog(this, R.style.Custom);
        mProgress.show();

        createExampleList();
        buildRecyclerView();
    }

    private void filter(String text) {
        ArrayList<Project> filteredList = new ArrayList<>();

        for (Project item : mExampleList) {
            if (item.getNameProject().toLowerCase().contains(text.toLowerCase()) | item.getCategory().toLowerCase().contains(text.toLowerCase()) | item.getStatus().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

    private void createExampleList() {
        mExampleList = new ArrayList<>();

        String url = "http://si-enclave.herokuapp.com/api/v1/projects?limit=1000&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject p = (JSONObject) array.get(i);
                        id = p.getInt("id");
                        earning = p.getInt("earning");
                        nameProject = p.getString("name");
                        techonology = p.getString("technology");
                        status = p.getString("status");
                        JSONObject jsonObject = p.getJSONObject("category");
                        category = jsonObject.getString("name");
                        mExampleList.add(new Project(nameProject, techonology, category, status, earning, id));
//                        movie = new Project(nameProject, techonology, category, status, earning, id);
//                        itemsList.add(movie);
                    }
                    mAdapter = new Project2Adapter(Project2Activity.this, mExampleList);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgress.dismiss();
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

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_project);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Project2Adapter(Project2Activity.this, mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Project2Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
                startActivity(intent);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Project2Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
        startActivity(intent);
        super.onBackPressed();
    }
}
