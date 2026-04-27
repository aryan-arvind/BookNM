package com.example.booknm;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// FIX: Changed the import statement to match the User class's actual package
import com.example.booknm.User;

public class AddAdminActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnAddAdmin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
        btnAddAdmin = findViewById(R.id.btnAddAdmin);

        btnAddAdmin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString();

            if (!isValidEmail(email)) { etEmail.setError("Use institutional email"); return; }
            if (!isValidPassword(pass)) { etPassword.setError("Password needs uppercase and number"); return; }

            // This line now works because the import is correct
            User u = new User("Admin", email, pass, "admin");
            boolean ok = db.addAdmin(u);
            if (ok) {
                Toast.makeText(this, "Admin added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Add failed (email may exist)", Toast.LENGTH_SHORT).show();
            }
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
