package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BidPatternList {
    public final List<BidPattern> bids;

    public BidPatternList(List<BidPattern> bids) {
        super();
        this.bids = bids;
    }

    public static BidPatternList valueOf(String str) {
        List<BidPattern> l = new ArrayList<>();
        for (String part : str.trim().split("\\s+")) {
            l.add(BidPattern.valueOf(part));
        }
        return new BidPatternList(l);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (BidPattern bid : bids) {
            sb.append(delim).append(bid);
            delim = " ";
        }
        return sb.toString();
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
