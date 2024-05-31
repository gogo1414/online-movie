package model;

public class Seat {
    private String seatID;
    private int theaterID;
    private boolean isOccupied;

    public Seat() {}

    public Seat(String seatID, int theaterID, boolean isOccupied) {
        this.seatID = seatID;
        this.theaterID = theaterID;
        this.isOccupied = isOccupied;
    }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public int getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(int theaterID) {
        this.theaterID = theaterID;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }
}