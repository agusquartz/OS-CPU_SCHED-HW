package mm;

import java.util.ArrayList;
import java.util.LinkedList;
import core.BCP;
import core.Process;

public class LogManager {
    private Page[][] matrix;
    private ArrayList<Page[]> ramSnapshots;
    private ArrayList<Boolean> isPageFaults;
    private LinkedList<BCP> BCPList;

    /**
     * Constructor for LogManager.
     *
     * @param BCPList List of BCPs (Process Control Blocks) to manage.
     */
    public LogManager(LinkedList<BCP> BCPList) {
        this.BCPList = BCPList;
        this.ramSnapshots = new ArrayList<>();
        this.isPageFaults = new ArrayList<>();
    }

    /**
     * Adds a new entry to the log with the current RAM snapshot and whether it was a page fault.
     *
     * @param ramSnapshot Array of Page objects representing the current state of RAM.
     * @param isPageFault Boolean indicating if the entry is a page fault.
     */
    public void addEntry(Page[] ramSnapshot, boolean isPageFault) {
        ramSnapshots.add(ramSnapshot);
        isPageFaults.add(isPageFault);
    }

    /**
     * Converts the logged RAM snapshots to a CSV format.
     *
     * @return A string in CSV format representing the RAM snapshots and page faults.
     */
    public String toCSV(String algorithmName) {
        matrix = convertListToMatrix(ramSnapshots);
        Page[][] transposedMatrix = transposePages(matrix);
        StringBuilder sb = new StringBuilder();

        // Add algorithm name
        sb.append(algorithmName).append(",\n");

        int columnas = transposedMatrix[0].length;
        for (int i = 0; i < columnas; i++) {
            sb.append("T").append(i).append(",");
        }
        sb.append("\n");

        for (Page[] fila : transposedMatrix) {
            for (int j = 0; j < fila.length; j++) {
                Page page = fila[j];
                if (page != null) {
                    int pageNumber = page.getPageNumber();
                    int processId = page.getProcessId();
                    String processName = getProcess(processId).getName();
                    sb.append(processName).append(":").append(pageNumber);
                } else {
                    sb.append("null"); // O puedes dejarlo vacío
                }

                if (j < fila.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }

        for (int i = 0; i < isPageFaults.size(); i++) {
            if (isPageFaults.get(i)) {
                sb.append("PF,");
            } else {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    /**
     * Retrieves the process associated with a given process ID.
     *
     * @param processId The ID of the process to retrieve.
     * @return The Process object associated with the given ID, or null if not found.
     */
    private Process getProcess(int processId) {
        for (BCP bcp : BCPList) {
            if (bcp.getId() == processId) {
                return bcp.getProcess();
            }
        }
        return null;
    }

    /**
     * Convierte la lista de snapshots de RAM a un formato matricial.
     * Se asegura de que cada fila tenga siempre tamaño 20. Si una fila
     * original tiene menos de 20 elementos, se rellena con null. Si tiene
     * más de 20, se copian únicamente los primeros 20.
     *
     * @param ramSnapshots List de arreglos de Page que representan los snapshots de RAM.
     * @return Una matriz 2D de Page con filas de longitud fija 20.
     */
    private Page[][] convertListToMatrix(ArrayList<Page[]> ramSnapshots) {
        int rows = ramSnapshots.size();
        if (rows == 0) {
            return null; // matriz vacía
        }

        final int FIXED_COLUMNS = 20;
        Page[][] matrix = new Page[rows][FIXED_COLUMNS];

        for (int i = 0; i < rows; i++) {
            Page[] originalRow = ramSnapshots.get(i);
            Page[] fixedRow = new Page[FIXED_COLUMNS];

            // Copiar hasta 20 elementos o hasta la longitud de la fila original, lo que sea menor
            int lengthToCopy = Math.min(originalRow.length, FIXED_COLUMNS);
            System.arraycopy(originalRow, 0, fixedRow, 0, lengthToCopy);

            // Si originalRow.length < 20, las posiciones restantes ya quedan como null
            matrix[i] = fixedRow;
        }

        return matrix;
    }

    // /**
    //  * Converts the list of RAM snapshots to a matrix format.
    //  *
    //  * @param ramSnapshots List of Page arrays representing the RAM snapshots.
    //  * @return A 2D array (matrix) of Page objects.
    //  */
    // private Page[][] convertListToMatrix(ArrayList<Page[]> ramSnapshots) {
    //     int rows = ramSnapshots.size();
    //     if (rows == 0) {
    //         return null; // empty matrix
    //     }

    //     int columns = ramSnapshots.get(0).length;
    //     Page[][] matrix = new Page[rows][columns];

    //     for (int i = 0; i < rows; i++) {
    //         Page[] row = ramSnapshots.get(i);
    //         if (row.length != columns) {
    //             throw new IllegalArgumentException("All rows must have the same number of columns");
    //         }
    //         matrix[i] = row;
    //     }

    //     return matrix;
    // }

    /**
     * Transposes a 2D array (matrix) of Page objects.
     *
     * @param matrix The 2D array to transpose.
     * @return A new 2D array that is the transposed version of the input matrix.
     */
    private Page[][] transposePages(Page[][] matrix) {
        int rows = matrix.length;
        int colums = matrix[0].length;

        Page[][] transposed = new Page[colums][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colums; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }
}