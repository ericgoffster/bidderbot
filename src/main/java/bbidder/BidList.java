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
        Bid lastBid = getLastBidSuit();
        if (lastBid != null && bid.isSuitBid() && bid.ordinal() < lastBid.ordinal()) {
            throw new IllegalArgumentException("Invalid bid '"+bid+"'");
        }
            
        List<Bid> newBids = new ArrayList<>(bids);
        newBids.add(bid);
        return new BidList(newBids);
    }
    
    public Contract getContract() {
        boolean redoubled = false;
        boolean doubled = false;
        for (int i = bids.size() - 1; i >= 0; i--) {
            Bid bid = bids.get(i);
            if (bid.isSuitBid()) {
                return new Contract(i, bid, doubled, redoubled);
            }
            if (bid == Bid.X) {
                doubled = true;
            }
            if (bid == Bid.XX) {
                redoubled = true;
            }
        }
        return new Contract(0, Bid.P, false, false);
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
        Contract contract = getContract();
        return contract.winningBid == Bid.P ? null : contract.winningBid;
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
        boolean we = false;
        boolean first = true;
        for (String part : parts) {
            if (part.startsWith("(") && part.endsWith(")")) {
                if (!first && !we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part.substring(1,part.length() - 1));
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = false;
            } else {
                if (!first && we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part);
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = true;
            }
            first = false;
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
