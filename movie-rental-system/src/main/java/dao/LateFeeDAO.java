package dao;

import model.LateFee;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class LateFeeDAO {
    // Create a new late fee rule
    public LateFee create(LateFee lateFee) throws SQLException {
        String sql = "INSERT INTO late_fees (days_late_start, days_late_end, fee_per_day) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, lateFee.getDaysLateStart());
            stmt.setInt(2, lateFee.getDaysLateEnd());
            stmt.setBigDecimal(3, lateFee.getFeePerDay());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating late fee rule failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                lateFee.setId(rs.getInt(1));
                return lateFee;
            } else {
                throw new SQLException("Creating late fee rule failed, no ID obtained.");
            }
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get late fee rule by ID
    public LateFee getById(int id) throws SQLException {
        String sql = "SELECT * FROM late_fees WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLateFee(rs);
            }
            
            return null;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Get all late fee rules
    public List<LateFee> getAll() throws SQLException {
        String sql = "SELECT * FROM late_fees ORDER BY days_late_start";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<LateFee> lateFees = new ArrayList<>();
            while (rs.next()) {
                lateFees.add(mapResultSetToLateFee(rs));
            }
            
            return lateFees;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Update late fee rule
    public boolean update(LateFee lateFee) throws SQLException {
        String sql = "UPDATE late_fees SET days_late_start = ?, days_late_end = ?, fee_per_day = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, lateFee.getDaysLateStart());
            stmt.setInt(2, lateFee.getDaysLateEnd());
            stmt.setBigDecimal(3, lateFee.getFeePerDay());
            stmt.setInt(4, lateFee.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Delete late fee rule
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM late_fees WHERE id = ?";
        
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
    
    // Get applicable late fee for days late
    public LateFee getApplicableLateFee(int daysLate) throws SQLException {
        String sql = "SELECT * FROM late_fees WHERE ? BETWEEN days_late_start AND days_late_end";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, daysLate);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLateFee(rs);
            }
            
            return null;
        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(stmt);
        }
    }
    
    // Calculate late fee amount
    public BigDecimal calculateLateFee(int daysLate) throws SQLException {
        LateFee applicableFee = getApplicableLateFee(daysLate);
        if (applicableFee == null) {
            return BigDecimal.ZERO;
        }
        return applicableFee.getFeePerDay().multiply(BigDecimal.valueOf(daysLate));
    }
    
    // Helper method to map ResultSet to LateFee object
    private LateFee mapResultSetToLateFee(ResultSet rs) throws SQLException {
        LateFee lateFee = new LateFee();
        lateFee.setId(rs.getInt("id"));
        lateFee.setDaysLateStart(rs.getInt("days_late_start"));
        lateFee.setDaysLateEnd(rs.getInt("days_late_end"));
        lateFee.setFeePerDay(rs.getBigDecimal("fee_per_day"));
        lateFee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return lateFee;
    }
    
    // Check if range overlaps with existing ranges
    public boolean rangeOverlaps(int start, int end, Integer excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM late_fees WHERE " +
                    "((? BETWEEN days_late_start AND days_late_end) OR " +
                    "(? BETWEEN days_late_start AND days_late_end))";
        
        if (excludeId != null) {
            sql += " AND id != ?";
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, start);
            stmt.setInt(2, end);
            
            if (excludeId != null) {
                stmt.setInt(3, excludeId);
            }
            
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
