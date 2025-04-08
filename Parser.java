import java.util.ArrayList;

public class Parser {

    public static Parser getInstance() {
        if (instance == null) {
            instance = new Parser();
        }
        return instance;
    }

    /**
     * Converts lines of text (excluding the header) into Process objects.
     *
     * Assumes that the CSV columns are in the following order:
     * Process, Arrival, Burst, Priority
     *
     * @param lines an ArrayList<String> containing all lines of the CSV file,
     *              with the first element being the header.
     * @return An array of Process objects, one for each non-header line.
     */
    public Process[] parse(ArrayList<String> lines) {

        int procArraySize = lines.size() - 1;
        Process[] processes = new Process[procArraySize];

        // Iterate over the lines, starting from index 1 to skip the header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            // Split the line by commas: "A,0,5,1" -> ["A", "0", "5", "1"]
            String[] columns = line.split(",");

            //   columns[1] = arrival time
            //   columns[2] = CPU burst time
            //   columns[3] = priority
            int arrival = Integer.parseInt(columns[1].trim());
            int burst = Integer.parseInt(columns[2].trim());
            int priority = Integer.parseInt(columns[3].trim());

            // For the ID, we use the (index - 1) so each process gets a unique ID.
            // Ignoring completely the char read from the file.
            int id = i - 1;

            Process newProcess = new Process(id, burst, priority, arrival);

            // Store the process object in the array (using i-1 because the first line is a header)
            processes[i - 1] = newProcess;
        }

        return processes;
    }

    private Parser(){};

    private static Parser instance;
}

