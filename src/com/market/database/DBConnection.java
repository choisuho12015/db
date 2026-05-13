package com.market.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/shopping_mall?serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("드라이버를 찾을 수 없습니다.");
        }
    }

    // 요청하신 대로 리턴 타입 없이 void 유지
    public static void insertBook(String[] bookData) {
        String sql = "INSERT INTO book (id, name, price, author, description, category, release_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bookData[0]);
            pstmt.setString(2, bookData[1]);
            pstmt.setInt(3, Integer.parseInt(bookData[2]));
            pstmt.setString(4, bookData[3]);
            pstmt.setString(5, bookData[4]);
            pstmt.setString(6, bookData[5]);
            pstmt.setString(7, bookData[6]);
            
            pstmt.executeUpdate();
            System.out.println("DB 저장 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}