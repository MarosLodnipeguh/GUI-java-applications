package UI;

import Logic.BSC;
import Logic.BSCLayer;
import Handlers.BSCListener;
import Handlers.UpdateStationPanelUIEvent;

import javax.swing.*;
import java.awt.*;

public class BSCLayerUI extends JPanel implements BSCListener {

    private JPanel stationsContainer;
    JScrollPane scroll;
    private BSCLayer bscLayer;

    public BSCLayerUI (BSCLayer layer) {

        bscLayer = layer;

        setPreferredSize(new Dimension(130, 474));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        stationsContainer = new JPanel();
        stationsContainer.setLayout(new BoxLayout(stationsContainer, BoxLayout.PAGE_AXIS));

        scroll = new JScrollPane(stationsContainer);
        scroll.setBorder(BorderFactory.createTitledBorder("BSC Layer " + layer.getLayerNumber()));
        add(scroll, BorderLayout.CENTER);


    }


    @Override
    public BSCListener AddNewBSCPanelUI (BSC bsc) {
        BSCPanelUI ui = new BSCPanelUI(bsc);

        SwingUtilities.invokeLater(() -> stationsContainer.add(ui));
        SwingUtilities.invokeLater(() -> stationsContainer.revalidate());
        SwingUtilities.invokeLater(() -> stationsContainer.repaint());
        SwingUtilities.invokeLater(() -> scroll.repaint());

        revalidate();
        repaint();

        return ui;
    }



    @Override
    public void updateBSCPanel (UpdateStationPanelUIEvent evt) {

    }

    @Override
    public void AddNewBSCLayer () {

    }

    @Override
    public void RemoveLastBSCLayer () {

    }
    @Override
    public BSCListener AddNewBSCLayerUI(BSCLayer layer) {
        return null;
    }
}