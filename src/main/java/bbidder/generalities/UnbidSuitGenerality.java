package bbidder.generalities;

import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.utils.SplitUtil;

public final class UnbidSuitGenerality extends Generality {
    private final Symbol symbol;

    public UnbidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new UnbidSuitGenerality(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolvedStrain();
        if (players.me.infSummary.getBidSuits().filter(s -> (s & (1 << suit)) != 0).isPresent()) {
            return false;
        }
        if (players.partner.infSummary.getBidSuits().filter(s -> (s & (1 << suit)) != 0).isPresent()) {
            return false;
        }
        if (players.lho.infSummary.getBidSuits().filter(s -> (s & (1 << suit)) != 0).isPresent()) {
            return false;
        }
        if (players.rho.infSummary.getBidSuits().filter(s -> (s & (1 << suit)) != 0).isPresent()) {
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
