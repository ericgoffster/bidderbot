package bbidder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BidInference {
    public final BidPatternList bids;
    public final InferenceList inferences;
    
    static final short ALL_SUITS = 0xf;
    static final short MINORS = 0x3;
    static final short MAJORS = MINORS << 2;

    public BidInference(BidPatternList bids, InferenceList inferences) {
        super();
        this.bids = bids;
        this.inferences = inferences;
    }

    public static BidInference valueOf(InferenceRegistry reg, String str) {
        int pos = str.indexOf("=>");
        return new BidInference(BidPatternList.valueOf(str.substring(0, pos)), InferenceList.valueOf(reg, str.substring(pos + 2)));
    }

    @Override
    public String toString() {
        return bids + " => " + inferences;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, inferences);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidInference other = (BidInference) obj;
        return Objects.equals(bids, other.bids) && Objects.equals(inferences, other.inferences);
    }
    
    public List<BidCtx> getContexts() {
        BidCtx ctx = new BidCtx(new BidList(List.of()), new HashMap<>());
        
        // no patterns, then a wide open context.
        if (bids.bids.isEmpty()) {
            return List.of(ctx);
        }

        List<BidCtx> l = new ArrayList<>();
        // If the first pattern is us, then assume we can have an initial pass from the opposition
        // Otherwise, starts with opposition always
        BidPattern pattern = bids.bids.get(0);
        if (!pattern.isOpposition) {
            getContexts(l, new BidCtx(ctx.boundBidList.addBid(Bid.P), ctx.suits), bids, false);
            getContexts(l, ctx, bids, false);
        } else {
            getContexts(l, ctx, bids, true);
        }
        return l;
    }

    void getContexts(List<BidCtx> l, BidCtx ctx, BidPatternList remaining, boolean isOpp) {
        if (remaining.bids.isEmpty()) {
            if (!isOpp) {
                throw new IllegalArgumentException("last bid must be made by 'we'");
            }
            l.add(ctx);
            return;
        }
        // If it is the opps turn and the next bid is not opp, then assume pass for opps
        BidPattern pattern = remaining.bids.get(0);
        if (isOpp && !pattern.isOpposition) {
            getContexts(l, new BidCtx(ctx.boundBidList.addBid(Bid.P), ctx.suits), remaining, !isOpp);
            return;
        }
        
        for(Bid bid: ctx.getBids(pattern)) {
            if (bid.isSuitBid()) {
                Map<String, Integer> newSuits = new HashMap<String, Integer>(ctx.suits);
                String symbol = pattern.getSuit();
                Integer strain = ctx.getSuit(symbol);
                if (strain == null) {
                    newSuits.put(symbol, bid.strain);
                }
                getContexts(l, new BidCtx(ctx.boundBidList.addBid(bid), newSuits), new BidPatternList(remaining.bids.subList(1, remaining.bids.size())), !isOpp);
            } else {
                getContexts(l, new BidCtx(ctx.boundBidList.addBid(bid), ctx.suits), new BidPatternList(remaining.bids.subList(1, remaining.bids.size())), !isOpp);
            }
        }
    }
    
    public List<BoundBidInference> getBoundInference() {
        List<BoundBidInference> result = new ArrayList<>();
        for(BidCtx ctx: getContexts()) {
            result.add(new BoundBidInference(ctx, inferences));
        }
        return result;
    }
}
