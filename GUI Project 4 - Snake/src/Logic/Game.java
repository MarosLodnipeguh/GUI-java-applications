package Logic;

import Enums.Direction;
import Handlers.*;

import java.awt.*;
import java.util.ArrayList;


public class Game extends Thread {

    private int tick;
    private int[][] board;
    private volatile boolean running;
    private Direction direction;
    private int bodyParts;
    private String snakeName;
    private final int[] x;
    private final int[] y;
    private ArrayList<Dimension> foodPositions = new ArrayList<>();

    private GameManager manager;


    public Game (GameManager manager, String snakeName, int tick, int boardWidth, int boardHeight) {
        this.tick = tick;
        this.x = new int[boardWidth * boardHeight];
        this.y = new int[boardWidth * boardHeight];
        this.board = new int[boardHeight][boardWidth];

        this.manager = manager;
        this.snakeName = snakeName;

        direction = Direction.DOWN;
        bodyParts = 0;

        generateHead();

        generateFood();
        generateFood();
        generateFood();
        generateFood();
        generateFood();
//        for (int i = 0; i < 300; i++) {
//            generateFood();
//        }

        running = true;

    }

    // 1 - snake head
    // 2 - snake body
    // 3 - food

    public void run() {
        System.out.println("Game thread started");


        while (running) {

            checkEat();

            try {
                move();
                checkEat();
            } catch (ArrayIndexOutOfBoundsException e) {
                manager.gameOver(new ImpactEvent(this, x[0], y[0], snakeName, bodyParts+1));
            }

            fillGraphics(board);

            try {
                Thread.sleep(1000/tick); // tick = refresh per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Game thread stopped");
    }




    private void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[(i - 1)]; // ostatnia wartość pozycji ciała jest teraz przedostatnią
            y[i] = y[(i - 1)];
        }

        switch (direction) {

            case UP -> {
                y[0]--;
                checkBodyCollision();
            }

            case DOWN -> {
                y[0]++;
                checkBodyCollision();
            }

            case LEFT -> {
                x[0]--;
                checkBodyCollision();
            }

            case RIGHT -> {
                x[0]++;
                checkBodyCollision();
            }
        }

        // clear the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 3) {
                    board[i][j] = 0;
                }
            }
        }

        board[y[0]][x[0]] = 1; // dodanie głowy
        for (int i = 1; i <= bodyParts; i++) {
            board[y[i]][x[i]] = 2; // dodanie ciała
        }

//        System.out.println("head at: " + x[0] + " " + y[0]);
    }

    private void checkEat() {

        for (int i = 0; i < foodPositions.size(); i++) {
            if (x[0] == foodPositions.get(i).height && y[0] == foodPositions.get(i).width) {
                bodyParts++;
                manager.updateScore(new ConsumptionEvent(this, bodyParts+1));
                foodPositions.remove(i);
                generateFood();
            }
        }

    }


    private void checkBodyCollision () {

        for (int i = bodyParts; i > 0; i--) {
            if ((i > 3) && (x[0] == x[i]) && (y[0] == y[i])) {
                manager.gameOver(new ImpactEvent(this, x[0], y[0], snakeName, bodyParts+1));
            }
        }

    }

    public void setDirection (Direction direction) {
        if (this.direction == Direction.UP && direction == Direction.DOWN) {
            return;
        }
        if (this.direction == Direction.DOWN && direction == Direction.UP) {
            return;
        }
        if (this.direction == Direction.LEFT && direction == Direction.RIGHT) {
            return;
        }
        if (this.direction == Direction.RIGHT && direction == Direction.LEFT) {
            return;
        }
        this.direction = direction;
    }


    public void fillGraphics (int[][] board) {
        manager.fillGameboard(board);
    }

    public void generateHead() {
        x[0] = 0;
        y[0] = 0;
    }

    public void generateFood() {

        int randomX = (int) (Math.random() * board.length);
        int randomY = (int) (Math.random() * board[0].length);

        if (board[randomX][randomY] == 0) {
            board[randomX][randomY] = 3;
            foodPositions.add(new Dimension(randomX, randomY));
        } else {
            generateFood();
        }

    }

    public void forceGameOver () {
        manager.gameOver(new ImpactEvent(this, x[0], y[0], snakeName, bodyParts+1));
    }

    public void setRunning (boolean running) {
        this.running = running;
    }

    public void setTick (int tick) {
        this.tick = tick;
    }


}
