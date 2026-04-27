package com.example.booknm;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// FIX: Changed the import to the correct package for the Venue class
import com.example.booknm.Venue;

public class AddVenueActivity extends AppCompatActivity {
    private EditText etName, etCapacity, etLocation, etDesc;
    private Button btnAdd;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etVenueName);
        etCapacity = findViewById(R.id.etVenueCapacity);
        etLocation = findViewById(R.id.etVenueLocation);
        etDesc = findViewById(R.id.etVenueDesc);
        btnAdd = findViewById(R.id.btnAddVenue);

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String capS = etCapacity.getText().toString().trim();
            String loc = etLocation.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(capS) || TextUtils.isEmpty(loc)) {
                Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }
            int cap;
            try {
                cap = Integer.parseInt(capS);
            } catch (NumberFormatException ex) {
                etCapacity.setError("Enter number");
                return;
            }

            // This line now works because the import is correct
            Venue vobj = new Venue(name, cap, loc, desc);
            long id = db.addVenue(vobj);
            if (id != -1) {
                Toast.makeText(this, "Venue added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
