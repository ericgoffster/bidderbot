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
    public static final BidPattern PASS = createSimpleBid(Bid.P);
    public static final BidPattern PASS_OPP = createSimpleBid(Bid.P).withIsOpposition(true);
    public final boolean isOpposition;
    public final Symbol symbol;
    public final Integer level;
    public final Bid simpleBid;
    private final Integer jumpLevel;
    public final Generality generality;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel,
            Generality generality) {
        super();
        this.isOpposition = isOpposition;
        this.symbol = symbol;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.generality = generality;
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A Bid Pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality);
    }

    public BidPattern withGenerality(Generality generality) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality);
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
        return Objects.hash(isOpposition, jumpLevel, level, simpleBid, symbol, generality);
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
                && simpleBid == other.simpleBid && Objects.equals(symbol, other.symbol)
                && Objects.equals(generality, other.generality);
    }

    public String getLevelString() {
        return level == null ? "" : String.valueOf(level + 1);
    }

    private String _getString() {
        if (simpleBid != null) {
            return simpleBid.toString();
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
        return new BidPattern(false, symbol, null, null, jumpLevel, null);
    }

    /**
     * @param simpleBid
     *            The simple bid
     * @return A simple "constant" bid
     */
    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, new ConstSymbol(simpleBid.strain), simpleBid.level, simpleBid, null, null);
        }
        return new BidPattern(false, null, null, simpleBid, null, null);
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
        return new BidPattern(false, symbol, level, null, null, null);
    }

    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, generality);
    }

    /**
     * @param symbol
     *            The new symbol
     * @return A bid with the suit bound to a specific strain
     */
    public BidPattern bindSuit(Symbol symbol) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality);
    }

    /**
     * @param bc
     *            The bidding context
     * @return A list of contexts representing the symbol bound to actual values
     */
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        if (generality != null) {
            List<BiddingContext> l = new ArrayList<>();
            for(BidPatternContext b: generality.resolveSymbols(new BidPatternContext(createWild(TrueGenerality.T), bc.getSuits()))) {
                l.add(new BiddingContext(bc.bidInference.withBidAdded(b.bid), b.getSuits()));
            }
            return l;
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
}
