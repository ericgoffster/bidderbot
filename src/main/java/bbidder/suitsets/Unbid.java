package bbidder.suitsets;

import java.util.OptionalInt;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;

public final class Unbid extends SuitSet {
    public Unbid() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        OptionalInt lhoSuits = players.lho.infSummary.getBidSuits();
        OptionalInt rhoSuits = players.rho.infSummary.getBidSuits();
        OptionalInt partnerSuits = players.partner.infSummary.getBidSuits();
        OptionalInt meSuits = players.me.infSummary.getBidSuits();
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
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return Stream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return "unbid";
    }
}