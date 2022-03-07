package bbidder;

import java.util.List;

/**
 * Represents a list of hands that have been created in the hand generator.
 * 
 * @author goffster
 *
 */
public class HandList {
    public final List<Hand> l;

    public HandList(List<Hand> l) {
        super();
        this.l = l;
    }

    public int minHcp() {
        Integer minHcp = null;
        for (Hand h : l) {
            int hcp = h.numHCP();
            if (minHcp == null || hcp < minHcp) {
                minHcp = hcp;
            }
        }
        return minHcp;
    }

    public int maxHcp() {
        Integer maxHcp = null;
        for (Hand h : l) {
            int hcp = h.numHCP();
            if (maxHcp == null || hcp > maxHcp) {
                maxHcp = hcp;
            }
        }
        return maxHcp;
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
        return minInSuit;
    }

    public int maxInSuit(int suit) {
        Integer maxInSuit = null;
        for (Hand h : l) {
            int len = h.numInSuit(suit);
            if (maxInSuit == null || len > maxInSuit) {
                maxInSuit = len;
            }
        }
        return maxInSuit;
    }

    @Override
    public String toString() {
        return String.valueOf(l);
    }
}
