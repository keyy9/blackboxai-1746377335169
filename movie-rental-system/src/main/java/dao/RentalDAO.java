package dao;

import model.Rental;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class RentalDAO {
    // Create a new rental
    public Rental create(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (user_id, movie_id, rental_date, due_date, base_price, total_price) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Create rental record
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, rental.getUserId());
            stmt.setInt(2, rental.getMovieId());
            stmt.setDate(3, Date.valueOf(rental.getRentalDate()));
            stmt.setDate(4, Date.valueOf(rental.getDueDate()));
            stmt.setBigDecimal(5, rental.getBasePrice());
            stmt.setBigDecimal(6, rental.getTotalPrice());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating rental failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                rental.setId(rs.getInt(1));
                
                // Update movie available copies
                String updateMovieSql = "UPDATE movies SET available_copies = available_copies - 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateMovieSql);
                updateStmt.setInt(1, rental.getMovieId());
                updateStmt.executeUpdate();
                updateStmt.close();
                
                conn.commit();
                return rental;
            } else {
                throw new SQLException("Creating rental failed, no ID obtained.");
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
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get rental by ID with movie and user details
    public Rental getById(int id) throws SQLException {
        String sql = "SELECT r.*, m.title as movie_title, u.name as user_name " +
                    "FROM rentals r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRental(rs);
            }
            
            return null;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get all rentals with movie and user details
    public List<Rental> getAll() throws SQLException {
        String sql = "SELECT r.*, m.title as movie_title, u.name as user_name " +
                    "FROM rentals r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "ORDER BY r.rental_date DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Rental> rentals = new ArrayList<>();
            while (rs.next()) {
                rentals.add(mapResultSetToRental(rs));
            }
            
            return rentals;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get active rentals (not returned)
    public List<Rental> getActiveRentals() throws SQLException {
        String sql = "SELECT r.*, m.title as movie_title, u.name as user_name " +
                    "FROM rentals r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.return_date IS NULL " +
                    "ORDER BY r.due_date ASC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Rental> rentals = new ArrayList<>();
            while (rs.next()) {
                rentals.add(mapResultSetToRental(rs));
            }
            
            return rentals;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Return movie and update late fees
    public boolean returnMovie(int rentalId, LocalDate returnDate, BigDecimal lateFee) throws SQLException {
        String sql = "UPDATE rentals SET return_date = ?, late_fee = ?, total_price = base_price + ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Update rental record
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setBigDecimal(2, lateFee);
            stmt.setBigDecimal(3, lateFee);
            stmt.setInt(4, rentalId);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get movie ID for this rental
                String getMovieIdSql = "SELECT movie_id FROM rentals WHERE id = ?";
                PreparedStatement getMovieStmt = conn.prepareStatement(getMovieIdSql);
                getMovieStmt.setInt(1, rentalId);
                ResultSet rs = getMovieStmt.executeQuery();
                
                if (rs.next()) {
                    int movieId = rs.getInt("movie_id");
                    
                    // Update movie available copies
                    String updateMovieSql = "UPDATE movies SET available_copies = available_copies + 1 WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateMovieSql);
                    updateStmt.setInt(1, movieId);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                }
                
                rs.close();
                getMovieStmt.close();
                
                conn.commit();
                return true;
            }
            
            return false;
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
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get overdue rentals
    public List<Rental> getOverdueRentals() throws SQLException {
        String sql = "SELECT r.*, m.title as movie_title, u.name as user_name " +
                    "FROM rentals r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.return_date IS NULL AND r.due_date < CURRENT_DATE " +
                    "ORDER BY r.due_date ASC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Rental> rentals = new ArrayList<>();
            while (rs.next()) {
                rentals.add(mapResultSetToRental(rs));
            }
            
            return rentals;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Helper method to map ResultSet to Rental object
    private Rental mapResultSetToRental(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setId(rs.getInt("id"));
        rental.setUserId(rs.getInt("user_id"));
        rental.setMovieId(rs.getInt("movie_id"));
        rental.setRentalDate(rs.getDate("rental_date").toLocalDate());
        rental.setDueDate(rs.getDate("due_date").toLocalDate());
        
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            rental.setReturnDate(returnDate.toLocalDate());
        }
        
        rental.setBasePrice(rs.getBigDecimal("base_price"));
        rental.setLateFee(rs.getBigDecimal("late_fee"));
        rental.setTotalPrice(rs.getBigDecimal("total_price"));
        rental.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        // Set additional display fields
        rental.setMovieTitle(rs.getString("movie_title"));
        rental.setUserName(rs.getString("user_name"));
        
        return rental;
    }
}
