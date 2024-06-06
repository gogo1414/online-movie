package model;

import java.sql.Time;
import java.util.Date;

public class Schedule {
    private int scheduleID;
    private int movieID;
    private int theaterID;
    private Date startDate;
    private String weekday;
    private Time startTime;

    public Schedule() {}
    
    public Schedule(int movieID, int theaterID, Date startDate, String weekday, Time startTime) {
        this.movieID = movieID;
        this.theaterID = theaterID;
        this.startDate = startDate;
        this.weekday = weekday;
        this.startTime = startTime;
    }

    public Schedule(int scheduleID, int movieID, int theaterID, Date startDate, String weekday, Time startTime) {
        this.scheduleID = scheduleID;
        this.movieID = movieID;
        this.theaterID = theaterID;
        this.startDate = startDate;
        this.weekday = weekday;
        this.startTime = startTime;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(int theaterID) {
        this.theaterID = theaterID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
}