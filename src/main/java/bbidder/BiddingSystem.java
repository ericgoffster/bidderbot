package bbidder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import bbidder.inferences.AndBoundInference;
import bbidder.inferences.ConstBoundInference;
import bbidder.inferences.NotBoundInference;
import bbidder.inferences.OrBoundInference;

/**
 * Represents a compiled bidding system.
 */
public class BiddingSystem {
    public final List<BoundBidInference> inferences;

    public BiddingSystem(List<BoundBidInference> inferences) {
        super();
        this.inferences = inferences;
    }

    public static BiddingSystem load(String spec) throws IOException {
        try (InputStream is = new URL(null, spec, new Handler(BiddingSystem.class.getClassLoader())).openStream()) {
            return load(is);
        }
    }

    public static BiddingSystem load(InputStream is) throws IOException {
        List<BoundBidInference> inferences = new ArrayList<>();
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            for (;;) {
                String ln = rd.readLine();
                if (ln == null) {
                    break;
                }
                int pos = ln.indexOf('#');
                if (pos >= 0) {
                    ln = ln.substring(0, pos);
                }
                ln = ln.trim();
                if (ln.startsWith("include")) {
                    inferences.addAll(load(ln.substring(7).trim()).inferences);
                } else if (!ln.equals("")) {
                    inferences.addAll(BidInference.valueOf(reg, ln).getBoundInference());
                }
            }
        }
        return new BiddingSystem(inferences);
    }

    public void spew(OutputStream os) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            for (BoundBidInference bi : inferences) {
                bw.write(bi.toString());
            }
        }
    }

    public Bid getBid(BidList bids, LikelyHands likelyHands, Hand hand) {
        for (BoundBidInference i : inferences) {
            if (i.ctx.bids.bids.size() == bids.bids.size() + 1 && i.ctx.bids.exceptLast().equals(bids)) {
                IBoundInference newInference = i.bind(likelyHands);
                if (newInference.matches(hand)) {
                    return i.ctx.bids.getLastBid();
                }
            }
        }
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
        IBoundInference result = ConstBoundInference.create(false);
        IBoundInference negative = ConstBoundInference.create(true);
        for (BoundBidInference i : inferences) {
            if (i.ctx.bids.bids.size() == bids.bids.size() && i.ctx.bids.exceptLast().equals(bids.exceptLast())) {
                IBoundInference newInference = i.bind(likelyHands);
                if (i.ctx.bids.equals(bids)) {
                    result = OrBoundInference.create(AndBoundInference.create(newInference, negative), result);
                }
                negative = AndBoundInference.create(negative, NotBoundInference.create(newInference));
            }
        }
        if (bids.bids.size() > 0) {
            Bid lastBid = bids.getLastBid();
            if (lastBid == Bid.P) {
                result = OrBoundInference.create(negative, result);
            }
        }
        return result;
    }
}
