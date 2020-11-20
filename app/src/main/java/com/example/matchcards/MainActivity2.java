package com.example.matchcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    TextView yourTimeView, bestTimeView;
    Button play, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // shared preference
        SharedPreferences store = getSharedPreferences("bestTime", MODE_PRIVATE);
        int bestTime = store.getInt("bestTime", 999999);

        // intent
        Intent intent = getIntent();
        int currentTime = intent.getIntExtra("time", 999);

        if(currentTime < bestTime) {
            SharedPreferences.Editor storeEditor = store.edit();
            storeEditor.putInt("bestTime", currentTime);
            storeEditor.apply();
            bestTime = currentTime;
        }

        yourTimeView = findViewById(R.id.yourtime);
        bestTimeView = findViewById(R.id.besttime);
        play = findViewById(R.id.play);
        exit = findViewById(R.id.exit);

        yourTimeView.setText(currentTime + "");
        bestTimeView.setText(bestTime + "");

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });
    }
}