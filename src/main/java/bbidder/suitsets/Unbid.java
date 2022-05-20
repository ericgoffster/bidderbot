package bbidder.suitsets;

import java.util.OptionalInt;

import bbidder.Players;
import bbidder.Position;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class Unbid extends SuitSet {
    public Unbid() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        OptionalInt lhoSuits = players.getPlayer(Position.LHO).infSummary.getBidSuits();
        OptionalInt rhoSuits = players.rho.infSummary.getBidSuits();
        OptionalInt partnerSuits = players.getPlayer(Position.PARTNER).infSummary.getBidSuits();
        OptionalInt meSuits = players.getPlayer(Position.ME).infSummary.getBidSuits();
        if (!lhoSuits.isPresent() || !rhoSuits.isPresent() || !partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        int allBid = lhoSuits.getAsInt() | rhoSuits.getAsInt() | partnerSuits.getAsInt() | meSuits.getAsInt();
        return (short) (0xf ^ allBid);
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
        return "unbid";
    }
}