package UI;

import Handlers.VBDListener;
import Logic.VBD;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class VBDPanelUI extends JPanel implements VBDListener {
    private JLabel numberLabel;
    private JTextField deviceNumberTextField;
    private JComboBox<String> stateComboBox;
    private JLabel frequencyLabel;
    private JSlider frequencySlider;
    private JButton removeButton;

    private VBDListener LogicListener;
    private VBDListener UIListener;



    public VBDPanelUI (VBD vbd) {



        setPreferredSize(new Dimension(210, 160));
        setBorder(BorderFactory.createTitledBorder("VBD"));


        // ===================================== CONTENT ===================================== //
        numberLabel = new JLabel("Number: ");
        // ===================================== NUMBER FIELD ===================================== //
        deviceNumberTextField = new JTextField( String.valueOf(vbd.getNumber()) );
        deviceNumberTextField.setEditable(false);

        // ===================================== COMBO BOX ===================================== //
        stateComboBox = new JComboBox<>(new String[]{"WAITING", "ACTIVE"});

        stateComboBox.addActionListener(e -> {
            String selectedOption = (String) stateComboBox.getSelectedItem();
            if (selectedOption.equals("ACTIVE")) {
                vbd.setSending(true);
            } else if (selectedOption.equals("WAITING")) {
                vbd.setSending(false);
            }
        });

        // ===================================== SLIDER ===================================== //
        frequencyLabel = new JLabel("Frequency of sending: 5s");

        //Slider w ms będzie 100ms = 0.1s, 10000ms = 10s

        frequencySlider = new JSlider(100, 10000, 5000);
        frequencySlider.setMajorTickSpacing(10000); // Ustawienie większych kroków
        frequencySlider.setMinorTickSpacing(1000); // Ustawienie mniejszych kroków
        frequencySlider.setPaintTicks(true);
        frequencySlider.setPaintLabels(true);

        // opisy przedziałów
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(100, new JLabel("0.1s"));
        labelTable.put(3000, new JLabel("3s"));
        labelTable.put(5000, new JLabel("5s"));
        labelTable.put(7000, new JLabel("7s"));
        labelTable.put(10000, new JLabel("10s"));
        frequencySlider.setLabelTable(labelTable);


        frequencySlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                double value = source.getValue();
                frequencyLabel.setText("Frequency of sending: " + value/1000 + "s");
                vbd.setSendFrequency(value);
            }
        });
        // ===================================== STOP BUTTON ===================================== //
        removeButton = new JButton("Remove VBD");

        removeButton.addActionListener(e -> {
            RemoveVBD(vbd);
        });



        add(numberLabel);
        add(deviceNumberTextField);
        add(stateComboBox);
        add(frequencyLabel);
        add(frequencySlider);
        add(removeButton);

    }

    @Override
    public void setLogicListener (VBDListener listenerLogic) {
        this.LogicListener = listenerLogic;
    }

    public void setUIListener (VBDListener UIListener) {
        this.UIListener = UIListener;
    }

    @Override
    public void RemoveVBD (VBD vbd) {
        LogicListener.RemoveVBD(vbd);
        RemoveVBDPanelUI(this);
    }

    @Override
    public void RemoveVBDPanelUI (VBDPanelUI ui) {
        UIListener.RemoveVBDPanelUI(ui);
    }



    @Override
    public VBDListener AddNewVBDPanelUI (VBD vbd) {
        return null;
    }

    @Override
    public void AddNewVBD (String messageText) {

    }


}
