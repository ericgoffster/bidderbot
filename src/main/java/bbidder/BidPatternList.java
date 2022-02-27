package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BidPatternList {
    public final List<BidPattern> bids;
    public final boolean upTheLine;

    public BidPatternList(List<BidPattern> bids, boolean upTheLine) {
        super();
        this.bids = bids;
        this.upTheLine = upTheLine;
    }

    public static BidPatternList valueOf(String str) {
        String[] parts = str.split(":");
        List<BidPattern> l = new ArrayList<>();
        for (String part : parts[0].trim().split("\\s+")) {
            l.add(BidPattern.valueOf(part));
        }
        boolean upTheLine = parts.length > 1 && parts[1].trim().equals("down");
        return new BidPatternList(l, upTheLine);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (BidPattern bid : bids) {
            sb.append(delim).append(bid);
            delim = " ";
        }
        return upTheLine ? sb.toString() : sb + ":down";
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidPatternList other = (BidPatternList) obj;
        return Objects.equals(bids, other.bids);
    }
}
