package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Theater;
import util.DBConnection;

public class TheaterDAO {
	
	public List<String> getAllTheatersName() throws SQLException {
        List<String> theatersName = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT TheaterName FROM theaters";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String theaterName = rs.getString("TheaterName");
            theatersName.add(theaterName);
        }

        return theatersName;
    }

    public List<Theater> getAllTheaters() throws SQLException {
        List<Theater> theaters = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM theaters";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Theater theater = new Theater();
            theater.setTheaterID(rs.getInt("TheaterID"));
            theater.setSeatCount(rs.getInt("SeatCount"));
            theater.setTheaterName(rs.getString("TheaterName"));
            theater.setActive(rs.getBoolean("IsActive"));
            theater.setWidth(rs.getInt("Width"));
            theater.setHeight(rs.getInt("Height"));
            theaters.add(theater);
        }

        return theaters;
    }

    public Theater getTheaterByID(int theaterID) throws SQLException {
        Theater theater = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM theaters WHERE TheaterID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            theater = new Theater();
            theater.setTheaterID(rs.getInt("TheaterID"));
            theater.setSeatCount(rs.getInt("SeatCount"));
            theater.setTheaterName(rs.getString("TheaterName"));
            theater.setActive(rs.getBoolean("IsActive"));
            theater.setWidth(rs.getInt("Width"));
            theater.setHeight(rs.getInt("Height"));
        }

        return theater;
    }

    public void addTheater(Theater theater) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO theaters (TheaterID, SeatCount, TheaterName, IsActive, Width, Height) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theater.getTheaterID());
        stmt.setInt(2, theater.getSeatCount());
        stmt.setString(3, theater.getTheaterName());
        stmt.setBoolean(4, theater.isActive());
        stmt.setInt(5, theater.getWidth());
        stmt.setInt(6, theater.getHeight());
        stmt.executeUpdate();
    }
    
    public void deleteTheater(int theaterID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM theaters WHERE TheaterID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        stmt.executeUpdate();
    }
}