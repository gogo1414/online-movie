package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.BookingDAO;
import dao.SeatDAO;
import dao.TicketDAO;
import model.Booking;
import model.Schedule;

public class SeatScreen extends JFrame {
    
    private Booking booking;
    
    private BookingDAO bookingDao;
    private TicketDAO ticketDao;
    private SeatDAO seatDao;
    
    private static final int ROW = 10;
    private static final int HEIGHT = 10;
    private static final int SEAT_PRICE = 10000;
    private static final int TOTAL_SEATS = 255;

    private String selectedSeat = null;

    private char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private int[] cols = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    
    private JButton selectedSeatButton = null;
    private JPanel mainPanel;
    private JPanel seatPanel;

    private JButton reservationButton;

    private JLabel selectedSeatLabel;
    private JLabel seatedCounterLabel = new JLabel("좌석 현황");
    private JLabel seatedCounterLabel2 = new JLabel("255 / " + TOTAL_SEATS);

    private JLabel totalPriceLabel;
    
    private JButton backButton = new JButton("Back");

    private JButton cardButton;
    private JButton cashButton;
    
    private String selectedMethod = null; // No default selected method
    private String currentDate = "2023-01-01";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    
    public SeatScreen(Schedule schedule) {
        bookingDao = new BookingDAO();
        ticketDao = new TicketDAO();

        setTitle("Seat Selection");
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        
        backButton.setBounds(760, 600, 100, 40);
        mainPanel.add(backButton);
        backButton.addActionListener(e -> {
            try {
                new BookingScreen().setVisible(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            dispose();
        });
        
        seatPanel = new JPanel();
        seatPanel.setBounds(30, 50, 600, 600);
        seatPanel.setBackground(Color.white);
        seatPanel.setLayout(new BorderLayout(5, 5));
        JLabel labelScreen = new JLabel("Screen", SwingConstants.CENTER);
        seatPanel.add(labelScreen, BorderLayout.NORTH);

        JPanel seatsGrid = new JPanel(new GridLayout(ROW + 1, HEIGHT + 1, 5, 5));
        seatPanel.add(seatsGrid, BorderLayout.CENTER);

        seatsGrid.add(new JLabel());  // Add an empty cell at the top-left corner

        // Add column labels
        for (int col : cols) {
            seatsGrid.add(new JLabel(String.valueOf(col), SwingConstants.CENTER));
        }
        
        // Add row labels and seat buttons
        for (int row = 0; row < ROW; row++) {
            seatsGrid.add(new JLabel(String.valueOf(rows[row]), SwingConstants.CENTER));
            for (int col = 0; col < HEIGHT; col++) {
                JButton seatButton = new JButton(rows[row] + "" + (col + 1));
                seatButton.setBackground(Color.gray);
                seatButton.setSize(10, 10);
                seatButton.addActionListener(new SeatButtonListener(rows[row], col + 1));
                seatsGrid.add(seatButton);
            }
        }

        mainPanel.add(seatPanel);

        reservationButton = new JButton("예매하기");
        reservationButton.setSize(100, 50);
        reservationButton.setLocation(650, 600);
        reservationButton.addActionListener(e -> {
            if (selectedSeat == null) {
                JOptionPane.showMessageDialog(SeatScreen.this, "No seats selected.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (selectedMethod == null) {
                JOptionPane.showMessageDialog(SeatScreen.this, "Please select a payment method.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(SeatScreen.this, "Reserved seat: " + selectedSeat, "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);
                
                makeDate();
                booking = new Booking(selectedMethod, 1, SEAT_PRICE, MainMenuScreen.customer.getCustomerID(), date);
                
                try {
                    bookingDao.addBooking(booking);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
                new MainMenuScreen(MainMenuScreen.customer).setVisible(true);
                dispose();
            }
        });
        
        mainPanel.add(reservationButton);

        selectedSeatLabel = new JLabel("선택된 좌석: None");
        selectedSeatLabel.setBounds(650, 500, 200, 30);
        mainPanel.add(selectedSeatLabel);

        seatedCounterLabel.setBounds(300, 670, 100, 100);
        mainPanel.add(seatedCounterLabel);

        seatedCounterLabel2.setBounds(300, 690, 100, 100);
        mainPanel.add(seatedCounterLabel2);

        totalPriceLabel = new JLabel("결제 금액: 0");
        totalPriceLabel.setBounds(650, 450, 150, 30);
        mainPanel.add(totalPriceLabel);

        // Add the payment method buttons
        cardButton = new JButton("Card");
        cardButton.setBounds(650, 550, 80, 30);
        cardButton.addActionListener(e -> {
            selectedMethod = "Card";
            cardButton.setBackground(Color.green);
            cashButton.setBackground(null);
        });
        mainPanel.add(cardButton);

        cashButton = new JButton("Cash");
        cashButton.setBounds(750, 550, 80, 30);
        cashButton.addActionListener(e -> {
            selectedMethod = "Cash";
            cashButton.setBackground(Color.green);
            cardButton.setBackground(null);
        });
        mainPanel.add(cashButton);

        add(mainPanel);
        setVisible(true);
    }
    
    public void makeDate() {
    	try {
        	date = dateFormat.parse(currentDate);
        } catch (ParseException err) {
        	err.printStackTrace();
        }
    }

    private class SeatButtonListener implements ActionListener {

        private String row;
        private int col;

        public SeatButtonListener(char row, int col) {
            this.row = String.valueOf(row);
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String seatNumber = row + col;
            if (selectedSeatButton != null) {
                selectedSeatButton.setBackground(Color.gray);
            }
            selectedSeat = seatNumber;
            selectedSeatButton = source;
            source.setBackground(Color.green);
            updateSelectedSeatLabel();
        }
    }

    private void updateSelectedSeatLabel() {
        if (selectedSeat == null) {
            selectedSeatLabel.setText("선택된 좌석: None");
            totalPriceLabel.setText("결제 금액: 0");
        } else {
            selectedSeatLabel.setText("선택된 좌석: " + selectedSeat);
            totalPriceLabel.setText("결제 금액: " + SEAT_PRICE);
        }
        seatedCounterLabel2.setText(TOTAL_SEATS - (selectedSeat != null ? 1 : 0) + " / " + TOTAL_SEATS);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeatScreen(null).setVisible(true));
    }
}
