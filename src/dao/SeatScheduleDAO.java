package dao;

import model.SeatSchedule;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatScheduleDAO {
    public void addSeatSchedule(SeatSchedule seatSchedule) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO SeatSchedules (ScheduleID, SeatID, IsOccupied) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, seatSchedule.getScheduleID());
        stmt.setString(2, seatSchedule.getSeatID());
        stmt.setBoolean(3, seatSchedule.isOccupied());
        stmt.executeUpdate();
    }

    public List<SeatSchedule> getSeatSchedulesByScheduleID(int scheduleID) throws SQLException {
        List<SeatSchedule> seatSchedules = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM SeatSchedules WHERE ScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, scheduleID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            SeatSchedule seatSchedule = new SeatSchedule();
            seatSchedule.setSeatScheduleID(rs.getInt("SeatScheduleID"));
            seatSchedule.setScheduleID(rs.getInt("ScheduleID"));
            seatSchedule.setSeatID(rs.getString("SeatID"));
            seatSchedule.setOccupied(rs.getBoolean("IsOccupied"));
            seatSchedules.add(seatSchedule);
        }

        return seatSchedules;
    }

    public void updateSeatOccupiedStatus(int seatScheduleID, boolean isOccupied) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE SeatSchedules SET IsOccupied = ? WHERE SeatScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setBoolean(1, isOccupied);
        stmt.setInt(2, seatScheduleID);
        stmt.executeUpdate();
    }
}
