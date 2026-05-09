package com.market.cart;

import java.util.ArrayList;
import com.market.bookitem.Book;

public class Cart implements CartInterface {

	public ArrayList<CartItem> mCartItem = new ArrayList<CartItem>();
	public static int mCartCount = 0;

	public Cart() {
	}

	// 장바구니 전체 금액을 계산하여 반환하는 메서드 추가
	public int getTotalPrice() {
		int total = 0;
		for (int i = 0; i < mCartItem.size(); i++) {
			total += mCartItem.get(i).getTotalPrice();
		}
		return total;
	}

	public void printBookList(ArrayList<Book> booklist) {
		for (int i = 0; i < booklist.size(); i++) {
			Book bookitem = booklist.get(i);
			System.out.print(bookitem.getBookId() + " | " + bookitem.getName() + " | " + bookitem.getUnitPrice() + " | "
					+ bookitem.getAuthor() + " | " + bookitem.getDescription() + " | " + bookitem.getCategory() + " | "
					+ bookitem.getReleaseDate());
			System.out.println("");
		}
	}

	public void insertBook(Book book) {
		CartItem bookitem = new CartItem(book);
		mCartItem.add(bookitem);
		mCartCount = mCartItem.size();
	}

	public void deleteBook() {
		mCartItem.clear();
		mCartCount = 0;
	}

	public void printCart() {
		System.out.println("장바구니 상품 목록 :");
		System.out.println("---------------------------------------------");
		System.out.println("    도서ID \t|     수량 \t|      합계");
		for (int i = 0; i < mCartItem.size(); i++) {
			System.out.print("    " + mCartItem.get(i).getBookID() + " \t| ");
			System.out.print("    " + mCartItem.get(i).getQuantity() + " \t| ");
			System.out.print("    " + mCartItem.get(i).getTotalPrice());
			System.out.println("  ");
		}
		System.out.println("---------------------------------------------");
	}

	public boolean isCartInBook(String bookId) {
		boolean flag = false;
		for (int i = 0; i < mCartItem.size(); i++) {
			if (bookId.equals(mCartItem.get(i).getBookID())) {
				mCartItem.get(i).setQuantity(mCartItem.get(i).getQuantity() + 1);
				flag = true;
			}
		}
		return flag;
	}

	public void removeCart(int numId) {
		mCartItem.remove(numId);
		mCartCount = mCartItem.size();
	}

	public void reduceCart(int numId) {
		CartItem item = mCartItem.get(numId);
		int quantity = item.getQuantity();
		if (quantity > 1) {
			item.setQuantity(quantity - 1);
		} else {
			mCartItem.remove(numId);
		}
		mCartCount = mCartItem.size();
	}

	public ArrayList<CartItem> getmCartItem() {
		return mCartItem;
	}

	public void setmCartItem(ArrayList<CartItem> mCartItem) {
		this.mCartItem = mCartItem;
	}
}