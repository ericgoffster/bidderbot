package bbidder.generalities;

import java.util.OptionalInt;
import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.utils.BitUtil;

public final class WeAreThreeSuited extends Generality {
    public static final String NAME = "we_are_three_suited";

    public WeAreThreeSuited() {
        super();
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return Stream.of(new WeAreThreeSuited().new Context(suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        OptionalInt mySuits = players.me.infSummary.getBidSuits();
        OptionalInt pSuits = players.partner.infSummary.getBidSuits();
        if (mySuits.isEmpty() || pSuits.isEmpty()) {
            return false;
        }
        int suits = mySuits.getAsInt() | pSuits.getAsInt();
        return BitUtil.size(suits) == 3;
    }

    @Override
    public String toString() {
        return NAME;
    }

}
