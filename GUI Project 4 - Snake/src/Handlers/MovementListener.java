package Handlers;

import Enums.Direction;
import Logic.Scoreboard;

import java.util.List;

public interface MovementListener {

    // MainFrame -> GameManager
    void setDirection(Direction direction);

    // GameManager -> MainFrame
    void fillGameboard (int[][] board);
    void showScoreboard (List<Scoreboard.ScoreboardEntry> scores);
    void showGameboard();

}
