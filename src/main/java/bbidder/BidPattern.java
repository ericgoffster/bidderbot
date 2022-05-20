package bbidder;

import java.util.Objects;

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
    public final TagSet tags;
    public final boolean antiMatch;
    public final Seats seats;
    public final boolean downTheLine;
    public final BidPattern greaterThan;
    public final BidPattern lessThan;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel, Generality generality, TagSet tags,
            boolean antiMatch, Seats seats, boolean downTheLine, BidPattern greaterThan, BidPattern lessThan) {
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

    public boolean isPass() {
        return simpleBid == Bid.P;
    }

    public BidPattern withSymbol(Symbol symbol) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan);
    }

    public BidPattern withSeats(Seats seats) {
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

    public static BidPattern createJump(Symbol symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, null, TagSet.EMPTY, false, Seats.ALL, false, null, null);
    }

    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, new ConstSymbol(simpleBid.strain), simpleBid.level, simpleBid, null, null, TagSet.EMPTY, false, Seats.ALL, false,
                    null, null);
        }
        return new BidPattern(false, null, null, simpleBid, null, null, TagSet.EMPTY, false, Seats.ALL, false, null, null);
    }

    public static BidPattern createBid(Integer level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, null, null, TagSet.EMPTY, false, Seats.ALL, false, null, null);
    }

    public static BidPattern createWild(Generality generality) {
        return new BidPattern(false, null, null, null, null, generality, TagSet.EMPTY, false, Seats.ALL, false, null, null);
    }
    
    /**
     * @param suitTable
     *            The symbols
     * @return A list of contexts representing the symbol bound to actual values
     */
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        if (generality != null) {
            return generality.resolveSuits(suitTable).map(e -> createWild(e.getGenerality()).new Context(e.suitTable));
        }
        if (simpleBid != null) {
            return MyStream.of(new Context(suitTable));
        }
        return MyStream.of(new Context(suitTable)).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.greaterThan != null) {
                return bidPattern.greaterThan.resolveSuits(e.suitTable)
                        .map(e2 -> bidPattern.withGreaterThan(e2.getBidPattern()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.lessThan != null) {
                return bidPattern.lessThan.resolveSuits(e.suitTable)
                        .map(e2 -> bidPattern.withLessThan(e2.getBidPattern()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            return bidPattern.symbol.resolveSuits(suitTable)
                    .flatMap(e2 -> MyStream.of(bidPattern.withSymbol(e2.getSymbol()))
                            .flatMap(bidPattern2 -> MyStream.of(bidPattern2.new Context(e2.suitTable))));
        });
    }
    
    TaggedBid getResolvedBid(Contract contract, TaggedBid bid) {
        if (simpleBid != null) {
            return new TaggedBid(simpleBid, tags);
        }
        int strain = symbol.getResolvedStrain();
        if (jumpLevel != null) {
            Bid b = contract.getBid(jumpLevel, strain);
            return new TaggedBid(b, tags);
        }
        if (level == null) {
            if (bid == null) {
                throw new IllegalArgumentException("anonymous level not allowed");
            }
            Bid b = Bid.valueOf(bid.bid.level, strain);
            return new TaggedBid(b, tags);
        }
        return new TaggedBid(Bid.valueOf(level, strain), tags);
    }

    public boolean isBidCompatible(Contract contract, Bid b) {
        if (!contract.isLegalBid(b)) {
            return false;
        }
        if (!seats.hasSeat(contract.numPasses)) {
            return false;
        }
        if (lessThan != null) {
            TaggedBid tb = lessThan.getResolvedBid(contract, null);
            Bid comparisonBid = tb.bid;
            if (b.compareTo(comparisonBid) >= 0) {
                return false;
            }
        }
        if (greaterThan != null) {
            TaggedBid tb = greaterThan.getResolvedBid(contract, null);
            Bid comparisonBid = tb.bid;
            if (b.compareTo(comparisonBid) <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (generality != null) {
            return "[" + generality + "]";
        }
        if (isOpposition) {
            return "(" + _getString() + ")";
        }
        return _getString();
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

    private String _getBidString() {
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
        if (level == null) {
            return "?" + symbol;
        } else {
            return String.valueOf(level + 1) + symbol;
        }
    }
    
    private String _getString() {
        String str = _getBidString();
        if (downTheLine) {
            str = str + ":down";
        }
        if (!seats.equals(Seats.ALL)) {
            str = str + ":" + seats;
        }
        str = str + tags;
        if (lessThan != null) {
            str = str + ":<" + lessThan;
        }
        if (greaterThan != null) {
            str = str + ":>" + greaterThan;
        }
        return str;
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
