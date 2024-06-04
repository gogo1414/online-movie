package ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.BookingDAO;
import dao.TicketDAO;
import model.Booking;
import model.Schedule;
import model.Ticket;

public class SeatScreen extends JFrame {

	
	private Booking booking;
	private Ticket ticket;
	
	private BookingDAO bookingDao;
	private TicketDAO ticketDao;
	
    private static final int ROW = 10;
    private static final int HEIGHT = 10;
    private static final int SEAT_PRICE = 10000;
    private static final int TOTAL_SEATS = 255;

    private List<String> seatNumbers = new ArrayList<>();

    private char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private int[] cols = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    
    private Set<JButton> selectedSeats;
    private JPanel mainPanel;
    private JPanel seatPanel;
    private JPanel nowSelectedPanel;

    private JButton reservationButton;

    private JTextArea selectedSeatInfoArea;
    private JScrollPane scrollPane;
    private JLabel seatedCounterLabel = new JLabel("좌석 현황");
    private JLabel seatedCounterLabel2 = new JLabel("255 / " + TOTAL_SEATS);

    private JLabel totalPeopleLabel;
    private JLabel totalPriceLabel;
    
    private JButton backButton = new JButton("Back");

    public SeatScreen(Schedule schedule) {
    	bookingDao = new BookingDAO();
    	ticketDao = new TicketDAO();

        setTitle("Seat Selection");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectedSeats = new HashSet<>();

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        
        backButton.setBounds(670,610,100,40);
        mainPanel.add(backButton);
        backButton.addActionListener(e->{
           try {
            new BookingScreen().setVisible(true);
         } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
           dispose();
        });
        
        seatPanel = new JPanel();
        seatPanel.setBounds(30, 30, 500, 500);
        seatPanel.setBackground(Color.white);
        seatPanel.setLayout(new GridLayout(ROW + 1, HEIGHT + 1, 5, 5));
        seatPanel.add(new JLabel(""));
        for (int col : cols) {
            seatPanel.add(new JLabel(String.valueOf(col), SwingConstants.CENTER));
        }
        mainPanel.add(seatPanel);

        
        reservationButton = new JButton("좌석 선택");
        reservationButton.setSize(100, 50);
        reservationButton.setLocation(670, 550);
        reservationButton.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(SeatScreen.this, "No seats selected.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                StringBuilder reservedSeats = new StringBuilder("Reserved seats: ");
                for (JButton seat : selectedSeats) {
                    reservedSeats.append(seat.getText()).append(" ");
                  
                }
                
                
                JOptionPane.showMessageDialog(SeatScreen.this, reservedSeats.toString(), "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);
                String dateString = "2023-01-01";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = formatter.parse(dateString);
                    System.out.println("Date: " + date);
                } catch (ParseException err) {
                    err.printStackTrace();
                }
                booking = new Booking("Credit Card", "Paid", 1500, "2", date);
                try {
					bookingDao.addBooking(booking);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                
                new MainMenuScreen(MainMenuScreen.customer).setVisible(true);
                dispose();
            }
        });
        mainPanel.add(reservationButton);

        nowSelectedPanel = new JPanel();
        nowSelectedPanel.setLayout(null);
        nowSelectedPanel.setBounds(540, 30, 200, 500);
        nowSelectedPanel.setBackground(Color.white);

        selectedSeatInfoArea = new JTextArea();
        selectedSeatInfoArea.setEditable(false);
        scrollPane = new JScrollPane(selectedSeatInfoArea);
        scrollPane.setBounds(10, 10, 180, 400);
        nowSelectedPanel.add(scrollPane);

        JButton reSelect = new JButton("다시 선택");
        reSelect.setBounds(10, 440, 100, 50);
        reSelect.addActionListener(e -> {
           for(JButton btn : selectedSeats) {
              btn.setBackground(Color.gray);
           }
            selectedSeats.clear();
            seatNumbers.clear();
            updateSelectedSeatLabel();
        });
        nowSelectedPanel.add(reSelect);

        mainPanel.add(nowSelectedPanel);

        for (int row = 0; row < ROW; row++) {
            seatPanel.add(new JLabel(String.valueOf(rows[row]), SwingConstants.CENTER));
            for (int col = 0; col < HEIGHT; col++) {
                JButton seatButton = new JButton(rows[row] + "" + (col + 1));
                seatButton.setBackground(Color.gray);
                seatButton.setSize(10, 10);
                seatButton.addActionListener(new SeatButtonListener(rows[row], col + 1));
                seatPanel.add(seatButton);
            }
        }

        seatedCounterLabel.setBounds(300, 520, 100, 100);
        mainPanel.add(seatedCounterLabel);

        seatedCounterLabel2.setBounds(300, 540, 100, 100);
        mainPanel.add(seatedCounterLabel2);

        totalPeopleLabel = new JLabel("총 인원: 0");
        totalPeopleLabel.setBounds(550, 540, 150, 30);
        mainPanel.add(totalPeopleLabel);

        totalPriceLabel = new JLabel("총 가격: 0");
        totalPriceLabel.setBounds(550, 570, 150, 30);
        mainPanel.add(totalPriceLabel);

        add(mainPanel);
        setVisible(true);
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
            if (selectedSeats.contains(source)) {
                selectedSeats.remove(source);
                source.setBackground(Color.gray);
                seatNumbers.remove(seatNumber);
            } else {
                selectedSeats.add(source);
                source.setBackground(Color.green);
                seatNumbers.add(seatNumber);
            }
            updateSelectedSeatLabel();
        }
    }

    private void updateSelectedSeatLabel() {
        if (seatNumbers.isEmpty()) {
            selectedSeatInfoArea.setText("선택된 좌석: None");
            totalPeopleLabel.setText("총 인원: 0");
            totalPriceLabel.setText("총 가격: 0");
        } else {
            selectedSeatInfoArea.setText("선택된 좌석:\n" + String.join("\n", seatNumbers));
            totalPeopleLabel.setText("총 인원: " + seatNumbers.size());
            totalPriceLabel.setText("총 가격: " + (seatNumbers.size() * SEAT_PRICE));
        }
        seatedCounterLabel2.setText(TOTAL_SEATS - selectedSeats.size() + " / " + TOTAL_SEATS);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeatScreen(null).setVisible(true));
    }
}