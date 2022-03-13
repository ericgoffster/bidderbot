package bbidder;

import java.util.Objects;

public class NOfTop {
    public final Range r;
    public final int top;
    public final int suit;
    public NOfTop(Range r, int top, int suit) {
        super();
        this.r = r;
        this.top = top;
        this.suit = suit;
    }
    @Override
    public int hashCode() {
        return Objects.hash(r, suit, top);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NOfTop other = (NOfTop) obj;
        return Objects.equals(r, other.r) && suit == other.suit && top == other.top;
    }
    @Override
    public String toString() {
        return r + " of top " + top + " in " + Constants.STR_ALL_SUITS.charAt(suit);
    }
    
    public boolean isEmpty() {
        return r.isEmpty();
    }
    
    public boolean isFull() {
        return r.unBounded();
    }
}
