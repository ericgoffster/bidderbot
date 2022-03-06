package bbidder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import bbidder.inferences.AndBoundInference;
import bbidder.inferences.ConstBoundInference;
import bbidder.inferences.NotBoundInference;
import bbidder.inferences.OrBoundInference;

public class BiddingSystem {
    public final List<BidInference> inferences;

    public BiddingSystem(List<BidInference> inferences) {
        super();
        this.inferences = inferences;
    }

    public static BiddingSystem load(InputStream is) throws IOException {
        List<BidInference> inferences = new ArrayList<>();
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
                if (!ln.equals("")) {
                    inferences.add(BidInference.valueOf(reg, ln));
                }
            }
        }
        return new BiddingSystem(inferences);
    }

    public void spew(OutputStream os) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            for (BidInference bi : inferences) {
                bw.write(bi.toString());
            }
        }
    }

    /**
     * Retrieves the inference from a list of bids according to the system.
     * 
     * @param bids
     *            The list of bids.
     * @return The inference
     */
    public IBoundInference getInference(BidList bids) {
        Bid lastBid = bids.bids.get(bids.bids.size() - 1);
        BidList exceptLast = new BidList(bids.bids.subList(0, bids.bids.size() - 1));
        IBoundInference result = ConstBoundInference.create(false);
        IBoundInference negative = ConstBoundInference.create(true);
        for (BidInference i : inferences) {
            BidContext bc = new BidContext(exceptLast, i.bids);
            for (Bid b : bc.getMatchingBids()) {
                BidContext bc2 = bc.clone();
                bc2.addWe(b);
                SimpleContext context = new SimpleContext(s -> bc2.getSuit(s));
                IBoundInference newInference = i.inferences.bind(context);
                if (b == lastBid) {
                    result = OrBoundInference.create(AndBoundInference.create(newInference, negative), result);
                }
                negative = AndBoundInference.create(negative, NotBoundInference.create(newInference));
            }
        }
        return result;
    }
}
