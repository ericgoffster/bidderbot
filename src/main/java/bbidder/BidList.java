package bbidder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A list of bids
 * 
 * @author goffster
 *
 */
public class BidList {
    public final List<Bid> bids;

    public BidList(List<Bid> bids) {
        super();
        this.bids = Collections.unmodifiableList(bids);
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new bid list with the given bid added
     */
    public BidList withBidAdded(Bid bid) {
        List<Bid> newBids = new ArrayList<>(bids);
        newBids.add(bid);
        return new BidList(newBids);
    }

    /**
     * @param bid
     *            The bid to add
     * @return A new bid list with the given bid prepended
     */
    public BidList withBidPrepended(Bid bid) {
        List<Bid> newBids = new ArrayList<>();
        newBids.add(bid);
        newBids.addAll(bids);
        return new BidList(newBids);
    }

    /**
     * @return The last bid in the sequence
     */
    public Bid getLastBid() {
        return bids.get(bids.size() - 1);
    }

    /**
     * @return The last actual suit bid. Null if all pass.
     */
    public Bid getLastBidSuit() {
        for (int i = bids.size() - 1; i >= 0; i--) {
            if (bids.get(i).isSuitBid()) {
                return bids.get(i);
            }
        }
        return null;
    }

    /**
     * @return A bid list of everything exception the last bid.
     */
    public BidList exceptLast() {
        return new BidList(bids.subList(0, bids.size() - 1));
    }

    /**
     * @param str
     *            The string to parse
     * @return A bid list parsed from the string
     */
    public static BidList valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+");
        List<Bid> bids = new ArrayList<>();
        for (String part : parts) {
            Bid b = Bid.fromStr(part);
            if (b == null) {
                throw new IllegalArgumentException("Illegal bid: '" + part + "'");
            }
            bids.add(b);
        }
        return new BidList(bids);
    }
    
    /**
     * @param str
     *            The string to parse
     * @return A bid list parsed from the string
     */
    public static BidList valueOf2(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+");
        List<Bid> bids = new ArrayList<>();
        boolean we = false;
        for (String part : parts) {
            if (part.startsWith("(") && part.endsWith(")")) {
                Bid b = Bid.fromStr(part.substring(1,part.length() - 1));
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = false;
            } else {
                if (we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part);
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = true;
            }
        }
        return new BidList(bids);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (Bid bid : bids) {
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
        BidList other = (BidList) obj;
        return Objects.equals(bids, other.bids);
    }
}
