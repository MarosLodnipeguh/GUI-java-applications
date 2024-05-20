package ListenersExample;

import ListenersExample.exent.ChangeColorListener;
import ListenersExample.exent.ColorEvent;

import javax.swing.*;
import java.awt.*;

public
    class RightPanel
    extends JPanel
    implements ChangeColorListener {

    public RightPanel() {
        this.setBackground(Color.RED);
        this.setPreferredSize(new Dimension( 300, 300));
    }

    @Override
    public void colorSet(ColorEvent evt) {
        this.setBackground(evt.getColor());
    }
}
