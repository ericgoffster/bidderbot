package bbidder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BidContext {
    public final BidPatternList patterns;
    public final Map<String, Integer> suits = new HashMap<>();

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
            if (symbol.equals("M")) {
                return Set.of(2, 3);
            } else if (symbol.equals("m")) {
                return Set.of(0, 1);
            } else {
                Set<Integer> values = new HashSet<>();
                values.add(0);
                values.add(1);
                values.add(2);
                values.add(3);
                values.removeAll(suits.values());
                return values;
            }
        }

        public Set<Bid> getBids() {
            Set<Bid> result = new HashSet<>();
            switch (level) {
            case "J":
                for (int s: getSuits()) {
                    result.add(nextLevel(s).raise());
                }
                return result;
            case "NJ":
                for (int s: getSuits()) {
                    result.add(nextLevel(s));
                }
                return result;
            default:
                int lev = Integer.parseInt(level) - 1;
                for (int s: getSuits()) {
                    result.add(Bid.valueOf(lev, s));
                }
                return result;
            }
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
