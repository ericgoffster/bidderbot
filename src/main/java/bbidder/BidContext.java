package bbidder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BidContext {
    public final BidPatternList patterns;
    public final Map<String, Integer> suits = new HashMap<>();
    private static final List<Integer> ALL_SUITS = List.of(0, 1, 2, 3);
    private static final List<Integer> MINORS = List.of(0, 1);
    private static final List<Integer> MAJORS = List.of(2, 3);

    Bid lastBidSuit = null;
    int patternPos = 0;
    boolean matches = true;

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
        super();
        this.patterns = patterns;
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

    public class LevelAndSymbol {
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
        
        public Set<Integer> getSuits() {
            Integer strain = getSuit(symbol);
            if (strain != null) {
                return Set.of(strain);
            }
            HashSet<Integer> values = new HashSet<>(getSuitClass());
            values.removeAll(suits.values());
            return values;
        }

        private List<Integer> getSuitClass() {
            if (symbol.equals("M")) {
                return MAJORS;
            } else if (symbol.equals("m")) {
                return MINORS;
            } else {
                return ALL_SUITS;
            }
        }

        public Set<Bid> getBids() {
            Set<Bid> result = new HashSet<>();
            for (int s : getSuits()) {
                switch (level) {
                case "J":
                    result.add(nextLevel(s).raise());
                    break;
                case "NJ":
                    result.add(nextLevel(s));
                    break;
                default:
                    result.add(Bid.valueOf(Integer.parseInt(level) - 1, s));
                    break;
                }
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

    public LevelAndSymbol parsePattern(String pattern) {
        if (pattern.startsWith("NJ")) {
            return new LevelAndSymbol(pattern.substring(0, 2), pattern.substring(2));
        }
        return new LevelAndSymbol(pattern.substring(0, 1), pattern.substring(1));
    }

    public Set<Bid> getBids(String pattern) {
        if (pattern.equalsIgnoreCase("P")) {
            return Set.of(Bid.P);
        }
        if (pattern.equalsIgnoreCase("X")) {
            return Set.of(Bid.X);
        }
        if (pattern.equalsIgnoreCase("XX")) {
            return Set.of(Bid.XX);
        }
        return parsePattern(pattern).getBids();
    }

    public boolean matches(Bid bid, String pattern) {
        Set<Bid> bids = getBids(pattern);
        if (bids.contains(bid)) {
            if (!bid.isSuitBid()) {
                return true;
            }
            LevelAndSymbol levelSym = parsePattern(pattern);
            String symbol = levelSym.symbol;
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
