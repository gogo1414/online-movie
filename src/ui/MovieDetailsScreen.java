package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import find_catalog.MovieListScreen;
import model.AllMovieInfo;
import model.Movie;

public class MovieDetailsScreen extends JFrame {
    private Movie movie;

    public MovieDetailsScreen(Movie movie) {
    	AllMovieInfo.movie = movie;
        this.movie = movie;

        setTitle("영화 상세정보");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("제목 : " + movie.getTitle());
        JLabel directorLabel = new JLabel("감독 : " + movie.getDirector());
        JLabel actorsLabel = new JLabel("배우 : " + movie.getActors());
        JLabel genreLabel = new JLabel("장르 : " + movie.getGenre());
        JLabel storyLabel = new JLabel("<html><body style='width: 350px'>" + "줄거리 : " + movie.getStory() + "</body></html>");
        JLabel releaseDateLabel = new JLabel("영화 개봉일: " + movie.getReleaseDate());
        JLabel scoreLabel = new JLabel("평점 : " + movie.getScore());

        JButton bookButton = new JButton("Book Movie");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					new BookingScreen().setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                dispose();
            }
        });
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieListScreen(MovieListScreen.catalogue).setVisible(true);
                dispose();
            }
        });

        panel.add(titleLabel);
        panel.add(directorLabel);
        panel.add(actorsLabel);
        panel.add(genreLabel);
        panel.add(storyLabel);
        panel.add(releaseDateLabel);
        panel.add(scoreLabel);
        panel.add(bookButton);
        panel.add(backButton);

        add(panel);
    }
}