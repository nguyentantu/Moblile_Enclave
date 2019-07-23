package com.example.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResetPassword2Activity extends AppCompatActivity {
    Button btnResetPassword, btnBack, btnConfirm;
    EditText edtEmail, edtPassword, edtConfirmPassword, edtCode;
    ProgressBar progressBar;
    String Email, password, code;
    TextView txtInform;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password2);

        addControls();
    }

    private void addControls() {
        progressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirmPass);
        btnBack = findViewById(R.id.btn_back2);
        btnConfirm = findViewById(R.id.btn_confirm);
        txtInform = findViewById(R.id.txt_inform);
        edtCode = findViewById(R.id.edt_code);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        Intent intent = getIntent();
        Email = intent.getStringExtra("email");
        id = intent.getIntExtra("id",0);

        edtEmail.setText(Email);
        edtEmail.setEnabled(false);
        edtCode.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
        txtInform.setVisibility(View.GONE);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(Email)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplication(), "Enter your new password", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (!edtPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
                        Toast.makeText(ResetPassword2Activity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            new postJSON().execute();
                            txtInform.setVisibility(View.VISIBLE);
                            edtCode.setVisibility(View.VISIBLE);
                            btnResetPassword.setVisibility(View.GONE);
                            btnConfirm.setVisibility(View.VISIBLE);
                        } catch (Exception e){
                        }
                    }
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = edtCode.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                if (edtCode.getText().toString().equals(null)){
                    Toast.makeText(ResetPassword2Activity.this, "Please enter your code", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        new postJSON2().execute();
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    public class postJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/auth/forget/sendcode");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", Email);
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                //JSONObject jsonObject = new JSONObject(builder.toString());
                //id = jsonObject.getInt("id");

                br.close();
                os.flush();
                os.close();

                final int status = conn.getResponseCode();
                Log.e("loggggggggggg", status+"");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (status){
                            case 200:
                                progressBar.setVisibility(View.GONE);
//                        Intent intent = new Intent(ResetPassword2Activity.this, ResetPasswordActivity.class);
//                        intent.putExtra("email", Email);
//                        startActivity(intent);
                                break;
                        }
                    }
                });
            }catch (Exception ex){
                Log.i("myAppTag","Some error............................."+ex.toString());
            }
            return null;
        }
    }

    public class postJSON2 extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/auth/forget/resetPassword/"+id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("verify", code);
                jsonParam.put("password", password);
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                //JSONObject jsonObject = new JSONObject(builder.toString());
                //id = jsonObject.getInt("id");

                br.close();
                os.flush();
                os.close();

                final int status = conn.getResponseCode();
                Log.e("loggggggggggg", status+"");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (status){
                            case 200:
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ResetPassword2Activity.this, "Change password successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPassword2Activity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                    }
                });

            }catch (Exception ex){
                Log.i("myAppTag","Some error............................."+ex.toString());
            }
            return null;
        }
    }
}
