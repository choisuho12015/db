package com.market.bookitem;

import java.util.ArrayList;
import java.sql.*;
import com.market.database.DBConnection;

public class BookInIt {
    private static ArrayList<Book> mBookList;
    private static int mTotalBook = 0;

    public static void init() {
        mBookList = new ArrayList<Book>();
        // [사용자 수정] 텍스트 파일이나 고정 데이터 대신 DB에서 데이터를 가져오도록 로직 변경
        setDBToBookList(mBookList);
        mTotalBook = mBookList.size();
    }

    /**
     * [사용자 수정] MySQL의 book 테이블에 저장된 모든 도서 정보를 가져와 
     * 프로그램 메모리(ArrayList)에 담아주는 핵심 연동 메서드 구현
     */
    public static void setDBToBookList(ArrayList<Book> booklist) {
        String sql = "SELECT * FROM book";

        // [사용자 수정] DBConnection을 통해 연결을 생성하고 SELECT 쿼리를 실행하여 결과(ResultSet)를 반복문으로 처리
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // [사용자 수정] DB 컬럼명에 맞춰 데이터를 추출하고 Book 객체를 생성하여 리스트에 추가
                Book bookitem = new Book(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("author"),
                    rs.getString("description"),
                    rs.getString("category"),
                    rs.getString("release_date")
                );
                booklist.add(bookitem);
            }
        } catch (SQLException e) {
            System.out.println("데이터 로딩 오류: " + e.getMessage());
        }
    }

    public static ArrayList<Book> getmBookList() {
        if (mBookList == null) init();
        return mBookList;
    }

    public static int getmTotalBook() {
        // [사용자 수정] DB에서 로드된 리스트의 크기를 실시간으로 반영하여 전체 도서 수 반환
        return (mBookList != null) ? mBookList.size() : 0;
    }
}