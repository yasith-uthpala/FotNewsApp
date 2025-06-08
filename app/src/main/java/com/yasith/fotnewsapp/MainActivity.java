package com.yasith.fotnewsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private List<NewsItem> filteredNewsList;
    private DatabaseReference databaseReference;

    private LinearLayout sportCategory, academicCategory, eventsCategory;
    private EditText searchNewsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchNewsEditText = findViewById(R.id.searchNews);

        newsList = new ArrayList<>();
        filteredNewsList = new ArrayList<>();

        newsAdapter = new NewsAdapter(filteredNewsList);
        recyclerView.setAdapter(newsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("news");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewsItem newsItem = snapshot.getValue(NewsItem.class);
                    newsList.add(newsItem);
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

        sportCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNewsByCategory("Sport");
            }
        });

        academicCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNewsByCategory("Academic");
            }
        });

        eventsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNewsByCategory("Events");
            }
        });

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
    }

    private void sortNewsByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Collections.sort(newsList, (news1, news2) -> {
            try {
                Date date1 = sdf.parse(news1.getDate());
                Date date2 = sdf.parse(news2.getDate());
                return date2.compareTo(date1);
            } catch (Exception e) {
                return 0;
            }
        });
    }

    private void filterNews(String query) {
        filteredNewsList.clear();

        if (query.isEmpty()) {
            filteredNewsList.addAll(newsList);
        } else {
            for (NewsItem newsItem : newsList) {
                if (newsItem.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        newsItem.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredNewsList.add(newsItem);
                }
            }
        }

        newsAdapter.notifyDataSetChanged();
    }

    private void filterNewsByCategory(String category) {
        filteredNewsList.clear();
        boolean isCategoryEmpty = true;

        if (category.equals("all")) {
            filteredNewsList.addAll(newsList);
        } else {
            for (NewsItem newsItem : newsList) {
                if (newsItem.getCategory().equalsIgnoreCase(category)) {
                    filteredNewsList.add(newsItem);
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
