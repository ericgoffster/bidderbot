package bbidder;

import java.util.List;

import bbidder.inferences.LikelyHandSummary;
import bbidder.inferences.LikelySuitSummary;

/**
 * Represents a list of hands that have been created in the hand generator.
 * 
 * @author goffster
 *
 */
public class HandList implements IHandList {
    public final List<Hand> l;
    LikelyHandSummary summary ;

    public HandList(List<Hand> l) {
        super();
        this.l = l;
    }

    public int minTotalPoints(int suit) {
        Integer minHcp = null;
        for (Hand h : l) {
            int hcp = h.totalPoints(suit);
            if (minHcp == null || hcp < minHcp) {
                minHcp = hcp;
            }
        }
        return minHcp == null ? 0 : minHcp;
    }

    public int minHcp() {
        Integer minHcp = null;
        for (Hand h : l) {
            int hcp = h.numHCP();
            if (minHcp == null || hcp < minHcp) {
                minHcp = hcp;
            }
        }
        return minHcp == null ? 0 : minHcp;
    }

    public int maxHcp() {
        Integer maxHcp = null;
        for (Hand h : l) {
            int hcp = h.numHCP();
            if (maxHcp == null || hcp > maxHcp) {
                maxHcp = hcp;
            }
        }
        return maxHcp == null ? 40 : maxHcp;
    }

    public double avgLenInSuit(int suit) {
        long sum = 0;
        for (Hand h : l) {
            sum += h.numInSuit(suit);
        }
        return sum / (double) l.size();
    }

    public int minInSuit(int suit) {
        Integer minInSuit = null;
        for (Hand h : l) {
            int len = h.numInSuit(suit);
            if (minInSuit == null || len < minInSuit) {
                minInSuit = len;
            }
        }
        return minInSuit == null ? 0 : minInSuit;
    }

    public int maxInSuit(int suit) {
        Integer maxInSuit = null;
        for (Hand h : l) {
            int len = h.numInSuit(suit);
            if (maxInSuit == null || len > maxInSuit) {
                maxInSuit = len;
            }
        }
        return maxInSuit == null ? 13 : maxInSuit;
    }

    @Override
    public String toString() {
        return String.valueOf(l);
    }

    @Override
    public LikelyHandSummary getSummary() {
        if (summary == null) {
            summary = createSummary();
        }
        return summary;
    }

    private LikelyHandSummary createSummary() {
        LikelySuitSummary[] summary = new LikelySuitSummary[5];
        for (int i = 0; i < 4; i++) {
            summary[i] = new LikelySuitSummary(minTotalPoints(i), minInSuit(i));
        }
        summary[4] = new LikelySuitSummary(minTotalPoints(4), 0);
        return new LikelyHandSummary(summary);
    }
}
