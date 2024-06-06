package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import model.Schedule;
import util.DBConnection;

public class ScheduleDAO {
	
	public List<String> getAllScheduleDateByTheaterAndMovie(int theaterID, int movieID) throws SQLException {
        List<String> theatersName = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT DISTINCT StartDate FROM schedules WHERE TheaterID = ? AND MovieID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        stmt.setInt(2, movieID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String theaterName = rs.getString("StartDate");
            theatersName.add(theaterName);
        }

        return theatersName;
    }
	
	public List<String> getScheduleTimeByTheaterAndMovie(int theaterID, int movieID) throws SQLException {
        List<String> theatersName = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT DISTINCT StartTime FROM schedules WHERE TheaterID = ? AND MovieID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        stmt.setInt(2, movieID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String theaterName = rs.getString("StartTime");
            theatersName.add(theaterName);
        }

        return theatersName;
    }

    public List<Schedule> getSchedulesByTheaterAndMovie(int theaterID, int movieID) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM schedules WHERE TheaterID = ? AND MovieID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        stmt.setInt(2, movieID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Schedule schedule = new Schedule();
            schedule.setScheduleID(rs.getInt("ScheduleID"));
            schedule.setMovieID(rs.getInt("MovieID"));
            schedule.setTheaterID(rs.getInt("TheaterID"));
            schedule.setStartDate(rs.getDate("StartDate"));
            schedule.setWeekday(rs.getString("Weekday"));
            schedule.setStartTime(rs.getTime("StartTime"));
            schedules.add(schedule);
        }

        return schedules;
    }
    
    public List<Schedule> getSchedulesByTheaterAndMovieAndDate(int theaterID, int movieID, String startDate) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM schedules WHERE TheaterID = ? AND MovieID = ? AND StartDate = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        stmt.setInt(2, movieID);
        stmt.setString(3, startDate);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Schedule schedule = new Schedule();
            schedule.setScheduleID(rs.getInt("ScheduleID"));
            schedule.setMovieID(rs.getInt("MovieID"));
            schedule.setTheaterID(rs.getInt("TheaterID"));
            schedule.setStartDate(rs.getDate("StartDate"));
            schedule.setWeekday(rs.getString("Weekday"));
            schedule.setStartTime(rs.getTime("StartTime"));
            schedules.add(schedule);
        }

        return schedules;
    }

    public Schedule getScheduleByID(int scheduleID) throws SQLException {
    	Schedule schedule = new Schedule();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM schedules WHERE ScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, scheduleID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            schedule.setScheduleID(rs.getInt("ScheduleID"));
            schedule.setMovieID(rs.getInt("MovieID"));
            schedule.setTheaterID(rs.getInt("TheaterID"));
            schedule.setStartDate(rs.getDate("StartDate"));
            schedule.setWeekday(rs.getString("Weekday"));
            schedule.setStartTime(rs.getTime("StartTime"));
        }

        return schedule;
    }
    
    public List<Schedule> getScheduleByTheaterID(int theaterID) throws SQLException {
    	List<Schedule> schedules = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM schedules WHERE TheaterID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, theaterID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Schedule schedule = new Schedule();
            schedule.setScheduleID(rs.getInt("ScheduleID"));
            schedule.setMovieID(rs.getInt("MovieID"));
            schedule.setTheaterID(rs.getInt("TheaterID"));
            schedule.setStartDate(rs.getDate("StartDate"));
            schedule.setWeekday(rs.getString("Weekday"));
            schedule.setStartTime(rs.getTime("StartTime"));
            schedules.add(schedule);
        }

        return schedules;
    }

    public void deleteSchedule(int scheduleID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM schedules WHERE ScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, scheduleID);
        stmt.executeUpdate();
    }

	public Schedule getAllScheduleDateAndTime(String selectedStartDate, String selectedStartTime) throws SQLException {
		
		Schedule schedule = new Schedule();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM schedules WHERE StartDate = ? And StartTime = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, selectedStartDate);
        stmt.setString(2, selectedStartTime);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            schedule.setScheduleID(rs.getInt("ScheduleID"));
            schedule.setMovieID(rs.getInt("MovieID"));
            schedule.setTheaterID(rs.getInt("TheaterID"));
            schedule.setStartDate(rs.getDate("StartDate"));
            schedule.setWeekday(rs.getString("Weekday"));
            schedule.setStartTime(rs.getTime("StartTime"));
        }

        return schedule;
	}
}