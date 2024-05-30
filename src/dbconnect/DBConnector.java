package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

	public void createTable() {
		// 데이터베이스 연결 정보
        String url = "jdbc:mysql://localhost:3306/dbtest";
        String user = "root";  // MySQL 사용자명
        String password = "1234";  // MySQL 비밀번호

        String[] sqlStatements = {
        		"SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;",
                "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;",
                "SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';",
                "CREATE SCHEMA IF NOT EXISTS `dbtest` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;",
                "USE `dbtest`;",
                "DROP TABLE IF EXISTS `dbtest`.`tickets`;",
                "DROP TABLE IF EXISTS `dbtest`.`seats`;",
                "DROP TABLE IF EXISTS `dbtest`.`schedules`;",
                "DROP TABLE IF EXISTS `dbtest`.`theaters`;",
                "DROP TABLE IF EXISTS `dbtest`.`movies`;",
                "DROP TABLE IF EXISTS `dbtest`.`bookings`;",
                "DROP TABLE IF EXISTS `dbtest`.`customers`;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`customers` (" +
                "`CustomerID` VARCHAR(50) NOT NULL," +
                "`Name` VARCHAR(100) NOT NULL," +
                "`Phone` VARCHAR(20) NOT NULL," +
                "`Email` VARCHAR(100) NOT NULL," +
                "`IsAdmin` TINYINT(1) NOT NULL," +
                "PRIMARY KEY (`CustomerID`))" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`bookings` (" +
                "`BookingID` INT NOT NULL," +
                "`PaymentMethod` VARCHAR(50) NOT NULL," +
                "`PaymentStatus` VARCHAR(50) NOT NULL," +
                "`Amount` INT NOT NULL," +
                "`CustomerID` VARCHAR(50) NOT NULL," +
                "`PaymentDate` DATE NOT NULL," +
                "PRIMARY KEY (`BookingID`)," +
                "INDEX `CustomerID` (`CustomerID` ASC) VISIBLE," +
                "CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `dbtest`.`customers` (`CustomerID`) ON DELETE CASCADE ON UPDATE CASCADE)" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`movies` (" +
                "`MovieID` INT NOT NULL," +
                "`Title` VARCHAR(255) NOT NULL," +
                "`Duration` VARCHAR(50) NOT NULL," +
                "`Rating` VARCHAR(50) NOT NULL," +
                "`Director` VARCHAR(100) NOT NULL," +
                "`Actors` VARCHAR(255) NOT NULL," +
                "`Genre` VARCHAR(50) NOT NULL," +
                "`Story` TEXT NOT NULL," +
                "`ReleaseDate` DATE NOT NULL," +
                "`Score` INT NOT NULL," +
                "PRIMARY KEY (`MovieID`))" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`theaters` (" +
                "`TheaterID` INT NOT NULL," +
                "`SeatCount` INT NOT NULL," +
                "`IsActive` TINYINT(1) NOT NULL," +
                "`Width` INT NOT NULL," +
                "`Height` INT NOT NULL," +
                "PRIMARY KEY (`TheaterID`))" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`schedules` (" +
                "`ScheduleID` INT NOT NULL," +
                "`MovieID` INT NOT NULL," +
                "`TheaterID` INT NOT NULL," +
                "`StartDate` DATE NOT NULL," +
                "`Weekday` VARCHAR(50) NOT NULL," +
                "`ShowNumber` INT NOT NULL," +
                "`StartTime` TIME NOT NULL," +
                "PRIMARY KEY (`ScheduleID`)," +
                "INDEX `MovieID` (`MovieID` ASC) VISIBLE," +
                "INDEX `TheaterID` (`TheaterID` ASC) VISIBLE," +
                "CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`MovieID`) REFERENCES `dbtest`.`movies` (`MovieID`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `schedules_ibfk_2` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) ON DELETE CASCADE ON UPDATE CASCADE)" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`seats` (" +
                "`SeatID` VARCHAR(10) NOT NULL," +
                "`TheaterID` INT NOT NULL," +
                "`IsOccupied` TINYINT(1) NOT NULL," +
                "PRIMARY KEY (`SeatID`)," +
                "INDEX `TheaterID` (`TheaterID` ASC) VISIBLE," +
                "CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) ON DELETE CASCADE ON UPDATE CASCADE)" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "CREATE TABLE IF NOT EXISTS `dbtest`.`tickets` (" +
                "`TicketID` INT NOT NULL," +
                "`ScheduleID` INT NOT NULL," +
                "`TheaterID` INT NOT NULL," +
                "`SeatID` VARCHAR(10) NOT NULL," +
                "`BookingID` INT NOT NULL," +
                "`IsIssued` TINYINT(1) NOT NULL," +
                "`StandardPrice` INT NOT NULL," +
                "`SalePrice` INT NOT NULL," +
                "PRIMARY KEY (`TicketID`)," +
                "INDEX `ScheduleID` (`ScheduleID` ASC) VISIBLE," +
                "INDEX `TheaterID` (`TheaterID` ASC) VISIBLE," +
                "INDEX `SeatID` (`SeatID` ASC) VISIBLE," +
                "INDEX `BookingID` (`BookingID` ASC) VISIBLE," +
                "CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`ScheduleID`) REFERENCES `dbtest`.`schedules` (`ScheduleID`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `tickets_ibfk_2` FOREIGN KEY (`TheaterID`) REFERENCES `dbtest`.`theaters` (`TheaterID`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `tickets_ibfk_3` FOREIGN KEY (`SeatID`) REFERENCES `dbtest`.`seats` (`SeatID`) ON DELETE CASCADE ON UPDATE CASCADE," +
                "CONSTRAINT `tickets_ibfk_4` FOREIGN KEY (`BookingID`) REFERENCES `dbtest`.`bookings` (`BookingID`) ON DELETE CASCADE ON UPDATE CASCADE)" +
                "ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;",
                "SET SQL_MODE=@OLD_SQL_MODE;",
                "SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;",
                "SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;"
            };
        Connection connection;
        Statement statement = null;

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
        	connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            for (String sql : sqlStatements) {
                statement.execute(sql);
            }
            
            System.out.println("SQL script executed successfully.");
        } catch(ClassNotFoundException e) {
        	System.out.println("JDBC 드라이버 로드 오류");
		} catch (SQLException e) {
			System.out.println("SQL 실행오류");
        }
    }
}
