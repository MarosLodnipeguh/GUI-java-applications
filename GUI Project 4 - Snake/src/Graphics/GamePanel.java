package Graphics;

import Logic.Scoreboard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {

    private JScrollPane tableScrollPane;
    private Table gameTable;
    private JPanel scorePanel;

    private int boardWidth;
    private int boardHeight;

    public GamePanel (int boardWidth, int boardHeight) {
        setPreferredSize(new Dimension(550, 360));

        this.scorePanel = new JPanel(new BorderLayout()); // Null Panel
        scorePanel.add(new JLabel("Press New Game to Start"), BorderLayout.CENTER);
        add(scorePanel);


        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;


    }

    public void showGameboard() {
        remove(scorePanel);

        gameTable = new Table(boardHeight, boardWidth);
        tableScrollPane = new JScrollPane(gameTable);

        add(tableScrollPane, BorderLayout.NORTH);
    }

    public void showScoreboard (List<Scoreboard.ScoreboardEntry> scores) {
        remove(tableScrollPane);

        scorePanel = new JPanel(new GridLayout(0, 1));
        scorePanel.setPreferredSize(new Dimension(200, 350));

        scorePanel.add(new JLabel("TOP 10 SNAKES: \n"));
        for (int i = 0; i < scores.size(); i++) {
            Scoreboard.ScoreboardEntry entry = scores.get(i);
            String labelText = (i + 1) + ". " + entry.getPlayerName() + " - " + entry.getScore();
            scorePanel.add(new JLabel(labelText));
        }

        add(scorePanel, BorderLayout.NORTH);
    }

    public void fillGameboard (int[][] board) {
//        requestFocus();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                switch (board[i][j]) {
                    case 0 -> gameTable.setValueAt(Color.WHITE, i, j); //empty
                    case 1 -> gameTable.setValueAt(Color.GREEN, i, j); //head
                    case 2 -> gameTable.setValueAt(Color.GRAY, i, j); //body
                    case 3 -> gameTable.setValueAt(Color.RED, i, j); //fruit
                }

            }
        }
//        repaint();
    }
}
