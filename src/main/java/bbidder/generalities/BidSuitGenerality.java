package bbidder.generalities;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.TaggedAuction;
import bbidder.utils.MyStream;

public final class BidSuitGenerality extends Generality {
    public static final String NAME = "bid";
    private final Symbol symbol;
    private final int position;
    private final SuitLengthRange range;

    public BidSuitGenerality(Symbol symbol, int position, SuitLengthRange range) {
        super();
        this.symbol = symbol;
        this.position = position;
        this.range = range;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new BidSuitGenerality(e.getSymbol(), position, range).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, TaggedAuction bidList) {
        int suit = symbol.getResolvedStrain();
        Players rotate = players.rotate(position);
        OptionalInt minLenInSuit = rotate.me.infSummary.minLenInSuit(suit);
        if (!minLenInSuit.isPresent()) {
            return false;
        }
        return rotate.iBidSuit(suit) && range.contains(minLenInSuit.getAsInt());
    }
    
    public String getPosName() {
        switch(position) {
        case 0: return "i";
        case 1: return "rho";
        case 2: return "partner";
        case 3: return "lho";
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, range, symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidSuitGenerality other = (BidSuitGenerality) obj;
        return position == other.position && Objects.equals(range, other.range) && Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return getPosName()+ "_" + NAME + " " + symbol + " promising " + range;
    }

}
