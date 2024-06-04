package model;

public class Ticket {
    private int ticketID;
    private int scheduleID;
    private int theaterID;
    private String seatID;
    private int bookingID;
    private boolean isIssued;
    private int standardPrice;
    private int salePrice;

    public Ticket() {}
    
    public Ticket(int scheduleID, int theaterID, String seatID, int bookingID, boolean isIssued, int standardPrice, int salePrice) {
        this.scheduleID = scheduleID;
        this.theaterID = theaterID;
        this.seatID = seatID;
        this.bookingID = bookingID;
        this.isIssued = isIssued;
        this.standardPrice = standardPrice;
        this.salePrice = salePrice;
    }

    public Ticket(int ticketID, int scheduleID, int theaterID, String seatID, int bookingID, boolean isIssued, int standardPrice, int salePrice) {
        this.ticketID = ticketID;
        this.scheduleID = scheduleID;
        this.theaterID = theaterID;
        this.seatID = seatID;
        this.bookingID = bookingID;
        this.isIssued = isIssued;
        this.standardPrice = standardPrice;
        this.salePrice = salePrice;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public int getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(int theaterID) {
        this.theaterID = theaterID;
    }

    public String getSeatID() {
        return seatID;
    }

    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean isIssued) {
        this.isIssued = isIssued;
    }

    public int getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(int standardPrice) {
        this.standardPrice = standardPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }
}