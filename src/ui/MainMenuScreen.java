package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import find_catalog.FindMovieCatalog;
import model.Customer;

public class MainMenuScreen extends JFrame {
    public static Customer customer;

    public MainMenuScreen(Customer customer) {
        this.customer = customer;

        setTitle("메인 메뉴");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton movieListButton = new JButton("영화 조회 및 예매");
        movieListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindMovieCatalog().setVisible(true);
                dispose();
            }
        });

        JButton bookingHistoryButton = new JButton("예매 내역 확인");
        bookingHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookingHistoryScreen(customer).setVisible(true);
                dispose();
            }
        });

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(e -> {
            new AdminOrUserScreen().setVisible(true);
            dispose();
        });

        // Logout button at top right
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(logoutButton, gbc);

        // Reset gbc for other buttons
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        // Place buttons in the center slightly above their previous positions
        gbc.gridx = 0;
        gbc.gridy = 1; // Move up one row
        gbc.gridwidth = 2; // Span two columns to center the buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(movieListButton, gbc);

        gbc.gridy = 2; // Move up one row
        panel.add(bookingHistoryButton, gbc);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenuScreen(null).setVisible(true);
            }
        });
    }
}
