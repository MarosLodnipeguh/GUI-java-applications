package UI;

import Logic.BSC;
import Handlers.BSCListener;
import Handlers.UpdateStationPanelUIEvent;
import Logic.BSCLayer;

import javax.swing.*;
import java.awt.*;

public class BSCPanelUI extends JPanel implements BSCListener {
    private JLabel numberLabel;
    private JLabel processedMessagesNumber;
    private JLabel waitingMessagesNumber;

    public BSCPanelUI (BSC bsc) {
        setPreferredSize(new Dimension(100, 75));
        setBorder(BorderFactory.createTitledBorder("BSC"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===================================== CONTENT ===================================== //
        numberLabel = new JLabel("Station ID: " + bsc.getId());
        processedMessagesNumber = new JLabel("Processed: " + bsc.getProcessedMessages());
        waitingMessagesNumber = new JLabel("Waiting: " + bsc.getWaitingMessages());

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
    public /*synchronized*/ void updateBSCPanel (UpdateStationPanelUIEvent evt) {
        updateProcessedMessagesNumber(evt.getProcessedMessages());
        updateWaitingMessagesNumber(evt.getWaitingMessages());
    }


    @Override
    public BSCListener AddNewBSCLayerUI(BSCLayer layer) {
        return null;
    }

    @Override
    public BSCListener AddNewBSCPanelUI(BSC bsc) {
        return null;
    }



    @Override
    public void AddNewBSCLayer () {

    }

    @Override
    public void RemoveLastBSCLayer () {

    }


}
