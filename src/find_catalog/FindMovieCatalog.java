package find_catalog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import ui.BookingHistoryScreen;

public class FindMovieCatalog extends JFrame{
	
	public FindMovieCatalog() {
		setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton movieListButton = new JButton("영화명");
        movieListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MovieListScreen("영화명").setVisible(true);
                dispose();
            }
        });

        JButton directorButton = new JButton("감독명");
        directorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MovieListScreen("감독명").setVisible(true); // 예매 화면으로 이동할 수 있도록 수정
                dispose();
            }
        });

        JButton actorButton = new JButton("배우명");
        actorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MovieListScreen("배우명").setVisible(true);
                dispose();
            }
        });
        
        JButton genreButton = new JButton("장르");
        genreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MovieListScreen("장르").setVisible(true);
                dispose();
            }
   
        });
        

        panel.add(movieListButton);
        panel.add(directorButton);
        panel.add(actorButton);
        panel.add(genreButton);

        add(panel);
	}

	
	 public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new FindMovieCatalog().setVisible(true);
	            }
	        });
	    }
}
