package com.example.booknm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvVenueName;
    private TextInputEditText etDate, etPurpose;
    private Spinner spinnerTime;
    private CheckBox cbTerms;
    private Button btnBook, btnBack;
    private int userId, venueId;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = new DatabaseHelper(this);
        firebaseHelper = new FirebaseHelper();
        tvVenueName = findViewById(R.id.tvBookingVenue);
        etDate = findViewById(R.id.etDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        etPurpose = findViewById(R.id.etPurpose);
        cbTerms = findViewById(R.id.cbTerms);
        btnBook = findViewById(R.id.btnConfirmBooking);
        btnBack = findViewById(R.id.btnBackBooking);

        userId = getIntent().getIntExtra("userId", -1);
        venueId = getIntent().getIntExtra("venueId", -1);

        if (userId == -1 || venueId == -1) {
            Toast.makeText(this, "Error: Missing booking data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Venue venue = db.getVenueById(venueId);
        if (venue != null) {
            tvVenueName.setText(venue.getName());
        }

        setupDatePicker();
        setupTimeSpinner();
        setupTermsCheckBox();

        btnBack.setOnClickListener(v -> finish());
        btnBook.setOnClickListener(v -> performBooking());
    }

    private void setupDatePicker() {
        // Syllabus Item: Date Picker
        etDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                        etDate.setText(selectedDate);
                    }, year, month, day);

            // Prevent selecting past dates
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    private void setupTimeSpinner() {
        // Syllabus Item: Spinner
        String[] timeSlots = {"09:00 - 11:00", "11:00 - 13:00", "14:00 - 16:00", "16:00 - 18:00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeSlots);
        spinnerTime.setAdapter(adapter);
    }

    private void setupTermsCheckBox() {
        // Syllabus Item: Check Boxes
        cbTerms.setOnCheckedChangeListener((buttonView, isChecked) -> btnBook.setEnabled(isChecked));
    }

    private void performBooking() {
        String date = etDate.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();
        String selectedSlot = spinnerTime.getSelectedItem().toString();
        String[] times = selectedSlot.split(" - ");
        String startTime = times[0];
        String endTime = times[1];

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(purpose)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Booking booking = new Booking(userId, venueId, date, startTime, endTime, purpose);
        long bookingId = db.addBooking(booking);

        if (bookingId == -1) {
            Toast.makeText(this, "Booking conflict! This slot is already taken.", Toast.LENGTH_LONG).show();
        } else {
            // Cloud Sync
            User user = db.getUserById(userId);
            Venue venue = db.getVenueById(venueId);
            booking.setId((int) bookingId);
            firebaseHelper.syncBookingToCloud(booking, user.getName(), venue.getName());

            Toast.makeText(this, "Booking Successful (Cloud Synced)!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BookingActivity.this, BookingSlipActivity.class);
            intent.putExtra("bookingId", (int) bookingId);
            startActivity(intent);
            finish();
        }
    }
}
