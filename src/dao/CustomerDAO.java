package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import util.DBConnection;

public class CustomerDAO {

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM customers";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomerID(rs.getString("CustomerID"));
            customer.setName(rs.getString("Name"));
            customer.setPhone(rs.getString("Phone"));
            customer.setEmail(rs.getString("Email"));
            customer.setAdmin(rs.getBoolean("IsAdmin"));
            customer.setPassword(rs.getString("Password"));
            customers.add(customer);
        }

        return customers;
    }

    public Customer getCustomerByID(String customerID) throws SQLException {
        Customer customer = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM customers WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customerID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            customer = new Customer();
            customer.setCustomerID(rs.getString("CustomerID"));
            customer.setName(rs.getString("Name"));
            customer.setPhone(rs.getString("Phone"));
            customer.setEmail(rs.getString("Email"));
            customer.setAdmin(rs.getBoolean("IsAdmin"));
            customer.setPassword(rs.getString("Password"));
        }

        return customer;
    }

    public void addCustomer(Customer customer) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO customers (CustomerID, Name, Phone, Email, IsAdmin, Passowrd) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customer.getCustomerID());
        stmt.setString(2, customer.getName());
        stmt.setString(3, customer.getPhone());
        stmt.setString(4, customer.getEmail());
        stmt.setBoolean(5, customer.isAdmin());
        stmt.setString(6, customer.getPassword());
        stmt.executeUpdate();
    }

    public void updateCustomer(Customer customer) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE customers SET Name = ?, Phone = ?, Email = ?, IsAdmin = ?, Password = ? WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getPhone());
        stmt.setString(3, customer.getEmail());
        stmt.setBoolean(4, customer.isAdmin());
        stmt.setString(5, customer.getPassword());
        stmt.setString(6, customer.getCustomerID());
        stmt.executeUpdate();
    }

    public void deleteCustomer(String customerID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM customers WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customerID);
        stmt.executeUpdate();
    }
    
    public Customer authenticate(String userName, String password) throws SQLException {
        Customer customer = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM customers WHERE Name = ? AND Password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, userName);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            customer = new Customer();
            customer.setCustomerID(rs.getString("CustomerID"));
            customer.setName(rs.getString("Name"));
            customer.setPhone(rs.getString("Phone"));
            customer.setEmail(rs.getString("Email"));
            customer.setAdmin(rs.getBoolean("IsAdmin"));
            customer.setPassword(rs.getString("Password"));
        }

        return customer;
    }
}