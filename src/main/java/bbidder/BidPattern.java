package bbidder;

public class BidPattern {
    public final boolean isOpposition;
    public final String str;

    private BidPattern(boolean isOpposition, String str) {
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
}
