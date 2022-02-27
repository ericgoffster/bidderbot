package bbidder;

import java.util.Objects;

public class BidPattern {
    public final boolean isOpposition;
    public final String str;

    public BidPattern(boolean isOpposition, String str) {
        this.isOpposition = isOpposition;
        this.str = str;
    }

    @Override
    public String toString() {
        if (isOpposition) {
            return "(" + str + ")";
        }
        return str;
    }

    public static BidPattern valueOf(String str) {
        str = str.trim();
        boolean isOpposition = str.startsWith("(") && str.endsWith(")");
        if (isOpposition) {
            str = str.substring(1, str.length() - 1).trim();
        }
        return new BidPattern(isOpposition, str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isOpposition, str);
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
        return isOpposition == other.isOpposition && Objects.equals(str, other.str);
    }
}
