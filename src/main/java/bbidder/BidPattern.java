package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import bbidder.generalities.AndGenerality;
import bbidder.generalities.TrueGenerality;
import bbidder.symbols.ConstSymbol;

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
    public static final BidPattern PASS_OPP = createSimpleBid(Bid.P).withIsOpposition(true);
    public final boolean isOpposition;
    public final Symbol symbol;
    public final Integer level;
    public final Bid simpleBid;
    private final Integer jumpLevel;
    public final boolean reverse;
    public final boolean nonreverse;
    public final Generality generality;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel, boolean reverse, boolean notreverse,
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
    public Symbol getSymbol() {
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
        if (generality != null) {
            return "[" + generality + "]";
        }
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

    public String getLevelString() {
        return level == null ? "" : String.valueOf(level + 1);
    }

    private String _getString() {
        if (simpleBid != null) {
            return simpleBid.toString();
        }
        if (reverse) {
            return STR_REVERSE + getLevelString() + symbol;
        }
        if (nonreverse) {
            return STR_NONREVERSE + getLevelString() + symbol;
        }
        if (jumpLevel != null) {
            switch (jumpLevel.intValue()) {
            case 0:
                return STR_NONJUMP + getLevelString() + symbol;
            case 1:
                return STR_JUMP + getLevelString() + symbol;
            case 2:
                return STR_DOUBLEJUMP + getLevelString() + symbol;
            default:
                throw new IllegalStateException();
            }
        }
        return getLevelString() + symbol;
    }

    /**
     * @param symbol
     *            The suit
     * @param jumpLevel
     *            The number of jumps
     * @return A pattern where the level is "jump" based.
     */
    public static BidPattern createJump(Symbol symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, false, false, null);
    }

    /**
     * @param symbol
     *            The suit to reverse
     * @return A bid that is the reverse of a suit
     */
    public static BidPattern createReverse(Symbol symbol) {
        return new BidPattern(false, symbol, null, null, 0, true, false, null);
    }

    /**
     * @param suit
     *            to non-reverse
     * @return A bid that is the non-reverse reverse of a suit
     */
    public static BidPattern createNonReverse(Symbol suit) {
        return new BidPattern(false, suit, null, null, 0, false, true, null);
    }

    /**
     * @param simpleBid
     *            The simple bid
     * @return A simple "constant" bid
     */
    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, new ConstSymbol(simpleBid.strain), simpleBid.level, simpleBid, null, false, false, null);
        }
        return new BidPattern(false, null, null, simpleBid, null, false, false, null);
    }

    /**
     * 
     * @param level
     *            The level
     * @param symbol
     *            The suitz
     * @return A bid that is the level of a suit
     */
    public static BidPattern createBid(int level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, null, false, false, null);
    }

    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, false, false, generality);
    }

    /**
     * @param symbol
     *            The new symbol
     * @return A bid with the suit bound to a specific strain
     */
    public BidPattern bindSuit(Symbol symbol) {
        if (level != null) {
            return createSimpleBid(Bid.valueOf(level, symbol.getResolved()));
        }
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, reverse, nonreverse, generality);
    }

    /**
     * @param bc
     *            The bidding context
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
        for (Entry<Symbol, BiddingContext> e : bc.resolveSymbols(getSymbol()).entrySet()) {
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
        int strain = symbol.getResolved();
        if (reverse) {
            Bid b = bidList.nextLevel(strain);
            if (level != null && b.level != level.intValue()) {
                return null;
            }
            if (!symbol.compatibleWith(b)) {
                return null;
            }
            if (!bidList.isReverse(b)) {
                return null;
            } else {
                return b;
            }
        }
        if (nonreverse) {
            Bid b = bidList.nextLevel(strain);
            if (level != null && b.level != level.intValue()) {
                return null;
            }
            if (!symbol.compatibleWith(b)) {
                return null;
            }
            if (!bidList.isNonReverse(b)) {
                return null;
            } else {
                return b;
            }
        }
        if (getJumpLevel() != null) {
            int newLevel = getJumpLevel();
            Bid b = bidList.getBid(newLevel, strain);
            if (level != null && b.level != level.intValue()) {
                return null;
            }
            if (!symbol.compatibleWith(b)) {
                return null;
            }
            return b;
        }
        return Bid.valueOf(getLevel(), strain);
    }

    public static BidPattern createBid(Integer jumpLevel, boolean reverse, boolean nonreverse, Integer level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, jumpLevel, reverse, nonreverse, null);
    }
}
