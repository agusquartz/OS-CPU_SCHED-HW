
public class BCP {
    private int id;
    private int burst;
    private int priority;
    private int arrival;
    private int wait;
    private int exec;
    private int resp;
    private int state;
    private Process process;

    public BCP(Process process, int priority, int arrival) {
        this.process = process;
        this.priority = priority;
        this.arrival = arrival;

        this.id = process.getId();
        this.burst = process.getBurst();
        this.wait = 0;
        this.exec = 0;
        this.resp = 0;
        this.state = 0;
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

    public int getarrival() {
        return arrival;
    }

    public int getWait() {
        return wait;
    }

    public int getExec() {
        return exec;
    }

    public int getResp() {
        return resp;
    }

    public int getState() {
        return state;
    }

    public Process getProcess() {
        return process;
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

    public void setarrival(int arrival) {
        this.arrival = arrival;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public void setExec(int exec) {
        this.exec = exec;
    }

    public void setResp(int resp) {
        this.resp = resp;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void Reset() {
        this.burst = process.getBurst();
        this.wait = 0;
        this.exec = 0;
        this.resp = 0;
        this.state = 0;
    }
}