package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AdminOrUserScreen extends JFrame {
	
    public AdminOrUserScreen() {
        setTitle("로그인");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton adminButton = new JButton("관리자");
        adminButton.addActionListener(e->{
        	new LoginScreen("Admin").setVisible(true);
        	dispose();
        });
        

        JButton userButton = new JButton("회원");
        userButton.addActionListener(e->{
        	new LoginScreen("User").setVisible(true);
        	dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(adminButton, gbc);

        gbc.gridy = 1;
        panel.add(userButton, gbc);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminOrUserScreen().setVisible(true);
            }
        });
    }
}
