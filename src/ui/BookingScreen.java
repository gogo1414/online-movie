package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dao.SeatDAO;
import dao.TheaterDAO;
import model.Movie;
import model.Seat;
import model.Theater;

public class BookingScreen extends JFrame {
    private Movie movie;
    private TheaterDAO theaterDAO;
    private SeatDAO seatDAO;
    private JComboBox<Integer> theaterComboBox;
    private JComboBox<String> seatComboBox;
    private JButton bookButton;

    public BookingScreen(Movie movie) {
        this.movie = movie;
        theaterDAO = new TheaterDAO();
        seatDAO = new SeatDAO();

        setTitle("Book Movie");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Booking for: " + movie.getTitle());

        theaterComboBox = new JComboBox<>();
        loadTheaters();

        seatComboBox = new JComboBox<>();

        theaterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSeats();
            }
        });

        bookButton = new JButton("Confirm Booking");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmBooking();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieDetailsScreen(movie).setVisible(true);
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(new JLabel("Select Theater:"));
        panel.add(theaterComboBox);
        panel.add(new JLabel("Select Seat:"));
        panel.add(seatComboBox);
        panel.add(bookButton);
        panel.add(backButton);

        add(panel);
    }

    private void loadTheaters() {
        try {
            List<Theater> theaters = theaterDAO.getAllTheaters();
            for (Theater theater : theaters) {
                theaterComboBox.addItem(theater.getTheaterID());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading theaters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSeats() {
        seatComboBox.removeAllItems();
        Integer selectedTheaterID = (Integer) theaterComboBox.getSelectedItem();
        if (selectedTheaterID != null) {
            try {
                List<Seat> seats = seatDAO.getAvailableSeats(selectedTheaterID);
                for (Seat seat : seats) {
                    seatComboBox.addItem(seat.getSeatID());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading seats.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmBooking() {
        Integer selectedTheaterID = (Integer) theaterComboBox.getSelectedItem();
        String selectedSeat = (String) seatComboBox.getSelectedItem();
        if (selectedTheaterID != null && selectedSeat != null) {
            // Implement booking logic here
            JOptionPane.showMessageDialog(this, "Booking confirmed for " + movie.getTitle() + " at theater " + selectedTheaterID + " seat " + selectedSeat);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a theater and seat.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookingScreen(null).setVisible(true);
            }
        });
    }
}

