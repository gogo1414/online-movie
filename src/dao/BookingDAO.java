package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Booking;
import util.DBConnection;


public class BookingDAO {

    public List<Booking> getBookingsByCustomer(String customerID) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM bookings WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customerID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Booking booking = new Booking();
            booking.setBookingID(rs.getInt("BookingID"));
            booking.setPaymentMethod(rs.getString("PaymentMethod"));
            booking.setPaymentStatus(rs.getString("PaymentStatus"));
            booking.setAmount(rs.getInt("Amount"));
            booking.setCustomerID(rs.getString("CustomerID"));
            booking.setPaymentDate(rs.getDate("PaymentDate"));
            bookings.add(booking);
        }

        return bookings;
    }

    public Booking getBookingByID(int bookingID) throws SQLException {
        Booking booking = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM bookings WHERE BookingID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, bookingID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            booking = new Booking();
            booking.setBookingID(rs.getInt("BookingID"));
            booking.setPaymentMethod(rs.getString("PaymentMethod"));
            booking.setPaymentStatus(rs.getString("PaymentStatus"));
            booking.setAmount(rs.getInt("Amount"));
            booking.setCustomerID(rs.getString("CustomerID"));
            booking.setPaymentDate(rs.getDate("PaymentDate"));
        }

        return booking;
    }

    public void addBooking(Booking booking) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO bookings (PaymentMethod, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, booking.getPaymentMethod());
        stmt.setString(2, booking.getPaymentStatus());
        stmt.setInt(3, booking.getAmount());
        stmt.setString(4, booking.getCustomerID());
        stmt.setDate(5, new java.sql.Date(booking.getPaymentDate().getTime()));
        stmt.executeUpdate();
    }

    public void deleteBooking(int bookingID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM bookings WHERE BookingID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, bookingID);
        stmt.executeUpdate();
    }
}