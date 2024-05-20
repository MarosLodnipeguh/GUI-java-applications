import Enums.Direction;
import Graphics.MainFrame;
import Logic.GameManager;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainApp {

    private static int boardWidth = 25;
    private static int boardHeight = 16;

    public static void main (String[] args) {

        SwingUtilities.invokeLater(() -> {

            GameManager manager = new GameManager();
            MainFrame graphics = new MainFrame(boardWidth, boardHeight);

            graphics.setMovementListener(manager);
            graphics.getUserPanel().setEventListener(manager);

            manager.setMovementListener(graphics);
            manager.setEventListener(graphics.getUserPanel());

            graphics.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed (KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> manager.setDirection(Direction.UP);
                        case KeyEvent.VK_DOWN -> manager.setDirection(Direction.DOWN);
                        case KeyEvent.VK_LEFT -> manager.setDirection(Direction.LEFT);
                        case KeyEvent.VK_RIGHT -> manager.setDirection(Direction.RIGHT);
                    }
                }
            });


        });



    }

}