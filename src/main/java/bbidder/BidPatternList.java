package bbidder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the bid patterns as read in from the notes.
 * This structure lasts only long enough to call getContexts() which is what is actually used
 * in the compiled version of the notes.
 * 
 * @author goffster
 *
 */
public class BidPatternList {
    public static final BidPatternList EMPTY = new BidPatternList(List.of());
    private final List<BidPattern> bids;

    private BidPatternList(List<BidPattern> bids) {
        super();
        this.bids = bids;
    }
    
    public BidPattern getLastBid() {
        return bids.get(bids.size() - 1);
    }
    
    public List<BidPattern> getBids() {
        return Collections.unmodifiableList(bids);
    }

    public BidPatternList withBidAdded(BidPattern patt) {
        List<BidPattern> nbids = new ArrayList<>(bids);
        nbids.add(patt);
        return new BidPatternList(nbids);
    }

    public BidPatternList withLastBidReplaced(BidPattern patt) {
        List<BidPattern> nbids = new ArrayList<>(bids);
        nbids.set(nbids.size() - 1, patt);
        return new BidPatternList(nbids);
    }

    /*
     * Retrieves the list of bidding contexts for this bid pattern list.
     */
    public List<BiddingContext> resolveSymbols() {
        // no patterns, then a wide open context.
        if (bids.isEmpty()) {
            return List.of(BiddingContext.EMPTY);
        }

        // Add in first hand passing
        BidPattern pattern = bids.get(0);
        List<BiddingContext> l = new ArrayList<>();
        l.addAll(resolveSymbols(BiddingContext.EMPTY.withBidAdded(BidPattern.PASS).withBidAdded(BidPattern.PASS), true));
        l.addAll(resolveSymbols(BiddingContext.EMPTY.withBidAdded(BidPattern.PASS), true));
        l.addAll(resolveSymbols(BiddingContext.EMPTY, true));
        if (!pattern.isOpposition) {
            l.addAll(resolveSymbols(BiddingContext.EMPTY, false));
        }
        return l;
    }

    public static BidPatternList valueOf(InferenceRegistry reg, String str) {
        if (str == null) {
            return null;
        }
        ListParser<BidPattern> parser = new ListParser<BidPattern>(new BidPatternParser(reg), "");
        try(Input inp = new Input(new StringReader(str))) {
            List<BidPattern> l = parser.parse(inp);
            inp.advanceWhite();
            if (inp.ch != -1) {
                throw new IllegalArgumentException("invalid bids: '"+str+"'");
            }
            return new BidPatternList(l);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid bids: '"+str+"'", e);
        }
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

    private List<BiddingContext> resolveSymbols(BiddingContext ctx, boolean isOpp) {
        if (bids.isEmpty()) {
            if (!isOpp) {
                throw new IllegalArgumentException("last bid must be made by 'we'");
            }
            return List.of(ctx);
        }
        if (ctx.getInference().bids.isCompleted()) {
            return List.of();
        }
        // If it is the opps turn and the next bid is not opp, then assume pass for opps
        BidPattern pattern = bids.get(0);
        if (pattern.generality != null) {
            return new BidPatternList(bids.subList(1, bids.size())).resolveSymbols(ctx, pattern, false);
        }
        if (isOpp && !pattern.isOpposition) {
            return resolveSymbols(ctx, BidPattern.PASS, !isOpp);
        }
        return new BidPatternList(bids.subList(1, bids.size())).resolveSymbols(ctx, pattern, !isOpp);
    }

    private List<BiddingContext> resolveSymbols(BiddingContext ctx, BidPattern pattern, boolean isOpp) {
        List<BiddingContext> l = new ArrayList<>();

        for (BiddingContext newCtx : pattern.resolveSymbols(ctx)) {
            l.addAll(resolveSymbols(newCtx, isOpp));
        }
        return l;
    }

    public boolean isCompleted() {
        return bids.size() >= 4 && bids.get(bids.size() - 1).equals(BidPattern.PASS) && bids.get(bids.size() - 2).equals(BidPattern.PASS)
                && bids.get(bids.size() - 3).equals(BidPattern.PASS) && bids.get(bids.size() - 4).equals(BidPattern.PASS);
    }

    public int positionOfWild() {
        for (int i = 0; i < bids.size(); i++) {
            if (bids.get(i).generality != null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Given a bid pattern list where the suits have already been bound,
     * return the last bid of the pattern in the context of a bidList.
     * 
     * @param bidList
     *            The bidList to match.
     * @param players
     *            the players
     * @return The last bid. null if there was no match.
     */
    public Bid getMatch(BidList bidList, Players players) {
        List<Bid> theBids = bidList.getBids();
        int wildSize = theBids.size() - bids.size() + 2;
        int wildPos = positionOfWild();
        if (wildPos < 0 && theBids.size() != bids.size() - 1) {
            return null;
        }
        if (wildPos >= 0 && wildSize < 0) {
            return null;
        }
        int j = 0;
        int i = 0;
        while (i < bids.size() - 1) {
            BidPattern pattern = bids.get(i);
            if (pattern.generality != null) {
                j += wildSize;
            } else {
                Bid bid = theBids.get(j);
                Bid expected = pattern.resolveToBid(bidList.firstN(j));
                if (bid != expected) {
                    return null;
                }
                j++;
            }
            i++;
        }
        Bid theNextBid = bids.get(bids.size() - 1).resolveToBid(bidList);
        if (theNextBid == null || !bidList.isLegalBid(theNextBid)) {
            return null;
        }
        return theNextBid;
    }
}
