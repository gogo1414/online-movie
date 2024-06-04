package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Seat;
import util.DBConnection;

public class SeatDAO {

    public List<Seat> getAvailableSeats(int theaterID) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM seats WHERE TheaterID = ? AND IsOccupied = 0";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Seat seat = new Seat();
            seat.setSeatID(rs.getString("SeatID"));
            seat.setTheaterID(rs.getInt("TheaterID"));
            seat.setOccupied(rs.getBoolean("IsOccupied"));
            seats.add(seat);
        }

        return seats;
    }
    
    public List<Seat> getAllSeats(int theaterID) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM seats WHERE TheaterID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Seat seat = new Seat();
            seat.setSeatID(rs.getString("SeatID"));
            seat.setTheaterID(rs.getInt("TheaterID"));
            seat.setOccupied(rs.getBoolean("IsOccupied"));
            seats.add(seat);
        }

        return seats;
    }
    
    public Seat getSeatByIDAndTheaterID(String seatID, int theaterID) throws SQLException {
        Seat seat = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM seats WHERE SeatID = ? AND TheaterID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, seatID);
        stmt.setInt(2, theaterID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            seat = new Seat();
            seat.setSeatID(rs.getString("SeatID"));
            seat.setTheaterID(rs.getInt("TheaterID"));
            seat.setOccupied(rs.getBoolean("IsOccupied"));
        }

        return seat;
    }

    public Seat getSeatByID(String seatID) throws SQLException {
        Seat seat = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM seats WHERE SeatID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, seatID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            seat = new Seat();
            seat.setSeatID(rs.getString("SeatID"));
            seat.setTheaterID(rs.getInt("TheaterID"));
            seat.setOccupied(rs.getBoolean("IsOccupied"));
        }

        return seat;
    }

    public void addSeat(Seat seat) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO seats (SeatID, TheaterID, IsOccupied) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, seat.getSeatID());
        stmt.setInt(2, seat.getTheaterID());
        stmt.setBoolean(3, seat.isOccupied());
        stmt.executeUpdate();
    }


    public void deleteSeat(String seatID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM seats WHERE SeatID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, seatID);
        stmt.executeUpdate();
    }
}