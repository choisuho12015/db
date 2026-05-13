package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import com.market.database.DBConnection;
import com.market.bookitem.BookInIt;

public class AdminPage extends JPanel {

    public AdminPage(JPanel panel) {
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);
        setLayout(null);

        Rectangle rect = panel.getBounds();
        setPreferredSize(rect.getSize());

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
        String strDate = formatter.format(date);

        // UI 구성 (ID, 이름, 가격 등 패널 설정)
        JPanel idPanel = new JPanel();
        idPanel.setBounds(100, 0, 700, 50);
        JLabel idLabel = new JLabel("도서ID : ");
        idLabel.setFont(ft);
        JLabel idTextField = new JLabel("ISBN" + strDate);
        idTextField.setFont(ft);
        idTextField.setPreferredSize(new Dimension(290, 50));
        idPanel.add(idLabel);
        idPanel.add(idTextField);
        add(idPanel);

        JPanel namePanel = new JPanel();
        namePanel.setBounds(100, 50, 700, 50);
        JLabel nameLabel = new JLabel("도서명 : ");
        nameLabel.setFont(ft);
        JTextField nameTextField = new JTextField(20);
        nameTextField.setFont(ft);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        add(namePanel);

        JPanel pricePanel = new JPanel();
        pricePanel.setBounds(100, 100, 700, 50);
        JLabel priceLabel = new JLabel("가    격 : ");
        priceLabel.setFont(ft);
        JTextField priceTextField = new JTextField(20);
        priceTextField.setFont(ft);
        pricePanel.add(priceLabel);
        pricePanel.add(priceTextField);
        add(pricePanel);

        JPanel authorPanel = new JPanel();
        authorPanel.setBounds(100, 150, 700, 50);
        JLabel authorLabel = new JLabel("저    자 : ");
        authorLabel.setFont(ft);
        JTextField authorTextField = new JTextField(20);
        authorTextField.setFont(ft);
        authorPanel.add(authorLabel);
        authorPanel.add(authorTextField);
        add(authorPanel);

        JPanel descPanel = new JPanel();
        descPanel.setBounds(100, 200, 700, 50);
        JLabel descLabel = new JLabel("설    명 : ");
        descLabel.setFont(ft);
        JTextField descTextField = new JTextField(20);
        descTextField.setFont(ft);
        descPanel.add(descLabel);
        descPanel.add(descTextField);
        add(descPanel);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setBounds(100, 250, 700, 50);
        JLabel categoryLabel = new JLabel("분    야 : ");
        categoryLabel.setFont(ft);
        JTextField categoryTextField = new JTextField(20);
        categoryTextField.setFont(ft);
        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryTextField);
        add(categoryPanel);

        JPanel datePanel = new JPanel();
        datePanel.setBounds(100, 300, 700, 50);
        JLabel dateLabel = new JLabel("출판일 : ");
        dateLabel.setFont(ft);
        JTextField dateTextField = new JTextField(20);
        dateTextField.setFont(ft);
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);
        add(datePanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(100, 350, 700, 50);
        add(buttonPanel);

        JButton okButton = new JButton("추가");
        okButton.setFont(ft);
        buttonPanel.add(okButton);

        okButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String[] writeBook = new String[7];
                writeBook[0] = idTextField.getText();
                writeBook[1] = nameTextField.getText();
                writeBook[2] = priceTextField.getText();
                writeBook[3] = authorTextField.getText();
                writeBook[4] = descTextField.getText();
                writeBook[5] = categoryTextField.getText();
                writeBook[6] = dateTextField.getText();

                // [사용자 수정] 입력받은 배열 데이터를 DB의 book 테이블에 실제로 저장하는 메서드 호출
                DBConnection.insertBook(writeBook);
                
                // [사용자 수정] DB에 저장된 최신 도서 목록을 불러와 프로그램 메모리(ArrayList 등)를 동기화
                BookInIt.init();

                JOptionPane.showMessageDialog(okButton, "데이터베이스에 저장되었습니다.");

                // [사용자 수정] 저장이 완료된 후 다음 입력을 위해 모든 텍스트 필드를 비우고 ID를 새로 생성
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
                idTextField.setText("ISBN" + formatter.format(date));
                nameTextField.setText("");
                priceTextField.setText("");
                authorTextField.setText("");
                descTextField.setText("");
                categoryTextField.setText("");
                dateTextField.setText("");
            }
        });

        JButton noButton = new JButton("취소");
        noButton.setFont(ft);
        buttonPanel.add(noButton);
        noButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // [사용자 수정] 취소 버튼 클릭 시 입력 중이던 모든 내용을 필드에서 제거
                nameTextField.setText("");
                priceTextField.setText("");
                authorTextField.setText("");
                descTextField.setText("");
                categoryTextField.setText("");
                dateTextField.setText("");
            }
        });
    }
}