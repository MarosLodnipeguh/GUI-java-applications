package Handlers;

import java.util.EventObject;

public class TickEvent extends EventObject {

    private int tick;

    public TickEvent (Object source, int tick) {
        super(source);
        this.tick = tick;
    }

    public int getTick () {
        return tick;
    }
}
