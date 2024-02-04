package com.example.newsaggregator;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newsaggregator.databinding.NewsArticleBinding;

public class NewsViewHolder extends RecyclerView.ViewHolder
{
    TextView title;
    TextView date;
    TextView author;
    ImageView newsImage;
    TextView desc;
    TextView articleCount;

    private NewsArticleBinding binding;

    public NewsViewHolder(NewsArticleBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;

        title = binding.newsHeading;
        date = binding.newsDate;
        author = binding.newsAuthor;
        newsImage = binding.newsImage;
        desc = binding.newsDesc;
        articleCount = binding.pageNumber;
    }
}
