package com.example.newsaggregator;

public class NewsArticles
{
    public String author;
    public String title;
    public String description;
    public String urlToImage;
    public String publishedAt;
    public String url;

    public String getAuthor()
    {
        return author;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUrlToImage()
    {
        return urlToImage;
    }

    public String getPublishedAt()
    {
        return publishedAt;
    }

    public String getUrl()
    {
        return url;
    }

    public NewsArticles(String author, String title, String description, String url, String urlToImage, String publishedAt)
    {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }
}
