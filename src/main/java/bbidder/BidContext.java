package bbidder;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

public class BidContext {
    final BidList boundBidList;
    final Map<String, Integer> suits;
    static final short ALL_SUITS = 0xf;
    static final short MINORS = 0x3;
    static final short MAJORS = MINORS << 2;

    public BidContext(BidList boundBidList, Map<String, Integer> suits) {
        super();
        this.boundBidList = boundBidList;
        this.suits = suits;
    }
    @Override
    public int hashCode() {
        return Objects.hash(boundBidList, suits);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidContext other = (BidContext) obj;
        return Objects.equals(boundBidList, other.boundBidList) && Objects.equals(suits, other.suits);
    }
    
    Integer getSuit(String symbol) {
        Integer strain = Bid.getStrain(symbol);
        if (strain != null) {
            return strain;
        }
        return suits.get(symbol);
    }

    private static short getSuitClass(BidPattern pattern) {
        switch (pattern.getSuit()) {
        case "M":
            return MAJORS;
        case "m":
            return MINORS;
        default:
            return ALL_SUITS;
        }
    }

    private Bid getBid(BidPattern pattern, int strain) {
        String level = pattern.getLevel();
        switch (level) {
        case "J":
            return nextLevel(strain).raise();
        case "NJ":
            return nextLevel(strain);
        default:
            return Bid.valueOf(level.charAt(0) - '1', strain);
        }
    }
    
    public NavigableSet<Bid> getBids(BidPattern pattern) {
        if (pattern.str.equalsIgnoreCase("P")) {
            TreeSet<Bid> ts = new TreeSet<Bid>();
            ts.add(Bid.P);
            return ts;
        }
        if (pattern.str.equalsIgnoreCase("X")) {
            TreeSet<Bid> ts = new TreeSet<Bid>();
            ts.add(Bid.X);
            return ts;
        }
        if (pattern.str.equalsIgnoreCase("XX")) {
            TreeSet<Bid> ts = new TreeSet<Bid>();
            ts.add(Bid.XX);
            return ts;
        }
        Bid lastBidSuit = boundBidList.getLastBidSuit();
        TreeSet<Bid> result = new TreeSet<>();
        for (int strain : BitUtil.iterate(getStrains(pattern))) {
            Bid bid = getBid(pattern, strain);
            if (lastBidSuit == null || bid.ordinal() > lastBidSuit.ordinal()) {
                result.add(bid);
            }
        }
        if (!pattern.upTheLine) {
            return result.descendingSet();
        }
        return result;
    }

    public short getStrains(BidPattern pattern) {
        Integer strain = getSuit(pattern.getSuit());
        if (strain != null) {
            return (short) (1 << strain);
        }
        short values = getSuitClass(pattern);
        for (int i : suits.values()) {
            values &= ~(1 << i);
        }
        return values;
    }

    public Bid nextLevel(int strain) {
        Bid lastBidSuit = boundBidList.getLastBidSuit();
        if (strain > lastBidSuit.strain) {
            return Bid.valueOf(lastBidSuit.level, strain);
        } else {
            return Bid.valueOf(lastBidSuit.level + 1, strain);
        }
    }
    @Override
    public String toString() {
        return "BidCtx [boundBidList=" + boundBidList + ", suits=" + suits + "]";
    }
}