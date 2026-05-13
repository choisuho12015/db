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

		this.mCart = cart;
		
		radioOk.setSelected(true);
		radioNo.setSelected(false);
		UserShippingInfo(true);

		radioOk.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (radioOk.isSelected()) {
					shippingPanel.removeAll();
					UserShippingInfo(true);
					shippingPanel.revalidate();
					shippingPanel.repaint();
					radioNo.setSelected(false);
				}
			}
		});

		radioNo.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (radioNo.isSelected()) {
					shippingPanel.removeAll();
					UserShippingInfo(false);
					shippingPanel.revalidate();
					shippingPanel.repaint();
					radioOk.setSelected(false);
				}
			}
		});
	}

	public void UserShippingInfo(boolean select) {
		Font ft = new Font("함초롬돋움", Font.BOLD, 15);

		// 고객명
		JPanel namePanel = new JPanel();
		namePanel.setBounds(0, 30, 700, 50);
		JLabel nameLabel = new JLabel("고객명 : ");
		nameLabel.setFont(ft);
		namePanel.add(nameLabel);
		JTextField nameLabel2 = new JTextField(15);
		nameLabel2.setFont(ft);
		if (select) {
			nameLabel2.setBackground(Color.LIGHT_GRAY);
			nameLabel2.setText(UserInIt.getmUser().getName());
		}
		namePanel.add(nameLabel2);
		shippingPanel.add(namePanel);

		// 연락처
		JPanel phonePanel = new JPanel();
		phonePanel.setBounds(0, 80, 700, 50);
		JLabel phoneLabel = new JLabel("연락처 : ");
		phoneLabel.setFont(ft);
		phonePanel.add(phoneLabel);
		JTextField phoneLabel2 = new JTextField(15);
		phoneLabel2.setFont(ft);
		if (select) {
			phoneLabel2.setBackground(Color.LIGHT_GRAY);
			phoneLabel2.setText(String.valueOf(UserInIt.getmUser().getPhone()));
		}
		phonePanel.add(phoneLabel2);
		shippingPanel.add(phonePanel);

		// 배송지
		JPanel addressPanel = new JPanel();
		addressPanel.setBounds(0, 130, 700, 50);
		JLabel label = new JLabel("배송지 : ");
		label.setFont(ft);
		addressPanel.add(label);
		JTextField addressText = new JTextField(15);
		addressText.setFont(ft);
		addressPanel.add(addressText);
		shippingPanel.add(addressPanel);

		// --- 쿠폰 영역 ---
		JPanel couponPanel = new JPanel();
		couponPanel.setBounds(0, 190, 700, 50);
		JLabel couponLabel = new JLabel("혜택 : ");
		couponLabel.setFont(ft);
		couponPanel.add(couponLabel);
		JButton couponButton = new JButton("10% 할인 쿠폰 적용");
		couponButton.setFont(ft);
		JLabel discountLabel = new JLabel("할인액: 0원");
		discountLabel.setFont(ft);
		discountLabel.setForeground(Color.RED);
		couponPanel.add(couponButton);
		couponPanel.add(discountLabel);
		shippingPanel.add(couponPanel);

		// --- 금액 표시 영역 ---
		JPanel totalAmountPanel = new JPanel();
		totalAmountPanel.setBounds(0, 240, 700, 50);
		int initialTotal = mCart.getTotalPrice(); // Cart 클래스에서 새로 만든 메서드 호출
		final int[] finalPrice = {initialTotal}; // 할인된 금액 저장용 배열
		
		JLabel totalLabel = new JLabel("최종 결제 금액 : " + initialTotal + "원");
		totalLabel.setFont(new Font("함초롬돋움", Font.BOLD, 18));
		totalAmountPanel.add(totalLabel);
		shippingPanel.add(totalAmountPanel);

		// 쿠폰 이벤트
		couponButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int discount = (int) (initialTotal * 0.1);
				finalPrice[0] = initialTotal - discount;
				discountLabel.setText("할인액: -" + discount + "원");
				totalLabel.setText("최종 결제 금액 : " + finalPrice[0] + "원");
				JOptionPane.showMessageDialog(null, "쿠폰이 적용되었습니다!");
				couponButton.setEnabled(false);
			}
		});

		// 주문 완료 버튼
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 320, 700, 100);
		JButton orderButton = new JButton("주문완료");
		orderButton.setFont(ft);
		buttonPanel.add(orderButton);
		shippingPanel.add(buttonPanel);

		orderButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				UserInIt.getmUser().setAddress(addressText.getText());
				radioPanel.removeAll();
				radioPanel.revalidate();
				radioPanel.repaint();
				
				shippingPanel.removeAll();
				// 영수증 페이지로 이동
				shippingPanel.add("주문 배송지", new CartOrderBillPage(shippingPanel, mCart, finalPrice[0]));
				
				mCart.deleteBook(); // 주문 완료 후 장바구니 비우기
				shippingPanel.revalidate();
				shippingPanel.repaint();
			}
		});
	}
}