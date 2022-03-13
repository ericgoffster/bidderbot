package bbidder;

import static bbidder.Constants.ALL_SUITS;
import static bbidder.Constants.MAJORS;
import static bbidder.Constants.MINORS;

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
    public static final BidPattern PASS = createSimpleBid(Bid.P);
    public final boolean isOpposition;
    public final String suit;
    public final Integer level;
    public final Bid simpleBid;
    private final Integer jumpLevel;
    public final boolean reverse;
    public final boolean notreverse;

    private BidPattern(boolean isOpposition, String suit, Integer level, Bid simpleBid, Integer jumpLevel, boolean reverse, boolean notreverse) {
        super();
        this.isOpposition = isOpposition;
        this.suit = suit;
        this.level = level;
        this.simpleBid = simpleBid;
        this.jumpLevel = jumpLevel;
        this.reverse = reverse;
        this.notreverse = notreverse;
    }

    /**
     * @param isOpposition
     *            true, if is an opposition bid
     * @return A Bid Pattern with isOpposition set.
     */
    public BidPattern withIsOpposition(boolean isOpposition) {
        return new BidPattern(isOpposition, suit, level, simpleBid, jumpLevel, reverse, notreverse);
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
        return Objects.hash(isOpposition, jumpLevel, level, notreverse, reverse, simpleBid, suit);
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
                && notreverse == other.notreverse && reverse == other.reverse && simpleBid == other.simpleBid && Objects.equals(suit, other.suit);
    }

    private String _getString() {
        if (simpleBid != null) {
            return simpleBid.toString();
        }
        if (reverse) {
            return "RV" + suit;
        }
        if (notreverse) {
            return "NR" + suit;
        }
        if (jumpLevel != null) {
            switch (jumpLevel.intValue()) {
            case 0:
                return "NJ" + suit;
            case 1:
                return "J" + suit;
            case 2:
                return "DJ" + suit;
            default:
                throw new IllegalStateException();
            }
        }
        return (level + 1) + suit;
    }

    public static BidPattern createJump(String suit, int jumpLevel) {
        return new BidPattern(false, suit, null, null, jumpLevel, false, false);
    }

    public static BidPattern createReverse(String suit) {
        return new BidPattern(false, suit, null, null, 0, true, false);
    }

    public static BidPattern createNonReverse(String suit) {
        return new BidPattern(false, suit, null, null, 0, false, true);
    }

    public static BidPattern createSimpleBid(Bid simpleBid) {
        if (simpleBid.isSuitBid()) {
            return new BidPattern(false, String.valueOf(Constants.STR_ALL_SUITS.charAt(simpleBid.strain)), simpleBid.level, simpleBid, null, false,
                    false);
        }
        return new BidPattern(false, null, null, simpleBid, null, false, false);
    }
    
    public static BidPattern createBid(int level, String suit) {
        return new BidPattern(false, suit, level, null, null, false, false);
    }
    
    public BidPattern resolveSuit(int strain) {
        if (level != null) {
            return BidPattern.createSimpleBid(Bid.valueOf(level, strain));
        }
        return new BidPattern(isOpposition, String.valueOf(Constants.STR_ALL_SUITS.charAt(strain)), level, simpleBid, jumpLevel, reverse, notreverse);
    }

    private static BidPattern create(String str) {
        String upper = str.toUpperCase();
        if (upper.startsWith("NJ")) {
            return createJump(str.substring(2), 0);
        } else if (upper.startsWith("DJ")) {
            return createJump(str.substring(2), 2);
        } else if (upper.startsWith("J")) {
            return createJump(str.substring(1), 1);
        } else if (upper.startsWith("RV")) {
            return createReverse(str.substring(2));
        } else if (upper.startsWith("NR")) {
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
