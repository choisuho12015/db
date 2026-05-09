package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.market.cart.Cart;
import com.market.cart.CartItem;

public class CartSearchPage extends JPanel {

    public CartSearchPage(JPanel parent, Cart cart) {

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JLabel lblTitle = new JLabel("도서명 검색");
        JTextField txtSearch = new JTextField(12);
        JTextArea areaResult = new JTextArea(8, 28);

        areaResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResult);

        JButton btnSearch = new JButton("검색");

        add(lblTitle);
        add(txtSearch);
        add(btnSearch);
        add(scrollPane);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String keyword = txtSearch.getText().trim();
                areaResult.setText("");

                boolean match = false;

                for (CartItem item : cart.getmCartItem()) {

                    if (item.getItemBook().getName().contains(keyword)) {
                        areaResult.append(item.getItemBook().getName() +
                                " | 수량: " + item.getQuantity() +
                                " | 합계: " + item.getTotalPrice() + "원\n");
                        match = true;
                    }
                }
                if (!match) {
                    areaResult.setText("검색 결과가 없습니다.");
                }
            }
        });
    }
}
