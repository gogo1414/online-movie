package find_catalog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BoxLayout;
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
import ui.MainMenuScreen;
import ui.MovieDetailsScreen;

public class MovieListScreen extends JFrame {
    private MovieDAO movieDAO;
    private JList<String> movieList;
    private DefaultListModel<String> listModel;
    public static String catalogue;

    public MovieListScreen(String catalogue) {
    	this.catalogue = catalogue;
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

        JButton detailsButton = new JButton("View Details");
        detailsButton.setBounds(350, 20, 120, 30); // 위치와 크기를 설정
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMovieCatalogue = movieList.getSelectedValue();
                System.out.println(selectedMovieCatalogue);
                Movie selectedMovie = null;
                if (selectedMovieCatalogue != null) {
                    try {
                    	if(MovieListScreen.catalogue == "영화명") {
                    		selectedMovie = movieDAO.getMovieByTitle(selectedMovieCatalogue);
                        }
                    	else if(MovieListScreen.catalogue == "배우명") {
                    		selectedMovie = movieDAO.getMovieByActor(selectedMovieCatalogue);
                    	}
                    	else if(MovieListScreen.catalogue == "감독명") {
                    		selectedMovie = movieDAO.getMovieByDirector(selectedMovieCatalogue);
                    	}
                    	else if(MovieListScreen.catalogue == "장르") {
                    		selectedMovie = movieDAO.getMovieByGenre(selectedMovieCatalogue);
                    	}
                    	
                        if (selectedMovie != null) {
                            new MovieDetailsScreen(selectedMovie).setVisible(true);
                            dispose();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(MovieListScreen.this, "Error fetching movie details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MovieListScreen.this, "Please select a movie.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        JButton backButton = new JButton("Back");
        backButton.setBounds(350, 60, 120, 30); // 위치와 크기를 설정
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindMovieCatalog().setVisible(true);
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

    private void loadMovies(String catalogue) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            for (Movie movie : movies) {
            	if(catalogue == "영화명") {
            		listModel.addElement(movie.getTitle());
            		}
            	else if (catalogue == "감독명") {
            		listModel.addElement(movie.getDirector());
            	}
            	else if (catalogue == "배우명") {
            		listModel.addElement(movie.getActors());
            	}
            	else if (catalogue == "장르") {
            		listModel.addElement(movie.getGenre());
            	}
            	else {
            		listModel.addElement(movie.getRating());
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
                new MovieListScreen("영화명").setVisible(true);
            }
        });
    }
}
