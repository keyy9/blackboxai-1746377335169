package model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private int availableCopies;
    private LocalDateTime createdAt;
    private BigDecimal currentPrice; // To store the current price based on pricing category

    // Default constructor
    public Movie() {}

    // Constructor without id and createdAt (for new movie creation)
    public Movie(String title, String genre, int availableCopies) {
        this.title = title;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }

    // Full constructor
    public Movie(int id, String title, String genre, int availableCopies, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.availableCopies = availableCopies;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", availableCopies=" + availableCopies +
                ", currentPrice=" + currentPrice +
                ", createdAt=" + createdAt +
                '}';
    }

    // Helper method to check if movie is available
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    // Helper method to decrease available copies
    public void decreaseAvailableCopies() {
        if (isAvailable()) {
            this.availableCopies--;
        }
    }

    // Helper method to increase available copies
    public void increaseAvailableCopies() {
        this.availableCopies++;
    }
}
