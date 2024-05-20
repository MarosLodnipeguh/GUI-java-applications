package Handlers;

import Logic.BSC;
import Logic.BSCLayer;
import UI.BSCLayerUI;
import UI.BSCPanelUI;

public interface BSCListener {

    // Logic to UI:
    BSCListener AddNewBSCLayerUI(BSCLayer layer); // BSCManager -> StationsPanel
    BSCListener AddNewBSCPanelUI(BSC bsc); // BSCLayer -> BSCLayerUI
    void updateBSCPanel(UpdateStationPanelUIEvent evt); // BSC -> BSCPanelUI


    // UI to Logic: (+ - buttons)
    void AddNewBSCLayer(); // StationsPanel -> BSCManager
    void RemoveLastBSCLayer(); // StationsPanel -> BSCManager

}
