package com.yasith.fotnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedNewsActivity extends AppCompatActivity {

    private ImageView newsImageView, backButton, infoIcon, userIcon, likeButton;
    private TextView titleTextView, descriptionTextView, dateTextView, timeTextView, likeCount;
    private int likes = 0;
    private String newsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        newsImageView = findViewById(R.id.newsImage);
        backButton = findViewById(R.id.backButton);
        infoIcon = findViewById(R.id.infoIcon);
        userIcon = findViewById(R.id.userIcon);
        titleTextView = findViewById(R.id.newsTitle);
        descriptionTextView = findViewById(R.id.newsDescription);
        dateTextView = findViewById(R.id.newsDate);
        timeTextView = findViewById(R.id.newsTime);
        likeButton = findViewById(R.id.likeButton);
        likeCount = findViewById(R.id.likeCount);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String imageUrl = intent.getStringExtra("imageUrl");
        newsKey = intent.getStringExtra("newsKey");

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        dateTextView.setText(date);
        timeTextView.setText(getFormattedTime(date, time));

        int cornerRadiusDp = 40;
        float density = getResources().getDisplayMetrics().density;
        int cornerRadiusPx = (int) (cornerRadiusDp * density);

        Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(cornerRadiusPx)))
                .into(newsImageView);

        // Always read live likes from Firebase on open
        if (newsKey != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("news").child(newsKey).child("likes");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    likes = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                    likeCount.setText(String.valueOf(likes));
                }
                @Override
                public void onCancelled(DatabaseError error) { }
            });
        }

        likeButton.setOnClickListener(v -> {
            likes++;
            likeCount.setText(String.valueOf(likes));
            if (newsKey != null) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("news").child(newsKey).child("likes");
                ref.setValue(likes);
            }
        });

        backButton.setOnClickListener(v -> onBackPressed());
        infoIcon.setOnClickListener(v -> { });
        userIcon.setOnClickListener(v -> { });
    }

    private String getFormattedTime(String dateString, String timeString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

            Date now = new Date();
            String todayString = dateFormat.format(now);

            if (dateString.equals(todayString)) {
                Date newsDateTime = dateTimeFormat.parse(dateString + " " + timeString);
                long diffMillis = now.getTime() - newsDateTime.getTime();

                long minutes = diffMillis / (60 * 1000);
                long hours = diffMillis / (60 * 60 * 1000);

                if (minutes < 60) {
                    return minutes + " min ago";
                } else {
                    return hours + " hours ago";
                }
            } else {
                return timeString;
            }
        } catch (Exception e) {
            return timeString;
        }
    }
}
