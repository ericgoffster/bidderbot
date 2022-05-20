package bbidder.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import bbidder.BidInference;
import bbidder.BiddingSystem;
import bbidder.BiddingTest;
import bbidder.ResolvedBidInference;
import bbidder.utils.ClassPathUrlHandler;
import bbidder.utils.SplitUtil;

/**
 * Parses a bidding system.
 * 
 * @author goffster
 *
 */
public final class BiddingSystemParser {

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
        List<ResolvedBidInference> inferences = new ArrayList<>();
        List<BiddingTest> tests = new ArrayList<>();
        BiddingSystemParser.load("", urlSpec, reportErrors, inferences::add, tests::add);
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
    private static void load(String where, String urlSpec, Consumer<ParseException> reportErrors, Consumer<ResolvedBidInference> inferences,
            Consumer<BiddingTest> tests) {
        try (InputStream is = new URL(null, urlSpec, new ClassPathUrlHandler(BiddingSystem.class.getClassLoader())).openStream()) {
            load(urlSpec, is, reportErrors, inferences, tests);
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
    private static void load(String where, InputStream is, Consumer<ParseException> reportErrors, Consumer<ResolvedBidInference> inferences,
            Consumer<BiddingTest> tests) {
        int lineno = 0;
        BidInference last = null;
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
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
                sb.append(" " + ln);
                ln = sb.toString().trim();
                if (ln.endsWith(";")) {
                    ln = ln.substring(0, ln.length() - 1);
                    String[] comm = SplitUtil.split(ln, "\\s+", 2);
                    if (comm.length == 2 && comm[0].equalsIgnoreCase("include")) {
                        load(where, resolveUrlSpec(where, comm[1]), reportErrors, inferences, tests);
                    } else {
                        String here = where + ":" + lineno;
                        if (comm.length == 2 && comm[0].equalsIgnoreCase("test")) {
                            try {
                                tests.accept(BiddingTest.valueOf(last, false, here, comm[1]));
                            } catch (Exception e) {
                                reportErrors.accept(new ParseException(here, e));
                            }
                        } else if (comm.length == 2 && comm[0].equalsIgnoreCase("anti_test")) {
                            try {
                                tests.accept(BiddingTest.valueOf(last, true, here, comm[1]));
                            } catch (Exception e) {
                                reportErrors.accept(new ParseException(here, e));
                            }
                        } else if (!ln.equals("")) {
                            try {
                                BidInference unresolved = BidInference.valueOf(here, ln);
                                List<BidInference> resolved = new ArrayList<>();
                                unresolved.resolveSuits().forEach(resolved::add);
                                last = unresolved;
                                inferences.accept(new ResolvedBidInference(unresolved, resolved));
                            } catch (Exception e) {
                                reportErrors.accept(new ParseException(here, e));
                            }
                        }
                    }
                    sb.setLength(0);
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