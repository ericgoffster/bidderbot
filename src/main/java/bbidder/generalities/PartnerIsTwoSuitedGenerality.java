package bbidder.generalities;

import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.utils.SplitUtil;

public final class PartnerIsTwoSuitedGenerality extends Generality {
    private final Symbol symbol1;
    private final Symbol symbol2;

    public PartnerIsTwoSuitedGenerality(Symbol symbol1, Symbol symbol2) {
        super();
        this.symbol1 = symbol1;
        this.symbol2 = symbol2;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol1.resolveSuits(suitTable)
                .flatMap(e1 -> symbol2.resolveSuits(e1.suitTable)
                        .map(e2 -> new PartnerIsTwoSuitedGenerality(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int s1 = symbol1.getResolvedStrain();
        int s2 = symbol2.getResolvedStrain();
        return players.flip().iBidSuit(s1) && players.flip().iBidSuit(s2);
    }

    public static PartnerIsTwoSuitedGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length == 3 && parts[0].equalsIgnoreCase("partner_is_two_suited")) {
            Symbol sym1 = SymbolParser.parseSymbol(parts[1]);
            Symbol sym2 = SymbolParser.parseSymbol(parts[2]);
            if (sym1 != null && sym2 != null) {
                return new PartnerIsTwoSuitedGenerality(sym1, sym2);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "partner_is_two_suited " + symbol1 + " " + symbol2;
    }

}
