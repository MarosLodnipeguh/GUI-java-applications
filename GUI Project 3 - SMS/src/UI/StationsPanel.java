package UI;

import Handlers.NullListener;
import Handlers.UpdateStationPanelUIEvent;
import Handlers.BSCListener;
import Handlers.BTSListener;
import Logic.BSC;
import Logic.BSCLayer;
import Logic.BTS;
import Logic.BTSLayer;

import javax.swing.*;
import java.awt.*;

// BTS & BSC CONTROLLER STATIONS PANEL - MIDDLE
public class StationsPanel extends JPanel implements BTSListener, BSCListener {

    private JScrollPane scroll;
    private JPanel stationsContainer;
    private JPanel BTSLayer1;
    private JPanel BSCLayersContainer;
    private JPanel BTSLayer2;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton removeButton;

    private BSCListener BSClistener;


    public StationsPanel () {
        BSClistener = new NullListener();

        setLayout(new BorderLayout());

        addButton = new JButton("Add Layer");
        addButton.addActionListener(e -> {
            AddNewBSCLayer();
        });

        removeButton = new JButton("Remove Layer");
        removeButton.addActionListener(e -> {
            RemoveLastBSCLayer();
        });

        stationsContainer = new JPanel();
        stationsContainer.setLayout(new BoxLayout(stationsContainer, BoxLayout.X_AXIS));

        // 1st BTS LAYER:
        BTSLayer1 = new JPanel();
        BTSLayer1.setPreferredSize(new Dimension(100, 474));
        stationsContainer.add(BTSLayer1);

        // BSC LAYERS:
        BSCLayersContainer = new JPanel();
        JScrollPane BSCscroll = new JScrollPane(BSCLayersContainer);
        BSCscroll.setPreferredSize(new Dimension(600, 474));
        stationsContainer.add(BSCscroll);

        // 2nd BTS LAYER:
        BTSLayer2 = new JPanel();
        stationsContainer.add(BTSLayer2);

        // BOTTOM BUTTONS:
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        add(stationsContainer, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public void refresh () {
        repaint();
        revalidate();
    }

    // ======================================== FROM LOGIC METHODS: ========================================

    @Override
    public BTSListener AddNewBTSLayerUI (BTSLayer layer) {
        BTSLayerUI layerUI = new BTSLayerUI(layer);

        if (layerUI.getLayerNumber() == 0) {
            BTSLayer1.add(layerUI);
            BTSLayer1.repaint();
            BTSLayer1.revalidate();
        } else {
            BTSLayer2.add(layerUI);
            BTSLayer2.repaint();
            BTSLayer2.revalidate();
        }

        return layerUI;

    }

    @Override
    public BSCListener AddNewBSCLayerUI(BSCLayer layer) { // zwracane przez nowo utworzony BSC
        BSCLayerUI layerUI = new BSCLayerUI(layer);


        BSCLayersContainer.add(layerUI);
        BSCLayersContainer.repaint();
        BSCLayersContainer.revalidate();

        return layerUI;
    }

    // ======================================== SEND TO LOGIC METHODS: ========================================
    @Override
    public void AddNewBSCLayer () { // wysyłane przez przycisk do BSCManagera by utworzył nowy BSC
        BSClistener.AddNewBSCLayer();

        revalidate();
        repaint();
    }

    @Override
    public void RemoveLastBSCLayer() {
        BSClistener.RemoveLastBSCLayer();

        BSCLayersContainer.remove(BSCLayersContainer.getComponentCount()-1);
        BSCLayersContainer.repaint();
        BSCLayersContainer.revalidate();

        revalidate();
        repaint();
    }





    public void setBSClistener (BSCListener l) {
        BSClistener = l;
        System.out.println("StationsPanel listener set to " + l.getClass().getSimpleName() );
    }











    @Override
    public BSCListener AddNewBSCPanelUI(BSC bsc) {
        return null;
    }





    @Override
    public void updateBSCPanel (UpdateStationPanelUIEvent evt) {

    }




    @Override
    public BTSListener AddNewBTSPanelUI (BTS bts) {

        return null;
    }

    @Override
    public void updateBTSPanel (UpdateStationPanelUIEvent evt) {

    }
}
