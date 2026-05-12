package com.market.page;

import javax.swing.*;
import java.awt.*;
import com.market.cart.Cart;
import com.market.member.UserInIt;
import java.awt.event.ActionEvent;

public class CartShippingPage extends JPanel {
	Cart mCart;
	JPanel shippingPanel;
	JPanel radioPanel;

	public CartShippingPage(JPanel panel, Cart cart) {
		this.mCart = cart;
		Font ft = new Font("함초롬돋움", Font.BOLD, 15);
		setLayout(null);

		Rectangle rect = panel.getBounds();
		setPreferredSize(rect.getSize());

		radioPanel = new JPanel();
		radioPanel.setBounds(300, 0, 700, 50);
		radioPanel.setLayout(new FlowLayout());
		add(radioPanel);
		
		JLabel radioLabel = new JLabel("배송받을 분은 고객정보와 같습니까?");
		radioLabel.setFont(ft);
		JRadioButton radioOk = new JRadioButton("예");
		radioOk.setFont(ft);
		JRadioButton radioNo = new JRadioButton("아니오");
		radioNo.setFont(ft);
		radioPanel.add(radioLabel);
		radioPanel.add(radioOk);
		radioPanel.add(radioNo);

		shippingPanel = new JPanel();
		shippingPanel.setBounds(200, 50, 700, 600);
		shippingPanel.setLayout(null);
		add(shippingPanel);

		radioOk.setSelected(true);
		UserShippingInfo(true);

		radioOk.addActionListener(e -> {
			if (radioOk.isSelected()) {
				radioNo.setSelected(false);
				shippingPanel.removeAll();
				UserShippingInfo(true);
				shippingPanel.revalidate();
				shippingPanel.repaint();
			}
		});

		radioNo.addActionListener(e -> {
			if (radioNo.isSelected()) {
				radioOk.setSelected(false);
				shippingPanel.removeAll();
				UserShippingInfo(false);
				shippingPanel.revalidate();
				shippingPanel.repaint();
			}
		});
	}

	public void UserShippingInfo(boolean select) {
		Font ft = new Font("함초롬돋움", Font.BOLD, 15);

		// 고객명
		JPanel namePanel = new JPanel();
		namePanel.setBounds(0, 30, 700, 50);
		namePanel.add(new JLabel("고객명 : "));
		JTextField nameText = new JTextField(15);
		if (select && UserInIt.getmUser() != null) {
			nameText.setText(UserInIt.getmUser().getName());
			nameText.setBackground(Color.LIGHT_GRAY);
		}
		namePanel.add(nameText);
		shippingPanel.add(namePanel);

		// 연락처
		JPanel phonePanel = new JPanel();
		phonePanel.setBounds(0, 80, 700, 50);
		phonePanel.add(new JLabel("연락처 : "));
		JTextField phoneText = new JTextField(15);
		if (select && UserInIt.getmUser() != null) {
			phoneText.setText(UserInIt.getmUser().getPhone());
			phoneText.setBackground(Color.LIGHT_GRAY);
		}
		phonePanel.add(phoneText);
		shippingPanel.add(phonePanel);

		// 배송지
		JPanel addressPanel = new JPanel();
		addressPanel.setBounds(0, 130, 700, 50);
		addressPanel.add(new JLabel("배송지 : "));
		JTextField addressText = new JTextField(15);
		addressPanel.add(addressText);
		shippingPanel.add(addressPanel);

		// 최종 금액 및 주문 버튼
		int initialTotal = mCart.getTotalPrice();
		final int[] finalPrice = {initialTotal}; 
		
		JPanel totalAmountPanel = new JPanel();
		totalAmountPanel.setBounds(0, 240, 700, 50);
		JLabel totalLabel = new JLabel("최종 결제 금액 : " + initialTotal + "원");
		totalLabel.setFont(new Font("함초롬돋움", Font.BOLD, 18));
		totalAmountPanel.add(totalLabel);
		shippingPanel.add(totalAmountPanel);

		JButton orderButton = new JButton("주문완료");
		orderButton.setFont(ft);
		orderButton.setBounds(300, 320, 100, 30);
		shippingPanel.add(orderButton);

		orderButton.addActionListener(e -> {
			if (UserInIt.getmUser() != null) {
				UserInIt.getmUser().setAddress(addressText.getText());
			}
			shippingPanel.removeAll();
			shippingPanel.add(new CartOrderBillPage(shippingPanel, mCart, finalPrice[0]));
			mCart.deleteBook(); 
			shippingPanel.revalidate();
			shippingPanel.repaint();
		});
	}
}