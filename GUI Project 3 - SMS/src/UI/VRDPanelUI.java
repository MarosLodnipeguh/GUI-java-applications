package UI;

import Handlers.VRDListener;
import Logic.VRD;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class VRDPanelUI extends JPanel implements VRDListener {

    private JLabel receivedMessagesNumber;
    private JCheckBox autoMessageDelete;
    private JButton removeButton;
    private VRDListener LogicListener;
    private VRDListener UIListener;

    public VRDPanelUI (VRD vrd) {
        setPreferredSize(new Dimension(210, 110));
        setBorder(BorderFactory.createTitledBorder("VRD"));

        // ===================================== CHECK BOX ===================================== //
        autoMessageDelete = new JCheckBox("Auto delete messages (10s) : no");

        autoMessageDelete.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (autoMessageDelete.isSelected()) {
                    vrd.setAutoDelete(true);
                    autoMessageDelete.setText("Auto delete messages (10s) : yes");
                } else {
                    vrd.setAutoDelete(false);
                    autoMessageDelete.setText("Auto delete messages (10s) : no");
                }
            }
        });

        // ===================================== RECEIVED NUMBER ===================================== //
        receivedMessagesNumber = new JLabel("Received messages: " + vrd.receivedMessagesCount());
        // ===================================== STOP BUTTON ===================================== //
        removeButton = new JButton("Remove VRD");

        removeButton.addActionListener(e ->  {
            RemoveVRD(vrd);
        });



        add(receivedMessagesNumber);
        add(autoMessageDelete);
        add(removeButton);

    }

    public void setLogicListener (VRDListener listenerLogic) {
        this.LogicListener = listenerLogic;
    }

    public void setUIListener (VRDListener UIListener) {
        this.UIListener = UIListener;
    }

    @Override
    public void RemoveVRD (VRD vrd) {
        LogicListener.RemoveVRD(vrd);
        RemoveVRDPanelUI(this);
    }

    @Override
    public void RemoveVRDPanelUI (VRDPanelUI ui) {
        UIListener.RemoveVRDPanelUI(ui);
    }

    @Override
    public void UpdateVRDPanelUI (int receivedCount) {
        updateReceivedMessagesNumber(receivedCount);
    }

    public void updateReceivedMessagesNumber (int count) {
        SwingUtilities.invokeLater(() -> receivedMessagesNumber.setText("Received messages: " + count));
    }






    @Override
    public VRDListener AddNewVRDPanelUI (VRD vrd) {
        return null;
    }

    @Override
    public void AddNewVRD () {

    }


}
