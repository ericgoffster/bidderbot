package bbidder.generalities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import bbidder.BidList;
import bbidder.BiddingContext;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SplitUtil;
import bbidder.Symbol;
import bbidder.SymbolParser;

public class FitEstablished implements Generality {
    public final Symbol symbol;

    public FitEstablished(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext bc) {
        List<BiddingContext> result = new ArrayList<>();
        for (Entry<Symbol, BiddingContext> e : bc.resolveSymbols(symbol).entrySet()) {
            result.add(e.getValue().withGeneralityAdded(new FitEstablished(e.getKey())));
        }
        return result;
    }

    @Override
    public boolean matches(Players players, BidList bidList) {
        int s = symbol.getResolved();
        return players.partner.infSummary.minLenInSuit(s) + players.me.infSummary.minLenInSuit(s) >= 8;
    }

    public static FitEstablished valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("fit_established")) {
            Symbol sym = SymbolParser.parseSymbol(parts[1]);
            if (sym != null) {
                return new FitEstablished(sym);
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FitEstablished other = (FitEstablished) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return "fit_established " + symbol;
    }

}
