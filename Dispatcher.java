import java.util.LinkedList;
public class Dispatcher {
    /* Constructor */
    public Dispatcher(LinkedList<BCP> bcpList, Gantt chart){
        this.processList = bcpList;
        this.chart = chart;
    }
    /* Run well... 'runs' the algorithm and fills the Gantt chart, then returns the String form of this chart
     */
    public boolean run(Algorithm a){
        //simulates time
        //set up the loop
        int currentTime = 0;
        BCP executing = a.apply(this.processList, currentTime);    //get first process to run

        if (executing == null) {
            System.out.println("Dispatcher: el algoritmo no devolvió ningún proceso para ejecutar.");
            return false;
        }

        executing.setState(State.RUNNING);  //this process starts RUNNING in the first instant
        executing.setFirstTime(currentTime);    //store in the BCP when this process started running
        GanttEntry e = new GanttEntry(executing.getId(), currentTime);  //create a GanttEntry for this execution burst
        while(!allProcessesTerminated(this.processList)){
            //at each time run the algorithm
            BCP newExecuting = a.apply(this.processList,currentTime);

            // Verification of next BCP
            if(newExecuting != executing) {
                
                if(executing.getState() == State.RUNNING) {
                    executing.setState(State.READY);
                }
            }
            executing = newExecuting;

            //has the running process changed?
            if((executing.getState() == State.READY) || (executing.getState() == State.WAITING)){
                //yes then close current GanttEntry
                e.setEndTime(currentTime);
                this.chart.addEntry(e);
                //create a new GanttEntry with current executing processId
                e = new GanttEntry(executing.getId(), currentTime);
            }
            //save curTime
            int timeBeforeUpdate = currentTime;
            //increment currentTime
            currentTime++;
            //update the executing process bcp
            updateBCP(executing, timeBeforeUpdate);
        }
        //close last GanttEntry
        e.setEndTime(currentTime);
        this.chart.addEntry(e);
        //all processes have terminated
        //return the GanttChar in csv form
        return true;
    }

    //private methods
    private void updateBCP(BCP b, int instant){
        switch (b.getState()){
            case READY:   //if the process just started
                b.setState(State.RUNNING);   //change its state to RUNNING
                b.setFirstTime(instant);     //store its startTime in the bcp
                break;
            case RUNNING: //if the process was already executing
            case WAITING: //if the process is comming back from waiting
                b.setState(State.RUNNING);   //change its state back to RUNNING
                break;
            default:
                //shouldnt get here.
                System.out.println("There has been a serious mistake");
                break;
        }
        if(b.getState() == State.RUNNING && b.getRemainingTime() > 0){
            //anyways decrease remainingTime
            b.setRemainingTime(b.getRemainingTime() - 1);
        }
        if(b.getRemainingTime() == 0 && b.getState() == State.RUNNING){   //if remainingTime is now 0
            b.setState(State.TERMINATED); //set its state to TERMINATED
            b.setTerminationTime(instant + 1); //store its endTime in the bcp
        }
        return;
    }

    private boolean allProcessesTerminated(LinkedList<BCP> processes){
        for(BCP b : processes){
            if(b.getState() != State.TERMINATED){
                return false;
            }
        }
        return true;
    }


    //Private attributes
    private LinkedList<BCP> processList;
    private Gantt chart;
    int currentTime;
}
