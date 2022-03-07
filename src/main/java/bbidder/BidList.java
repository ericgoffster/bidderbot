package bbidder;

import java.util.ArrayList;
import java.util.List;

public class BidList {
    public final List<Bid> bids;

    public BidList(List<Bid> bids) {
        super();
        this.bids = bids;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for(Bid bid: bids) {
            sb.append(delim).append(bid);
            delim = " ";
        }
        return sb.toString();
    }
    
    public BidList addBid(Bid bid) {
        List<Bid> newBids = new ArrayList<>(bids);
        newBids.add(bid);
        return new BidList(newBids);
    }
    
    public static BidList valueOf(String str) {
        String[] parts = str.split("\\s+");
        List<Bid> bids = new ArrayList<>();
        for(String part: parts) {
            bids.add(Bid.fromStr(part.trim()));
        }
        return new BidList(bids);
    }
}
