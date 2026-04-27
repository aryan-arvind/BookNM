package com.example.booknm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SmartSuggestionActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RadioGroup rgCapacity;
    private CheckBox cbPcs, cbSmartboard;
    private Button btnGetSuggestion;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_suggestion);
        setTitle("Venue Suggester");

        db = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("userId", -1);

        rgCapacity = findViewById(R.id.rgCapacity);
        cbPcs = findViewById(R.id.cb_pcs);
        cbSmartboard = findViewById(R.id.cb_smartboard);
        btnGetSuggestion = findViewById(R.id.btnGetSuggestion);

        btnGetSuggestion.setOnClickListener(v -> findAndShowSuggestion());
    }

    private void findAndShowSuggestion() {
        int checkedCapacityId = rgCapacity.getCheckedRadioButtonId();
        if (checkedCapacityId == -1) {
            Toast.makeText(this, "Please select the number of attendees.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean needsPcs = cbPcs.isChecked();
        boolean needsSmartboard = cbSmartboard.isChecked();
        RadioButton selectedCapacity = findViewById(checkedCapacityId);
        String capacityChoice = selectedCapacity.getText().toString();

        // Simple 'if' statements to implement the suggestion logic
        String suggestionName;
        if (needsSmartboard && capacityChoice.equals("Over 70")) {
            suggestionName = "Auditorium";
        } else if (needsPcs && capacityChoice.equals("41 to 70")) {
            suggestionName = "Lab L209 (Premium)"; // Suggests the premium lab
        } else if (needsPcs) {
            suggestionName = "Lab L103"; // Suggests a standard i5 lab
        } else if (!needsPcs && !needsSmartboard) {
            suggestionName = "Seminar Room";
        } else {
            suggestionName = "Auditorium"; // Default suggestion for large groups or special equipment
        }

        // Get the suggested venue from the database to pass its ID
        Venue suggestedVenue = db.getVenueByName(suggestionName);
        if (suggestedVenue == null) {
            Toast.makeText(this, "Could not find a suitable venue.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the suggestion in a Pop-up Dialog Box
        new AlertDialog.Builder(this)
                .setTitle("Smart Suggestion")
                .setMessage("Based on your needs, we recommend the:\n\n" + suggestedVenue.getName() + "\n\nDescription: " + suggestedVenue.getDescription())
                .setPositiveButton("Book This Venue", (dialog, which) -> {
                    // Proceed to booking this specific venue
                    Intent intent = new Intent(SmartSuggestionActivity.this, BookingActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("venueId", suggestedVenue.getId());
                    startActivity(intent);
                    finish(); // Close the suggestion activity
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
