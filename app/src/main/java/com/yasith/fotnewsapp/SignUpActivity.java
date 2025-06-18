package com.yasith.fotnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, emailEditText;
    private Button signUpButton;
    private TextView loginHereText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginHereText = findViewById(R.id.loginHereText);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserDetails(user.getUid(), username, email);
                            signInUser(email, password);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginHereText.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Sign-up and login successful", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        // startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void saveUserDetails(String userId, String username, String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        User user = new User(username, email);


        databaseReference.setValue(user)
                .addOnSuccessListener(aVoid -> Toast.makeText(SignUpActivity.this, "User details saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Failed to save user details", Toast.LENGTH_SHORT).show());
    }
}
