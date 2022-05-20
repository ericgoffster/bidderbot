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
    private final Symbol longer;
    private final Symbol shorter;

    public PartnerIsTwoSuitedGenerality(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(e1 -> shorter.resolveSuits(e1.suitTable)
                        .map(e2 -> new PartnerIsTwoSuitedGenerality(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int l = longer.getResolvedStrain();
        int s = shorter.getResolvedStrain();
        boolean iBidSuit = players.flip().iBidSuit(l);
        boolean iBidSuit2 = players.flip().iBidSuit(s);
        return iBidSuit && iBidSuit2;
    }

    public static PartnerIsTwoSuitedGenerality valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+", 3);
        if (parts.length == 3 && parts[0].equalsIgnoreCase("partner_is_two_suited")) {
            Symbol longer = SymbolParser.parseSymbol(parts[1]);
            Symbol shorter = SymbolParser.parseSymbol(parts[2]);
            if (longer != null && shorter != null) {
                return new PartnerIsTwoSuitedGenerality(longer, shorter);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "partner_is_two_suited " + longer + " " + shorter;
    }

}
