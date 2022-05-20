package bbidder.generalities;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.Generality;
import bbidder.Players;
import bbidder.Position;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.TaggedAuction;
import bbidder.utils.MyStream;

public final class UnbidSuitGenerality extends Generality {
    public static final String NAME = "unbid_suit";
    private final Symbol symbol;

    public UnbidSuitGenerality(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new UnbidSuitGenerality(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, TaggedAuction bidList) {
        int suit = symbol.getResolvedStrain();
        OptionalInt meSuits = players.getPlayer(Position.ME).infSummary.getBidSuits();
        OptionalInt partnerSuits = players.getPlayer(Position.PARTNER).infSummary.getBidSuits();
        OptionalInt lhoSuits = players.lho.infSummary.getBidSuits();
        OptionalInt rhoSuits = players.rho.infSummary.getBidSuits();
        if (!meSuits.isPresent() || !partnerSuits.isPresent() || !lhoSuits.isPresent() || !rhoSuits.isPresent()) {
            return false;
        }
        int combined = meSuits.getAsInt() | partnerSuits.getAsInt() | lhoSuits.getAsInt() | rhoSuits.getAsInt();
        if ((combined & (1 << suit)) != 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return NAME + " " + symbol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(symbol);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UnbidSuitGenerality other = (UnbidSuitGenerality) obj;
        return Objects.equals(symbol, other.symbol);
    }
}
