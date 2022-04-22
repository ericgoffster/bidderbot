package bbidder;

import java.util.Objects;

/**
 * Represent a Contract.
 * @author goffster
 */
public final class Contract {
    public final int position;
    public final Bid winningBid;
    public final boolean doubled;
    public final boolean redoubled;
    public final int numPasses;

    public Contract(int position, Bid winningBid, boolean doubled, boolean redoubled, int numPasses) {
        super();
        this.position = position;
        this.winningBid = winningBid;
        this.doubled = doubled;
        this.redoubled = redoubled;
        this.numPasses = numPasses;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubled, position, redoubled, winningBid, numPasses);
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
        return doubled == other.doubled && position == other.position && redoubled == other.redoubled && winningBid == other.winningBid && numPasses == other.numPasses;
    }

    @Override
    public String toString() {
        if (winningBid == Bid.P) {
            return "all pass";
        }
        return winningBid + (redoubled ? "XX" : doubled ? "X" : "") + " by " + position;
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
}
