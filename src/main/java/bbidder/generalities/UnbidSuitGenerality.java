package bbidder.generalities;

import java.util.OptionalInt;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
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
    public boolean test(Players players, Auction bidList) {
        int suit = symbol.getResolvedStrain();
        OptionalInt meSuits = players.me.infSummary.getBidSuits();
        OptionalInt partnerSuits = players.partner.infSummary.getBidSuits();
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

}
