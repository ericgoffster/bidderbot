package bbidder.generalities;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.Generality;
import bbidder.Player;
import bbidder.Players;
import bbidder.Position;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.TaggedAuction;
import bbidder.utils.MyStream;

public final class BidSuitGenerality extends Generality {
    public static final String NAME = "bid";
    private final Symbol symbol;
    private final Position position;
    private final SuitLengthRange range;

    public BidSuitGenerality(Symbol symbol, Position position, SuitLengthRange range) {
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
        Player me = players.getPlayer(position);
        OptionalInt minLenInSuit = me.infSummary.minLenInSuit(suit);
        if (!minLenInSuit.isPresent()) {
            return false;
        }
        return players.bidSuit(position, suit) && range.contains(minLenInSuit.getAsInt());
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
        return position + "_" + NAME + " " + symbol + " promising " + range;
    }

}
