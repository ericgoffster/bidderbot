package bbidder.generalities;

import java.util.List;

import bbidder.BiddingContext;
import bbidder.Generality;

public class TrueGenerality implements Generality {
    public static TrueGenerality T = new TrueGenerality();
    private TrueGenerality() {
    }
    
    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        return List.of(bc.withGeneralityAdded(this));
    }
}
