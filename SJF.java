import java.util.LinkedList;

public class SJF implements Algorithm {

    @Override
    public BCP apply(LinkedList<BCP> bcpList, int currentTime){
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
            if (bcp.getBurst() < selectedBCP.getBurst()) {
                selectedBCP = bcp;
            }
        }

        return selectedBCP;
    }
}
