package com.example.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.navigation.fragment.DoneFragment;
import com.example.navigation.fragment.InprogressFragment;
import com.example.navigation.fragment.PendingFragment;

public class ProjectActivity extends AppCompatActivity {

    ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        toolbar.setTitle("PROJECT INPROGRESS");
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2ecc71")));
        loadFragment(new InprogressFragment());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("PROJECT INPROGRESS");
                    toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2ecc71")));
                    toolbar.setDisplayHomeAsUpEnabled(true);
                    fragment = new InprogressFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle("PROJECT DONE");
                    toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e74c3c")));
                    toolbar.setDisplayHomeAsUpEnabled(true);
                    fragment = new DoneFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    toolbar.setTitle("PROJECT PENDING");
                    toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f1c40f")));
                    toolbar.setDisplayHomeAsUpEnabled(true);
                    fragment = new PendingFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProjectActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // dont reload
        startActivity(intent);
        super.onBackPressed();
    }
}
