package bbidder;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Inferences happen in the context of a series of bids.
 * This represents those bids in addition to a symbol table of suits.
 * i.e.
 * 1M 2M (M would get bound to hearts or spades)
 * 
 * @author goffster
 *
 */
public class BiddingContext {
    private static final short ALL_SUITS = 0xf;
    private static final short MINORS = 0x3;
    private static final short MAJORS = MINORS << 2;
    public final BidList bids;
    public final Map<String, Integer> suits;

    public BiddingContext(BidList boundBidList, Map<String, Integer> suits) {
        super();
        this.bids = boundBidList;
        this.suits = suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BiddingContext other = (BiddingContext) obj;
        return Objects.equals(bids, other.bids) && Objects.equals(suits, other.suits);
    }

    /**
     * @param symbol
     *            The symbol
     * @return (0,1,2,3,4) for a given symbol
     */
    public Integer getSuit(String symbol) {
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

    /**
     * @param pattern
     *            The bid pattern
     * @return The set of possible bids for a pattern
     */
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
        Bid lastBidSuit = bids.getLastBidSuit();
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

    private short getStrains(BidPattern pattern) {
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

    private Bid nextLevel(int strain) {
        Bid lastBidSuit = bids.getLastBidSuit();
        if (strain > lastBidSuit.strain) {
            return Bid.valueOf(lastBidSuit.level, strain);
        } else {
            return Bid.valueOf(lastBidSuit.level + 1, strain);
        }
    }

    @Override
    public String toString() {
        return bids + " where " + suits;
    }
}