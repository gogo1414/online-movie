package chatGptTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class SeatSelectionScreen extends JFrame {
    private Set<JButton> selectedSeats;
    private JPanel seatPanel;

    public SeatSelectionScreen() {
        setTitle("Seat Selection");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        selectedSeats = new HashSet<>();
        seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(5, 5, 10, 10)); // 5x5 좌석 배치, 간격 10px

        // 좌석 버튼 생성
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                JButton seatButton = new JButton(row + "," + col);
                seatButton.setBackground(Color.GREEN);
                seatButton.addActionListener(new SeatButtonListener());
                seatPanel.add(seatButton);
            }
        }

        JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSeats.isEmpty()) {
                    JOptionPane.showMessageDialog(SeatSelectionScreen.this, "No seats selected.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    StringBuilder reservedSeats = new StringBuilder("Reserved seats: ");
                    for (JButton seat : selectedSeats) {
                        reservedSeats.append(seat.getText()).append(" ");
                    }
                    System.out.println(reservedSeats.toString());
                    JOptionPane.showMessageDialog(SeatSelectionScreen.this, reservedSeats.toString(), "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(seatPanel, BorderLayout.CENTER);
        mainPanel.add(reserveButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private class SeatButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (selectedSeats.contains(source)) {
                selectedSeats.remove(source);
                source.setBackground(Color.GREEN);
            } else {
                selectedSeats.add(source);
                source.setBackground(Color.RED);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SeatSelectionScreen().setVisible(true);
            }
        });
    }
}