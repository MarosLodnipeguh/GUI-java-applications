package Handlers;

import java.util.EventObject;

public class NewGameEvent extends EventObject {

    private String playerName;

    private int tick;
    private int boardWidth;
    private int boardHeight;

    public NewGameEvent (Object source, String playerName, int tick, int boardWidth, int boardHeight) {
        super(source);
        this.playerName = playerName;
        this.tick = tick;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    // logic thread start dopiero tutaj!


    public String getPlayerName () {
        return playerName;
    }

    public int getTick () {
        return tick;
    }

    public int getBoardWidth () {
        return boardWidth;
    }

    public int getBoardHeight () {
        return boardHeight;
    }
}
