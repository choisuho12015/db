package com.market.database;

import java.sql.*;
import com.market.cart.Cart;
import com.market.cart.CartItem;
import com.market.member.UserInIt;

public class DBConnection {
    // MySQL 접속을 위한 기본 설정 정보
    private static final String URL = "jdbc:mysql://localhost:3306/shopping_mall?serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root";

    /**
     * 데이터베이스 연결 객체를 생성하여 반환합니다.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("드라이버를 찾을 수 없습니다.");
        }
    }

    /**
     * 관리자 페이지에서 입력한 도서 정보를 DB에 저장합니다.
     */
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

    /**
     * [사용자 수정] 로그인 시 회원의 주소 정보를 불러오고 최근 주문 영수증을 콘솔에 출력합니다.
     * - member, orders, order_items 3개 테이블을 조인하여 연관 데이터를 한 번에 가져옵니다.
     * - 조회된 주소는 UserInIt 객체에 세팅하여 프로그램 전역에서 사용 가능하게 합니다.
     */
    public static void saveUser(String name, String email) {
        String sql = "SELECT m.address, o.total_price, oi.book_id, oi.quantity " +
                     "FROM member m " +
                     "LEFT JOIN orders o ON m.phone = o.user_email " +
                     "LEFT JOIN order_items oi ON o.order_id = oi.order_id " +
                     "WHERE m.name = ? AND m.phone = ? " +
                     "ORDER BY o.order_date DESC LIMIT 1"; 
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // [사용자 수정] DB에서 가져온 주소를 실시간으로 유저 객체(UserInIt)에 동기화
                    String dbAddress = rs.getString("address");
                    if (UserInIt.getmUser() != null) {
                        UserInIt.getmUser().setAddress(dbAddress);
                    }

                    // [사용자 수정] 최근 주문 내역을 영수증 형태로 콘솔에 출력하는 로직 구현
                    System.out.println("\n========== 고객 정보 및 최근 주문 내역 ==========");
                    System.out.println("고객명 : " + name);
                    System.out.println("주문한 도서 : " + rs.getString("book_id"));
                    System.out.println("주문한 도서의 수량 : " + rs.getInt("quantity"));
                    System.out.println("총 금액 : " + rs.getInt("total_price") + "원");
                    System.out.println("================================================\n");
                    
                } else {
                    System.out.println("[알림] 일치하는 고객 정보나 주문 내역이 없습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * [사용자 수정] 결제 완료 시 장바구니 리스트를 DB에 기록하는 핵심 주문 로직입니다.
     * - setAutoCommit(false)를 통해 주문 마스터와 상세 내역이 모두 성공해야 저장되는 트랜잭션을 구현했습니다.
     * - RETURN_GENERATED_KEYS로 생성된 주문 번호를 가져와 상세 내역(order_items)과 연결합니다.
     */
    public static void insertOrder(String email, Cart cart, int finalPrice) {
        String orderSql = "INSERT INTO orders (user_email, total_price) VALUES (?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, book_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            // [사용자 수정] 데이터 일관성을 위해 수동 커밋 모드 활성화
            conn.setAutoCommit(false); 

            // 1. 주문 마스터 저장 (누가, 얼마를 샀는가)
            PreparedStatement pstmtOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setString(1, email);
            pstmtOrder.setInt(2, finalPrice);
            pstmtOrder.executeUpdate();

            // [사용자 수정] DB가 자동 생성한 주문 ID를 상세 테이블 연동을 위해 추출
            ResultSet rs = pstmtOrder.getGeneratedKeys();
            int orderId = rs.next() ? rs.getInt(1) : 0;

            // 2. 주문 상세 저장 (어떤 책을 샀는가 - Batch 처리)
            PreparedStatement pstmtItem = conn.prepareStatement(itemSql);
            for (CartItem item : cart.mCartItem) {
                pstmtItem.setInt(1, orderId);
                pstmtItem.setString(2, item.getBookID());
                pstmtItem.setInt(3, item.getQuantity());
                pstmtItem.addBatch(); // [사용자 수정] 대량 품목 처리를 위한 배치 추가
            }
            pstmtItem.executeBatch();
            
            // [사용자 수정] 모든 작업이 성공했을 때만 DB에 최종 반영
            conn.commit(); 
            System.out.println("주문 정보가 DB에 안전하게 기록되었습니다.");
            
        } catch (SQLException e) {
            System.out.println("[오류] 주문 처리 중 에러 발생, 작업이 취소되었습니다.");
            e.printStackTrace();
        }
    }
}