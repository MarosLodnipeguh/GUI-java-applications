package Handlers;

import java.util.EventObject;

public class UpdateStationPanelUIEvent extends EventObject {

    private int id;
    private int processedMessages;
    private int waitingMessages;

    public UpdateStationPanelUIEvent (Object source, int id, int processedMessages, int waitingMessages) {
        super(source);
        this.id = id;
        this.processedMessages = processedMessages;
        this.waitingMessages = waitingMessages;
    }

    public int getId () {
        return id;
    }

    public int getProcessedMessages () {
        return processedMessages;
    }

    public int getWaitingMessages () {
        return waitingMessages;
    }
}
