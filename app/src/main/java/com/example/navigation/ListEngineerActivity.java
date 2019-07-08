package com.example.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import adapter.MainAdapter;
import model.Engineers;

public class ListEngineerActivity extends AppCompatActivity {

    ProgressDialog mProgress;
    ListView lvMain;
    ArrayAdapter<Engineers> mainAdapter;
    TextView txtName;
    Button btnBack;

    int id, totalEn = 0;
    String name;
    String username;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_engineer);

        addControls();

    }

    private void addControls() {


        lvMain = (ListView) findViewById(R.id.lv_engineer);
        mainAdapter = new MainAdapter(ListEngineerActivity.this,R.layout.itemsmain);
        lvMain.setAdapter(mainAdapter);

        mProgress = new ProgressDialog(ListEngineerActivity.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        mProgress.show();

        txtName = findViewById(R.id.txt_nameApp);
        txtName.setText("Engineers List");
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListEngineerActivity.this, MainActivity.class);
//                intent.putExtra("total", total);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        DanhSachSanPhamTask task = new DanhSachSanPhamTask();
        task.execute();

    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> engineers) {
            super.onPostExecute(engineers);
            mainAdapter.clear();
            mainAdapter.addAll(engineers);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Engineers> doInBackground(Void... voids) {
            ArrayList<Engineers>dsEngineer = new ArrayList<>();
            try {
                URL url = new URL("https://cool-demo-api.herokuapp.com/api/v1/engineers");// link API
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line=br.readLine()) != null){
                    builder.append(line);
                }
                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONArray result = jsonObject.getJSONArray("results");

                for (int  i=0;i<result.length();i++){

                    JSONObject p = (JSONObject)result.get(i);
                    id = p.getInt("id");
                    name = p.getString("firstName");
                    username = p.getString("lastName");
                    email = p.getString("email");
                    Engineers engineers = new Engineers();
                    engineers.setId(id);
                    engineers.setName(name);
                    engineers.setUsername(username);
                    engineers.setEmail(email);
                    dsEngineer.add(engineers);

                }

                br.close();

                int status = connection.getResponseCode();
                if (status == 200 ){
                    mProgress.dismiss();
                }

            } catch (Exception ex){
                Log.e("LOI", ex.toString());
            }
            return dsEngineer;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ListEngineerActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.onBackPressed();
    }
}