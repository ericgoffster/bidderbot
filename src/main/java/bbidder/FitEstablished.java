package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class FitEstablished implements Generality {
    public final String symbol;
    
    public FitEstablished(String symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        BidPattern b = bc.getInference().bids.getLastBid();
        for (Entry<Integer, BiddingContext> e : bc.resolveSymbols(symbol).entrySet()) {
            Generality g2 = AndGenerality.create(b.generality, new FitEstablished(Strain.getName(e.getKey())));
            BidPattern b2 = b.withGenerality(g2);
            result.add(e.getValue().withLastBidReplaced(b2));
        }
        return result;
    }

}
