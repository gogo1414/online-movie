package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

import util.DBConnection;

public class AdminPanel extends JFrame {

    private Connection conn;

    public AdminPanel() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setTitle("관리자 패널");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10)); // 30픽셀 간격
        JButton dbInitButton = new JButton("데이터베이스 초기화");
        dbInitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeDatabase();
            }
        });
        JButton viewButton = new JButton("전체 테이블 보기");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTablesWindow();
            }
        });
        buttonPanel.add(dbInitButton);
        buttonPanel.add(viewButton);

        // SQL 입력 패널
        JPanel sqlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel actionLabel = new JLabel("변경/삭제/삽입/조회");
        actionLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        sqlPanel.add(actionLabel, gbc);

        JLabel sqlLabel = new JLabel("SQL 문:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        sqlPanel.add(sqlLabel, gbc);

        JTextField sqlField = new JTextField("예: SELECT * FROM 테이블명;", 30); // 고정된 크기의 텍스트 필드와 예시 기본값
        gbc.gridx = 1;
        gbc.gridy = 1;
        sqlPanel.add(sqlField, gbc);

        JButton sqlButton = new JButton("실행");
        gbc.gridx = 2;
        gbc.gridy = 1;
        sqlPanel.add(sqlButton, gbc);
        sqlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSQL(sqlField.getText());
            }
        });

        // 메인 패널에 추가
        add(buttonPanel, BorderLayout.NORTH);
        add(sqlPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeDatabase() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP DATABASE dbtest");
            stmt.executeUpdate("CREATE DATABASE dbtest");
            JOptionPane.showMessageDialog(this, "데이터베이스 초기화 완료.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "데이터베이스 초기화 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeSQL(String query) {
        try (Statement stmt = conn.createStatement()) {
            boolean isResultSet = stmt.execute(query);
            if (isResultSet) {
                ResultSet rs = stmt.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                StringBuilder result = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    result.append(metaData.getColumnName(i)).append("\t");
                }
                result.append("\n");
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(rs.getString(i)).append("\t");
                    }
                    result.append("\n");
                }
                JTextArea resultArea = new JTextArea(result.toString());
                resultArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(resultArea);
                JFrame resultFrame = new JFrame("SQL 결과");
                resultFrame.setSize(600, 400);
                resultFrame.add(scrollPane);
                resultFrame.setVisible(true);
            } else {
                int updateCount = stmt.getUpdateCount();
                JOptionPane.showMessageDialog(this, "쿼리 실행 완료: " + updateCount + "개의 행에 영향을 미쳤습니다.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "쿼리 실행 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAllTablesWindow() {
        JFrame allTablesFrame = new JFrame("전체 테이블 보기");
        allTablesFrame.setSize(1000, 1000);

        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
            while (rs.next()) {
                String tableName = rs.getString(1);
                resultArea.append("테이블: " + tableName + "\n");
                try (Statement tableStmt = conn.createStatement();
                     ResultSet tableRs = tableStmt.executeQuery("SELECT * FROM " + tableName)) {
                    ResultSetMetaData metaData = tableRs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (tableRs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            resultArea.append(metaData.getColumnName(i) + ": " + tableRs.getString(i) + "\t");
                        }
                        resultArea.append("\n");
                    }
                }
                resultArea.append("\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "테이블 조회 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }

        allTablesFrame.add(scrollPane);
        allTablesFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel());
    }
}
