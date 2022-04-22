package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import bbidder.BidList;
import bbidder.BiddingContext;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SplitUtil;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.symbols.ConstSymbol;

public class IBidSuitGenerality implements Generality {
    public final Symbol longer;


    public IBidSuitGenerality(Symbol longer) {
        super();
        this.longer = longer;
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        for (Entry<Integer, BiddingContext> e : bc.resolveSymbols(longer).entrySet()) {
            result.add(e.getValue().withGeneralityAdded(new IBidSuitGenerality(new ConstSymbol(e.getKey()))));                
        }
        return result;
    }

    @Override
    public boolean matches(Players players, BidList bidList) {
        int l = longer.getResolved();
        if (players.me.infSummary.minLenInSuit(l) + players.partner.infSummary.minLenInSuit(l) >= 8) {
            return false;
        }
        if (players.me.infSummary.avgLenInSuit(l) < 4) {
            return false;
        }
        return true;
    }

    public static IBidSuitGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("i_bid_suit")) {
            Symbol longer = SymbolParser.parseSymbol(parts[1]);
            if (longer != null) {
                return new IBidSuitGenerality(longer);
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "i_bid_suit " + longer;
    }

}
