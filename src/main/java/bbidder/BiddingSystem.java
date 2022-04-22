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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import bbidder.inferences.bound.AndBoundInf;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.OrBoundInf;

/**
 * Represents a compiled bidding system.
 */
public class BiddingSystem {
    private final List<BidInference> inferences;
    private final List<BiddingTest> tests;

    private BiddingSystem(List<BidInference> inferences, List<BiddingTest> tests) {
        super();
        this.inferences = inferences;
        this.tests = tests;
    }

    /**
     * Loads a bidding system from a url.
     * 
     * @param urlSpec
     *            The url specification
     * @param reportErrors
     *            A consumer or parse exceptions
     * @return The bidding system
     */
    public static BiddingSystem load(String urlSpec, Consumer<ParseException> reportErrors) {
        List<BidInference> inferences = new ArrayList<>();
        List<BiddingTest> tests = new ArrayList<>();
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        load("", urlSpec, reportErrors, inferences, tests, reg);
        return new BiddingSystem(inferences, tests);
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
            for (BidInference bi : inferences) {
                bw.write(bi.toString());
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
        List<PossibleBid> l = new ArrayList<>();
        for (BidInference i : inferences) {
            Bid match = i.bids.getMatch(bids, players);
            if (match != null) {
                l.add(new PossibleBid(i, match));
            }
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
        List<IBoundInference> negative = new ArrayList<>();
        List<PossibleBid> possible = getPossibleBids(bids.exceptLast(), players);
        for (PossibleBid i : possible) {
            IBoundInference inf = i.inf.inferences.bind(players);
            if (i.bid.equals(lastBid)) {
                positive.add(AndBoundInf.create(inf, OrBoundInf.create(negative).negate()));
            }
            negative.add(inf);
        }

        // Pass means... Nothing else works, this will get smarter.
        if (lastBid == Bid.P) {
            positive.add(OrBoundInf.create(negative).negate());
        }
        return OrBoundInf.create(positive);
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
    private static void load(String where, String urlSpec, Consumer<ParseException> reportErrors, List<BidInference> inferences,
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
    private static void load(String where, InputStream is, Consumer<ParseException> reportErrors, List<BidInference> inferences,
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
                        inferences.addAll(BidInference.valueOf(reg, ln).at(where + ":" + lineno).resolveSymbols());
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
}
