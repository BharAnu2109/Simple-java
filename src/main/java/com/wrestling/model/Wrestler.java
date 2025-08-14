package com.wrestling.model;

public class Wrestler {
    private String name;
    private int rating;
    private String category;
    private int wins;
    private int losses;

    public Wrestler() {}

    public Wrestler(String name, int rating, String category, int wins, int losses) {
        this.name = name;
        this.rating = rating;
        this.category = category;
        this.wins = wins;
        this.losses = losses;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }
    
    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public double getWinPercentage() {
        int totalMatches = wins + losses;
        return totalMatches > 0 ? (double) wins / totalMatches * 100 : 0.0;
    }

    @Override
    public String toString() {
        return String.format("Wrestler{name='%s', rating=%d, category='%s', wins=%d, losses=%d, winRate=%.1f%%}", 
                name, rating, category, wins, losses, getWinPercentage());
    }
}