package dao;

import model.Movie;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MovieDAO {
    // Create a new movie
    public Movie create(Movie movie, int pricingCategoryId) throws SQLException {
        String sql = "INSERT INTO movies (title, genre, available_copies) VALUES (?, ?, ?)";
        String pricingSql = "INSERT INTO movie_pricing (movie_id, pricing_category_id) VALUES (?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement pricingStmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert movie
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getAvailableCopies());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating movie failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                movie.setId(rs.getInt(1));
                
                // Insert pricing
                pricingStmt = conn.prepareStatement(pricingSql);
                pricingStmt.setInt(1, movie.getId());
                pricingStmt.setInt(2, pricingCategoryId);
                pricingStmt.executeUpdate();
                
                conn.commit();
                return movie;
            } else {
                throw new SQLException("Creating movie failed, no ID obtained.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error rolling back transaction", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(pricingStmt);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get movie by ID with current price
    public Movie getById(int id) throws SQLException {
        String sql = "SELECT m.*, pc.base_price FROM movies m " +
                    "LEFT JOIN movie_pricing mp ON m.id = mp.movie_id " +
                    "LEFT JOIN pricing_categories pc ON mp.pricing_category_id = pc.id " +
                    "WHERE m.id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMovie(rs);
            }
            
            return null;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get all movies with their current prices
    public List<Movie> getAll() throws SQLException {
        String sql = "SELECT m.*, pc.base_price FROM movies m " +
                    "LEFT JOIN movie_pricing mp ON m.id = mp.movie_id " +
                    "LEFT JOIN pricing_categories pc ON mp.pricing_category_id = pc.id " +
                    "ORDER BY m.title";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                movies.add(mapResultSetToMovie(rs));
            }
            
            return movies;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Update movie
    public boolean update(Movie movie, Integer pricingCategoryId) throws SQLException {
        String sql = "UPDATE movies SET title = ?, genre = ?, available_copies = ? WHERE id = ?";
        String pricingSql = "UPDATE movie_pricing SET pricing_category_id = ? WHERE movie_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement pricingStmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Update movie
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getAvailableCopies());
            stmt.setInt(4, movie.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            // Update pricing if provided
            if (pricingCategoryId != null) {
                pricingStmt = conn.prepareStatement(pricingSql);
                pricingStmt.setInt(1, pricingCategoryId);
                pricingStmt.setInt(2, movie.getId());
                pricingStmt.executeUpdate();
            }
            
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error rolling back transaction", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            DatabaseConnection.closeStatement(pricingStmt);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Update available copies
    public boolean updateAvailableCopies(int movieId, int copies) throws SQLException {
        String sql = "UPDATE movies SET available_copies = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, copies);
            stmt.setInt(2, movieId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Delete movie
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        
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
    
    // Helper method to map ResultSet to Movie object
    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setGenre(rs.getString("genre"));
        movie.setAvailableCopies(rs.getInt("available_copies"));
        movie.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        // Set current price if available
        BigDecimal basePrice = rs.getBigDecimal("base_price");
        if (basePrice != null) {
            movie.setCurrentPrice(basePrice);
        }
        
        return movie;
    }
    
    // Get available movies
    public List<Movie> getAvailableMovies() throws SQLException {
        String sql = "SELECT m.*, pc.base_price FROM movies m " +
                    "LEFT JOIN movie_pricing mp ON m.id = mp.movie_id " +
                    "LEFT JOIN pricing_categories pc ON mp.pricing_category_id = pc.id " +
                    "WHERE m.available_copies > 0 " +
                    "ORDER BY m.title";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                movies.add(mapResultSetToMovie(rs));
            }
            
            return movies;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
}
