package bbidder.generalities;

import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SuitTable;
import bbidder.utils.SplitUtil;

public final class TwoSuitedGenerality extends Generality {
    private final Symbol longer;
    private final Symbol shorter;

    public TwoSuitedGenerality(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        return longer.resolveSymbols(suitTable)
                .flatMap(e1 -> shorter.resolveSymbols(e1.suitTable)
                        .map(e2 -> new TwoSuitedGenerality(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
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
