package com.example.navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import model.Engineers;

import static android.Manifest.permission.CALL_PHONE;

public class DetailEnActivity extends AppCompatActivity {

    CircleImageView imgProDetail ;
    AlertDialog mProgress;
    TextView txtUserName, txtEngName, txtEmail, txtAddress, txtDayOff, txtExperienceYears, txtSkype, txtPhone, txtSkill;
    Button btnBack;
    String englishName, email, address, firstName, lastName, Skype, phone, avatar, nameSkill;
    int salary, experienceYears;
    int id;
    ArrayList<String> listSkill = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        //getSupportActionBar().hide();

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
        btnBack = findViewById(R.id.btn_back);
        imgProDetail = findViewById(R.id.img_proDetail);
        txtSkill = findViewById(R.id.txt_skill);
        mProgress = new SpotsDialog(this, R.style.Custom);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Engineer Detail");

//        TextView txtNameApp = findViewById(R.id.txt_nameApp);
//        txtNameApp.setText("Engineer Detail");

//        btnBack = findViewById(R.id.btn_back);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DetailEnActivity.this, ListEngineerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//            }
//        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        avatar = intent.getStringExtra("avatar");
        Picasso.with(getApplicationContext()).load(avatar)//download URL
                .into(imgProDetail);

            mProgress.show();
            if (!txtUserName.getText().toString().equals(null)){
                mProgress.dismiss();
            }

        DetailEnActivity.DanhSachSanPhamTask task = new DetailEnActivity.DanhSachSanPhamTask();
        task.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Call(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(DetailEnActivity.this); //Home is name of the activity
        builder.setMessage("Do you want to call " + txtUserName.getText().toString()+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" +txtPhone.getText().toString() ));
                if (ContextCompat.checkSelfPermission(DetailEnActivity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    public void SendEmail(View view) {

        AlertDialog.Builder builder=new AlertDialog.Builder(DetailEnActivity.this); //Home is name of the activity
        builder.setMessage("Do you want to send an Email to " + txtUserName.getText().toString()+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{txtEmail.getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Informing email from Enclave");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailEnActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<Engineers>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Engineers> sanPhams) {

            String text="";
            for (String details : listSkill) {
                text = text + details + ", ";
            }
            txtSkill.setText(text);
            txtUserName.setText(lastName+" "+firstName);
            txtEmail.setText(email);
            txtAddress.setText(address);
            txtDayOff.setText(salary/1000000+"M VNĐ");
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DetailEnActivity.this, ListEngineerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
                startActivity(intent);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
