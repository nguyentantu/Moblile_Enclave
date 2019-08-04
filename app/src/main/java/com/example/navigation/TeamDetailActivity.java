package com.example.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
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
import java.util.ArrayList;

import Common.Common;
import Common.LinearLayoutManagerWithSmoothScroller;
import adapter.EngineerInTeamAdapter;
import dmax.dialog.SpotsDialog;
import model.Engineers;
import model.Person;

public class TeamDetailActivity extends AppCompatActivity {

    String firstName, lastName, avatar, role, email, teamName, dateCreated, projectName;
    int id, salary, totalMember, experienceYear;
    TextView txtTeamName, txtNumberOfMember, txtDateCreated, txtProjectName;
    AlertDialog mProgress;
    RecyclerView recycler_person;
    LinearLayoutManager layoutManager;
    ArrayList<Person> people;
    Person person;
    EngineerInTeamAdapter personAdapter;
    RequestQueue requestQueue;
    int idPerson, idProject, idTeam;
    LinearLayout llProjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_person = findViewById(R.id.recycler_personTeam);
        recycler_person.setHasFixedSize(true);
        layoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        recycler_person.setLayoutManager(layoutManager);
        recycler_person.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        people = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        mProgress = new SpotsDialog(this, R.style.Custom);

        personAdapter = new EngineerInTeamAdapter(TeamDetailActivity.this, people);
        recycler_person.setAdapter(personAdapter);

        addControls();
    }


    private void addControls() {

        txtTeamName = findViewById(R.id.txt_teamName);
        txtNumberOfMember = findViewById(R.id.txt_numberOfMember);
        txtDateCreated = findViewById(R.id.txt_dayCreate);
        txtProjectName = findViewById(R.id.txt_nameProject);

        llProjectName = findViewById(R.id.ll_projectName);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        TeamDetailActivity.DanhSachSanPhamTask task = new TeamDetailActivity.DanhSachSanPhamTask();
        task.execute();
    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>> {
        @Override
        protected void onPreExecute() {
            mProgress.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> sanPhams) {
            mProgress.dismiss();
            getSupportActionBar().setTitle("Team Detail");

            txtTeamName.setText(teamName);
            txtNumberOfMember.setText(totalMember+" members");
            txtDateCreated.setText(dateCreated.substring(0,10));
            txtProjectName.setText(projectName);

            llProjectName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context2 = v.getContext();
                    Intent intent = new Intent(context2, DetailProjectActivity.class);
                    intent.putExtra("id", idProject);
                    intent.putExtra("idTeam", idTeam);
                    context2.startActivity(intent);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Engineers> doInBackground(Void... voids) {
            ArrayList<Engineers> dsEngineer = new ArrayList<>();
            try {
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/teams/"+ id);// link API
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
                teamName = jsonArray.getString("name");
                totalMember = jsonArray.getInt("totalMember");
                dateCreated = jsonArray.getString("createdAt");
                idTeam = jsonArray.getInt("id");
                JSONObject jsonObject = jsonArray.getJSONObject("projects");
                projectName = jsonObject.getString("name");
                idProject = jsonObject.getInt("id");
                JSONArray array = jsonArray.getJSONArray("engineers");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject p = (JSONObject) array.get(i);
                    idPerson = p.getInt("id");
                    firstName = p.getString("firstName");
                    lastName = p.getString("lastName");
                    avatar = p.getString("avatar");
                    email = p.getString("email");
                    role = p.getString("role");
                    salary = p.getInt("salary");
                    experienceYear = p.getInt("expYear");
                    person = new Person(idPerson,firstName, lastName, email, avatar, role, salary, experienceYear);
                    people.add(person);
                }
                TeamDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        personAdapter.updatePersonList(people);
                    }
                });
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
        Intent intent = new Intent(TeamDetailActivity.this, TeamActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}