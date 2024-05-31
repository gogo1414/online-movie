package find_catalog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FindMovieCatalog extends JFrame {

    public FindMovieCatalog() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

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
                new MovieListScreen("감독명").setVisible(true);
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(movieListButton, gbc);

        gbc.gridy = 1;
        panel.add(directorButton, gbc);

        gbc.gridy = 2;
        panel.add(actorButton, gbc);

        gbc.gridy = 3;
        panel.add(genreButton, gbc);

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
