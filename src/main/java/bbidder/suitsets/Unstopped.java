package bbidder.suitsets;

import java.util.OptionalInt;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;

public final class Unstopped extends SuitSet {
    public Unstopped() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        OptionalInt partnerSuits = players.partner.infSummary.getBidSuits();
        OptionalInt meSuits = players.me.infSummary.getBidSuits();
        if (!partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        int allBid = partnerSuits.getAsInt() | meSuits.getAsInt();
        for (int i = 0; i < 4; i++) {
            if (players.me.infSummary.stoppers.stopperIn(i) || players.partner.infSummary.stoppers.stopperIn(i)
                    || players.me.infSummary.partialStoppers.stopperIn(i) || players.partner.infSummary.partialStoppers.stopperIn(i)) {
                allBid |= 1 << i;
            }
        }
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
        return "unstopped";
    }
}