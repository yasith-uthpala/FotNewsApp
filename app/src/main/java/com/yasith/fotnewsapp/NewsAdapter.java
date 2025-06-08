package com.yasith.fotnewsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private List<String> newsKeys;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView newsTitle;
        public TextView newsDescription;
        public TextView newsDate;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsDate = itemView.findViewById(R.id.newsDate);
        }
    }

    public NewsAdapter(List<NewsItem> newsList, List<String> newsKeys) {
        this.newsList = newsList;
        this.newsKeys = newsKeys;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem currentItem = newsList.get(position);
        String newsKey = newsKeys.get(position);

        Glide.with(holder.newsImage.getContext())
                .load(currentItem.getImageUrl())
                .into(holder.newsImage);

        holder.newsTitle.setText(currentItem.getTitle());
        holder.newsDescription.setText(currentItem.getDescription());
        holder.newsDate.setText(currentItem.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailedNewsActivity.class);
                intent.putExtra("title", currentItem.getTitle());
                intent.putExtra("description", currentItem.getDescription());
                intent.putExtra("date", currentItem.getDate());
                intent.putExtra("imageUrl", currentItem.getImageUrl());
                intent.putExtra("time", currentItem.getTime());
                intent.putExtra("likes", currentItem.getLikes());
                intent.putExtra("newsKey", newsKey);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
