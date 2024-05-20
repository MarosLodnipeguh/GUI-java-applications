package UI;

import Handlers.BTSListener;
import Handlers.UpdateStationPanelUIEvent;
import Logic.BTS;
import Logic.BTSLayer;

import javax.swing.*;
import java.awt.*;

public class BTSPanelUI extends JPanel implements BTSListener {
    private JLabel numberLabel;
    private JLabel processedMessagesNumber;
    private JLabel waitingMessagesNumber;

    public BTSPanelUI (BTS bts) {
        setPreferredSize(new Dimension(100, 75));
        setBorder(BorderFactory.createTitledBorder("BTS"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===================================== CONTENT ===================================== //
        numberLabel = new JLabel("Station ID: " + bts.getId());
        processedMessagesNumber = new JLabel("Processed: " + bts.getProcessedMessages());
        waitingMessagesNumber = new JLabel("Waiting: " + bts.getWaitingMessages());

        add(numberLabel);
        add(processedMessagesNumber);
        add(waitingMessagesNumber);

    }

    public void updateProcessedMessagesNumber (int processedMessages) {
        SwingUtilities.invokeLater(() -> processedMessagesNumber.setText("Processed: " + processedMessages));
    }
    public void updateWaitingMessagesNumber (int waitingMessages) {
        SwingUtilities.invokeLater(() -> waitingMessagesNumber.setText("Waiting: " + waitingMessages));
    }
    @Override
    public /*synchronized*/ void updateBTSPanel (UpdateStationPanelUIEvent evt) {
        updateProcessedMessagesNumber(evt.getProcessedMessages());
        updateWaitingMessagesNumber(evt.getWaitingMessages());
    }





    @Override
    public BTSListener AddNewBTSLayerUI (BTSLayer layer) {
        return null;
    }

    @Override
    public BTSListener AddNewBTSPanelUI (BTS bts) {
        return null;
    }

}
