package bbidder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The context of applying a series of bids, trying to match a pattern.
 */
public class BidContext implements Cloneable {
    public final BidPatternList patterns;
    public final Map<String, Integer> suits;
    private static final short ALL_SUITS = 0xf;
    private static final short MINORS = 0x3;
    private static final short MAJORS = MINORS << 2;

    Bid lastBidSuit;
    int patternPos;
    boolean matches;

    private BidContext(BidPatternList patterns, Map<String, Integer> suits, Bid lastBidSuit, int patternPos, boolean matches) {
        this.patterns = patterns;
        this.suits = suits;
        this.lastBidSuit = lastBidSuit;
        this.patternPos = patternPos;
        this.matches = matches;
    }

    @Override
    public BidContext clone() {
        return new BidContext(patterns, new HashMap<>(suits), lastBidSuit, patternPos, matches);
    }

    /**
     * @return all matching bids, in either ascending order or descending order.
     */
    public Set<Bid> getMatchingBids() {
        if (!matches) {
            return Set.of();
        }
        if (patternPos >= patterns.bids.size()) {
            return Set.of();
        }
        if (patternPos < patterns.bids.size() - 1) {
            return Set.of();
        }
        BidPattern patt = patterns.bids.get(patternPos);
        TreeSet<Bid> result = getBids(patt);
        return patterns.upTheLine ? result : result.descendingSet();
    }

    /**
     * Adds a bid made by opposition.
     * 
     * @param bid
     *            The bid to add.
     */
    public void addThey(Bid bid) {
        if (!matches) {
            return;
        }
        if (patternPos >= patterns.bids.size()) {
            matches = false;
            return;
        }
        BidPattern patt = patterns.bids.get(patternPos);
        if (!patt.isOpposition) {
            add(bid, new BidPattern(true, "P"));
        } else {
            patternPos++;
            add(bid, patt);
        }
    }

    /**
     * Adds a bid made by us.
     * 
     * @param bid
     *            The bid to add.
     */
    public void addWe(Bid bid) {
        if (!matches) {
            return;
        }
        if (patternPos >= patterns.bids.size()) {
            matches = false;
            return;
        }
        BidPattern patt = patterns.bids.get(patternPos++);
        if (patt.isOpposition) {
            matches = false;
            return;
        }
        add(bid, patt);
    }

    /**
     * Constructs a bid context from a list of bids, and a list of bid patterns.
     * 
     * @param bids
     *            The bids
     * @param patterns
     *            The patterns
     */
    public BidContext(BidList bids, BidPatternList patterns) {
        this(patterns, new HashMap<>(), null, 0, true);
        boolean isOpp = bids.bids.size() % 2 == 0;
        for (Bid bid : bids.bids) {
            if (isOpp) {
                addThey(bid);
            } else {
                addWe(bid);
            }
            isOpp = !isOpp;
        }
    }

    /**
     * @param symbol
     * @return The suit represented by the symbol. null if not found.
     */
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
        if (strain > lastBidSuit.strain) {
            return Bid.valueOf(lastBidSuit.level, strain);
        } else {
            return Bid.valueOf(lastBidSuit.level + 1, strain);
        }
    }

    private TreeSet<Bid> getBids(BidPattern pattern) {
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
        TreeSet<Bid> result = new TreeSet<>();
        for (int strain : BitUtil.iterate(getStrains(pattern))) {
            result.add(getBid(pattern, strain));
        }
        return result;
    }

    private void add(Bid bid, BidPattern pattern) {
        Set<Bid> bids = getBids(pattern);
        if (!bids.contains(bid)) {
            matches = false;
            return;
        }
        if (bid.isSuitBid()) {
            String symbol = pattern.getSuit();
            Integer strain = getSuit(symbol);
            if (strain == null) {
                suits.put(symbol, bid.strain);
            }
            lastBidSuit = bid;
        }
    }

    public boolean matches() {
        return matches && patternPos == patterns.bids.size();
    }
}
