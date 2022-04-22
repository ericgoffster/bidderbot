package bbidder.generalities;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.utils.ListUtil;
import bbidder.utils.SplitUtil;

public final class FitEstablished extends Generality {
    private final Symbol symbol;

    public FitEstablished(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public List<Context> resolveSymbols(SymbolTable symbols) {
        return ListUtil.map(symbol.resolveSymbols(symbols).collect(Collectors.toList()), e -> new FitEstablished(e.getSymbol()).new Context(e.symbols));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int s = symbol.getResolved();
        return players.partner.infSummary.minLenInSuit(s) + players.me.infSummary.minLenInSuit(s) >= 8;
    }

    public static FitEstablished valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("fit_established")) {
            Symbol symbol = SymbolParser.parseSymbol(parts[1]);
            if (symbol != null) {
                return new FitEstablished(symbol);
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
