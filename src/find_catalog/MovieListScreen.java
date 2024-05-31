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
import model.Movie;
import ui.MainMenuScreen;
import ui.MovieDetailsScreen;

public class MovieListScreen extends JFrame {
    private MovieDAO movieDAO;
    private JList<String> movieList;
    private DefaultListModel<String> listModel;

    public MovieListScreen() {
        setTitle("Movie List");
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
                String selectedMovieTitle = movieList.getSelectedValue();
                if (selectedMovieTitle != null) {
                    try {
                        Movie selectedMovie = movieDAO.getMovieByTitle(selectedMovieTitle);
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
                new MainMenuScreen(MainMenuScreen.customer).setVisible(true);
                dispose();
            }
        });

        loadMovies();

        JPanel panel = new JPanel();
        panel.setLayout(null); // 절대 레이아웃을 사용
        panel.add(scrollPane);
        panel.add(detailsButton);
        panel.add(backButton);
        add(panel);
    }

    private void loadMovies() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            for (Movie movie : movies) {
                listModel.addElement(movie.getTitle());
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
                new MovieListScreen().setVisible(true);
            }
        });
    }
}
