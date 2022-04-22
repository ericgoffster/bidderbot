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

public final class UnbidSuitGenerality implements Generality {
    public final Symbol symbol;


    public UnbidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<GeneralityContext> resolveSymbols(SymbolTable bc) {
        List<GeneralityContext> result = new ArrayList<>();
        for (Entry<Symbol, SymbolTable> e : SymbolContext.resolveSymbols(bc, symbol).entrySet()) {
            result.add(new GeneralityContext(new UnbidSuitGenerality(e.getKey()), e.getValue()));
        }
        return result;
    }

    @Override
    public boolean matches(Players players, Auction bidList) {
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
