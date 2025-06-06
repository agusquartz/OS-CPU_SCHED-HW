import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

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

    public void appendToFile(String text) {
        String filePath = "gantt.csv";

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(text);
            writer.write(System.lineSeparator()); // Agrega un salto de línea después del texto
            System.out.println("Texto agregado exitosamente al archivo.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public void checkOutput() {
        File file = new File("gantt.csv");

        try {
            if (file.exists()) {
                // Si el archivo existe, lo vacía
                FileWriter writer = new FileWriter(file);
                writer.write(""); // Escribe una cadena vacía para borrar el contenido
                writer.close();
                System.out.println("El archivo ya existía, pero ha sido vaciado.");
            } else {
                // Si no existe, crea uno nuevo
                if (file.createNewFile()) {
                    System.out.println("El archivo no existía, se ha creado uno nuevo.");
                } else {
                    System.err.println("No se pudo crear el archivo.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al manejar el archivo: " + e.getMessage());
        }
    }



    private FileManager(){};

    private static FileManager instance;
}

