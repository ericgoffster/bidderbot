package bbidder.generalities;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class IsTwoSuitedGenerality extends Generality {
    public static final String NAME = "two_suited";
    private final Symbol symbol1;
    private final Symbol symbol2;
    private final int pos;

    public IsTwoSuitedGenerality(Symbol symbol1, Symbol symbol2, int pos) {
        super();
        this.symbol1 = symbol1;
        this.symbol2 = symbol2;
        this.pos = pos;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol1.resolveSuits(suitTable)
                .flatMap(e1 -> symbol2.resolveSuits(e1.suitTable)
                        .map(e2 -> new IsTwoSuitedGenerality(e1.getSymbol(), e2.getSymbol(), pos).new Context(e2.suitTable)));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int s1 = symbol1.getResolvedStrain();
        int s2 = symbol2.getResolvedStrain();
        return players.rotate(pos).iBidSuit(s1) && players.rotate(pos).iBidSuit(s2);
    }
    
    public String getPosName() {
        switch(pos) {
        case 0: return "i_am";
        case 1: return "rho_is";
        case 2: return "partner_is";
        case 3: return "lho_is";
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return getPosName() + "_" + NAME + " " + symbol1 + " " + symbol2;
    }

}
