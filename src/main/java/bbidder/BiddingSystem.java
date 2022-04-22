package bbidder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.OrBoundInf;

/**
 * Represents a compiled bidding system.
 */
public class BiddingSystem {
    private final List<BidInference> inferences;
    private final List<BiddingTest> tests;

    public BiddingSystem(List<BidInference> inferences, List<BiddingTest> tests) {
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
            String last = null;
            for (BidInference bi : inferences) {
                if (last == null || !last.equals(bi.where)) {
                    bw.write(bi.where + ":\n");
                    last = bi.where;
                }
                bw.write("    " + bi + "\n");
            }
        }
    }

    /**
     * @param bids
     *            The list of bids
     * @param players
     *            The players
     * @return A list of all possible bids given the list of bids.
     */
    public List<PossibleBid> getPossibleBids(BidList bids, Players players) {
        DebugUtils.breakpointGetPossibleBid(bids, players);
        List<PossibleBid> l = new ArrayList<>();
        for (BidInference i : inferences) {
            Bid match = i.bids.getMatch(bids, players);
            if (match != null) {
                DebugUtils.breakpointGetPossibleBid(bids, players, match, i);
                l.add(new PossibleBid(i, match));
            }
        }
        if (l.isEmpty()) {
            DebugUtils.breakpointGetPossibleBid(bids, players, l);
        }
        return l;
    }

    /**
     * Retrieve the bid for a hand starting from the list of bids and likely hands for everyone.
     * 
     * @param bids
     *            The list of bids
     * @param players
     *            Likely hands fro everyone
     * @param hand
     *            The hand to evaluate
     * @return The right bid
     */
    public BidSource getBid(BidList bids, Players players, Hand hand) {
        List<PossibleBid> possible = getPossibleBids(bids, players);
        for (PossibleBid i : possible) {
            IBoundInference inf = i.inf.inferences.bind(players);
            if (inf.matches(hand)) {
                return new BidSource(i, possible);
            }
        }

        // For now always pass, this will get smarter.
        return new BidSource(new PossibleBid(null, Bid.P), possible);
    }

    /**
     * Retrieves the inference from a list of bids according to the system.
     * 
     * @param bids
     *            The list of bids.
     * @param players
     *            The like hands for everyone so far.
     * @return The inference The inference from the bid.
     */
    public IBoundInference getInference(BidList bids, Players players) {
        if (bids.getBids().size() == 0) {
            return ConstBoundInference.create(false);
        }
        Bid lastBid = bids.getLastBid();
        List<IBoundInference> positive = new ArrayList<>();
        List<IBoundInference> positiveWild = new ArrayList<>();
        List<IBoundInference> negative = new ArrayList<>();
        List<IBoundInference> negativeWild = new ArrayList<>();
        List<PossibleBid> possible = getPossibleBids(bids.exceptLast(), players);
        for (PossibleBid i : possible) {
            IBoundInference inf = i.inf.inferences.bind(players);
            if (i.bid.equals(lastBid)) {
                if (i.inf.bids.positionOfWild() < 0) {
                    positive.add(AndBoundInf.create(inf, OrBoundInf.create(negative).negate()));
                } else {
                    positiveWild.add(AndBoundInf.create(inf, OrBoundInf.create(negativeWild).negate()));
                }
            }
            if (i.inf.bids.positionOfWild() < 0) {
                negative.add(inf);
            } else {
                negativeWild.add(inf);
            }
        }
        
        if (positive.isEmpty() && positiveWild.isEmpty() && lastBid != Bid.P) {
            throw new RuntimeException("Unrecognized bidding: " + bids);
        }

        // Pass means... Nothing else works, this will get smarter.
        if (lastBid == Bid.P) {
            positiveWild.add(OrBoundInf.create(negativeWild).negate());
            positive.add(OrBoundInf.create(negative).negate());
        }
        if (positive.size() > 0) {
            return OrBoundInf.create(positive);
        }
        return OrBoundInf.create(positiveWild);
    }
}
