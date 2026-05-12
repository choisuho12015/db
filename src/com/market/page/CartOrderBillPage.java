package com.market.page;

import javax.swing.*;
import com.market.cart.Cart;
import com.market.member.UserInIt;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CartOrderBillPage extends JPanel {
	Cart mCart;
	JPanel shippingPanel;
	int mFinalPrice;

	public CartOrderBillPage(JPanel panel, Cart cart, int finalPrice) {
		this.mCart = cart;
		this.mFinalPrice = finalPrice;
		setLayout(null);

		Rectangle rect = panel.getBounds();
		setPreferredSize(rect.getSize());

		shippingPanel = new JPanel();
		shippingPanel.setBounds(0, 0, rect.width, rect.height);
		shippingPanel.setLayout(null);
		add(shippingPanel);
		
		if (UserInIt.getmUser() != null) {
			printBillInfo(UserInIt.getmUser().getName(), 
					      UserInIt.getmUser().getPhone(), 
					      UserInIt.getmUser().getAddress());
		}
	}

	public void printBillInfo(String name, String phone, String address) {
		Font ft = new Font("함초롬돋움", Font.BOLD, 15);
		String strDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

		JLabel label01 = new JLabel("---------------------배송 받을 고객 정보-----------------------");
		label01.setBounds(50, 20, 500, 30); label01.setFont(ft);
		shippingPanel.add(label01);

		JLabel label02 = new JLabel("고객명 : " + name + "      연락처 : " + phone);
		label02.setBounds(50, 50, 500, 30); label02.setFont(ft);
		shippingPanel.add(label02);

		JLabel label03 = new JLabel("배송지 : " + address + "      발송일 : " + strDate);
		label03.setBounds(50, 80, 500, 30); label03.setFont(ft);
		shippingPanel.add(label03);

		JPanel printPanel = new JPanel();
		printPanel.setLayout(new BoxLayout(printPanel, BoxLayout.Y_AXIS));
		printPanel.setBounds(50, 120, 500, 350);
		printCart(printPanel);
		shippingPanel.add(printPanel);
	}

	public void printCart(JPanel panel) {
		panel.add(new JLabel("      장바구니 상품 목록 :"));
		panel.add(new JLabel("------------------------------------------------------------"));
		for (int i = 0; i < mCart.mCartItem.size(); i++) {
			panel.add(new JLabel("   " + mCart.mCartItem.get(i).getBookID() + " | "
					+ mCart.mCartItem.get(i).getQuantity() + "개 | "
					+ mCart.mCartItem.get(i).getTotalPrice() + "원"));
		}
		panel.add(new JLabel("------------------------------------------------------------"));
		JLabel finalLabel = new JLabel("      최종 결제 금액 : " + mFinalPrice + "원");
		finalLabel.setFont(new Font("함초롬돋움", Font.BOLD, 18));
		panel.add(finalLabel);
	}
}