import java.util.LinkedList;
import java.util.Iterator;

public class RR implements Algorithm {
    private int quantum;
    private int currentQuantum;
    private boolean firstTime;

    public RR(int quantum){
        this.quantum = quantum;
        this.currentQuantum = quantum;
        firstTime = true;
    }

    @Override
    public BCP apply(LinkedList<BCP> bcpList, int currentTime){
        LinkedList<BCP> eligibleBCPs = new LinkedList<>();

        // If it's the first time calling the method
        if (firstTime){
            bcpList.sort(Comparator.comparingInt(BCP::getArrival));
            currentQuantum++;
            firstTime = false;
        }

        for (BCP bcp : bcpList) {
            if (!bcp.getState().equals(State.TERMINATED) && bcp.getArrival() <= currentTime) {
                eligibleBCPs.add(bcp);
            }
        }

        if (eligibleBCPs.isEmpty()) {
            return null;
        }

        currentQuantum--;

        if (0 == currentQuantum){
            currentQuantum = quantum;
            for (Iterator<BCP> it = bcpList.iterator(); it.hasNext();) {
                BCP bcp = it.next();
                if (!bcp.getState().equals(State.TERMINATED) && bcp.getArrival() <= currentTime) {
                    // Remove the BCP from its actual position
                    it.remove();
                    // add the BCP to the last position
                    bcpList.add(bcp);

                    currentQuantum = quantum;
                    break;
                }
            }
            
        }
        
        return eligibleBCPs.get(0);
    }

}