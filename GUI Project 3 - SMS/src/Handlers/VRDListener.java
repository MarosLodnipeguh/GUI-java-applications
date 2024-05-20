package Handlers;

import Logic.VRD;
import UI.VRDPanelUI;

public interface VRDListener {

    // Logic to UI:
    VRDListener AddNewVRDPanelUI(VRD vrd); // MainLogic -> VRDLayerUI
    void setLogicListener(VRDListener listener);
    void UpdateVRDPanelUI(int receivedCount); // VRD -> VRDPanelUI

    // UI to Logic:
    void AddNewVRD(); // VRDLayerUI -> MainLogic
    void RemoveVRD(VRD vrd); // VRDPanelUI -> MainLogic

    // UI to UI:
    void RemoveVRDPanelUI(VRDPanelUI ui); // VRDLayerUI -> ReceiverPanel

}
