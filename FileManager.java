import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    /**
     * Reads a CSV file line by line and stores each complete line in an ArrayList.
     *
     * @param path the path to the CSV file.
     * @return an ArrayList containing each line of the file.
     */
    public ArrayList<String> readFile(String path) {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }

    // Main method for testing the functionality
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        ArrayList<String> fileLines = fileManager.readFile("test.csv");

        for (String line : fileLines) {
            System.out.println(line);
        }
    }
}

