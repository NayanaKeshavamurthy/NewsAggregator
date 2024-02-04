package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsaggregator.databinding.NewsArticleBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder>
{
    private final MainActivity mainActivity;
    private final ArrayList<NewsArticles> newsList;

    public NewsAdapter(MainActivity mainActivity, ArrayList<NewsArticles> newsList)
    {
        this.mainActivity = mainActivity;
        this.newsList = newsList;
    }

    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new NewsViewHolder(NewsArticleBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position)
    {
        // news object fetch from position
        NewsArticles news = newsList.get(position);

        // check null for author value
        if (news.getAuthor() != null && !news.getAuthor().isEmpty() && news.getAuthor() != "null")
        {
            holder.author.setText(news.getAuthor());
        }
        else
            holder.author.setVisibility(TextView.GONE);


        // fetching the date and setting it
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try
        {
            date = parser.parse(news.getPublishedAt().split("T")[0]);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = formatter.format(date);
        holder.date.setText(formattedDate +" " + news.getPublishedAt().split("T")[1].split(":")[0] + ":" + news.getPublishedAt().split("T")[1].split(":")[1]);

        // check null for date value
        if(news.getPublishedAt().equals("null") || news.getPublishedAt() == null)
        {
            holder.date.setVisibility(TextView.GONE);
        }

        //setting the title
        holder.title.setText(news.getTitle());

        //setting the description
        holder.desc.setText(news.getDescription() == null  || news.getDescription() == "null"
                || news.getDescription().isEmpty()? " " : news.getDescription());


        // instantiate picasso
        Picasso imageDownload = Picasso.get();
        if (TextUtils.isEmpty(news.getUrlToImage()))
            holder.newsImage.setImageResource(R.drawable.noimage);
        else
            imageDownload.load(news.getUrlToImage()).error(R.drawable.brokenimage).into(holder.newsImage);

        holder.articleCount.setText((position + 1) +" of " + (newsList.size()));

        // set on click listeners for all the controls body - title - image
        holder.desc.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            String strWebUrl = news.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // parse through the url and open it
            Uri url = Uri.parse(strWebUrl);
            intent.setData(url);
            mainActivity.startActivity(intent);
        } });

        holder.title.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            String strWebUrl = news.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // parse through the url and open it
            Uri url = Uri.parse(strWebUrl);
            intent.setData(url);
            mainActivity.startActivity(intent);
        } });

        holder.newsImage.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            String strWebUrl = news.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // parse through the url and open it
            Uri url = Uri.parse(strWebUrl);
            intent.setData(url);
            mainActivity.startActivity(intent);
        } });
    }

    @Override
    public int getItemCount()
    {
        return newsList.size();
    }

}



