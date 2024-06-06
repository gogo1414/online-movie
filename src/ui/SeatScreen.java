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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.BookingDAO;
import dao.SeatDAO;
import dao.TheaterDAO;
import dao.TicketDAO;
import model.AllMovieInfo;
import model.Booking;
import model.Schedule;
import model.Seat;
import model.Theater;
import model.Ticket;

public class SeatScreen extends JFrame {
    
    private Booking booking;
    private Ticket ticket;
    private Theater theater;
    private List<Seat> seats;
    
    private BookingDAO bookingDao;
    private TheaterDAO theaterDao;
    private TicketDAO ticketDao;
    private SeatDAO seatDao;
    
    private static final int ROW = 10;
    private static final int HEIGHT = 10;
    private static final int SEAT_PRICE = 10000;
    private final int TOTAL_SEATS = 255;
    private int occupiedSeats = 0;

    private String selectedSeat = null;

    private char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private int[] cols = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    
    private JButton selectedSeatButton = null;
    private JPanel mainPanel;
    private JPanel seatPanel;

    private JButton reservationButton;

    private JLabel selectedSeatLabel;

    private JLabel totalPriceLabel;
    
    private JButton backButton = new JButton("뒤로가기");

    private JButton cardButton;
    private JButton cashButton;
    
    private String selectedMethod = null; // No default selected method
    private String currentDate = "2023-01-01";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    
    public SeatScreen(Schedule schedule) throws SQLException {
        bookingDao = new BookingDAO();
        ticketDao = new TicketDAO();
        theaterDao = new TheaterDAO();
        seatDao = new SeatDAO();
        
        theater = theaterDao.getTheaterByID(schedule.getTheaterID());
        seats = seatDao.getAllSeats(theater.getTheaterID());

        setTitle("Seat Selection");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        
        backButton.setBounds(810, 600, 100, 40);
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
        seatPanel.setBounds(0, 50, 700, 600);
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
               String seatID = rows[row] + "" + (col + 1);
               boolean isOccupied = seats.stream().anyMatch(seat -> seat.getSeatID().equals(seatID) && seat.isOccupied());
                
                JButton seatButton = new JButton(seatID);
                seatButton.setBackground(isOccupied ? Color.green : Color.gray);
                seatButton.setSize(10, 10);
                seatButton.setEnabled(!isOccupied);
                
                if(isOccupied)
                   occupiedSeats++;
                else
                   seatButton.addActionListener(new SeatButtonListener(rows[row], col + 1));
                seatsGrid.add(seatButton);
            }
        }

        mainPanel.add(seatPanel);

        reservationButton = new JButton("예매하기");
        reservationButton.setSize(100, 50);
        reservationButton.setLocation(700, 600);
        reservationButton.addActionListener(e -> {
            if (selectedSeat == null) {
                JOptionPane.showMessageDialog(SeatScreen.this, "좌석이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            } else if (selectedMethod == null) {
                JOptionPane.showMessageDialog(SeatScreen.this, "결제 방법을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(SeatScreen.this, "예약된 좌석: " + selectedSeat, "예약 완료", JOptionPane.INFORMATION_MESSAGE);
                
                if(AllMovieInfo.changeReservation==1) {
                	try {
						bookingDao.deleteBooking(AllMovieInfo.bookingID);
						AllMovieInfo.changeReservation=0;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	
                }
                
                
                makeDate();
                
                booking = new Booking(selectedMethod, 1, SEAT_PRICE, MainMenuScreen.customer.getCustomerID(), date);
                
                try {
                    booking = bookingDao.addBooking(booking);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
                try {
               seatDao.updateSeatOccupiedStatus(selectedSeat, schedule.getTheaterID(), true);
            } catch (SQLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
                
                ticket = new Ticket(
                      schedule.getScheduleID(),
                      schedule.getTheaterID(),
                      selectedSeat,
                      booking.getBookingID(),
                      1,
                      SEAT_PRICE,
                      SEAT_PRICE);
                
                try {
               ticketDao.addTicket(ticket);
            } catch (SQLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
                
                try {
               theaterDao.decreaseSeatCount(theater.getTheaterID());
            } catch (SQLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
                
                new MainMenuScreen(MainMenuScreen.customer).setVisible(true);
                dispose();
            }
        });
        
        mainPanel.add(reservationButton);

        selectedSeatLabel = new JLabel("선택된 좌석: None");
        selectedSeatLabel.setBounds(700, 500, 200, 30);
        mainPanel.add(selectedSeatLabel);

        totalPriceLabel = new JLabel("결제 금액: 0");
        totalPriceLabel.setBounds(700, 450, 150, 30);
        mainPanel.add(totalPriceLabel);

        // Add the payment method buttons
        cardButton = new JButton("카드");
        cardButton.setBounds(700, 550, 80, 30);
        cardButton.addActionListener(e -> {
            selectedMethod = "Card";
            cardButton.setBackground(Color.green);
            cashButton.setBackground(null);
        });
        mainPanel.add(cardButton);

        cashButton = new JButton("현금");
        cashButton.setBounds(800, 550, 80, 30);
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
         try {
            new SeatScreen(null).setVisible(true);
         } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      });
    }
}