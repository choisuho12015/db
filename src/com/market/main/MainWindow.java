package com.market.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.market.page.GuestInfoPage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.market.cart.Cart;
import com.market.member.UserInIt;
import com.market.bookitem.BookInIt;
import com.market.page.CartAddItemPage;
import com.market.page.CartItemListPage;
import com.market.page.CartShippingPage;
import com.market.page.AdminLoginDialog;
import com.market.page.AdminPage;
import com.market.page.CartSearchPage;

public class MainWindow extends JFrame {
	static Cart mCart;
	static JPanel mMenuPanel, mPagePanel;

	public MainWindow(String title, int x, int y, int width, int height) {

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

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false); // 현재 프레임 감추기
				new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
			}
		});
	}

	private void menuIntroduction() {
		mCart = new Cart();
		Font ft;
		ft = new Font("함초롬돋움", Font.BOLD, 15);
	}

	private void initMenu() {
		Font ft;
		ft = new Font("함초롬돋움", Font.BOLD, 15);

		JMenuBar menuBar = new JMenuBar();

		JMenu menu01 = new JMenu("고객");
		menu01.setFont(ft);
		JMenuItem item01 = new JMenuItem("고객 정보");
		JMenuItem item11 = new JMenuItem("로그아웃");
		JMenuItem item12 = new JMenuItem("종료");
		menu01.add(item01);
		menu01.add(item11);
		menu01.add(item12);
		menuBar.add(menu01);

		JMenu menu02 = new JMenu("상품");
		menu02.setFont(ft);
		JMenuItem item02 = new JMenuItem("상품 목록");
		menu02.add(item02);
		menuBar.add(menu02);

		JMenu menu03 = new JMenu("장바구니");
		menu03.setFont(ft);
		JMenuItem item03 = new JMenuItem("항목 추가");
		JMenuItem item04 = new JMenuItem("항목 목록");
		JMenuItem item06 = new JMenuItem("장바구니 비우기");
		JMenuItem item08 = new JMenuItem("장바구니 검색");
		menu03.add(item03);
		menu03.add(item04);
		menu03.add(item06);
		menu03.add(item08);
		menuBar.add(menu03);

		JMenu menu04 = new JMenu("주문");
		menu04.setFont(ft);
		JMenuItem item07 = new JMenuItem("주문하기");
		menu04.add(item07);
		menuBar.add(menu04);
		setJMenuBar(menuBar);
		
		JMenu menu05 = new JMenu("관리자");
		menu05.setFont(ft);
		JMenuItem item00 = new JMenuItem("로그인");
		menu05.add(item00);
		menuBar.add(menu05);

		item01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mPagePanel.removeAll();
				mPagePanel.add("고객 정보 확인 ", new GuestInfoPage(mPagePanel));
				add(mPagePanel);
				mPagePanel.revalidate();
			}
		});

		item02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mPagePanel.removeAll();
				BookInIt.init();
				mPagePanel.add("장바구니에 항목 추가하기", new CartAddItemPage(mPagePanel, mCart));
				add(mPagePanel);
				mPagePanel.revalidate();
			}
		});
		
		item03.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mPagePanel.removeAll();
				BookInIt.init();
				mPagePanel.add("장바구니에 항목 추가하기", new CartAddItemPage(mPagePanel, mCart));
				mPagePanel.revalidate();
				mPagePanel.repaint();
			}
		});
		
		item04.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (mCart.mCartCount == 0)
					JOptionPane.showMessageDialog(item04, "장바구니에 항목이 없습니다", "장바구니 항목 수량 줄이기", JOptionPane.ERROR_MESSAGE);
				else {
					mPagePanel.removeAll();
					CartItemListPage cartList = new CartItemListPage(mPagePanel, mCart);
				}
				mPagePanel.add("장바구니에 항목수량 줄이기", new CartItemListPage(mPagePanel, mCart));
				
				mPagePanel.revalidate();
				mPagePanel.repaint();
			}
			
		});
		
		item06.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (mCart.mCartCount == 0)
					JOptionPane.showMessageDialog(item06, "장바구니에 항목이 없습니다", "장바구니 비우기", JOptionPane.ERROR_MESSAGE);
				else {
					mPagePanel.removeAll();
					menuCartClear(item06);
					mPagePanel.add("장바구니 비우기", new CartItemListPage(mPagePanel, mCart));
					mPagePanel.revalidate();
					mPagePanel.repaint();
				}
			}
		});
		
		item07.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if (mCart.mCartCount == 0)
					JOptionPane.showMessageDialog(item07, "주문을 시도했으나 장바구니에 항목이 없습니다", "주문처리", JOptionPane.ERROR_MESSAGE);
				else {

					mPagePanel.removeAll();
					mPagePanel.add("주문 배송지", new CartShippingPage(mPagePanel, mCart));
					mPagePanel.revalidate();
					mPagePanel.repaint();
				}
			}

		});
		
		item08.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mCart.mCartCount == 0)
					JOptionPane.showMessageDialog(item08, "장바구니에 항목이 없습니다", "검색하기", JOptionPane.ERROR_MESSAGE);
				else {
					mPagePanel.removeAll();
					mPagePanel.add(new CartSearchPage(mPagePanel, mCart));
					mPagePanel.revalidate();
					mPagePanel.repaint();
				}
			}

		});

		item11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mPagePanel.removeAll();
				setVisible(false);
				new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
				add(mPagePanel);
				mPagePanel.revalidate();
			}
		});
		
		item12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int select = JOptionPane.showConfirmDialog(item12, "쇼핑몰을 종료하겠습니까? ");
				if (select == 0) {
					System.exit(1);
				}
			}
		});
		
		item00.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminLoginDialog adminDialog;
				JFrame frame = new JFrame();
				adminDialog = new AdminLoginDialog(frame, "관리자 로그인");
				adminDialog.setVisible(true);
				if (adminDialog.isLogin) {
					mPagePanel.removeAll();
					mPagePanel.add("관리자", new AdminPage(mPagePanel));
					mPagePanel.revalidate();
					mPagePanel.repaint();
				}
			}
		});
	}

	private void menuCartClear(JButton button) {

		if (mCart.mCartCount == 0)
			JOptionPane.showMessageDialog(button, "장바구니의 항목이 없습니다");
		else {

			int select = JOptionPane.showConfirmDialog(button, "장바구니의 모든 항목을 삭제하겠습니까? ");

			if (select == 0) {
				mCart.deleteBook();
				JOptionPane.showMessageDialog(button, "장바구니의 모든 항목을 삭제했습니다");
			}
		}
	}
	
	private void menuCartClear(JMenuItem button) {

		if (mCart.mCartCount == 0)
			JOptionPane.showMessageDialog(button, "장바구니의 항목이 없습니다");
		else {

			int select = JOptionPane.showConfirmDialog(button, "장바구니의 모든 항목을 삭제하겠습니까? ");

			if (select == 0) {
				mCart.deleteBook();
				JOptionPane.showMessageDialog(button, "장바구니의 모든 항목을 삭제했습니다");
			}
		}
	}
}