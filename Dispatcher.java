import java.util.LinkedList;
public class Dispatcher {
    /* Constructor */
    public Dispatcher(LinkedList<BCP> bcpList){
        this.list = bcpList;
    }
    /* Run well... 'runs' the algorithm and fills the Gantt chart, then returns the String form of this chart
     */
    public String run(Algorithm a){
        this.totalBursts = calculateTotalBursts(this.list);
        this.nproc = this.list.size();
        Gantt g = new Gantt(this.totalBursts, this.nproc);
        for(int i = 0; i < this.totalBursts; i++){
            g.setInstant(a.apply(this.list,i),i);
        }
        return g.toCSV();
    }

    //Private methods
    private int calculateTotalBurst(LinkedList<BCP> list){
        int total = 0;
        for(BCP bcp : list){
            total += bcp.getBurst();
        }
        return total;
    }

    //Private attributes
    private int totalBursts;
    private int nproc;
    private LinkedList<> list;
}
