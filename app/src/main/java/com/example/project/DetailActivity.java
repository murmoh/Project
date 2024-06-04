package com.example.project;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView textViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        textViewDetails = findViewById(R.id.textViewDetails);
        String details = getIntent().getStringExtra("details");
        textViewDetails.setText(details != null ? details : "No details available.");
    }
}
