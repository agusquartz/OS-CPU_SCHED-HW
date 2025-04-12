public class Process {
    private int id;
    private int burst;

    public Process(int id, int burst) {
        this.id = id;
        this.burst = burst;
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

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", burst=" + burst +
                '}';
    }
}