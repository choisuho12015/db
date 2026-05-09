package com.market.bookitem;

import java.util.ArrayList;
import java.sql.*;
import com.market.database.DBConnection;

public class BookInIt {
    private static ArrayList<Book> mBookList;
    private static int mTotalBook = 0;

    public static void init() {
        mBookList = new ArrayList<Book>();
        // DB 데이터를 리스트에 채움
        setDBToBookList(mBookList);
        mTotalBook = mBookList.size();
    }

    public static void setDBToBookList(ArrayList<Book> booklist) {
        String sql = "SELECT * FROM book";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
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
        return (mBookList != null) ? mBookList.size() : 0;
    }
}