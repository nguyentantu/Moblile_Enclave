package com.example.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import Common.Common;
import Common.LinearLayoutManagerWithSmoothScroller;
import adapter.PersonAdapter;
import dmax.dialog.SpotsDialog;
import model.Person;

public class ListEngineerActivity extends AppCompatActivity {

    RecyclerView recycler_person;
    LinearLayoutManager layoutManager;
    ArrayList<Person> people;
    PersonAdapter personAdapter;
    RequestQueue requestQueue;
    AlertDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_engineer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Engineer List");

        recycler_person = findViewById(R.id.recycler_person);
        recycler_person.setHasFixedSize(true);
        layoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        recycler_person.setLayoutManager(layoutManager);
        recycler_person.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        people = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        mProgress = new SpotsDialog(this, R.style.Custom);
        mProgress.show();

        parseJSON();
    }

    private void parseJSON() {
        String url = "http://si-enclave.herokuapp.com/api/v1/engineers?limit=1000&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("results");
                    for (int  i=0;i<array.length();i++){
                        JSONObject p = (JSONObject)array.get(i);
                        int id = p.getInt("id");
                        String name = p.getString("firstName");
                        String skype = p.getString("skype");
                        String avatar = p.getString("avatar");
                        int yearExperience = p.getInt("expYear");
                        people.add(new Person(name, skype, -1, id, avatar, yearExperience));
                    }
                    people = Common.sortList(people); // sort
                    people = Common.addAlphabet(people);
                    personAdapter = new PersonAdapter(ListEngineerActivity.this, people);
                    recycler_person.setAdapter(personAdapter);

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

    // Because in PersonAdapter we calling startActivityForResult to start AlphatbetActivity
    // And in AlphabetActivity we have go back with value
    // So here we need override onActivityResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Common.RESULT_CODE){
            if (resultCode == Activity.RESULT_OK){
                String group_character_clicked = data.getStringExtra("result");
                int position = Common.findPositionWithName(group_character_clicked, people);
                recycler_person.smoothScrollToPosition(position);
            } else {
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListEngineerActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ListEngineerActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
                startActivity(intent);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}