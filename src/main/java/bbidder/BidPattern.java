package bbidder;

import java.util.Objects;
import java.util.Optional;

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
    private static final short ALL_SEATS = (short) 0xf;
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
    public final TagSet tags;
    public final boolean antiMatch;
    public final short seats;
    public final boolean downTheLine;
    public final BidPattern greaterThan;
    public final BidPattern lessThan;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel, Generality generality, TagSet tags,
            boolean antiMatch, short seats, boolean downTheLine, BidPattern greaterThan, BidPattern lessThan) {
        super();
        this.isOpposition = isOpposition;
        this.symbol = symbol;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.generality = generality;
        this.tags = tags;
        this.antiMatch = antiMatch;
        this.seats = seats;
        this.downTheLine = downTheLine;
        this.greaterThan = greaterThan;
        this.lessThan = lessThan;
    }

    /**
     * @return True if this bid pattern is a pass.
     */
    public boolean isPass() {
        return simpleBid == Bid.P;
    }

    public BidPattern withSymbol(Symbol symbol) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withSeats(short seats) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withDownTheLine(boolean downTheLine) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withTags(TagSet tags) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withGreaterThan(BidPattern greaterThan) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withLessThan(BidPattern lessThan) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withAntiMatch(boolean antiMatch) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    /**
     * @param symbol
     *            The strain
     * @param jumpLevel
     *            The number of jumps
     * @return A pattern where the level is "jump" based.
     */
    public static BidPattern createJump(Symbol symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, null, TagSet.EMPTY, false, ALL_SEATS, false, null, null);
    }

    /**
     * @param simpleBid
     *            The simple bid
     * @return A simple "constant" bid
     */
    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, new ConstSymbol(simpleBid.strain), simpleBid.level, simpleBid, null, null, TagSet.EMPTY, false, ALL_SEATS, false,
                    null, null);
        }
        return new BidPattern(false, null, null, simpleBid, null, null, TagSet.EMPTY, false, ALL_SEATS, false, null, null);
    }

    /**
     * @param level
     *            The level
     * @param symbol
     *            The strain
     * @return A bid that is the level of a suit
     */
    public static BidPattern createBid(Integer level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, null, null, TagSet.EMPTY, false, ALL_SEATS, false, null, null);
    }

    /**
     * @param generality
     *            The generality.
     * @return A bid that represents a series of bids that fit a generality
     */
    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, generality, TagSet.EMPTY, false, ALL_SEATS, false, null, null);
    }
    
    private Optional<BidPattern> withResolvedBid(Contract contract, Symbol symbol, Bid b) {
        if (!isBidCompatible(contract, symbol, b)) {
            return Optional.empty();
        }
        return Optional.of(new BidPattern(isOpposition, symbol, b.level, b, null, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan));
    }

    /**
     * @param contract
     *            Current contract
     * @param symbol
     *            The new symbol
     * @return A bid with the suit bound to a specific strain. empty if none found.
     */
    private Optional<BidPattern> withSymbol(Contract contract, Symbol symbol) {
        if (contract != null) {
            if (level != null) {
                Bid b = Bid.valueOf(level, symbol.getResolvedStrain());
                return withResolvedBid(contract, symbol, b);
            }
            if (jumpLevel != null) {
                Bid b = contract.getBid(jumpLevel, symbol.getResolvedStrain());
                return withResolvedBid(contract, symbol, b);
            }
        }
        return Optional.of(withSymbol(symbol));
    }

    private boolean isBidCompatible(Contract contract, Symbol symbol, Bid b) {
        if (!contract.isLegalBid(b)) {
            return false;
        }
        if ((seats & (1 << contract.numPasses)) == 0) {
            return false;
        }
        if (lessThan != null) {
            Optional<TaggedBid> tb = lessThan.resolveToBid(contract, null);
            if (tb.isPresent()) {
                Bid comparisonBid = tb.get().bid;
                if (b.compareTo(comparisonBid) >= 0) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (greaterThan != null) {
            Optional<TaggedBid> tb = greaterThan.resolveToBid(contract, null);
            if (tb.isPresent()) {
                Bid comparisonBid = tb.get().bid;
                if (b.compareTo(comparisonBid) <= 0) {
                    return false;
                }
            } else {
                return false;
            }
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
        return MyStream.of(new Context(suitTable)).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.greaterThan != null) {
                return bidPattern.greaterThan.resolveSuits(contract, e.suitTable)
                        .map(e2 -> bidPattern.withGreaterThan(e2.getBidPattern()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.lessThan != null) {
                return bidPattern.lessThan.resolveSuits(contract, e.suitTable)
                        .map(e2 -> bidPattern.withLessThan(e2.getBidPattern()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            return bidPattern.symbol.resolveSuits(suitTable)
                    .flatMap(e2 -> MyStream.ofOptional(bidPattern.withSymbol(contract, e2.getSymbol()))
                            .flatMap(bidPattern2 -> MyStream.of(bidPattern2.new Context(e2.suitTable))));
        });
    }

    /**
     * At this point, the bid pattern's suit has already been
     * resolved, so we just need to determine the level.
     * 
     * @param contract
     *            The current contract.s
     * @param bid
     *            The bid I am trying to match.
     * @return The bid associated with the given pattern. Null, if not valid.
     */
    public Optional<TaggedBid> resolveToBid(Contract contract, TaggedBid bid) {
        if (simpleBid != null) {
            return Optional.of(new TaggedBid(simpleBid, tags));
        }
        int strain = symbol.getResolvedStrain();
        if (contract != null) {
            if (jumpLevel != null) {
                Bid b = contract.getBid(jumpLevel, strain);
                if (!isBidCompatible(contract, symbol, b)) {
                    return Optional.empty();
                }
                return Optional.of(new TaggedBid(b, tags));
            }
            if (level == null) {
                if (bid == null) {
                    throw new IllegalArgumentException("anonymous level not allowed");
                }
                Bid b = Bid.valueOf(bid.bid.level, strain);
                if (!isBidCompatible(contract, symbol, b)) {
                    return Optional.empty();
                }
                return Optional.of(new TaggedBid(b, tags));
            }
        }
        return Optional.of(new TaggedBid(Bid.valueOf(level, strain), tags));
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
        return Objects.hash(antiMatch, downTheLine, generality, greaterThan, isOpposition, jumpLevel, lessThan, level, seats, simpleBid, symbol,
                tags);
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
        return antiMatch == other.antiMatch && downTheLine == other.downTheLine && Objects.equals(generality, other.generality)
                && Objects.equals(greaterThan, other.greaterThan) && isOpposition == other.isOpposition && Objects.equals(jumpLevel, other.jumpLevel)
                && Objects.equals(lessThan, other.lessThan) && Objects.equals(level, other.level) && seats == other.seats
                && simpleBid == other.simpleBid && Objects.equals(symbol, other.symbol) && Objects.equals(tags, other.tags);
    }

    private String _getString() {
        if (simpleBid != null && symbol == null) {
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
        String sym = symbol.toString();
        if (downTheLine) {
            sym = sym + ":down";
        }
        if (seats != 0xf) {
            sym = sym + ":seats" + seats;
        }
        if (!tags.isEmpty()) {
            sym = sym + ":"+tags;
        }
        if (level == null) {
            return "?" + sym;
        }
        return String.valueOf(level + 1) + sym;
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
