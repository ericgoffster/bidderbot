package bbidder.generalities;

import java.util.OptionalInt;

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
    private final int minLength;

    public BidSuitGenerality(Symbol symbol, int pos, int minLength) {
        super();
        this.symbol = symbol;
        this.pos = pos;
        this.minLength = minLength;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new BidSuitGenerality(e.getSymbol(), pos, minLength).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolvedStrain();
        Players rotate = players.rotate(pos);
        OptionalInt minLenInSuit = rotate.me.infSummary.minLenInSuit(suit);
        if (!minLenInSuit.isPresent()) {
            return false;
        }
        return rotate.iBidSuit(suit) && minLenInSuit.getAsInt() >= minLength;
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
        return getPosName()+ "_" + NAME + " " + symbol + " at_least " + minLength;
    }

}
