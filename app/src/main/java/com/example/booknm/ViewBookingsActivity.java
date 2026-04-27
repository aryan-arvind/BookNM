package com.example.booknm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ViewBookingsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView lvAllBookings;
    private List<Booking> bookingsList;
    private BookingAdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);
        setTitle("All Booking Requests");

        db = new DatabaseHelper(this);
        lvAllBookings = findViewById(R.id.lvAllBookings);

        // Fixes the "at very much top" issue by adding space around the list
        lvAllBookings.setClipToPadding(false);
        lvAllBookings.setPadding(16, 16, 16, 16);

        // Handle item clicks to open the new details screen
        lvAllBookings.setOnItemClickListener((parent, view, position, id) -> {
            Booking clickedBooking = bookingsList.get(position);
            Intent intent = new Intent(ViewBookingsActivity.this, AdminBookingDetailsActivity.class);
            intent.putExtra("booking_id", clickedBooking.getId());
            startActivity(intent);
        });
    }

    private void loadBookings() {
        bookingsList = db.getAllBookings();
        if (bookingsList.isEmpty()) {
            Toast.makeText(this, "No bookings found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use our new custom adapter
        adapter = new BookingAdminAdapter(this, bookingsList);
        lvAllBookings.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This reloads the list every time you return to this screen,
        // so you see the status changes immediately after approving/denying.
        loadBookings();
    }
}
