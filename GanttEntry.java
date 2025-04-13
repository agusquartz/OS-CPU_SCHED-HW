
public class GanttEntry {

    public GanttEntry(String id, int start){
        this.processId = id;
        this.startTime = start;
    }

    public String getId(){
        return this.processId;
    }

    public int getStartTime(){
        return this.startTime;
    }

    public int getEndTime(){
        return this.endTime;
    }

    public void setId(String id){
        this.processId = id;
    }

    public void setStartTime(int start){
        this.startTime = start;
    }

    public void setEndTime(int end){
        this.endTime = end;
    }

    //private attributes
    private int processId;
    private int startTime;
    private int endTime;
}
