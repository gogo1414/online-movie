package model;

public class Theater {
    private int theaterID;
    private int seatCount;
    private String theaterName;
    private boolean isActive;
    private int width;
    private int height;

    public Theater() {}

    public Theater(int theaterID, int seatCount, String theaterName, boolean isActive, int width, int height) {
        this.theaterID = theaterID;
        this.seatCount = seatCount;
        this.theaterName = theaterName;
        this.isActive = isActive;
        this.width = width;
        this.height = height;
    }
    
    public String getTheaterName() {
    	return theaterName;
    }
    
    public void setTheaterName(String theaterName) {
    	this.theaterName = theaterName;
    }

    public int getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(int theaterID) {
        this.theaterID = theaterID;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}