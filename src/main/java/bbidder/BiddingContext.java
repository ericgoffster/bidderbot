package bbidder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    public final BidList bids;
    public final Map<String, Integer> suits;

    public BiddingContext(BidList boundBidList, Map<String, Integer> suits) {
        super();
        this.bids = boundBidList;
        this.suits = suits;
    }

    public BiddingContext() {
        this(BidList.create(List.of()));
    }

    public BiddingContext(BidList boundBidList) {
        this(boundBidList, new HashMap<>());
    }

    public BiddingContext withNewBid(Bid bid, BidPattern pattern) {
        Set<Bid> acceptable = getBids(pattern);
        if (!acceptable.contains(bid)) {
            throw new IllegalArgumentException(bid + " is incomptabile with " + pattern);
        }
        Map<String, Integer> newSuits;
        if (bid.isSuitBid()) {
            newSuits = new HashMap<String, Integer>(suits);
            String symbol = pattern.getSuit();
            if (symbol != null) {
                Integer strain = getSuit(symbol);
                if (strain == null) {
                    putSuit(newSuits, symbol, bid.strain);
                }
            }
        } else {
            newSuits = suits;
        }
        return new BiddingContext(bids.withBidAdded(bid), newSuits);
    }

    /**
     * @param symbol
     *            The symbol
     * @return (0,1,2,3,4) for a given symbol, null if not found.
     */
    public Integer getSuit(String symbol) {
        Matcher m = SUIT_PATTERN.matcher(symbol);
        if (m.matches()) {
            String lhs = m.group(1);
            int delta = Integer.parseInt(m.group(2));
            Integer s = getSuit(lhs);
            if (s == null) {
                return null;
            }
            return (s + 5 - delta) % 5;
        }
        Integer strain = Strain.getStrain(symbol);
        if (strain != null) {
            return strain;
        }
        if (symbol.equals("OM")) {
            return otherMajor(suits.get("M"));
        }
        if (symbol.equals("om")) {
            return otherMinor(suits.get("m"));
        }
        return suits.get(symbol);
    }

    /**
     * @param pattern
     *            The bid pattern
     * @return The set of possible bids for a pattern
     */
    public Set<Bid> getBids(BidPattern pattern) {
        if (pattern.simpleBid != null) {
            return Set.of(pattern.simpleBid);
        }
        Bid lastBidSuit = bids.getLastBidSuit();
        Bid myLastBid = bids.bids.size() >= 4 ? bids.bids.get(bids.bids.size() - 4) : null;
        Bid partnerLastBid = bids.bids.size() >= 2 ? bids.bids.get(bids.bids.size() - 2) : null;
        Bid myRebid = myLastBid != null && myLastBid.isSuitBid() ? bids.nextLegalBidOf(myLastBid.strain) : null;
        TreeSet<Bid> result = new TreeSet<>();
        for (int strain : BitUtil.iterate(getStrains(pattern))) {
            Bid bid = getBid(pattern, strain);
            if (myRebid != null && bid.isSuitBid()) {
                boolean supportedPartner = partnerLastBid != null && partnerLastBid.isSuitBid() && bid.strain == partnerLastBid.strain;
                if (pattern.reverse) {
                    if (myLastBid == null || !myLastBid.isSuitBid() || bid.ordinal() <= myRebid.ordinal() || supportedPartner || bid.strain == myLastBid.strain || bid.level == myLastBid.level) {
                        continue;
                    }
                }
                if (pattern.notreverse) {
                    if (myLastBid == null || !myLastBid.isSuitBid() || bid.ordinal() >= myRebid.ordinal() || supportedPartner || bid.strain == myLastBid.strain || bid.level == myLastBid.level) {
                        continue;
                    }
                }
            }
            if (lastBidSuit == null || bid.ordinal() > lastBidSuit.ordinal()) {
                result.add(bid);
            }
        }
        if (!pattern.upTheLine) {
            return result.descendingSet();
        }
        return result;
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return bids.toString();
        }
        return bids + " where " + suits;
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

    private static void putSuit(Map<String, Integer> suits, String symbol, int strain) {
        Matcher m = SUIT_PATTERN.matcher(symbol);
        if (m.matches()) {
            String lhs = m.group(1);
            int delta = Integer.parseInt(m.group(2));
            putSuit(suits, lhs, (strain + delta) % 5);
            return;
        }
        if (strain < 0 || strain > 4) {
            throw new IllegalArgumentException("Invalid strain");
        }
        if (symbol.equals("OM")) {
            suits.put("M", otherMajor(strain));
        } else if (symbol.equals("om")) {
            suits.put("m", otherMinor(strain));
        } else {
            suits.put(symbol, strain);
        }
    }

    private static Integer otherMajor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.HEART:
            return Constants.SPADE;
        case Constants.SPADE:
            return Constants.HEART;
        default:
            throw new IllegalArgumentException("invalid major");
        }
    }

    private static Integer otherMinor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.CLUB:
            return Constants.DIAMOND;
        case Constants.DIAMOND:
            return Constants.CLUB;
        default:
            throw new IllegalArgumentException("invalid minor");
        }
    }

    private Bid getBid(BidPattern pattern, int strain) {
        Integer jl = pattern.getJumpLevel();
        if (jl != null) {
            Bid b = nextLevel(strain);
            while (jl > 0) {
                b = b.raise();
                jl--;
            }
            return b;
        }
        return Bid.valueOf(pattern.getLevel(), strain);
    }
    
    public Map<Integer, BiddingContext> getSuits(String suit) {        
        if (suit != null) {
            Integer strain = getSuit(suit);
            if (strain != null) {
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return Map.of(strain, this);
            }
        }
        Map<Integer, BiddingContext> m = new HashMap<>();
        for(int strain: BitUtil.iterate(BidPattern.getSuitClass(suit))) {
            Map<String, Integer> newSuits = new HashMap<>(suits);
            putSuit(newSuits, suit, strain);
            m.put(strain, new BiddingContext(bids, newSuits));
        }
        return m;
    }

    private short getStrains(BidPattern pattern) {
        if (pattern.getSuit() != null) {
            Integer strain = getSuit(pattern.getSuit());
            if (strain != null) {
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return (short) (1 << strain);
            }
        }
        short values = pattern.getSuitClass();
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
}