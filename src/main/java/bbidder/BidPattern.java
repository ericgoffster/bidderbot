package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import bbidder.generalities.AndGenerality;
import bbidder.generalities.TrueGenerality;

import java.util.Objects;

/**
 * Represents an immutable matchable bid pattern.
 * 
 * Like 1S or 2x or (2M)
 * 
 * Bid patterns typically last only long enough to be compiled into the notes as actual bids.
 * 
 * @author goffster
 *
 */
public class BidPattern {
    static final String STR_DOUBLEJUMP = "DJ";
    static final String STR_JUMP = "J";
    static final String STR_NONJUMP = "NJ";
    static final String STR_NONREVERSE = "NR";
    static final String STR_REVERSE = "RV";
    public static final BidPattern PASS = createSimpleBid(Bid.P);
    public final boolean isOpposition;
    public final String symbol;
    public final Integer level;
    public final Bid simpleBid;
    private final Integer jumpLevel;
    public final boolean reverse;
    public final boolean nonreverse;
    public final Generality generality;

    private BidPattern(boolean isOpposition, String symbol, Integer level, Bid simpleBid, Integer jumpLevel, boolean reverse, boolean notreverse,
            Generality generality) {
        super();
        this.isOpposition = isOpposition;
        this.symbol = symbol;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.reverse = reverse;
        this.nonreverse = notreverse;
        this.generality = generality;
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A Bid Pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, reverse, nonreverse, generality);
    }
    
    public BidPattern withGenerality(Generality generality) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, reverse, nonreverse, generality);
    }

    public BidPattern withGeneralityAdded(Generality g) {
        return withGenerality(AndGenerality.create(generality, g));
    }
    /**
     * @return The number of jumps. Null if N/A
     */
    public Integer getJumpLevel() {
        return jumpLevel;
    }

    /**
     * @return The suit part.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return The level part.
     */
    public Integer getLevel() {
        return level;
    }

    @Override
    public String toString() {
        String s = _getString();
        if (isOpposition) {
            return "(" + s + ")";
        }
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isOpposition, jumpLevel, level, nonreverse, reverse, simpleBid, symbol, generality);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidPattern other = (BidPattern) obj;
        return isOpposition == other.isOpposition && Objects.equals(jumpLevel, other.jumpLevel) && Objects.equals(level, other.level)
                && nonreverse == other.nonreverse && reverse == other.reverse && simpleBid == other.simpleBid && Objects.equals(symbol, other.symbol)
                && Objects.equals(generality, other.generality);
    }

    private String _getString() {
        if (simpleBid != null) {
            return simpleBid.toString();
        }
        if (reverse) {
            return STR_REVERSE + symbol;
        }
        if (nonreverse) {
            return STR_NONREVERSE + symbol;
        }
        if (jumpLevel != null) {
            switch (jumpLevel.intValue()) {
            case 0:
                return STR_NONJUMP + symbol;
            case 1:
                return STR_JUMP + symbol;
            case 2:
                return STR_DOUBLEJUMP + symbol;
            default:
                throw new IllegalStateException();
            }
        }
        return (level + 1) + symbol;
    }

    /**
     * @param symbol The suit
     * @param jumpLevel The number of jumps
     * @return A pattern where the level is "jump" based.
     */
    public static BidPattern createJump(String symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, false, false, null);
    }

    /**
     * @param symbol The suit to reverse
     * @return A bid that is the reverse of a suit
     */
    public static BidPattern createReverse(String symbol) {
        return new BidPattern(false, symbol, null, null, 0, true, false, null);
    }

    /**
     * @param suit to non-reverse
     * @return A bid that is the non-reverse reverse of a suit
     */
    public static BidPattern createNonReverse(String suit) {
        return new BidPattern(false, suit, null, null, 0, false, true, null);
    }

    /**
     * @param simpleBid The simple bid
     * @return A simple "constant" bid
     */
    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, Strain.getName(simpleBid.strain), simpleBid.level, simpleBid, null, false,
                    false, null);
        }
        return new BidPattern(false, null, null, simpleBid, null, false, false, null);
    }

    /**
     * 
     * @param level The level
     * @param symbol The suitz
     * @return A bid that is the level of a suit
     */
    public static BidPattern createBid(int level, String symbol) {
        return new BidPattern(false, symbol, level, null, null, false, false, null);
    }
    
    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, false, false, generality);
    }

    /**
     * @param strain The strain
     * @return A bid with the suit bound to a specific strain
     */
    public BidPattern bindSuit(int strain) {
        if (level != null) {
            return createSimpleBid(Bid.valueOf(level, strain));
        }
        return new BidPattern(isOpposition, Strain.getName(strain), level, simpleBid, jumpLevel, reverse, nonreverse,
                generality);
    }

    /**
     * @param bc The bidding context
     * @return A list of contexts representing the symbol bound to actual values
     */
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        if (generality != null) {
            return generality.resolveSymbols(bc.withBidAdded(createWild(TrueGenerality.T)));
        }
        if (simpleBid != null) {
            return List.of(bc.withBidAdded(this));
        }
        List<BiddingContext> result = new ArrayList<>();
        for (Entry<Integer, BiddingContext> e : bc.resolveSymbols(getSymbol()).entrySet()) {
            result.add(e.getValue().withBidAdded(bindSuit(e.getKey())));
        }
        return result;
    }

    /**
     * At this point, the bid pattern's suit has already been
     * resolved, so we just need to determine the level.
     * 
     * @param bidList
     *            The current list of bids.
     * @return The bid associated with the given pattern.
     */
    public Bid resolveToBid(BidList bidList) {
        if (simpleBid != null) {
            return simpleBid;
        }
        Integer strain = Strain.getStrain(symbol);
        if (strain == null) {
            throw new IllegalStateException();
        }
        if (reverse) {
            Bid b = bidList.nextLevel(strain);
            if (!bidList.isReverse(b)) {
                return null;
            } else {
                return b;
            }
        }
        if (nonreverse) {
            Bid b = bidList.nextLevel(strain);
            if (!bidList.isNonReverse(b)) {
                return null;
            } else {
                return b;
            }
        }
        if (getJumpLevel() != null) {
            return bidList.getBid(getJumpLevel(), strain);
        }
        return Bid.valueOf(getLevel(), strain);
    }
}
