package com.example.app.data.model.card;

public class CardModel {
    private int id;
    private String name;
    private String description;
    private int rating;
    private String user;
    private String dateAcquired;

    public CardModel() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public String getUser() {
        return user;
    }

    public String getDateAcquired() {
        return dateAcquired;
    }
}
