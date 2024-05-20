package ListenersExample;

import ListenersExample.exent.ChangeColorListener;
import ListenersExample.exent.ColorEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public
    class LeftPanel
    extends JPanel {

    public LeftPanel() {
        this.setBackground(Color.GREEN);
        this.setPreferredSize(new Dimension( 300, 300));

        JButton jButton = new JButton("click me");
        this.add(jButton);

        jButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireColorSet(
                        new Color(
                            (int)(Math.random()*255),
                            (int)(Math.random()*255),
                            (int)(Math.random()*255)
                        )
                    );
                }
            }
        );
    }

    private ArrayList<ChangeColorListener> listeners = new ArrayList<>();

    public void addChangeColorListener(ChangeColorListener listener){ // argument tutaj to klasa docelowa która nasłuchuje na event! w której sie coś stanie i trafia do listy słuchaczy
        this.listeners.add(listener);
    }

    public void removeChangeColorListener(ChangeColorListener listener){
        this.listeners.remove(listener);
    }

    public void fireColorSet(Color color){
        ColorEvent evt = new ColorEvent( this, color);
        for(ChangeColorListener listener : listeners)
            listener.colorSet(evt);
    }

////    // można też tak, bez kolekcji listenerów:
////    ChangeColorListener listenerTest = new ChangeColorListener() {
////        ColorEvent evt = new ColorEvent( this, color);
////        @Override
////        public void colorSet (ColorEvent evt) {
////
////        }
//    }
}
