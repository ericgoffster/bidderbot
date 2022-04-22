package bbidder;

import java.util.List;
import java.util.Objects;

import bbidder.symbols.ConstSymbol;
import bbidder.utils.ListUtil;

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
public final class BidPattern {
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
    public final boolean isNonConventional;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel,
            Generality generality, boolean isNonConventional) {
        super();
        this.isOpposition = isOpposition;
        this.symbol = symbol;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.generality = generality;
        this.isNonConventional = isNonConventional;
    }
    
    public boolean isPass() {
        return simpleBid == Bid.P;
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A Bid Pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, isNonConventional);
    }

    /**
     * @return The number of jumps. Null if N/A
     */
    public Integer getJumpLevel() {
        return jumpLevel;
    }

    /**
     * @return The strain part.  Null if N/A (i.e. for pass)
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * @return The level part.  Null if N/A
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param symbol
     *            The strain
     * @param jumpLevel
     *            The number of jumps
     * @return A pattern where the level is "jump" based.
     */
    public static BidPattern createJump(Symbol symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, null, symbol.nonConvential());
    }

    /**
     * @param simpleBid
     *            The simple bid
     * @return A simple "constant" bid
     */
    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, new ConstSymbol(simpleBid.strain), simpleBid.level, simpleBid, null, null, false);
        }
        return new BidPattern(false, null, null, simpleBid, null, null, false);
    }

    /**
     * @param level
     *            The level
     * @param symbol
     *            The suitz
     * @return A bid that is the level of a suit
     */
    public static BidPattern createBid(int level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, null, null, symbol.nonConvential());
    }

    /**
     * @param generality
     *            The generality.
     * @return A bid that represents a seires of bids that fit a generality
     */
    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, generality, false);
    }

    /**
     * @param symbol
     *            The new symbol
     * @return A bid with the suit bound to a specific strain
     */
    private BidPattern withSymbol(Symbol symbol) {
        if (level != null) {
            int resolved = symbol.getResolved();
            Bid b = Bid.valueOf(level, resolved);
            if (symbol.compatibleWith(b)) {
                return new BidPattern(isOpposition, new ConstSymbol(symbol.getResolved()), level, b, jumpLevel, generality, isNonConventional);
            }
        }
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, isNonConventional);
    }
    
    /**
     * @param symbols
     *            The suits
     * @return A list of contexts representing the symbol bound to actual values
     */
    public List<Context> resolveSymbols(SymbolTable symbols) {
        if (generality != null) {
            return ListUtil.map(generality.resolveSymbols(symbols), e -> createWild(e.getGenerality()).new Context(e.symbols));
        }
        if (simpleBid != null) {
            return List.of(new Context(symbols));
        }
        return ListUtil.map(getSymbol().resolveSymbols(symbols), e -> withSymbol(e.getSymbol()).new Context(e.symbols));
    }

    /**
     * At this point, the bid pattern's suit has already been
     * resolved, so we just need to determine the level.
     * 
     * @param auction
     *            The current list of bids.
     * @return The bid associated with the given pattern.  Null, if not valid.
     */
    public Bid resolveToBid(Auction auction) {
        if (simpleBid != null) {
            return simpleBid;
        }
        int strain = symbol.getResolved();
        if (getJumpLevel() != null) {
            int newLevel = getJumpLevel();
            Bid b = auction.getContract().getBid(newLevel, strain);
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

    private String _getString() {
        if (simpleBid != null) {
            return simpleBid.toString();
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
        return String.valueOf(level + 1) + symbol;
    }
    
    public class Context {
        public final SymbolTable symbols;

        public Context(SymbolTable symbols) {
            super();
            this.symbols = symbols;
        }

        public BidPattern getBidPattern() {
            return BidPattern.this;
        }
    }
}
