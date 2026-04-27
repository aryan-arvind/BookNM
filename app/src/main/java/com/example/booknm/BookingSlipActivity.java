package com.example.booknm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// FIX: Corrected the import statements to the proper package
import com.example.booknm.User;
import com.example.booknm.Venue;

/*
 Brief: Fixed class name to match file. This activity:
  - reads "bookingId" from intent extras
  - queries the bookings table
  - fetches related user and venue via DatabaseHelper
  - displays booking slip details
  - provides Back to Home and Logout buttons
*/
public class BookingSlipActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvDetails;
    private Button btnHome, btnLogout;
    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_slip);

        db = new DatabaseHelper(this);
        tvDetails = findViewById(R.id.tvSlipDetails);
        btnHome = findViewById(R.id.btnSlipHome);
        btnLogout = findViewById(R.id.btnSlipLogout);

        bookingId = getIntent().getIntExtra("bookingId", -1);
        if (bookingId == -1) {
            Toast.makeText(this, "No booking specified", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Query booking record
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT user_id, venue_id, date, start_time, end_time, purpose FROM bookings WHERE id = ?",
                new String[]{String.valueOf(bookingId)}
        );

        if (c != null) {
            if (c.moveToFirst()) {
                int userId = c.getInt(c.getColumnIndexOrThrow("user_id"));
                int venueId = c.getInt(c.getColumnIndexOrThrow("venue_id"));
                String date = c.getString(c.getColumnIndexOrThrow("date"));
                String start = c.getString(c.getColumnIndexOrThrow("start_time"));
                String end = c.getString(c.getColumnIndexOrThrow("end_time"));
                String purpose = c.getString(c.getColumnIndexOrThrow("purpose"));

                // These lines now work because imports are correct
                User user = db.getUserById(userId);
                // FIX: Corrected typo from enueId to venueId
                Venue venue = db.getVenueById(venueId);

                StringBuilder details = new StringBuilder();
                details.append("Venue: ").append(venue != null ? venue.getName() : "N/A").append("\n");
                details.append("Location: ").append(venue != null ? venue.getLocation() : "N/A").append("\n\n");
                details.append("Date: ").append(date).append("\n");
                details.append("Time: ").append(start).append(" - ").append(end).append("\n\n");
                details.append("Purpose: ").append(purpose).append("\n\n");
                details.append("Booked by: ").append(user != null ? user.getName() + " (" + user.getEmail() + ")" : "N/A");

                tvDetails.setText(details.toString());

                // Back to Home sends userId so UserHomeActivity can show user context
                final int finalUserId = userId;
                btnHome.setOnClickListener(v -> {
                    Intent i = new Intent(BookingSlipActivity.this, UserHomeActivity.class);
                    i.putExtra("userId", finalUserId);
                    startActivity(i);
                    finish();
                });

                btnLogout.setOnClickListener(v -> {
                    Intent i = new Intent(BookingSlipActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finishAffinity();
                });

            } else {
                Toast.makeText(this, "Booking not found", Toast.LENGTH_SHORT).show();
                finish();
            }
            c.close();
        } else {
            Toast.makeText(this, "Error reading booking", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
