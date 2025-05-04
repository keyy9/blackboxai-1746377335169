package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PricingCategory {
    private int id;
    private String name;
    private BigDecimal basePrice;
    private LocalDateTime createdAt;

    // Default constructor
    public PricingCategory() {}

    // Constructor without id and createdAt (for new category creation)
    public PricingCategory(String name, BigDecimal basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    // Full constructor
    public PricingCategory(int id, String name, BigDecimal basePrice, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PricingCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", createdAt=" + createdAt +
                '}';
    }
}
