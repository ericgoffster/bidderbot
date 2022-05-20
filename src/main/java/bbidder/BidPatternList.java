package bbidder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import bbidder.utils.IteratorStream;
import bbidder.utils.MyStream;

/**
 * Represents the bid patterns as read in from the notes.
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
     * Resolve the suit symbols for all of the bids in the list.
     * Allow for first, second, third or fourth chair openings
     * 
     * @param suits
     *            suits
     * 
     * @return The stream of resolved bidding pattern contexts
     */
    public MyStream<Context> resolveSuits(SuitTable suits) {
        return new IteratorStream<>(withOpposingBidding().addInitialPasses()).flatMap(bpl -> bpl.resolveSuits(BidPatternList.EMPTY, suits));
    }

    /**
     * Returns a match for the last bid in sequence
     * for the given auction.
     * 
     * @param auction
     *            The auction to match.
     * @param players
     *            the players
     * @return The last bid.
     */
    public Optional<TaggedBid> getMatch(TaggedAuction auction, Players players) {
        List<TaggedBid> theBids = auction.getBids();
        int wildSize = theBids.size() - bids.size() + 2;
        int wildPos = positionOfWild();
        if (wildPos < 0 && theBids.size() != bids.size() - 1) {
            return Optional.empty();
        }
        if (wildPos >= 0 && wildSize < 0) {
            return Optional.empty();
        }
        int i = 0;
        for (BidPattern pattern : bids.subList(0, bids.size() - 1)) {
            if (pattern.generality != null) {
                if (!pattern.generality.test(players, auction)) {
                    return Optional.empty();
                }
                i += wildSize;
            } else {
                TaggedBid bid = theBids.get(i);
                Contract subContract = auction.firstN(i).getContract();
                TaggedBid newBid = pattern.resolveLevel(subContract, bid.bid).getResolvedBid();
                if ((!Objects.equals(bid, newBid)) ^ pattern.antiMatch) {
                    return Optional.empty();
                }
                i++;
            }
            if (i > 0) {
                TaggedBid bid = theBids.get(i - 1);
                Contract subContract = auction.firstN(i - 1).getContract();
                if (!pattern.isBidCompatible(subContract, bid.bid)) {
                    return Optional.empty();
                }
            }
        }
        BidPattern pattern = bids.get(bids.size() - 1);
        if (pattern.generality != null) {
            throw new IllegalArgumentException("generality not permitted in this context");
        }
        Contract contract = auction.getContract();
        TaggedBid newBid = pattern.resolveLevel(contract, null).getResolvedBid();
        if (!pattern.isBidCompatible(contract, newBid.bid)) {
            return Optional.empty();
        }
        return Optional.of(newBid);
    }

    /**
     * Given an unopposed bidding sequencs, will insert
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
    private MyStream<Context> resolveSuits(BidPatternList previous, SuitTable suitTable) {
        if (bids.isEmpty()) {
            return MyStream.of(previous.new Context(suitTable));
        }
        // For each context gotten from the first bid,
        // evaluate the remaining bids in the context of that bid.
        BidPatternList exceptFirst = exceptFirst();
        BidPattern first = bids.get(0);
        return first.resolveSuits(suitTable).flatMap(b -> exceptFirst.resolveSuits(previous.withBidAdded(b.getBidPattern()), b.suitTable));
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
        public final SuitTable suitTable;

        public Context(SuitTable suitTable) {
            super();
            this.suitTable = suitTable;
        }

        public BidPatternList getBids() {
            return BidPatternList.this;
        }
    }
}
