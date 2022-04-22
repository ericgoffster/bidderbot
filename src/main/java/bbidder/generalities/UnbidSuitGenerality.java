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

public class UnbidSuitGenerality implements Generality {
    public final Symbol symbol;


    public UnbidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        for (Entry<Integer, BiddingContext> e : bc.resolveSymbols(symbol).entrySet()) {
            result.add(e.getValue().withGeneralityAdded(new UnbidSuitGenerality(new ConstSymbol(e.getKey()))));                
        }
        return result;
    }

    @Override
    public boolean matches(Players players, BidList bidList) {
        int suit = symbol.getResolved();
        int bidSuits = players.me.infSummary.getBidSuits() |  players.partner.infSummary.getBidSuits() | players.lho.infSummary.getBidSuits() | players.rho.infSummary.getBidSuits();
        if ((bidSuits & (1 << suit)) != 0) {
            return false;
        }
        return true;
    }

    public static UnbidSuitGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("unbid_suit")) {
            Symbol symbol = SymbolParser.parseSymbol(parts[1]);
            if (symbol != null) {
                return new UnbidSuitGenerality(symbol);
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "unbid_suit " + symbol;
    }

}
