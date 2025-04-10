public class Gantt{

    /* Constructor */
    public Gantt(Process[] procList){
        this.procList = procList;
    }	

    /* Populates gantt array */
    public boolean generate(Algorithm a){

        //reset procTable and ganttarray
        this.totalBursts = calculateBursts(this.procList);
        this.procTable = new int[this.procList.length][PROC_TABLE_FIELDS];
        this.ganttArray = new int[this.totalBursts][this.procList.length];

        //populate
        populateProcTable();
        //apply algorithm
        a.apply(this);
        //average
        average();
        return true;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     * Private stuff
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /* Calculate total number of bursts. Adds all bursts from all 
     * processes, and returns the total. It's used to determine the 
     * number of columns in our gantt chart
     */
    private int calculateBursts(Process[] procs){
        int totalBursts = 0;
        for(int i = 0; i < procs.length; i++){
            totalBursts += procs[i].getBurst();
        }
        return totalBursts;
    }

    /* 
     * This method fills the processes table with info from the list 
     * We use this info to feed the algorithms while creating the 
     * gantt chart
     */
    private boolean populateProcTable(){
        Process[] list = this.procList;
        for (Process proc : list){
            this.procTable[this.ID] = proc.getID();
            this.procTable[this.BURST] = proc.getBurst();
            this.procTable[this.PRIO] = proc.getPriority();
            this.procTable[this.ARRIVAL] = proc.getArrival();
            //the following fields will be calculated after the algorithm has been applied
            this.procTable[this.WAIT] = -1;  //total wait time of this process
            this.procTable[this.RESP] = -1;  //total response time of this process
            this.procTable[this.EXEC] = -1;  //total execution time of this process
        }
        return true;
    }


    /* 
     * This method calculates the relevant averages after the algorithm
     * has been applied. It reads the finished Gantt chart and writes temp
     * info to it, then finally calculates the averages which stores in
     * the private array averages
     *      averages[0] is average wait time 
     *      averages[1] is average response time
     *      averages[2] is average execution time
     */
    private boolean average(){
        //populate missing fields in procTable
        //calculate wait time for each process
        int i,j;
        boolean response = false;   //no process has responded yet
        for(i = 0; i < this.procList.length; i++){  //each process
            for(j = this.procTable[i][this.ARRIVAL]; j < this.totalBursts; j++){  //each instant since the process arrives
                switch (gantarray[i][j]){

                    case WAITING:           //is waiting
                        this.procTable[i][this.WAIT]++;
                        this.procTable[i][this.EXEC]++;
                        if(!response){
                            this.procTable[i][this.RESP]++;
                        }
                        break;

                    case EMPTY:                //it is finished
                        //do nothing!
                        break;

                    case RUNNING:           //is running this instant
                        if(!response){      //if it hadn't responded yet, now it has
                            response = true;
                        }
                        this.procTable[i][this.EXEC]++;
                        break;

                }
            }
            response = false;       //set up for the next process
        }
        //calculate average
        for(i = 0; i < this.procList.length; i++){
            averages[0] += this.procTable[i][this.WAIT];
            averages[1] += this.procTable[i][this.RESP];
            averages[2] += this.procTable[i][this.EXEC];
        }
        //number of processes now in i
        averages[0] /= i;
        averages[1] /= i;
        averages[2] /= i;
    }

    //private attributes
    private int[][] ganttArray;
    private int[][] procTable;
    private Process[] procList; 
    private double[3] averages;
    private int totalBursts;
    //constants for array indexing
    public static final int PROC_TABLE_FIELDS = 7;
    public static final int ID = 0;
    public static final int BURST = 1;
    public static final int PRIO = 2;
    public static final int ARRIVAL = 3;
    public static final int WAIT = 4;
    public static final int RESP = 5;
    public static final int EXEC = 6;
    //constants for ganttarray
    public static final int EMPTY = 0;
    public static final int RUNNING = 1;
    public static final int WAITING = 2;

}


