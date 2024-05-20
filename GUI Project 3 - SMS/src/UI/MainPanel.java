package UI;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private SenderPanel sender;
    private StationsPanel stations;
    private ReceiverPanel receiver;

    public MainPanel () {
        setLayout(new BorderLayout());

        sender = new SenderPanel();
        sender.setBorder(BorderFactory.createTitledBorder("Send"));
        sender.setPreferredSize(new Dimension(260, 600));

        stations = new StationsPanel();
//        BSCManager.setListener(stations);
        stations.setBorder(BorderFactory.createTitledBorder("Controller Stations"));
//        stations.setPreferredSize(new Dimension(600, 600));

        receiver = new ReceiverPanel();
//        receiver.setBackground(Color.decode("#F5EFE6"));
        receiver.setBorder(BorderFactory.createTitledBorder("Receive"));
        receiver.setPreferredSize(new Dimension(260, 600));

        add(sender, BorderLayout.WEST);
        add(stations, BorderLayout.CENTER);
        add(receiver, BorderLayout.EAST);
    }

    public SenderPanel getSender () {
        return sender;
    }

    public StationsPanel getStations () {
        return stations;
    }

    public ReceiverPanel getReceiver () {
        return receiver;
    }


}