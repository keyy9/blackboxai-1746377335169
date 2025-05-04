package dao;

import model.PricingCategory;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PricingCategoryDAO {
    // Create a new pricing category
    public PricingCategory create(PricingCategory category) throws SQLException {
        String sql = "INSERT INTO pricing_categories (name, base_price) VALUES (?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, category.getName());
            stmt.setBigDecimal(2, category.getBasePrice());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating pricing category failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                category.setId(rs.getInt(1));
                return category;
            } else {
                throw new SQLException("Creating pricing category failed, no ID obtained.");
            }
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get pricing category by ID
    public PricingCategory getById(int id) throws SQLException {
        String sql = "SELECT * FROM pricing_categories WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPricingCategory(rs);
            }
            
            return null;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get all pricing categories
    public List<PricingCategory> getAll() throws SQLException {
        String sql = "SELECT * FROM pricing_categories ORDER BY base_price";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<PricingCategory> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapResultSetToPricingCategory(rs));
            }
            
            return categories;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Update pricing category
    public boolean update(PricingCategory category) throws SQLException {
        String sql = "UPDATE pricing_categories SET name = ?, base_price = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, category.getName());
            stmt.setBigDecimal(2, category.getBasePrice());
            stmt.setInt(3, category.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Delete pricing category
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pricing_categories WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get pricing category for a movie
    public PricingCategory getPricingCategoryForMovie(int movieId) throws SQLException {
        String sql = "SELECT pc.* FROM pricing_categories pc " +
                    "JOIN movie_pricing mp ON pc.id = mp.pricing_category_id " +
                    "WHERE mp.movie_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, movieId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPricingCategory(rs);
            }
            
            return null;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Helper method to map ResultSet to PricingCategory object
    private PricingCategory mapResultSetToPricingCategory(ResultSet rs) throws SQLException {
        PricingCategory category = new PricingCategory();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setBasePrice(rs.getBigDecimal("base_price"));
        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return category;
    }
    
    // Check if pricing category name exists
    public boolean nameExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pricing_categories WHERE name = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
}
