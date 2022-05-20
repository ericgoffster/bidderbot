package bbidder.suitsets;

import java.util.OptionalInt;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class TheirSuits extends SuitSet {
    public TheirSuits() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        OptionalInt lhoSuits = players.lho.infSummary.getBidSuits();
        OptionalInt rhoSuits = players.rho.infSummary.getBidSuits();
        if (!lhoSuits.isPresent() || !rhoSuits.isPresent()) {
            return 0;
        }
        int theirSuits = lhoSuits.getAsInt() | rhoSuits.getAsInt();
        return (short) (0xf ^ theirSuits);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return "theirs";
    }
}