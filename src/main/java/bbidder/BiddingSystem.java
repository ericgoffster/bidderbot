package bbidder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
}
