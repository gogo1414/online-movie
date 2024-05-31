package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import dao.BookingDAO;
import dao.MovieDAO;
import dao.ScheduleDAO;
import dao.TicketDAO;
import model.Booking;
import model.Customer;

public class BookingHistoryScreen extends JFrame {
    private Customer customer;
    private BookingDAO bookingDAO;
    private TicketDAO ticketDAO;
    private ScheduleDAO scheduleDAO;
    private MovieDAO movieDAO;
    private JList<String> bookingList;
    private DefaultListModel<String> listModel;
    private JButton cancelButton;
    private JButton detailsButton;

    public BookingHistoryScreen(Customer customer) {
        this.customer = customer;
        bookingDAO = new BookingDAO();
        ticketDAO = new TicketDAO();
        scheduleDAO = new ScheduleDAO();
        movieDAO = new MovieDAO();

        setTitle("Booking History");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        bookingList = new JList<>(listModel);
        bookingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookingList);

        cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedBookingIndex = bookingList.getSelectedIndex();
                if (selectedBookingIndex != -1) {
                    int bookingID = Integer.parseInt(listModel.getElementAt(selectedBookingIndex).split(":")[0]);
                    cancelBooking(bookingID);
                } else {
                    JOptionPane.showMessageDialog(BookingHistoryScreen.this, "Please select a booking to cancel.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedBookingIndex = bookingList.getSelectedIndex();
                if (selectedBookingIndex != -1) {
                    int bookingID = Integer.parseInt(listModel.getElementAt(selectedBookingIndex).split(":")[0]);
                    viewBookingDetails(bookingID);
                } else {
                    JOptionPane.showMessageDialog(BookingHistoryScreen.this, "Please select a booking to view details.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        loadBookings();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scrollPane);
        panel.add(detailsButton);
        panel.add(cancelButton);

        add(panel);
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = bookingDAO.getBookingsByCustomer(customer.getCustomerID());
            for (Booking booking : bookings) {
                // For simplicity, assuming bookingID as a string with format "BookingID: MovieTitle"
                listModel.addElement(booking.getBookingID() + ": " + "Movie Title Placeholder");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking(int bookingID) {
        try {
            bookingDAO.deleteBooking(bookingID);
            JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
            listModel.removeElementAt(bookingList.getSelectedIndex());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cancelling booking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewBookingDetails(int bookingID) {
        try {
            Booking booking = bookingDAO.getBookingByID(bookingID);
            JOptionPane.showMessageDialog(this, 
                "Booking ID: " + booking.getBookingID() +
                "\nPayment Method: " + booking.getPaymentMethod() +
                "\nPayment Status: " + booking.getPaymentStatus() +
                "\nAmount: " + booking.getAmount() +
                "\nPayment Date: " + booking.getPaymentDate());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching booking details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookingHistoryScreen(null).setVisible(true);
            }
        });
    }
}