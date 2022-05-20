package bbidder;

import java.util.Comparator;
import java.util.Objects;

import bbidder.generalities.AndGenerality;
import bbidder.symbols.ConstSymbol;
import bbidder.utils.MyStream;

/**
 * Represents an immutable matchable bid pattern.
 * 
 * Like 1S or 2x or (2M) or [partner bid_suit M]
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
    public final boolean isWild;

    private BidPattern(boolean isOpposition, Symbol symbol, Integer level, Bid simpleBid, Integer jumpLevel, Generality generality, TagSet tags,
            boolean antiMatch, Seats seats, boolean downTheLine, BidPattern greaterThan, BidPattern lessThan, boolean isWild) {
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
        this.isWild = isWild;
    }

    public boolean isPass() {
        return simpleBid == Bid.P;
    }

    public BidPattern withSymbol(Symbol symbol) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withLevel(Integer level) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withJumpLevel(Integer jumpLevel) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withGenerality(Generality generality) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern addGenerality(Generality generality) {
        if (this.generality == null) {
            return withGenerality(generality);
        } else {
            return withGenerality(AndGenerality.create(this.generality, generality));
        }
    }

    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withSeats(Seats seats) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withDownTheLine(boolean downTheLine) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withTags(TagSet tags) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withGreaterThan(BidPattern greaterThan) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withLessThan(BidPattern lessThan) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public BidPattern withAntiMatch(boolean antiMatch) {
        return new BidPattern(isOpposition, symbol, level, simpleBid, jumpLevel, generality, tags, antiMatch, seats, downTheLine, greaterThan,
                lessThan, isWild);
    }

    public static BidPattern createJump(Symbol symbol, int jumpLevel) {
        return new BidPattern(false, symbol, null, null, jumpLevel, null, TagSet.EMPTY, false, Seats.ALL, false, null, null, false);
    }

    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, new ConstSymbol(simpleBid.strain), simpleBid.level, null, null, null, TagSet.EMPTY, false, Seats.ALL, false,
                    null, null, false);
        }
        return new BidPattern(false, null, null, simpleBid, null, null, TagSet.EMPTY, false, Seats.ALL, false, null, null, false);
    }

    public static BidPattern createBid(Integer level, Symbol symbol) {
        return new BidPattern(false, symbol, level, null, null, null, TagSet.EMPTY, false, Seats.ALL, false, null, null, false);
    }

    public static BidPattern createWild() {
        return new BidPattern(false, null, null, null, null, null, TagSet.EMPTY, false, Seats.ALL, false, null, null, true);
    }

    /**
     * @param suitTable
     *            The symbols
     * @return A stream of contexts with all symbols in the pattern bound to actual suits.
     */
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable)).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.generality != null) {
                return bidPattern.generality.resolveSuits(suitTable).map(e2 -> bidPattern.withGenerality(e2.getGenerality()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.greaterThan != null) {
                return bidPattern.greaterThan.resolveSuits(e.suitTable)
                        .map(e2 -> bidPattern.withGreaterThan(e2.getBidPattern()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.lessThan != null) {
                return bidPattern.lessThan.resolveSuits(e.suitTable).map(e2 -> bidPattern.withLessThan(e2.getBidPattern()).new Context(e2.suitTable));
            }
            return MyStream.of(e);
        }).flatMap(e -> {
            BidPattern bidPattern = e.getBidPattern();
            if (bidPattern.symbol == null) {
                return MyStream.of(e);
            }
            return bidPattern.symbol.resolveSuits(suitTable)
                    .flatMap(e2 -> MyStream.of(bidPattern.withSymbol(e2.getSymbol()).new Context(e2.suitTable)));
        });
    }

    /**
     * @param contract
     *            The current contract
     * @param bid
     *            The current bid (used to resolve anonymous levels)
     * @return The level resolved bid pattern.
     */
    public BidPattern resolveLevel(Contract contract, Bid bid) {
        if (simpleBid != null) {
            return this;
        }
        int strain = symbol.getResolvedStrain();
        if (level != null) {
            return this;
        }
        if (jumpLevel != null) {
            return withLevel(contract.getJumpBid(jumpLevel, strain).level).withJumpLevel(null);
        }
        if (bid == null) {
            throw new IllegalArgumentException("anonymous level not allowed");
        }
        return withLevel(bid.level);
    }

    /**
     * @return The bid this pattern represents (after resolution has been performed)
     */
    public TaggedBid getResolvedBid() {
        if (simpleBid != null) {
            return new TaggedBid(simpleBid, tags);
        }
        return new TaggedBid(Bid.createBid(level, symbol.getResolvedStrain()), tags);
    }

    /**
     * @param contract
     *            The current contract
     * @param bid
     *            The bid to test
     * @return true, if the bid was compatible with this pattern
     */
    public boolean isBidCompatible(Contract contract, Bid bid) {
        if (!contract.isLegalBid(bid)) {
            return false;
        }
        if (!seats.hasSeat(contract.numPasses)) {
            return false;
        }
        if (lessThan != null) {
            Bid lt = lessThan.resolveLevel(contract, null).getResolvedBid().bid;
            if (bid.compareTo(lt) >= 0) {
                return false;
            }
        }
        if (greaterThan != null) {
            Bid gt = greaterThan.resolveLevel(contract, null).getResolvedBid().bid;
            if (bid.compareTo(gt) <= 0) {
                return false;
            }
        }
        return true;
    }

    public Comparator<Bid> getBidPriority() {
        if (downTheLine) {
            return (a, b) -> {
                int cmp = Integer.compare(a.level, b.level);
                if (cmp != 0) {
                    return cmp;
                }
                return -Integer.compare(a.strain, b.strain);
            };
        } else {
            return (a, b) -> a.compareTo(b);
        }
    }

    @Override
    public String toString() {
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
        if (isOpposition) {
            str = "(" + str + ")";
        }
        return str;
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(antiMatch, downTheLine, generality, greaterThan, isOpposition, isWild, jumpLevel, lessThan, level, seats, simpleBid,
                symbol, tags);
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
                && Objects.equals(greaterThan, other.greaterThan) && isOpposition == other.isOpposition && isWild == other.isWild
                && Objects.equals(jumpLevel, other.jumpLevel) && Objects.equals(lessThan, other.lessThan) && Objects.equals(level, other.level)
                && Objects.equals(seats, other.seats) && simpleBid == other.simpleBid && Objects.equals(symbol, other.symbol)
                && Objects.equals(tags, other.tags);
    }

    private String _getBidString() {
        if (generality != null) {
            return "[" + generality + "]";
        }
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
        } else {
            return String.valueOf(level + 1) + symbol;
        }
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
