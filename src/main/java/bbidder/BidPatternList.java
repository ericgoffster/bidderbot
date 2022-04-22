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
     * @param pattern pattern
     * @param bc
     *            The bidding context
     * @return A list of contexts representing the symbol bound to actual values
     */
    public List<BidPatternListContext> resolveSymbols(BidPattern pattern, BidPatternListContext bc) {
        List<BidPatternListContext> l = new ArrayList<>();
        for(BidPatternContext b: pattern.resolveSymbols(bc.suits)) {
            l.add(new BidPatternListContext(bc.bids.withBidAdded(b.bid), b.suits));
        }
        return l;
    }
    
    /**
     * Resolve the first symbol.
     * Allow for first, second, third or fourth chair openings
     * @param suits suits
     * 
     * @return The list of resolved bidding contexts
     */
    public List<BidPatternListContext> resolveFirstSymbol(SymbolTable suits) {
        // If there are no bids, then we are done
        BidPatternListContext empty = new BidPatternListContext(BidPatternList.EMPTY, suits);
        if (bids.isEmpty()) {
            return List.of(empty);
        }
        BidPattern pattern = bids.get(0);
        BidPatternList exceptFirst = exceptFirst();

        // If the first bid is a generality, then the generality
        // takes care of all chairs
        if (pattern.generality != null) {
            BidPatternListContext ctx = empty;
            return exceptFirst.resolveSymbols(ctx, resolveSymbols(pattern, ctx), bids.get(1).isOpposition);
        }

        // generate contexts for {[bid], [(P) bid], [P (P) bids], [(P) P (P) bids]}
        boolean isOpp = pattern.isOpposition;
        BidPattern p1 = BidPattern.PASS.withIsOpposition(!isOpp);
        BidPattern p2 = BidPattern.PASS.withIsOpposition(isOpp);
        List<BidPatternListContext> list = new ArrayList<>();
        BidPatternListContext ctx = empty;
        list.addAll(exceptFirst.resolveSymbols(ctx, resolveSymbols(pattern, ctx), !isOpp));
        BidPatternListContext ctx1 = new BidPatternListContext(BidPatternList.EMPTY.withBidAdded(p1), suits);
        list.addAll(exceptFirst.resolveSymbols(ctx1, resolveSymbols(pattern, ctx1), !isOpp));
        BidPatternListContext ctx2 = new BidPatternListContext(BidPatternList.EMPTY.withBidAdded(p2).withBidAdded(p1), suits);
        list.addAll(exceptFirst.resolveSymbols(ctx2, resolveSymbols(pattern, ctx2), !isOpp));
        BidPatternListContext ctx3 = new BidPatternListContext(BidPatternList.EMPTY.withBidAdded(p1).withBidAdded(p2).withBidAdded(p1), suits);
        list.addAll(exceptFirst.resolveSymbols(ctx3, resolveSymbols(pattern, ctx3), !isOpp));
        return list;
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
            int numWild = 0 ;
            for(BidPattern patt: l) {
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

    private List<BidPatternListContext> resolveSymbols(BidPatternListContext ctx, List<BidPatternListContext> contexts, boolean isOpp) {
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
        
        if (pattern.generality != null) {
            boolean newIsOpp = bids.get(1).isOpposition;
            List<BidPatternListContext> l = new ArrayList<>();
            for (BidPatternListContext newCtx : contexts) {
                l.addAll(exceptFirst().resolveSymbols(newCtx, resolveSymbols(pattern, newCtx), newIsOpp));
            }
            return l;
        }
        if (isOpp && !pattern.isOpposition) {
            BidPattern pass = BidPattern.PASS.withIsOpposition(isOpp);
            List<BidPatternListContext> l = new ArrayList<>();
            for (BidPatternListContext newCtx : contexts) {
                l.addAll(resolveSymbols(newCtx, resolveSymbols(pass, newCtx), !isOpp));
            }
            return l;
        }
        List<BidPatternListContext> l = new ArrayList<>();
        for (BidPatternListContext newCtx : contexts) {
            l.addAll(exceptFirst().resolveSymbols(newCtx, resolveSymbols(pattern, newCtx), !isOpp));
        }
        return l;
    }

    private boolean isCompleted() {
        return bids.size() >= 4 && bids.get(bids.size() - 1).equals(BidPattern.PASS) && bids.get(bids.size() - 2).equals(BidPattern.PASS)
                && bids.get(bids.size() - 3).equals(BidPattern.PASS) && bids.get(bids.size() - 4).equals(BidPattern.PASS);
    }

    /**
     * @return the position of the wild card (or generality)
     */
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
     * @param matched 
     *            the set of matched bids
     * @return The last bid. null if there was no match.
     */
    public Bid getMatch(BidList bidList, Players players, Set<Bid> matched) {
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
}
