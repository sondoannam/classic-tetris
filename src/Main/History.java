package Main;

import java.io.*;

public class History {
    private final String filePath;

    public History(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    // Ensure the score file exists
    private void ensureFileExists() {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Score file created at: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error creating score file: " + e.getMessage());
        }
    }

    // Save the current score to the file
    public void saveScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(score + "\n");
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    // Read the file and return the highest score
    public int getHighestScore() {
        int highestScore = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int score = Integer.parseInt(line.trim());
                    if (score > highestScore) {
                        highestScore = score;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid score format in file: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading score file: " + e.getMessage());
        }
        return highestScore;
    }
}
