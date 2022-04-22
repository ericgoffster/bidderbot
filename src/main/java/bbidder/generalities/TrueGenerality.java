package bbidder.generalities;

import java.util.List;

import bbidder.BidList;
import bbidder.BidPatternContext;
import bbidder.Generality;
import bbidder.Players;

public class TrueGenerality implements Generality {
    public static TrueGenerality T = new TrueGenerality();

    private TrueGenerality() {
    }

    @Override
    public List<BidPatternContext> resolveSymbols(BidPatternContext bc) {
        return List.of(new BidPatternContext(bc.bid.withGeneralityAdded(this), bc.getSuits()));
    }

    @Override
    public boolean matches(Players players, BidList bidList) {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == T;
    }
}
