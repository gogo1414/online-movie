package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.MovieDAO;
import dao.ScheduleDAO;
import dao.SeatDAO;
import dao.TheaterDAO;
import model.AllMovieInfo;
import model.Movie;
import model.Schedule;
import model.Theater;
import model.Ticket;

public class BookingScreenX extends JFrame {
    private Movie movie;
    private Ticket ticket;
    private Theater theater;
    
    private TheaterDAO theaterDAO = new TheaterDAO();
    private SeatDAO seatDAO = new SeatDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();
    
    private JPanel mainPanel = new JPanel();
	private JPanel mNorthPanel = new JPanel();
	private JPanel mCenterPanel = new JPanel();
	private JPanel mEastPanel = new JPanel();
    
    private JPanel mCenterMoviePanel = new JPanel();
	private JPanel mEastSouthPanel = new JPanel();
	private JPanel mEastSouthCenterPanel = new JPanel();
	private JPanel mCenterTheatherPanel = new JPanel();
	private JPanel mCenterCalendarPanel = new JPanel();
	private JPanel mCenerButtomPanel = new JPanel();
	private JPanel roomPanel = new JPanel();
	
	private JLabel theatherLabel = new JLabel("-");
	private JLabel dayLabel = new JLabel("-");
	private JLabel personLabel = new JLabel("-");
	private JLabel costLabel = new JLabel("-");
	private JLabel choiceMovieLabel = new JLabel("영화 선택");
	
	private JLabel choiceTheaterLabel = new JLabel("극장 선택");
	private JLabel choiceCalendarLabel = new JLabel("날짜 선택");
	
	private ButtonGroup adultCount = new ButtonGroup();
	private ButtonGroup teenagerCount = new ButtonGroup();
	
	private JRadioButton one = new JRadioButton("1");
	private JRadioButton two = new JRadioButton("2");
	private JRadioButton three = new JRadioButton("3");
	private JRadioButton four = new JRadioButton("4");
	private JRadioButton five = new JRadioButton("5");
	private JRadioButton zero = new JRadioButton("0");
	
	private JRadioButton oneT = new JRadioButton("1");
	private JRadioButton twoT = new JRadioButton("2");
	private JRadioButton threeT = new JRadioButton("3");
	private JRadioButton fourT = new JRadioButton("4");
	private JRadioButton fiveT = new JRadioButton("5");
	private JRadioButton zeroT = new JRadioButton("0");
	
	private JButton ticketingButton = new JButton("예매하기");
	
	//관 정보
	private JLabel roomNumber1 = new JLabel("");
	private ButtonGroup timeGroup= new ButtonGroup();
	private JRadioButton timeButton1 = new JRadioButton("9:00~");
	private JRadioButton timeButton2 = new JRadioButton("12:00~");
	private JRadioButton timeButton3 = new JRadioButton("15:00~");
	private JRadioButton timeButton4 = new JRadioButton("18:00~");
	private JRadioButton timeButton5 = new JRadioButton("21:00~");
	//여기 관련 내용을 Database에서 꺼내게 바꾼다. 
	
	private List<String> movies;
	private DefaultListModel<String> listModel = new DefaultListModel<>();
	private JList movielist;
	private JButton backButton = new JButton("Back");
	
	public void loadMovies() throws SQLException {
		movies = movieDAO.getAllMoviesName();
		for(String movieName : movies) {
			listModel.addElement(movieName);
		}
		movielist = new JList<>(listModel);
	}
	
	private List<String> theathers;
	private DefaultListModel<String> listModel2 = new DefaultListModel<>();
	private JList theatherlist;
	
	public void loadTheaters() throws SQLException {
		theathers = theaterDAO.getAllTheatersName();
		for(String theaterName : theathers) {
			listModel2.addElement(theaterName);
		}
		theatherlist = new JList<>(listModel2);
	}
	
	private List<String> startDates;
	private DefaultListModel<String> listModel3 = new DefaultListModel<>();
	private JList dateList;
	
	public void loadSchedule() throws SQLException {
		startDates = scheduleDAO.getAllScheduleDate();
		for(String startDate : startDates) {
			listModel3.addElement(startDate);
		}
		dateList = new JList<>(listModel3);
	}
	

	//check 관련
	private String movieName ="";
	private String theatherName ="";
	private String checkDay; 
	private int roomNumber;  //관정보  
	private int time; //회차 정보 
	
	private static final int ADULTCOST = 10000;
	private static final int TEENAGECOST = 8000;
	
	// 가격
	private int totalCost = 0;
	private int adultCost = 0;
	private int teenagerCost = 0;
	private int person = 0;

    public BookingScreenX(Movie movie) {
        this.movie = movie;
        theaterDAO = new TheaterDAO();
        seatDAO = new SeatDAO();

        setTitle("티켓 예메");
		setSize(900, 700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 화면 가운데 출력시키기
		setResizable(false);
		setVisible(true);
		getContentPane().setLayout(null);
		mainPanel.setBounds(13, 0, 799, 583);
		

		// main
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		mNorthPanel.setBounds(0, 0, 799, 28);
		

		mainPanel.add(mNorthPanel);
		mNorthPanel.setLayout(null);
		JLabel nameLabel = new JLabel(MainMenuScreen.customer.getName() + " 고객님 예매");
		nameLabel.setBounds(5, 5, 789, 18);
		nameLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		nameLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		nameLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 15));
		mNorthPanel.add(nameLabel);
		
		
		mEastPanel.setBounds(598, 38, 189, 600);
		mainPanel.add(mEastPanel);
		
		mEastPanel.setLayout(null);
		mEastSouthPanel.setBounds(0, 390, 178, 155);
		
		
		mEastPanel.add(mEastSouthPanel);
		
		mEastSouthPanel.setLayout(new BorderLayout(0, 0));
		JLabel blank_5 = new JLabel(" ");
		JLabel blank_6 = new JLabel(" ");
		JLabel blank_7 = new JLabel(" ");
		JLabel blank_8 = new JLabel(" ");
		mEastSouthPanel.add(blank_5, BorderLayout.NORTH);
		mEastSouthPanel.add(blank_6, BorderLayout.SOUTH);
		mEastSouthPanel.add(blank_7, BorderLayout.WEST);
		mEastSouthPanel.add(blank_8, BorderLayout.EAST);
		mEastSouthPanel.add(mEastSouthCenterPanel, BorderLayout.CENTER);
		mEastSouthPanel.add(backButton,BorderLayout.SOUTH);
		mEastSouthCenterPanel.setLayout(new GridLayout(5, 2));
		backButton.addActionListener(e->{
			
			new MovieDetailsScreen(AllMovieInfo.movie).setVisible(true);
			dispose();
		});
		
		ticketingButton.addActionListener(e->{
			new SeatScreen().setVisible(true);
			dispose();
		});
		
		
		
		JLabel lblNewLabel_1_1 = new JLabel("\uADF9\uC7A5 :");
		JLabel lblNewLabel_5 = new JLabel("\uC778\uC6D0 :");
		JLabel lblNewLabel_7 = new JLabel("금액 :");
		JLabel lblNewLabel_9 = new JLabel("");
		

		lblNewLabel_1_1.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
		mEastSouthCenterPanel.add(lblNewLabel_1_1);
		theatherLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 10));
		mEastSouthCenterPanel.add(theatherLabel);
		JLabel lblNewLabel_3_1 = new JLabel("\uB0A0\uC9DC :");
		lblNewLabel_3_1.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
		mEastSouthCenterPanel.add(lblNewLabel_3_1);
		dayLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 10));
		mEastSouthCenterPanel.add(dayLabel);
		lblNewLabel_5.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
		mEastSouthCenterPanel.add(lblNewLabel_5);
		personLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 10));
		mEastSouthCenterPanel.add(personLabel);
		lblNewLabel_7.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
		mEastSouthCenterPanel.add(lblNewLabel_7);
		costLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 10));
		mEastSouthCenterPanel.add(costLabel);
		mEastSouthCenterPanel.add(lblNewLabel_9);
		ticketingButton.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		mEastSouthCenterPanel.add(ticketingButton);
		mCenterPanel.setBounds(0, 38, 586, 545);

		mainPanel.add(mCenterPanel);
		mCenterPanel.setLayout(null);
		mCenterMoviePanel.setBounds(12, 10, 158, 325);
		// 영화 선택
		mCenterMoviePanel.setLayout(null);
		mCenterPanel.add(mCenterMoviePanel);
		
		try {
			loadMovies();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		movielist.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		movielist.setBounds(12, 35, 122, 281);
		mCenterMoviePanel.add(movielist);
		
		try {
			loadTheaters();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		theatherlist.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		theatherlist.setBounds(12, 35, 122, 280);

		choiceMovieLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		choiceMovieLabel.setBounds(37, 10, 76, 15);
		mCenterMoviePanel.add(choiceMovieLabel);
		
		mCenterTheatherPanel.setBounds(182, 10, 158, 325);
		mCenterTheatherPanel.setLayout(null);
		mCenterTheatherPanel.add(theatherlist);

		mCenterPanel.add(mCenterTheatherPanel);
		choiceTheaterLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		choiceTheaterLabel.setBounds(37, 10, 76, 15);
		
		try {
			loadSchedule();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dateList.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		dateList.setBounds(12, 35, 122, 280);
		
		mCenterTheatherPanel.add(choiceTheaterLabel);
		mCenterCalendarPanel.setBounds(352, 10, 158, 325);
		mCenterCalendarPanel.setLayout(null);
		mCenterCalendarPanel.add(dateList);
		
		mCenterPanel.add(mCenterCalendarPanel);
		choiceCalendarLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		choiceCalendarLabel.setBounds(37, 10, 76, 15);
		mCenterCalendarPanel.add(choiceCalendarLabel);
		

		adultCount.add(one);
		adultCount.add(two);
		adultCount.add(three);
		adultCount.add(four);
		adultCount.add(five);
		adultCount.add(zero);
		teenagerCount.add(oneT);
		teenagerCount.add(twoT);
		teenagerCount.add(threeT);
		teenagerCount.add(fourT);
		teenagerCount.add(fiveT);
		teenagerCount.add(zeroT);
		
		one.setFont(new Font("Dialog", Font.BOLD, 9));
		one.setBounds(342, 72, 31, 29);
		mCenerButtomPanel.add(one);
		two.setFont(new Font("Dialog", Font.BOLD, 9));
		two.setBounds(377, 72, 31, 29);
		mCenerButtomPanel.add(two);
		three.setFont(new Font("Dialog", Font.BOLD, 9));
		three.setBounds(412, 72, 31, 29);
		mCenerButtomPanel.add(three);
		four.setFont(new Font("Dialog", Font.BOLD, 9));
		four.setBounds(447, 72, 31, 29);
		mCenerButtomPanel.add(four);
		five.setFont(new Font("Dialog", Font.BOLD, 9));
		five.setBounds(482, 72, 31, 29);
		mCenerButtomPanel.add(five);
		zero.setFont(new Font("Dialog", Font.BOLD, 9));
		zero.setBounds(517, 72, 31, 29);
		mCenerButtomPanel.add(zero);
		
		oneT.setFont(new Font("Dialog", Font.BOLD, 9));
		oneT.setBounds(342, 137, 31, 29);
		mCenerButtomPanel.add(oneT);
		twoT.setFont(new Font("굴림", Font.BOLD, 9));
		twoT.setBounds(377, 137, 31, 29);
		mCenerButtomPanel.add(twoT);
		threeT.setFont(new Font("굴림", Font.BOLD, 9));
		threeT.setBounds(412, 137, 31, 29);
		mCenerButtomPanel.add(threeT);
		fourT.setFont(new Font("굴림", Font.BOLD, 9));
		fourT.setBounds(447, 137, 31, 29);
		mCenerButtomPanel.add(fourT);
		fiveT.setFont(new Font("굴림", Font.BOLD, 9));
		fiveT.setBounds(482, 137, 31, 29);
		mCenerButtomPanel.add(fiveT);
		zeroT.setFont(new Font("Dialog", Font.BOLD, 9));
		zeroT.setBounds(517, 137, 31, 29);
		mCenerButtomPanel.add(zeroT);

		JLabel timeTableLabel = new JLabel("\uC0C1\uC601\uC2DC\uAC04\uD45C");
		timeTableLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 15));
		timeTableLabel.setBounds(12, 10, 108, 23);
		mCenerButtomPanel.add(timeTableLabel);

		JLabel headCountLabel = new JLabel("\uC778\uC6D0\uC120\uD0DD");
		headCountLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 15));
		headCountLabel.setBounds(342, 10, 121, 29);
		mCenerButtomPanel.add(headCountLabel);

		JLabel adultLabel = new JLabel("성인 (10,000 원)");
		adultLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 12));
		adultLabel.setBounds(342, 54, 121, 15);
		mCenerButtomPanel.add(adultLabel);

		JLabel teenagerLabel = new JLabel("청소년 (8,000 원)");
		teenagerLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 12));
		teenagerLabel.setBounds(342, 116, 121, 15);
		mCenerButtomPanel.add(teenagerLabel);
		backButton.setBounds(500,30,100,100);
		
		//관 gui
		roomPanel.setBounds(12, 30, 322, 159);
		mCenerButtomPanel.add(roomPanel);
		roomPanel.setLayout(null);
		
		roomNumber1.setBounds(12, 10, 57, 15);
		roomPanel.add(roomNumber1);
		timeButton1.setFont(new Font("굴림", Font.PLAIN, 10));
		
		timeButton1.setBounds(8, 36, 99, 23);
		roomPanel.add(timeButton1);
		timeButton2.setFont(new Font("굴림", Font.PLAIN, 10));
		timeButton2.setBounds(113, 36, 99, 23);
		roomPanel.add(timeButton2);
		timeButton3.setFont(new Font("굴림", Font.PLAIN, 10));
		
		timeButton3.setBounds(215, 36, 99, 23);
		roomPanel.add(timeButton3);
		timeButton4.setFont(new Font("굴림", Font.PLAIN, 10));
		
		timeButton4.setBounds(8, 61, 99, 23);
		roomPanel.add(timeButton4);
		timeButton5.setFont(new Font("굴림", Font.PLAIN, 10));
		
		timeButton5.setBounds(113, 61, 99, 23);
		roomPanel.add(timeButton5);
		
		timeGroup.add(timeButton1);
		timeGroup.add(timeButton2);
		timeGroup.add(timeButton3);
		timeGroup.add(timeButton4);
		timeGroup.add(timeButton5);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookingScreenX(null).setVisible(true);
            }
        });
    }
}