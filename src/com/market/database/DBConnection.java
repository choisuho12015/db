package com.market.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    // 1. MySQL 접속 정보 (본인의 환경에 맞게 수정)
    // shopping_mall은 데이터베이스 이름입니다.
    private static final String URL = "jdbc:mysql://localhost:3306/shopping_mall?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root"; // MySQL 설치 시 설정한 비밀번호

    // 2. DB 연결 객체를 생성하여 반환하는 메서드
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC 드라이버를 찾을 수 없습니다.");
        }
    }

    // 3. [기존] 도서(Book) 정보 저장 메서드
    public static void insertBook(String[] bookData) {
        String sql = "INSERT INTO book (id, name, price, author, description, category, release_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bookData[0]); // ID
            pstmt.setString(2, bookData[1]); // 도서명
            pstmt.setInt(3, Integer.parseInt(bookData[2])); // 가격 (숫자 변환)
            pstmt.setString(4, bookData[3]); // 저자
            pstmt.setString(5, bookData[4]); // 설명
            pstmt.setString(6, bookData[5]); // 분야
            pstmt.setString(7, bookData[6]); // 출판일
            
            pstmt.executeUpdate();
            System.out.println("도서 정보가 MySQL에 저장되었습니다.");
            
        } catch (SQLException e) {
            System.err.println("도서 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 4. [신규] 고객(Member) 정보 저장 메서드 (로그인 시 호출)
    public static void insertMember(String name, String email) {
        // 이미 존재하는 이메일이면 이름을 최신화(UPDATE)하도록 처리
        String sql = "INSERT INTO member (name, email) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, name); // 중복 시 업데이트할 이름
            
            pstmt.executeUpdate();
            System.out.println("고객 정보(" + name + ")가 DB에 저장/갱신되었습니다.");
            
        } catch (SQLException e) {
            System.err.println("고객 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 5. [신규] 주문(Orders) 내역 저장 메서드 (주문 완료 시 호출)
    public static void insertOrder(String email, String bookNames, int quantity, int price) {
        String sql = "INSERT INTO orders (email, book_names, total_quantity, total_price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);      // 주문자 이메일 (member 테이블의 PK 참조)
            pstmt.setString(2, bookNames);  // 주문한 도서들 요약
            pstmt.setInt(3, quantity);      // 총 권수
            pstmt.setInt(4, price);         // 총 결제 금액
            
            pstmt.executeUpdate();
            System.out.println("주문 내역이 DB에 기록되었습니다.");
            
        } catch (SQLException e) {
            System.err.println("주문 저장 중 오류 발생: " + e.getMessage());
            System.err.println("팁: member 테이블에 해당 이메일이 먼저 등록되어 있어야 합니다.");
        }
    }
}