package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

import util.DBConnection;

public class AdminPanel extends JFrame {

    private Connection conn;

    public AdminPanel() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setTitle("관리자 메뉴");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10)); // 30픽셀 간격
        JButton dbInitButton = new JButton("데이터베이스 초기화");
        dbInitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeDatabase();
            }
        });
        
        JButton viewButton = new JButton("전체 테이블 보기");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTablesWindow();
            }
        });
        
        buttonPanel.add(dbInitButton);
        buttonPanel.add(viewButton);

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(e->{
        	new AdminOrUserScreen().setVisible(true);
        	dispose();
        });
        
        buttonPanel.add(logoutButton);
        // SQL 입력 패널
        JPanel sqlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel actionLabel = new JLabel("변경/삭제/삽입/조회");
        actionLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        sqlPanel.add(actionLabel, gbc);

        JLabel sqlLabel = new JLabel("SQL 문:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        sqlPanel.add(sqlLabel, gbc);

        JTextField sqlField = new JTextField("예: SELECT * FROM 테이블명;", 30); // 고정된 크기의 텍스트 필드와 예시 기본값
        gbc.gridx = 1;
        gbc.gridy = 1;
        sqlPanel.add(sqlField, gbc);

        JButton sqlButton = new JButton("실행");
        gbc.gridx = 2;
        gbc.gridy = 1;
        sqlPanel.add(sqlButton, gbc);
        sqlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSQL(sqlField.getText());
            }
        });

        // 메인 패널에 추가
        add(buttonPanel, BorderLayout.NORTH);
        add(sqlPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeDatabase() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP DATABASE IF EXISTS dbtest");
            stmt.executeUpdate("CREATE DATABASE dbtest");
            
            stmt.executeUpdate("USE dbtest");
            
            // 추가할 쿼리들
            stmt.executeUpdate("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
            stmt.executeUpdate("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
            stmt.executeUpdate("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';");

            stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS `dbtest` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;");
            stmt.executeUpdate("USE `dbtest` ;");

            // 테이블 생성 쿼리들
            String[] tableCreationQueries = new String[] {
        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`customers` (" +
        	    "`CustomerID` VARCHAR(50) NOT NULL, `Name` VARCHAR(100) NOT NULL, `Phone` VARCHAR(20) NOT NULL, " +
        	    "`Email` VARCHAR(100) NOT NULL, `IsAdmin` TINYINT(1) NOT NULL, `Password` VARCHAR(20) NOT NULL, " +
        	    "PRIMARY KEY (`CustomerID`)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`bookings` (" +
        	    "`BookingID` INT NOT NULL AUTO_INCREMENT, `PaymentMethod` VARCHAR(50) NOT NULL, `PaymentStatus` tinyint NOT NULL, " +
        	    "`Amount` INT NOT NULL, `CustomerID` VARCHAR(50) NOT NULL, `PaymentDate` DATE NOT NULL, " +
        	    "PRIMARY KEY (`BookingID`), INDEX `CustomerID` (`CustomerID` ASC) VISIBLE, " +
        	    "CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `dbtest`.`customers` (`CustomerID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`movies` (" +
        	    "`MovieID` INT NOT NULL AUTO_INCREMENT, `Title` VARCHAR(255) NOT NULL, `Duration` VARCHAR(50) NOT NULL, `Rating` VARCHAR(50) NOT NULL, " +
        	    "`Director` VARCHAR(100) NOT NULL, `Actors` VARCHAR(255) NOT NULL, `Genre` VARCHAR(50) NOT NULL, `Story` TEXT NOT NULL, " +
        	    "`ReleaseDate` DATE NOT NULL, `Score` INT NOT NULL, PRIMARY KEY (`MovieID`)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`theaters` (" +
        	    "`TheaterID` INT NOT NULL AUTO_INCREMENT, `SeatCount` INT NOT NULL, `TheaterName` VARCHAR(255) NOT NULL, `IsActive` TINYINT(1) NOT NULL, " +
        	    "`Width` INT NOT NULL, `Height` INT NOT NULL, PRIMARY KEY (`TheaterID`)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`schedules` (" +
        	    "`ScheduleID` INT NOT NULL AUTO_INCREMENT, `MovieID` INT NOT NULL, `TheaterID` INT NOT NULL, `StartDate` DATE NOT NULL, `Weekday` VARCHAR(50) NOT NULL, " +
        	    "`ShowNumber` INT NOT NULL, `StartTime` TIME NOT NULL, PRIMARY KEY (`ScheduleID`), INDEX `MovieID` (`MovieID` ASC) VISIBLE, " +
        	    "INDEX `TheaterID` (`TheaterID` ASC) VISIBLE, CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`MovieID`) REFERENCES `dbtest`.`movies` (`MovieID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `schedules_ibfk_2` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`seats` (" +
        	    "`SeatID` VARCHAR(10) NOT NULL, `TheaterID` INT NOT NULL, `IsOccupied` TINYINT(1) NOT NULL, PRIMARY KEY (`SeatID`, `TheaterID`), " +
        	    "INDEX `TheaterID` (`TheaterID` ASC) VISIBLE, CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

        	    "CREATE TABLE IF NOT EXISTS `dbtest`.`tickets` (" +
        	    "`TicketID` INT NOT NULL AUTO_INCREMENT, `ScheduleID` INT NOT NULL, `TheaterID` INT NOT NULL, `SeatID` VARCHAR(10) NOT NULL, `BookingID` INT NOT NULL, " +
        	    "`IsIssued` TINYINT(1) NOT NULL, `StandardPrice` INT NOT NULL, `SalePrice` INT NOT NULL, PRIMARY KEY (`TicketID`), " +
        	    "INDEX `ScheduleID` (`ScheduleID` ASC) VISIBLE, INDEX `TheaterID` (`TheaterID` ASC) VISIBLE, INDEX `SeatID` (`SeatID` ASC) VISIBLE, " +
        	    "INDEX `BookingID` (`BookingID` ASC) VISIBLE, CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`ScheduleID`) REFERENCES `dbtest`.`schedules` (`ScheduleID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `tickets_ibfk_2` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `tickets_ibfk_4` FOREIGN KEY (`BookingID`) REFERENCES `dbtest`.`bookings` (`BookingID`) " +
        	    "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;"
        	};



            for (String query : tableCreationQueries) {
                stmt.executeUpdate(query);
            }

            // 데이터 삽입
            String[] dataInsertionQueries = new String[] {
        	    "INSERT INTO customers (CustomerID, Name, Phone, Email, IsAdmin, Password) VALUES " +
        	    "('1', 'root', '1234567890', 'john@example.com', 1, '1234'), " +
        	    "('2', 'user1', '0987654321', 'admin1@example.com', 0, 'user1'), " +
        	    "('3', 'user2', '0987654322', 'admin2@example.com', 0, 'user2'), " +
        	    "('4', 'user3', '0987654323', 'admin3@example.com', 0, 'user3'), " +
        	    "('5', 'user4', '0987654324', 'admin4@example.com', 0, 'user4'), " +
        	    "('6', 'user5', '0987654325', 'admin5@example.com', 0, 'user5'), " +
        	    "('7', 'user6', '0987654326', 'admin6@example.com', 0, 'user6'), " +
        	    "('8', 'user7', '0987654327', 'admin7@example.com', 0, 'user7'), " +
        	    "('9', 'user8', '0987654328', 'admin8@example.com', 0, 'user8'), " +
        	    "('10', 'user9', '0987654329', 'admin9@example.com', 0, 'user9'), " +
        	    "('11', 'user10', '0987654310', 'admin10@example.com', 0, 'user10'), " +
        	    "('12', 'user11', '0987654311', 'admin11@example.com', 0, 'user11');",

        	    "INSERT INTO movies (Title, Duration, Rating, Director, Actors, Genre, Story, ReleaseDate, Score) VALUES " +
        	    "('Movie 1', '120 min', 'PG-13', 'Director 1', '김준수', 'Action', 'Story 1', '2024-01-15', 85), " +
        	    "('Movie 2', '110 min', 'R', 'Director 2', '이동호', 'Horror', 'Story 2', '2024-02-12', 78), " +
        	    "('Movie 3', '130 min', 'PG', 'Director 3', '홍길동', 'Comedy', 'Story 3', '2024-03-05', 88), " +
        	    "('Movie 4', '140 min', 'PG-13', 'Director 4', '김준수', 'Drama', 'Story 4', '2024-04-10', 92), " +
        	    "('Movie 5', '125 min', 'R', 'Director 5', '이동호', 'Thriller', 'Story 5', '2024-05-20', 76), " +
        	    "('Movie 6', '115 min', 'PG', 'Director 6', '홍길동', 'Adventure', 'Story 6', '2024-06-18', 80), " +
        	    "('Movie 7', '100 min', 'PG', 'Director 7', '김준수', 'Animation', 'Story 7', '2024-07-22', 90), " +
        	    "('Movie 8', '135 min', 'R', 'Director 8', '이동호', 'Sci-Fi', 'Story 8', '2024-08-30', 87), " +
        	    "('Movie 9', '145 min', 'PG-13', 'Director 9', '홍길동', 'Fantasy', 'Story 9', '2024-09-12', 83), " +
        	    "('Movie 10', '120 min', 'R', 'Director 10', '김준수', 'Mystery', 'Story 10', '2024-10-08', 79), " +
        	    "('Movie 11', '110 min', 'PG-13', 'Director 11', '이동호', 'Romance', 'Story 11', '2024-11-16', 81), " +
        	    "('Movie 12', '105 min', 'PG', 'Director 12', '홍길동', 'Family', 'Story 12', '2024-12-25', 84);",

        	    "INSERT INTO theaters (SeatCount, TheaterName, IsActive, Width, Height) VALUES " +
	            "(98, '1대입구', 1, 10, 10), " +
	            "(97, '2대입구', 1, 10, 10), " +
	            "(95, '3대입구', 1, 10, 10), " +
	            "(100, '4대입구', 1, 10, 10), " +
	            "(100, '5대입구', 1, 10, 10), " +
	            "(100, '6대입구', 0, 10, 10), " +
	            "(100, '7대입구', 0, 10, 10), " +
	            "(100, '8대입구', 0, 10, 10), " +
	            "(100, '9대입구', 0, 10, 10), " +
	            "(100, '10대입구', 0, 10, 10), " +
	            "(100, '11대입구', 0, 10, 10), " +
	            "(100, '12대입구', 0, 10, 10);",

        	    "INSERT INTO schedules (MovieID, TheaterID, StartDate, Weekday, ShowNumber, StartTime) VALUES " +
        	    "(1, 1, '2024-01-15', 'Monday', 1, '10:00:00'), " +
        	    "(2, 2, '2024-02-12', 'Tuesday', 1, '12:00:00'), " +
        	    "(3, 3, '2024-03-05', 'Wednesday', 1, '14:00:00'), " +
        	    "(4, 4, '2024-04-10', 'Thursday', 1, '16:00:00'), " +
        	    "(5, 5, '2024-05-20', 'Friday', 1, '18:00:00'), " +
        	    "(6, 1, '2024-06-18', 'Saturday', 1, '20:00:00'), " +
        	    "(7, 2, '2024-07-22', 'Sunday', 1, '22:00:00'), " +
        	    "(8, 3, '2024-08-30', 'Monday', 1, '11:00:00'), " +
        	    "(9, 4, '2024-09-12', 'Tuesday', 1, '13:00:00'), " +
        	    "(10, 5, '2024-10-08', 'Wednesday', 1, '15:00:00'), " +
        	    "(11, 1, '2024-11-16', 'Thursday', 1, '17:00:00'), " +
        	    "(12, 2, '2024-12-25', 'Friday', 1, '19:00:00');",

        	    "INSERT INTO seats (SeatID, TheaterID, IsOccupied) VALUES " +
        	    "('A1', 1, 0), ('A2', 1, 1), ('A3', 1, 0), ('A4', 1, 0), ('A5', 1, 0), ('A6', 1, 0), ('A7', 1, 0), ('A8', 1, 0), ('A9', 1, 0), ('A10', 1, 0), " +
        	    "('B1', 1, 0), ('B2', 1, 0), ('B3', 1, 1), ('B4', 1, 0), ('B5', 1, 0), ('B6', 1, 0), ('B7', 1, 0), ('B8', 1, 0), ('B9', 1, 0), ('B10', 1, 0), " +
        	    "('C1', 1, 0), ('C2', 1, 0), ('C3', 1, 0), ('C4', 1, 0), ('C5', 1, 0), ('C6', 1, 0), ('C7', 1, 0), ('C8', 1, 0), ('C9', 1, 0), ('C10', 1, 0), " +
        	    "('D1', 1, 0), ('D2', 1, 0), ('D3', 1, 0), ('D4', 1, 0), ('D5', 1, 0), ('D6', 1, 0), ('D7', 1, 0), ('D8', 1, 0), ('D9', 1, 0), ('D10', 1, 0), " +
        	    "('E1', 1, 0), ('E2', 1, 0), ('E3', 1, 0), ('E4', 1, 0), ('E5', 1, 0), ('E6', 1, 0), ('E7', 1, 0), ('E8', 1, 0), ('E9', 1, 0), ('E10', 1, 0), " +
        	    "('F1', 1, 0), ('F2', 1, 0), ('F3', 1, 0), ('F4', 1, 0), ('F5', 1, 0), ('F6', 1, 0), ('F7', 1, 0), ('F8', 1, 0), ('F9', 1, 0), ('F10', 1, 0), " +
        	    "('G1', 1, 0), ('G2', 1, 0), ('G3', 1, 0), ('G4', 1, 0), ('G5', 1, 0), ('G6', 1, 0), ('G7', 1, 0), ('G8', 1, 0), ('G9', 1, 0), ('G10', 1, 0), " +
        	    "('H1', 1, 0), ('H2', 1, 0), ('H3', 1, 0), ('H4', 1, 0), ('H5', 1, 0), ('H6', 1, 0), ('H7', 1, 0), ('H8', 1, 0), ('H9', 1, 0), ('H10', 1, 0), " +
        	    "('I1', 1, 0), ('I2', 1, 0), ('I3', 1, 0), ('I4', 1, 0), ('I5', 1, 0), ('I6', 1, 0), ('I7', 1, 0), ('I8', 1, 0), ('I9', 1, 0), ('I10', 1, 0), " +
        	    "('J1', 1, 0), ('J2', 1, 0), ('J3', 1, 0), ('J4', 1, 0), ('J5', 1, 0), ('J6', 1, 0), ('J7', 1, 0), ('J8', 1, 0), ('J9', 1, 0), ('J10', 1, 0), " +

        	    "('A1', 2, 0), ('A2', 2, 0), ('A3', 2, 0), ('A4', 2, 0), ('A5', 2, 0), ('A6', 2, 0), ('A7', 2, 0), ('A8', 2, 0), ('A9', 2, 0), ('A10', 2, 0), " +
        	    "('B1', 2, 0), ('B2', 2, 0), ('B3', 2, 0), ('B4', 2, 0), ('B5', 2, 0), ('B6', 2, 0), ('B7', 2, 0), ('B8', 2, 0), ('B9', 2, 0), ('B10', 2, 0), " +
        	    "('C1', 2, 0), ('C2', 2, 0), ('C3', 2, 0), ('C4', 2, 0), ('C5', 2, 0), ('C6', 2, 0), ('C7', 2, 1), ('C8', 2, 0), ('C9', 2, 0), ('C10', 2, 0), " +
        	    "('D1', 2, 0), ('D2', 2, 0), ('D3', 2, 0), ('D4', 2, 0), ('D5', 2, 0), ('D6', 2, 0), ('D7', 2, 0), ('D8', 2, 0), ('D9', 2, 0), ('D10', 2, 1), " +
        	    "('E1', 2, 0), ('E2', 2, 0), ('E3', 2, 0), ('E4', 2, 0), ('E5', 2, 0), ('E6', 2, 0), ('E7', 2, 0), ('E8', 2, 0), ('E9', 2, 0), ('E10', 2, 1), " +
        	    "('F1', 2, 0), ('F2', 2, 0), ('F3', 2, 0), ('F4', 2, 0), ('F5', 2, 0), ('F6', 2, 0), ('F7', 2, 0), ('F8', 2, 0), ('F9', 2, 0), ('F10', 2, 0), " +
        	    "('G1', 2, 0), ('G2', 2, 0), ('G3', 2, 0), ('G4', 2, 0), ('G5', 2, 0), ('G6', 2, 0), ('G7', 2, 0), ('G8', 2, 0), ('G9', 2, 0), ('G10', 2, 0), " +
        	    "('H1', 2, 0), ('H2', 2, 0), ('H3', 2, 0), ('H4', 2, 0), ('H5', 2, 0), ('H6', 2, 0), ('H7', 2, 0), ('H8', 2, 0), ('H9', 2, 0), ('H10', 2, 0), " +
        	    "('I1', 2, 0), ('I2', 2, 0), ('I3', 2, 0), ('I4', 2, 0), ('I5', 2, 0), ('I6', 2, 0), ('I7', 2, 0), ('I8', 2, 0), ('I9', 2, 0), ('I10', 2, 0), " +
        	    "('J1', 2, 0), ('J2', 2, 0), ('J3', 2, 0), ('J4', 2, 0), ('J5', 2, 0), ('J6', 2, 0), ('J7', 2, 0), ('J8', 2, 0), ('J9', 2, 0), ('J10', 2, 0), " +

        	    "('A1', 3, 0), ('A2', 3, 1), ('A3', 3, 0), ('A4', 3, 0), ('A5', 3, 0), ('A6', 3, 0), ('A7', 3, 0), ('A8', 3, 0), ('A9', 3, 0), ('A10', 3, 0), " +
        	    "('B1', 3, 0), ('B2', 3, 0), ('B3', 3, 0), ('B4', 3, 0), ('B5', 3, 0), ('B6', 3, 1), ('B7', 3, 0), ('B8', 3, 0), ('B9', 3, 0), ('B10', 3, 0), " +
        	    "('C1', 3, 0), ('C2', 3, 0), ('C3', 3, 0), ('C4', 3, 0), ('C5', 3, 0), ('C6', 3, 0), ('C7', 3, 1), ('C8', 3, 0), ('C9', 3, 0), ('C10', 3, 0), " +
        	    "('D1', 3, 0), ('D2', 3, 0), ('D3', 3, 0), ('D4', 3, 0), ('D5', 3, 0), ('D6', 3, 0), ('D7', 3, 1), ('D8', 3, 0), ('D9', 3, 0), ('D10', 3, 0), " +
        	    "('E1', 3, 0), ('E2', 3, 0), ('E3', 3, 0), ('E4', 3, 0), ('E5', 3, 0), ('E6', 3, 0), ('E7', 3, 1), ('E8', 3, 0), ('E9', 3, 0), ('E10', 3, 0), " +
        	    "('F1', 3, 0), ('F2', 3, 0), ('F3', 3, 0), ('F4', 3, 0), ('F5', 3, 0), ('F6', 3, 0), ('F7', 3, 0), ('F8', 3, 0), ('F9', 3, 0), ('F10', 3, 0), " +
        	    "('G1', 3, 0), ('G2', 3, 0), ('G3', 3, 0), ('G4', 3, 0), ('G5', 3, 0), ('G6', 3, 0), ('G7', 3, 0), ('G8', 3, 0), ('G9', 3, 0), ('G10', 3, 0), " +
        	    "('H1', 3, 0), ('H2', 3, 0), ('H3', 3, 0), ('H4', 3, 0), ('H5', 3, 0), ('H6', 3, 0), ('H7', 3, 0), ('H8', 3, 0), ('H9', 3, 0), ('H10', 3, 0), " +
        	    "('I1', 3, 0), ('I2', 3, 0), ('I3', 3, 0), ('I4', 3, 0), ('I5', 3, 0), ('I6', 3, 0), ('I7', 3, 0), ('I8', 3, 0), ('I9', 3, 0), ('I10', 3, 0), " +
        	    "('J1', 3, 0), ('J2', 3, 0), ('J3', 3, 0), ('J4', 3, 0), ('J5', 3, 0), ('J6', 3, 0), ('J7', 3, 0), ('J8', 3, 0), ('J9', 3, 0), ('J10', 3, 0), " +

        	    "('A1', 4, 0), ('A2', 4, 0), ('A3', 4, 0), ('A4', 4, 0), ('A5', 4, 0), ('A6', 4, 0), ('A7', 4, 0), ('A8', 4, 0), ('A9', 4, 0), ('A10', 4, 0), " +
        	    "('B1', 4, 0), ('B2', 4, 0), ('B3', 4, 0), ('B4', 4, 0), ('B5', 4, 0), ('B6', 4, 0), ('B7', 4, 0), ('B8', 4, 0), ('B9', 4, 0), ('B10', 4, 0), " +
        	    "('C1', 4, 0), ('C2', 4, 0), ('C3', 4, 0), ('C4', 4, 0), ('C5', 4, 0), ('C6', 4, 0), ('C7', 4, 0), ('C8', 4, 0), ('C9', 4, 0), ('C10', 4, 0), " +
        	    "('D1', 4, 0), ('D2', 4, 0), ('D3', 4, 0), ('D4', 4, 0), ('D5', 4, 0), ('D6', 4, 0), ('D7', 4, 0), ('D8', 4, 0), ('D9', 4, 0), ('D10', 4, 0), " +
        	    "('E1', 4, 0), ('E2', 4, 0), ('E3', 4, 0), ('E4', 4, 0), ('E5', 4, 0), ('E6', 4, 0), ('E7', 4, 0), ('E8', 4, 0), ('E9', 4, 0), ('E10', 4, 0), " +
        	    "('F1', 4, 0), ('F2', 4, 0), ('F3', 4, 0), ('F4', 4, 0), ('F5', 4, 0), ('F6', 4, 0), ('F7', 4, 0), ('F8', 4, 0), ('F9', 4, 0), ('F10', 4, 0), " +
        	    "('G1', 4, 0), ('G2', 4, 0), ('G3', 4, 0), ('G4', 4, 0), ('G5', 4, 0), ('G6', 4, 0), ('G7', 4, 0), ('G8', 4, 0), ('G9', 4, 0), ('G10', 4, 0), " +
        	    "('H1', 4, 0), ('H2', 4, 0), ('H3', 4, 0), ('H4', 4, 0), ('H5', 4, 0), ('H6', 4, 0), ('H7', 4, 0), ('H8', 4, 0), ('H9', 4, 0), ('H10', 4, 0), " +
        	    "('I1', 4, 0), ('I2', 4, 0), ('I3', 4, 0), ('I4', 4, 0), ('I5', 4, 0), ('I6', 4, 0), ('I7', 4, 0), ('I8', 4, 0), ('I9', 4, 0), ('I10', 4, 0), " +
        	    "('J1', 4, 0), ('J2', 4, 0), ('J3', 4, 0), ('J4', 4, 0), ('J5', 4, 0), ('J6', 4, 0), ('J7', 4, 0), ('J8', 4, 0), ('J9', 4, 0), ('J10', 4, 0), " +

        	    "('A1', 5, 0), ('A2', 5, 0), ('A3', 5, 0), ('A4', 5, 0), ('A5', 5, 0), ('A6', 5, 0), ('A7', 5, 0), ('A8', 5, 0), ('A9', 5, 0), ('A10', 5, 0), " +
        	    "('B1', 5, 0), ('B2', 5, 0), ('B3', 5, 0), ('B4', 5, 0), ('B5', 5, 0), ('B6', 5, 0), ('B7', 5, 0), ('B8', 5, 0), ('B9', 5, 0), ('B10', 5, 0), " +
        	    "('C1', 5, 0), ('C2', 5, 0), ('C3', 5, 0), ('C4', 5, 0), ('C5', 5, 0), ('C6', 5, 0), ('C7', 5, 0), ('C8', 5, 0), ('C9', 5, 0), ('C10', 5, 0), " +
        	    "('D1', 5, 0), ('D2', 5, 0), ('D3', 5, 0), ('D4', 5, 0), ('D5', 5, 0), ('D6', 5, 0), ('D7', 5, 0), ('D8', 5, 0), ('D9', 5, 0), ('D10', 5, 0), " +
        	    "('E1', 5, 0), ('E2', 5, 0), ('E3', 5, 0), ('E4', 5, 0), ('E5', 5, 0), ('E6', 5, 0), ('E7', 5, 0), ('E8', 5, 0), ('E9', 5, 0), ('E10', 5, 0), " +
        	    "('F1', 5, 0), ('F2', 5, 0), ('F3', 5, 0), ('F4', 5, 0), ('F5', 5, 0), ('F6', 5, 0), ('F7', 5, 0), ('F8', 5, 0), ('F9', 5, 0), ('F10', 5, 0), " +
        	    "('G1', 5, 0), ('G2', 5, 0), ('G3', 5, 0), ('G4', 5, 0), ('G5', 5, 0), ('G6', 5, 0), ('G7', 5, 0), ('G8', 5, 0), ('G9', 5, 0), ('G10', 5, 0), " +
        	    "('H1', 5, 0), ('H2', 5, 0), ('H3', 5, 0), ('H4', 5, 0), ('H5', 5, 0), ('H6', 5, 0), ('H7', 5, 0), ('H8', 5, 0), ('H9', 5, 0), ('H10', 5, 0), " +
        	    "('I1', 5, 0), ('I2', 5, 0), ('I3', 5, 0), ('I4', 5, 0), ('I5', 5, 0), ('I6', 5, 0), ('I7', 5, 0), ('I8', 5, 0), ('I9', 5, 0), ('I10', 5, 0), " +
        	    "('J1', 5, 0), ('J2', 5, 0), ('J3', 5, 0), ('J4', 5, 0), ('J5', 5, 0), ('J6', 5, 0), ('J7', 5, 0), ('J8', 5, 0), ('J9', 5, 0), ('J10', 5, 0), " +

        	    "('A1', 6, 0), ('A2', 6, 0), ('A3', 6, 0), ('A4', 6, 0), ('A5', 6, 0), ('A6', 6, 0), ('A7', 6, 0), ('A8', 6, 0), ('A9', 6, 0), ('A10', 6, 0), " +
        	    "('B1', 6, 0), ('B2', 6, 0), ('B3', 6, 0), ('B4', 6, 0), ('B5', 6, 0), ('B6', 6, 0), ('B7', 6, 0), ('B8', 6, 0), ('B9', 6, 0), ('B10', 6, 0), " +
        	    "('C1', 6, 0), ('C2', 6, 0), ('C3', 6, 0), ('C4', 6, 0), ('C5', 6, 0), ('C6', 6, 0), ('C7', 6, 0), ('C8', 6, 0), ('C9', 6, 0), ('C10', 6, 0), " +
        	    "('D1', 6, 0), ('D2', 6, 0), ('D3', 6, 0), ('D4', 6, 0), ('D5', 6, 0), ('D6', 6, 0), ('D7', 6, 0), ('D8', 6, 0), ('D9', 6, 0), ('D10', 6, 0), " +
        	    "('E1', 6, 0), ('E2', 6, 0), ('E3', 6, 0), ('E4', 6, 0), ('E5', 6, 0), ('E6', 6, 0), ('E7', 6, 0), ('E8', 6, 0), ('E9', 6, 0), ('E10', 6, 0), " +
        	    "('F1', 6, 0), ('F2', 6, 0), ('F3', 6, 0), ('F4', 6, 0), ('F5', 6, 0), ('F6', 6, 0), ('F7', 6, 0), ('F8', 6, 0), ('F9', 6, 0), ('F10', 6, 0), " +
        	    "('G1', 6, 0), ('G2', 6, 0), ('G3', 6, 0), ('G4', 6, 0), ('G5', 6, 0), ('G6', 6, 0), ('G7', 6, 0), ('G8', 6, 0), ('G9', 6, 0), ('G10', 6, 0), " +
        	    "('H1', 6, 0), ('H2', 6, 0), ('H3', 6, 0), ('H4', 6, 0), ('H5', 6, 0), ('H6', 6, 0), ('H7', 6, 0), ('H8', 6, 0), ('H9', 6, 0), ('H10', 6, 0), " +
        	    "('I1', 6, 0), ('I2', 6, 0), ('I3', 6, 0), ('I4', 6, 0), ('I5', 6, 0), ('I6', 6, 0), ('I7', 6, 0), ('I8', 6, 0), ('I9', 6, 0), ('I10', 6, 0), " +
        	    "('J1', 6, 0), ('J2', 6, 0), ('J3', 6, 0), ('J4', 6, 0), ('J5', 6, 0), ('J6', 6, 0), ('J7', 6, 0), ('J8', 6, 0), ('J9', 6, 0), ('J10', 6, 0), " +

        	    "('A1', 7, 0), ('A2', 7, 0), ('A3', 7, 0), ('A4', 7, 0), ('A5', 7, 0), ('A6', 7, 0), ('A7', 7, 0), ('A8', 7, 0), ('A9', 7, 0), ('A10', 7, 0), " +
        	    "('B1', 7, 0), ('B2', 7, 0), ('B3', 7, 0), ('B4', 7, 0), ('B5', 7, 0), ('B6', 7, 0), ('B7', 7, 0), ('B8', 7, 0), ('B9', 7, 0), ('B10', 7, 0), " +
        	    "('C1', 7, 0), ('C2', 7, 0), ('C3', 7, 0), ('C4', 7, 0), ('C5', 7, 0), ('C6', 7, 0), ('C7', 7, 0), ('C8', 7, 0), ('C9', 7, 0), ('C10', 7, 0), " +
        	    "('D1', 7, 0), ('D2', 7, 0), ('D3', 7, 0), ('D4', 7, 0), ('D5', 7, 0), ('D6', 7, 0), ('D7', 7, 0), ('D8', 7, 0), ('D9', 7, 0), ('D10', 7, 0), " +
        	    "('E1', 7, 0), ('E2', 7, 0), ('E3', 7, 0), ('E4', 7, 0), ('E5', 7, 0), ('E6', 7, 0), ('E7', 7, 0), ('E8', 7, 0), ('E9', 7, 0), ('E10', 7, 0), " +
        	    "('F1', 7, 0), ('F2', 7, 0), ('F3', 7, 0), ('F4', 7, 0), ('F5', 7, 0), ('F6', 7, 0), ('F7', 7, 0), ('F8', 7, 0), ('F9', 7, 0), ('F10', 7, 0), " +
        	    "('G1', 7, 0), ('G2', 7, 0), ('G3', 7, 0), ('G4', 7, 0), ('G5', 7, 0), ('G6', 7, 0), ('G7', 7, 0), ('G8', 7, 0), ('G9', 7, 0), ('G10', 7, 0), " +
        	    "('H1', 7, 0), ('H2', 7, 0), ('H3', 7, 0), ('H4', 7, 0), ('H5', 7, 0), ('H6', 7, 0), ('H7', 7, 0), ('H8', 7, 0), ('H9', 7, 0), ('H10', 7, 0), " +
        	    "('I1', 7, 0), ('I2', 7, 0), ('I3', 7, 0), ('I4', 7, 0), ('I5', 7, 0), ('I6', 7, 0), ('I7', 7, 0), ('I8', 7, 0), ('I9', 7, 0), ('I10', 7, 0), " +
        	    "('J1', 7, 0), ('J2', 7, 0), ('J3', 7, 0), ('J4', 7, 0), ('J5', 7, 0), ('J6', 7, 0), ('J7', 7, 0), ('J8', 7, 0), ('J9', 7, 0), ('J10', 7, 0), " +

        	    "('A1', 8, 0), ('A2', 8, 0), ('A3', 8, 0), ('A4', 8, 0), ('A5', 8, 0), ('A6', 8, 0), ('A7', 8, 0), ('A8', 8, 0), ('A9', 8, 0), ('A10', 8, 0), " +
        	    "('B1', 8, 0), ('B2', 8, 0), ('B3', 8, 0), ('B4', 8, 0), ('B5', 8, 0), ('B6', 8, 0), ('B7', 8, 0), ('B8', 8, 0), ('B9', 8, 0), ('B10', 8, 0), " +
        	    "('C1', 8, 0), ('C2', 8, 0), ('C3', 8, 0), ('C4', 8, 0), ('C5', 8, 0), ('C6', 8, 0), ('C7', 8, 0), ('C8', 8, 0), ('C9', 8, 0), ('C10', 8, 0), " +
        	    "('D1', 8, 0), ('D2', 8, 0), ('D3', 8, 0), ('D4', 8, 0), ('D5', 8, 0), ('D6', 8, 0), ('D7', 8, 0), ('D8', 8, 0), ('D9', 8, 0), ('D10', 8, 0), " +
        	    "('E1', 8, 0), ('E2', 8, 0), ('E3', 8, 0), ('E4', 8, 0), ('E5', 8, 0), ('E6', 8, 0), ('E7', 8, 0), ('E8', 8, 0), ('E9', 8, 0), ('E10', 8, 0), " +
        	    "('F1', 8, 0), ('F2', 8, 0), ('F3', 8, 0), ('F4', 8, 0), ('F5', 8, 0), ('F6', 8, 0), ('F7', 8, 0), ('F8', 8, 0), ('F9', 8, 0), ('F10', 8, 0), " +
        	    "('G1', 8, 0), ('G2', 8, 0), ('G3', 8, 0), ('G4', 8, 0), ('G5', 8, 0), ('G6', 8, 0), ('G7', 8, 0), ('G8', 8, 0), ('G9', 8, 0), ('G10', 8, 0), " +
        	    "('H1', 8, 0), ('H2', 8, 0), ('H3', 8, 0), ('H4', 8, 0), ('H5', 8, 0), ('H6', 8, 0), ('H7', 8, 0), ('H8', 8, 0), ('H9', 8, 0), ('H10', 8, 0), " +
        	    "('I1', 8, 0), ('I2', 8, 0), ('I3', 8, 0), ('I4', 8, 0), ('I5', 8, 0), ('I6', 8, 0), ('I7', 8, 0), ('I8', 8, 0), ('I9', 8, 0), ('I10', 8, 0), " +
        	    "('J1', 8, 0), ('J2', 8, 0), ('J3', 8, 0), ('J4', 8, 0), ('J5', 8, 0), ('J6', 8, 0), ('J7', 8, 0), ('J8', 8, 0), ('J9', 8, 0), ('J10', 8, 0), " +

        	    "('A1', 9, 0), ('A2', 9, 0), ('A3', 9, 0), ('A4', 9, 0), ('A5', 9, 0), ('A6', 9, 0), ('A7', 9, 0), ('A8', 9, 0), ('A9', 9, 0), ('A10', 9, 0), " +
        	    "('B1', 9, 0), ('B2', 9, 0), ('B3', 9, 0), ('B4', 9, 0), ('B5', 9, 0), ('B6', 9, 0), ('B7', 9, 0), ('B8', 9, 0), ('B9', 9, 0), ('B10', 9, 0), " +
        	    "('C1', 9, 0), ('C2', 9, 0), ('C3', 9, 0), ('C4', 9, 0), ('C5', 9, 0), ('C6', 9, 0), ('C7', 9, 0), ('C8', 9, 0), ('C9', 9, 0), ('C10', 9, 0), " +
        	    "('D1', 9, 0), ('D2', 9, 0), ('D3', 9, 0), ('D4', 9, 0), ('D5', 9, 0), ('D6', 9, 0), ('D7', 9, 0), ('D8', 9, 0), ('D9', 9, 0), ('D10', 9, 0), " +
        	    "('E1', 9, 0), ('E2', 9, 0), ('E3', 9, 0), ('E4', 9, 0), ('E5', 9, 0), ('E6', 9, 0), ('E7', 9, 0), ('E8', 9, 0), ('E9', 9, 0), ('E10', 9, 0), " +
        	    "('F1', 9, 0), ('F2', 9, 0), ('F3', 9, 0), ('F4', 9, 0), ('F5', 9, 0), ('F6', 9, 0), ('F7', 9, 0), ('F8', 9, 0), ('F9', 9, 0), ('F10', 9, 0), " +
        	    "('G1', 9, 0), ('G2', 9, 0), ('G3', 9, 0), ('G4', 9, 0), ('G5', 9, 0), ('G6', 9, 0), ('G7', 9, 0), ('G8', 9, 0), ('G9', 9, 0), ('G10', 9, 0), " +
        	    "('H1', 9, 0), ('H2', 9, 0), ('H3', 9, 0), ('H4', 9, 0), ('H5', 9, 0), ('H6', 9, 0), ('H7', 9, 0), ('H8', 9, 0), ('H9', 9, 0), ('H10', 9, 0), " +
        	    "('I1', 9, 0), ('I2', 9, 0), ('I3', 9, 0), ('I4', 9, 0), ('I5', 9, 0), ('I6', 9, 0), ('I7', 9, 0), ('I8', 9, 0), ('I9', 9, 0), ('I10', 9, 0), " +
        	    "('J1', 9, 0), ('J2', 9, 0), ('J3', 9, 0), ('J4', 9, 0), ('J5', 9, 0), ('J6', 9, 0), ('J7', 9, 0), ('J8', 9, 0), ('J9', 9, 0), ('J10', 9, 0), " +

        	    "('A1', 10, 0), ('A2', 10, 0), ('A3', 10, 0), ('A4', 10, 0), ('A5', 10, 0), ('A6', 10, 0), ('A7', 10, 0), ('A8', 10, 0), ('A9', 10, 0), ('A10', 10, 0), " +
        	    "('B1', 10, 0), ('B2', 10, 0), ('B3', 10, 0), ('B4', 10, 0), ('B5', 10, 0), ('B6', 10, 0), ('B7', 10, 0), ('B8', 10, 0), ('B9', 10, 0), ('B10', 10, 0), " +
        	    "('C1', 10, 0), ('C2', 10, 0), ('C3', 10, 0), ('C4', 10, 0), ('C5', 10, 0), ('C6', 10, 0), ('C7', 10, 0), ('C8', 10, 0), ('C9', 10, 0), ('C10', 10, 0), " +
        	    "('D1', 10, 0), ('D2', 10, 0), ('D3', 10, 0), ('D4', 10, 0), ('D5', 10, 0), ('D6', 10, 0), ('D7', 10, 0), ('D8', 10, 0), ('D9', 10, 0), ('D10', 10, 0), " +
        	    "('E1', 10, 0), ('E2', 10, 0), ('E3', 10, 0), ('E4', 10, 0), ('E5', 10, 0), ('E6', 10, 0), ('E7', 10, 0), ('E8', 10, 0), ('E9', 10, 0), ('E10', 10, 0), " +
        	    "('F1', 10, 0), ('F2', 10, 0), ('F3', 10, 0), ('F4', 10, 0), ('F5', 10, 0), ('F6', 10, 0), ('F7', 10, 0), ('F8', 10, 0), ('F9', 10, 0), ('F10', 10, 0), " +
        	    "('G1', 10, 0), ('G2', 10, 0), ('G3', 10, 0), ('G4', 10, 0), ('G5', 10, 0), ('G6', 10, 0), ('G7', 10, 0), ('G8', 10, 0), ('G9', 10, 0), ('G10', 10, 0), " +
        	    "('H1', 10, 0), ('H2', 10, 0), ('H3', 10, 0), ('H4', 10, 0), ('H5', 10, 0), ('H6', 10, 0), ('H7', 10, 0), ('H8', 10, 0), ('H9', 10, 0), ('H10', 10, 0), " +
        	    "('I1', 10, 0), ('I2', 10, 0), ('I3', 10, 0), ('I4', 10, 0), ('I5', 10, 0), ('I6', 10, 0), ('I7', 10, 0), ('I8', 10, 0), ('I9', 10, 0), ('I10', 10, 0), " +
        	    "('J1', 10, 0), ('J2', 10, 0), ('J3', 10, 0), ('J4', 10, 0), ('J5', 10, 0), ('J6', 10, 0), ('J7', 10, 0), ('J8', 10, 0), ('J9', 10, 0), ('J10', 10, 0), " +

        	    "('A1', 11, 0), ('A2', 11, 0), ('A3', 11, 0), ('A4', 11, 0), ('A5', 11, 0), ('A6', 11, 0), ('A7', 11, 0), ('A8', 11, 0), ('A9', 11, 0), ('A10', 11, 0), " +
        	    "('B1', 11, 0), ('B2', 11, 0), ('B3', 11, 0), ('B4', 11, 0), ('B5', 11, 0), ('B6', 11, 0), ('B7', 11, 0), ('B8', 11, 0), ('B9', 11, 0), ('B10', 11, 0), " +
        	    "('C1', 11, 0), ('C2', 11, 0), ('C3', 11, 0), ('C4', 11, 0), ('C5', 11, 0), ('C6', 11, 0), ('C7', 11, 0), ('C8', 11, 0), ('C9', 11, 0), ('C10', 11, 0), " +
        	    "('D1', 11, 0), ('D2', 11, 0), ('D3', 11, 0), ('D4', 11, 0), ('D5', 11, 0), ('D6', 11, 0), ('D7', 11, 0), ('D8', 11, 0), ('D9', 11, 0), ('D10', 11, 0), " +
        	    "('E1', 11, 0), ('E2', 11, 0), ('E3', 11, 0), ('E4', 11, 0), ('E5', 11, 0), ('E6', 11, 0), ('E7', 11, 0), ('E8', 11, 0), ('E9', 11, 0), ('E10', 11, 0), " +
        	    "('F1', 11, 0), ('F2', 11, 0), ('F3', 11, 0), ('F4', 11, 0), ('F5', 11, 0), ('F6', 11, 0), ('F7', 11, 0), ('F8', 11, 0), ('F9', 11, 0), ('F10', 11, 0), " +
        	    "('G1', 11, 0), ('G2', 11, 0), ('G3', 11, 0), ('G4', 11, 0), ('G5', 11, 0), ('G6', 11, 0), ('G7', 11, 0), ('G8', 11, 0), ('G9', 11, 0), ('G10', 11, 0), " +
        	    "('H1', 11, 0), ('H2', 11, 0), ('H3', 11, 0), ('H4', 11, 0), ('H5', 11, 0), ('H6', 11, 0), ('H7', 11, 0), ('H8', 11, 0), ('H9', 11, 0), ('H10', 11, 0), " +
        	    "('I1', 11, 0), ('I2', 11, 0), ('I3', 11, 0), ('I4', 11, 0), ('I5', 11, 0), ('I6', 11, 0), ('I7', 11, 0), ('I8', 11, 0), ('I9', 11, 0), ('I10', 11, 0), " +
        	    "('J1', 11, 0), ('J2', 11, 0), ('J3', 11, 0), ('J4', 11, 0), ('J5', 11, 0), ('J6', 11, 0), ('J7', 11, 0), ('J8', 11, 0), ('J9', 11, 0), ('J10', 11, 0), " +

        	    "('A1', 12, 0), ('A2', 12, 0), ('A3', 12, 0), ('A4', 12, 0), ('A5', 12, 0), ('A6', 12, 0), ('A7', 12, 0), ('A8', 12, 0), ('A9', 12, 0), ('A10', 12, 0), " +
        	    "('B1', 12, 0), ('B2', 12, 0), ('B3', 12, 0), ('B4', 12, 0), ('B5', 12, 0), ('B6', 12, 0), ('B7', 12, 0), ('B8', 12, 0), ('B9', 12, 0), ('B10', 12, 0), " +
        	    "('C1', 12, 0), ('C2', 12, 0), ('C3', 12, 0), ('C4', 12, 0), ('C5', 12, 0), ('C6', 12, 0), ('C7', 12, 0), ('C8', 12, 0), ('C9', 12, 0), ('C10', 12, 0), " +
        	    "('D1', 12, 0), ('D2', 12, 0), ('D3', 12, 0), ('D4', 12, 0), ('D5', 12, 0), ('D6', 12, 0), ('D7', 12, 0), ('D8', 12, 0), ('D9', 12, 0), ('D10', 12, 0), " +
        	    "('E1', 12, 0), ('E2', 12, 0), ('E3', 12, 0), ('E4', 12, 0), ('E5', 12, 0), ('E6', 12, 0), ('E7', 12, 0), ('E8', 12, 0), ('E9', 12, 0), ('E10', 12, 0), " +
        	    "('F1', 12, 0), ('F2', 12, 0), ('F3', 12, 0), ('F4', 12, 0), ('F5', 12, 0), ('F6', 12, 0), ('F7', 12, 0), ('F8', 12, 0), ('F9', 12, 0), ('F10', 12, 0), " +
        	    "('G1', 12, 0), ('G2', 12, 0), ('G3', 12, 0), ('G4', 12, 0), ('G5', 12, 0), ('G6', 12, 0), ('G7', 12, 0), ('G8', 12, 0), ('G9', 12, 0), ('G10', 12, 0), " +
        	    "('H1', 12, 0), ('H2', 12, 0), ('H3', 12, 0), ('H4', 12, 0), ('H5', 12, 0), ('H6', 12, 0), ('H7', 12, 0), ('H8', 12, 0), ('H9', 12, 0), ('H10', 12, 0), " +
        	    "('I1', 12, 0), ('I2', 12, 0), ('I3', 12, 0), ('I4', 12, 0), ('I5', 12, 0), ('I6', 12, 0), ('I7', 12, 0), ('I8', 12, 0), ('I9', 12, 0), ('I10', 12, 0), " +
        	    "('J1', 12, 0), ('J2', 12, 0), ('J3', 12, 0), ('J4', 12, 0), ('J5', 12, 0), ('J6', 12, 0), ('J7', 12, 0), ('J8', 12, 0), ('J9', 12, 0), ('J10', 12, 0);",

        	    "INSERT INTO bookings (PaymentMethod, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES " +
        	    "('Card', '1', 10000, '2', '2024-06-07'), " +
        	    "('Card', '1', 10000, '2', '2024-06-07'), " +
        	    "('Cash', '1', 10000, '2', '2024-06-07'), " +
        	    "('Card', '1', 10000, '4', '2024-06-07'), " +
        	    "('Card', '1', 10000, '6', '2024-06-07'), " +
        	    "('Card', '1', 10000, '7', '2024-06-07'), " +
        	    "('Cash', '1', 10000, '2', '2024-06-07'), " +
        	    "('Cash', '1', 10000, '2', '2024-06-07'), " +
        	    "('Cash', '1', 10000, '9', '2024-06-07'), " +
        	    "('Card', '1', 10000, '10', '2024-06-07'), " +
        	    "('Cash', '1', 10000, '11', '2024-06-07'), " +
        	    "('Cash', '1', 10000, '12', '2024-06-07');",

        	    "INSERT INTO tickets (ScheduleID, TheaterID, SeatID, BookingID, IsIssued, StandardPrice, SalePrice) VALUES " +
        	    "(1, 1, 'A1', 1, 1, 10000, 10000), " +
        	    "(2, 2, 'A2', 2, 1, 10000, 10000), " +
        	    "(3, 3, 'A3', 3, 1, 10000, 10000), " +
        	    "(4, 4, 'A4', 4, 1, 10000, 10000), " +
        	    "(5, 5, 'A5', 5, 1, 10000, 10000), " +
        	    "(6, 1, 'A6', 6, 1, 10000, 10000), " +
        	    "(7, 2, 'B1', 7, 1, 10000, 10000), " +
        	    "(8, 3, 'C1', 8, 1, 10000, 10000), " +
        	    "(9, 4, 'D1', 9, 1, 10000, 20000), " +
        	    "(10, 5, 'E1', 10, 1, 10000, 10000), " +
        	    "(11, 1, 'A7', 11, 1, 10000, 10000), " +
        	    "(12, 2, 'B2', 12, 1, 10000, 10000);"
        	};




            for (String query : dataInsertionQueries) {
                stmt.executeUpdate(query);
            }

            stmt.executeUpdate("SET SQL_MODE=@OLD_SQL_MODE;");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;");
            stmt.executeUpdate("SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;");
            
            JOptionPane.showMessageDialog(this, "데이터베이스 초기화 완료.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "데이터베이스 초기화 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeSQL(String query) {
        try (Statement stmt = conn.createStatement()) {
            boolean isResultSet = stmt.execute(query);
            if (isResultSet) {
                ResultSet rs = stmt.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                StringBuilder result = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    result.append(metaData.getColumnName(i)).append("\t");
                }
                result.append("\n");
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(rs.getString(i)).append("\t");
                    }
                    result.append("\n");
                }
                JTextArea resultArea = new JTextArea(result.toString());
                resultArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(resultArea);
                JFrame resultFrame = new JFrame("SQL 결과");
                resultFrame.setSize(600, 400);
                resultFrame.add(scrollPane);
                resultFrame.setVisible(true);
            } else {
                int updateCount = stmt.getUpdateCount();
                JOptionPane.showMessageDialog(this, "쿼리 실행 완료: " + updateCount + "개의 행에 영향을 미쳤습니다.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "쿼리 실행 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAllTablesWindow() {
        JFrame allTablesFrame = new JFrame("전체 테이블 보기");
        allTablesFrame.setSize(900, 900);

        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
            while (rs.next()) {
                String tableName = rs.getString(1);
                resultArea.append("테이블: " + tableName + "\n");
                try (Statement tableStmt = conn.createStatement();
                     ResultSet tableRs = tableStmt.executeQuery("SELECT * FROM " + tableName)) {
                    ResultSetMetaData metaData = tableRs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (tableRs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            resultArea.append(metaData.getColumnName(i) + ": " + tableRs.getString(i) + "\t");
                        }
                        resultArea.append("\n");
                    }
                }
                resultArea.append("\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "테이블 조회 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }

        allTablesFrame.add(scrollPane);
        allTablesFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel());
    }
}
