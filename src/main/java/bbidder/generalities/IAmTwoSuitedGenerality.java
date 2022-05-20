package bbidder.generalities;

import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;

public final class IAmTwoSuitedGenerality extends Generality {
    private final Symbol longer;
    private final Symbol shorter;

    public IAmTwoSuitedGenerality(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(e1 -> shorter.resolveSuits(e1.suitTable)
                        .map(e2 -> new IAmTwoSuitedGenerality(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int l = longer.getResolvedStrain();
        int s = shorter.getResolvedStrain();
        return players.iBidSuit(l) && players.iBidSuit(s);
    }

    @Override
    public String toString() {
        return "i_am_two_suited " + longer + " " + shorter;
    }

}
