import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Chess extends JFrame {

    public static void main(String[] args) throws IOException {
        new Chess();
    }

    public Chess () throws IOException {
        setTitle("Chess");
        setSize(520, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        setAlwaysOnTop(true);
        setLocation(10,0);

        Board b = new Board();
        b.setupBoard();

        System.out.println("\nAVAILABLE COMMANDS: restart, draw, save, load");

        Scanner scanner = new Scanner(System.in);
        boolean isWhiteTurn = true;
        label:
        while (true){

            if (isWhiteTurn) {
                System.out.print("White turn: ");
            } else {
                System.out.print("Black turn: ");
            }

            String scan = scanner.nextLine();

            if (scan.length() == 5) {

                int startX = scan.charAt(0) - 'a' +1; // Letter
                int startY = scan.charAt(1) - 48; // Number

                int endX = scan.charAt(3) - 'a' +1; // Letter
                int endY = scan.charAt(4) - 48; // Number

                if (Board.gameboard[startX][startY] instanceof Piece pieceToMove && startX >= 1 && startX <= 8 && startY >= 1 && startY <= 8 && endX >= 1 && endX <= 8 && endY >= 1 && endY <= 8 && pieceToMove.isWhite() == isWhiteTurn ) { // && pieceToMove.canMove(startX, startY, endX, endY)

                    if (pieceToMove.move(startX, startY, endX, endY) ) {

                        if (Board.isInCheck(Board.gameboard, true)){
                            System.out.println("WHITE IN CHECK!");

//                        if (Board.isCheckmate(Board.gameboard, true)) System.out.println("White checkmate!");
                        }
                        else if (Board.isInCheck(Board.gameboard, false)){
                            System.out.println("BLACK IN CHECK!");

//                        if (Board.isCheckmate(Board.gameboard, false)) System.out.println("Black checkmate!");
                        }

                        isWhiteTurn = !isWhiteTurn;
                    }
                    else System.out.println("Can't move there! Try again");
                }
                else System.out.println("Incorrect move! Try again");
            }

            switch (scan) {
                case "restart" -> b.setupBoard();

                case "draw" -> {
                    System.out.print("player 1 agrees? (y/n): ");
                    String scan_player1 = scanner.nextLine();
                    System.out.print("player 2 agrees? (y/n): ");
                    String scan_player2 = scanner.nextLine();
                    if (scan_player1.equals("y") && scan_player2.equals("y")) {
                        System.out.println("Game Draw!");
                        System.exit(0);
                        break label;
                    }
                }

                case "load" -> {
                    System.out.print("Enter filename(.bin) to read from: ");
                    String inputFileName = scanner.nextLine();
                    b.readState(inputFileName);
                }

                case "save" -> {
                    System.out.print("Enter filename(.bin) to save to: ");
                    String inputFileName = scanner.nextLine();
                    b.writeState(inputFileName);
                }
                default -> {
                }
            }

            repaint();

        }


    }

    // ========================================== Board class =============================================== //
    public class Board {
        public static Object[][] gameboard;

        public Board() {
            gameboard = new Object[9][9];
        }

        public static boolean occupied (int x, int y){
            if (Board.gameboard[x][y] == null) return false; // wolny
            else return true; // zajęty
        }
        public static boolean checkIfWhite(int x, int y){
            Object obj = gameboard[x][y];
            if (obj instanceof Piece && ((Piece) obj).isWhite()) return true;
            else return false;
        }

        public static void kill (int x, int y) {
//            ArrayList<Piece> captured = (ArrayList<Piece>) gameboard[0][0]; // KURWA CAST EXCEPTION przy biciu
//            captured.add((Piece) gameboard[x][y]);
            gameboard[x][y] = null;

//        for (int i = 0; i < captured.size(); i++) {
//            System.out.println(captured.get(i).toString());
//        }
        }



        private static Point findKingPos (Object[][] gameboard, boolean isWhite) {

            Point position = new Point();

            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    Piece piece = ((Piece) gameboard[i][j]);
                    if (piece != null && piece.getType() == 1 && piece.isWhite() == isWhite){
                        position = new Point(i, j);

                    }
                }
            }
            return position;
        }

        private static List<Point> getThreatPos (Object[][] gameboard, boolean isWhite, Point findKingPos) {

            List<Point> threatPos = new ArrayList<>();

            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    Piece piece = ((Piece) gameboard[i][j]);
                    if (piece != null && piece.isWhite() == isWhite) {
                        if (piece.canMove(i, j, findKingPos.x, findKingPos.y) ) {
                            threatPos.add(new Point(findKingPos.x,findKingPos.y));
                        }
                    }
                }
            }
            return threatPos;
        }

        public static boolean isInCheck (Object[][] gameboard, boolean isWhite) {
            Point kingPos = findKingPos(gameboard, isWhite);
            List<Point> threatPos = getThreatPos(gameboard, !isWhite, kingPos);
            gameboard[kingPos.x][kingPos.y] = new King(isWhite); // obejście buga ze znikaniem króla przy szachu na około..
            return threatPos.contains(kingPos);
        }
        public static boolean moveIsInCheck (Object[][] gameboard, boolean isWhite, int x, int y) {
            Point newKingPos = new Point(x, y);
            List<Point> threatPos = getThreatPos(gameboard, !isWhite, newKingPos);
            return threatPos.contains(newKingPos);
        }

//    public static boolean isCheckmate(Object[][] gameboard, boolean isWhite) {
//        if (isInCheck(gameboard, isWhite)) {
//
//            List<Point> legalMoves = new ArrayList<>();
//
//            for (int i = 1; i < gameboard.length; i++) {
//                for (int j = 1; j < gameboard[i].length; j++) {
//                    Piece piece = ((Piece) gameboard[i][j]);
//                    if (piece != null && piece.isWhite() == isWhite) {
//
//                        for (int k = 1; k < 9; k++) {
//                            for (int l = 1; l < 9; l++) {
//                                if (piece.canMove(i, j, k, l) ) {
//                                    legalMoves.add(new Point(k,l));
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//
//            for (Point move : legalMoves) {
//                Object[][] gameboardCopy = gameboard.clone();
//                for (int i = 1; i < 9; i++) {
//                    for (int j = 1; j < 9; j++) {
//                        Piece piece = ((Piece) gameboardCopy[i][j]);
//                        if (piece != null && piece.isWhite() == isWhite) {
//                            ((Piece) gameboardCopy[i][j]).move(i, j, move.x, move.y);
//                            if (!isInCheck(gameboardCopy, isWhite)) return false;
//                        }
//                    }
//                }
//            }
//            return true;
//
//        } else return false; // jeśli nie ma szachu, to nie ma szach-matu
//    }


        public void setupBoard() {
            for (Object[] row : gameboard) { // opróżnienie aktualnej tablicy gry
                Arrays.fill(row, null);
            }
            gameboard[0][0] = new ArrayList<Piece>();

            // BIAŁE
            gameboard[1][8] = new Rook(true);
            gameboard[2][8] = new Knight(true);
            gameboard[3][8] = new Bishop(true);
            gameboard[4][8] = new Queen(true);
            gameboard[5][8] = new King(true);
            gameboard[6][8] = new Bishop(true);
            gameboard[7][8] = new Knight(true);
            gameboard[8][8] = new Rook(true);
            for (int i = 1; i < 9; i++) {
                gameboard[i][7] = new Pawn(true);
            }

            // CZARNE
            for (int i = 1; i < 9; i++) {
                gameboard[i][2] = new Pawn(false);
            }
            gameboard[1][1] = new Rook(false);
            gameboard[2][1] = new Knight(false);
            gameboard[3][1] = new Bishop(false);
            gameboard[4][1] = new Queen(false);
            gameboard[5][1] = new King(false);
            gameboard[6][1] = new Bishop(false);
            gameboard[7][1] = new Knight(false);
            gameboard[8][1] = new Rook(false);
        }

        public void writeState(String fileName) throws IOException {
            FileOutputStream fos = new FileOutputStream(fileName);
            DataOutputStream dos = new DataOutputStream(fos);

            //każda figurę w pliku dopełniamy zerami do 2B pełnych, 16 bitów

            for (int i = 1; i < gameboard.length; i++) {
                for (int j = 1; j < gameboard[i].length; j++) {

                    if (gameboard[i][j] != null){

                        int type = ((Piece) gameboard[i][j]).getType();

                        boolean isWhite = ((Piece) gameboard[i][j]).isWhite();
                        int color;
                        if (isWhite) color = 1;
                        else color = 0;

                        byte combined = (byte) (type | (i << 3) | (j << 7));
                        byte combined2 = (byte) ((j >> 1) | (color << 3));

                        dos.writeByte((combined2) & 0xFF);
                        dos.writeByte((combined) & 0xFF);
                    }
                }
            }
            dos.close();
            fos.close();
        }

        public void readState (String fileName) throws IOException{
            FileInputStream fis = new FileInputStream(fileName);
            DataInputStream dis = new DataInputStream(fis);

            for (Object[] row : gameboard) { // opróżnienie aktualnej tablicy gry, żeby wczytać grę z pliku
                Arrays.fill(row, null);
            }
            gameboard[0][0] = new ArrayList<Piece>();

            while (dis.available() != 0) {
                short read = dis.readShort();
                int color = (read >> 11);
                int y = (read >> 7) & 0xF;
                int x = (read >> 3) & 0xF;
                int type = read & 0x7;

                boolean isWhite;
                if (color == 1) isWhite = true;
                else isWhite = false;

                switch (type) {
                    case 0 -> gameboard[x][y] = new Pawn(isWhite);
                    case 1 -> gameboard[x][y] = new King(isWhite);
                    case 2 -> gameboard[x][y] = new Queen(isWhite);
                    case 3 -> gameboard[x][y] = new Rook(isWhite);
                    case 4 -> gameboard[x][y] = new Bishop(isWhite);
                    case 5 -> gameboard[x][y] = new Knight(isWhite);
                }
            }
            System.out.println("Game State loaded from file");
            dis.close();
            fis.close();
        }
    }

    // ========================================== Piece Interface =============================================== //
    public interface Piece {
        boolean canMove(int startX, int startY, int endX, int endY);
        boolean move(int startX, int startY, int endX, int endY);
        boolean isWhite();
        int getType();
        boolean hasMoved ();
    }

    // ========================================== PAWN =============================================== //
    public class Pawn implements Piece {
        public int type = 0;
        public String iconB = "♟";
        public String iconW = "♙";

        private boolean isWhite;

        public Pawn(boolean isWhite) {
            this.isWhite = isWhite;
        }

        public boolean isWhite() {
            return isWhite;
        }
        public int getType(){
            return type;
        }

        @Override
        public boolean hasMoved () {
            return false;
        }

        @Override
        public boolean canMove(int startX, int startY, int endX, int endY) {

            if (startX == endX) {
                if (this.isWhite) { // BIAŁE
                    if (startY == 7 && endY == 5 && !Board.occupied(startX, 6) && !Board.occupied(startX, 5)) {
                        return true;
                    }
                    else if (startY == endY+1 && !Board.occupied(endX, endY)) {
                        return true;
                    }

                } else { // CZARNE
                    if (startY == 2 && endY == 4 && !Board.occupied(startX, 3) && !Board.occupied(startX, 4) ) {
                        return true;
                    }
                    else if (startY == endY-1 && !Board.occupied(endX, endY) ) {
                        return true;
                    }
                }
            }

            else if (Math.abs(startX - endX) == 1) {
                if (this.isWhite) { // BIAŁE BICIE
                    if (startY == endY + 1 && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
                else { // CZARNE BICIE
                    if (startY == endY - 1 && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            }

            return false;

        }

        public void transorm (boolean isWhite, int x, int y) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Choose number to transform your Pawn into (2-Queen, 3-Rook, 4-Bishop, 5-Knight): ");
            int choose = scanner.nextInt();

            switch (choose) {
                case 2 -> Board.gameboard[x][y] = new Queen(isWhite);
                case 3 -> Board.gameboard[x][y] = new Rook(isWhite);
                case 4 -> Board.gameboard[x][y] = new Bishop(isWhite);
                case 5 -> Board.gameboard[x][y] = new Knight(isWhite);
            }

        }
        @Override
        public boolean move(int startX, int startY, int endX, int endY) {
            if (canMove(startX, startY, endX, endY)){
                Board.gameboard[endX][endY] = this;
                Board.gameboard[startX][startY] = null;


                if (isWhite && endY == 1) {
                    transorm(true, endX, endY);
                }
                else if (!isWhite && endY == 8) {
                    transorm(false, endX, endY);
                }

                return true;
            }

            return false;
        }

        @Override
        public String toString () {
            if (isWhite) return iconW;
            else return iconB;
        }
    }
    // ========================================== KING =============================================== //
    public static class King implements Piece {
        public int type = 1;
        public String iconB = "♚";
        public String iconW = "♔";

        private boolean isWhite;
        private boolean hasMoved; // potrzebne do roszady

        public King(boolean isWhite) {
            this.isWhite = isWhite;
            this.hasMoved = false;
        }

        public boolean isWhite() {
            return isWhite;
        }
        public int getType(){
            return type;
        }
        public boolean hasMoved() {
            return hasMoved;
        }

        @Override
        public boolean canMove(int startX, int startY, int endX, int endY) {
            int row = Math.abs(startX - endX);
            int col = Math.abs(startY - endY);
            if (row <= 1 && col <= 1 ) {
                if (!Board.moveIsInCheck(Board.gameboard, isWhite, endX, endY) ) {

                    if (!Board.occupied(endX, endY)) return true;
                        // BICIE
                    else if (Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
//            else if (Board.isCheckmate(isWhite)) {
//                System.out.println("CHECKMATE!");
//                return false;
//            }
                else {
                    System.out.println("Can't move there - check");
                    return false;
                }
            }
            else if (castle(startX, startY, endX, endY)) return true;
            return false;
        }

        public boolean castle(int startX, int startY, int endX, int endY) {

//        Piece test = (Piece) Board.gameboard[endX][endY];


//        if (test.getType() == 3){
            if (isWhite && !hasMoved) {
                if (startX == 5 && startY == 8 && endX == 8 && endY == 8 && Board.gameboard[6][8] == null && Board.gameboard[7][8] == null) {

                    Board.gameboard[7][8] = this;
                    Board.gameboard[6][8] = Board.gameboard[endX][endY];
                    Board.gameboard[endX][endY] = null;
                    Board.gameboard[startX][startY] = null;
                    return true;

                }
                else if (startX == 5 && startY == 8 && endX == 8 && endY == 8 && Board.gameboard[2][8] == null && Board.gameboard[3][8] == null && Board.gameboard[4][8] == null) {
                    Board.gameboard[3][8] = this;
                    Board.gameboard[4][8] = Board.gameboard[endX][endY];
                    Board.gameboard[endX][endY] = null;
                    Board.gameboard[startX][startY] = null;
                    return true;
                }
            }
            else if (!hasMoved){
                if (startX == 5 && startY == 1 && endX == 1 && endY == 1 && Board.gameboard[6][1] == null && Board.gameboard[7][1] == null) {

                    Board.gameboard[7][1] = this;
                    Board.gameboard[6][1] = Board.gameboard[endX][endY];
                    Board.gameboard[endX][endY] = null;
                    Board.gameboard[startX][startY] = null;
                    return true;

                }
                else if (startX == 5 && startY == 1 && endX == 1 && endY == 1 && Board.gameboard[2][1] == null && Board.gameboard[3][1] == null && Board.gameboard[4][1] == null) {

                    Board.gameboard[3][1] = this;
                    Board.gameboard[4][1] = Board.gameboard[endX][endY];
                    Board.gameboard[endX][endY] = null;
                    Board.gameboard[startX][startY] = null;
                    return true;

                }
            }
//        }
            return false;
        }

        @Override
        public boolean move(int startX, int startY, int endX, int endY) {
            if (canMove(startX, startY, endX, endY)){
                Board.gameboard[endX][endY] = this;
                Board.gameboard[startX][startY] = null;
                this.hasMoved = true;
                return true;
            }
            return false;
        }

        @Override
        public String toString () {
            if (isWhite) return iconW;
            else return iconB;
        }
    }

    // ========================================== QUEEN =============================================== //
    public class Queen implements Piece {
        public int type = 2;
        public String iconB = "♛";
        public String iconW = "♕";

        private boolean isWhite;

        public Queen(boolean isWhite) {
            this.isWhite = isWhite;

        }

        public boolean isWhite() {
            return isWhite;
        }
        public int getType(){
            return type;
        }

        @Override
        public boolean hasMoved () {
            return false;
        }

        @Override
        public boolean canMove(int startX, int startY, int endX, int endY) {

            if (startX == endX) { // PIONOWO

                if (startY - endY < 0) { // GÓRA

                    boolean onWay = false;
                    for (int i = startY + 1; i < endY; i++) { // nie sprawdza końcowego
                        if (Board.occupied(endX, i)) {
                            onWay = true;
                            break;
                        }
                    }

                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else { // DÓŁ

                    boolean onWay = false;

                    for (int i = startY - 1; i > endY; i--) {
                        if (Board.occupied(endX, i)) {
                            onWay = true;
                            break;
                        }
                    }

                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            } else if (startY == endY) { // POZIOMO

                if (startX - endX < 0) { // PRAWO

                    boolean onWay = false;

                    for (int i = startX + 1; i < endX; i++) {
                        if (Board.occupied(i, endY)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else { // LEWO

                    boolean onWay = false;

                    for (int i = startX - 1; i > endX; i--) {
                        if (Board.occupied(i, endY)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            } else if (Math.abs(startX - endX) == Math.abs(startY - endY)) { // PO PRZEKĄTNEJ

                if (startX - endX < 0 && startY - endY < 0) { // PRAWO DÓŁ
                    boolean onWay = false;
                    for (int i = startX +1, j = startY +1; i < endX && j < endY; i++, j++) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else if (startX - endX < 0 && startY - endY > 0) { // PRAWO GÓRA
                    boolean onWay = false;
                    for (int i = startX +1, j = startY -1; i < endX && j > endY; i++, j--) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else if (startX - endX > 0 && startY - endY < 0) { // LEWO DÓŁ
                    boolean onWay = false;
                    for (int i = startX -1, j = startY +1; i > endX && j < endY; i--, j++) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else if (startX - endX > 0 && startY - endY > 0) { // LEWO GÓRA
                    boolean onWay = false;
                    for (int i = startX -1, j = startY -1; i > endX && j > endY; i--, j--) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            }
            return false;
        }


        @Override
        public boolean move(int startX, int startY, int endX, int endY) {
            if (canMove(startX, startY, endX, endY)){
                Board.gameboard[endX][endY] = this;
                Board.gameboard[startX][startY] = null;
                return true;
            }
            return false;
        }

        @Override
        public String toString () {
            if (isWhite) return iconW;
            else return iconB;
        }
    }

    // ========================================== ROOK =============================================== //
    public class Rook implements Piece {
        public int type = 3;
        public String iconB = "♜";
        public String iconW = "♖";

        private boolean isWhite;
        private boolean hasMoved; // potrzebne do roszady

        public Rook(boolean isWhite) {
            this.isWhite = isWhite;
            this.hasMoved = false;
        }

        public boolean isWhite() {
            return isWhite;
        }
        public int getType(){
            return type;
        }
        public boolean hasMoved() {
            return hasMoved;
        }

        @Override
        public boolean canMove(int startX, int startY, int endX, int endY) {

            if (startX == endX) { // PIONOWO

                if (startY - endY < 0){

                    boolean onWay = false;
                    for (int i = startY+1; i < endY; i++) { // nie sprawdza końcowego
                        if (Board.occupied(endX, i)) {
                            onWay = true;
                            break;
                        }
                    }

                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else {
                    boolean onWay = false;

                    for (int i = startY-1; i > endY; i--) {
                        if (Board.occupied(endX, i)) {
                            onWay = true;
                            break;
                        }
                    }

                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            }

            else if (startY == endY){ // POZIOMO

                if (startX - endX < 0){

                    boolean onWay = false;

                    for (int i = startX+1; i < endX; i++) {
                        if (Board.occupied(i, endY)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else {

                    boolean onWay = false;

                    for (int i = startX-1; i > endX; i--) {
                        if (Board.occupied(i, endY)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean move(int startX, int startY, int endX, int endY) {
            if (canMove(startX, startY, endX, endY)){
                Board.gameboard[endX][endY] = this;
                Board.gameboard[startX][startY] = null;
                this.hasMoved = true;
                return true;
            }
            return false;
        }

        @Override
        public String toString () {
            if (isWhite) return iconW;
            else return iconB;
        }
    }

    // ========================================== BISHOP =============================================== //
    public class Bishop implements Piece {
        public int type = 4;
        public String iconB = "♝";
        public String iconW = "♗";

        private boolean isWhite;

        public Bishop(boolean isWhite) {
            this.isWhite = isWhite;

        }

        public boolean isWhite() {
            return isWhite;
        }
        public int getType(){
            return type;
        }

        @Override
        public boolean hasMoved () {
            return false;
        }

        @Override
        public boolean canMove(int startX, int startY, int endX, int endY) {
            if (Math.abs(startX - endX) == Math.abs(startY - endY)) { // PO PRZEKĄTNEJ

                if (startX - endX < 0 && startY - endY < 0) { // PRAWO DÓŁ
                    boolean onWay = false;
                    for (int i = startX + 1, j = startY + 1; i < endX && j < endY; i++, j++) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else if (startX - endX < 0 && startY - endY > 0) { // PRAWO GÓRA
                    boolean onWay = false;
                    for (int i = startX + 1, j = startY - 1; i < endX && j > endY; i++, j--) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else if (startX - endX > 0 && startY - endY < 0) { // LEWO DÓŁ
                    boolean onWay = false;
                    for (int i = startX - 1, j = startY + 1; i > endX && j < endY; i--, j++) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }

                } else if (startX - endX > 0 && startY - endY > 0) { // LEWO GÓRA
                    boolean onWay = false;
                    for (int i = startX - 1, j = startY - 1; i > endX && j > endY; i--, j--) {
                        if (Board.occupied(i, j)) {
                            onWay = true;
                            break;
                        }
                    }
                    if (!onWay && !Board.occupied(endX, endY)) return true;

                    else if (!onWay && Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                        Board.kill(endX, endY);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean move(int startX, int startY, int endX, int endY) {
            if (canMove(startX, startY, endX, endY)){
                Board.gameboard[endX][endY] = this;
                Board.gameboard[startX][startY] = null;
                return true;
            }
            return false;
        }

        @Override
        public String toString () {
            if (isWhite) return iconW;
            else return iconB;
        }
    }

    // ========================================== KNIGHT =============================================== //
    public class Knight implements Piece {
        public int type = 5;
        public String iconB = "♞";
        public String iconW = "♘";

        private boolean isWhite;

        public Knight(boolean isWhite) {
            this.isWhite = isWhite;

        }

        public boolean isWhite() {
            return isWhite;
        }
        public int getType(){
            return type;
        }

        @Override
        public boolean hasMoved () {
            return false;
        }

        @Override
        public boolean canMove(int startX, int startY, int endX, int endY) {
            int row = Math.abs(startX - endX);
            int col = Math.abs(startY - endY);

            if (row == 2 && col == 1) {
                if (!Board.occupied(endX, endY)) return true;
                    // BICIE
                else if (Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                    Board.kill(endX, endY);
                    return true;
                }
            }

            else if (row == 1 && col == 2) {
                if (!Board.occupied(endX, endY)) return true;
                    // BICIE
                else if (Board.occupied(endX, endY) && this.isWhite() != Board.checkIfWhite(endX, endY)) {
                    Board.kill(endX, endY);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean move(int startX, int startY, int endX, int endY) {
            if (canMove(startX, startY, endX, endY)){
                Board.gameboard[endX][endY] = this;
                Board.gameboard[startX][startY] = null;
                return true;
            }
            return false;
        }

        @Override
        public String toString () {
            if (isWhite) return iconW;
            else return iconB;
        }
    }


// =================================================== Window Interface ================================================ //
    public void paint(Graphics g) {
        super.paint(g);

        // mniejszy bok okna
        int smallerDimension = Math.min(getWidth(), (getHeight()+ insets().top));
        int size = smallerDimension/9;

        int x = 0;
        int y = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(new Color(224, 200, 174)); // biały
                } else {
                    g.setColor(new Color(174, 120, 97)); // czarny
                }

                g.fillRect(x + insets().left, y +insets().top, size, size);
                x += size;

                g.setColor(Color.BLACK);
                // literki
                char letter = (char) ('A' + i);
                String label_char = Character.toString(letter);
                g.drawString(label_char, i * size + size / 2 , 8 * size + size / 2 + size/3);

                // cyferki
                int number = i + 1;
                String label_num = Integer.toString(number);
                g.drawString(label_num, 8 * size + size / 2 -size/3, i * size + size / 2 +size/2);
            }
            x = 0;
            y += size;
        }


        // WYPISYWANIE FIGUR
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f+size/2);
        g.setFont(newFont);

        for (int i = 1; i < Board.gameboard.length; i++) {
            for (int j = 1; j < Board.gameboard[i].length; j++) {
                Object drawPiece = Board.gameboard[i][j];
                if (drawPiece != null) {

                    // Pobieramy pozycję figury na planszy na podstawie jej indeksu w tablicy
                    int x_pos = i * size -size +size/4;
                    int y_pos = j * size +size/3;

                    g.drawString(drawPiece.toString(), x_pos, y_pos);
                }
            }
        }
    }

}