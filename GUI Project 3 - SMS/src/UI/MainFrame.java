package UI;

import Handlers.MainFrameFunctionsListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    MainPanel mainPanel;

    SenderPanel sender;
    StationsPanel stations;
    ReceiverPanel receiver;


    public MainFrame () throws HeadlessException {
        setTitle("Symulator SMS");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1600, 600);

        mainPanel = new MainPanel();

        add(mainPanel);
        setVisible(true);


        sender = mainPanel.getSender();
        stations = mainPanel.getStations();
        receiver = mainPanel.getReceiver();


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.out.println("Window closed");
                System.out.println("STOPPING ALL THREADS IN BACKGROUND AND GENERATING DATA FILE");

                callFunctions();

                System.exit(0);
            }
        });
    }



    private List <MainFrameFunctionsListener> functions = new ArrayList<>();
    public void addFunction (MainFrameFunctionsListener function) {
        functions.add(function);
    }
    public void callFunctions () {
        for (MainFrameFunctionsListener function : functions) {
            function.execute();
        }
    }


    public SenderPanel getSender () {
        return sender;
    }

    public StationsPanel getStations () {
        return stations;
    }

    public ReceiverPanel getReceiver () {
        return receiver;
    }
}
