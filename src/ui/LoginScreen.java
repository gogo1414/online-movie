package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import dao.CustomerDAO;
import model.AllMovieInfo;
import model.Customer;


public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private CustomerDAO customerDAO = new CustomerDAO();
    private String userOrAdmin;
    private JButton backButton = new JButton("뒤로가기");
    
    
    public LoginScreen(String userOrAdmin) {
       this.userOrAdmin =userOrAdmin;
       
        setTitle("로그인");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        backButton.setBounds(10,120,100,30);
        panel.add(backButton);
        
        backButton.addActionListener(e->{
           new AdminOrUserScreen().setVisible(true);
           dispose();
        });
        

        JLabel userLabel = new JLabel("아이디");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("패스워드");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);
        
       

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);
        
        if(userOrAdmin.equals("User")) {
           usernameField.setText("user1");
           passwordField.setText("user1");
        }
        else {
           usernameField.setText("root");
           passwordField.setText("1234");
        }

        loginButton = new JButton("로그인");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        loginButton.addKeyListener(enterKeyListener);

        add(panel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Customer customer = customerDAO.authenticate(username, password);

            if (customer != null) {
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                AllMovieInfo.customer=customer;
                if(!customer.isAdmin()) new MainMenuScreen(customer).setVisible(true);
                else new AdminPanel().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "이름 혹은 패스워드를 정확하게 입력해주세요.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 오류.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen(null).setVisible(true);
            }
        });
    }
}