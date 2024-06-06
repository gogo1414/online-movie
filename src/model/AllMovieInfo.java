package model;

import java.util.List;

public class AllMovieInfo {
	public static Customer customer;
	public static Booking booking;
	public static Movie movie;
	public static Schedule schedule;
	public static Seat seats;
	public static Theater theater;
	public static Ticket ticket;
	public static String catalog;
	public static String whatIsIt;
	public static int bookingID;
	public static int nowIdx =0;
	public static int changeReservation = 0;
	
	public static void init() {
		booking  = null;
		movie = null;
		schedule = null;
		seats = null;
		theater = null;
		ticket= null;
		catalog = null;
		whatIsIt = null;
		bookingID = 0;
		changeReservation = 0;
		
	}

}
