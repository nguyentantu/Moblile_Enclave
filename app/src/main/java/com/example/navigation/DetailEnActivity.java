package com.example.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Engineers;

public class DetailEnActivity extends AppCompatActivity {

    CircleImageView imgProDetail ;

    TextView txtUserName, txtEngName, txtEmail, txtAddress, txtDayOff, txtExperienceYears, txtSkype, txtPhone, txtSkill;
    TextView txtName;
    Button btnBack;
    String englishName, email, address, firstName, lastName, Skype, phone, avatar, nameSkill;
    int salary, experienceYears;
    int id;
    ArrayList<String> listSkill = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        addControls();
    }

    private void addControls() {

        txtUserName = findViewById(R.id.txt_userName);
//        txtEngName = findViewById(R.id.txt_engName);
        txtEmail = findViewById(R.id.txt_email);
        txtAddress = findViewById(R.id.txt_address);
        txtDayOff = findViewById(R.id.txt_dayOff);
        txtExperienceYears = findViewById(R.id.txt_yearExperience);
        txtSkype = findViewById(R.id.txt_skype);
        txtPhone = findViewById(R.id.txt_phone);
        txtName = findViewById(R.id.txt_nameApp);
        btnBack = findViewById(R.id.btn_back);
        imgProDetail = findViewById(R.id.img_proDetail);
        txtSkill = findViewById(R.id.txt_skill);

//        txtName.setText("Details");
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DetailEnActivity.this, ListEngineerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
//                startActivity(intent);
//            }
//        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        avatar = intent.getStringExtra("avatar");
        Picasso.with(getApplicationContext()).load(avatar)//download URL
                .into(imgProDetail);

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
            Log.e("========", listSkill+"");

            String text="";
            for (String details : listSkill) {
                text = text + details + ", ";
            }
            txtSkill.setText(text);



            txtUserName.setText(lastName+" "+firstName);
//            txtEngName.setText(englishName);
            txtEmail.setText(email);
            txtAddress.setText(address);
            txtDayOff.setText(salary/1000000+"M");
            txtExperienceYears.setText(experienceYears+" Years");
            txtSkype.setText(Skype);
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
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/engineers/"+id);// link API
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
                salary = jsonArray.getInt("salary");
                experienceYears = jsonArray.getInt("expYear");
                Skype = jsonArray.getString("skype");
                phone = jsonArray.getString("phoneNumber");

                JSONArray array = jsonArray.getJSONArray("skills");
                for(int i=0;i<array.length();i++){
                    JSONObject json_obj = array.getJSONObject(i);
                    nameSkill = json_obj.getString("name");
                    listSkill.add(nameSkill);
                }

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
