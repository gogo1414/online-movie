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
            stmt.executeUpdate("DROP DATABASE dbtest");
            stmt.executeUpdate("CREATE DATABASE dbtest");
            
            stmt.executeUpdate("USE dbtest");
            
            // 추가할 쿼리들
            stmt.executeUpdate("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
            stmt.executeUpdate("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
            stmt.executeUpdate("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';");

            stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS `dbtest` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;");
            stmt.executeUpdate("USE `dbtest` ;");

            // 테이블 생성
            String[] tableCreationQueries = new String[] {
            	    "CREATE TABLE IF NOT EXISTS `dbtest`.`customers` (" +
            	    "`CustomerID` VARCHAR(50) NOT NULL, `Name` VARCHAR(100) NOT NULL, `Phone` VARCHAR(20) NOT NULL, " +
            	    "`Email` VARCHAR(100) NOT NULL, `IsAdmin` TINYINT(1) NOT NULL, `Password` VARCHAR(20) NOT NULL, " +
            	    "PRIMARY KEY (`CustomerID`)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

            	    "CREATE TABLE IF NOT EXISTS `dbtest`.`bookings` (" +
            	    "`BookingID` INT NOT NULL AUTO_INCREMENT, `PaymentMethod` VARCHAR(50) NOT NULL, `PaymentStatus` VARCHAR(50) NOT NULL, " +
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
            	    "`SeatID` VARCHAR(10) NOT NULL, `TheaterID` INT NOT NULL, `IsOccupied` TINYINT(1) NOT NULL, PRIMARY KEY (`SeatID`), " +
            	    "INDEX `TheaterID` (`TheaterID` ASC) VISIBLE, CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) " +
            	    "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",

            	    "CREATE TABLE IF NOT EXISTS `dbtest`.`tickets` (" +
            	    "`TicketID` INT NOT NULL AUTO_INCREMENT, `ScheduleID` INT NOT NULL, `TheaterID` INT NOT NULL, `SeatID` VARCHAR(10) NOT NULL, `BookingID` INT NOT NULL, " +
            	    "`IsIssued` TINYINT(1) NOT NULL, `StandardPrice` INT NOT NULL, `SalePrice` INT NOT NULL, PRIMARY KEY (`TicketID`), " +
            	    "INDEX `ScheduleID` (`ScheduleID` ASC) VISIBLE, INDEX `TheaterID` (`TheaterID` ASC) VISIBLE, INDEX `SeatID` (`SeatID` ASC) VISIBLE, " +
            	    "INDEX `BookingID` (`BookingID` ASC) VISIBLE, CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`ScheduleID`) REFERENCES `dbtest`.`schedules` (`ScheduleID`) " +
            	    "ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `tickets_ibfk_2` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) " +
            	    "ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `tickets_ibfk_3` FOREIGN KEY (`SeatID`) REFERENCES `dbtest`.`seats` (`SeatID`) " +
            	    "ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `tickets_ibfk_4` FOREIGN KEY (`BookingID`) REFERENCES `dbtest`.`bookings` (`BookingID`) " +
            	    "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;"
            	};


            for (String query : tableCreationQueries) {
                stmt.executeUpdate(query);
            }

            // 데이터 삽입
            String[] dataInsertionQueries = new String[] {
            	    "INSERT INTO customers (CustomerID, Name, Phone, Email, IsAdmin, Password) VALUES" +
            	    "('1', 'root', '12customers34567890', 'john@example.com', 0, '1234')," +
            	    "('2', 'user1', '0987654321', 'admin@example.com', 1, 'user1');",

            	    "INSERT INTO movies (Title, Duration, Rating, Director, Actors, Genre, Story, ReleaseDate, Score) VALUES" +
            	    "('Movie 1', '120 min', 'PG-13', 'Director 1', '김준수', 'Action', 'Story 1', '2023-01-15', 85)," +
            	    "('Movie 2', '110 min', 'R', 'Director 2', '이동호', 'Horror', 'Story 2', '2023-02-12', 78)," +
            	    "('Movie 3', '130 min', 'PG', 'Director 3', '홍길동', 'Comedy', 'Story 3', '2023-03-05', 88)," +
            	    "('Movie 4', '140 min', 'PG-13', 'Director 4', '김준수', 'Drama', 'Story 4', '2023-04-10', 92)," +
            	    "('Movie 5', '125 min', 'R', 'Director 5', '이동호', 'Thriller', 'Story 5', '2023-05-20', 76)," +
            	    "('Movie 6', '115 min', 'PG', 'Director 6', '홍길동', 'Adventure', 'Story 6', '2023-06-18', 80)," +
            	    "('Movie 7', '100 min', 'PG', 'Director 7', '김준수', 'Animation', 'Story 7', '2023-07-22', 90)," +
            	    "('Movie 8', '135 min', 'R', 'Director 8', '이동호', 'Sci-Fi', 'Story 8', '2023-08-30', 87)," +
            	    "('Movie 9', '145 min', 'PG-13', 'Director 9', '홍길동', 'Fantasy', 'Story 9', '2023-09-12', 83)," +
            	    "('Movie 10', '120 min', 'R', 'Director 10', '김준수', 'Mystery', 'Story 10', '2023-10-08', 79)," +
            	    "('Movie 11', '110 min', 'PG-13', 'Director 11', '이동호', 'Romance', 'Story 11', '2023-11-16', 81)," +
            	    "('Movie 12', '105 min', 'PG', 'Director 12', '홍길동', 'Family', 'Story 12', '2023-12-25', 84);",

            	    "INSERT INTO theaters (SeatCount, TheaterName, IsActive, Width, Height) VALUES" +
            	    "(16, '1대입구', 1, 4, 4)," +
            	    "(16, '2대입구', 1, 4, 4)," +
            	    "(16, '3대입구', 1, 4, 4)," +
            	    "(16, '4대입구', 1, 4, 4)," +
            	    "(16, '5대입구', 1, 4, 4)," +
            	    "(16, '6대입구', 1, 4, 4)," +
            	    "(16, '7대입구', 1, 4, 4)," +
            	    "(16, '8대입구', 1, 4, 4)," +
            	    "(16, '9대입구', 1, 4, 4)," +
            	    "(16, '10대입구', 1, 4, 4)," +
            	    "(16, '11대입구', 1, 4, 4)," +
            	    "(16, '12대입구', 1, 4, 4);",

            	    "INSERT INTO schedules (MovieID, TheaterID, StartDate, Weekday, ShowNumber, StartTime) VALUES" +
            	    "(1, 1, '2023-01-15', 'Monday', 1, '10:00:00')," +
            	    "(2, 2, '2023-02-12', 'Tuesday', 1, '12:00:00')," +
            	    "(3, 3, '2023-03-05', 'Wednesday', 1, '14:00:00')," +
            	    "(4, 4, '2023-04-10', 'Thursday', 1, '16:00:00')," +
            	    "(5, 5, '2023-05-20', 'Friday', 1, '18:00:00')," +
            	    "(6, 6, '2023-06-18', 'Saturday', 1, '20:00:00')," +
            	    "(7, 7, '2023-07-22', 'Sunday', 1, '22:00:00')," +
            	    "(8, 8, '2023-08-30', 'Monday', 1, '11:00:00')," +
            	    "(9, 9, '2023-09-12', 'Tuesday', 1, '13:00:00')," +
            	    "(10, 10, '2023-10-08', 'Wednesday', 1, '15:00:00')," +
            	    "(11, 11, '2023-11-16', 'Thursday', 1, '17:00:00')," +
            	    "(12, 12, '2023-12-25', 'Friday', 1, '19:00:00');",

            	    "INSERT INTO seats (SeatID, TheaterID, IsOccupied) VALUES" +
            	    "('A1', 1, 0), ('A2', 1, 0), ('A3', 1, 0), ('A4', 1, 0)," +
            	    "('A5', 1, 0), ('A6', 1, 0), ('A7', 1, 0), ('A8', 1, 0)," +
            	    "('A9', 1, 0), ('A10', 1, 0), ('A11', 1, 0), ('A12', 1, 0)," +
            	    "('A13', 1, 0), ('A14', 1, 0), ('A15', 1, 0), ('A16', 1, 0);",

            	    "INSERT INTO bookings (PaymentMethod, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES" +
            	    "('Credit Card', 'Paid', 150, '1', '2023-01-10')," +
            	    "('Credit Card', 'Paid', 120, '2', '2023-02-15')," +
            	    "('PayPal', 'Paid', 130, '2', '2023-03-20')," +
            	    "('Credit Card', 'Paid', 140, '1', '2023-04-25')," +
            	    "('Debit Card', 'Paid', 160, '1', '2023-05-30')," +
            	    "('Credit Card', 'Paid', 170, '1', '2023-06-05')," +
            	    "('PayPal', 'Paid', 180, '1', '2023-07-10')," +
            	    "('Credit Card', 'Paid', 190, '2', '2023-08-15')," +
            	    "('Debit Card', 'Paid', 200, '1', '2023-09-20')," +
            	    "('Credit Card', 'Paid', 210, '2', '2023-10-25')," +
            	    "('PayPal', 'Paid', 220, '1', '2023-11-30')," +
            	    "('Debit Card', 'Paid', 230, '2', '2023-12-05');",

            	    "INSERT INTO tickets (ScheduleID, TheaterID, SeatID, BookingID, IsIssued, StandardPrice, SalePrice) VALUES" +
            	    "(1, 1, 'A1', 1, 1, 15000, 15000)," +
            	    "(2, 2, 'A2', 2, 1, 12000, 12000)," +
            	    "(3, 3, 'A3', 3, 1, 13000, 13000)," +
            	    "(4, 4, 'A4', 4, 1, 14000, 14000)," +
            	    "(5, 5, 'A5', 5, 1, 16000, 16000)," +
            	    "(6, 6, 'A6', 6, 1, 17000, 17000)," +
            	    "(7, 7, 'A7', 7, 1, 18000, 18000)," +
            	    "(8, 8, 'A8', 8, 1, 19000, 19000)," +
            	    "(9, 9, 'A9', 9, 1, 20000, 20000)," +
            	    "(10, 10, 'A10', 10, 1, 21000, 21000)," +
            	    "(11, 11, 'A11', 11, 1, 22000, 22000)," +
            	    "(12, 12, 'A12', 12, 1, 23000, 23000);"
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
        allTablesFrame.setSize(1000, 1000);

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
