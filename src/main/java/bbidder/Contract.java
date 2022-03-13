package bbidder;

import java.util.Objects;

public class Contract {
    public final int position;
    public final Bid winningBid;
    public final boolean doubled;
    public final boolean redoubled;

    public Contract(int position, Bid winningBid, boolean doubled, boolean redoubled) {
        super();
        this.position = position;
        this.winningBid = winningBid;
        this.doubled = doubled;
        this.redoubled = redoubled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubled, position, redoubled, winningBid);
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
        return doubled == other.doubled && position == other.position && redoubled == other.redoubled && winningBid == other.winningBid;
    }

    @Override
    public String toString() {
        if (winningBid == Bid.P) {
            return "all pass";
        }
        return winningBid + (redoubled ? "XX" : doubled ? "X" : "") + " by " + position;
    }
}
