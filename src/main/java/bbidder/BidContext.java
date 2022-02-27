package bbidder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BidContext {
    public final List<Bid> bids = new ArrayList<Bid>();
    public final BidPatternList patterns;
    public final Map<String, Integer> suits = new HashMap<>();
    static Pattern BID = Pattern.compile("(\\d)(\\w+)");
    static Pattern NJ = Pattern.compile("NJ(\\w+)");
    static Pattern J = Pattern.compile("J(\\w+)");
    
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
        for(Bid bid: bids.bids) {
            if (isOpp) {
                addThey(bid);
            } else {
                addWe(bid);
            }
            isOpp = !isOpp;
        }
    }

    public Bid nextLevel(Bid lastBidSuit, int strain) {
        if (strain > lastBidSuit.strain) {
            return Bid.valueOf(lastBidSuit.level, strain);
        } else {
            return Bid.valueOf(lastBidSuit.level + 1, strain);
        }
    }

    Integer getSuit(String symbol) {
        Integer strain = Bid.getStrain(symbol);
        if (strain != null) {
            return strain;
        }
        return suits.get(symbol);
    }

    public boolean matchesStrain(Bid bid, String symbol) {
        return matchesStrain(bid.strain, symbol);
    }
    public boolean matchesStrain(int bidStrain, String symbol) {
        Integer strain = getSuit(symbol);
        if (strain != null) {
            return bidStrain == strain.intValue();
        }
        if (symbol.equals("M")) {
            return bidStrain == 2 || bidStrain == 3;
        } else if (symbol.equals("m")) {
            return bidStrain == 0 || bidStrain == 1;
        } else {
            if (bidStrain == 4) {
                return false;
            }
            if (suits.values().contains(bidStrain)) {
                return false;
            }
            return true;
        }
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
        Set<Bid> result = new HashSet<>();
        {
            Matcher m = BID.matcher(pattern);
            if (m.matches()) {
                String strain = m.group(2);
                int level = Integer.parseInt(m.group(1)) - 1;
                for(int s = 0; s < 5; s++) {
                    if (matchesStrain(s, strain)) {
                        result.add(Bid.valueOf(level, s));
                    }
                }
                return result;
            }
        }
        {
            Matcher m = NJ.matcher(pattern);
            if (m.matches()) {
                String strain = m.group(1);
                for(int s = 0; s < 5; s++) {
                    if (matchesStrain(s, strain)) {
                        result.add(nextLevel(lastBidSuit, getStrain(s, strain)));
                    }
                }
                return result;
            }
        }
        {
            Matcher m = J.matcher(pattern);
            if (m.matches()) {
                String strain = m.group(1);
                for(int s = 0; s < 5; s++) {
                    if (matchesStrain(s, strain)) {
                        result.add(nextLevel(lastBidSuit, getStrain(s, strain)).raise());
                    }
                }
                return result;
            }
        }
        return result;
    }

    public boolean matches(Bid bid, String pattern) {
        Set<Bid> bids = getBids(pattern);
        if (bids.contains(bid)) {
            if (!bid.isSuitBid()) {
                return true;
            }
            {
                Matcher m = BID.matcher(pattern);
                if (m.matches()) {
                    String strain = m.group(2);
                    addStrain(bid, strain);
                    return true;
                }
            }
            {
                Matcher m = NJ.matcher(pattern);
                if (m.matches()) {
                    String strain = m.group(1);
                    addStrain(bid, strain);
                    return true;
                }
            }
            {
                Matcher m = J.matcher(pattern);
                if (m.matches()) {
                    String strain = m.group(1);
                    addStrain(bid, strain);
                    return true;
                }
            }
            return true;
        }
        return false;
    }
    
    private int getStrain(int bidStrain, String symbol) {
        Integer strain = getSuit(symbol);
        if (strain != null) {
            return strain;
        } else {
            return bidStrain;
        }
    }

    private void addStrain(Bid bid, String symbol) {
        Integer strain = getSuit(symbol);
        if (strain == null) {
            suits.put(symbol, bid.strain);
        }
    }

    public boolean matches() {
        return matches && patternPos == patterns.bids.size();
    }
}
