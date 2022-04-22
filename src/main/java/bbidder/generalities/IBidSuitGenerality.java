package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.GeneralityContext;
import bbidder.Players;
import bbidder.SplitUtil;
import bbidder.Symbol;
import bbidder.SymbolContext;
import bbidder.SymbolParser;
import bbidder.SymbolTable;

public final class IBidSuitGenerality implements Generality {
    public final Symbol symbol;

    public IBidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<GeneralityContext> resolveSymbols(SymbolTable symbols) {
        List<GeneralityContext> result = new ArrayList<>();
        for (Entry<Symbol, SymbolTable> e : SymbolContext.resolveSymbols(symbols, symbol).entrySet()) {
            result.add(new GeneralityContext(new IBidSuitGenerality(e.getKey()), e.getValue()));
        }
        return result;
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolved();
        int minLenInSuit = players.me.infSummary.minLenInSuit(suit);
        int minLenInSuit2 = players.partner.infSummary.minLenInSuit(suit);
        if (minLenInSuit + minLenInSuit2 >= 8) {
            return false;
        }
        if ((players.me.infSummary.getBidSuits() & (1 << suit)) == 0) {
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
            Symbol symbol = SymbolParser.parseSymbol(parts[1]);
            if (symbol != null) {
                return new IBidSuitGenerality(symbol);
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "i_bid_suit " + symbol;
    }

}
