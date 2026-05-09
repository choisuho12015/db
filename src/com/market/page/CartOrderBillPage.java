package com.market.page;

import javax.swing.*;
import com.market.bookitem.BookInIt;
import com.market.cart.Cart;
import com.market.member.UserInIt;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CartOrderBillPage extends JPanel {

	Cart mCart;
	JPanel shippingPanel;
	int mFinalPrice; // 최종 결제 금액을 저장할 변수

	// 생성자에 finalPrice 매개변수 추가
	public CartOrderBillPage(JPanel panel, Cart cart, int finalPrice) {
		this.mCart = cart;
		this.mFinalPrice = finalPrice; // 전달받은 최종 금액 저장

		Font ft = new Font("함초롬돋움", Font.BOLD, 15);
		setLayout(null);

		Rectangle rect = panel.getBounds();
		setPreferredSize(rect.getSize());

		shippingPanel = new JPanel();
		shippingPanel.setBounds(0, 0, 700, 600); // 높이 넉넉히 조절
		shippingPanel.setLayout(null);
		panel.add(shippingPanel);
		
		printBillInfo(UserInIt.getmUser().getName(), String.valueOf(UserInIt.getmUser().getPhone()), UserInIt.getmUser().getAddress());
	}

	public void printBillInfo(String name, String phone, String address) {
		Font ft = new Font("함초롬돋움", Font.BOLD, 15);

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = formatter.format(date);

		JPanel panel01 = new JPanel();
		panel01.setBounds(0, 0, 500, 30);
		JLabel label01 = new JLabel("---------------------배송 받을 고객 정보-----------------------");
		label01.setFont(ft);
		panel01.add(label01);
		shippingPanel.add(panel01);

		JPanel panel02 = new JPanel();
		panel02.setBounds(0, 30, 500, 30);
		JLabel label02 = new JLabel("고객명 : " + name + "             연락처 :      " + phone);
		label02.setFont(ft);
		panel02.add(label02);
		shippingPanel.add(panel02);

		JPanel panel03 = new JPanel();
		panel03.setBounds(0, 60, 500, 30);
		JLabel label03 = new JLabel("배송지 : " + address + "                 발송일 :       " + strDate);
		label03.setFont(ft);
		panel03.add(label03);
		shippingPanel.add(panel03);

		// 영수증 상품 목록 패널
		JPanel printPanel = new JPanel();
		printPanel.setLayout(new BoxLayout(printPanel, BoxLayout.Y_AXIS)); // 세로 정렬을 위해 BoxLayout 권장
		printPanel.setBounds(0, 100, 500, 400);
		printCart(printPanel);
		shippingPanel.add(printPanel);
	}

	public void printCart(JPanel panel) {
		Font ft = new Font("함초롬돋움", Font.BOLD, 12);
		Font ftBold = new Font("함초롬돋움", Font.BOLD, 15);

		panel.add(new JLabel("      장바구니 상품 목록 :"));
		panel.add(new JLabel("------------------------------------------------------------"));
		panel.add(new JLabel("      도서ID           |        수량           |      합계         "));
		panel.add(new JLabel("------------------------------------------------------------"));

		for (int i = 0; i < mCart.mCartItem.size(); i++) {
			JLabel itemLabel = new JLabel("    " + mCart.mCartItem.get(i).getBookID() + "                 "
					+ mCart.mCartItem.get(i).getQuantity() + "                "
					+ mCart.mCartItem.get(i).getTotalPrice() + "원");
			itemLabel.setFont(ft);
			panel.add(itemLabel);
		}

		panel.add(new JLabel("------------------------------------------------------------"));

		// 원가 계산 (할인 전 금액)
		int totalBeforeDiscount = mCart.getTotalPrice();
		int discountAmount = totalBeforeDiscount - mFinalPrice;

		// 할인 정보 표시
		if (discountAmount > 0) {
			JLabel discountLabel = new JLabel("      쿠폰 할인 금액 : -" + discountAmount + "원");
			discountLabel.setFont(ftBold);
			discountLabel.setForeground(Color.RED);
			panel.add(discountLabel);
		}

		// 최종 금액 표시
		JLabel finalLabel = new JLabel("      최종 결제 금액 : " + mFinalPrice + "원");
		finalLabel.setFont(new Font("함초롬돋움", Font.BOLD, 20)); // 강조를 위해 크기 키움
		panel.add(finalLabel);
	}
}