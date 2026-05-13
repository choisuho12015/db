package com.market.cart;

import com.market.bookitem.Book;

public class CartItem {

	// private String[] itemBook = new String[7];
	// [사용자 수정] 기존 String 배열 방식에서 Book 객체를 직접 참조하도록 변경
	private Book itemBook;
	private String bookID;
	private int quantity;
	private int totalPrice;

	public CartItem() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public CartItem(String[] book) { this.itemBook = book; this.bookID = book[0];
	 * this.quantity = 1; updateTotalPrice(); }
	 * * public String[] getItemBook() { return itemBook; }
	 * * public void setItemBook(String[] itemBook) { this.itemBook = itemBook; }
	 */
    
	// [사용자 수정] Book 객체를 매개변수로 받아 도서 정보와 ID를 세팅하는 생성자로 변경
	public CartItem(Book booklist) {
		this.itemBook = booklist;
		this.bookID = booklist.getBookId();
		this.quantity = 1;
		updateTotalPrice();
	}

	public Book getItemBook() {
		return itemBook;
	}

	public void setItemBook(Book itemBook) {
		this.itemBook = itemBook;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getBookID() {
		return bookID;
	}

	public void setBookID(String bookID) {
		this.bookID = bookID;
		this.updateTotalPrice();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		this.updateTotalPrice();
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void updateTotalPrice() {
		// totalPrice = Integer.parseInt(this.itemBook[2]) * this.quantity;
		// [사용자 수정] 배열 인덱스 접근 대신 Book 객체의 메서드(getUnitPrice)를 사용하여 금액 계산
		totalPrice = this.itemBook.getUnitPrice() * this.quantity;
	}
}