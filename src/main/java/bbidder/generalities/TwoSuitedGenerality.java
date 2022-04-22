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

public class TwoSuitedGenerality implements Generality {
    public final Symbol longer;
    public final Symbol shorter;


    public TwoSuitedGenerality(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public List<BidPatternContext> resolveSymbols(BidPatternContext bc) {
        List<BidPatternContext> result = new ArrayList<>();
        for (Entry<Symbol, Map<String, Integer>> e : SymbolContext.resolveSymbols(bc.getSuits(), longer).entrySet()) {
            for (Entry<Symbol, Map<String, Integer>> e2 : SymbolContext.resolveSymbols(e.getValue(), shorter).entrySet()) {
                result.add(new BidPatternContext(bc.bid.withGeneralityAdded(new TwoSuitedGenerality(e.getKey(), e2.getKey())), e.getValue()));
            }
        }
        return result;
    }

    @Override
    public boolean matches(Players players, BidList bidList) {
        int l = longer.getResolved();
        int s = shorter.getResolved();
        if (players.me.infSummary.minLenInSuit(s) + players.partner.infSummary.minLenInSuit(s) >= 8) {
            return false;
        }
        if (players.me.infSummary.minLenInSuit(l) + players.partner.infSummary.minLenInSuit(l) >= 8) {
            return false;
        }
        if ((players.me.infSummary.getBidSuits() & (1 << l)) == 0) {
            return false;
        }
        if ((players.me.infSummary.getBidSuits() & (1 << s)) == 0) {
            return false;
        }
        return true;
    }

    public static TwoSuitedGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length == 3 && parts[0].equalsIgnoreCase("i_am_two_suited")) {
            Symbol longer = SymbolParser.parseSymbol(parts[1]);
            Symbol shorter = SymbolParser.parseSymbol(parts[2]);
            if (longer != null && shorter != null) {
                return new TwoSuitedGenerality(longer, shorter);
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "i_am_two_suited " + longer + " " + shorter;
    }

}
