package Handlers;

import Logic.BTS;
import Logic.BTSLayer;

public interface BTSListener {

    // Logic to UI:
    BTSListener AddNewBTSLayerUI (BTSLayer layer); // BTSManager -> StationsPanel
    BTSListener AddNewBTSPanelUI (BTS bts); // BTSLayer -> BTSLayerUI
    void updateBTSPanel(UpdateStationPanelUIEvent evt); // BTS -> BTSPanelUI

}
