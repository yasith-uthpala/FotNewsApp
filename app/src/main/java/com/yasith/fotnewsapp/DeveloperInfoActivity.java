package com.yasith.fotnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        Button exitButton = findViewById(R.id.exitButton);
        ImageView backButton = findViewById(R.id.backButton);

        exitButton.setOnClickListener(v -> {
            finishAffinity();
        });

        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(DeveloperInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
