package model;

public class Customer {
    private String customerID;
    private String name;
    private String phone;
    private String email;
    private boolean isAdmin;
    private String password;

    public Customer() {}

    public Customer(String customerID, String name, String phone, String email, boolean isAdmin, String password) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.isAdmin = isAdmin;
        this.password = password;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}

