package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import model.Schedule;
import util.DBConnection;

public class ScheduleDAO {
	
	public List<String> getAllScheduleDate() throws SQLException {
        List<String> theatersName = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT StartDate FROM schedules";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String theaterName = rs.getString("StartDate");
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
            schedule.setShowNumber(rs.getInt("ShowNumber"));
            schedule.setStartTime(rs.getTime("StartTime"));
            schedules.add(schedule);
        }

        return schedules;
    }

    public Schedule getScheduleByID(int scheduleID) throws SQLException {
        Schedule schedule = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM schedules WHERE ScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, scheduleID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            schedule = new Schedule();
            schedule.setScheduleID(rs.getInt("ScheduleID"));
            schedule.setMovieID(rs.getInt("MovieID"));
            schedule.setTheaterID(rs.getInt("TheaterID"));
            schedule.setStartDate(rs.getDate("StartDate"));
            schedule.setWeekday(rs.getString("Weekday"));
            schedule.setShowNumber(rs.getInt("ShowNumber"));
            schedule.setStartTime(rs.getTime("StartTime"));
        }

        return schedule;
    }

    public void addSchedule(Schedule schedule) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO schedules (ScheduleID, MovieID, TheaterID, StartDate, Weekday, ShowNumber, StartTime) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, schedule.getScheduleID());
        stmt.setInt(2, schedule.getMovieID());
        stmt.setInt(3, schedule.getTheaterID());
        stmt.setDate(4, new java.sql.Date(schedule.getStartDate().getTime()));
        stmt.setString(5, schedule.getWeekday());
        stmt.setInt(6, schedule.getShowNumber());
        stmt.setTime(7, schedule.getStartTime());
        stmt.executeUpdate();
    }

    public void updateSchedule(Schedule schedule) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE schedules SET MovieID = ?, TheaterID = ?, StartDate = ?, Weekday = ?, ShowNumber = ?, StartTime = ? WHERE ScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, schedule.getMovieID());
        stmt.setInt(2, schedule.getTheaterID());
        stmt.setDate(3, new java.sql.Date(schedule.getStartDate().getTime()));
        stmt.setString(4, schedule.getWeekday());
        stmt.setInt(5, schedule.getShowNumber());
        stmt.setTime(6, schedule.getStartTime());
        stmt.setInt(7, schedule.getScheduleID());
        stmt.executeUpdate();
    }

    public void deleteSchedule(int scheduleID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM schedules WHERE ScheduleID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, scheduleID);
        stmt.executeUpdate();
    }
}