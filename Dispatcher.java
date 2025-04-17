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
        int currentTime = 0;    //inicia el tiempo
        BCP executing = a.apply(this.processList, currentTime);    //get first process to run
        boolean firstTime = true; //in case we dont execute since the instant 0, keep track of when is the first time we process
        GanttEntry e = null; //initialize GanttEntry to something

        if(!(executing == null)){   //if we receive a process in instant 0
            executing.setState(State.RUNNING);  //this process starts RUNNING in the first instant
            executing.setFirstTime(currentTime);    //store in the BCP when this process started running
            e = new GanttEntry(executing.getId(), currentTime);  //create a GanttEntry for this execution burst
            firstTime = false;  //we started executing since instant 0, so firstTime is unnecesary
        }
        while(!allProcessesTerminated(this.processList)){   //so long as there are still processes to work on
            //at each time run the algorithm
            BCP newExecuting = a.apply(this.processList,currentTime);   //get the process to work on
            if (newExecuting == null) { //if we don't have a process at this instant
                currentTime++;  //in cases where there is no process at this time
            }else{
                if(firstTime){  //is this the first loop with a process to work on?
                    e = new GanttEntry(newExecuting.getId(),currentTime);   //yes, create a Gantt Entry
                    firstTime = false;  //now firstTime is unnecesary
                }
                // Verification of next BCP
                if(newExecuting != executing && executing != null) { //is the process we got now, different to the one  we were working on last time?
                    if(executing.getState() == State.RUNNING) { //if we were working on something, and we expulsed it
                        executing.setState(State.READY);    //make sure the other process can come back
                        e.setEndTime(currentTime);  //log the work we did
                        this.chart.addEntry(e);
                    }else{  //we have finished our work
                    e.setEndTime(executing.getTerminationTime());  //log the work we did
                    this.chart.addEntry(e);
                    }
                    e = new GanttEntry(newExecuting.getId(), currentTime);
                }
                executing = newExecuting;   //the current process is now the one we just got
                                            
                //save curTime
                int timeBeforeUpdate = currentTime;
                //increment currentTime
                currentTime++;
                //update the executing process bcp
                updateBCP(executing, timeBeforeUpdate);
                System.out.println("Bucle N°: "+currentTime);
                chart.getEntries().stream().forEach(m -> System.out.println(m.getId()+" "+m.getStartTime()+" "+m.getEndTime()+"."+m.toString()));
            }
        }
                //close last GanttEntry
                e.setEndTime(executing.getTerminationTime());
                this.chart.addEntry(e);
                System.out.println("Bucle N°: "+currentTime);
                chart.getEntries().stream().forEach(m -> System.out.println(m.getId()+" "+m.getStartTime()+" "+m.getEndTime()+"."+m.toString()));

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
