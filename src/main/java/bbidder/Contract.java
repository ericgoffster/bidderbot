package bbidder;

import java.util.Objects;

/**
 * Represent a Contract.
 * 
 * @author goffster
 */
public final class Contract {
    public static final Contract FIRST = new Contract(Bid.P, false, false, 0);
    public final Bid winningBid;
    public final boolean doubled;
    public final boolean redoubled;
    public final int numPasses;

    public Contract(Bid winningBid, boolean doubled, boolean redoubled, int numPasses) {
        super();
        this.winningBid = winningBid;
        this.doubled = doubled;
        this.redoubled = redoubled;
        this.numPasses = numPasses;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubled, redoubled, winningBid, numPasses);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contract other = (Contract) obj;
        return doubled == other.doubled && redoubled == other.redoubled && winningBid == other.winningBid && numPasses == other.numPasses;
    }

    @Override
    public String toString() {
        if (winningBid == Bid.P) {
            return "all pass";
        }
        return winningBid + (redoubled ? "XX" : doubled ? "X" : "");
    }

    public boolean isCompleted() {
        return numPasses == 4 || winningBid != Bid.P && numPasses == 3;
    }

    public Bid nextLevel(int strain) {
        if (winningBid == Bid.P) {
            return Bid.valueOf(0, strain);
        }
        if (strain > winningBid.strain) {
            return Bid.valueOf(winningBid.level, strain);
        } else {
            return Bid.valueOf(winningBid.level + 1, strain);
        }
    }

    public Bid getBid(int jumpLevel, int strain) {
        Bid b = nextLevel(strain);
        while (jumpLevel > 0) {
            b = b.raise();
            jumpLevel--;
        }
        return b;
    }

    public Contract addBid(Bid bid) {
        switch (bid) {
        case P:
            return new Contract(winningBid, doubled, redoubled, numPasses + 1);
        case XX:
            return new Contract(winningBid, true, true, 0);
        case X:
            return new Contract(winningBid, true, redoubled, 0);
        default:
            return new Contract(bid, false, false, 0);
        }
    }

    /**
     * 
     * @param bid
     *            The bid to test
     * @return true if the bid represents a legal bid in the auction
     */
    public boolean isLegalBid(Bid bid) {
        if (isCompleted()) {
            return false;
        }

        switch (bid) {
        case P:
            return true;
        case XX:
            if (redoubled) {
                return false;
            }
            if (!doubled) {
                return false;
            }
            if (numPasses % 2 != 0) {
                return false;
            }
            return true;
        case X:
            if (redoubled || doubled) {
                return false;
            }
            if (winningBid == Bid.P) {
                return false;
            }
            if (numPasses % 2 != 0) {
                return false;
            }
            return true;
        default:
            if (winningBid != Bid.P && bid.ordinal() <= winningBid.ordinal()) {
                return false;
            }
            return true;
        }
    }
}
