package com.example.booknm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.card.MaterialCardView;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        MaterialCardView adminCard = findViewById(R.id.cardAdmin);
        MaterialCardView userCard = findViewById(R.id.cardUser);

        adminCard.setOnClickListener(v -> showAdminLoginDialog());
        userCard.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void showAdminLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Admin Login");

        // Inflate a custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_admin_login, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Login", (dialog, which) -> {
            android.widget.EditText etAdminUser = dialogView.findViewById(R.id.etAdminUsername);
            android.widget.EditText etAdminPass = dialogView.findViewById(R.id.etAdminPassword);

            String username = etAdminUser.getText().toString().trim();
            String password = etAdminPass.getText().toString().trim();

            // Static Admin Credentials
            if (username.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LandingActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
