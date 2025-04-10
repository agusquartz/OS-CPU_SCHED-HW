import java.util.Arrays;
import java.util.Comparator;

public class FCFS implements Algorithm {

    @Override
    public boolean apply(Gantt g){
        int[][] ganttArray = g.getGanttArray();
        int[][] procTable = g.getProcTable();
        int amountProcs = procTable.length;

        /*
         * This section creates and sorts an array of process indexes
         * based on their arrival time from procTable.
         * 
         * > An Integer array is used instead of int[] because it
         *   needs to be sorted using a custom Comparator, which
         *   requires objects rather than a primitive type.
         * 
         * > The custom Comparator compares the arrival times stored
         *   at ARRIVAL index (3) in procTable for each process.
         * 
         * > Finally, we have a sorted array of indexes in ascending order
         *   (from earlier arrival time to the latest).
         * 
         */
        Integer[] indexes = new Integer[amountProcs];
        for (int i = 0; i < amountProcs; i++){
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>(){
            @Override
            public int compare(Integer proc1, Integer proc2){
                return Integer.compare(procTable[proc1][Gantt.ARRIVAL], procTable[proc2][Gantt.ARRIVAL]);
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
            int finish;

            // Fill Gantt.WAITING
            finish = (waiting + tempTime);
            for (int j = tempTime; j < finish; j++){
                ganttArray[procIndex][j] = Gantt.WAITING;
            }

            // Fill Gantt.RUNNING (currentTime++)
            finish = (burst + currentTime);
            for (int j = currentTime; j < finish; j++){
                ganttArray[procIndex][j] = Gantt.RUNNING;
                currentTime++;
            }
        }

            return true;

    }
}
