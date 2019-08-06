package com.example.navigation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckInternetActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);
        getSupportActionBar().hide();

        // At activity startup we manually check the internet status and change
        // the text status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            changeTextStatus(true);
        } else {
            changeTextStatus(false);
        }
    }

    // Method to change the text status
    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        if (isConnected) {
            Intent intent = new Intent(CheckInternetActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            final Dialog dialog1 = new Dialog(CheckInternetActivity.this, R.style.df_dialog);
            dialog1.setContentView(R.layout.dialog_no_internet);
            dialog1.setCancelable(true);
            dialog1.setCanceledOnTouchOutside(true);
            dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //progressBar.setVisibility(View.VISIBLE);
                    dialog1.findViewById(R.id.progressBar_checkinternet).setVisibility(View.VISIBLE);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        changeTextStatus(true);
                    } else {
                        Toast.makeText(CheckInternetActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                        changeTextStatus(false);
                    }
                }
            });
            dialog1.show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        MyApplication.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.activityResumed();// On Resume notify the Application
    }

}