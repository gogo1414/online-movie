package model;

import java.util.Date;

public class Booking {
    private int bookingID;
    private String paymentMethod;
    private String paymentStatus;
    private int amount;
    private String customerID;
    private Date paymentDate;

    public Booking() {}

    public Booking( String paymentMethod, String paymentStatus, int amount, String customerID, Date paymentDate) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.customerID = customerID;
        this.paymentDate = paymentDate;
    }

    
    
    public Booking(int bookingID, String paymentMethod, String paymentStatus, int amount, String customerID, Date paymentDate) {
        this.bookingID = bookingID;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.customerID = customerID;
        this.paymentDate = paymentDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    
    
    
    
}