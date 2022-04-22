package bbidder;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
    public final Integer jumpLevel;
    public final Generality generality;
    public final boolean isNonConventional;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel, Generality generality,
            boolean isNonConventional) {
        super();
        this.isOpposition = isOpposition;
        this.symbol = symbol;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.generality = generality;
        this.isNonConventional = isNonConventional;
    }

    /**
     * @return True if this bid pattern is a pass.
     */
    public boolean isPass() {
        return simpleBid == Bid.P;
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, isNonConventional);
    }

    /**
     * @param symbol
     *            The strain
     * @param jumpLevel
     *            The number of jumps
     * @return A pattern where the level is "jump" based.
     */
    public static BidPattern createJump(Symbol symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, null, symbol.isNonConvential());
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
     *            The strain
     * @return A bid that is the level of a suit
     */
    public static BidPattern createBid(int level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, null, null, symbol.isNonConvential());
    }

    /**
     * @param generality
     *            The generality.
     * @return A bid that represents a series of bids that fit a generality
     */
    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, generality, false);
    }

    /**
     * @param contract
     *            Current contract
     * @param symbol
     *            The new symbol
     * @return A bid with the suit bound to a specific strain.  empty if none found.
     */
    private Optional<BidPattern> withSymbol(Contract contract, Symbol symbol) {
        if (level != null) {
            int resolved = symbol.getResolvedStrain();
            Bid b = Bid.valueOf(level, resolved);
            if (contract != null && !contract.isLegalBid(b)) {
                return Optional.empty();
            }
            if (symbol.compatibleWith(b)) {
                return Optional.of(new BidPattern(isOpposition, new ConstSymbol(symbol.getResolvedStrain()), b.level, b, null, null, isNonConventional));
            } else {
                return Optional.empty();
            }
        }
        if (jumpLevel != null) {
            if (contract != null) {
                int resolved = symbol.getResolvedStrain();
                Bid b = contract.getBid(jumpLevel, resolved);
                if (symbol.compatibleWith(b) && contract.isLegalBid(b)) {
                    return Optional
                            .of(new BidPattern(isOpposition, new ConstSymbol(symbol.getResolvedStrain()), b.level, b, null, null, isNonConventional));
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.of(new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, isNonConventional));
    }

    /**
     * @param contract
     *            Current contract
     * @param suitTable
     *            The symbols
     * @return A list of contexts representing the symbol bound to actual values
     */
    public Stream<Context> resolveSuits(Contract contract, SuitTable suitTable) {
        if (generality != null) {
            return generality.resolveSuits(suitTable).map(e -> createWild(e.getGenerality()).new Context(e.suitTable));
        }
        if (simpleBid != null) {
            return Stream.of(new Context(suitTable));
        }
        return symbol.resolveSuits(suitTable).flatMap(e -> withSymbol(contract, e.getSymbol()).stream().map(newSym -> newSym.new Context(e.suitTable)));
    }

    /**
     * At this point, the bid pattern's suit has already been
     * resolved, so we just need to determine the level.
     * 
     * @param auction
     *            The current list of bids.
     * @return The bid associated with the given pattern. Null, if not valid.
     */
    public Optional<Bid> resolveToBid(Auction auction) {
        if (simpleBid != null) {
            return Optional.of(simpleBid);
        }
        int strain = symbol.getResolvedStrain();
        if (jumpLevel != null) {
            Bid b = auction.getContract().getBid(jumpLevel, strain);
            if (level != null && b.level != level.intValue()) {
                return Optional.empty();
            }
            if (!symbol.compatibleWith(b)) {
                return Optional.empty();
            }
            return Optional.of(b);
        }
        return Optional.of(Bid.valueOf(level, strain));
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
                && simpleBid == other.simpleBid && Objects.equals(symbol, other.symbol) && Objects.equals(generality, other.generality);
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
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public BidPattern getBidPattern() {
            return BidPattern.this;
        }
    }
}
