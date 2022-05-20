package bbidder.generalities;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class BidSuitGenerality extends Generality {
    public static final String NAME = "bid_suit";
    private final Symbol symbol;
    private final int pos;

    public BidSuitGenerality(Symbol symbol, int pos) {
        super();
        this.symbol = symbol;
        this.pos = pos;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new BidSuitGenerality(e.getSymbol(), pos).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolvedStrain();
        return players.rotate(pos).iBidSuit(suit);
    }
    
    public String getPosName() {
        switch(pos) {
        case 0: return "i";
        case 1: return "rho";
        case 2: return "partner";
        case 3: return "lho";
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return getPosName()+ "_" + NAME + " " + symbol;
    }

}
