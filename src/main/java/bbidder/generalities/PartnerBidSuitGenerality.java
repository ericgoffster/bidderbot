package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bbidder.BidList;
import bbidder.BidPatternContext;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SplitUtil;
import bbidder.Symbol;
import bbidder.SymbolContext;
import bbidder.SymbolParser;

public class PartnerBidSuitGenerality implements Generality {
    public final Symbol symbol;


    public PartnerBidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<BidPatternContext> resolveSymbols(BidPatternContext bc) {
        List<BidPatternContext> result = new ArrayList<>();
        for (Entry<Symbol, Map<String, Integer>> e : SymbolContext.resolveSymbols(bc.getSuits(), symbol).entrySet()) {
            result.add(new BidPatternContext(bc.bid.withGeneralityAdded(new PartnerBidSuitGenerality(e.getKey())), e.getValue()));
        }
        return result;
    }

    @Override
    public boolean matches(Players players, BidList bidList) {
        int suit = symbol.getResolved();
        if (players.me.infSummary.minLenInSuit(suit) + players.partner.infSummary.minLenInSuit(suit) >= 8) {
            return false;
        }
        if ((players.partner.infSummary.getBidSuits() & (1 << suit)) == 0) {
            return false;
        }
        return true;
    }

    public static PartnerBidSuitGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partner_bid_suit")) {
            Symbol symbol = SymbolParser.parseSymbol(parts[1]);
            if (symbol != null) {
                return new PartnerBidSuitGenerality(symbol);
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "partner_bid_suit " + symbol;
    }

}
