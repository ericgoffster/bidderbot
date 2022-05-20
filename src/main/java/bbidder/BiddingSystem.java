package bbidder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.OrBoundInf;

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
     * @param auction
     *            The list of bids
     * @param players
     *            The players
     * @return A list of all possible bids given the list of bids.
     */
    public List<PossibleBid> getPossibleBids(TaggedAuction auction, Players players) {
        List<PossibleBid> l = new ArrayList<>();
        Map<Bid, TaggedBid> matched = new HashMap<>();
        inferences.forEach(rsbi -> {
            List<PossibleBid> tmp = new ArrayList<>();
            rsbi.inferences.forEach(i -> {
                i.bids.getMatch(auction, players).ifPresent(match -> {
                    TaggedBid tb = matched.get(match.bid);
                    if (tb == null || tb.equals(match)) {
                        tmp.add(new PossibleBid(i, match));
                        matched.put(match.bid, match);
                    }
                });
            });
            BidPattern p = rsbi.unresolved.bids.getLastBid();
            Comparator<Bid> priority = p.getBidPriority();
            tmp.sort((a, b) -> priority.compare(a.bid.bid, b.bid.bid));
            l.addAll(tmp);
        });
        return l;
    }

    /**
     * Retrieve the bid for a hand starting from the list of bids and a summary from everyone.
     * 
     * @param auction
     *            The auction
     * @param players
     *            Likely hands fro everyone
     * @param hand
     *            The hand to evaluate
     * @return The right bid
     */
    public BidSource getBid(TaggedAuction auction, Players players, Hand hand) {
        List<PossibleBid> possible = getPossibleBids(auction, players);
        return possible.stream()
                .filter(i -> i.inf.inferences.bind(players).test(hand))
                .findFirst()
                .map(i -> new BidSource(i, possible))
                .orElse(new BidSource(new PossibleBid(null, new TaggedBid(Bid.P, TagSet.EMPTY.addTag("nobid"))), possible));
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
     * @return The inference from the bid.
     */
    public TaggedBoundInference getInference(TaggedAuction auction, Players players, Bid lastBid) {
        IBoundInference positive = ConstBoundInference.F;
        IBoundInference negative = ConstBoundInference.F;
        List<PossibleBid> possible = getPossibleBids(auction, players);
        TagSet tags = null;
        for (var i : possible) {
            IBoundInference inf = i.inf.inferences.bind(players);
            if (i.bid.bid.equals(lastBid)) {
                positive = OrBoundInf.create(positive, AndBoundInf.create(inf, negative.negate()));
                if (tags == null) {
                    tags = i.bid.tags;
                } else {
                    tags = tags.and(i.bid.tags);
                }
            }
            negative = OrBoundInf.create(negative, inf);
        }

        if (positive == ConstBoundInference.F && lastBid != Bid.P) {
            throw new RuntimeException(lastBid + ": Unrecognized bid for: " + auction);
        }

        if (tags == null) {
            tags = TagSet.EMPTY;
        }

        // Pass means... Nothing else works, this will get smarter.
        if (lastBid == Bid.P) {
            return new TaggedBoundInference(OrBoundInf.create(positive, negative.negate()), tags);
        }
        return new TaggedBoundInference(positive, tags);
    }
}
