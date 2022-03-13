package bbidder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import bbidder.inferences.AndBoundInference;
import bbidder.inferences.ConstBoundInference;
import bbidder.inferences.OrBoundInference;

/**
 * Represents a compiled bidding system.
 */
public class BiddingSystem {
    public final List<BoundBidInference> inferences;
    public final List<BiddingTest> tests;

    public BiddingSystem(List<BoundBidInference> inferences, List<BiddingTest> tests) {
        super();
        this.inferences = inferences;
        this.tests = tests;
    }

    /**
     * Load a bidding system in from a urlSpec
     * 
     * @param where
     *            Where we are loading from
     * @param urlSpec
     *            The url spec
     * @param reportErrors
     *            The consumer of parse errors
     * @return The bidding system.
     */
    public static BiddingSystem load(String where, String urlSpec, Consumer<ParseException> reportErrors) {
        try (InputStream is = new URL(null, urlSpec, new Handler(BiddingSystem.class.getClassLoader())).openStream()) {
            return load(urlSpec, is, reportErrors);
        } catch (MalformedURLException e) {
            reportErrors.accept(new ParseException(where, e));
            return new BiddingSystem(List.of(), List.of());
        } catch (IOException e) {
            reportErrors.accept(new ParseException(where, e));
            return new BiddingSystem(List.of(), List.of());
        }
    }

    /**
     * Load a bidding system in from an input stream.
     * 
     * @param where
     *            Where we are loading from
     * @param is
     *            The input stream
     * @param reportErrors
     *            The consumer of parse errors
     * @return The bidding system.
     */
    public static BiddingSystem load(String where, InputStream is, Consumer<ParseException> reportErrors) {
        List<BoundBidInference> inferences = new ArrayList<>();
        List<BiddingTest> tests = new ArrayList<>();
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        int lineno = 0;
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            for (;;) {
                String ln = rd.readLine();
                if (ln == null) {
                    break;
                }
                lineno++;
                int pos = ln.indexOf('#');
                if (pos >= 0) {
                    ln = ln.substring(0, pos);
                }
                ln = ln.trim();
                String[] comm = ln.split("\\s+", 2);
                if (comm.length == 2 && comm[0].equals("include")) {
                    BiddingSystem bs = load(where, comm[1].trim(), reportErrors);
                    inferences.addAll(bs.inferences);
                    tests.addAll(bs.tests);
                } else if (comm.length == 2 && comm[0].equals("test")) {
                    try {
                        tests.add(BiddingTest.valueOf(comm[1]));
                    } catch (Exception e) {
                        reportErrors.accept(new ParseException(where + ":" + lineno, e));
                    }
                } else if (!ln.equals("")) {
                    try {
                        inferences.addAll(BidInference.valueOf(reg, ln).getBoundInferences());
                    } catch (Exception e) {
                        reportErrors.accept(new ParseException(where + ":" + lineno, e));
                    }
                }
            }
        } catch (IOException e) {
            reportErrors.accept(new ParseException(where, e));
        }
        return new BiddingSystem(inferences, tests);
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
            for (BoundBidInference bi : inferences) {
                bw.write(bi.toString());
            }
        }
    }

    /**
     * Retrieve the bid for a hand starting from the list of bids and likely hands for everyone.
     * 
     * @param bids
     *            The list of bids
     * @param likelyHands
     *            Likely hands fro everyone
     * @param hand
     *            The hand to evaluate
     * @return The right bid
     */
    public Bid getBid(BidList bids, LikelyHands likelyHands, Hand hand) {
        for (BoundBidInference i : inferences) {
            if (i.ctx.bids.bids.size() == bids.bids.size() + 1 && i.ctx.bids.exceptLast().equals(bids)) {
                IBoundInference newInference = i.bind(likelyHands);
                if (newInference.matches(hand)) {
                    return i.ctx.bids.getLastBid();
                }
            }
        }

        // For now always pass, this will get smarter.
        return Bid.P;
    }

    /**
     * Retrieves the inference from a list of bids according to the system.
     * 
     * @param bids
     *            The list of bids.
     * @param likelyHands
     *            The like hands for everyone so far.
     * @return The inference The inference from the bid.
     */
    public IBoundInference getInference(BidList bids, LikelyHands likelyHands) {
        IBoundInference positive = ConstBoundInference.create(false);
        IBoundInference negative = ConstBoundInference.create(true);
        for (BoundBidInference i : inferences) {
            if (i.ctx.bids.bids.size() == bids.bids.size() && i.ctx.bids.exceptLast().equals(bids.exceptLast())) {
                IBoundInference newInference = i.bind(likelyHands);
                if (i.ctx.bids.getLastBid().equals(bids.getLastBid())) {
                    positive = OrBoundInference.create(AndBoundInference.create(newInference, negative), positive);
                }
                negative = AndBoundInference.create(negative, newInference.negate());
            }
        }

        // Pass means... Nothing else works, this will get smarter.
        if (bids.bids.size() > 0) {
            Bid lastBid = bids.getLastBid();
            if (lastBid == Bid.P) {
                positive = OrBoundInference.create(negative, positive);
            }
        }
        return positive;
    }
}
