package scheduling;
import core.*;
import java.util.LinkedList;


public class Priority implements Algorithm {
    private BCP last;

    @Override
    public BCP apply(LinkedList<BCP> bcpList, int currentTime){

        if (null != last && last.getRemainingTime() > 0){
            return last;
        }

        LinkedList<BCP> eligibleBCPs = new LinkedList<>();

        for (BCP bcp : bcpList) {
            if (!bcp.getState().equals(State.TERMINATED) && bcp.getArrival() <= currentTime) {
                eligibleBCPs.add(bcp);
            }
        }

        if (eligibleBCPs.isEmpty()) {
            return null;
        }

        BCP selectedBCP = eligibleBCPs.getFirst();
        for (BCP bcp : eligibleBCPs) {
            if (bcp.getPriority() > selectedBCP.getPriority()) {
                selectedBCP = bcp;
            }
        }

        last = selectedBCP;

        return selectedBCP;
    }
}