package com.example.booknm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// FIX: Corrected the import statement to the project's actual package
import com.example.booknm.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString();
            if (!isValidEmail(email)) {
                etEmail.setError("Use institutional email");
                return;
            }
            if (!isValidPassword(pass)) {
                etPassword.setError("Password needs uppercase and a number");
                return;
            }
            // Firebase Auth Integration
            mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User user = db.getUserByEmail(email);
                        if (user != null) {
                            if ("admin".equalsIgnoreCase(user.getRole())) {
                                Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                i.putExtra("userId", user.getId());
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(LoginActivity.this, UserHomeActivity.class);
                                i.putExtra("userId", user.getId());
                                startActivity(i);
                                finish();
                            }
                        } else {
                            Toast.makeText(this, "Cloud login OK, but local profile missing.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Firebase Auth failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        });
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email)) && email.endsWith("@nmims.in") && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String p) {
        if (p == null) return false;
        boolean hasUpper = !p.equals(p.toLowerCase());
        boolean hasDigit = p.matches(".*\\d.*");
        return hasUpper && hasDigit && p.length() >= 6;
    }
}
