package scheduling;
import core.*;
import java.util.Comparator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;

public class RR implements Algorithm {
    private int quantum;
    private int currentQuantum;
    private boolean firstTime;
    private boolean lastTime;
    private ArrayList<BCP> bcps;
    private BCP lastBCP;

    public RR(int quantum){
        this.quantum = quantum;
        this.currentQuantum = quantum;

        firstTime = true;
        lastTime = false;
        bcps = new ArrayList<>();
    }

    @Override
    public BCP apply(LinkedList<BCP> bcpList, int currentTime){
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

        BCP currentBCP;
        LinkedList<BCP> eligibleBCPs;

        if (lastBCP != null && lastBCP.getRemainingTime() > 0){
            currentBCP = lastBCP;

        } else {
            eligibleBCPs = new LinkedList<>();

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

            currentBCP = eligibleBCPs.get(0);
        }

        if (null != currentBCP && currentBCP.getRemainingTime() < currentQuantum){
            currentQuantum = currentBCP.getRemainingTime();
        }

        currentQuantum--;

        /* If only one element left in our filtered list and the remaining time is less
         * or equals quantum, is gonna be last time dispatcher calls us, so re-order the
         * list by their names.
         */
        long currentSize = bcps.stream().filter(bcp -> bcp.getState() != State.TERMINATED).count();

        if (currentSize == 1 && currentBCP.getRemainingTime() <= currentQuantum){
            lastTime = true;
        }

        if (currentQuantum < 1){
            bcps.remove(currentBCP);
            bcps.add(currentBCP);
            this.currentQuantum = this.quantum;
            lastBCP = null;
        } else {
            lastBCP = currentBCP;
        }

        if (lastTime){
            bcpList.sort((bcp1, bcp2) -> 
                bcp1.getProcess().getName().compareTo(bcp2.getProcess().getName()));
        }

        return currentBCP;

    }

}
