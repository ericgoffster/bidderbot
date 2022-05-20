package bbidder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.OrBoundInf;
import bbidder.utils.DebugUtils;

/**
 * Represents a compiled bidding system.
 */
public final class BiddingSystem {
    private final List<ResolvedBidInference> inferences;
    private final List<BiddingTest> tests;

    public BiddingSystem(List<ResolvedBidInference> inferences, List<BiddingTest> tests) {
        super();
        this.inferences = inferences;
        this.tests = tests;
    }

    /**
     * @return The tests for this bidding system
     */
    public List<BiddingTest> getTests() {
        return Collections.unmodifiableList(tests);
    }

    /**
     * Dump the bidding system
     * 
     * @param os
     *            Where to dump it.
     * @throws IOException
     *             an I/O error occurred.
     */
    public void dump(OutputStream os) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            for (ResolvedBidInference bi : inferences) {
                bw.write(bi.unresolved.description + ":\n");
                bw.write(bi.unresolved.where + ":\n");
                for (BidInference inf : bi.inferences) {
                    bw.write("    " + inf + "\n");
                }
            }
        }
    }

    /**
     * @param bidding
     *            The list of bids
     * @param players
     *            The players
     * @return A list of all possible bids given the list of bids.
     */
    public List<PossibleBid> getPossibleBids(TaggedAuction bidding, Players players) {
        DebugUtils.breakpointGetPossibleBid(bidding, players);
        List<PossibleBid> l = new ArrayList<>();
        Set<Bid> matched = new HashSet<>();
        inferences.forEach(rsbi -> {
            List<PossibleBid> tmp = new ArrayList<>();
            rsbi.inferences.forEach(i -> {
                i.bids.getMatch(bidding, players, matched).ifPresent(match -> {
                    DebugUtils.breakpointGetPossibleBid(bidding, players, match, i);
                    tmp.add(new PossibleBid(i, match));
                    matched.add(match.bid);
                });
            });
            BidPattern p = rsbi.unresolved.bids.getLastBid();
            if (p.symbol != null && p.symbol.downTheLine()) {
                tmp.sort((a, b) -> {
                    int cmp = Integer.compare(a.bid.bid.level, b.bid.bid.level);
                    if (cmp != 0) {
                        return cmp;
                    }
                    return -Integer.compare(a.bid.bid.strain, b.bid.bid.strain);
                });
            } else {
                tmp.sort((a, b) -> a.bid.bid.compareTo(b.bid.bid));
            }
            l.addAll(tmp);
        });
        if (l.isEmpty()) {
            DebugUtils.breakpointGetPossibleBid(bidding, players, l);
        }
        return l;
    }

    /**
     * Retrieve the bid for a hand starting from the list of bids and likely hands for everyone.
     * 
     * @param bidding
     *            The auction
     * @param players
     *            Likely hands fro everyone
     * @param hand
     *            The hand to evaluate
     * @return The right bid
     */
    public BidSource getBid(TaggedAuction bidding, Players players, Hand hand) {
        List<PossibleBid> possible = getPossibleBids(bidding, players);
        return possible.stream()
                .filter(i -> i.inf.inferences.bind(players).test(hand))
                .findFirst()
                .map(i -> new BidSource(i, possible))
                .orElse(new BidSource(new PossibleBid(null, new TaggedBid(Bid.P, Set.of("nobid"))), possible));
    }

    /**
     * Retrieves the inference from a list of bids according to the system.
     * 
     * @param auction
     *            The auction.
     * @param players
     *            The like hands for everyone so far.
     * @param lastBid 
     *            The last bid
     * @return The inference The inference from the bid.
     */
    public TaggedBoundInference getInference(TaggedAuction auction, Players players, Bid lastBid) {
        IBoundInference positive = ConstBoundInference.F;
        IBoundInference negative = ConstBoundInference.F;
        List<PossibleBid> possible = getPossibleBids(auction, players);
        Set<String> tags = null;
        for (var i : possible) {
            IBoundInference inf = i.inf.inferences.bind(players);
            if (i.bid.bid.equals(lastBid)) {
                positive = OrBoundInf.create(positive, AndBoundInf.create(inf, negative.negate()));
                if (tags == null) {
                    tags = new HashSet<>(i.bid.tags);
                } else {
                    tags.retainAll(i.bid.tags);
                }
            }
            negative = OrBoundInf.create(negative, inf);
        }

        if (positive == ConstBoundInference.F && lastBid != Bid.P) {
            throw new RuntimeException(lastBid + ": Unrecognized bid for: " + auction);
        }
        
        if (tags == null) {
            tags = Set.of();
        }

        // Pass means... Nothing else works, this will get smarter.
        if (lastBid == Bid.P) {
            return new TaggedBoundInference(OrBoundInf.create(positive, negative.negate()), tags);
        }
        return new TaggedBoundInference(positive, tags);
    }
}
