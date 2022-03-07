package bbidder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

public class BidInference {
    public final BidPatternList bids;
    public final InferenceList inferences;
    
    static final short ALL_SUITS = 0xf;
    static final short MINORS = 0x3;
    static final short MAJORS = MINORS << 2;

    public BidInference(BidPatternList bids, InferenceList inferences) {
        super();
        this.bids = bids;
        this.inferences = inferences;
    }

    public static BidInference valueOf(InferenceRegistry reg, String str) {
        int pos = str.indexOf("=>");
        return new BidInference(BidPatternList.valueOf(str.substring(0, pos)), InferenceList.valueOf(reg, str.substring(pos + 2)));
    }

    @Override
    public String toString() {
        return bids + " => " + inferences;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, inferences);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidInference other = (BidInference) obj;
        return Objects.equals(bids, other.bids) && Objects.equals(inferences, other.inferences);
    }
    
    static class BidCtx {
        final BidList boundBidList;
        final Bid lastBidSuit;
        final Map<String, Integer> suits;
        public BidCtx(BidList boundBidList, Bid lastBidSuit, Map<String, Integer> suits) {
            super();
            this.boundBidList = boundBidList;
            this.lastBidSuit = lastBidSuit;
            this.suits = suits;
        }
        @Override
        public int hashCode() {
            return Objects.hash(boundBidList, lastBidSuit, suits);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BidCtx other = (BidCtx) obj;
            return Objects.equals(boundBidList, other.boundBidList) && lastBidSuit == other.lastBidSuit && Objects.equals(suits, other.suits);
        }
    }
    
    void getContexts(List<BidCtx> l, BidList boundBidList, Bid lastBidSuit, Map<String, Integer> suits, BidPatternList remaining, boolean isOpp) {
        if (remaining.bids.isEmpty()) {
            if (!isOpp) {
                throw new IllegalArgumentException("last bid must be made by 'we'");
            }
            l.add(new BidCtx(boundBidList, lastBidSuit, suits));
            return;
        }
        BidPattern pattern = remaining.bids.get(0);
        // If it is the opps turn and the next bid is not opp, then assume pass for opps
        if (isOpp && !pattern.isOpposition) {
            getContexts(l, boundBidList.addBid(Bid.P), lastBidSuit, suits, remaining, !isOpp);
            return;
        }
        for(Bid bid: getBids(pattern, lastBidSuit, suits)) {
            Bid newLastBidSuit = lastBidSuit;
            Map<String, Integer> newSuits = new HashMap<String, Integer>(suits);
            if (bid.isSuitBid()) {
                String symbol = pattern.getSuit();
                Integer strain = getSuit(symbol, newSuits);
                if (strain == null) {
                    newSuits.put(symbol, bid.strain);
                }
                newLastBidSuit = bid;
            }
            getContexts(l, boundBidList.addBid(bid), newLastBidSuit, newSuits, new BidPatternList(remaining.bids.subList(1, remaining.bids.size())), !isOpp);
        }
    }
    
     static Integer getSuit(String symbol, Map<String, Integer> suits) {
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

    private static Bid getBid(BidPattern pattern, int strain, Bid lastBidSuit) {
        String level = pattern.getLevel();
        switch (level) {
        case "J":
            return nextLevel(strain, lastBidSuit).raise();
        case "NJ":
            return nextLevel(strain, lastBidSuit);
        default:
            return Bid.valueOf(level.charAt(0) - '1', strain);
        }
    }
    
    private NavigableSet<Bid> getBids(BidPattern pattern, Bid lastBidSuit, Map<String, Integer> suits) {
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
        for (int strain : BitUtil.iterate(getStrains(pattern, lastBidSuit, suits))) {
            Bid bid = getBid(pattern, strain, lastBidSuit);
            if (lastBidSuit == null || bid.ordinal() > lastBidSuit.ordinal()) {
                result.add(bid);
            }
        }
        if (!pattern.upTheLine) {
            return result.descendingSet();
        }
        return result;
    }

    private static short getStrains(BidPattern pattern, Bid lastBidSuitl, Map<String, Integer> suits) {
        Integer strain = getSuit(pattern.getSuit(), suits);
        if (strain != null) {
            return (short) (1 << strain);
        }
        short values = getSuitClass(pattern);
        for (int i : suits.values()) {
            values &= ~(1 << i);
        }
        return values;
    }

    private static Bid nextLevel(int strain, Bid lastBidSuit) {
        if (strain > lastBidSuit.strain) {
            return Bid.valueOf(lastBidSuit.level, strain);
        } else {
            return Bid.valueOf(lastBidSuit.level + 1, strain);
        }
    }


}
