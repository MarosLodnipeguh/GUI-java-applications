package Handlers;

import Logic.VBD;
import UI.VBDPanelUI;

public interface VBDListener {

    // Logic to UI:
    VBDListener AddNewVBDPanelUI(VBD vbd); // MainLogic -> VBDLayerUI
    void setLogicListener(VBDListener listener);

    // UI to Logic:
    void AddNewVBD(String messageText); // VBDLayerUI -> MainLogic
    void RemoveVBD(VBD vbd); // VBDPanelUI -> MainLogic

    // UI to UI:
    void RemoveVBDPanelUI(VBDPanelUI ui); // VBDLayerUI -> SenderPanel


}
