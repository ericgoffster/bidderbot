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
    public final Map<String, Integer> suits ;
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
    
    public BidContext clone() {
        return new BidContext(patterns, new HashMap<>(suits), lastBidSuit, patternPos, matches);
    }
    
    public Set<Bid> getMatchingBids() {
        if (!matches) {
            return Set.of();
        }
        if (patternPos >= patterns.bids.size()) {
            return Set.of();
        }
        BidPattern patt = patterns.bids.get(patternPos);
        TreeSet<Bid> result = getBids(patt.str);
        return patterns.upTheLine ? result : result.descendingSet();
    }

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
            if (bid != Bid.P) {
                matches = false;
                return;
            }
        } else {
            patternPos++;
            if (!matches(bid, patt.str)) {
                matches = false;
                return;
            }
        }
        if (bid.isSuitBid()) {
            lastBidSuit = bid;
        }
    }

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
        if (!matches(bid, patt.str)) {
            matches = false;
            return;
        }
        if (bid.isSuitBid()) {
            lastBidSuit = bid;
        }
    }

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

    Integer getSuit(String symbol) {
        Integer strain = Bid.getStrain(symbol);
        if (strain != null) {
            return strain;
        }
        return suits.get(symbol);
    }

    private class LevelAndSymbol {
        public final String level;
        public final String symbol;

        public LevelAndSymbol(String level, String symbol) {
            super();
            this.level = level;
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return level + symbol;
        }
        
        public short getStrains() {
            Integer strain = getSuit(symbol);
            if (strain != null) {
                return (short)(1 << strain);
            }
            short values = getSuitClass();
            for(int i: suits.values()) {
                values &= ~(1 << i);
            }
            return values;
        }

        private short getSuitClass() {
            if (symbol.equals("M")) {
                return MAJORS;
            } else if (symbol.equals("m")) {
                return MINORS;
            } else {
                return ALL_SUITS;
            }
        }

        public Bid getBid(int strain) {
            switch (level) {
            case "J":
                return nextLevel(strain).raise();
            case "NJ":
                return nextLevel(strain);
            default:
                return Bid.valueOf(level.charAt(0) - '1', strain);
            }
        }
        public TreeSet<Bid> getBids() {
            TreeSet<Bid> result = new TreeSet<>();
            for (int strain : BitUtil.iterate(getStrains())) {
                result.add(getBid(strain));
            }
            return result;
        }

        public Bid nextLevel(int strain) {
            if (strain > lastBidSuit.strain) {
                return Bid.valueOf(lastBidSuit.level, strain);
            } else {
                return Bid.valueOf(lastBidSuit.level + 1, strain);
            }
        }
    }

    private LevelAndSymbol parsePattern(String pattern) {
        if (pattern.startsWith("NJ")) {
            return new LevelAndSymbol(pattern.substring(0, 2), pattern.substring(2));
        }
        return new LevelAndSymbol(pattern.substring(0, 1), pattern.substring(1));
    }

    private TreeSet<Bid> getBids(String pattern) {
        if (pattern.equalsIgnoreCase("P")) {
            TreeSet<Bid> ts = new TreeSet<Bid>();
            ts.add(Bid.P);
            return ts;
        }
        if (pattern.equalsIgnoreCase("X")) {
            TreeSet<Bid> ts = new TreeSet<Bid>();
            ts.add(Bid.X);
            return ts;
        }
        if (pattern.equalsIgnoreCase("XX")) {
            TreeSet<Bid> ts = new TreeSet<Bid>();
            ts.add(Bid.XX);
            return ts;
        }
        return parsePattern(pattern).getBids();
    }

    private boolean matches(Bid bid, String pattern) {
        Set<Bid> bids = getBids(pattern);
        if (bids.contains(bid)) {
            if (!bid.isSuitBid()) {
                return true;
            }
            String symbol = parsePattern(pattern).symbol;
            Integer strain = getSuit(symbol);
            if (strain == null) {
                suits.put(symbol, bid.strain);
            }
            return true;
        }
        return false;
    }

    public boolean matches() {
        return matches && patternPos == patterns.bids.size();
    }
}
