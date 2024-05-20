package Logic;

import Handlers.BSCListener;
import Handlers.UpdateStationPanelUIEvent;
import SMS.PhoneBookLogic;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BSC implements Runnable {

    private int id;
    private ConcurrentLinkedQueue<String> gatheredMessages;
    private AtomicInteger waitingMessages;
    private volatile boolean isFull;
    private AtomicInteger processedMessages;
    private BSC connectedBSC;
    private BTS connectedBTS;
    private final int layerNumber;
    public BSCLayer layer;
    private BSCListener listener;
    private volatile boolean running = true;
    private final Object lock;

    public BSC(BSCLayer layer) {
        this.id = PhoneBookLogic.StationsCounter;
        PhoneBookLogic.StationsCounter++;

        gatheredMessages = new ConcurrentLinkedQueue<String>();
        waitingMessages = new AtomicInteger(0);
        isFull = false;
        processedMessages = new AtomicInteger(0);

        this.layer = layer;
        layerNumber = layer.getLayerNumber();

        connectedBSC = null;
        connectedBTS = null;

        lock = new Object();
    }

    @Override
    public void run() {
        System.out.println("BSC: " + id + " started");

        while (running) {

            synchronized (lock) {
                if (!gatheredMessages.isEmpty()) {
                    listener.updateBSCPanel(new UpdateStationPanelUIEvent(this, this.id, this.getProcessedMessages(), this.getWaitingMessages()));
                    processNextMessage();
                    listener.updateBSCPanel(new UpdateStationPanelUIEvent(this, this.id, this.getProcessedMessages(), this.getWaitingMessages()));
                } else {
                    try {
                        lock.wait(100); // czekaj przy braku wiadomości
//                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
//        System.out.println("BSC: " + id + " stopped");
    }

    public void addMessage(String message) {
//        synchronized (lock) {
            gatheredMessages.add(message);
            waitingMessages.incrementAndGet();

            listener.updateBSCPanel(new UpdateStationPanelUIEvent(this, this.id, this.getProcessedMessages(), this.getWaitingMessages()));


        if (gatheredMessages.size() > 5) {
                isFull = true;
            }
//        }
    }

    public void connectToBSC(BSC bsc) {
//        synchronized (lock) {
            this.connectedBSC = bsc;
//        }
    }

    public void connectToBTS(BTS bts) {
//        synchronized (lock) {
            this.connectedBTS = bts;
//        }
    }

    public void processNextMessage() {
        try {
            Thread.sleep((long) (Math.random() * 10000 + 5000)); // wait for random time (5-15s)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String m;

        synchronized (lock) { // gatheredMessages?
            if (!gatheredMessages.isEmpty()) {
                m = gatheredMessages.poll();
                waitingMessages.decrementAndGet();
                processedMessages.incrementAndGet();
                listener.updateBSCPanel(new UpdateStationPanelUIEvent(this, this.id, this.getProcessedMessages(), this.getWaitingMessages()));
                isFull = false;
            } else {
                return; // No messages to process
            }
//        }

        if (layerNumber != BSCManager.getLastLayerNumber()) { // not last layer - pass to next BSC
//            synchronized (BSCManager.class) {
                connectToBSC(BSCManager.getLayerXbsc(layerNumber + 1));
//            }
        } else { // last layer - pass to BTS
//            synchronized (BTSManager.class) {
                connectToBTS(BTSManager.getLayerXBTS(1));
//            }
        }

//        synchronized (lock) {
            if (connectedBSC != null) {
                connectedBSC.addMessage(m);
                connectedBSC = null;
            } else if (connectedBTS != null) {
                connectedBTS.addMessage(m);
                connectedBTS = null;
            } else {
                System.out.println("BSChandlers " + id + " is not connected to any BTS or BSC");
            }
        }
    }

    public void InstantlyPassAllMessages() {
//        synchronized (lock) {
            isFull = true;
            for (String m : gatheredMessages) {
                if (layerNumber != BSCManager.getLastLayerNumber()) { // not last layer - pass to next BSC
                    connectToBSC(BSCManager.getLayerXbsc(layerNumber + 1));
                } else { // last layer - pass to BTS
                    connectToBTS(BTSManager.getLayerXBTS(1));
                }

                if (connectedBSC != null) {
                    connectedBSC.addMessage(m);
                    connectedBSC = null;
                } else if (connectedBTS != null) {
                    connectedBTS.addMessage(m);
                    connectedBTS = null;
                } else {
                    System.out.println("BSChandlers " + id + " is not connected to any BTS or BSC");
                }

                waitingMessages.decrementAndGet();
                processedMessages.incrementAndGet();
            }
            gatheredMessages.clear();
//        }
    }

    public void stopBSC() {
        running = false;
        InstantlyPassAllMessages();
    }

    public void setListener(BSCListener listener) {
            this.listener = listener;
    }

    public int getId() {
        return id;
    }

    public int getProcessedMessages() {
        return processedMessages.get();
    }

    public int getWaitingMessages() {
        return waitingMessages.get();
    }

    public boolean getIsFull () {
        return isFull;
    }
}
