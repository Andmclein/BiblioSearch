package com.poo.bibliosearch.Entities;

import java.io.Serializable;

public class Book implements Serializable {
    private int cover, idNumber, rating,releaseYear;
    private String name, author, description;
    String TYPE = "BOOK";

    public Book(int idNumber, int cover, String name, String author, String description, int releaseYear, int rating) {
        this.author = author;
        this.description = description;
        this.cover = cover;
        this.name = name;
        this.idNumber = idNumber;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    public int getCover() {
        return cover;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public String getTYPE() {
        return TYPE;
    }
}

