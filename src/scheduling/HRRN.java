package scheduling;
import core.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class HRRN implements Algorithm{
    
    private ArrayList<BCP> bcps;
    private BCP last;

    public HRRN(){
        bcps = new ArrayList<>();
    }

    @Override
    public BCP apply(LinkedList<BCP> bcpList, int currentTime){

        if (last != null && last.getRemainingTime() > 0){
            return last;
        }
        bcps.clear();
        
        LinkedList<BCP> eligibleBCPs = new LinkedList<>();
        for (BCP bcp : bcpList) {
            if (!bcp.getState().equals(State.TERMINATED) && bcp.getArrival() <= currentTime) {
                eligibleBCPs.add(bcp);
            }
        }

        if (eligibleBCPs.isEmpty()){
            return null;
        }

        for (BCP bcp : eligibleBCPs) {
            bcp.setRatio( calculateRatio(bcp, currentTime));
            bcps.add(bcp);
        }
        Collections.sort(bcps, Comparator.comparingDouble(BCP::getRatio).reversed());

        last = bcps.get(0);
        return last;
    }

    private double calculateRatio(BCP bcp, int currentTime){
        int waitingTime = currentTime - bcp.getArrival();
        int burst = bcp.getBurst();
        return ( (double)(waitingTime + burst)) / burst;
    }
}