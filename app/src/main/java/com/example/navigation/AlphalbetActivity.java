package com.example.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import Interface.IOnAlphabetClickListenner;
import adapter.AlphabetAdapter;

public class AlphalbetActivity extends AppCompatActivity {

    RecyclerView recycler_alphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphalbet);
        getSupportActionBar().hide();

        recycler_alphabet = findViewById(R.id.recycler_alphabet);
        AlphabetAdapter alphabetAdapter = new AlphabetAdapter();
        alphabetAdapter.setiOnAlphabetClickListenner(new IOnAlphabetClickListenner() {
            @Override
            public void onAlphabetClickListenner(String alphabet, int postition) {
                if (postition != -1){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", alphabet);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {

                }
            }
        });
        recycler_alphabet.setLayoutManager(new GridLayoutManager(this, 4));
        recycler_alphabet.setAdapter(alphabetAdapter);
    }
}
