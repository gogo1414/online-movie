package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Ticket;
import util.DBConnection;

public class TicketDAO {

	public Ticket getTicketByBookingID(int bookingID) throws SQLException {
        Ticket ticket = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM tickets WHERE BookingID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, bookingID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            ticket = new Ticket();
            ticket.setTicketID(rs.getInt("TicketID"));
            ticket.setScheduleID(rs.getInt("ScheduleID"));
            ticket.setTheaterID(rs.getInt("TheaterID"));
            ticket.setSeatID(rs.getString("SeatID"));
            ticket.setBookingID(rs.getInt("BookingID"));
            ticket.setIssued(rs.getBoolean("IsIssued"));
            ticket.setStandardPrice(rs.getInt("StandardPrice"));
            ticket.setSalePrice(rs.getInt("SalePrice"));
        }

        return ticket;
    }

    public Ticket getTicketByID(int ticketID) throws SQLException {
        Ticket ticket = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM tickets WHERE TicketID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ticketID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            ticket = new Ticket();
            ticket.setTicketID(rs.getInt("TicketID"));
            ticket.setScheduleID(rs.getInt("ScheduleID"));
            ticket.setTheaterID(rs.getInt("TheaterID"));
            ticket.setSeatID(rs.getString("SeatID"));
            ticket.setBookingID(rs.getInt("BookingID"));
            ticket.setIssued(rs.getBoolean("IsIssued"));
            ticket.setStandardPrice(rs.getInt("StandardPrice"));
            ticket.setSalePrice(rs.getInt("SalePrice"));
        }

        return ticket;
    }

    public void addTicket(Ticket ticket) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO tickets (TicketID, ScheduleID, TheaterID, SeatID, BookingID, IsIssued, StandardPrice, SalePrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ticket.getTicketID());
        stmt.setInt(2, ticket.getScheduleID());
        stmt.setInt(3, ticket.getTheaterID());
        stmt.setString(4, ticket.getSeatID());
        stmt.setInt(5, ticket.getBookingID());
        stmt.setBoolean(6, ticket.isIssued());
        stmt.setInt(7, ticket.getStandardPrice());
        stmt.setInt(8, ticket.getSalePrice());
        stmt.executeUpdate();
    }

    public void updateTicket(Ticket ticket) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE tickets SET ScheduleID = ?, TheaterID = ?, SeatID = ?, BookingID = ?, IsIssued = ?, StandardPrice = ?, SalePrice = ? WHERE TicketID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ticket.getScheduleID());
        stmt.setInt(2, ticket.getTheaterID());
        stmt.setString(3, ticket.getSeatID());
        stmt.setInt(4, ticket.getBookingID());
        stmt.setBoolean(5, ticket.isIssued());
        stmt.setInt(6, ticket.getStandardPrice());
        stmt.setInt(7, ticket.getSalePrice());
        stmt.setInt(8, ticket.getTicketID());
        stmt.executeUpdate();
    }

    public void deleteTicket(int ticketID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM tickets WHERE TicketID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ticketID);
        stmt.executeUpdate();
    }
}
