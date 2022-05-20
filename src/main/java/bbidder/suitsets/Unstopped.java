package bbidder.suitsets;

import java.util.OptionalInt;

import bbidder.InfSummary;
import bbidder.Players;
import bbidder.Position;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class Unstopped extends SuitSet {
    public Unstopped() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        InfSummary partner = players.getPlayer(Position.PARTNER).infSummary;
        InfSummary me = players.getPlayer(Position.ME).infSummary;
        OptionalInt partnerSuits = partner.getBidSuits();
        OptionalInt meSuits = me.getBidSuits();
        if (!partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        int stopped = partnerSuits.getAsInt() | meSuits.getAsInt();
        for (int i = 0; i < 4; i++) {
            if (me.stoppers.stopperIn(i) || partner.stoppers.stopperIn(i)
                    || me.partialStoppers.stopperIn(i) || partner.partialStoppers.stopperIn(i)) {
                stopped |= 1 << i;
            }
        }
        return (short) (0xf ^ stopped);
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
        return "unstopped";
    }
}