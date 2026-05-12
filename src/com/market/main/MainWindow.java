package com.market.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.market.cart.Cart;
import com.market.member.UserInIt;
import com.market.bookitem.BookInIt;
import com.market.page.*;

public class MainWindow extends JFrame {
    static Cart mCart;
    static JPanel mMenuPanel, mPagePanel;
    
    // ✅ 주문 시 이메일 정보가 필요하므로 static으로 관리 (CartShippingPage 등에서 접근 가능)
    public static String currentUserEmail; 

    public MainWindow(String title, int x, int y, int width, int height) {
        // ✅ [수정 포인트] UserInIt의 getter를 통해 안전하게 이메일 정보를 가져옴
        if (UserInIt.getmUser() != null) {
            currentUserEmail = UserInIt.getmUser().getEmail();
            System.out.println("현재 로그인 유저 이메일: " + currentUserEmail);
        } else {
            currentUserEmail = "guest@example.com"; // 유저 정보가 없을 경우 대비
        }

        initContainer(title, x, y, width, height);
        initMenu();

        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());
    }

    private void initContainer(String title, int x, int y, int width, int height) {
        setTitle(title);
        setBounds(x, y, width, height);
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2);

        mMenuPanel = new JPanel();
        mMenuPanel.setBounds(0, 20, width, 130);
        menuIntroduction();
        add(mMenuPanel);

        mPagePanel = new JPanel();
        mPagePanel.setBounds(0, 150, width, height);
        add(mPagePanel);

        // 창을 닫으면 다시 로그인 창(GuestWindow)으로 돌아감
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(false);
                new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
            }
        });
    }

    private void menuIntroduction() {
        mCart = new Cart();
        // 기타 초기화 로직 (필요 시 추가)
    }

    private void initMenu() {
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);
        JMenuBar menuBar = new JMenuBar();

        // [메뉴 구성]
        JMenu menu01 = new JMenu("고객");
        JMenuItem item01 = new JMenuItem("고객 정보 확인");
        JMenuItem item11 = new JMenuItem("로그아웃");
        JMenuItem item12 = new JMenuItem("종료");
        menu01.add(item01); menu01.add(item11); menu01.add(item12);

        JMenu menu02 = new JMenu("상품");
        JMenuItem item02 = new JMenuItem("상품 목록 확인");

        JMenu menu03 = new JMenu("장바구니");
        JMenuItem item03 = new JMenuItem("항목 추가");
        JMenuItem item04 = new JMenuItem("항목 목록");
        JMenuItem item06 = new JMenuItem("장바구니 비우기");
        JMenuItem item08 = new JMenuItem("장바구니 검색");
        menu03.add(item03); menu03.add(item04); menu03.add(item06); menu03.add(item08);

        JMenu menu04 = new JMenu("주문");
        JMenuItem item07 = new JMenuItem("주문하기");
        menu04.add(item07);

        JMenu menu05 = new JMenu("관리자");
        JMenuItem item00 = new JMenuItem("관리자 로그인");
        menu05.add(item00);

        menuBar.add(menu01); menuBar.add(menu02); menuBar.add(menu03); 
        menuBar.add(menu04); menuBar.add(menu05);
        setJMenuBar(menuBar);

        // --- 이벤트 리스너 (람다식 활용) ---

        // 고객 정보
        item01.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("고객 정보 확인", new GuestInfoPage(mPagePanel));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

        // 상품 목록 및 항목 추가
        ActionListener goToAddPage = e -> {
            mPagePanel.removeAll();
            BookInIt.init(); // DB 연동 시 최신 목록 로드
            mPagePanel.add("장바구니에 항목 추가하기", new CartAddItemPage(mPagePanel, mCart));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        };
        item02.addActionListener(goToAddPage);
        item03.addActionListener(goToAddPage);

        // 장바구니 목록
        item04.addActionListener(e -> {
            if (mCart.mCartCount == 0) {
                JOptionPane.showMessageDialog(null, "장바구니에 항목이 없습니다.");
            } else {
                mPagePanel.removeAll();
                mPagePanel.add("장바구니 항목 목록", new CartItemListPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        // 장바구니 비우기
        item06.addActionListener(e -> {
            if (mCart.mCartCount == 0) {
                JOptionPane.showMessageDialog(null, "장바구니가 이미 비어 있습니다.");
            } else {
                if (JOptionPane.showConfirmDialog(null, "장바구니를 모두 비우시겠습니까?") == 0) {
                    mCart.deleteBook();
                    mPagePanel.removeAll();
                    mPagePanel.add("장바구니 항목 목록", new CartItemListPage(mPagePanel, mCart));
                    mPagePanel.revalidate();
                    mPagePanel.repaint();
                }
            }
        });

        // 주문하기
        item07.addActionListener(e -> {
            if (mCart.mCartCount == 0) {
                JOptionPane.showMessageDialog(null, "장바구니가 비어 있어 주문할 수 없습니다.");
            } else {
                mPagePanel.removeAll();
                // ✅ 이 페이지 내에서 MainWindow.currentUserEmail을 사용하여 DB에 주문을 저장함
                mPagePanel.add("주문 배송지", new CartShippingPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        // 로그아웃
        item11.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?") == 0) {
                currentUserEmail = null;
                setVisible(false);
                new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
            }
        });

        // 종료
        item12.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "종료하시겠습니까?") == 0) System.exit(0);
        });

        // 관리자 로그인
        item00.addActionListener(e -> {
            AdminLoginDialog adminDialog = new AdminLoginDialog(this, "관리자 로그인");
            adminDialog.setVisible(true);
            if (adminDialog.isLogin) {
                mPagePanel.removeAll();
                mPagePanel.add("관리자", new AdminPage(mPagePanel));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });
    }
}