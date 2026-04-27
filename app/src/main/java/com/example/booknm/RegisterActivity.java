package com.example.booknm;

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

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etConfirm;
    private Button btnRegister;
    private DatabaseHelper db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmailReg);
        etPassword = findViewById(R.id.etPasswordReg);
        etConfirm = findViewById(R.id.etConfirm);
        btnRegister = findViewById(R.id.btnRegisterUser);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String conf = etConfirm.getText().toString();

            if (TextUtils.isEmpty(name)) { 
                etName.setError(getString(R.string.required_field)); 
                return; 
            }
            if (!isValidEmail(email)) { 
                etEmail.setError(getString(R.string.email_error)); 
                return; 
            }
            if (!isValidPassword(pass)) { 
                etPassword.setError(getString(R.string.password_error)); 
                return; 
            }
            if (!pass.equals(conf)) { 
                etConfirm.setError(getString(R.string.passwords_match_error)); 
                return; 
            }

            // Firebase Auth Integration
            mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User successfully registered in Firebase
                        User u = new User(name, email, pass, "user");
                        boolean ok = db.addUser(u);
                        if (ok) {
                            Toast.makeText(this, "Registered with Cloud Sync. Login now.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Cloud synced, but local save failed.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
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
