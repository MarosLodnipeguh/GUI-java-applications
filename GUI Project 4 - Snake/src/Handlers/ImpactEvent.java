package Handlers;

import java.util.EventObject;

public class ImpactEvent extends EventObject {

    private int x;
    private int y;
    private int finalScore;
    private String playerName;

    public ImpactEvent (Object source, int x, int y, String playerName, int finalScore) {
        super(source);
        this.x = x;
        this.y = y;
        this.playerName = playerName;
        this.finalScore = finalScore;
        System.out.println("Impact at: " + x + ", " + y);
    }

    public String getPlayerName () {
        return playerName;
    }

    public int getFinalScore () {
        return finalScore;
    }
}
