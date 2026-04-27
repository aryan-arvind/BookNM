package com.example.booknm;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// FIX: Corrected the import statements to the project's actual package
import com.example.booknm.Booking;
import com.example.booknm.Venue;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView lvMyBookings;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        db = new DatabaseHelper(this);
        lvMyBookings = findViewById(R.id.lvMyBookings);
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadBookings();
    }

    private void loadBookings() {
        List<Booking> bookings = db.getBookingsByUser(userId);
        List<String> rows = new ArrayList<>();
        for (Booking b : bookings) {
            // These lines now work because the imports are correct
            Venue v = db.getVenueById(b.getVenueId());
            if (v != null) { // Good practice to check if venue exists
                String row = v.getName() + " | " + b.getDate() + " | " + b.getStartTime() + "-" + b.getEndTime();
                rows.add(row);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        lvMyBookings.setAdapter(adapter);
    }
}
