package bbidder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import bbidder.symbols.ConstSymbol;
import bbidder.utils.MyStream;

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
    public static final String STR_DOUBLEJUMP = "DJ";
    public static final String STR_JUMP = "J";
    public static final String STR_NONJUMP = "NJ";
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
    public static BidPattern createBid(Integer level, Symbol symbol) {
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
     * @return A bid with the suit bound to a specific strain. empty if none found.
     */
    private Optional<BidPattern> withSymbol(Contract contract, Symbol symbol) {
        if (level != null) {
            int resolved = symbol.getResolvedStrain();
            Bid b = Bid.valueOf(level, resolved);
            if (!isBidCompatible(contract, symbol, b)) {
                return Optional.empty();
            }
            return Optional.of(new BidPattern(isOpposition, new ConstSymbol(symbol.getResolvedStrain()), b.level, b, null, null, isNonConventional));
        }
        if (jumpLevel != null) {
            if (contract != null) {
                int resolved = symbol.getResolvedStrain();
                Bid b = contract.getBid(jumpLevel, resolved);
                if (!isBidCompatible(contract, symbol, b)) {
                    return Optional.empty();
                }
                return Optional
                        .of(new BidPattern(isOpposition, new ConstSymbol(symbol.getResolvedStrain()), b.level, b, null, null, isNonConventional));
            }
        }
        return Optional.of(new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, isNonConventional));
    }

    private boolean isBidCompatible(Contract contract, Symbol symbol, Bid b) {
        if (contract != null && !contract.isLegalBid(b)) {
            return false;
        }
        if (contract != null && (symbol.getSeats() & (1 << contract.numPasses)) == 0) {
            return false;
        }
        if (!symbol.compatibleWith(b)) {
            return false;
        }
        return true;
    }

    /**
     * @param contract
     *            Current contract
     * @param suitTable
     *            The symbols
     * @return A list of contexts representing the symbol bound to actual values
     */
    public MyStream<Context> resolveSuits(Contract contract, SuitTable suitTable) {
        if (generality != null) {
            return generality.resolveSuits(suitTable).map(e -> createWild(e.getGenerality()).new Context(e.suitTable));
        }
        if (simpleBid != null) {
            return MyStream.of(new Context(suitTable));
        }
        return symbol.resolveSuits(suitTable)
                .flatMap(e -> MyStream.ofOptional(withSymbol(contract, e.getSymbol())).map(newSym -> newSym.new Context(e.suitTable)));
    }
    
    public Set<String> getTags() {
        if (symbol == null) {
            return new HashSet<>();
        }
        return symbol.getTags();
    }

    /**
     * At this point, the bid pattern's suit has already been
     * resolved, so we just need to determine the level.
     * 
     * @param taggedAuction
     *            The current list of bids.
     * @param bid
     *            The bid I am trying to match.
     * @return The bid associated with the given pattern. Null, if not valid.
     */
    public Optional<TaggedBid> resolveToBid(TaggedAuction taggedAuction, TaggedBid bid) {
        if (simpleBid != null) {
            return Optional.of(new TaggedBid(simpleBid, getTags()));
        }
        int strain = symbol.getResolvedStrain();
        if (jumpLevel != null) {
            Contract contract = taggedAuction.getContract();
            Bid b = contract.getBid(jumpLevel, strain);
            if (level != null && b.level != level.intValue()) {
                return Optional.empty();
            }
            if (!isBidCompatible(contract, symbol, b)) {
                return Optional.empty();
            }
            return Optional.of(new TaggedBid(b, getTags()));
        }
        if (level == null) {
            if (bid == null) {
                throw new IllegalArgumentException("anonymous level not allowed");
            }
            Contract contract = taggedAuction.getContract();
            Bid b = Bid.valueOf(bid.bid.level, strain);
            if (level != null && b.level != level.intValue()) {
                return Optional.empty();
            }
            if (!isBidCompatible(contract, symbol, b)) {
                return Optional.empty();
            }
            return Optional.of(new TaggedBid(b, getTags()));
        }
        return Optional.of(new TaggedBid(Bid.valueOf(level, strain), getTags()));
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
        return Objects.hash(generality, isNonConventional, isOpposition, jumpLevel, level, simpleBid, symbol);
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
        return Objects.equals(generality, other.generality) && isNonConventional == other.isNonConventional && isOpposition == other.isOpposition
                && Objects.equals(jumpLevel, other.jumpLevel) && Objects.equals(level, other.level) && simpleBid == other.simpleBid
                && Objects.equals(symbol, other.symbol);
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
        if (level == null) {
            return "?" + symbol;
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
