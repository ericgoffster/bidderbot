package bbidder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import bbidder.utils.ListUtil;

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
     * @return true if PASS can be added to beginning.
     */
    public boolean canPrependPass() {
        return numInitialPasses() < 3 || numInitialPasses() == 3 && bids.size() == 3;
    }

    /**
     * @return A new bid pattern list with PASS added to the beginning.
     */
    public BidPatternList withPassPrepended() {
        List<BidPattern> l = new ArrayList<>(bids);
        l.add(0, BidPattern.PASS.withIsOpposition(!bids.get(0).isOpposition));
        return new BidPatternList(l);
    }

    /**
     * Creates bidding sequences in the first, second, third or fourth chair.
     * 
     * @return List of sequences with 0-3 passes added to the beginning
     */
    public List<BidPatternList> addInitialPasses() {
        // Generalities already take care of initial passes
        if (bids.get(0).generality != null) {
            return List.of(this);
        } else {
            List<BidPatternList> list = new ArrayList<>();
            BidPatternList prev = this;
            list.add(prev);
            // Add up to 3 passes
            for (int i = 0; i < 3; i++) {
                if (!prev.canPrependPass()) {
                    break;
                }
                prev = prev.withPassPrepended();
                list.add(prev);
            }
            return list;
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
    public List<Context> resolveSymbols(SymbolTable suits) {
        return ListUtil.flatMap(withOpposingBidding().addInitialPasses(), bpl -> bpl.resolveSymbols(BidPatternList.EMPTY, suits));
    }

    /**
     * Given a bid pattern list where the suits have already been bound,
     * return the last bid of the pattern in the context of an auction.
     * 
     * @param auction
     *            The auction to match.
     * @param players
     *            the players
     * @param matched
     *            the set of matched bids
     * @return The last bid. null if there was no match.
     */
    public Bid getMatch(Auction auction, Players players, Set<Bid> matched) {
        List<Bid> theBids = auction.getBids();
        int wildSize = theBids.size() - bids.size() + 2;
        int wildPos = positionOfWild();
        if (wildPos < 0 && theBids.size() != bids.size() - 1) {
            return null;
        }
        if (wildPos >= 0 && wildSize < 0) {
            return null;
        }
        int i = 0;
        for (BidPattern pattern : bids.subList(0, bids.size() - 1)) {
            if (pattern.generality != null) {
                if (!pattern.generality.test(players, auction)) {
                    return null;
                }
                i += wildSize;
            } else {
                Bid bid = theBids.get(i);
                Bid expected = pattern.resolveToBid(auction.firstN(i));
                if (bid != expected) {
                    return null;
                }
                i++;
            }
        }
        BidPattern lastPatt = bids.get(bids.size() - 1);
        Bid theNextBid = lastPatt.resolveToBid(auction);
        if (theNextBid == null || !auction.isLegalBid(theNextBid)) {
            return null;
        }
        if (matched.contains(theNextBid) && lastPatt.isNonConventional) {
            return null;
        }
        return theNextBid;
    }

    /**
     * Given unopposed bidding sequences, will insert
     * passes where the opposing bidding is missing.
     * 
     * @return The new bid pattern list with passes inserted in the middle.
     */
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
     * Resolve the symbols starting from a previous context
     * 
     * @param ctx
     *            The previous context.
     * @return The list of resolved bidding pattern contexts
     */
    private List<Context> resolveSymbols(BidPatternList previous, SymbolTable symbols) {
        if (bids.isEmpty()) {
            return List.of(previous.new Context(symbols));
        }
        // For each context gotten from the first bid,
        // evaluate the remaining bids in the context of that bid.
        BidPatternList exceptFirst = exceptFirst();
        BidPattern pattern = bids.get(0);
        return ListUtil.flatMap(pattern.resolveSymbols(previous.getContract(), symbols),
                b -> exceptFirst.resolveSymbols(previous.withBidAdded(b.getBidPattern()), b.symbols));
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

    /**
     * @return The number of initial passes.
     */
    private int numInitialPasses() {
        for (int i = 0; i < bids.size(); i++) {
            if (!bids.get(i).isPass()) {
                return i;
            }
        }
        return bids.size();
    }

    public final class Context {
        public final SymbolTable symbols;

        public Context(SymbolTable symbols) {
            super();
            this.symbols = symbols;
        }

        public BidPatternList getBids() {
            return BidPatternList.this;
        }
    }

    public Contract getContract() {
        boolean redoubled = false;
        boolean doubled = false;
        int numPasses = 0;
        for (int i = bids.size() - 1; i >= 0; i--) {
            Bid bid = bids.get(i).simpleBid;
            if (bid == null) {
                return null;
            }
            if (bid.isSuitBid()) {
                return new Contract(i, bid, doubled, redoubled, numPasses);
            }
            if (bid == Bid.X) {
                doubled = true;
            }
            if (bid == Bid.XX) {
                redoubled = true;
            }
            if (bid == Bid.P) {
                if (!doubled && !redoubled) {
                    numPasses++;
                }
            }
        }
        return new Contract(0, Bid.P, false, false, numPasses);

    }
}
