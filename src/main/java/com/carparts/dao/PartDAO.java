package com.carparts.dao;

import com.carparts.model.Part;
import com.carparts.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartDAO {

    public Part findById(int id) throws SQLException {
        String sql = "SELECT * FROM parts WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractPart(rs);
            }
        }
        return null;
    }

    public boolean create(Part part) throws SQLException {
        String sql = "INSERT INTO parts (name, category, manufacturer, price, stock, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, part.getName());
            pstmt.setString(2, part.getCategory());
            pstmt.setString(3, part.getManufacturer());
            pstmt.setDouble(4, part.getPrice());
            pstmt.setInt(5, part.getStock());
            pstmt.setString(6, part.getDescription());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(Part part) throws SQLException {
        String sql = "UPDATE parts SET name=?, category=?, manufacturer=?, price=?, stock=?, description=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, part.getName());
            pstmt.setString(2, part.getCategory());
            pstmt.setString(3, part.getManufacturer());
            pstmt.setDouble(4, part.getPrice());
            pstmt.setInt(5, part.getStock());
            pstmt.setString(6, part.getDescription());
            pstmt.setInt(7, part.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM parts WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Part> findAll() throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts ORDER BY id DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parts.add(extractPart(rs));
            }
        }
        return parts;
    }

    public List<Part> search(String keyword) throws SQLException {
        List<Part> parts = new ArrayList<>();
        String sql = "SELECT * FROM parts WHERE name LIKE ? OR category LIKE ? OR manufacturer LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                parts.add(extractPart(rs));
            }
        }
        return parts;
    }

    private Part extractPart(ResultSet rs) throws SQLException {
        Part part = new Part();
        part.setId(rs.getInt("id"));
        part.setName(rs.getString("name"));
        part.setCategory(rs.getString("category"));
        part.setManufacturer(rs.getString("manufacturer"));
        part.setPrice(rs.getDouble("price"));
        part.setStock(rs.getInt("stock"));
        part.setDescription(rs.getString("description"));
        return part;
    }
}