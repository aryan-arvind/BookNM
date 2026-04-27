package com.example.booknm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AdminBookingDetailsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvVenueName, tvUserName, tvDateTime, tvPurpose, tvFinalStatus;
    private Button btnApprove, btnDeny;
    private LinearLayout actionButtonsLayout;
    private int bookingId;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_details);

        db = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();
        bookingId = getIntent().getIntExtra("booking_id", -1);

        if (bookingId == -1) {
            Toast.makeText(this, "Error: Booking ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize all views
        tvVenueName = findViewById(R.id.tvDetailVenueName);
        tvUserName = findViewById(R.id.tvDetailUserName);
        tvDateTime = findViewById(R.id.tvDetailDateTime);
        tvPurpose = findViewById(R.id.tvDetailPurpose);
        tvFinalStatus = findViewById(R.id.tvFinalStatus);
        actionButtonsLayout = findViewById(R.id.layoutActionButtons);
        btnApprove = findViewById(R.id.btnApprove);
        btnDeny = findViewById(R.id.btnDeny);

        loadBookingDetails();

        btnApprove.setOnClickListener(v -> updateBookingStatus("Approved"));
        btnDeny.setOnClickListener(v -> updateBookingStatus("Denied"));
    }

    private void loadBookingDetails() {
        Booking booking = db.getBookingById(bookingId); // We will create this method in Step 3
        if (booking == null) {
            Toast.makeText(this, "Error: Could not load booking details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        User user = db.getUserById(booking.getUserId());
        Venue venue = db.getVenueById(booking.getVenueId());

        String userName = (user != null) ? user.getName() + " (" + user.getEmail() + ")" : "Unknown User";
        String venueName = (venue != null) ? venue.getName() : "Unknown Venue";
        String dateTime = booking.getDate() + ", " + booking.getStartTime() + " - " + booking.getEndTime();

        tvVenueName.setText(venueName);
        tvUserName.setText(userName);
        tvDateTime.setText(dateTime);
        tvPurpose.setText(booking.getPurpose());

        // Show/hide buttons based on status
        if (booking.getStatus().equals("Pending")) {
            actionButtonsLayout.setVisibility(View.VISIBLE);
            tvFinalStatus.setVisibility(View.GONE);
        } else {
            actionButtonsLayout.setVisibility(View.GONE);
            tvFinalStatus.setVisibility(View.VISIBLE);
            tvFinalStatus.setText("Request has been " + booking.getStatus());
            if (booking.getStatus().equals("Approved")) {
                tvFinalStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            } else {
                tvFinalStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            }
        }
    }

    private void updateBookingStatus(String newStatus) {
        boolean success = db.updateBookingStatus(bookingId, newStatus);
        if (success) {
            // Cloud Sync Status Update
            firebaseHelper.updateBookingStatusInCloud(bookingId, newStatus);
            
            Toast.makeText(this, "Booking status updated to " + newStatus, Toast.LENGTH_SHORT).show();
            loadBookingDetails();
        } else {
            Toast.makeText(this, "Failed to update status.", Toast.LENGTH_SHORT).show();
        }
    }
}
