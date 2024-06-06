package ui;

import java.awt.Color;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import dao.MovieDAO;
import dao.ScheduleDAO;
import dao.SeatDAO;
import dao.TheaterDAO;
import find_catalog.FindMovieCatalog;
import model.AllMovieInfo;
import model.Booking;
import model.Movie;
import model.Schedule;
import model.Theater;

public class BookingScreen extends JFrame {
	
	private Theater theater;
	private Movie movie;
	private Schedule schedule;
	
	private String selectedTheaterName;
	private String selectedMovieTitle;
	private String selectedStartDate;
	private String selectedStartTime;

    private TheaterDAO theaterDAO = new TheaterDAO();
    private SeatDAO seatDAO = new SeatDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    private JPanel mainPanel = new JPanel();
    private JPanel movieSelectPanel = new JPanel();
    private JPanel theaterSelectPanel = new JPanel();
    private JPanel dateSelectPanel = new JPanel();
    private JPanel timeSelectPanel = new JPanel();
    private JPanel reservationPanel = new JPanel();

    private List<String> movies;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> movielist;

    private List<Theater> theaters;
    private DefaultListModel<String> listModel2 = new DefaultListModel<>();
    private JList<String> theaterlist;

    private List<Schedule> startDates;
    private DefaultListModel<String> listModel3 = new DefaultListModel<>();
    private JList<String> datelist = new JList<>(listModel3);

    private List<Schedule> startTimes;
    private DefaultListModel<String> listModel4 = new DefaultListModel<>();
    private JList<String> timelist = new JList<>(listModel4);

    private JLabel movieLabel = new JLabel("영화 : ");
    private JLabel theaterLabel = new JLabel("극장 : ");
    private JLabel dateLabel = new JLabel("날짜 : ");
    private JLabel timeLabel = new JLabel("시간 : ");

    private JButton reservationButton = new JButton("예약하기");
    private JButton backButton = new JButton("뒤로가기");

    public BookingScreen() throws SQLException {

        setTitle("티켓 예매");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        mainPanel.setLayout(null);

        // *******영화 선택 패널********
        makePanel(50, 100, 150, 400, "movieSelectPanel");

        // *********극장 선택 패널********
        makePanel(250, 100, 150, 400, "theaterSelectPanel");

        // **********날짜 선택 패널 *******
        makePanel(450, 100, 150, 400, "dateSelectPanel");

        // ********상영 시간 선택 패널 *******
        makePanel(650, 100, 150, 400, "timeSelectPanel");

        // ******최종 예약 패널**********
        makePanel(650, 550, 200, 200, "reservationPanel");

        //*******뒤로가기 버튼********
        backButton.setBounds(50, 800, 100, 70);
        mainPanel.add(backButton);
        backButton.addActionListener(e -> {
        	if(AllMovieInfo.changeReservation==1) {
        		JOptionPane.showMessageDialog(BookingScreen.this, "예약 변경을 완료해주세요", "오류", JOptionPane.ERROR_MESSAGE);
        	}
        	else {
            new MovieDetailsScreen(AllMovieInfo.movie, AllMovieInfo.catalog).setVisible(true);
            dispose();
        	}
        });

        add(mainPanel);
        
    }

    private void showDBList(String name) throws SQLException {
        if (name.equals("movieSelectPanel")) {
            loadMovies();
            JScrollPane scrollPane = new JScrollPane(movielist);
            scrollPane.setBounds(0, 0, 150, 400);
            movieSelectPanel.add(scrollPane);
            movielist.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String selectedMovie = movielist.getSelectedValue();
                        if (selectedMovie != null) {
                            movieLabel.setText("영화 : " + selectedMovie);
                            theaterLabel.setText("극장 : ");
                            dateLabel.setText("날짜 : ");
                            timeLabel.setText("시간 : ");
                            theaterlist.clearSelection();
                            datelist.clearSelection();
                            timelist.clearSelection();
                            listModel3.clear(); // 개봉 날짜 초기화
                            listModel4.clear(); // 상영 시간 초기화
                            datelist.setEnabled(false);
                            timelist.setEnabled(false);
                        }
                    }
                }
            });

        } else if (name.equals("theaterSelectPanel")) {
            loadTheaters();
            JScrollPane scrollPane = new JScrollPane(theaterlist);
            scrollPane.setBounds(0, 0, 150, 400);
            theaterSelectPanel.add(scrollPane);
            theaterlist.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedTheaterName = theaterlist.getSelectedValue();
                        selectedMovieTitle = movielist.getSelectedValue();
                        if (selectedTheaterName != null) {
                            theaterLabel.setText("극장 : " + selectedTheaterName);
                            dateLabel.setText("날짜 : ");
                            timeLabel.setText("시간 : ");
                            datelist.clearSelection();
                            timelist.clearSelection();
                            listModel3.clear(); // 개봉 날짜 초기화
                            listModel4.clear(); // 상영 시간 초기화
                            datelist.setEnabled(true);
                            try {
								loadStartDate();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
                        }
                    }
                }
            });

        } else if (name.equals("dateSelectPanel")) {
            datelist.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(datelist);
            scrollPane.setBounds(0, 0, 150, 400);
            dateSelectPanel.add(scrollPane);
            datelist.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedStartDate = datelist.getSelectedValue();
                        if (selectedStartDate != null) {
                            dateLabel.setText("날짜 : " + selectedStartDate);
                            timeLabel.setText("시간 : ");
                            timelist.clearSelection();
                            listModel4.clear(); // 상영 시간 초기화
                            timelist.setEnabled(true);
                            try {
								loadStartTime();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
                        }
                    }
                }
            });
        } else if (name.equals("timeSelectPanel")) {
            timelist.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(timelist);
            scrollPane.setBounds(0, 0, 150, 400);
            timeSelectPanel.add(scrollPane);
            timelist.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                         selectedStartTime = timelist.getSelectedValue();
                        if (selectedStartTime != null) {
                            timeLabel.setText("시간 : " + selectedStartTime);
                        }
                    }
                }
            });
        }
    }

    private void makePanel(int x, int y, int w, int h, String panelName) throws SQLException {
        if (panelName.equals("movieSelectPanel")) {
            movieSelectPanel.setBounds(x, y, w, h);
            movieSelectPanel.setBackground(Color.white);
            movieSelectPanel.setLayout(null);

            JLabel label = new JLabel("영화 선택");
            label.setBounds(x + 30, y - 30, 100, 30);
            label.setFont(label.getFont().deriveFont(20.0f));
            mainPanel.add(label);

            showDBList(panelName);

            mainPanel.add(movieSelectPanel);

        } else if (panelName.equals("theaterSelectPanel")) {
            theaterSelectPanel.setBounds(x, y, w, h);
            theaterSelectPanel.setBackground(Color.white);
            theaterSelectPanel.setLayout(null);

            JLabel label = new JLabel("극장 선택");
            label.setBounds(x + 30, y - 30, 100, 30);
            label.setFont(label.getFont().deriveFont(20.0f));
            mainPanel.add(label);

            showDBList(panelName);

            mainPanel.add(theaterSelectPanel);
        } else if (panelName.equals("dateSelectPanel")) {
            dateSelectPanel.setBounds(x, y, w, h);
            dateSelectPanel.setBackground(Color.white);
            dateSelectPanel.setLayout(null);

            JLabel label = new JLabel("개봉 날짜");
            label.setBounds(x + 30, y - 30, 100, 30);
            label.setFont(label.getFont().deriveFont(20.0f));
            mainPanel.add(label);

            showDBList(panelName);

            mainPanel.add(dateSelectPanel);
        } else if (panelName.equals("timeSelectPanel")) {
            timeSelectPanel.setBounds(x, y, w, h);
            timeSelectPanel.setBackground(Color.white);
            timeSelectPanel.setLayout(null);

            JLabel label = new JLabel("상영 시간");
            label.setBounds(x + 30, y - 30, 100, 30);
            label.setFont(label.getFont().deriveFont(20.0f));
            mainPanel.add(label);

            showDBList(panelName);

            mainPanel.add(timeSelectPanel);
        } else if (panelName.equals("reservationPanel")) {
            reservationPanel.setBounds(x, y, w, h);
            reservationPanel.setBackground(Color.white);
            reservationPanel.setLayout(null);

            makeReservationInfo();

            mainPanel.add(reservationPanel);
        }
    }

    private void makeReservationInfo() {

        movieLabel.setBounds(5, 0, 180, 40);
        theaterLabel.setBounds(5, 40, 180, 40);
        dateLabel.setBounds(5, 80, 180, 40);
        timeLabel.setBounds(5, 120, 180, 40);
        reservationButton.setBounds(10, 160, 180, 40);

        reservationPanel.add(movieLabel);
        reservationPanel.add(theaterLabel);
        reservationPanel.add(dateLabel);
        reservationPanel.add(timeLabel);
        reservationPanel.add(reservationButton);

        reservationButton.addActionListener(e -> {
            if (movieLabel.getText().equals("영화 : ") || 
                theaterLabel.getText().equals("극장 : ") || 
                dateLabel.getText().equals("날짜 : ") || 
                timeLabel.getText().equals("시간 : ")) {
                JOptionPane.showMessageDialog(this, "모든 항목을 선택해야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
            } else {
            	try {
					schedule = scheduleDAO.getAllScheduleDateAndTime(selectedStartDate, selectedStartTime);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
                try {
					new SeatScreen(schedule).setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
                dispose();
            }
           
        });

    }

    public void loadMovies() throws SQLException {
        movies = movieDAO.getAllMoviesName();
        for (String movieName : movies) {
            listModel.addElement(movieName);
        }
        movielist = new JList<>(listModel);
    }

    public void loadTheaters() throws SQLException {
        theaters = theaterDAO.getAllTheaters();
        for (Theater theater : theaters) {
        	if (theater.isActive()) {
        		listModel2.addElement(theater.getTheaterName());
            }
        }
        theaterlist = new JList<>(listModel2);
    }

    public void loadStartDate() throws SQLException {
    	listModel3.clear();
    	theater = null;
    	movie = null;
    	if (selectedTheaterName != null && selectedMovieTitle != null) {
    		theater = theaterDAO.getTheaterByName(selectedTheaterName);
    		movie = movieDAO.getMovieByTitle(selectedMovieTitle);
    		startDates = scheduleDAO.getSchedulesByTheaterAndMovie(theater.getTheaterID(), movie.getMovieID());
            for (Schedule startDate : startDates) {
                listModel3.addElement(startDate.getStartDate().toString());
            }
    	}
    }

    public void loadStartTime() throws SQLException {
        startTimes = scheduleDAO.getSchedulesByTheaterAndMovieAndDate(theater.getTheaterID(), movie.getMovieID(), selectedStartDate);
        listModel4.clear();
        for (Schedule startTime : startTimes) {
            listModel4.addElement(startTime.getStartTime().toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BookingScreen().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
