package com.example.booknm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = new DatabaseHelper(this);

        Button btnViewBookings = findViewById(R.id.btnViewBookings);
        Button btnAddVenue = findViewById(R.id.btnAddVenueAdmin);
        Button btnAddAdmin = findViewById(R.id.btnAddAdmin);

        btnViewBookings.setOnClickListener(v -> startActivity(new Intent(this, ViewBookingsActivity.class)));
        btnAddVenue.setOnClickListener(v -> startActivity(new Intent(this, AddVenueActivity.class)));
        btnAddAdmin.setOnClickListener(v -> startActivity(new Intent(this, AddAdminActivity.class)));
    }

    // Syllabus Item: Menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_export_bookings_csv) {
            exportBookingsToCSV();
            return true;
        } else if (itemId == R.id.menu_export_venues_txt) {
            exportVenuesToTXT();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Syllabus Item: File handling using .csv files
    private void exportBookingsToCSV() {
        List<Booking> bookings = db.getAllBookings();
        StringBuilder data = new StringBuilder();
        data.append("BookingID,UserID,VenueID,Date,StartTime,EndTime,Purpose\n");

        for (Booking b : bookings) {
            data.append(b.getId()).append(",").append(b.getUserId()).append(",").append(b.getVenueId())
                    .append(",").append(b.getDate()).append(",").append(b.getStartTime())
                    .append(",").append(b.getEndTime()).append(",").append(b.getPurpose()).append("\n");
        }
        saveToFile("BookingsExport.csv", data.toString());
    }

    // Syllabus Item: File handling using .txt files
    private void exportVenuesToTXT() {
        List<Venue> venues = db.getAllVenues();
        StringBuilder data = new StringBuilder();
        data.append("--- Venue List ---\n\n");

        for (Venue v : venues) {
            data.append("ID: ").append(v.getId()).append("\n");
            data.append("Name: ").append(v.getName()).append("\n");
            data.append("Capacity: ").append(v.getCapacity()).append("\n");
            data.append("Location: ").append(v.getLocation()).append("\n");
            data.append("--------------------\n");
        }
        saveToFile("VenuesExport.txt", data.toString());
    }

    private void saveToFile(String fileName, String content) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Successfully exported to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
    