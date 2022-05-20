package bbidder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import bbidder.parsers.BidPatternParser;
import bbidder.parsers.Input;
import bbidder.parsers.ListParser;
import bbidder.utils.IteratorStream;
import bbidder.utils.MyStream;

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
        if (patt.simpleBid != null) {
            Contract contract = getContract();
            if (contract != null && !contract.isLegalBid(patt.simpleBid)) {
                throw new IllegalArgumentException("Invalid bid " + patt);
            }
        }
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
    public MyStream<Context> resolveSuits(SuitTable suits) {
        return new IteratorStream<>(withOpposingBidding().addInitialPasses()).flatMap(bpl -> bpl.resolveSuits(BidPatternList.EMPTY, suits));
    }

    /**
     * Given a bid pattern list where the suits have already been bound,
     * return the last bid of the pattern in the context of an auction.
     * 
     * @param bidding
     *            The auction to match.
     * @param players
     *            the players
     * @return The last bid. null if there was no match.
     */
    public Optional<TaggedBid> getMatch(TaggedAuction bidding, Players players) {
        List<TaggedBid> theBids = bidding.getBids();
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
                if (!pattern.generality.test(players, bidding)) {
                    return Optional.empty();
                }
                i += wildSize;
            } else {
                TaggedBid bid = theBids.get(i);
                Optional<TaggedBid> expected = pattern.resolveToBid(bidding.firstN(i), bid);
                boolean matches = expected.isPresent() && Objects.equals(bid, expected.get());
                if ((!matches) ^ pattern.antiMatch) {
                    return Optional.empty();
                }
                i++;
            }
        }
        BidPattern lastBid = bids.get(bids.size() - 1);
        return lastBid.resolveToBid(bidding, null).flatMap(nextBid -> {
            if (!bidding.isLegalBid(nextBid.bid)) {
                return Optional.empty();
            }
            return Optional.of(nextBid);
        });
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
     * @param str
     *            The String to parse
     * @return A parsed BidPatternList
     */
    public static BidPatternList valueOf(String str) {
        if (str == null) {
            return null;
        }
        ListParser<BidPattern> parser = new ListParser<BidPattern>(new BidPatternParser(), "");
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
    private MyStream<Context> resolveSuits(BidPatternList previous, SuitTable suitTable) {
        if (bids.isEmpty()) {
            return MyStream.of(previous.new Context(suitTable));
        }
        // For each context gotten from the first bid,
        // evaluate the remaining bids in the context of that bid.
        BidPatternList exceptFirst = exceptFirst();
        BidPattern first = bids.get(0);
        return first.resolveSuits(previous.getContract(), suitTable)
                .flatMap(b -> exceptFirst.resolveSuits(previous.withBidAdded(b.getBidPattern()), b.suitTable));
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

    public Contract getContract() {
        List<Bid> lastBids = new ArrayList<>();
        for (int i = bids.size() - 1; i >= 0; i--) {
            Bid bid = bids.get(i).simpleBid;
            if (bid == null) {
                return null;
            }
            lastBids.add(0, bid);
            if (bid.isSuitBid()) {
                return Auction.create(lastBids).getContract();
            }
        }
        return Auction.create(lastBids).getContract();
    }
}
