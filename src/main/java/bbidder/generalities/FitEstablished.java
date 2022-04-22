package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import bbidder.BiddingContext;
import bbidder.Generality;
import bbidder.Strain;

public class FitEstablished implements Generality {
    public final String symbol;
    
    public FitEstablished(String symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        for (Entry<Integer, BiddingContext> e : bc.resolveSymbols(symbol).entrySet()) {
            result.add(bc.withGeneralityAdded(new FitEstablished(Strain.getName(e.getKey()))));
         }
        return result;
    }

}
