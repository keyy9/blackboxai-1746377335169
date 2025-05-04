package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LateFee {
    private int id;
    private int daysLateStart;
    private int daysLateEnd;
    private BigDecimal feePerDay;
    private LocalDateTime createdAt;

    // Default constructor
    public LateFee() {}

    // Constructor without id and createdAt (for new late fee creation)
    public LateFee(int daysLateStart, int daysLateEnd, BigDecimal feePerDay) {
        this.daysLateStart = daysLateStart;
        this.daysLateEnd = daysLateEnd;
        this.feePerDay = feePerDay;
    }

    // Full constructor
    public LateFee(int id, int daysLateStart, int daysLateEnd, BigDecimal feePerDay, LocalDateTime createdAt) {
        this.id = id;
        this.daysLateStart = daysLateStart;
        this.daysLateEnd = daysLateEnd;
        this.feePerDay = feePerDay;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDaysLateStart() {
        return daysLateStart;
    }

    public void setDaysLateStart(int daysLateStart) {
        this.daysLateStart = daysLateStart;
    }

    public int getDaysLateEnd() {
        return daysLateEnd;
    }

    public void setDaysLateEnd(int daysLateEnd) {
        this.daysLateEnd = daysLateEnd;
    }

    public BigDecimal getFeePerDay() {
        return feePerDay;
    }

    public void setFeePerDay(BigDecimal feePerDay) {
        this.feePerDay = feePerDay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to check if days late falls within this fee range
    public boolean isApplicable(int daysLate) {
        return daysLate >= daysLateStart && daysLate <= daysLateEnd;
    }

    // Helper method to calculate fee for given days late
    public BigDecimal calculateFee(int daysLate) {
        if (!isApplicable(daysLate)) {
            return BigDecimal.ZERO;
        }
        return feePerDay.multiply(BigDecimal.valueOf(daysLate));
    }

    @Override
    public String toString() {
        return "LateFee{" +
                "id=" + id +
                ", daysLateStart=" + daysLateStart +
                ", daysLateEnd=" + daysLateEnd +
                ", feePerDay=" + feePerDay +
                ", createdAt=" + createdAt +
                '}';
    }
}
