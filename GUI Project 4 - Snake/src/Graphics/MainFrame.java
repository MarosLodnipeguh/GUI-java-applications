package Graphics;

import Enums.Direction;
import Handlers.MovementListener;
import Logic.Scoreboard;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class MainFrame extends JFrame implements MovementListener {

    private GamePanel gamePanel;
    private UserPanel userPanel;

    private MovementListener movementListener;

    public MainFrame (int boardWidth, int boardHeight) {

        setPreferredSize(new Dimension(600, 600));
        setTitle("Graphics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);

        gamePanel = new GamePanel(boardWidth, boardHeight);
        userPanel = new UserPanel();

        getContentPane().add(gamePanel, BorderLayout.NORTH);
        getContentPane().add(userPanel, BorderLayout.SOUTH);


        pack();
        setVisible(true);

    }

    @Override
    public void showGameboard() {
        gamePanel.showGameboard();
        pack();
        repaint();
        revalidate();
    }

    @Override
    public void showScoreboard (List<Scoreboard.ScoreboardEntry> scores) {
        gamePanel.showScoreboard(scores);
        pack();
        repaint();
        revalidate();
    }

    @Override
    public void fillGameboard (int[][] board) {
        gamePanel.fillGameboard(board);
        requestFocus();
        repaint();
    }




    @Override
    public void setDirection (Direction direction) {
        movementListener.setDirection(direction);
    }

    public void setMovementListener (MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    public UserPanel getUserPanel () {
        return userPanel;
    }
}
