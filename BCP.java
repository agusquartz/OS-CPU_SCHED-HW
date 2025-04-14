
public class BCP {
    private int id;
    private int burst;
    private int priority;
    private int arrival;
    private int startTime;
    private int endTime;
    private int remainingTime;
    private State state;
    private Process process;

    public BCP(Process process, int priority, int arrival) {
        this.process = process;
        this.priority = priority;
        this.arrival = arrival;

        this.id = process.getId();
        this.burst = process.getBurst();
        this.remainingTime = this.burst;    //a lot of work remaining!
        this.state = State.READY;   //all processes arrive READY. No time for NEW
    }

    public int getStartTime(){
        return this.startTime;
    }

    public int getId() {
        return id;
    }

    public int getBurst() {
        return burst;
    }

    public int getPriority() {
        return priority;
    }

    public int getArrival() {
        return arrival;
    }

    public int getFirstTime(){
        return this.startTime;
    }

    public int getTerminationTime(){
        return this.endTime;
    }

    public int getWait() {
        return getExec() - this.burst;
    }

    public int getExec() {
        return this.endTime - this.arrival;
    }

    public int getResp() {
        return getWait() + 1;
    }

    public State getState() {
        return this.state;
    }

    public Process getProcess() {
        return process;
    }

    public int getRemainingTime(){
        return remainingTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBurst(int burst) {
        this.burst = burst;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setRemainingTime(int remaining){
        this.remainingTime = remaining;
    }
    
    public void setFirstTime(int execStart){
        this.startTime = execStart;
    }
    
    public void setTerminationTime(int termination){
        this.endTime = termination;
    }

    public void reset() {
        this.startTime = 0;
        this.endTime = 0;
        this.remainingTime = this.burst;
        this.state = State.READY;
    }
}
