import java.util.ArrayList;
import java.util.LinkedList;

public class Gantt{

    /* Constructor */
    public Gantt(LinkedList<BCP> processList){
        this.entries = new ArrayList<GanttEntry>();
        this.processes = processList;
    }	

    public void addEntry(GanttEntry e){
        this.entries.add(e);
    }

    /*
     *
     *
     */
    public String toCSV(String algorithmName){
        StringBuilder sb = new StringBuilder("");
        double avgWait, avgResp, avgExec;
        avgWait = 0;
        avgResp = 0;
        avgExec = 0;
        //calculate totalBursts
        int totalBursts = entries.stream().mapToInt(GanttEntry::getEndTime).max().orElse(0); 
        //Name of our algorithm
        sb.append(algorithmName+",\n");
        //Column Headers
        sb.append("PROC,ARRIVAL,BURST,PRIO,,");
        //instants
        for(int i = 0; i < totalBursts ; i++){
            sb.append(i+",");
        }
        //Process stats headers
        sb.append(",WAIT,RESP,EXEC,\n");
        //for each process
        for(BCP b : processes){
            sb.append(generateRow(b, totalBursts));
            avgWait += b.getWait();
            avgResp += b.getResp();
            avgExec += b.getExec();
        }
        if(processes.size() != 0){
            avgWait /= processes.size();
            avgResp /= processes.size();
            avgExec /= processes.size();
        }else{
            System.out.println("There are no processes in this list!");
        }
        sb.append(",,,,,");
        //instants
        for(int i = 0; i < totalBursts ; i++){
            sb.append(",");
        }
        sb.append("Averages:,"+avgWait+","+avgResp+","+avgExec+"\n"); 
        return sb.toString();
    }

    //private methods
    private BCP getById(int id){
        for(BCP b : this.processes){
            if(b.getId() == id){
                return b;
            }
        }
        return null;
    }

    private String generateRow(BCP proc, int totalTime){
        //StringBuilder
        StringBuilder sb = new StringBuilder("");
        //append the ID, time of arrival, burst time, the priority and a blank cell
        sb.append(proc.getProcess().getName() + ",").append(proc.getArrival() + ",").append(proc.getBurst() + ",").append(proc.getPriority() + ",,");
        int i;
        for(i = 0; i < totalTime; i++){
            if(i < proc.getArrival()){
                sb.append(",");
            } else if(isRunningAtTime(proc,i)){
                //fill with runningness until expulsion
                sb.append("R,");
            } else if(i < proc.getTerminationTime()){
                //fill with waitingness until execution
                sb.append("W,");
            }else{
                //fill with terminationess until totalbursts
                sb.append("T,");
            }
        }
        //append a blankcell, waitTime, responseTime, executionTime and a newline
        sb.append(","+proc.getWait()+",").append(proc.getResp()+",").append(proc.getExec()+",\n");
        return sb.toString();
    } 

    private boolean isRunningAtTime(BCP proc, int instant){
        for(GanttEntry e : entries){
            if(e.getId() == proc.getId() && instant >= e.getStartTime() && instant <= e.getEndTime()){
                return true;
            }
        }
        return false;
    }
    public ArrayList<GanttEntry> getEntries(){
        return this.entries;
    }

    //private attributes
    private ArrayList<GanttEntry> entries;
    private LinkedList<BCP> processes;
}


