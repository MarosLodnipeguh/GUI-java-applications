package Logic;

// Virtual Receiver Device (Receiver)

import Handlers.VRDListener;
import SMS.PhoneBookLogic;
import UI.VRDPanelUI;

import java.util.concurrent.ConcurrentLinkedQueue;

import static SMS.PhoneBookLogic.phoneBook;
import static SMS.PhoneBookLogic.recipientBook;


public class VRD implements Runnable, VRDListener {
    private final int number;
    private final ConcurrentLinkedQueue<String> receivedMessages;
    private boolean autoDelete;
    private VRDListener listener;
    private volatile boolean running = true;
//    private final Object lock;

    public VRD () {
        this.number = PhoneBookLogic.generateNumber();
        this.receivedMessages = new ConcurrentLinkedQueue<String>();
        this.autoDelete = false;

        phoneBook.add(number);
        recipientBook.add(number);

        //DEBUG:
        System.out.println("VRD created with number: " + number);

//        lock = new Object();
    }

    @Override
    public void run() {
        System.out.println("VRD: " + number + " started");

        while (running) {
//            synchronized (lock) {
                if (autoDelete) {

                    receivedMessages.clear();
                    UpdateVRDPanelUI(receivedMessagesCount());

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
//                        lock.wait(100);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//            }

        }
//        System.out.println("VRD: " + number + " stopped");
    }






    public /*synchronized*/ void addMessage(String message) {
        receivedMessages.add(message);
        UpdateVRDPanelUI(receivedMessagesCount());
//        System.out.println("VRD: " + number + " received message ");
    }

    public int receivedMessagesCount() {
//        synchronized (receivedMessages) {
            return receivedMessages.size();
//        }
    }

    public int getNumber () {
        return number;
    }

    public void setAutoDelete (boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    @Override
    public void UpdateVRDPanelUI(int receivedCount) {
//        synchronized (listener) {
            listener.UpdateVRDPanelUI(receivedCount);
//        }
    }

    public void stopVRD () {
        running = false;
    }

    public void setListener (VRDListener listener) {
        this.listener = listener;
    }

    @Override
    public VRDListener AddNewVRDPanelUI (VRD vrd) {
        return null;
    }

    @Override
    public void setLogicListener (VRDListener listener) {

    }

    @Override
    public void AddNewVRD () {

    }
    @Override
    public void RemoveVRD (VRD vrd) {

    }
    @Override
    public void RemoveVRDPanelUI (VRDPanelUI ui) {

    }
}
