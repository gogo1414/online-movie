package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dbtest";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
    	Connection connection = null;

        try {
        	//Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
        	connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
			System.out.println("SQL 실행오류");
        }
        
        return connection;
    }
}