package bbidder;

/**
 * Represents the list of all possible hands.
 * 
 * @author goffster
 *
 */
public class AllPossibleHands implements IHandList {
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
}
