package bbidder.suitsets;

import java.util.OptionalInt;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.utils.MyStream;

public final class DeniedStoppers extends SuitSet {
    public DeniedStoppers() {
        super();
    }

    @Override
    public short evaluate(Players players) {
        OptionalInt partnerSuits = players.partner.infSummary.getBidSuits();
        OptionalInt meSuits = players.me.infSummary.getBidSuits();
        if (!partnerSuits.isPresent() || !meSuits.isPresent()) {
            return 0;
        }
        int bidSuits = partnerSuits.getAsInt() | meSuits.getAsInt();
        int unstopped = 0;
        for (int i = 0; i < 4; i++) {
            if (players.partner.infSummary.stoppers.noStopperIn(i) && players.partner.infSummary.partialStoppers.noStopperIn(i)
                    && !players.me.infSummary.partialStoppers.stopperIn(i) && !players.me.infSummary.stoppers.stopperIn(i)
                    && ((1 << i) & bidSuits) == 0) {
                unstopped |= 1 << i;
            }
        }
        return (short) unstopped;
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
        return "denied";
    }
}