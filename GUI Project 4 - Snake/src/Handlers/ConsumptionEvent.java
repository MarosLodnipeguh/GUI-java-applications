package Handlers;

import java.util.EventObject;

public class ConsumptionEvent extends EventObject {

    private int score;
    public ConsumptionEvent (Object source, int score) {
        super(source);
        this.score = score;
    }

    public int getScore () {
        return score;
    }

}
