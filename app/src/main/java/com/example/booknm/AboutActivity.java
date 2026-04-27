package com.example.booknm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About BookNM");
        }

        TextView tvVersion = findViewById(R.id.tvAboutVersion);
        tvVersion.setText("Version 1.0.0-MVP");

        Button btnBack = findViewById(R.id.btnAboutBack);
        btnBack.setOnClickListener(v -> finish());
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
