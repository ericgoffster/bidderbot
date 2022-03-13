package bbidder;

import static bbidder.Constants.ALL_SUITS;
import static bbidder.Constants.MAJORS;
import static bbidder.Constants.MINORS;

import java.util.Objects;

/**
 * Represents a matchable bid pattern.
 * 
 * Like 1S or 2x or (2M)
 * 
 * UpTheline refers to how a variable should be matched,
 * from lower bids to higher bids, or vice versa.
 * 
 * Bid patterns typically last only long enough to be compiled into the notes as actual bids.
 * 
 * @author goffster
 *
 */
public class BidPattern {
    public final boolean isOpposition;
    public final String str;
    public final String suit;
    public final Integer level;
    public final Bid simpleBid;
    private final Integer jumpLevel;
    public final boolean reverse;
    public final boolean notreverse;

    public BidPattern(boolean isOpposition, String str) {
        this.isOpposition = isOpposition;
        this.str = str;
        String upper = str.toUpperCase();
        if (upper.startsWith("NJ")) {
            simpleBid = null;
            jumpLevel = 0;
            level = null;
            suit = str.substring(2);
            reverse = false;
            notreverse = false;
        } else if (upper.startsWith("DJ")) {
            jumpLevel = 2;
            level = null;
            suit = str.substring(2);
            simpleBid = null;
            reverse = false;
            notreverse = false;
        } else if (upper.startsWith("J")) {
            jumpLevel = 1;
            level = null;
            suit = str.substring(1);
            simpleBid = null;
            reverse = false;
            notreverse = false;
        } else if (upper.startsWith("RV")) {
            jumpLevel = 0;
            level = null;
            suit = str.substring(2);
            simpleBid = null;
            reverse = true;
            notreverse = false;
        } else if (upper.startsWith("NR")) {
            jumpLevel = 0;
            level = null;
            suit = str.substring(2);
            simpleBid = null;
            reverse = false;
            notreverse = true;
        } else {
            jumpLevel = null;
            simpleBid = Bid.fromStr(str);
            if (simpleBid != null && !simpleBid.isSuitBid()) {
                level = null;
                suit = null;
            } else {
                level = Integer.parseInt(upper.substring(0, 1)) - 1;
                if (level < 0 || level > 6) {
                    throw new IllegalArgumentException("Invalid bid: '" + str + "'");
                }
                suit = str.substring(1);
            }
            reverse = false;
            notreverse = false;
        }
    }

    public static short rotateDown(short s) {
        return (short) ((s >> 1) | ((s & 1) << 4));
    }

    public static short getSuitClass(String str) {
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
        boolean isOpposition = str.startsWith("(") && str.endsWith(")");
        if (isOpposition) {
            str = str.substring(1, str.length() - 1).trim();
        }
        return new BidPattern(isOpposition, str);
    }

    @Override
    public String toString() {
        String s = str;
        if (isOpposition) {
            return "(" + s + ")";
        }
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isOpposition, str, reverse, notreverse);
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
        return isOpposition == other.isOpposition && Objects.equals(str, other.str) && Objects.equals(reverse, other.reverse)
                && Objects.equals(notreverse, other.notreverse);
    }
}
