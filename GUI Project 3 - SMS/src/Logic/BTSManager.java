package Logic;

import Handlers.BTSListener;
import Handlers.NullListener;
import Handlers.UpdateStationPanelUIEvent;
import UI.BTSLayerUI;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BTSManager implements BTSListener {

    private static List <BTSLayer> btsLayers;
    private int lastLayerNumber;
    private BTSListener listener;

    public BTSManager () {
        btsLayers = new CopyOnWriteArrayList <BTSLayer>();
        lastLayerNumber = 0;

        listener = new NullListener();
    }

    public synchronized static BTS getLayerXBTS (int x) {
        // wybierz ten BTS z warstwy, który zawiera najmniej SMSów:
        BTS selectedBTS = null;
        int minMessages = Integer.MAX_VALUE;

        for (BTS bts : btsLayers.get(x).getBtsList()) {
            if (!bts.getIsFull()) {
                if (bts.getWaitingMessages() < minMessages) {
                    minMessages = bts.getWaitingMessages();
                    selectedBTS = bts;
                }
            }
        }

        if (selectedBTS != null) {
            return selectedBTS;
        } else {
            btsLayers.get(x).newBTS(); // Jeżeli w danej warstwie ilość SMS w każdym z BSC lub BTS jest większa od 5, automatycznie dodawany jest nowy BTS/BSC;
            return getLayerXBTS(x);
        }

    }


    public void NewBTSLayer () {
        BTSLayer layer = new BTSLayer(lastLayerNumber);
        lastLayerNumber++;
        btsLayers.add(layer);

        // UI:
        BTSListener layerUI = AddNewBTSLayerUI(layer);

        layer.setListener(layerUI);
        layer.newBTS();
    }

    @Override
    public BTSListener AddNewBTSLayerUI (BTSLayer layer) {
        return listener.AddNewBTSLayerUI(layer);
    }

    public void setListener (BTSListener listener) {
        this.listener = listener;
//        System.out.println("BTSManager set listener to: " + listener.getClass().getSimpleName());
    }

    public void stopAllLayers () {
        for (BTSLayer layer : btsLayers) {
            layer.stopLayer();
        }
    }





    @Override
    public BTSListener AddNewBTSPanelUI (BTS bts) {
        return null;
    }
    @Override
    public void updateBTSPanel (UpdateStationPanelUIEvent evt) {}

}
