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
    public final boolean upTheLine;
    public final String suit;
    public final String level;
    public final Bid simpleBid;
    private final short suitClass;

    public BidPattern(boolean isOpposition, String str, boolean upTheLine) {
        this.isOpposition = isOpposition;
        this.str = str;
        this.upTheLine = upTheLine;
        String upper = str.toUpperCase();
        if (upper.startsWith("NJ") || upper.startsWith("DJ")) {
            level = upper.substring(0, 2);
            suit = str.substring(2);
            simpleBid = null;
            suitClass = getSuitClass(str.substring(2));
        } else if (upper.startsWith("J")) {
            level = upper.substring(0, 1);
            suit = str.substring(1);
            simpleBid = null;
            suitClass = getSuitClass(str.substring(2));
        } else {
            simpleBid = Bid.fromStr(str);
            if (simpleBid != null && !simpleBid.isSuitBid()) {
                level = null;
                suit = null;
                suitClass = 0;
            } else {
                level = upper.substring(0, 1);
                suit = str.substring(1);
                suitClass = getSuitClass(str.substring(1));
            }
        }
    }
    
    public static short rotateDown(short s) {
        return (short)((s >> 1) | ((s & 1) << 5));
    }
    
    public static short getSuitClass(String str) {
        switch (str) {
        case "M":
            return MAJORS;
        case "M-1":
            return rotateDown(MAJORS);
        case "M-2":
            return rotateDown(rotateDown(MAJORS));
        case "M-3":
            return rotateDown(rotateDown(rotateDown(MAJORS)));
        case "M-4":
            return rotateDown(rotateDown(rotateDown(rotateDown(MAJORS))));
        case "m":
            return MINORS;
        case "m-1":
            return rotateDown(MINORS);
        case "m-2":
            return rotateDown(rotateDown(MINORS));
        case "m-3":
            return rotateDown(rotateDown(rotateDown(MINORS)));
        case "m-4":
            return rotateDown(rotateDown(rotateDown(rotateDown(MINORS))));
        default:
            return ALL_SUITS;
        }
    }
    
    public short getSuitClass() {
        return suitClass;
    }
    
    public boolean isSuitSet() {
        return suit != null && suit.contains("/");
    }

    public boolean isNonJump() {
        return "NJ".equals(level);
    }

    public boolean isDoubleJump() {
        return "DJ".equals(level);
    }

    public boolean isJump() {
        return "J".equals(level);
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
    public String getLevel() {
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
        boolean downTheLine = false;
        if (str.endsWith(":down")) {
            downTheLine = true;
            str = str.substring(0, str.length() - 5);
        }
        return new BidPattern(isOpposition, str, !downTheLine);
    }

    @Override
    public String toString() {
        String s = str;
        if (!upTheLine) {
            s += ":down";
        }
        if (isOpposition) {
            return "(" + s + ")";
        }
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(upTheLine, isOpposition, str);
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
        return upTheLine == other.upTheLine && isOpposition == other.isOpposition && Objects.equals(str, other.str);
    }
}
