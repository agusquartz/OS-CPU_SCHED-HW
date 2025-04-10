import java.util.Arrays;
import java.util.Comparator;

public class Priority implements Algorithm {

    @Override
    public boolean apply(Gantt g){
        int[][] gantArray = g.getGanttArray();
        int[][] procTable = g.getProcTable();
        int amountProcs = procTable.length;

        /*
         * This section creates and sorts an array of process indexes
         * based on their priorities from procTable.
         * 
         * > An Integer array is used instead of int[] because it
         *   needs to be sorted using a custom Comparator, which
         *   requires objects rather than a primitive type.
         * 
         * > The custom Comparator compares the priotities stored
         *   at PRIO index (2) in procTable for each process.
         * 
         * > Finally, we have a sorted array of indexes in ascending order
         *   (from smallest priority to the biggest).
         * 
         */
        Integer[] indexes = new Integer[amountProcs];
        for (int i = 0; i < amountProcs; i++){
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>(){
            @Override
            public int compare(Integer proc1, Integer proc2){
                return Integer.compare(procTable[proc1][Gantt.PRIO], procTable[proc2][Gantt.PRIO]);
            }
        });

        int currentTime = 0;
        // For each process
        for (int i = 0; i < amountProcs; i++){
            int procIndex = indexes[i]; // retrieve the process index to be executed now.
            int arrival = procTable[procIndex][Gantt.ARRIVAL];
            int burst = procTable[procIndex][Gantt.BURST];
            int waiting = currentTime - arrival;
            int tempTime = arrival; // current Time (past) but to set Gantt.WAITING in ganttArray

            // Fill Gantt.WAITING
            for (int j = tempTime; j < (waiting + tempTime); j++){
                gantArray[j][procIndex] = Gantt.WAITING;
            }

            // Fill Gantt.RUNNING (currentTime++)
            for (int j = currentTime; j < (burst + currentTime); j++){
                ganttArray[j][procIndex] = Gantt.RUNNING;
                currentTime++;
            }

            return true;
        }

    }
}