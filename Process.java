public class Process {
    private int id;
    private int burst;  
    private int priority;
    private int arrival; 

    public Process(int id, int burst, int priority, int arrival) {
        this.id = id;
        this.burst = burst;
        this.priority = priority;
        this.arrival = arrival;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBurst() {
        return burst;
    }

    public void setBurst(int burst) {
        this.burst = burst;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", burst=" + burst +
                ", priority=" + priority +
                ", arrival=" + arrival +
                '}';
    }
}

