package com.example.booknm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import android.widget.ImageButton;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvWelcome;
    private Button btnLogout;
    private MaterialCardView cardMyBookings;
    private MaterialCardView cardSmartSuggestion;
    private ImageButton btnAbout;
    private SearchView searchView;
    private VenueAdapter adapter;
    private ListView lvVenues;
    private int userId;
    private User user;
    private List<Venue> venues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        db = new DatabaseHelper(this);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogoutHome);
        btnAbout = findViewById(R.id.btnAbout);
        cardMyBookings = findViewById(R.id.cardMyBookings);
        cardSmartSuggestion = findViewById(R.id.cardSmartSuggestion);
        searchView = findViewById(R.id.searchViewVenues);
        lvVenues = findViewById(R.id.lvVenues);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        user = db.getUserById(userId);
        if (user != null) {
            tvWelcome.setText(getString(R.string.welcome_user, user.getName()));
        } else {
            Toast.makeText(this, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadVenues();

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(UserHomeActivity.this, LandingActivity.class); // Go to Landing, not Login
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        btnAbout.setOnClickListener(v -> {
            Intent i = new Intent(UserHomeActivity.this, AboutActivity.class);
            startActivity(i);
        });

        // Updated listener for the card
        cardMyBookings.setOnClickListener(v -> {
            Intent i = new Intent(UserHomeActivity.this, MyBookingsActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

        cardSmartSuggestion.setOnClickListener(v -> {
            Intent i = new Intent(UserHomeActivity.this, SmartSuggestionActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        lvVenues.setOnItemClickListener((parent, view, position, id) -> {
            Venue v = venues.get(position);
            Intent i = new Intent(UserHomeActivity.this, BookingActivity.class);
            i.putExtra("userId", userId);
            i.putExtra("venueId", v.getId());
            startActivity(i);
        });
    }

    private void loadVenues() {
        venues = db.getAllVenues();
        adapter = new VenueAdapter(this, venues);
        lvVenues.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list of venues in case any were added by an admin
        loadVenues();
    }
}
