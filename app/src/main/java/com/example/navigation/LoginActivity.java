package com.example.navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    TextView btnLogin;
    AlertDialog mProgress;
    EditText edtUsername, edtPassword;
    String UserName, Password;
    int id;
    String token, tokenRead;


    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences; // save status
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //checkToken();
        addControls();
    }

//    private void checkToken() {
//        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
//        tokenRead = preferences.getString("token", "");
//        if (!tokenRead.equals("")){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
//    }

    private void addControls() {
        edtUsername = findViewById(R.id.edt_userName);
        edtPassword = findViewById(R.id.edt_password);

        mProgress = new SpotsDialog(this, R.style.Custom);
//        mProgress.setTitle("Processing...");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCancelable(true);
//        mProgress.setIndeterminate(true);

        saveLoginCheckBox = findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edtUsername.setText(loginPreferences.getString("username", ""));
            edtPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        btnLogin = findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkData()){
                    Toast.makeText(LoginActivity.this, "UserName and password are required!", Toast.LENGTH_SHORT).show();
                } else {

                    int lengthUser = edtUsername.getText().length();
                    int lengthPass = edtPassword.getText().length();

                    if (lengthUser < 3 || lengthPass <3 ){
                        Toast.makeText(LoginActivity.this, "UserName and password must be longer than 3 characters!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mProgress.show();

                        UserName = edtUsername.getText().toString();
                        Password = edtPassword.getText().toString();

                        try {

                            new postJSON().execute();

                        }catch (Exception e){
                            Log.i("myApp", "Error in on create........................."+e.toString());
                        }

                        if (view == btnLogin) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), 0);

//                    String username = edtUsername.getText().toString();
//                    String password = edtPassword.getText().toString();

                            if (saveLoginCheckBox.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", UserName);
                                loginPrefsEditor.putString("password", Password);
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }
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


    public class postJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            writeData();
            readData();

            if (s == "false"){
                Toast.makeText(LoginActivity.this, "UserName or password incorrect!", Toast.LENGTH_SHORT).show();
            }

            else {
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url = new URL("http://si-enclave.herokuapp.com/api/v1/auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", UserName);
                jsonParam.put("password", Password);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());

                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject jsonArray = new JSONObject(builder.toString());
                id = jsonArray.getInt("id");
                token = jsonArray.getString("token");

                br.close();

                os.flush();
                os.close();


                int status = conn.getResponseCode();
                Log.e("loggggggggggg", status+"");

                if (status == 200){
                    mProgress.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else if(status == 404){
                    mProgress.dismiss();
                    return "existed";
                }
                else{
                    mProgress.dismiss();
                    return "false";
                }

            }catch (Exception ex){
                mProgress.dismiss();
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(LoginActivity.this, "Username or password incorrect!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this); //Home is name of the activity
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();

        super.onBackPressed();
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void writeData()
    {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }



    public void readData()
    {
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        tokenRead = preferences.getString("token", "");
        Log.e("tokenshare", tokenRead);
    }

}