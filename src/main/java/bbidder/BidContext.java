package bbidder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BidContext {
    public final List<Bid> bids;
    public final Map<String, Integer> suits = new HashMap<>();
    static Pattern BID = Pattern.compile("(\\d)(\\w+)");
    static Pattern NJ = Pattern.compile("NJ(\\w+)");
    static Pattern J = Pattern.compile("J(\\w+)");

    public BidContext(List<Bid> bids) {
        super();
        this.bids = bids;
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
        Integer strain = getSuit(symbol);
        if (strain != null) {
            return bid.strain == strain.intValue();
        }
        if (symbol.equals("M")) {
            return bid.isMajor();
        } else if (symbol.equals("m")) {
            return bid.isMinor();
        } else {
            if (bid.strain == 4) {
                return false;
            }
            if (suits.values().contains(bid.strain)) {
                return false;
            }
            return true;
        }
    }

    public boolean matches(Bid lastBidSuit, Bid bid, String pattern) {
        if (!bid.isSuitBid()) {
            return bid.toString().toLowerCase().equals(pattern.toLowerCase());
        }
        {
            Matcher m = BID.matcher(pattern);
            if (m.matches()) {
                String strain = m.group(2);
                return matchesStrain(bid, strain) && Bid.valueOf(Integer.parseInt(m.group(1)) - 1, getStrain(bid, strain)) == bid;
            }
        }
        {
            Matcher m = NJ.matcher(pattern);
            if (m.matches()) {
                String strain = m.group(1);
                return matchesStrain(bid, strain) && (nextLevel(lastBidSuit, getStrain(bid, strain)) == bid);
            }
        }
        {
            Matcher m = J.matcher(pattern);
            if (m.matches()) {
                String strain = m.group(1);
                return matchesStrain(bid, strain) && (nextLevel(lastBidSuit, getStrain(bid, strain)).raise() == bid);
            }
        }
        return false;
    }

    private int getStrain(Bid bid, String symbol) {
        Integer strain = getSuit(symbol);
        if (strain != null) {
            return strain;
        } else {
            suits.put(symbol, bid.strain);
            return bid.strain;
        }
    }

    public boolean matches(List<BidPattern> patterns) {
        boolean isOpp = bids.size() % 2 == 0;
        int i = 0;
        Bid lastBidSuit = null;
        for (Bid bid : bids) {
            if (i >= patterns.size()) {
                return false;
            }
            BidPattern patt = patterns.get(i);
            if (isOpp && !patt.isOpposition) {
                if (bid != Bid.P) {
                    return false;
                }
            } else {
                i++;
                if (!matches(lastBidSuit, bid, patt.str)) {
                    return false;
                }
            }
            isOpp = !isOpp;
            if (bid.isSuitBid()) {
                lastBidSuit = bid;
            }
        }
        return i == patterns.size();
    }
}
