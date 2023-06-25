package com.app.flipcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.flipcardapp.db.CardDataBaseHelper;
import com.app.flipcardapp.model.Card;
import com.google.gson.Gson;

public class ConfirmDeleteActivity extends AppCompatActivity {
    private Button noButton,yesButton;
    private Card c;
    private CardDataBaseHelper cardDataBaseHelper;
    private SharedPreferences preferences;
    public static final String mypreferences = "mypref";
    public static final String userName = "nameKey";
    public static final String userDOB = "dobKey";
    public static final String userMobile = "mobileKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        preferences =   getSharedPreferences(mypreferences,
                Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString("c", "");
        c = gson.fromJson(json, Card.class);

        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        cardDataBaseHelper = new CardDataBaseHelper(getApplicationContext());


        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmDeleteActivity.this,MainActivity.class);
                intent .putExtra("tag",true);
                finish();
                startActivity(intent);
            }
        });


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardDataBaseHelper.deleteCard(c);
                Intent intent = new Intent(ConfirmDeleteActivity.this,MainActivity.class);
                intent .putExtra("tag",true);
                finish();
                startActivity(intent);
            }
        });

    }
}