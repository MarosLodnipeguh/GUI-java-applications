package Graphics;

import Handlers.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class UserPanel extends JPanel implements EventListener {

    private JButton startButton;

    private JLabel tickLabel;
    private JSlider tickSlider;

    private int score;
    private int tickValue;

    private EventListener eventListener;

    public UserPanel () {
        this.tickValue = 5;

        setPreferredSize(new Dimension(210, 160));
        setBorder(BorderFactory.createTitledBorder("User Panel"));

        // ===================================== TICK SLIDER ===================================== //
        tickLabel = new JLabel("Ticks per second: ");

        tickSlider = new JSlider(1, 15, tickValue);
        tickSlider.setMajorTickSpacing(10); // Ustawienie większych kroków
        tickSlider.setMinorTickSpacing(1); // Ustawienie mniejszych kroków
        tickSlider.setPaintTicks(true);
        tickSlider.setPaintLabels(true);

        // opisy przedziałów
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("1"));
//        labelTable.put(3, new JLabel("3"));
        labelTable.put(5, new JLabel("5"));
        labelTable.put(10, new JLabel("10"));
        labelTable.put(15, new JLabel("15"));
        tickSlider.setLabelTable(labelTable);


        tickSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                tickValue = source.getValue();
                tickLabel.setText("Ticks per second: " + tickValue);

                eventListener.changeTick(new TickEvent(this, tickValue));
            }
        });
        // ===================================== NEW GAME BUTTON ===================================== //
        startButton = new JButton("New game");

        startButton.addActionListener(e -> {
            if (eventListener.getGameState() == true) {
                forceGameOver();
                String name = GetSnakeName();
                startGame(new NewGameEvent(this, name, tickValue, 25, 16));
            }
            else {
                String name = GetSnakeName();
                startGame(new NewGameEvent(this, name, tickValue, 25, 16));
            }

        });
        // ===================================== SCORE COUNTER ===================================== //




        add(startButton);
        add(tickLabel);
        add(tickSlider);


    }



    private String GetSnakeName () {
        String text = JOptionPane.showInputDialog("Name your Snake: ");
        if (text != null && !text.isEmpty()) {
            return text;
        }
        return null;
    }

    @Override
    public void startGameNoButton () {
        String name = GetSnakeName();
        startGame(new NewGameEvent(this, name, 5, 25, 16));
    }

    @Override
    public boolean getGameState () {
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Dialog", Font.TYPE1_FONT, 12));
        g.drawString("Score: " + score, 250, 100);
    }

    public void setEventListener (EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void startGame (NewGameEvent evt) {
        eventListener.startGame(evt);
    }

    @Override
    public void updateScore (ConsumptionEvent evt) {
        score = evt.getScore();
    }

    @Override
    public void changeTick (TickEvent evt) {

    }

    @Override
    public void gameOver (ImpactEvent evt) {

    }

    public void forceGameOver () {
        eventListener.forceGameOver();
    }

}
