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

public class TwoSuitedGenerality implements Generality {
    public final Symbol longer;
    public final Symbol shorter;


    public TwoSuitedGenerality(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        for (Entry<Symbol, BiddingContext> e : bc.resolveSymbols(longer).entrySet()) {
            for (Entry<Symbol, BiddingContext> e2 : e.getValue().resolveSymbols(shorter).entrySet()) {
                result.add(e2.getValue().withGeneralityAdded(new TwoSuitedGenerality(e.getKey(), e2.getKey())));                
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
