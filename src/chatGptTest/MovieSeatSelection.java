package chatGptTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class MovieSeatSelection extends JFrame implements ActionListener {
    private JLabel selectedSeatLabel;
    private JPanel seatPanel;
    private Set<String> selectedSeats;

    public MovieSeatSelection() {
        setTitle("Movie Seat Selection");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectedSeats = new HashSet<>();

        selectedSeatLabel = new JLabel("Selected Seats: None");
        selectedSeatLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(selectedSeatLabel, BorderLayout.NORTH);

        seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(5, 5, 10, 10));
        add(seatPanel, BorderLayout.CENTER);

        for (int i = 1; i <= 25; i++) {
            JButton seatButton = new JButton("Seat " + i);
            seatButton.addActionListener(this);
            seatPanel.add(seatButton);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String seatNumber = clickedButton.getText();

        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber);
            clickedButton.setBackground(null); // Deselect: reset to default color
        } else {
            selectedSeats.add(seatNumber);
            clickedButton.setBackground(Color.GREEN); // Select: change color to green
        }

        updateSelectedSeatsLabel();
    }

    private void updateSelectedSeatsLabel() {
        if (selectedSeats.isEmpty()) {
            selectedSeatLabel.setText("Selected Seats: None");
        } else {
            selectedSeatLabel.setText("Selected Seats: " + String.join(", ", selectedSeats));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieSeatSelection());
    }
}
