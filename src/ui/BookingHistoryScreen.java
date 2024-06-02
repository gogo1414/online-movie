package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

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
import dao.SeatDAO;
import dao.TicketDAO;
import model.Booking;
import model.Customer;
import model.Movie;
import model.Schedule;
import model.Seat;
import model.Ticket;

public class BookingHistoryScreen extends JFrame {
    private Customer customer;
    private BookingDAO bookingDAO;
    private TicketDAO ticketDAO;
    private ScheduleDAO scheduleDAO;
    private MovieDAO movieDAO;
    private SeatDAO seatDAO;
    private JList<String> bookingList;
    private DefaultListModel<String> listModel;
    private JButton cancelButton;
    private JButton detailsButton;
    private JButton viewTicketButton;
    private JButton backButton;

    public BookingHistoryScreen(Customer customer) {
        this.customer = customer;
        bookingDAO = new BookingDAO();
        ticketDAO = new TicketDAO();
        scheduleDAO = new ScheduleDAO();
        movieDAO = new MovieDAO();
        seatDAO = new SeatDAO();

        setTitle("예매 내역");
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

        viewTicketButton = new JButton("View Ticket");
        viewTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedBookingIndex = bookingList.getSelectedIndex();
                if (selectedBookingIndex != -1) {
                    int bookingID = Integer.parseInt(listModel.getElementAt(selectedBookingIndex).split(":")[0]);
                    viewTicketDetails(bookingID);
                } else {
                    JOptionPane.showMessageDialog(BookingHistoryScreen.this, "Please select a booking to view ticket.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new MainMenuScreen(MainMenuScreen.customer).setVisible(true);
            dispose();
        });

        loadBookings();

        // Top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        // Main panel for the list and buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for the details and cancel buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(detailsButton);
        bottomPanel.add(viewTicketButton); // Add viewTicketButton between detailsButton and cancelButton
        bottomPanel.add(cancelButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = bookingDAO.getBookingsByCustomer(MainMenuScreen.customer.getCustomerID());
            for (Booking booking : bookings) {
                Ticket ticket = ticketDAO.getTicketByBookingID(booking.getBookingID());
                Movie movie = movieDAO.getMovieByID(ticket.getBookingID()); // Fixed to get movie by ticket's movie ID
                listModel.addElement(booking.getBookingID() + ": " + movie.getTitle());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking(int bookingID) {
        try {
            bookingDAO.deleteBooking(bookingID);
            JOptionPane.showMessageDialog(this, "예약 취소 성공.");
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
                "예약 번호: " + booking.getBookingID() +
                "\n결제 방법: " + booking.getPaymentMethod() +
                "\n결제 여부: " + booking.getPaymentStatus() +
                "\n결제 금액: " + booking.getAmount() +
                "\n결제 날짜: " + booking.getPaymentDate());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching booking details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewTicketDetails(int bookingID) {
        try {
            Ticket ticket = ticketDAO.getTicketByBookingID(bookingID);
            Seat seat = seatDAO.getSeatByID(ticket.getSeatID());
            Schedule schedule = scheduleDAO.getScheduleByID(ticket.getScheduleID());
            JOptionPane.showMessageDialog(this, 
                "티켓 번호: " + ticket.getTicketID() +
                "\n상영 날짜: " + schedule.getStartDate() + " " + schedule.getWeekday() +
                "\n상영 시간: " + schedule.getStartTime() +
                "\n상영관: " + schedule.getShowNumber() +
                "\n좌석 위치: " + seat.getSeatID() +
                "\n결제 금액: " + ticket.getSalePrice());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching ticket details.", "Error", JOptionPane.ERROR_MESSAGE);
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
