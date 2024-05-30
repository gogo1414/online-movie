package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Customer;

public class MainMenuScreen extends JFrame {
    private Customer customer;

    public MainMenuScreen(Customer customer) {
        this.customer = customer;

        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton movieListButton = new JButton("영화 조회");
        movieListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieListScreen().setVisible(true);
                dispose();
            }
        });

        JButton bookMovieButton = new JButton("영화 예매");
        bookMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieListScreen().setVisible(true); // 예매 화면으로 이동할 수 있도록 수정
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

        panel.add(movieListButton);
        panel.add(bookMovieButton);
        panel.add(bookingHistoryButton);

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
