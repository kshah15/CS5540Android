package com.example.kishan.newsapp2.model;

/**
 * Created by Kishan on 6/29/2017.
 */

public class NewsItem {

    String Source;

    String AuthorName;
    String Title;
    String NewsDescription;
    String UrlBar;
    String UrlBarImage;
    String PublishedAt;

    public NewsItem(String source, String author, String title, String description, String url, String urlToImage, String publishedAt) {
        this.Source = source;
        this.AuthorName = author;
        this.Title = title;
        this.NewsDescription = description;
        this.UrlBar = url;
        this.UrlBarImage = urlToImage;
        this.PublishedAt = publishedAt;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        this.Source = source;
    }

    public String getAuthor() {
        return AuthorName;
    }

    public void setAuthor(String author) {
        this.AuthorName = author;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDescription() {
        return NewsDescription;
    }

    public void setDescription(String description) {
        this.NewsDescription = description;
    }

    public String getUrl() {
        return UrlBar;
    }

    public void setUrl(String url) {
        this.UrlBar = url;
    }

    public String getUrlToImage() {
        return UrlBarImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.UrlBarImage = urlToImage;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }
}