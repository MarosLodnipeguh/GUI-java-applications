package Logic;

import Handlers.BTSListener;
import Handlers.NullListener;
import Handlers.UpdateStationPanelUIEvent;
import UI.BTSPanelUI;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BTSLayer implements BTSListener {
    private final ConcurrentLinkedQueue <BTS> btsList;
    private int layerNumber;
    private BTSListener listener;

    public BTSLayer (int layerNumber) {
        this.btsList = new ConcurrentLinkedQueue <BTS>();
        this.layerNumber = layerNumber;

        this.listener = new NullListener();
    }

    public void newBTS () {
        BTS bts = new BTS(this);
        btsList.add(bts);

        //UI:
        BTSListener ui = AddNewBTSPanelUI(bts);
        bts.setListener(ui);

        Thread btsThread = new Thread(bts);
        btsThread.start();
    }

    @Override
    public BTSListener AddNewBTSPanelUI (BTS bts) {
        return listener.AddNewBTSPanelUI(bts);
    }

    public void setListener (BTSListener listener) {
        this.listener = listener;
    }

    public ConcurrentLinkedQueue <BTS> getBtsList () {
        return btsList;
    }

    public int getLayerNumber () {
        return layerNumber;
    }

    @Override
    public BTSListener AddNewBTSLayerUI (BTSLayer layer) {
        return null;
    }
    @Override
    public void updateBTSPanel (UpdateStationPanelUIEvent evt) {}

    public void stopLayer () {
        for (BTS bts : btsList) {
            bts.stopBTS();
        }
    }
}
