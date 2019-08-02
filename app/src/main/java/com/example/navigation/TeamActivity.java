package com.example.navigation;

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

import java.util.ArrayList;

import adapter.TeamAdapter;
import model.Teams;

public class TeamActivity extends AppCompatActivity {

    private ArrayList<Teams> mExampleList;

    private RecyclerView mRecyclerView;
    private TeamAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RequestQueue requestQueue;

    private SearchView searchView;

//    RecyclerView recycler_person;
//    LinearLayoutManager layoutManager;
//    ArrayList<Teams> teams;
//    TeamAdapter teamAdapter;
//    RequestQueue requestQueue;
//    Button btnBack;
//    private GridLayoutManager gridLayoutManager;


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
//        getSupportActionBar().hide();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Team List");

        requestQueue = Volley.newRequestQueue(this);

        createExampleList();
        buildRecyclerView();

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
//            }
//        });

    }

    private void filter(String text) {
        ArrayList<Teams> filteredList = new ArrayList<>();

        for (Teams item : mExampleList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) | item.getProjectName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);
    }

    private void createExampleList() {
        mExampleList = new ArrayList<>();

        String url = "http://si-enclave.herokuapp.com/api/v1/teams?limit=100&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int  i=0;i<array.length();i++){
                        JSONObject p = (JSONObject)array.get(i);
                        int id = p.getInt("id");
                        int totalMember = p.getInt("totalMember");
                        String name = p.getString("name");
                        String projectName = p.getString("projectName");

                        mExampleList.add(new Teams(id, name, projectName, totalMember));
                    }

                    mAdapter = new TeamAdapter(TeamActivity.this, mExampleList);
                    mRecyclerView.setAdapter(mAdapter);

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
//
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "One", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Two", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Three", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Four", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Five", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Six", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Seven", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Eight", "Line 2"));
//        mExampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Nine", "Line 2"));

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_team);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TeamAdapter(TeamActivity.this, mExampleList);

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
                Intent intent = new Intent(TeamActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
                startActivity(intent);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TeamActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
        startActivity(intent);
        super.onBackPressed();
    }

}