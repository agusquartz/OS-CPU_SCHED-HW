package core;

public class Process {
    private int id;
    private int burst;
    private String name;

    public Process(int id, int burst, String name) {
        this.id = id;
        this.burst = burst;
        this.name = name;
    }

    public String getName(){
        return this.name;
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