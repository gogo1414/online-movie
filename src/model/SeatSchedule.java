package model;

public class SeatSchedule {
    private int seatScheduleID;
    private int scheduleID;
    private String seatID;
    private boolean isOccupied;

    // Getters and Setters
    public int getSeatScheduleID() {
        return seatScheduleID;
    }

    public void setSeatScheduleID(int seatScheduleID) {
        this.seatScheduleID = seatScheduleID;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
