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

import model.AllMovieInfo;
import ui.MainMenuScreen;

public class FindMovieCatalog extends JFrame {

    private JButton backButton = new JButton("뒤로가기");

    public FindMovieCatalog() {
        setTitle("영화 조회");
        setSize(500, 400); // 사이즈를 조금 더 키움
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back button setup
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new MainMenuScreen(AllMovieInfo.customer).setVisible(true);
               dispose();
            }
        });

        // Add back button to the top right corner
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(backButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.5; // 패널의 절반 크기로 설정
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(createButton("영화명"), gbc);

        gbc.gridy = 2;
        panel.add(createButton("감독명"), gbc);

        gbc.gridy = 3;
        panel.add(createButton("배우명"), gbc);

        gbc.gridy = 4;
        panel.add(createButton("장르"), gbc);

        add(panel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(text.equals("영화명")) {
            		new MovieListScreen(text).setVisible(true);
            		dispose();
            	}
            	else {
                new MovieListScreen2(text).setVisible(true);
                dispose();}
            	
               
            }
        });
        return button;
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
