public class Gantt{

    /* Constructor */
    public Gantt(int totalburst, int numberOfProcesses){
        this.totalBursts = totalburst;
        this.nProcess = numberOfProcesses;
    }	
    

    /* Populates gantt array */
    public Gantt generate(Algorithm a){

        //reset procTable and ganttarray
        this.totalBursts = calculateBursts(this.procList);
        this.procTable = new int[this.procList.length][PROC_TABLE_FIELDS];
        //why are rows in totalBursts and cols are list length?
        //this.ganttArray = new int[this.totalBursts][this.procList.length];
        this.ganttArray = new int[this.procList.length][this.totalBursts];

        //populate
        populateProcTable();
        //apply algorithm
        a.apply(this);
        //average
        average();
        return this;
    }
    /*
     *
     *
     *
     */
    public String toCSV(String algoName){
        StringBuilder sb = new StringBuilder(algoName + '\n');
        //construct header
        sb.append("ID,ARRIVAL,BURST,PRIO,,");
        int i,j;
        for(i = 0; i < this.totalBursts; i++){
            sb.append("t"+i+",");
        }
        sb.append(",WAIT,RESP,EXEC,"+ '\n');
        //for each process
        String id,arrival,burst,prio,wait,resp,exec;
        for(i = 0; i < this.procList.length; i++){
            //write process metadata
            id = String.valueOf(this.procTable[i][this.ID]);
            arrival = String.valueOf(this.procTable[i][this.ARRIVAL]);
            burst = String.valueOf(this.procTable[i][this.BURST]);
            prio = String.valueOf(this.procTable[i][this.PRIO]);
            wait = String.valueOf(this.procTable[i][this.WAIT]);
            resp = String.valueOf(this.procTable[i][this.RESP]);
            exec = String.valueOf(this.procTable[i][this.EXEC]);

            sb.append(id+",");
            sb.append(arrival+",");
            sb.append(burst+",");
            sb.append(prio+",,");
            //for each instant
            for(j = 0; j < this.totalBursts; j++){
                switch (this.ganttArray[i][j]){
                    //if we're waiting, write ==
                    case WAITING:
                        sb.append("#,");
                        break;
                        //if we're running, write ->
                    case RUNNING:
                        sb.append("->,");
                        break;
                        //add a new comma
                    default:
                        sb.append(",");
                        break;
                }
            }
            sb.append(","+wait+",");
            sb.append(resp+",");
            sb.append(exec+",\n");
        }
        //add a newline
        sb.append("\n\n");
        sb.append(",,,,,");
        for(j = 0; j < this.totalBursts; j++){
            sb.append(",");
        }
        sb.append(",");
        String avgWait,avgResp,avgExec;
        avgWait = String.valueOf(this.averages[AVGWAIT]);
        avgResp = String.valueOf(this.averages[AVGRESP]);
        avgExec = String.valueOf(this.averages[AVGEXEC]);
        sb.append(avgWait+","+avgResp+","+avgExec+",\n\n");

        return sb.toString();
    }

    //getters
    public int[][] getGanttArray(){
        return this.ganttArray;
    }

    public int[][] getProcTable(){
        return this.procTable;
    }



    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     * Private stuff
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /* 
     * This method fills the processes table with info from the list 
     * We use this info to feed the algorithms while creating the 
     * gantt chart
     */
    private boolean populateProcTable(){
        Process[] list = this.procList;
        for (Process proc : list){
            int i = proc.getId();
            this.procTable[i][this.ID] = proc.getId();
            this.procTable[i][this.BURST] = proc.getBurst();
            this.procTable[i][this.PRIO] = proc.getPriority();
            this.procTable[i][this.ARRIVAL] = proc.getArrival();
            //the following fields will be calculated after the algorithm has been applied
            this.procTable[i][this.WAIT] = 0;  //total wait time of this process
            this.procTable[i][this.RESP] = 0;  //total response time of this process
            this.procTable[i][this.EXEC] = 0;  //total execution time of this process
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
        this.averages = new double[3];
        int i,j;
        boolean response = false;   //no process has responded yet
        for(i = 0; i < this.procList.length; i++){  //each process
            for(j = this.procTable[i][this.ARRIVAL]; j < this.totalBursts; j++){  //each instant since the process arrives
                switch (ganttArray[i][j]){

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
                            this.procTable[i][this.RESP]++;
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
        return true;
    }

    //private attributes
    private int[][] ganttArray;
    private int[][] procTable;
    private Process[] procList; 
    private double[] averages;
    private int totalBursts;
    private int nProcess;
    //constants for array indexing
    public static final int PROC_TABLE_FIELDS = 7;
    public static final int ID = 0;
    public static final int BURST = 1;
    public static final int PRIO = 2;
    public static final int ARRIVAL = 3;
    public static final int WAIT = 4;
    public static final int RESP = 5;
    public static final int EXEC = 6;
    public static final int AVGWAIT = 0;
    public static final int AVGRESP = 1;
    public static final int AVGEXEC = 2;

    //constants for ganttarray
    public static final int EMPTY = 0;
    public static final int RUNNING = 1;
    public static final int WAITING = 2;

}


