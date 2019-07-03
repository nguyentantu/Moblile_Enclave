package com.example.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    Button btnSignup, signin1;
    EditText edtUsername, edtPassword, edtConfimPass;
    String UserName, Password, ConfirmPass;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        addControls();

    }

    private void addControls() {
        edtUsername = findViewById(R.id.edt_userName);
        edtPassword = findViewById(R.id.edt_password);
        edtConfimPass = findViewById(R.id.edt_confirmPass);

        mProgress = new ProgressDialog(SignUpActivity.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(true);
        mProgress.setIndeterminate(true);

        btnSignup = findViewById(R.id.btn_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkData()) {
                    Toast.makeText(SignUpActivity.this, "UserName and password are required!", Toast.LENGTH_SHORT).show();
                } else {

                    int lengthUser = edtUsername.getText().length();
                    int lengthPass = edtPassword.getText().length();
                    if (lengthUser < 3 || lengthPass < 3) {
                        Toast.makeText(SignUpActivity.this, "UserName and password must be longer than 3 characters!", Toast.LENGTH_SHORT).show();
                    } else {
                        UserName = edtUsername.getText().toString();
                        Password = edtPassword.getText().toString();
                        ConfirmPass = edtConfimPass.getText().toString();

                        try {

                            if (!Password.equals(ConfirmPass)) {
                                Toast.makeText(SignUpActivity.this, "Password Not matching", Toast.LENGTH_SHORT).show();
                            } else {
                                mProgress.show();
                                new SignUpActivity.postJSON().execute();
                            }


                        } catch (Exception e) {
                            Log.i("myApp", "Error in on create........................." + e.toString());
                        }
                    }
                }
            }
        });
    }

    private boolean checkData() {
        if(edtUsername.getText().length() == 0 || edtPassword.getText().length() == 0){
            return false;
        } else {
            return true;
        }
    }


    public void backLogin(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public class postJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "OK"){
                Toast.makeText(SignUpActivity.this,"Sing up successful!",Toast.LENGTH_SHORT).show();
                Log.i("myAppTag", "(onPostExecute method) Result = Posted");
            } else if(s == "exist"){
                Toast.makeText(SignUpActivity.this, "username exists!", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("myAppTag", "(onPostExecute method) Result = Failed to post");
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url = new URL("https://cool-demo-api.herokuapp.com/api/v1/auth/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", UserName);
                jsonParam.put("password", Password);

                Log.i("myAppTag", "g=============="+jsonParam);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                int status = conn.getResponseCode();
                Log.i("myAppTag", "==================="+status);

                if (status == 200){
                    mProgress.dismiss();
                    //Toast.makeText(SignUpActivity.this,"Successful!",Toast.LENGTH_SHORT).show();
                    return "OK";
                }else if(status == 409){
                    mProgress.dismiss();
                    return "exist";
                }

                else {
                    //Toast.makeText(SignUpActivity.this, "nooooooooooooooooooo",Toast.LENGTH_SHORT).show();
                    return null;
                }

            }catch (Exception ex){
                Log.i("myAppTag","Some error............................."+ex.toString());
            }
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}