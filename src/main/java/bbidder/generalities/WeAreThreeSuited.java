package bbidder.generalities;

import java.util.OptionalInt;

import bbidder.Generality;
import bbidder.Players;
import bbidder.Position;
import bbidder.SuitTable;
import bbidder.TaggedAuction;
import bbidder.utils.BitUtil;
import bbidder.utils.MyStream;

public final class WeAreThreeSuited extends Generality {
    public static final String NAME = "we_are_three_suited";
    public static final WeAreThreeSuited WE_R_3_SUITED = new WeAreThreeSuited();

    private WeAreThreeSuited() {
        super();
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new WeAreThreeSuited().new Context(suitTable));
    }

    @Override
    public boolean test(Players players, TaggedAuction bidList) {
        Position position = Position.ME;
        OptionalInt mySuits = players.getPlayer(position).infSummary.getBidSuits();
        OptionalInt pSuits = players.getPlayer(position.getOpposite()).infSummary.getBidSuits();
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

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
