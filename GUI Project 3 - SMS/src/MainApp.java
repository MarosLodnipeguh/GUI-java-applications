import Logic.MainLogic;
import UI.MainFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class MainApp {

    public static void main (String[] args) throws InterruptedException, InvocationTargetException {

        SwingUtilities.invokeAndWait(() -> {

//            startGraphics();

//            startLogic();

            startLogicAndGraphics();


        });

    }

    public static void startLogicAndGraphics () {

        MainLogic logic = new MainLogic();

        MainFrame graphics = new MainFrame();

        // set Logic Listeners to send events to UI
        logic.setVbdListener(graphics.getSender());
        logic.getBscManager().setListener(graphics.getStations());
        logic.getBtsManager().setListener(graphics.getStations());
        logic.setVrdListener(graphics.getReceiver());

        // set UI Listeners to send events to Logic
        graphics.getSender().setListener(logic);
        graphics.getStations().setBSClistener(logic.getBscManager());
        graphics.getReceiver().setListener(logic);

        // stan uruchomieniowy:
        logic.getBtsManager().NewBTSLayer();
        logic.getBscManager().AddNewBSCLayer();
        logic.getBtsManager().NewBTSLayer();

        // ======================================================= ON APPLICATION CLOSE =======================================================
        graphics.addFunction(logic::stopAllThreads);
        graphics.addFunction(logic::writeVBDsDataToFile);
    }

    public static void startLogic () {
        MainLogic logic = new MainLogic();

        logic.getBtsManager().NewBTSLayer();
        logic.getBscManager().AddNewBSCLayer();
        logic.getBtsManager().NewBTSLayer();
    }

    public static void startGraphics () {
        MainFrame graphics = new MainFrame();
    }


}





