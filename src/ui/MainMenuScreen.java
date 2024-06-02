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
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(movieListButton, gbc);
        
        gbc.gridy = 1;
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
