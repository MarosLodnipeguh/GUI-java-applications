package Logic;

import Enums.Direction;
import Handlers.*;

import java.util.List;

public class GameManager implements EventListener, MovementListener {

    private Game game;
    private MovementListener movementListener; // (słuchaczem jest MainFrame)
    private EventListener eventListener; // (słuchaczem jest UserPanel)

    @Override
    public void startGame (NewGameEvent evt) {
        showGameboard();

        this.game = new Game(this, evt.getPlayerName(), evt.getTick(), evt.getBoardWidth(), evt.getBoardHeight());
        Thread gameThread = new Thread(game, "GameThread");
        gameThread.start();

    }

    @Override
    public void updateScore (ConsumptionEvent evt) {
        eventListener.updateScore(evt);
    }

    @Override
    public void changeTick (TickEvent evt) {
        if (game != null) {
            game.setTick(evt.getTick());
        }
    }

    @Override
    public void gameOver (ImpactEvent evt) {
        game.setRunning(false);

        writeScoreToFile(evt.getPlayerName(), evt.getFinalScore());

        game = null;
        System.out.println("Game Nulled");
    }

    public void writeScoreToFile (String playerName, int score) {

        Scoreboard.addScore(playerName, score);
//        Scoreboard.displayScores();
        showScoreboard(Scoreboard.displayScores());

    }

    @Override
    public void showScoreboard (List<Scoreboard.ScoreboardEntry> scores) {
        movementListener.showScoreboard(scores);
    }

    @Override
    public void showGameboard () {
        movementListener.showGameboard();
    }

    @Override
    public void forceGameOver () {
        game.forceGameOver(); // -> gameOver()
    }







    @Override
    public void setDirection (Direction direction) {
        if (game == null) {
            eventListener.startGameNoButton();
        }
        game.setDirection(direction);
    }

    @Override
    public void fillGameboard (int[][] board) {
        movementListener.fillGameboard(board);
    }



    public void setEventListener (EventListener fromLogicEventListener) {
        this.eventListener = fromLogicEventListener;
    }

    public void setMovementListener (MovementListener fromLogicMovementListener) {
        this.movementListener = fromLogicMovementListener;
    }


    @Override
    public boolean getGameState () {
        if (game == null) {
            return false;
        }
        else {
            return true;
        }
    }


    @Override
    public void startGameNoButton () {

    }


}
