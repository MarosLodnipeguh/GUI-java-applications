package Logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scoreboard {

    private static final int MAX_SCORES = 10;
    private static final String FILENAME = "src/Logic/scores.bin";


    public static void addScore(String playerName, int score) {
        List<ScoreboardEntry> scores = readScores();
        scores.add(new ScoreboardEntry(playerName, score));
        Collections.sort(scores);
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES); // returns only the 10 greatest scores
        }
        writeScores(scores);
    }

    public static List<ScoreboardEntry> displayScores() {
        List<ScoreboardEntry> scores = readScores();
        return scores;

//        for (int i = 0; i < scores.size(); i++) {
//            ScoreEntry entry = scores.get(i);
//            System.out.println((i + 1) + ". " + entry.getPlayerName() + " - " + entry.getScore());
//        }
    }

    private static void writeScores (List<ScoreboardEntry> scores) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(FILENAME)) {
            for (ScoreboardEntry entry : scores) {

                int LEN;

                if (entry.getPlayerName() != null) {
                    LEN = entry.getPlayerName().length();

                    fileOutputStream.write((byte) LEN);
                    fileOutputStream.write(entry.getPlayerName().getBytes());
                }
                else {
                    String nullName = "Unknown";
                    LEN = nullName.length();

                    fileOutputStream.write((byte) LEN);
                    fileOutputStream.write(nullName.getBytes());
                }

                // 4 Bytes for points count
                fileOutputStream.write((entry.getScore() >> 24) & 0xFF);
                fileOutputStream.write((entry.getScore() >> 16) & 0xFF);
                fileOutputStream.write((entry.getScore() >> 8) & 0xFF);
                fileOutputStream.write(entry.getScore() & 0xFF);
            }

        } catch (IOException e) {
            System.out.println("Error writing scores to File: " + e.getMessage());
        }

    }

    private static List<ScoreboardEntry> readScores () {

        List<ScoreboardEntry> scores = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(FILENAME)) {

            while (fileInputStream.available() > 0) {

                int LEN = fileInputStream.read();

                byte[] playerNameBytes = new byte[LEN];
                fileInputStream.read(playerNameBytes); // reads as many bytes as the player's name
                String playerName = new String(playerNameBytes); // new String from the bytes array

                // 4 Bytes for points count
                int points = 0;
                points |= (fileInputStream.read() & 0xFF) << 24;
                points |= (fileInputStream.read() & 0xFF) << 16;
                points |= (fileInputStream.read() & 0xFF) << 8;
                points |= fileInputStream.read() & 0xFF;

                scores.add(new ScoreboardEntry(playerName, points));
            }

        } catch (IOException e) {
            System.out.println("Error reading scores from File: " + e.getMessage());
        }

        return scores;

    }


    public static class ScoreboardEntry implements Comparable<ScoreboardEntry> {
        private String playerName;
        private int score;

        public ScoreboardEntry (String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(ScoreboardEntry other) {
            return Integer.compare(other.score, this.score); // Sort in descending order
        }
    }




}
