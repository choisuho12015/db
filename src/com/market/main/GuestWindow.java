package com.market.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.market.member.UserInIt;
import com.market.database.DBConnection; // ✅ 추가

public class GuestWindow extends JFrame {

    public GuestWindow(String title, int x, int y, int width, int height) {
        initContainer(title, x, y, width, height);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());
    }

    private void initContainer(String title, int x, int y, int width, int height) {
        setTitle(title);
        setBounds(x, y, width, height);
        setLayout(null);

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2);

        JPanel userPanel = new JPanel();
        userPanel.setBounds(0, 100, 1000, 256);
        ImageIcon imageIcon = new ImageIcon("./images/user.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH));
        JLabel userLabel = new JLabel(imageIcon);
        userPanel.add(userLabel);
        add(userPanel);

        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(0, 380, 1000, 50);
        JLabel titleLabel = new JLabel("-- 구글 계정으로 로그인하여 시작하세요 --");
        titleLabel.setFont(ft);
        titleLabel.setForeground(Color.BLUE);
        titlePanel.add(titleLabel);
        add(titlePanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 450, 1000, 100);
        add(buttonPanel);

        ImageIcon googleIcon = new ImageIcon("./images/1.png");
        googleIcon.setImage(googleIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        JButton googleLoginButton = new JButton(" Google 계정으로 로그인", googleIcon);
        googleLoginButton.setFont(ft);
        googleLoginButton.setPreferredSize(new Dimension(300, 50));
        buttonPanel.add(googleLoginButton);

        googleLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    handleGoogleLogin();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "로그인 중 오류가 발생했습니다: " + ex.getMessage());
                }
            }
        });
    }

    private void handleGoogleLogin() throws Exception {
        File file = new File("src/client_secrets.json");
        if (!file.exists()) {
            throw new RuntimeException("client_secrets.json 파일을 찾을 수 없습니다.");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(), new FileReader(file));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                Arrays.asList(
                    "https://www.googleapis.com/auth/userinfo.email",
                    "https://www.googleapis.com/auth/userinfo.profile"
                ))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        if (credential.getAccessToken() != null) {
            URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + credential.getAccessToken());

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) response.append(scanner.nextLine());
            scanner.close();

            String json = response.toString();
            String name  = json.replaceAll(".*\"name\"\\s*:\\s*\"([^\"]+)\".*", "$1");
            String email = json.replaceAll(".*\"email\"\\s*:\\s*\"([^\"]+)\".*", "$1");

            // ✅ [수정] DB에 고객 정보 저장
            DBConnection.insertMember(name, email);

            JOptionPane.showMessageDialog(this, "구글 인증 및 DB 저장 성공!\n이름: " + name);

            UserInIt.init(name, email);
            dispose();
            new MainWindow("온라인 서점", 0, 0, 1000, 750);
        }
    }
}