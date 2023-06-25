    package com.app.flipcardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.flipcardapp.fragment.AddCardFragment;
import com.app.flipcardapp.fragment.CardFragment;
import com.app.flipcardapp.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class MainActivity extends AppCompatActivity {


        private BottomNavigationView bottomNav;
        Fragment selectedFragment = null;

        private String selectType,backText;

        private boolean flag;
        boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottomNav);


        Bundle extras = getIntent().getExtras();
        if(extras!=null && extras.containsKey("flag"))
            flag= extras.getBoolean("flag");
        if(flag){
            selectedFragment = new CardFragment();
        }



        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new CardFragment()).commit();
    }

        private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){
                    case R.id.card:
                        selectedFragment = new CardFragment();
                        break;
                    case R.id.add:
                        selectedFragment = new AddCardFragment();
                        break;
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,selectedFragment).commit();
                return true;
            }
        };




        @Override
        public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back button again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }