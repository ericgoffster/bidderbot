package bbidder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    public final BidList bids;
    public final Map<String, Integer> suits;

    public BiddingContext(BidList boundBidList, Map<String, Integer> suits) {
        super();
        this.bids = boundBidList;
        this.suits = suits;
    }
    
    public BiddingContext withNewBid(Bid bid, BidPattern pattern) {
        Map<String, Integer> newSuits;
        if (bid.isSuitBid()) {
            newSuits = new HashMap<String, Integer>(suits);
            String symbol = pattern.getSuit();
            if (symbol != null) {
                Integer strain = getSuit(symbol);
                if (strain == null) {
                    newSuits.put(symbol, bid.strain);
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
        Integer strain = Strain.getStrain(symbol);
        if (strain != null) {
            return strain;
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

    @Override
    public String toString() {
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

    private Bid getBid(BidPattern pattern, int strain) {
        if (pattern.isStep()) {
            return getSteppedBid(pattern.getNumberOfSteps());
        }
        if (pattern.isDoubleJump()) {
            return nextLevel(strain).raise().raise();
        }
        if (pattern.isNonJump()) {
            return nextLevel(strain);
        }
        if (pattern.isJump()) {
            return nextLevel(strain).raise();
        }
        String level = pattern.getLevel();
        switch (level) {
        case "1":
        case "2":
        case "3":
        case "4":
        case "5":
        case "6":
        case "7":
            return Bid.valueOf(level.charAt(0) - '1', strain);
        default:
            throw new IllegalArgumentException("Invalid level: '" + level + "'");
        }
    }

    private Bid getSteppedBid(int numberOfSteps) {
        Bid lastBidSuit = bids.getLastBidSuit();
        return Bid.values()[lastBidSuit.ordinal() + numberOfSteps];
    }

    private short getStrains(BidPattern pattern) {
        if (pattern.getSuit() != null) {
            Integer strain = getSuit(pattern.getSuit());
            if (strain != null) {
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