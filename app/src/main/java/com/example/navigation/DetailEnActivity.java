package com.example.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import model.Engineers;

public class DetailEnActivity extends AppCompatActivity {

    TextView txtUserName, txtEngName, txtEmail, txtAddress, txtDayOff, txtExperienceYears, txtSkype, txtPhone;
    TextView txtName;
    Button btnBack, btnProfile;
    String englishName, email, address, firstName, lastName, skype, phone;
    int dayOff, experienceYears;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_en);

        addControls();
    }

    private void addControls() {

        txtUserName = findViewById(R.id.txt_userName);
        txtEngName = findViewById(R.id.txt_engName);
        txtEmail = findViewById(R.id.txt_email);
        txtAddress = findViewById(R.id.txt_address);
        txtDayOff = findViewById(R.id.txt_dayOff);
        txtExperienceYears = findViewById(R.id.txt_yearExperience);
        txtSkype = findViewById(R.id.txt_skype);
        txtPhone = findViewById(R.id.txt_phone);
        txtName = findViewById(R.id.txt_nameApp);
        btnBack = findViewById(R.id.btn_back);

        txtName.setText("Details");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailEnActivity.this, ListEngineerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);


        DetailEnActivity.DanhSachSanPhamTask task = new DetailEnActivity.DanhSachSanPhamTask();
        task.execute();

    }


    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> sanPhams) {
            super.onPostExecute(sanPhams);

            txtUserName.setText(lastName+" "+firstName);
            txtEngName.setText(englishName);
            txtEmail.setText(email);
            txtAddress.setText(address);
            txtDayOff.setText(dayOff+"\nDay Off");
            txtExperienceYears.setText(experienceYears+"\nYears");
            txtSkype.setText(skype);
            txtPhone.setText(phone);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Engineers> doInBackground(Void... voids) {
            ArrayList<Engineers> dsEngineer = new ArrayList<>();
            try {
                URL url = new URL("https://cool-demo-api.herokuapp.com/api/v1/engineers/"+id);// link API
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
                firstName = jsonArray.getString("firstName");
                lastName = jsonArray.getString("lastName");
                englishName = jsonArray.getString("englishName");
                email = jsonArray.getString("email");
                address = jsonArray.getString("address");
                dayOff = jsonArray.getInt("dayOffRemain");
                experienceYears = jsonArray.getInt("expYear");
                skype = jsonArray.getString("skype");
                phone = jsonArray.getString("phoneNumber");

                br.close();

            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return dsEngineer;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailEnActivity.this, ListEngineerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
