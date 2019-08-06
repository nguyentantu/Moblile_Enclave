package com.example.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnResetPassword, btnBack;
    EditText edtEmail;
    ProgressBar progressBar;
    String Email;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        addControls();
    }

    private void addControls() {
        progressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edt_email);
        btnBack = findViewById(R.id.btn_back);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
               Email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(Email)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplication(), "Enter your registered Email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!isValidEmail(Email)){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ResetPasswordActivity.this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            new postJSON().execute();
                        } catch (Exception e){
                        }
                    }
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public class postJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/auth/forget");
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
                JSONObject jsonObject = new JSONObject(builder.toString());
                id = jsonObject.getInt("id");

                br.close();
                os.flush();
                os.close();

                final int status = conn.getResponseCode();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (status){
                            case 200:
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(ResetPasswordActivity.this, ResetPassword2Activity.class);
                                intent.putExtra("email", Email);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                break;
                            case 404:
                                   Toast.makeText(ResetPasswordActivity.this, "Something wrong!!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                   break;
                        }
                    }
                });
            }catch (Exception ex){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResetPasswordActivity.this, "Something wrong!!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            return null;
        }
    }
}
