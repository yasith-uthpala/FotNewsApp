package com.yasith.fotnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList;
    private List<String> newsKeys;
    private List<NewsItem> filteredNewsList;
    private List<String> filteredKeysList;
    private DatabaseReference databaseReference;

    private LinearLayout sportCategory, academicCategory, eventsCategory;
    private EditText searchNewsEditText;
    private ImageView infoIcon, userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchNewsEditText = findViewById(R.id.searchNews);

        newsList = new ArrayList<>();
        newsKeys = new ArrayList<>();
        filteredNewsList = new ArrayList<>();
        filteredKeysList = new ArrayList<>();

        newsAdapter = new NewsAdapter(filteredNewsList, filteredKeysList);
        recyclerView.setAdapter(newsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("news");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();
                newsKeys.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewsItem newsItem = snapshot.getValue(NewsItem.class);
                    String key = snapshot.getKey();
                    newsList.add(newsItem);
                    newsKeys.add(key);
                }

                sortNewsByDate();
                filterNews("");
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        sportCategory = findViewById(R.id.sportCategory);
        academicCategory = findViewById(R.id.academicCategory);
        eventsCategory = findViewById(R.id.eventsCategory);
        infoIcon = findViewById(R.id.infoIcon);
        userIcon = findViewById(R.id.userIcon);

        sportCategory.setOnClickListener(v -> filterNewsByCategory("Sport"));

        academicCategory.setOnClickListener(v -> filterNewsByCategory("Academic"));

        eventsCategory.setOnClickListener(v -> filterNewsByCategory("Events"));

        searchNewsEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().isEmpty()) {
                    filterNews("");
                } else {
                    filterNews(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        infoIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeveloperInfoActivity.class);
            startActivity(intent);
        });

        userIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });
    }

    private void sortNewsByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        List<NewsItem> sortedNewsList = new ArrayList<>();
        List<String> sortedKeys = new ArrayList<>();

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < newsList.size(); i++) indexes.add(i);

        Collections.sort(indexes, (i1, i2) -> {
            try {
                String dt1 = newsList.get(i1).getDate() + " " + newsList.get(i1).getTime();
                String dt2 = newsList.get(i2).getDate() + " " + newsList.get(i2).getTime();
                Date date1 = sdf.parse(dt1);
                Date date2 = sdf.parse(dt2);
                return date2.compareTo(date1);
            } catch (Exception e) {
                return 0;
            }
        });

        for (int idx : indexes) {
            sortedNewsList.add(newsList.get(idx));
            sortedKeys.add(newsKeys.get(idx));
        }
        newsList.clear();
        newsList.addAll(sortedNewsList);
        newsKeys.clear();
        newsKeys.addAll(sortedKeys);
    }


    private void filterNews(String query) {
        filteredNewsList.clear();
        filteredKeysList.clear();

        if (query.isEmpty()) {
            filteredNewsList.addAll(newsList);
            filteredKeysList.addAll(newsKeys);
        } else {
            for (int i = 0; i < newsList.size(); i++) {
                NewsItem newsItem = newsList.get(i);
                if (newsItem.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        newsItem.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredNewsList.add(newsItem);
                    filteredKeysList.add(newsKeys.get(i));
                }
            }
        }

        newsAdapter.notifyDataSetChanged();
    }

    private void filterNewsByCategory(String category) {
        filteredNewsList.clear();
        filteredKeysList.clear();
        boolean isCategoryEmpty = true;

        if (category.equals("all")) {
            filteredNewsList.addAll(newsList);
            filteredKeysList.addAll(newsKeys);
        } else {
            for (int i = 0; i < newsList.size(); i++) {
                NewsItem newsItem = newsList.get(i);
                if (newsItem.getCategory().equalsIgnoreCase(category)) {
                    filteredNewsList.add(newsItem);
                    filteredKeysList.add(newsKeys.get(i));
                    isCategoryEmpty = false;
                }
            }
        }

        if (isCategoryEmpty) {
            Toast.makeText(MainActivity.this, "No news available for this category.", Toast.LENGTH_SHORT).show();
        }

        newsAdapter.notifyDataSetChanged();
    }
}
