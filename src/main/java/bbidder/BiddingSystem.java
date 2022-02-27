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
            for(BidInference bi: inferences) {
                bw.write(bi.toString());
            }
        }
    }
    
    public List<BoundInference> getInference(BidList bids) {
        Bid lastBid = bids.bids.get(bids.bids.size() - 1);
        BidList exceptLast = new BidList(bids.bids.subList(0, bids.bids.size() - 1));
        List<BoundInference> result = new ArrayList<>();
        List<BoundInference> negative = new ArrayList<>();
        for(BidInference i: inferences) {
            BidContext bc = new BidContext(exceptLast, i.bids);
            for(Bid b: bc.getBids()) {
                BidContext bc2 = bc.clone();
                bc2.addWe(b);
                SimpleContext context = new SimpleContext(s -> bc2.getSuit(s));
                BoundInference newInference = new BoundInference(i.inferences, context, new ArrayList<>(negative));
                if (b == lastBid) {
                    result.add(newInference);
                }
                negative.add(newInference);
            }
        }
        return result;
    }
}
