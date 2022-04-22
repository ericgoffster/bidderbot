package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;

import bbidder.BidList;
import bbidder.BiddingContext;
import bbidder.Generality;
import bbidder.Players;

public class AndGenerality implements Generality {
    public final Generality g1;
    public final Generality g2;

    public AndGenerality(Generality g1, Generality g2) {
        super();
        this.g1 = g1;
        this.g2 = g2;
    }
    
    public static Generality create(Generality g1, Generality g2) {
        if (g1.equals(TrueGenerality.T)) {
            return g2;
        }
        if (g2.equals(TrueGenerality.T)) {
            return g1;
        }
        return new AndGenerality(g1, g2);
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        for (BiddingContext bc2: g1.resolveSymbols(bc)) {
            result.addAll(g2.resolveSymbols(bc2)) ;
        }
        return result;
    }
    
    @Override
    public boolean matches(Players players, BidList bidList) {
        return g1.matches(players, bidList) && g2.matches(players, bidList);
    }

}
