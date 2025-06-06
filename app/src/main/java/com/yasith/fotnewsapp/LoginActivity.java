package com.yasith.fotnewsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, signUpHereText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        signUpHereText = findViewById(R.id.signUpHereText); // Correct ID!

        // Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Implement actual login logic (e.g., Firebase Authentication)
                    Toast.makeText(LoginActivity.this, "Login successful (mock)", Toast.LENGTH_SHORT).show();

                    // Example: Move to main/home activity after successful login
                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // startActivity(intent);
                    // finish();
                }
            }
        });

        // Forgot password logic (mock)
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Sign up logic (mock)
        signUpHereText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to SignUpActivity (if you implement it in future)
                // Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                // startActivity(intent);
                Toast.makeText(LoginActivity.this, "Sign Up Here Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
