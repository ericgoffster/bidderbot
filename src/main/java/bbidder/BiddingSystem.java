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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.OrBoundInf;

/**
 * Represents a compiled bidding system.
 */
public class BiddingSystem {
    public final List<BoundBidInference> inferences;
    public final List<BiddingTest> tests;
    public final Map<BidList, List<BoundBidInference>> byPrefix = new LinkedHashMap<>();

    public BiddingSystem(List<BoundBidInference> inferences, List<BiddingTest> tests) {
        super();
        this.inferences = inferences;
        this.tests = tests;
        for (BoundBidInference i : inferences) {
            BidList bids = i.ctx.bids.exceptLast();
            List<BoundBidInference> l = byPrefix.get(bids);
            if (l == null) {
                l = new ArrayList<>();
                byPrefix.put(bids, l);
            }
            l.add(i);
        }
    }

    public static BiddingSystem load(String urlSpec, Consumer<ParseException> reportErrors) {
        List<BoundBidInference> inferences = new ArrayList<>();
        List<BiddingTest> tests = new ArrayList<>();
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        load("", urlSpec, reportErrors, inferences, tests, reg);
        return new BiddingSystem(inferences, tests);
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
     */
    private static void load(String where, String urlSpec, Consumer<ParseException> reportErrors, List<BoundBidInference> inferences,
            List<BiddingTest> tests, InferenceRegistry reg) {
        try (InputStream is = new URL(null, urlSpec, new Handler(BiddingSystem.class.getClassLoader())).openStream()) {
            load(urlSpec, is, reportErrors, inferences, tests, reg);
        } catch (MalformedURLException e) {
            reportErrors.accept(new ParseException(where, e));
        } catch (IOException e) {
            reportErrors.accept(new ParseException(where, e));
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
     */
    private static void load(String where, InputStream is, Consumer<ParseException> reportErrors, List<BoundBidInference> inferences,
            List<BiddingTest> tests, InferenceRegistry reg) {
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
                String[] comm = SplitUtil.split(ln, "\\s+", 2);
                if (comm.length == 2 && comm[0].equalsIgnoreCase("include")) {
                    load(where, resolveUrlSpec(where, comm[1]), reportErrors, inferences, tests, reg);
                } else if (comm.length == 2 && comm[0].equalsIgnoreCase("test")) {
                    try {
                        tests.add(BiddingTest.valueOf(where + ":" + lineno, comm[1]));
                    } catch (Exception e) {
                        reportErrors.accept(new ParseException(where + ":" + lineno, e));
                    }
                } else if (!ln.equals("")) {
                    try {
                        inferences.addAll(BidInference.valueOf(reg, ln).getBoundInferences(where + ":" + lineno, new LikelyHands()));
                    } catch (Exception e) {
                        reportErrors.accept(new ParseException(where + ":" + lineno, e));
                    }
                }
            }
        } catch (IOException e) {
            reportErrors.accept(new ParseException(where, e));
        }
    }

    private static String resolveUrlSpec(String where, String urlSpec) {
        if (!urlSpec.startsWith("/") && !urlSpec.contains(":")) {
            int ps = where.lastIndexOf("/");
            if (ps >= 0) {
                return where.substring(0, ps + 1) + urlSpec;
            }
            ps = where.lastIndexOf(":");
            if (ps >= 0) {
                return where.substring(0, ps + 1) + urlSpec;
            }
        }
        return urlSpec;
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
    public BidSource getBid(BidList bids, LikelyHands likelyHands, Hand hand) {
        List<BoundBidInference> possible = byPrefix.getOrDefault(bids, new ArrayList<>());
        for (BoundBidInference i : possible) {
            IBoundInference newInference = i.bind(likelyHands);
            if (newInference.matches(hand)) {
                return new BidSource(i, i.ctx.bids.getLastBid(), possible);
            }
        }

        // For now always pass, this will get smarter.
        return new BidSource(null, Bid.P, possible);
    }

    public static class BidSource {
        public final Bid bid;
        public final List<BoundBidInference> possible;
        public final BoundBidInference inference;

        public BidSource(BoundBidInference newInference, Bid bid, List<BoundBidInference> possible) {
            super();
            this.bid = bid;
            this.possible = possible;
            this.inference = newInference;
        }

        @Override
        public String toString() {
            return inference.toString();
        }
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
        if (bids.bids.size() == 0) {
            return ConstBoundInference.create(false);
        }
        Bid lastBid = bids.getLastBid();
        List<IBoundInference> positive = new ArrayList<>();
        List<IBoundInference> negative = new ArrayList<>();
        for (BoundBidInference i : byPrefix.getOrDefault(bids.exceptLast(), new ArrayList<>())) {
            IBoundInference newInference = i.bind(likelyHands);
            if (i.ctx.bids.getLastBid().equals(lastBid)) {
                positive.add(AndBoundInf.create(newInference, OrBoundInf.create(negative).negate()));
            }
            negative.add(newInference);
        }

        // Pass means... Nothing else works, this will get smarter.
        if (lastBid == Bid.P) {
            positive.add(OrBoundInf.create(negative).negate());
        }
        return OrBoundInf.create(positive);
    }
}
