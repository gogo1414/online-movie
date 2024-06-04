package find_catalog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import dao.MovieDAO;
import model.AllMovieInfo;
import model.Movie;
import ui.MovieDetailsScreen;

public class DuplicateScreen extends JFrame{
	
		private String category;
		private String whatIsIt;
	
		private MovieDAO movieDAO;
	    private JList<String> movieList;
	    private DefaultListModel<String> listModel;

	    public DuplicateScreen(String catalogue ,String whatIsIt) {
	    	AllMovieInfo.whatIsIt = whatIsIt;
	    	AllMovieInfo.catalog = catalogue;
	    	this.category = catalogue;
	    	this.whatIsIt = whatIsIt;
	    	AllMovieInfo.catalog = catalogue;
	        setTitle("영화 목록");
	        setSize(500, 500);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        movieDAO = new MovieDAO();
	        listModel = new DefaultListModel<>();
	        movieList = new JList<>(listModel);
	        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        
	        JScrollPane scrollPane = new JScrollPane(movieList);
	        scrollPane.setBounds(20, 20, 300, 400); // 위치와 크기를 설정

	        JButton detailsButton = new JButton("영화 세부사항");
	        detailsButton.setBounds(350, 20, 120, 30); // 위치와 크기를 설정
	        detailsButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String selectedMovieCatalogue = movieList.getSelectedValue();
	                System.out.println(selectedMovieCatalogue);
	                Movie selectedMovie = null;
	                if (selectedMovieCatalogue != null) {
	                    try {
	                    	if(category == "영화명") {
	                    		selectedMovie = movieDAO.getMovieByTitle(selectedMovieCatalogue);
	                        }
	                    	else if(category == "배우명") {
	                    		selectedMovie = movieDAO.getMovieByTitle(selectedMovieCatalogue);
	                    	}
	                    	else if(category == "감독명") {
	                    		selectedMovie = movieDAO.getMovieByTitle(selectedMovieCatalogue);
	                    	}
	                    	else if(category == "장르") {
	                    		selectedMovie = movieDAO.getMovieByTitle(selectedMovieCatalogue);
	                    	}
	                    	
	                    	System.out.println(selectedMovie.toString());
	                        if (selectedMovie != null) {
	                            new MovieDetailsScreen(selectedMovie,category).setVisible(true);
	                            dispose();
	                        }
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                        JOptionPane.showMessageDialog(DuplicateScreen.this, "Error fetching movie details.", "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(DuplicateScreen.this, "Please select a movie.", "Warning", JOptionPane.WARNING_MESSAGE);
	                }
	            }
	        });
	        
	        JButton backButton = new JButton("뒤로가기");
	        backButton.setBounds(350, 60, 120, 30); // 위치와 크기를 설정
	        backButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                new MovieListScreen2(catalogue).setVisible(true);
	                dispose();
	            }
	        });

	        loadMovies(catalogue);

	        JPanel panel = new JPanel();
	        panel.setLayout(null); // 절대 레이아웃을 사용
	        panel.add(scrollPane);
	        panel.add(detailsButton);
	        panel.add(backButton);
	        add(panel);
	    }

	    private void loadMovies(String category) {
	        try {
	        	
	        	List<Movie> movies = null;
	        	
	        	if(category=="감독명") movies = movieDAO.getSpecialDirectorMovies(whatIsIt);
	        	if(category=="배우명") movies = movieDAO.getSpecialActorMovies(whatIsIt);
	        	if(category =="장르") movies = movieDAO.getSpecialGenreMovies(whatIsIt);
	        	
	          
	            for (Movie movie : movies) {
	            	if(category == "영화명") {
	            		listModel.addElement(movie.getTitle());
	            		}
	            	else if (category == "감독명") {
	            		listModel.addElement(movie.getTitle());
	            	}
	            	else if (category == "배우명") {
	            		listModel.addElement(movie.getTitle());
	            	}
	            	else if (category == "장르") {
	            		listModel.addElement(movie.getTitle());
	            	}
	            	else {
	            		listModel.addElement(movie.getTitle());
	            	}
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Error loading movies.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new DuplicateScreen("배우명","이동호").setVisible(true);
	            }
	        });
	    }
}
