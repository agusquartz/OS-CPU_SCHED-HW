import java.util.Comparator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;

public class RR implements Algorithm {
    private int quantum;
    private int currentQuantum;
    private boolean firstTime;
    private boolean firstTime2;
    private boolean lastTime;
    private ArrayList<BCP> bcps;

    public RR(int quantum){
        this.quantum = quantum;
        this.currentQuantum = quantum;

        firstTime = true;
        lastTime = false;
        bcps = new ArrayList<>();
    }

    @Override
    public BCP apply(LinkedList<BCP> bcpList, int currentTime){
        LinkedList<BCP> eligibleBCPs = new LinkedList<>();

        /* If it's the first time calling the method, copy the bcpList to our ArrayList
         * Sort the array by arrival, and set "is not first time anymore"
         */
        if (firstTime){
            for (BCP bcp : bcpList){
                bcps.add(bcp);
            }
            Collections.sort(bcps, Comparator.comparingInt(BCP::getArrival));

            firstTime = false;
        }

        // Filter the list of bcps by those who are not terminated and have already arrived.
        for (BCP bcp : bcps) {
            if (!bcp.getState().equals(State.TERMINATED) && bcp.getArrival() <= currentTime && bcp.getRemainingTime() > 0) {
                eligibleBCPs.add(bcp);
            }
        }

        // If the filtered list is empty, return null
        if (eligibleBCPs.isEmpty()) {
            return null;
        }

        /* If remaining time of current bcp is less than quantum, quantum = remaining time
         * We'll avoid doing this the first time 'cause there's a possibility that remaining
         * time is smaller than quantum, and we need to take one from quantum the first time
         * for a good reason, right now dispatcher always calls the algorithm once before start.
         * !! I know, this is a huge bug, TALK TO WHO MADE DISPATCHER !!
         */
        BCP currentBCP = eligibleBCPs.get(0);
        if (currentBCP.getRemainingTime() < currentQuantum){
            this.currentQuantum = currentBCP.getRemainingTime();
        }

        /* If only one element left in our filtered list and the remaining time is less
         * or equals quantum, is gonna be last time dispatcher calls us, so re-order the
         * list by their names.
         */
        if (1 == eligibleBCPs.size() && currentBCP.getRemainingTime() <= currentQuantum){
            lastTime = true;
        }
        currentQuantum--;

        // if quantum is over, move the first element to the end and reset quantum
        if (0 == currentQuantum){
            BCP tmp = bcps.remove(0);
            bcps.add(tmp);
            this.currentQuantum = this.quantum;
        }

        // If its the las time, reorder by name
        if (lastTime){
            bcpList.sort((bcp1, bcp2) -> 
                bcp1.getProcess().getName().compareTo(bcp2.getProcess().getName()));
        }
        
        return currentBCP;
    }

}
