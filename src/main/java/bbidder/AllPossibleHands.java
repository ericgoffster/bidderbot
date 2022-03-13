package bbidder;

import bbidder.inferences.LikelyHandSummary;
import bbidder.inferences.LikelySuitSummary;

/**
 * Represents the list of all possible hands.
 * 
 * @author goffster
 *
 */
public class AllPossibleHands implements IHandList {
    private static final LikelyHandSummary SUMMARY = getAllPossibleSummary();
    public int minTotalPoints(int suit) {
        return 0;
    }

    public int minHcp() {
        return 0;
    }

    public int maxHcp() {
        return 40;
    }

    public double avgLenInSuit(int suit) {
        return 13 / 4.0;
    }

    public int minInSuit(int suit) {
        return 0;
    }

    public int maxInSuit(int suit) {
        return 13;
    }

    @Override
    public String toString() {
        return "The set of all hands";
    }
    
    @Override
    public LikelyHandSummary getSummary() {
        return SUMMARY;
    }

    private static LikelyHandSummary getAllPossibleSummary() {
        LikelySuitSummary[] summary = new LikelySuitSummary[5];
        for (int i = 0; i < 4; i++) {
            summary[i] = new LikelySuitSummary(0, 0);
        }
        summary[4] = new LikelySuitSummary(0, 0);
        return new LikelyHandSummary(summary);
    }
}
