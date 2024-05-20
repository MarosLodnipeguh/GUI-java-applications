package Logic;

// Virtual Base Device (Sender)

import SMS.Message;
import SMS.PhoneBookLogic;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static SMS.PhoneBookLogic.phoneBook;

public class VBD implements Runnable {
    private final int number;
    private int recipient;
    private final String messageText;
    private AtomicInteger sentMessages;
    private volatile boolean isSending;
    private double sendFrequency;
    private BTS connectedBTS;
    private volatile boolean running = true;
//    private final Object lock;


    public VBD (String messageText) {

        this.number = PhoneBookLogic.generateNumber();

        this.messageText = messageText;

        this.isSending = false;
        this.sendFrequency = 5000; // 5000

        phoneBook.add(number);

        // DEBUG:
        System.out.println("VBD created with number: " + number);

        sentMessages = new AtomicInteger(0);

//        lock = new Object();
    }

    @Override
    public /*synchronized*/ void run () {

        while (running) {
//            synchronized (lock) {
                if (isSending) {

                    // GENERATE MESSAGE:
                    recipient = PhoneBookLogic.getRandomRecipient();
                    Message message = new Message(this.number, recipient, this.messageText);

                    // ENDODE MESSAGE:
                    String messageInHexString = "";

                    if (messageText != null) {
                        byte[] messageInBytes = message.encodeMessage(message);
                        messageInHexString = byteArrayToHexString(messageInBytes);
                    }

                    // SEND MESSAGE:
                    connectToBTS(BTSManager.getLayerXBTS(0));
                    connectedBTS.addMessage(messageInHexString);
                    sentMessages.incrementAndGet();

//                    System.out.println("VBD: " + number + " sent message to: " + recipient);

                    try {
                        Thread.sleep((long) sendFrequency);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                else {
                    try {
//                        lock.wait(100);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//            }

        }
//        System.out.println("VBD: " + number + " stopped");
    }

    public /*synchronized*/ void connectToBTS (BTS bts) {
        this.connectedBTS = bts;
    }

    public void setSending (boolean sending) {
        System.out.println("VBD: " + number + " is sending: " + sending);
        isSending = sending;
    }

    public void setSendFrequency (double sendFrequency) {
        this.sendFrequency = sendFrequency;
    }

    public void stopVBD () {
        running = false;
    }

    public int getNumber () {
        return number;
    }



    public /*synchronized*/ void writeAllMessagesToFile (String filename) {

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename, true))) {

            String serializedVBD = this.getNumber() + " " + this.sentMessages + "x: '" + messageText + "'\n";

            bos.write(serializedVBD.getBytes());

            System.out.println("Plik zapisany");

        } catch (IOException e) {
            System.out.println("Błąd zapisu pliku: " + e.getMessage());
        }

    }



    public String byteArrayToHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }






}

