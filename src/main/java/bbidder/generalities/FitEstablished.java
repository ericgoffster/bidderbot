package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import bbidder.BidList;
import bbidder.BiddingContext;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SplitUtil;
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
    
    @Override
    public boolean matches(Players players, BidList bidList) {
        int s = Strain.getStrain(symbol);
        return players.partner.infSummary.getSuit(s).lowest() + players.me.infSummary.getSuit(s).lowest() >= 8;
    }
    
    public static FitEstablished valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("fit_established")) {
            if (BiddingContext.isValidSuit(parts[1])) {
                return new FitEstablished(parts[1]);
            }
        }
        return null;
    }

}
