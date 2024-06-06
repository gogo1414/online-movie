package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.BookingDAO;
import dao.MovieDAO;
import dao.ScheduleDAO;
import dao.SeatDAO;
import dao.TheaterDAO;
import dao.TicketDAO;
import model.AllMovieInfo;
import model.Booking;
import model.Customer;
import model.Movie;
import model.Schedule;
import model.Seat;
import model.Ticket;

public class BookingHistoryScreen extends JFrame {
    private Customer customer;
    private BookingDAO bookingDAO;
    private TheaterDAO theaterDAO;
    private TicketDAO ticketDAO;
    private ScheduleDAO scheduleDAO;
    private MovieDAO movieDAO;
    private SeatDAO seatDAO;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton cancelButton;
    private JButton modifyButton; // 예약 변경 버튼
    private JButton backButton;
    
    private int theaterID;
    private String seatID;

    public BookingHistoryScreen(Customer customer) {
        this.customer = customer;
        bookingDAO = new BookingDAO();
        ticketDAO = new TicketDAO();
        scheduleDAO = new ScheduleDAO();
        movieDAO = new MovieDAO();
        seatDAO = new SeatDAO();
        theaterDAO = new TheaterDAO();

        setTitle("예매 내역");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new Object[]{"예약 번호", "영화명", "상영일", "상영관번호", "좌석번호", "판매가격"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀을 수정 불가로 설정
            }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = bookingTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingID = (int) tableModel.getValueAt(selectedRow, 0);
                        showBookingDetails(bookingID);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        cancelButton = new JButton("예약 취소");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingTable.getSelectedRow();
                if (selectedRow != -1) {
                    int bookingID = (int) tableModel.getValueAt(selectedRow, 0);
                    theaterID = (int) tableModel.getValueAt(selectedRow, 3);
                    seatID = (String) tableModel.getValueAt(selectedRow, 4);
                    cancelBooking(bookingID);
                } else {
                    JOptionPane.showMessageDialog(BookingHistoryScreen.this, "취소할 예약을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        modifyButton = new JButton("예약 변경");
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingTable.getSelectedRow();
                if (selectedRow != -1) {
                    int bookingID = (int) tableModel.getValueAt(selectedRow, 0);
                    modifyBooking(bookingID);
                } else {
                    JOptionPane.showMessageDialog(BookingHistoryScreen.this, "변경할 예약을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> {
            new MainMenuScreen(MainMenuScreen.customer).setVisible(true);
            dispose();
        });

        loadBookings();

        // Top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        // Main panel for the table and buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for the cancel and modify buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(cancelButton);
        bottomPanel.add(modifyButton); // 예약 변경 버튼 추가

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = bookingDAO.getBookingsByCustomer(customer.getCustomerID());
            for (Booking booking : bookings) {
                Ticket ticket = ticketDAO.getTicketByBookingID(booking.getBookingID());
                Schedule schedule = scheduleDAO.getScheduleByID(ticket.getScheduleID());
                Movie movie = movieDAO.getMovieByID(schedule.getMovieID());
                Seat seat = seatDAO.getSeatByID(ticket.getSeatID());
                tableModel.addRow(new Object[]{
                    booking.getBookingID(),
                    movie.getTitle(),
                    schedule.getStartDate() + " " + schedule.getStartTime(),
                    schedule.getShowNumber(),
                    seat.getSeatID(),
                    ticket.getSalePrice()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "예약 내역을 불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking(int bookingID) {
        try {
        	seatDAO.updateSeatOccupiedStatus(seatID, theaterID, false);
        	theaterDAO.increaseSeatCount(theaterID);
            bookingDAO.deleteBooking(bookingID);
            JOptionPane.showMessageDialog(this, "예약 취소 성공.");
            int selectedRow = bookingTable.getSelectedRow();
            tableModel.removeRow(selectedRow);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "예약을 취소하는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyBooking(int bookingID) {
        AllMovieInfo.bookingID = bookingID;
        AllMovieInfo.changeReservation = 1;
        int selectedRow = bookingTable.getSelectedRow();
        tableModel.removeRow(selectedRow);
        try {
            new BookingScreen().setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dispose();
    }

    private void showBookingDetails(int bookingID) {
        try {
            Booking booking = bookingDAO.getBookingByID(bookingID);
            Ticket ticket = ticketDAO.getTicketByBookingID(bookingID);
            Seat seat = seatDAO.getSeatByID(ticket.getSeatID());
            Schedule schedule = scheduleDAO.getScheduleByID(ticket.getScheduleID());
            Movie movie = movieDAO.getMovieByID(schedule.getMovieID());
            JOptionPane.showMessageDialog(this,
                "예약 번호: " + booking.getBookingID() +
                "\n영화명: " + movie.getTitle() +
                "\n상영 날짜: " + schedule.getStartDate() + " " + schedule.getStartTime() +
                "\n상영관: " + schedule.getShowNumber() +
                "\n좌석 위치: " + seat.getSeatID() +
                "\n결제 금액: " + ticket.getSalePrice() +
                "\n결제 방법: " + booking.getPaymentMethod() +
                "\n결제 여부: " + booking.getPaymentStatus() +
                "\n결제 날짜: " + booking.getPaymentDate());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "예매 세부 정보를 불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
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
