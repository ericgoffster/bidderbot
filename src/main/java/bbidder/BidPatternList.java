package bbidder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the bid patterns as read in from the notes.
 * This structure lasts only long enough to call resolveFirstSymbol() which is what is actually used
 * in the compiled version of the notes.
 * 
 * @author goffster
 *
 */
public final class BidPatternList {
    public static final BidPatternList EMPTY = new BidPatternList(List.of());
    private final List<BidPattern> bids;

    private BidPatternList(List<BidPattern> bids) {
        super();
        this.bids = bids;
    }

    public static BidPatternList create(List<BidPattern> bids) {
        return new BidPatternList(new ArrayList<>(bids));
    }

    /**
     * @return The last bid
     */
    public BidPattern getLastBid() {
        return bids.get(bids.size() - 1);
    }

    /**
     * @return The unmodifiable list of bids
     */
    public List<BidPattern> getBids() {
        return Collections.unmodifiableList(bids);
    }

    /**
     * @return All bids except the first
     */
    public BidPatternList exceptFirst() {
        return new BidPatternList(bids.subList(1, bids.size()));
    }

    /**
     * @param patt
     *            The pattern to add
     * @return A new BidPatternList with the given bid added to the end
     */
    public BidPatternList withBidAdded(BidPattern patt) {
        List<BidPattern> nbids = new ArrayList<>(bids);
        nbids.add(patt);
        return new BidPatternList(nbids);
    }

    /**
     * 
     * @param patt
     *            The bid pattern
     * @return A new BidPatternList with the last bid replaced with the given bid.
     */
    public BidPatternList withLastBidReplaced(BidPattern patt) {
        List<BidPattern> nbids = new ArrayList<>(bids);
        nbids.set(nbids.size() - 1, patt);
        return new BidPatternList(nbids);
    }

    /**
     * @param pattern
     *            pattern
     * @param bc
     *            The bidding context
     * @return A list of contexts representing the symbol bound to actual values
     */
    public List<BidPatternListContext> resolveSymbols(BidPattern pattern, BidPatternListContext bc) {
        List<BidPatternListContext> l = new ArrayList<>();
        for (BidPatternContext b : pattern.resolveSymbols(bc.suits)) {
            l.add(new BidPatternListContext(bc.bids.withBidAdded(b.bidPattern), b.suits));
        }
        return l;
    }
    
    public List<BidPatternList> addInitialPasses() {
        if (bids.get(0).generality != null || isCompleted()) {
            return List.of(this);
        } else {
            List<BidPatternList> passes = new ArrayList<>();
            passes.add(this);
            for(int i = 0; i < 3; i++) {
                BidPatternList prev = passes.get(passes.size() - 1);
                List<BidPattern> l = new ArrayList<>(prev.bids);
                l.add(0, BidPattern.PASS.withIsOpposition(!l.get(0).isOpposition));
                BidPatternList bpl = new BidPatternList(l);
                if (bpl.numInitialPasses() > 4 || bpl.numInitialPasses() == 4 && l.size() > 4) {
                    break;
                }
                passes.add(bpl);
            }
            return passes;
        }
    }

    /**
     * Resolve the first symbol.
     * Allow for first, second, third or fourth chair openings
     * 
     * @param suits
     *            suits
     * 
     * @return The list of resolved bidding pattern contexts
     */
    public List<BidPatternListContext> resolveFirstSymbol(SymbolTable suits) {
        BidPatternList withOpp = withOpposingBidding();
        // If there are no bids, then we are done
        if (withOpp.bids.isEmpty()) {
            return List.of(new BidPatternListContext(BidPatternList.EMPTY, suits));
        }
        BidPattern pattern = withOpp.bids.get(0);

        // If the first bid is a generality, then the generality
        // takes care of all chairs
        if (pattern.generality != null) {
            return withOpp.resolveSymbols(new BidPatternListContext(BidPatternList.EMPTY, suits));
        }
        
        List<BidPatternListContext> list = new ArrayList<>();
        for(BidPatternList bpl: withOpp.addInitialPasses()) {
            list.addAll(bpl.resolveSymbols(new BidPatternListContext(BidPatternList.EMPTY, suits)));
        }
        return list;
    }
    
    /**
     * Evaluate
     * @param ctx
     * @param isOpp
     * @return
     */
    private List<BidPatternListContext> resolveSymbols(BidPatternListContext ctx) {
        BidPattern pattern = bids.get(0);
        boolean isOpp = pattern.generality != null ? bids.get(1).isOpposition : !pattern.isOpposition;
        return exceptFirst().resolveRemainingSymbols(ctx, resolveSymbols(pattern, ctx), isOpp);
    }

    /**
     * Given a bid pattern list where the suits have already been bound,
     * return the last bid of the pattern in the context of a bidList.
     * 
     * @param bidList
     *            The bidList to match.
     * @param players
     *            the players
     * @param matched
     *            the set of matched bids
     * @return The last bid. null if there was no match.
     */
    public Bid getMatch(Auction bidList, Players players, Set<Bid> matched) {
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
                if (!pattern.generality.matches(players, bidList)) {
                    return null;
                }
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
        BidPattern lastPatt = bids.get(bids.size() - 1);
        Bid theNextBid = lastPatt.resolveToBid(bidList);
        if (theNextBid == null || !bidList.isLegalBid(theNextBid)) {
            return null;
        }
        if (matched.contains(theNextBid) && lastPatt.symbol != null && lastPatt.symbol.nonConvential()) {
            return null;
        }
        return theNextBid;
    }

    /**
     * @param reg
     *            The inference registry
     * @param str
     *            The String to parse
     * @return A parsed BidPatternList
     */
    public static BidPatternList valueOf(InferenceRegistry reg, String str) {
        if (str == null) {
            return null;
        }
        ListParser<BidPattern> parser = new ListParser<BidPattern>(new BidPatternParser(reg), "");
        try (Input inp = new Input(new StringReader(str))) {
            List<BidPattern> l = parser.parse(inp);
            inp.advanceWhite();
            if (inp.ch != -1) {
                throw new IllegalArgumentException("invalid bids: '" + str + "'");
            }
            int numWild = 0;
            for (BidPattern patt : l) {
                if (patt.generality != null) {
                    numWild++;
                }
            }
            if (numWild > 1) {
                throw new IllegalArgumentException("Only one generality allowed");
            }
            if (l.size() > 0 && l.get(l.size() - 1).generality != null) {
                throw new IllegalArgumentException("bids may not end with a generality");
            }
            return new BidPatternList(l);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid bids: '" + str + "'", e);
        }
    }
    
    public BidPatternList withOpposingBidding() {
        if (bids.isEmpty()) {
            throw new IllegalStateException("List must be non-empty");
        }
        List<BidPattern> newBids = new ArrayList<>();
        BidPattern lastBid = getLastBid();
        if (lastBid.isOpposition) {
            throw new IllegalStateException("Last bid must be 'we'");
        }
        if (lastBid.generality != null) {
            throw new IllegalStateException("Last bid must not ne a wild card");
        }
        boolean isOpp = false;
        int i = bids.size() - 1;
        while (i >= 0) {
            // If we have a generality, then
            // whatever the next bid is fine.
            if (bids.get(i).generality != null) {
                newBids.add(0, bids.get(i--));
                if (i >= 0) {
                    isOpp = bids.get(i).isOpposition;
                }
            } else if (isOpp != bids.get(i).isOpposition) {
                // Not what we were expecting, then insert pass for opposition.
                newBids.add(0, BidPattern.PASS.withIsOpposition(isOpp));
                isOpp = !isOpp;
            } else {
                // Otherwise we have what we want.
                newBids.add(0, bids.get(i--));
                isOpp = !isOpp;
            }
        }
        return new BidPatternList(newBids);
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

    /**
     * @return the position of the wild card (or generality)
     */
    private int positionOfWild() {
        for (int i = 0; i < bids.size(); i++) {
            if (bids.get(i).generality != null) {
                return i;
            }
        }
        return -1;
    }

    private List<BidPatternListContext> resolveRemainingSymbols(BidPatternListContext ctx, List<BidPatternListContext> contexts, boolean isOpp) {
        // If we are already done then it is invalid to add any more bids
        if (ctx.bids.isCompleted() && !bids.isEmpty()) {
            return List.of();
        }

        // If no more bids then we are done
        if (bids.isEmpty()) {
            if (!isOpp) {
                throw new IllegalArgumentException("last bid must be made by 'we'");
            }
            return contexts;
        }

        BidPattern pattern = bids.get(0);

        List<BidPatternListContext> l = new ArrayList<>();
        for (BidPatternListContext newCtx : contexts) {
            if (pattern.generality != null) {
                l.addAll(resolveSymbols(newCtx));
            } else {
                l.addAll(resolveSymbols(newCtx));
            }
        }
        return l;
    }

    private int numInitialPasses() {
        for(int i = 0; i < bids.size(); i++) {
            if (!bids.get(i).isPass()) {
                return i;
            }
        }
        return bids.size();
    }
    
    private boolean isCompleted() {
        int sz = bids.size();
        return sz >= 4
                && bids.get(bids.size() - 1).isPass()
                && bids.get(bids.size() - 2).isPass()
                && bids.get(bids.size() - 3).isPass()
                && bids.get(bids.size() - 4).isPass();
    }
}
