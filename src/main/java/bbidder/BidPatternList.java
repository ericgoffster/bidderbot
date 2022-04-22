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
        // If there are no bids, then we are done
        if (bids.isEmpty()) {
            return List.of(new BidPatternListContext(BidPatternList.EMPTY, suits));
        }
        BidPattern pattern = bids.get(0);

        // If the first bid is a generality, then the generality
        // takes care of all chairs
        if (pattern.generality != null) {
            return resolve(new BidPatternListContext(BidPatternList.EMPTY, suits), bids.get(1).isOpposition);
        }

        boolean isOpp = pattern.isOpposition;

        // generate contexts for {[bid], [(P) bid], [P (P) bids], [(P) P (P) bids]}
        List<BidPatternListContext> list = new ArrayList<>();
        List<BidPattern> passes = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            list.addAll(resolve(new BidPatternListContext(new BidPatternList(passes), suits), !isOpp));
            passes.add(0, BidPattern.PASS.withIsOpposition((i % 2 == 0) ^ isOpp));
        }
        return list;
    }
    
    public List<BidPatternListContext> resolve(BidPatternListContext ctx, boolean isOpp) {
        BidPattern pattern = bids.get(0);
        BidPatternList exceptFirst = exceptFirst();
        return exceptFirst.resolveRemainingSymbols(ctx, resolveSymbols(pattern, ctx), isOpp);
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

        if (pattern.generality != null) {
            boolean newIsOpp = bids.get(1).isOpposition;
            List<BidPatternListContext> l = new ArrayList<>();
            for (BidPatternListContext newCtx : contexts) {
                l.addAll(resolve(newCtx, newIsOpp));
            }
            return l;
        }
        if (isOpp && !pattern.isOpposition) {
            BidPattern pass = BidPattern.PASS.withIsOpposition(isOpp);
            List<BidPatternListContext> l = new ArrayList<>();
            for (BidPatternListContext newCtx : contexts) {
                l.addAll(resolveRemainingSymbols(newCtx, resolveSymbols(pass, newCtx), !isOpp));
            }
            return l;
        }
        List<BidPatternListContext> l = new ArrayList<>();
        for (BidPatternListContext newCtx : contexts) {
            l.addAll(resolve(newCtx, !isOpp));
        }
        return l;
    }

    private boolean isCompleted() {
        return bids.size() >= 4 && bids.get(bids.size() - 1).equals(BidPattern.PASS) && bids.get(bids.size() - 2).equals(BidPattern.PASS)
                && bids.get(bids.size() - 3).equals(BidPattern.PASS) && bids.get(bids.size() - 4).equals(BidPattern.PASS);
    }
}
