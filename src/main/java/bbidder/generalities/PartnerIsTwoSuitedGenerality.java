package bbidder.generalities;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class PartnerIsTwoSuitedGenerality extends Generality {
    public static final String NAME = "partner_is_two_suited";
    private final Symbol symbol1;
    private final Symbol symbol2;

    public PartnerIsTwoSuitedGenerality(Symbol symbol1, Symbol symbol2) {
        super();
        this.symbol1 = symbol1;
        this.symbol2 = symbol2;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
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

    @Override
    public String toString() {
        return NAME + " " + symbol1 + " " + symbol2;
    }

}
