package UI;

import Logic.VRD;
import Handlers.VRDListener;

import javax.swing.*;
import java.awt.*;

// VRD RECEIVER DEVICE PANEL - RIGHT SIDE
public class ReceiverPanel extends JPanel implements VRDListener {

    private JScrollPane scroll;
    private JPanel devicesContainer;
    private JButton addButton;

    private VRDListener listener;

    public ReceiverPanel () {
        setLayout(new BorderLayout());


        addButton = new JButton("Add");
        addButton.addActionListener(e ->  {
            AddNewVRD();
        });

        add(addButton, BorderLayout.SOUTH);

        devicesContainer = new JPanel();
        devicesContainer.setLayout(new BoxLayout(devicesContainer, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(devicesContainer);

        add(scroll);

    }

    @Override
    public void AddNewVRD () {
        listener.AddNewVRD();
    }

    @Override
    public VRDListener AddNewVRDPanelUI (VRD vrd) {
        VRDPanelUI ui = new VRDPanelUI(vrd);

        ui.setUIListener(this);
        devicesContainer.add(ui);
        devicesContainer.revalidate();
        devicesContainer.repaint();

        revalidate();
        repaint();

        return ui;
    }

    @Override
    public void setLogicListener (VRDListener listener) {

    }

    @Override
    public void RemoveVRDPanelUI (VRDPanelUI ui) {
        devicesContainer.remove(ui);
        devicesContainer.revalidate();
        devicesContainer.repaint();

        revalidate();
        repaint();
    }

    public void setListener (VRDListener listener) {
        this.listener = listener;
    }




    @Override
    public void UpdateVRDPanelUI (int receivedCount) {

    }



    @Override
    public void RemoveVRD (VRD vrd) {

    }


}

