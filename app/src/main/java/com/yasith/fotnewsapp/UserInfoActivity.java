package com.yasith.fotnewsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {

    private TextView usernameText, emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            fetchUserDetails(user.getUid());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void openEditDialog(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_info, null);

        EditText editUsername = dialogView.findViewById(R.id.editUsername);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);

        editUsername.setText(usernameText.getText().toString());
        editEmail.setText(emailText.getText().toString());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Details")
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogView.findViewById(R.id.saveButton).setOnClickListener(v -> {
            saveDetails(editUsername, editEmail);
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void saveDetails(EditText editUsername, EditText editEmail) {
        String newUsername = editUsername.getText().toString();
        String newEmail = editEmail.getText().toString();

        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

            userRef.child("username").setValue(newUsername);
            userRef.child("email").setValue(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            usernameText.setText(newUsername);
                            emailText.setText(newEmail);
                            Toast.makeText(this, "Details Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error saving details", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSignOutDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sign_out, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button yesButton = dialogView.findViewById(R.id.yesButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelSignOutButton);

        yesButton.setOnClickListener(v -> {
            signOut();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void fetchUserDetails(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    usernameText.setText(username);
                    emailText.setText(email);
                } else {
                    Toast.makeText(UserInfoActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserInfoActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
