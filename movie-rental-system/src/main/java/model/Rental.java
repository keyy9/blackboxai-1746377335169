package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Rental {
    private int id;
    private int userId;
    private int movieId;
    private LocalDate rentalDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal basePrice;
    private BigDecimal lateFee;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    // Additional fields for displaying rental information
    private String movieTitle;
    private String userName;

    // Default constructor
    public Rental() {}

    // Constructor for new rental creation
    public Rental(int userId, int movieId, LocalDate rentalDate, LocalDate dueDate, BigDecimal basePrice) {
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.basePrice = basePrice;
        this.lateFee = BigDecimal.ZERO;
        this.totalPrice = basePrice;
    }

    // Full constructor
    public Rental(int id, int userId, int movieId, LocalDate rentalDate, LocalDate dueDate, 
                 LocalDate returnDate, BigDecimal basePrice, BigDecimal lateFee, 
                 BigDecimal totalPrice, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.basePrice = basePrice;
        this.lateFee = lateFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Helper methods
    public boolean isReturned() {
        return returnDate != null;
    }

    public int getDaysLate() {
        if (!isReturned()) {
            return 0;
        }
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysLate > 0 ? (int) daysLate : 0;
    }

    public void calculateTotalPrice() {
        this.totalPrice = this.basePrice.add(this.lateFee);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", userId=" + userId +
                ", movieId=" + movieId +
                ", movieTitle='" + movieTitle + '\'' +
                ", userName='" + userName + '\'' +
                ", rentalDate=" + rentalDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", basePrice=" + basePrice +
                ", lateFee=" + lateFee +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                '}';
    }
}
