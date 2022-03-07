package bbidder;

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

    public BidPattern(boolean isOpposition, String str, boolean upTheLine) {
        this.isOpposition = isOpposition;
        this.str = str;
        this.upTheLine = upTheLine;
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

    /**
     * @param str The string to parse
     * @return A BidPattern parsed from a string
     */
    public static BidPattern valueOf(String str) {
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
    public int hashCode() {
        return Objects.hash(upTheLine, isOpposition, str);
    }

    /**
     * @return The suit part.
     */
    public String getSuit() {
        if (str.startsWith("NJ")) {
            return str.substring(2);
        }
        return str.substring(1);
    }

    /**
     * @return The level part.
     */
    public String getLevel() {
        if (str.startsWith("NJ")) {
            return str.substring(0, 2);
        }
        return str.substring(0, 1);
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
