package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dao.BookingDAO;
import model.Booking;
import model.Customer;

public class BookingHistoryScreen extends JFrame {
    private Customer customer;
    private DefaultListModel<Booking> bookingListModel;
    private JList<Booking> bookingList;
    private JButton cancelButton;
    private BookingDAO bookingDAO;

    public BookingHistoryScreen(Customer customer) {
        this.customer = customer;

        setTitle("예매 내역 확인");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        bookingDAO = new BookingDAO();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        bookingListModel = new DefaultListModel<>();
        loadBookings();

        bookingList = new JList<>(bookingListModel);
        panel.add(new JScrollPane(bookingList), BorderLayout.CENTER);

        cancelButton = new JButton("예매 취소");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Booking selectedBooking = bookingList.getSelectedValue();
                if (selectedBooking != null) {
                    // 예매 취소 처리
                } else {
                    JOptionPane.showMessageDialog(BookingHistoryScreen.this, "취소할 예매를 선택하세요.");
                }
            }
        });
        panel.add(cancelButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = bookingDAO.getBookingsByCustomer(customer.getCustomerID());
            for (Booking booking : bookings) {
                bookingListModel.addElement(booking);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "예매 내역을 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
