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
        //set time
        int time = 0; 
        //this variables are for handling processes inside the loop
        BCP prev = null;
        BCP cur = null;
        //this is the entry where we log changes on who's executing when
        GanttEntry e = null;

        //as long as there are processes to be run
        while(!allProcessesTerminated(this.processList)){
            //replace prev with new prev
            prev = cur;
            //get new process
            cur = a.apply(this.processList, time);
            //update BCPs
            updateBCP(prev, cur, time);
            //log things
            e = manageGanttEntries(e, prev, cur, time);
            //advance in time
            time++;
        }
        return true;
    }



    //private methods
    private void updateBCP(BCP prev, BCP cur, int instant){

        //check if there is a process executing this instant
        if(cur != null){
            //check if cur is executing for the first time
            if(cur.getBurst() == cur.getRemainingTime()){
                cur.setFirstTime(instant);
            }
            //decrease its remaining time
            cur.setRemainingTime(cur.getRemainingTime()-1);
            //set its state to RUNNING
            cur.setState(State.RUNNING);
        }

        //check if there was a process executing before this instant
        if(prev != null){
            //check if prev has terminated
            if(prev.getRemainingTime() == 0){
                prev.setTerminationTime(instant); //because prev did its last work the previous loop 
                prev.setState(State.TERMINATED);
            } else {
                //if it hasn't, make sure it comes back
                prev.setState(State.WAITING);
            }
        }
        return;
    }

    private GanttEntry manageGanttEntries(GanttEntry e, BCP prev, BCP cur, int instant){
        //check if both elements are null
        if((cur == null)&&(prev == null)){
            e =  null; //this is a special case of the second to last clause, in that both processes are the same, so there was no
                         //change in who was using the processor (no one) so there is nothing to log
        }
        //if nothing was executing before
        else if(prev == null && cur != null){
            e = new GanttEntry(cur.getId(), instant); //create a new entry 
        }
        //if nothing is executing now
        else if (cur == null){
            e.setEndTime(instant);     //close old entry
            this.chart.addEntry(e);     //add it to the chart
            e = null;           //as nothing is executing, do not log anything
        }
        //if we got here, it's two different processes back to back
        //check if current process is different to previous process
        //no need to log any changes if prev and cur are the same after all
        else if(!cur.equals(prev)){
            if(e != null){
                e.setEndTime(instant);    //close old entry
                this.chart.addEntry(e);     //add it to the chart
            }
            e = new GanttEntry(cur.getId(), instant);   //create a new entry
        }
        //if we got here, check if this is the last of our processes
        else if(cur.getState() == State.TERMINATED){
            e.setEndTime(instant);
            this.chart.addEntry(e);
            e = null;
        }
        return e;
    }



    private boolean allProcessesTerminated(LinkedList<BCP> processes){
        return processes.stream().allMatch(p -> p.getState() == State.TERMINATED);
    }


    //Private attributes
    private LinkedList<BCP> processList;
    private Gantt chart;
    int currentTime;
}
