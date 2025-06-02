package core;

import java.util.ArrayList;
import java.util.LinkedList;

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
     * @return A LinkedList of BCP objects, one for each non-header line.
     */
    public LinkedList<BCP> parse(ArrayList<String> lines) {
        LinkedList<BCP> bcpList = new LinkedList<>();

        // Iterate over the lines, starting from index 1 to skip the header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            // Split the line by commas: "A,0,10,1" -> ["A", "0", "10", "1"]
            String[] columns = line.split(",");

            // Extract the relevant attributes
            int id = i - 1; // Use (index - 1) for unique ID
            
            //   columns[0] = Process Name
            //   columns[1] = arrival time
            //   columns[2] = CPU burst time
            //   columns[3] = priority
            String name = columns[0];
            int arrival = Integer.parseInt(columns[1].trim());
            int burst = Integer.parseInt(columns[2].trim());
            int priority = Integer.parseInt(columns[3].trim());

            // Create a Process object with id and burst
            Process newProcess = new Process(id, burst, name);

            // Create a BCP object for the process
            BCP newBCP = new BCP(newProcess, priority, arrival);

            // Add the BCP object to the linked list
            bcpList.add(newBCP);
        }

        // Return the linked list of BCPs
        return bcpList;
    }

    private Parser(){};

    private static Parser instance;
}

