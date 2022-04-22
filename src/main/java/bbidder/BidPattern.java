package bbidder;

import static bbidder.Constants.ALL_SUITS;
import static bbidder.Constants.MAJORS;
import static bbidder.Constants.MINORS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

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
    private static final String STR_DOUBLEJUMP = "DJ";
    private static final String STR_JUMP = "J";
    private static final String STR_NONJUMP = "NJ";
    private static final String STR_NONREVERSE = "NR";
    private static final String STR_REVERSE = "RV";
    public static final BidPattern PASS = createSimpleBid(Bid.P);
    public static final BidPattern WILD = new BidPattern(false, null, null, null, null, false, false, true);
    public final boolean isOpposition;
    public final String suit;
    public final Integer level;
    public final Bid simpleBid;
    private final Integer jumpLevel;
    public final boolean reverse;
    public final boolean nonreverse;
    public final boolean wild;

    private BidPattern(boolean isOpposition, String suit, Integer level, Bid simpleBid, Integer jumpLevel, boolean reverse, boolean notreverse, boolean wild) {
        super();
        this.isOpposition = isOpposition;
        this.suit = suit;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.reverse = reverse;
        this.nonreverse = notreverse;
        this.wild = wild;
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A Bid Pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, suit, level, simpleBid, jumpLevel, reverse, nonreverse, wild);
    }

    public static short rotateDown(short s) {
        return (short) ((s >> 1) | ((s & 1) << 4));
    }

    public static short getSuitClass(String str) {
        if (str.startsWith("~")) {
            return (short) (0xf ^ getSuitClass(str.substring(1)));
        }
        int pos = str.indexOf("-");
        if (pos >= 0) {
            int n = Integer.parseInt(str.substring(pos + 1));
            short suits = getSuitClass(str.substring(0, pos));
            while (n > 0) {
                suits = rotateDown(suits);
                n--;
            }
            return suits;
        }
        {
            Integer strain = Strain.getStrain(str);
            if (strain != null) {
                if (strain < 0 || strain > 3) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return (short) (1 << strain);
            }
        }

        switch (str) {
        case "M":
            return MAJORS;
        case "OM":
            return MAJORS;
        case "m":
            return MINORS;
        case "om":
            return MINORS;
        default:
            return ALL_SUITS;
        }
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
    public String getSuit() {
        return suit;
    }

    /**
     * @return The level part.
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param str
     *            The string to parse
     * @return A BidPattern parsed from a string
     */
    public static BidPattern valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.equals("*")) {
            return WILD;
        }
        if (str.startsWith("(") && str.endsWith(")")) {
            return BidPattern.create(str.substring(1, str.length() - 1).trim()).withIsOpposition(true);
        } else {
            return BidPattern.create(str);
        }
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
        return Objects.hash(isOpposition, jumpLevel, level, nonreverse, reverse, simpleBid, suit, wild);
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
                && nonreverse == other.nonreverse && reverse == other.reverse && simpleBid == other.simpleBid && Objects.equals(suit, other.suit)
                && wild == other.wild;
    }

    private String _getString() {
        if (simpleBid != null) {
            return simpleBid.toString();
        }
        if (reverse) {
            return STR_REVERSE + suit;
        }
        if (nonreverse) {
            return STR_NONREVERSE + suit;
        }
        if (jumpLevel != null) {
            switch (jumpLevel.intValue()) {
            case 0:
                return STR_NONJUMP + suit;
            case 1:
                return STR_JUMP + suit;
            case 2:
                return STR_DOUBLEJUMP + suit;
            default:
                throw new IllegalStateException();
            }
        }
        return (level + 1) + suit;
    }

    public static BidPattern createJump(String suit, int jumpLevel) {
        return new BidPattern(false, suit, null, null, jumpLevel, false, false, false);
    }

    public static BidPattern createReverse(String suit) {
        return new BidPattern(false, suit, null, null, 0, true, false, false);
    }

    public static BidPattern createNonReverse(String suit) {
        return new BidPattern(false, suit, null, null, 0, false, true, false);
    }

    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, String.valueOf(Constants.STR_ALL_SUITS.charAt(simpleBid.strain)), simpleBid.level, simpleBid, null, false,
                    false, false);
        }
        return new BidPattern(false, null, null, simpleBid, null, false, false, false);
    }
    
    public static BidPattern createBid(int level, String suit) {
        return new BidPattern(false, suit, level, null, null, false, false, false);
    }
    
    public BidPattern bindSuit(int strain) {
        if (level != null) {
            return createSimpleBid(Bid.valueOf(level, strain));
        }
        return new BidPattern(isOpposition, String.valueOf(Constants.STR_ALL_SUITS.charAt(strain)), level, simpleBid, jumpLevel, reverse, nonreverse, wild);
    }
    
    public List<BiddingContext> resolveSuits(BiddingContext bc) {
        if (simpleBid != null || wild) {
            return List.of(bc.withBidAdded(this));
        }
        List<BiddingContext> result = new ArrayList<>();
        Map<Integer, BiddingContext> m = bc.getMappedBiddingContexts(getSuit());
        for (Entry<Integer, BiddingContext> e : m.entrySet()) {
            BiddingContext bc2 = e.getValue();
            result.add(bc2.withBidAdded(bindSuit(e.getKey())));
        }
        return result;
    }
    
    /**
     * At this point, the bid pattern's suit has already been
     * resolved, so we just need to determine the level.
     * 
     * @param bidList The current list of bids.
     * @return The bid associated with the given pattern.
     */
    public Bid resolveToBid(BidList bidList) {
        if (simpleBid != null) {
            return simpleBid;
        }
        Integer strain = Strain.getStrain(suit);
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

    private static BidPattern create(String str) {
        String upper = str.toUpperCase();
        if (upper.startsWith(STR_NONJUMP)) {
            return createJump(str.substring(2), 0);
        } else if (upper.startsWith(STR_DOUBLEJUMP)) {
            return createJump(str.substring(2), 2);
        } else if (upper.startsWith(STR_JUMP)) {
            return createJump(str.substring(1), 1);
        } else if (upper.startsWith(STR_REVERSE)) {
            return createReverse(str.substring(2));
        } else if (upper.startsWith(STR_NONREVERSE)) {
            return createNonReverse(str.substring(2));
        } else {
            Bid simpleBid = Bid.fromStr(str);
            if (simpleBid != null) {
                return createSimpleBid(simpleBid);
            } else {
                final int level = Integer.parseInt(upper.substring(0, 1)) - 1;
                if (level < 0 || level > 6) {
                    throw new IllegalArgumentException("Invalid bid: '" + str + "'");
                }
                if (!BiddingContext.isValidSuit(str.substring(1))) {
                    throw new IllegalArgumentException("Invalid bid: " + str);
                }
                return createBid(level, str.substring(1));
            }
        }
    }
}
