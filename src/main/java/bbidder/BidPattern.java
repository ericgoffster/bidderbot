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
    public final Integer level;
    public final Bid simpleBid;
    private final short suitClass;
    private final Integer jumpLevel;
    public final boolean reverse;
    public final boolean notreverse;

    public BidPattern(boolean isOpposition, String str, boolean upTheLine, boolean reverse, boolean notreverse) {
        this.isOpposition = isOpposition;
        this.str = str;
        this.upTheLine = upTheLine;
        this.reverse = reverse;
        this.notreverse = notreverse;
        String upper = str.toUpperCase();
        if (upper.startsWith("NJ")) {
            simpleBid = null;
            jumpLevel = 0;
            level = null;
            suit = str.substring(2);
            suitClass = getSuitClass(str.substring(2));
        } else if (upper.startsWith("DJ")) {
            jumpLevel = 2;
            level = null;
            suit = str.substring(2);
            simpleBid = null;
            suitClass = getSuitClass(str.substring(2));
        } else if (upper.startsWith("J")) {
            jumpLevel = 1;
            level = null;
            suit = str.substring(1);
            simpleBid = null;
            suitClass = getSuitClass(str.substring(2));
        } else {
            jumpLevel = null;
            simpleBid = Bid.fromStr(str);
            if (simpleBid != null && !simpleBid.isSuitBid()) {
                level = null;
                suit = null;
                suitClass = 0;
            } else {
                level = Integer.parseInt(upper.substring(0, 1)) - 1;
                suit = str.substring(1);
                suitClass = getSuitClass(str.substring(1));
            }
        }
    }

    public static short rotateDown(short s) {
        return (short) ((s >> 1) | ((s & 1) << 5));
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
        default:
            return ALL_SUITS;
        }
    }

    public short getSuitClass() {
        return suitClass;
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
        String[] parts = SplitUtil.split(str, ":");
        boolean downTheLine = false;
        boolean reverse = false;
        boolean notreverse = false;
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("down")) {
                downTheLine = true;
            } else if (parts[i].equalsIgnoreCase("reverse")) {
                reverse = true;
            } else if (parts[i].equalsIgnoreCase("nonreverse")) {
                notreverse = true;
            } else {
                throw new IllegalArgumentException("Uknown qualifier: " + parts[i]);
            }
        }
        return new BidPattern(isOpposition, parts[0].trim(), !downTheLine, reverse, notreverse);
    }

    @Override
    public String toString() {
        String s = str;
        if (!upTheLine) {
            s += ":down";
        }
        if (notreverse) {
            s += ":nonreverse";
        }
        if (reverse) {
            s += ":reverse";
        }
        if (isOpposition) {
            return "(" + s + ")";
        }
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(upTheLine, isOpposition, str, reverse, notreverse);
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
        return upTheLine == other.upTheLine && isOpposition == other.isOpposition && Objects.equals(str, other.str)
                && Objects.equals(reverse, other.reverse) && Objects.equals(notreverse, other.notreverse);
    }
}
