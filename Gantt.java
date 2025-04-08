public class Gantt{

    /* Constructor */
	public Gantt(Process[] procList){
		this.procList = procList;
		//init ganttArray
        this.ganttArray = new int[calculateBursts(this.procList)][this.procList.length];
	}	

    /* Populates gantt array */
    public boolean generate(Algorithm a){
        //reset procTable and ganttarray
        this.procTable = new int[this.procList.length][PROC_TABLE_FIELDS];
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
        return false;
    }

	//private attributes
	private int[][] ganttArray;
	private int[][] procTable;
	private Process[] procList; 
    private int[3] averages;
    //constants for array indexing
    private final int PROC_TABLE_FIELDS = 7;
    private final int ID = 0;
    private final int BURST = 1;
    private final int PRIO = 2;
    private final int ARRIVAL = 3;
    private final int WAIT = 4;
    private final int RESP = 5;
    private final int EXEC = 6;
 
}
			

